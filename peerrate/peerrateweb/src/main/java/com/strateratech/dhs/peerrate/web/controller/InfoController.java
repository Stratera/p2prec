/**
 * 
 */
package com.strateratech.dhs.peerrate.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.strateratech.dhs.peerrate.web.utils.RestUtils;

import io.swagger.annotations.Api;

/**
 * 
 * @author 2020
 * @date Dec 9, 2015 3:45:34 PM
 * @version 
 */

@Component
@Api(value = "/info", description = "Controller returns the version string of the application")
@Controller
@RequestMapping(value = "/info")
public class InfoController {
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET)	
	public ResponseEntity<Map> getManifest(HttpServletRequest request) {
		Map manifest =(Map)request.getServletContext().getAttribute("manifest");
        HttpStatus status = HttpStatus.OK;
        if (manifest == null || manifest.isEmpty()) {
        	status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(manifest, RestUtils.buildRestHttpHeaders(), status);

	}

	@ResponseBody
	@RequestMapping(value="/version",method=RequestMethod.GET, consumes="text/plain")	
	public String getVersion(HttpServletRequest request) {
		
		return ((Map)request.getServletContext().getAttribute("manifest")).get("app-version").toString();
	}

}
