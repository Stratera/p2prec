package com.strateratech.dhs.peerrate.web.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
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
import com.strateratech.dhs.peerrate.rest.contract.UserProfile;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.RecognitionService;
import com.strateratech.dhs.peerrate.web.service.SecurityService;
import com.strateratech.dhs.peerrate.web.utils.RestUtils;
import com.strateratech.dhs.peerrate.web.utils.TokenUtils;

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
@Api(value = "/recognitions", description = "Handles all recognition operations")
@Component
@RestController
@RequestMapping(value = "/recognitions")
public class RecognitionController {
    private static final Logger log = LoggerFactory.getLogger(RecognitionController.class);

    @Inject
    private RecognitionService recognitionService;

    @Inject
    private SecurityService securityService;
    
    /**
     * Method to save user
     * 
     * @return ResponseEntity<UserProfile>
     */
    @ApiOperation("Create Recognition")
    @RequestMapping( method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Recognition> saveRecognition(@Valid @RequestBody Recognition recognition) {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        RestAuthenticationToken authToken = securityService.mapSamlTokenToContractModel(SecurityContextHolder.getContext().getAuthentication());
            
        recognition.setId(null);
        recognition = recognitionService.save(recognition, authToken.getEmail());
        headers.add(HttpHeaders.LOCATION, 
                    RestUtils.getBaseUrl()+RecognitionController.class.getAnnotation(RequestMapping.class).value()[0]
                            +RestUtils.URL_PATH_SEPARATOR
                            +recognition.getId());

        return new ResponseEntity<Recognition>(headers, status);

    }

    
    
  /**
   * Controller that allows a resource byte array to be downloaded within the
   * rest application
   * 
   * @param resourceName
   * @param request
   * @param response
   * @throws IOException
   */
    @ApiOperation("getting details of Recognition by id")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Recognition> getById(@PathVariable("id") Long id) {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders restHeaders = RestUtils.buildRestHttpHeaders();
 
          Recognition recognition = recognitionService.getById(id);
              
          if (recognition == null) {
              throw new EntityNotFoundException(); // Just let spring handle this
          }
          return new ResponseEntity<Recognition>(recognition, restHeaders,
                  status);
      }

}
