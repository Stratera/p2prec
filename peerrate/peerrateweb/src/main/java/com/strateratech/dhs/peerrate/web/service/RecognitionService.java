package com.strateratech.dhs.peerrate.web.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.Department;
import com.strateratech.dhs.peerrate.entity.Recognition;
import com.strateratech.dhs.peerrate.entity.UserProfile;
import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
import com.strateratech.dhs.peerrate.entity.repository.RecognitionRepository;
import com.strateratech.dhs.peerrate.rest.contract.saml.RestAuthenticationToken;

/**
 * General Service for interacting with repositories and converting between web
 * layer and data layer objects
 * 
 * @author myoung
 * @date Aug 23, 2016
 * @version
 *
 */
@Service
public class RecognitionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RecognitionService.class);

	@Inject
	private RecognitionRepository recognitionRepository;


	/**
	 * Convert Authentication object to user profile, if user profile does not
	 * already exist using that email, store it. When complete, return user
	 * profile (stored or existing)
	 * 
	 * @param auth
	 * @return
	 * @since Aug 23, 2016
	 */
	@Transactional
	public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listRecognitionsByRecipientUserProfile(Long userProfileId) {
	    List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
	    List<Recognition> dbList = recognitionRepository.findByRecipientUserProfileId(userProfileId);
	    for (Recognition dbObj: dbList) {
	        list.add(mapDbRecognitionToWebContractRecognition(dbObj));
	    }
	    return list;
	}

	private com.strateratech.dhs.peerrate.rest.contract.Recognition mapDbRecognitionToWebContractRecognition(
            Recognition dbObj) {
	    com.strateratech.dhs.peerrate.rest.contract.Recognition webRec = null;
	    if (dbObj != null) {
	        webRec = new com.strateratech.dhs.peerrate.rest.contract.Recognition();
	        webRec.setId(dbObj.getId());
	        webRec.setMessageText(dbObj.getMessage());
	        webRec.setAttachment(dbObj.getAttachment());
	        webRec.setAttachmentContentType(dbObj.getAttachmentContentType());
	        if ( dbObj.getDepartment() != null ) {
	            webRec.setRecipientDepartmentId(dbObj.getDepartment().getId());
	        }
	        if (dbObj.getRecipientUserProfile() != null) {
	            webRec.setRecipientUserProfileId(dbObj.getRecipientUserProfile().getId());
	        }
	        
	        webRec.setSubmitTs(dbObj.getSubmitTs());
	        if (dbObj.getSendingUserProfile() != null) {
	            webRec.setSendingUserProfileId(dbObj.getSendingUserProfile().getId());
	        }
	    }
        return webRec;
    }

    public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listRecognitionsByRecipientDepartment(
            Long deptId) {
        List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
        List<Recognition> dbList = recognitionRepository.findByRecipientDepartmentId(deptId);
        for (Recognition dbObj: dbList) {
            list.add(mapDbRecognitionToWebContractRecognition(dbObj));
        }
        return list;
    }
}