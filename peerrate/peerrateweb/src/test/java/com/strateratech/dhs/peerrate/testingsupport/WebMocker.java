package com.strateratech.dhs.peerrate.testingsupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.strateratech.dhs.peerrate.web.utils.RestUtils;

/**
 * Static utility class for Mocking web objects for servlet spec impl at
 * run-time without needing a real servlet container
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version 1.0
 */
public final class WebMocker {
    private WebMocker() {
        // private constructor, Use static methods.
    }

    /**
     * Constructs a HttpServletRequest object for use with unit tests! TODO come
     * back and add a version of this method to mock out basic auth!
     * 
     * @param serverName
     * @param appContext
     * @param relativeHttpResourcePath
     * @return HttpServletRequest
     */
    public static HttpServletRequest mockHttpRequest(String serverName, String appContext,
            String relativeHttpResourcePath) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath(appContext);
        request.setServerName(serverName);
        request.setServerPort(8080);
        request.setRequestURI(appContext + relativeHttpResourcePath);
        request.addHeader(HttpHeaders.ACCEPT, RestUtils.CONTENT_TYPE_JSON);
        return request;
    }
    

    /**
     * Constructs a HttpServletResponse object for use with unit tests! TODO come
     * back and add a version of this method to mock out basic auth!
     * 
     * @param serverName
     * @param appContext
     * @param relativeHttpResourcePath
     * @return HttpServletRequest
     */
    public static HttpServletResponse mockHttpResponse() {
        MockHttpServletResponse resp = new MockHttpServletResponse();
        resp.setWriterAccessAllowed(true);
        resp.setOutputStreamAccessAllowed(true);
        return resp;
    }
    
}
