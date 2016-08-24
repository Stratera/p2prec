package com.strateratech.dhs.peerrate.web.service;

import java.io.InputStream;
import java.util.Locale;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.UserProfile;
import com.strateratech.dhs.peerrate.entity.repository.UserProfileRepository;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;
/**
 * General Service for interacting with repositories and converting between web layer and data layer objects
 * @author 2020
 * @date Aug 23, 2016
 * @version 
 *
 */
@Service
public class UserProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
    private static final String DEFAULT_IMG_RESOURCE_PATH = "images/unknown-user.jpg";
    private static final String DEFAULT_PROFILE_PIC_CONTENT_TYPE = "image/jpeg";

    @Inject
    private UserProfileRepository userProfileRepository;
    
    @Inject
    private SecurityService securityService;


    /**
     * Convert Authentication object to user profile, if user profile does not already exist using that email,
     * store it.  When complete, return user profile (stored or existing)
     * @param auth
     * @return
     * @since Aug 23, 2016
     */
    @Transactional
    public UserProfile saveIfNotExists(Authentication auth) {
        UserProfile user = null;
        if (auth != null) {
            RestAuthenticationToken restToken = securityService.mapSamlTokenToContractModel(auth);
            user = userProfileRepository.findByEmail(restToken.getEmail());
            if (user == null) {
                user = mapRestToUserProfile(restToken);
                user = userProfileRepository.save(user);
            }
        }
        return user;
    }


    /**
     * Convert from Web object to user profile object
     * @param restToken
     * @return
     * @since Aug 23, 2016
     */
    private UserProfile mapRestToUserProfile(RestAuthenticationToken restToken) {
        UserProfile user = null;
        if (restToken != null) {
            user = new UserProfile();
            user.setEmail(restToken.getEmail());
            user.setFullName(restToken.getFirstName()+StringUtils.SPACE+restToken.getLastName());
            user.setOfficeCountry(LocaleContextHolder.getLocale().getCountry());
            if (user.getOfficeCountry() == null) {
                user.setOfficeCountry(Locale.US.getCountry());
            }
            InputStream is = null;
            try {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_IMG_RESOURCE_PATH);
                user.setProfilePic(IOUtils.toByteArray(is));
                user.setProfilePicContentType(DEFAULT_PROFILE_PIC_CONTENT_TYPE);
            } catch (Exception e) {
                LOGGER.error("Failed to set default profile pic", e);
            }
            finally {
                IOUtils.closeQuietly(is);
            }
            
        }
        return user;
    }
}
