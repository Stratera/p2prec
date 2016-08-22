package com.strateratech.dhs.peerrate.web.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.SecurityService;
import com.strateratech.dhs.peerrate.web.utils.RestUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * US5079: SAML Prototype - External Domain
 * 
 * User Controller
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@Api(value = "/users", description = "Handles all access to authenticated user resources.")
@Component
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private SecurityService securityService;

    /**
     * Method to Parse Saml Token
     * 
     * @return UsptoAuthenticationToken
     */
    @ApiOperation("Pulls the Saml Authentication Token from the spring authentication framework "
            + " and parses it out into a Model Object Specifically defined in our service Contract XSDs ")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/authentication")
    @ResponseBody
    public ResponseEntity<RestAuthenticationToken> parseSamlToken() {
        RestAuthenticationToken usptoToken = null;
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null) {
            Object authObj = SecurityContextHolder.getContext().getAuthentication();
            log.debug("Authentication Class is {}! is it Castable to SAMLAuthenticationToken? {}",
                    authObj.getClass().getCanonicalName(),
                    SAMLAuthenticationToken.class.isAssignableFrom(authObj.getClass()));

            Authentication samlAuthenticationToken = (Authentication) authObj;
            log.debug("Saml authentication token: [principal={} credentials={} authorities={}]",
                    samlAuthenticationToken.getPrincipal(), samlAuthenticationToken.getCredentials(),
                    samlAuthenticationToken.getAuthorities());
            usptoToken = securityService.mapSamlTokenToContractModel(samlAuthenticationToken);
        }

        return new ResponseEntity<RestAuthenticationToken>(usptoToken, RestUtils.buildRestHttpHeaders(),
                HttpStatus.OK);

    }
}
