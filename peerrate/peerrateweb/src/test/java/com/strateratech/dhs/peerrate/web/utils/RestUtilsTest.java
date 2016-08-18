package com.strateratech.dhs.peerrate.web.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.strateratech.dhs.peerrate.testingsupport.WebMocker;


public class RestUtilsTest {

	@Test
	public void testBuildRestHttpHeaders() {
		HttpHeaders headers=RestUtils.buildRestHttpHeaders();
		Assert.assertEquals("application/json", headers.getContentType().toString());
	}

	@Test
	public void testBuildErrorHttpHeaders() {
		HttpHeaders headers=RestUtils.buildErrorHttpHeaders();
		Assert.assertEquals("text/plain", headers.getContentType().toString());
	}

	@Test
	public void testBuildPlainHttpHeaders() {
		HttpHeaders headers=RestUtils.buildPlainHttpHeaders();
		Assert.assertEquals("text/plain", headers.getContentType().toString());
	}
	
	@Test 
	public void testCleanAcceptHeader() {
		String browserAccept="text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\\";
		String accept = RestUtils.cleanAcceptHeader(browserAccept);
		Assert.assertEquals(RestUtils.CONTENT_TYPE_XML,accept);
	}
	
	@Before
	public  void setup() {
		 
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(WebMocker.mockHttpRequest("localhost", "/cpcipcrestweb", "/symbols")));
       
	}

}
