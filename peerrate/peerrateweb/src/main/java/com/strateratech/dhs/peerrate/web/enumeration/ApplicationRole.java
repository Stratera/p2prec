package com.strateratech.dhs.peerrate.web.enumeration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.strateratech.dhs.peerrate.web.utils.TokenUtils;
/**
 * This is the enum which maps course grained application roles to fine grained permissions.  The permissions
 * here are the defaults.  They can be overridden at run time by the presence of a classpath properties 
 * file configuration (role_permission_mapping.properties) that overrides the permissions associated with a given role.  This allows us to have 
 * different permission sets in different environments. 
 * the format for the properties file is 
 * 
 * ApplicationRole_enum_name = comma,separated,list,of,fine,grained,permissions
 * 
 * @author 2020
 * @date Apr 26, 2016
 * @version 
 *
 */
public enum ApplicationRole {
	/**
	 * This is the lowest level role in the app.  If a user has no roles in saml assertions,
	 * he is assigned this role so that all our security logic will work
	 */
	EMPLOYEE(ApplicationPermission.NAVIGATOR_LIST, 
			ApplicationPermission.NAVIGATOR_SEARCH, 
			ApplicationPermission.NAVIGATOR_SIMPLE_EDIT),
	/**
	 * Classifiers have different permission sets
	 */
	PROJECTCOORDINATOR(ApplicationPermission.NAVIGATOR_LIST, 
			ApplicationPermission.NAVIGATOR_SEARCH, 
			ApplicationPermission.NAVIGATOR_SIMPLE_EDIT,
			ApplicationPermission.PROPOSAL_LIST,
			ApplicationPermission.PROPOSAL_SEARCH,
			ApplicationPermission.PROPOSAL_CREATE,
			ApplicationPermission.PROPOSAL_UPDATE),
	/**
	 * There are no use cases defining admin features yet
	 */
	ADMIN();
	
	private static final String PREFIX ="ROLE_";
			
	private List<ApplicationPermission> permissions;
	
	/**
	 * private constructor
	 * @param applicationPermissions
	 */
	private ApplicationRole(ApplicationPermission... applicationPermissions) {
		List<ApplicationPermission> myPerms= new ArrayList<ApplicationPermission>();
		if (applicationPermissions != null) {
			for (ApplicationPermission perm : applicationPermissions) {
				myPerms.add(perm);
			}
		}
		this.permissions=myPerms;
	}
	
	/**
	 * Constructs gets a Role by Role name.  Note: this also check is the Role is preceeded by "ROLE_"
	 * @param roleName
	 * @return
	 * @since Apr 26, 2016
	 */
	public static ApplicationRole byRoleName(String roleName) {
		roleName = roleName.replaceAll(TokenUtils.WHITESPACE_REGEX, StringUtils.EMPTY);
		ApplicationRole ret= null;
		if (!StringUtils.isBlank(roleName)) {
			roleName= roleName.toUpperCase().trim();
			for (ApplicationRole role: ApplicationRole.values()) {
				
				if (role.name().equalsIgnoreCase(roleName) || role.name().equalsIgnoreCase(PREFIX+roleName)) {
					ret=role;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * get List of permissions (fine grained granted authorities associated with roles)
	 * @return
	 * @since Apr 26, 2016
	 */
	public List<ApplicationPermission> getPermissions() {
		return permissions;
	}
	
	
}
