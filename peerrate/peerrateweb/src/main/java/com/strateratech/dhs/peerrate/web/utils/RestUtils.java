package com.strateratech.dhs.peerrate.web.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Rest Utility class to build Http Headers
 * 
 *  NOTE: Raw types are used throughout this class because the contract being implemented 
 * allows for potentially returning/handling types that might not extend/implement a 
 * common base class.  Getting rid of raw types would require the contract objects to 
 * extend a common base class which would have to be built into contract XSDs and offers
 * us nothing in the way of concrete type safety as every contract version may add/remove
 * fields.  These raw types are then passed back to spring to handle serialization via
 * whatever format the client is expecting.  THERE IS NO BENEFIT IN TERMS OF TYPE SAFETY 
 * TO TRY AND FORCE THESE RAW TYPES INTO AN CLASS HIERARCHY.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public final class RestUtils {
    public static final Logger log = LoggerFactory.getLogger(RestUtils.class);
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String ACCEPT_HEADER_SEPARATOR = ",|;";
    public static final String ACCEPT_HEADER_ANY = "*/*";
    public static final int CACHE_DURATION_IN_SECONDS = 60 * 60; // 1 hour
    public static final String CACHE_CONTROL_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'UTC'";

    public static final String URL_SEPARATOR = "/";
    
    private static final String LOAD_BALANCER_CLIENT_IP_HEADER_NAME = "X-Forwarded-For";
    

    /**
     * Private Constructor to enforce only static access of methods
     */
    private RestUtils() {
        super();
    }

    /**
     * Build Rest HTTP headers without a LAST_MODIFIED
     * @return
     * @since Aug 3, 2016
     */
    public static HttpHeaders buildRestHttpHeaders() {
    	return buildRestHttpHeaders(null);
    }
    /**
     * Method to build Rest Http Headers
     * 
     * @return HttpHeaders
     */
    public static HttpHeaders buildRestHttpHeaders(Date lastModifiedDate) {
        String accept = getAcceptHeader();
        log.debug("Using HttpHeaders [{}] ", accept);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, accept);
        if (lastModifiedDate != null) {
        	headers.add(HttpHeaders.LAST_MODIFIED, DateFormatUtils.format(lastModifiedDate, 
        			CACHE_CONTROL_DATE_FORMAT));
        }
        Map manifest = ((Map) getHttpServletRequest().getServletContext().getAttribute("manifest"));
        if (manifest != null && manifest.get("app-version") != null) {
            headers.add("APPLICATION_VERSION", manifest.get("app-version").toString());
        }
        return headers;
    }

    /**
     * Method to build Error Http Headers
     * 
     * @return HttpHeaders
     */
    public static HttpHeaders buildErrorHttpHeaders() {
        return buildPlainHttpHeaders();
    }

    /**
     * Method to build Plain Http Headers
     * 
     * @return HttpHeaders
     */
    public static HttpHeaders buildPlainHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_PLAIN);
        return headers;
    }

    /**
     * Gets the accept header value from the HttpServletRequest in scope for
     * this thread
     * 
     * @return String headerValue
     */
    public static String getAcceptHeader() {

        String accept = cleanAcceptHeader(getRequestHeaderValue(HttpHeaders.ACCEPT));

        if (StringUtils.isBlank(accept) || accept.startsWith(ACCEPT_HEADER_ANY)) {
            accept = CONTENT_TYPE_JSON;
        }
        return accept;
    }

    /**
     * browsers send a weird accept string
     * (text/html,application/xhtml+xml,application/xml;q=0.9,image/webp). It
     * needs to be broken down and parsed for the most acceptable
     * 
     * @param value
     * @return
     */
    public static String cleanAcceptHeader(String value) {
        if (value != null && value.split(ACCEPT_HEADER_SEPARATOR).length > 0) {
            String[] parts = value.split(ACCEPT_HEADER_SEPARATOR);
            value = parts[0];
            for (String part : parts) {
                if (Arrays.asList(CONTENT_TYPE_JSON, CONTENT_TYPE_XML).contains(part)) {
                    value = part;
                    break;
                }
            }
        }
        return value;
    }

    /**
     * Get Header value by name from the threadlocal bound HttpServletRequest
     * 
     * @param headerName
     * @return String header value
     */
    public static String getRequestHeaderValue(String headerName) {
        HttpServletRequest req = getHttpServletRequest();
        return req.getHeader(headerName);
    }

    /**
     * Gets the HttpServletRequest value from the spring RequestContextHolder
     * 
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        return req;
    }

    /**
     * This method handles checking appropriately for the X-FORWARDED-FOR header
     * in case of load balancing
     * 
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        if (!StringUtils.isBlank(request.getHeader(LOAD_BALANCER_CLIENT_IP_HEADER_NAME))) {
            ip = request.getHeader(LOAD_BALANCER_CLIENT_IP_HEADER_NAME);
        } else {
            ip = request.getRemoteAddr();
        }
        log.debug("using {} as ip.", ip);
        return ip;
    }

    /**
     * Method to get the base url
     * 
     * @return
     */
    public static String getBaseUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String requestUrl = request.getRequestURL().toString();
        log.debug("original url={} appContext={}", requestUrl, request.getContextPath());

        requestUrl = StringUtils.substringBefore(requestUrl, request.getContextPath());
        requestUrl += request.getContextPath();
        return requestUrl;

    }

}
