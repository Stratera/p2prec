package com.strateratech.dhs.peerrate.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
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

import com.strateratech.dhs.peerrate.rest.contract.UserProfile;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.SecurityService;
import com.strateratech.dhs.peerrate.web.service.UserProfileService;
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
@Api(value = "/userprofiles", description = "Handles all crud operations for user profile resource")
@Component
@RestController
@RequestMapping(value = "/userprofiles")
public class UserProfileController {
    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);
    
    private static int BUFFER_SIZE = 1024;

    @Inject
    private UserProfileService userProfileService;


    @Inject
    private SecurityService securityService;
    /**
     * Method to update user profile
     * 
     * @return ResponseEntity<UserProfile>
     */
    @ApiOperation("Update the User Profile, overriding the contents of user profile for a given id")
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable("id") Long id, @Valid @RequestBody UserProfile userProfile) {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        UserProfile user = userProfileService.getUserProfile(id);
        if (user == null || user.getId() == null) {
            throw new EntityNotFoundException("user profile "+id+" not found.");
        } else {
            RestAuthenticationToken authToken = securityService.mapSamlTokenToContractModel(SecurityContextHolder.getContext().getAuthentication());
            
            user.setId(id);
            user = userProfileService.save(user, authToken.getEmail());
            headers.add(HttpHeaders.LOCATION, 
                    RestUtils.getBaseUrl()+UserProfileController.class.getAnnotation(RequestMapping.class).value()[0]
                            +RestUtils.URL_SEPARATOR+user.getId());

        }
        return new ResponseEntity<UserProfile>(headers, status);

    }

    /**
     * Method to save user
     * 
     * @return ResponseEntity<UserProfile>
     */
    @ApiOperation("Create User Profile")
    @RequestMapping( method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserProfile> saveUserProfile(@Valid @RequestBody UserProfile user) {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        RestAuthenticationToken authToken = securityService.mapSamlTokenToContractModel(SecurityContextHolder.getContext().getAuthentication());
            
        user.setId(null);
        user = userProfileService.save(user, authToken.getEmail());
        headers.add(HttpHeaders.LOCATION, 
                    RestUtils.getBaseUrl()+UserProfileController.class.getAnnotation(RequestMapping.class).value()[0]
                            +RestUtils.URL_SEPARATOR+user.getId());

        return new ResponseEntity<UserProfile>(headers, status);

    }

    /**
     * List User Profiles (non-paged)
     * 
     * @return ResponseEntity<List<UserProfile>>
     */
    @ApiOperation("List User profiles")
    @RequestMapping( method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<UserProfile>> listUserProfiles() {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        List<UserProfile> list = userProfileService.listUserProfiles();
        ResponseEntity<List<UserProfile>> resp = new ResponseEntity<>(headers,HttpStatus.NO_CONTENT);
      
        if (!CollectionUtils.isEmpty(list) ) {
        	resp = new ResponseEntity<>(list,headers,HttpStatus.NO_CONTENT);
        		      
        }

        return resp;

    }
    
    /**
     * Method to Parse Saml Token
     * 
     * @return ResponseEntity<UserProfile>
     */
    @ApiOperation("get user profile for a given id")
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserProfile> getByID(@PathVariable("id") Long id) 
    {
        HttpStatus status = HttpStatus.OK;
        UserProfile profile = userProfileService.getUserProfile(id);
        HttpHeaders restHeaders = RestUtils.buildRestHttpHeaders();
        if (profile == null) {
            status = HttpStatus.NOT_FOUND;
        } else {
            restHeaders.add(HttpHeaders.LOCATION, 
                            RestUtils.getBaseUrl()+UserProfileController.class.getAnnotation(RequestMapping.class).value()[0]
                                    +RestUtils.URL_SEPARATOR+profile.getId());
 
        }
        return new ResponseEntity<UserProfile>(profile, restHeaders,
                status);

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
    @ApiOperation("Update the User Profile, overriding the contents of user profile for a given id")
    @RequestMapping(value="/{id}/profilepic", method=RequestMethod.GET)
    @ResponseBody
    public void getProfilePic(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
          OutputStream outputStream = null;
//          InputStream inputStream = null;
          ServletContext context = request.getServletContext();
//
          try {
              UserProfile userProfile = userProfileService.getUserProfile(id);
             
              // get MIME type of the file
              String mimeType = userProfile.getProfilePicContentType();
              log.debug("MIME type: " + mimeType);

              // set content attributes for the response
              response.setContentType(mimeType);

              // get output stream of the response
              outputStream = response.getOutputStream();

                  outputStream.write(userProfile.getProfilePicBytes());
                  outputStream.flush();
          } finally {
              IOUtils.closeQuietly(outputStream);
          }
          return;
      }

}
