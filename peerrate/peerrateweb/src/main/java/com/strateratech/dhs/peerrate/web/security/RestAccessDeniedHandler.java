/**
 * REST access denied handler.
 */
package com.strateratech.dhs.peerrate.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.strateratech.dhs.peerrate.web.controller.error.RestError;
import com.strateratech.dhs.peerrate.web.utils.JsonUtils;

/**
 * 
 * Class for Rest Access Denied Handler
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public class RestAccessDeniedHandler extends AccessDeniedHandlerImpl {
    private String restHeader;

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException exc)
            throws IOException, ServletException {
        if (!StringUtils.isBlank(req.getHeader(restHeader))
                && req.getHeader(restHeader).toLowerCase().startsWith("application/json")) {
            //TODO We need to add support for XML at some point (maybe)
            resp.getOutputStream()
                    .write(JsonUtils.toJson(new RestError(HttpStatus.FORBIDDEN, "Insufficient Privilege/Access"))
                            .getBytes(CharEncoding.UTF_8));
        } else {
            super.handle(req, resp, exc);
        }

    }

    /**
     * Get the REST header.
     *
     * @return the REST header.
     */
    public String getRestHeader() {
        return restHeader;
    }

    /**
     * Set the REST header.
     *
     * @param restHeader the REST header.
     */
    public void setRestHeader(String restHeader) {
        this.restHeader = restHeader;
    }

}
