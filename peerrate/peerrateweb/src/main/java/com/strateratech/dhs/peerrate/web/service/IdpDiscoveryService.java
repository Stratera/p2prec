package com.strateratech.dhs.peerrate.web.service;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * US5079: SAML Prototype - External Domain
 * 
 * Class for IDP Discovery Service to resolve IdpUrl - PTO Url or EPO Url
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Service("idpDiscoveryService")
public class IdpDiscoveryService {
    private static final Logger log = LoggerFactory.getLogger(IdpDiscoveryService.class);

    @Value("${public_idp_url}")
    private String publicIdpUrl;

    /**
     * Method to resolve Idp Url - PTO Url or EPO Url
     * 
     * @param ip
     * @return String redirectUrl
     */
    public String resolveIdpUrl(String ip) {

        String idp =  publicIdpUrl;
        log.info("Returning {} as the IDP", idp);
        return idp;
    }
}
