package com.strateratech.dhs.peerrate.web.security;

import javax.inject.Inject;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.web.service.SecurityService;

import gov.uspto.pe2e.cpc.ipc.rest.contract.saml.RestAuthenticationToken;

/**
 * Spring service for handling the audit fields (createTs, createUsername,
 * updateTs, updateUsername). If these fields are null, spring data handles
 * setting them to the appropriate values based on persistence life cycle and
 * annotations on the fields
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version 1.1
 */
@Service("cpcAuditAware")
public class StrateratechAuditAware implements AuditorAware<String> {
	
    @Inject
    private SecurityService securityService;

    /**
     * Pulls the string representation of the user (username, userid or email)
     * from the security context per spring's recommended practices
     * 
     * For this particular implementation, we are trying to pull email!
     */
    @Override
    public String getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
        	// This assumes  that if the user is not authenticated in the given context, it must be running 
        	// as a background task.  This also protects the code for when authentication is disabled completely
        	// as we do for load testing in FQT
            return SecurityService.SYSTEM_USERNAME;
        }

        RestAuthenticationToken token = securityService.mapSamlTokenToContractModel(authentication);
        return token.getEmail();
    }
}