package com.strateratech.dhs.peerrate.web.jee.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.strateratech.dhs.peerrate.entity.repository.UserProfileRepository;
import com.strateratech.dhs.peerrate.web.service.UserProfileService;
import com.strateratech.dhs.peerrate.web.service.saml.StrateratechSamlUserDetailsService;

/**
 * This filter intercepts all requests and specifies the requested (or
 * default/latest) published scheme version along with XSD mappings as thread
 * local context variable for use by subsequent code. Uses include but are not
 * limited to the
 * 
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version
 */
public class SaveLoggedInUserFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SaveLoggedInUserFilter.class);

    private UserProfileService userProfileService;


    /**
     * called when filter is shut down (context destroyed, reloaded, etc)
     */
    @Override
    public void destroy() {
    }

    /**
     * if findByEmail returns no user create profile stub with sensible defaults 
     * and save user profile records
     *  
     *     
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
      //  log.debug("\n\n\n\n-----------------------{}", SecurityContextHolder.getContext().getAuthentication());
        userProfileService.saveIfNotExists(SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }


    /**
     * Called once when the filter is configured as part of context startup. If
     * we allow any configuration of the filter, that needs to be handled here.
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * @return the userProfileService
     */
    public UserProfileService getUserProfileService() {
        return userProfileService;
    }

    /**
     * @param userProfileService the userProfileService to set
     */
    public void setUserProfileService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

}
