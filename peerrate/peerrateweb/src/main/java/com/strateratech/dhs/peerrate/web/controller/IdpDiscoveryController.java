package com.strateratech.dhs.peerrate.web.controller;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.strateratech.dhs.peerrate.web.service.IdpDiscoveryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * US5079: SAML Prototype - External Domain
 *
 * IDP Discovery Service Controller
 *
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */

@Component
@Api(value = "/idps", description = "Controller that handles all IDP Discovery related operations")
@RestController
@RequestMapping(value = "/idps")
public class IdpDiscoveryController {
    @Inject
    private IdpDiscoveryService discoveryService;

    /**
     * Resolve the authoritative IDP based off of the IP address supplied as a
     * Request Parameter
     *
     * @param ip
     * @return String - url in the header.
     */
    @ApiOperation("Resolve the authoritative IDP based off of the IP address supplied as a Request Parameter")
    @RequestMapping(value = "/resolve", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> resolveIdpUrl(@RequestParam("ip") String ip) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", discoveryService.resolveIdpUrl(ip));
        return new ResponseEntity<String>("", headers, HttpStatus.OK);
    }
}
