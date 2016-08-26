package com.strateratech.dhs.peerrate.web.service;

import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.saml.StrateratechSamlUserDetailsService;

/**
 * 
 * Security Service for handling security concerns agnostic of impelemenation
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Service("securityService")
public class SecurityService {
    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
    public static final String SYSTEM_USERNAME = "peerrateweb_svc";
	public static final String SYSTEM_USERNAME_MASK = "SYSTEM";
	
	public static final String AUTHENTICATED_USERNAME_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Inject 
    private StrateratechSamlUserDetailsService strateratechSamlUserDetailsService;

    @Value("${first_name_field_key:firstName}")
    private String firstNameFieldKey;

    @Value("${last_name_field_key:lastName}")
    private String lastNameFieldKey;

    @Value("${email_field_key:email}")
    private String emailFieldKey;

    /**
     * Method to map SAML Token to Contract Model.
     * 
     * @param springAuthToken
     * @return RestAuthenticationToken
     */
    public RestAuthenticationToken mapSamlTokenToContractModel(Authentication springAuthToken) {
        RestAuthenticationToken token = null;
        if (springAuthToken != null) {
            token = new RestAuthenticationToken();
            token.setUsername(springAuthToken.getName());
            try {
            	if (springAuthToken !=null && springAuthToken.getPrincipal() != null 
            			&& SecurityService.isValidEmail(springAuthToken.getPrincipal().toString())) {
            		token.setEmail(springAuthToken.getPrincipal().toString());
            	}
                
            } catch (Exception e) {
                log.warn("Could not map Email {} ", springAuthToken.getPrincipal(), e);
            }
            
            if (SAMLCredential.class.isAssignableFrom(springAuthToken.getCredentials().getClass())) {

                try {
                    SAMLCredential creds = (SAMLCredential) springAuthToken.getCredentials();
                    log.debug("SAMLCredentials nameId={} relayState={} " + " remoteEntityId={} localEntityId={}",
                            creds.getNameID(), creds.getRelayState(), creds.getRemoteEntityID(),
                            creds.getLocalEntityID());

                    for (Attribute attr : creds.getAttributes()) {
                        log.debug("Discovered SAML Attribute [{}={}]", attr.getName(), attr.getAttributeValues());
                    }
                    try {
                        String firstName = creds.getAttributeAsString(firstNameFieldKey);
                        token.setFirstName(firstName);
                    } catch (Exception e1) {
                        log.error("unable to set first name from field ({})", firstNameFieldKey, e1);
                    }

                    try {
                        String lastName = creds.getAttributeAsString(lastNameFieldKey);
                        token.setLastName(lastName);
                    } catch (Exception e1) {
                        log.error("unable to set last name from field ({})", lastNameFieldKey, e1);
                    }
                    try {
                    	if (StringUtils.isBlank(token.getEmail())) {
                    		String email = creds.getAttributeAsString(emailFieldKey);
                    		token.setEmail(email);
                    	}
                    } catch (Exception e1) {
                        log.error("unable to set last name from field ({})", lastNameFieldKey, e1);
                    }
                    
                    token.setTenant(StrateratechSamlUserDetailsService.getTenant(creds.getAuthenticationAssertion()));
                    
                } catch (Exception e) {
                    log.error("Could not serialize credentials", e);
                }

            }

            log.debug("Checking authorities");
            if (springAuthToken.getAuthorities() != null) {
            	log.info("authorities are not null");
                for (GrantedAuthority a : springAuthToken.getAuthorities()) {
                	log.info("Identified authority {}", a.getAuthority());
                    token.getAuthorities().add(a.getAuthority());
                }
            }
        }
        return token;
    }
    

    /**
     * Method to get First Name Key
     * 
     * @return String firstNameFieldKey
     */
    public String getFirstNameFieldKey() {
        return firstNameFieldKey;
    }

    /**
     * Method to get Last Name Key
     * 
     * @return String lastNameFieldKey
     */
    public String getLastNameFieldKey() {
        return lastNameFieldKey;
    }

    /**
     * Method to get Email Key
     * 
     * @return String emailFieldKey
     */
    public String getEmailFieldKey() {
        return emailFieldKey;
    }
    /**
     * Validates email against regex to verify it is properly formatted
     * @param val
     * @return
     * @since Jun 8, 2016
     */
    public static boolean isValidEmail(String val) {
    	return val != null && Pattern.matches(AUTHENTICATED_USERNAME_REGEX, val);
    }
    /**
     * Replaces returns masked string as SYSTEM_USERNAME_MASK if supplied param is not valid email
     * @param val
     * @return
     * @since Jun 8, 2016
     */
    public static String maskIFNotValidEmail(String val) {
    	if (!isValidEmail(val)) {
    		val = SYSTEM_USERNAME_MASK;
    	}
    	return val;
    }

   
}
