package com.strateratech.dhs.peerrate.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.RecognitionService;
import com.strateratech.dhs.peerrate.web.service.SecurityService;
import com.strateratech.dhs.peerrate.web.utils.RestUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * User Profile Controller
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/24/2016
 *
 */
@Api(value = "/recognitions", description = "Handles all crud operations for recognition resource")
@Component
@RestController
@RequestMapping(value = "/recognitions")
public class RecognitionController {
    private static final Logger log = LoggerFactory.getLogger(RecognitionController.class);
    
    private static int BUFFER_SIZE = 1024;

    @Inject
    private RecognitionService recognitionService;


    @Inject
    private SecurityService securityService;
    /**
     * Save Recognition web object.  Return only location header
     */
    @ApiOperation("Save Recognition web object")
    @RequestMapping( method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Recognition> save(@Valid @RequestBody Recognition recognition) {
    	HttpStatus status = HttpStatus.BAD_REQUEST;
        RestAuthenticationToken authToken = securityService.mapSamlTokenToContractModel(SecurityContextHolder.getContext().getAuthentication());
        
    	Long id = recognitionService.save(recognition, authToken.getEmail());
    	  HttpHeaders restHeaders = RestUtils.buildRestHttpHeaders();
          if (id != null) {
                            restHeaders.add(HttpHeaders.LOCATION, 
                              RestUtils.getBaseUrl()+RecognitionController.class.getAnnotation(RequestMapping.class).value()[0]
                                      +id);
                            status=HttpStatus.CREATED;
   
          }
    	return new ResponseEntity<>(restHeaders,status);

    }


    /**
     * Get Recognition by id
     */
    @ApiOperation("Save Recognition web object")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Recognition> save(@PathVariable("id") Long id) {
        return null;

    }

    
    /**
     * Method to List recognitions assigned to a given user by id
     * 
     * @return ResponseEntity<List<Recognition>>
     */
    @ApiOperation("List recognitions assigned to a given user by id")
    @RequestMapping(value="/by/recipient/user/{userProfileId}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Recognition>> listByRecipientUserId(@PathVariable("userProfileId") Long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        ResponseEntity<List<Recognition>> resp = new ResponseEntity<List<Recognition>>(headers,status);
        List<Recognition> list = recognitionService.listRecognitionsByRecipientUserProfile(id);
        if (list.size()>0) {
            resp = new ResponseEntity<List<Recognition>>(list,headers,HttpStatus.OK);
        }
        return resp;

    }

    /**
     * Method to List recognitions assigned to a given user by id
     * 
     * @return ResponseEntity<List<Recognition>>
     */
    @ApiOperation("List recognitions assigned to a given department by id")
    @RequestMapping(value="/by/recipient/department/{departmentId}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Recognition>> listByRecipientDepartmentId(@PathVariable("departmentId") Long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        ResponseEntity<List<Recognition>> resp = new ResponseEntity<List<Recognition>>(headers,status);
        List<Recognition> list = recognitionService.listRecognitionsByRecipientDepartment(id);
        if (list.size()>0) {
            resp = new ResponseEntity<List<Recognition>>(list,headers,HttpStatus.OK);
        }
        return resp;

    }

}
