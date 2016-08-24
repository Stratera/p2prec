package com.strateratech.dhs.peerrate.web.service;

import java.io.InputStream;
import java.util.Date;
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

import com.strateratech.dhs.peerrate.entity.Department;
import com.strateratech.dhs.peerrate.entity.UserProfile;
import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
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
    private DepartmentRepository departmentRepository;
    
    @Inject
    private SecurityService securityService;
    

    @Inject
    private DepartmentService departmentService;


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

    /**
     * 
     * @param id
     * @return
     * @since Aug 24, 2016
     */
    public com.strateratech.dhs.peerrate.rest.contract.UserProfile getUserProfile(Long id) {
        UserProfile dbUP = userProfileRepository.findOne(id);
        
        return mapDbUserProfileToWebUserProfile(dbUP);
    }
    
    /**
     * 
     * @param dbObj
     * @return
     * @since Aug 24, 2016
     */
    private com.strateratech.dhs.peerrate.rest.contract.UserProfile mapDbUserProfileToWebUserProfile(UserProfile dbObj) {
        com.strateratech.dhs.peerrate.rest.contract.UserProfile up = null;
        if (dbObj != null) {
            up = new com.strateratech.dhs.peerrate.rest.contract.UserProfile();
            up.setId(dbObj.getId());
            up.setEmail(dbObj.getEmail());
            up.setFullName(dbObj.getFullName());
            up.setProfilePicBytes(dbObj.getProfilePic());
            up.setProfilePicContentType(dbObj.getProfilePicContentType());
            up.setDateOfBirth(dbObj.getDateOfBirth());
            up.setDescription(dbObj.getDescription());
            up.setJobTitle(dbObj.getJobTitle());
            up.setOfficeCity(dbObj.getOfficeCity());
            up.setOfficeStateOrProv(dbObj.getOfficeStateOrProv());
            up.setOfficeCountry(dbObj.getOfficeCountry());
            up.setOfficePhone(dbObj.getOfficePhone());
            up.setPersonalPhone(dbObj.getPersonalPhone());
            up.setOfficePostalCode(dbObj.getOfficePostalCode());
            up.setOfficeStreetAddress(dbObj.getOfficeStreetAddress());
            up.setDepartment(mapDbDepartmentToWebDepartment(dbObj.getDepartment()));
        }
        return up;
    }


    /**
     * Map Department db object to web contract Department 
     * @param department
     * @return
     * @since Aug 24, 2016
     */
    private com.strateratech.dhs.peerrate.rest.contract.Department mapDbDepartmentToWebDepartment(Department department) {
        com.strateratech.dhs.peerrate.rest.contract.Department webDept = null;
        if (department != null) {
            webDept = new com.strateratech.dhs.peerrate.rest.contract.Department();
            webDept.setId(department.getId());
            webDept.setName(department.getName());
        }
        return webDept;
    }
    
   /**
    *  Map Web user to db user, save then return result mapped as web user
    *  
    */
   public com.strateratech.dhs.peerrate.rest.contract.UserProfile save(com.strateratech.dhs.peerrate.rest.contract.UserProfile user, 
           String username) {
       UserProfile dbUser = null;
       if (user != null) {
           dbUser = mapWebUserToDbUser(user, username);
           dbUser = userProfileRepository.save(dbUser);
           user = mapDbUserProfileToWebUserProfile(dbUser);
       }
       return user; 
   }


private UserProfile mapWebUserToDbUser(com.strateratech.dhs.peerrate.rest.contract.UserProfile user, String username) {
    UserProfile dbUser = null;
    Date now = new Date();
    if (user == null) {
        if (user.getId() == null) {
            dbUser = new UserProfile();
        } else {
            dbUser = userProfileRepository.findOne(user.getId());
        }
        dbUser.setId(user.getId());
        dbUser.setEmail(user.getEmail());
        dbUser.setFullName(user.getFullName());
        dbUser.setDateOfBirth(user.getDateOfBirth());
        dbUser.setDescription(user.getDescription());
        dbUser.setJobTitle(user.getJobTitle());
        dbUser.setOfficeCity(user.getOfficeCity());
        dbUser.setOfficeCountry(user.getOfficeCountry());
        dbUser.setOfficePhone(user.getOfficePhone());
        dbUser.setOfficePostalCode(user.getOfficePostalCode());
        dbUser.setOfficeStateOrProv(user.getOfficeStateOrProv());
        dbUser.setOfficeStreetAddress(user.getOfficeStreetAddress());
        dbUser.setPersonalPhone(user.getPersonalPhone());
        dbUser.setProfilePic(user.getProfilePicBytes());
        dbUser.setProfilePicContentType(user.getProfilePicContentType());
        dbUser.setUpdateTs(now);
        dbUser.setUpdateUsername(username);
        if (dbUser.getCreateUsername() == null) {
            dbUser.setCreateUsername(username);
        }
        if (dbUser.getCreateTs() == null) {
            dbUser.setCreateTs(now);
        }
        if (user.getDepartment() != null && user.getDepartment().getName() !=  null) {
            user.setDepartment(
                    departmentService.mapDbDepartmentToWebDepartment(
                            departmentRepository.findByName(user.getDepartment().getName())));
        }
    }
    return dbUser;
}
}
