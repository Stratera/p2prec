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

import com.strateratech.dhs.peerrate.rest.contract.Department;
import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.rest.contract.UserProfile;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
import com.strateratech.dhs.peerrate.web.service.DepartmentService;
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
@Api(value = "/departments", description = "Handles all crud operations for department resource")
@Component
@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {
    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);
    private static int BUFFER_SIZE = 1024;

    @Inject
    private DepartmentService departmentService;

    @Inject 
    private RecognitionService recognitionService;

    @Inject
    private SecurityService securityService;
    
    
    /**
     * Method to update user profile
     * 
     * @return ResponseEntity<List<Department>>
     */
    @ApiOperation("list all Departments ordered by name")
    @RequestMapping( method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Department>> listDepartments() {
        HttpStatus status = HttpStatus.OK;
        ResponseEntity<List<Department>> resp = null;
        HttpHeaders headers =RestUtils.buildRestHttpHeaders();
        List<Department> departments = departmentService.list();
        if (departments.size() == 0) {
            status = HttpStatus.NO_CONTENT;
            resp = new ResponseEntity<List<Department>>(headers, status);
        } else {
            resp = new ResponseEntity<List<Department>>(departments, headers, status);
  
        }
        return resp;

    }


    @ApiOperation("get list of recognitions sent to this department")
    @RequestMapping(value="/{departmentId}/recognitions", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Recognition>> listRecognitions(@PathVariable("departmentId") Long departmentId) {
        HttpStatus status = HttpStatus.OK;
        HttpHeaders restHeaders = RestUtils.buildRestHttpHeaders();
        ResponseEntity<List<Recognition>> resp = null;
              List<Recognition> recognitions = recognitionService.listByDepartment(departmentId);
              if (recognitions.size() == 0) {
                  status = HttpStatus.NO_CONTENT;
                  resp = new ResponseEntity<List<Recognition>>(restHeaders, status);
              } else {
                  resp = new ResponseEntity<List<Recognition>>(recognitions, restHeaders, status);
              }
              
          return resp;
      }
    
}
