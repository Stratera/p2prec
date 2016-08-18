package com.strateratech.dhs.peerrate.web.service.saml;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.SAMLDiscovery;

import com.strateratech.dhs.peerrate.web.service.IdpDiscoveryService;
import com.strateratech.dhs.peerrate.web.utils.RestUtils;

/**
 * 
 * US5079: SAML Prototype - External Domain
 * 
 * Class UsptoSamlDiscovery to support IdpDiscovery Service
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public class SamlDiscovery extends SAMLDiscovery {
    private static final Logger log = LoggerFactory.getLogger(SamlDiscovery.class);

    private IdpDiscoveryService idpDiscoveryService;

    public IdpDiscoveryService getIdpDiscoveryService() {
        return idpDiscoveryService;
    }

    /**
     * @param idpDiscoveryService
     */
    public void setIdpDiscoveryService(IdpDiscoveryService idpDiscoveryService) {
        this.idpDiscoveryService = idpDiscoveryService;
    }

    /**
     * @param request
     * @return String idp
     */
    @Override
    protected String getPassiveIDP(HttpServletRequest request) {
        String idp = idpDiscoveryService.resolveIdpUrl(RestUtils.getClientIp(request));
        if (idp == null) {
            idp = super.getPassiveIDP(request);
        }
        return idp;
    }

}
