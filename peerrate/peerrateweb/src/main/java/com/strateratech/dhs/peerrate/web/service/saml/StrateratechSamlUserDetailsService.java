package com.strateratech.dhs.peerrate.web.service.saml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;

import com.strateratech.dhs.peerrate.web.enumeration.ApplicationPermission;
import com.strateratech.dhs.peerrate.web.enumeration.ApplicationRole;
import com.strateratech.dhs.peerrate.web.security.RestGrantedAuthority;
import com.strateratech.dhs.peerrate.web.utils.TokenUtils;

/**
 * Loads a user and sets permissions for a user
 * 
 * @author 2020
 * @date Mar 10, 2016
 * @version
 *
 */
public class StrateratechSamlUserDetailsService implements org.springframework.security.saml.userdetails.SAMLUserDetailsService {
    public static final String DUMMY_PASSWORD = "<PASSWORD HIDDEN>";
    public static final String ROLE_ATTR_NAME = "role";
    private static final Logger log = LoggerFactory.getLogger(StrateratechSamlUserDetailsService.class);
    private static final String ROLE_MAPPER_PROPERTIES = "role_permission_mapping.properties";
    private HashMap<ApplicationRole, List<ApplicationPermission>> rolePermissionMap;

    private List<String> defaultRoles;

    /**
     * Default constructor used by spring
     */
    public StrateratechSamlUserDetailsService() {
        super();
        rolePermissionMap = new HashMap<>();
        loadDefaultRolePermissionMap();
        overrideRolePermissionMapWithConfig(ROLE_MAPPER_PROPERTIES);
    }

    /**
     * Specialized connstructor used only by the unit tests to allow us to test
     * the overriding of role/permission mappings per environment.
     * 
     * @param rolePermissionMappingFilename
     */
    public StrateratechSamlUserDetailsService(String rolePermissionMappingFilename) {
        super();
        rolePermissionMap = new HashMap<>();
        loadDefaultRolePermissionMap();
        overrideRolePermissionMapWithConfig(rolePermissionMappingFilename);
    }

    /**
     * uses a file to override role/permission mappings in case permissions to
     * use features are enabled/disabled in a particular Env
     * 
     * @param filename
     */
    private void overrideRolePermissionMapWithConfig(String filename) {

        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            if (is != null && is.available() > 0) {
                Properties props = new Properties();
                props.load(is);
                if (!props.isEmpty()) {
                    for (Map.Entry<Object, Object> e : props.entrySet()) {
                        ApplicationRole key = ApplicationRole.valueOf((String) e.getKey());
                        if (key != null) {
                            List<ApplicationPermission> permissions = new ArrayList<>();
                            String[] values = ((String) e.getValue()).split(TokenUtils.TOKEN_SPLIT_REGEX);
                            for (String permString : values) {
                                if (!StringUtils.isBlank(permString)) {
                                    ApplicationPermission perm = ApplicationPermission.valueOf(permString);
                                    if (perm != null) {
                                        permissions.add(perm);
                                    }
                                }
                            }
                            rolePermissionMap.put(key, permissions);
                        }
                    }
                } else {
                    log.debug("No properties found in role mapper config.  Continuing to use defaults");
                }
            } else {
                log.debug("InputStream is null or empty.  Using defaults");
            }
        } catch (Exception e) {
            log.warn("Something bad happened while trying to configure role/permission mapping overrides", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Loads the default role/permission mapping prior to running override
     * (Convention over configuration)
     */
    private void loadDefaultRolePermissionMap() {
        for (ApplicationRole role : ApplicationRole.values()) {
            log.debug("Configuring default permissions for {} permissions={}", role.name(), role.getPermissions());
            rolePermissionMap.put(role, role.getPermissions());
        }

    }

    /**
     * TODO: the cyclomatic complexity of this method is just too high. We need
     * to Fix it!
     * 
     * loadUserBySAML is a spring service method. it never gets called unless
     * credential is non-null
     * 
     * @since Mar 09, 2016
     */
    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String username = credential.getNameID().getValue();
        Assertion mya = credential.getAuthenticationAssertion();

        String tenant = getTenant(mya);
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        List<AttributeStatement> statements = mya.getAttributeStatements();
        if (statements != null && statements.size() > 0) {
            for (AttributeStatement stmt : statements) {
                if (stmt != null && stmt.hasChildren()) {
                    for (Attribute attr : stmt.getAttributes()) {

                        if (attr != null && attr.hasChildren() && attr.getName().equalsIgnoreCase(ROLE_ATTR_NAME)) {
                            List<XMLObject> xmlAttrValues = attr.getAttributeValues();
                            if (xmlAttrValues != null) {
                                for (XMLObject obj : xmlAttrValues) {
                                    if (XSString.class.isAssignableFrom(obj.getClass())) {
                                        if (ApplicationRole.byRoleName(((XSString) obj).getValue()) != null) {
                                            addPermissions(grantedAuthorities, tenant,
                                                    ApplicationRole.byRoleName(((XSString) obj).getValue()));
                                        }
                                    } else if (XSAny.class.isAssignableFrom(obj.getClass())) {
                                        if (ApplicationRole.byRoleName(((XSAny) obj).getTextContent()) != null) {
                                            addPermissions(grantedAuthorities, tenant,
                                                    ApplicationRole.byRoleName(((XSAny) obj).getTextContent()));
                                        }
                                    } else {
                                        log.debug("attr name: {} class:{}", attr.getElementQName().getLocalPart(),
                                                obj.getClass().getCanonicalName());
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        // default granted authorities if empty
        if (grantedAuthorities.isEmpty()) {
            for (String role : defaultRoles) {
                addPermissions(grantedAuthorities, getTenant(mya), ApplicationRole.byRoleName(role));
            }

        }
        log.debug("{} : {} : {}", username, DUMMY_PASSWORD, grantedAuthorities);
        return new User(username, DUMMY_PASSWORD, grantedAuthorities);
    }


    /**
     * This creates the role object given the mappings allowed by this instance
     * 
     * @param gas
     * @param org
     * @param role
     * @since Mar 10, 2016
     */
    private void addPermissions(Collection<GrantedAuthority> gas, String org, ApplicationRole role) {
        if (rolePermissionMap != null) {
            for (ApplicationPermission permission : rolePermissionMap.get(role)) {
                gas.add(new RestGrantedAuthority(org, role, permission));
            }
        }
    }

    /**
     * Setter Method
     * 
     * @param defaultRoles
     * @since Mar 10, 2016
     */
    public void setDefaultRoles(List<String> defaultRoles) {

        this.defaultRoles = defaultRoles;
    }

    /**
     * protected getter
     * 
     * @return
     */
    protected Map<ApplicationRole, List<ApplicationPermission>> getRolePermissionMappingConfig() {
        return this.rolePermissionMap;
    }

    
    /**
     * Returns a string representation of the tenant.  Also protects from NPE by checking along the way.
     * @param assertion
     * @return  May return null if assertion, issuer or value of issuer is null
     * @since Aug 17, 2016
     */
    public static String getTenant(Assertion assertion) {
        String tenant = null;
        if (assertion != null && assertion.getIssuer() != null) {
            tenant =  assertion.getIssuer().getValue();
        }
        return tenant;
    }

}