package com.strateratech.dhs.peerrate.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.Recognition;
import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
import com.strateratech.dhs.peerrate.entity.repository.RecognitionRepository;
import com.strateratech.dhs.peerrate.entity.repository.UserProfileRepository;

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


	@Inject
	private DepartmentRepository departmentRepository;

	@Inject
	private UserProfileRepository userProfileRepository;


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

    /**
     * converts recognition from web object to db object
     * save db object
     * return generated id
     * @param recognition
     * @return
     */
    @Transactional
	public Long save(com.strateratech.dhs.peerrate.rest.contract.Recognition recognition, String userId) {
		Recognition dbObj = mapWebRecognitionToDbRecognition(recognition, userId);
		dbObj = recognitionRepository.save(dbObj);
		return dbObj.getId();
	}

	private Recognition mapWebRecognitionToDbRecognition(
			com.strateratech.dhs.peerrate.rest.contract.Recognition recognition, String userId) {
		Recognition dbObj =  null;
		Date now = new Date();
		if (recognition != null) {
			dbObj = new Recognition();
			dbObj.setAttachment(recognition.getAttachment());
			dbObj.setAttachmentContentType(recognition.getAttachmentContentType());
			dbObj.setCreateTs(now);
			dbObj.setCreateUsername(userId);
			dbObj.setUpdateTs(now);
			dbObj.setUpdateUsername(userId);
			dbObj.setSubmitTs(recognition.getSubmitTs());
			if (recognition.getRecipientDepartmentId() != null) {
				dbObj.setDepartment(departmentRepository.findOne(recognition.getRecipientDepartmentId()));
			}
			if (recognition.getSendingUserProfileId() != null) {
				dbObj.setSendingUserProfile(userProfileRepository.findOne(recognition.getSendingUserProfileId()));
			}
			if (recognition.getRecipientUserProfileId() != null) {
				dbObj.setRecipientUserProfile(userProfileRepository.findOne(recognition.getRecipientUserProfileId()));
			}
			dbObj.setMessage(recognition.getMessageText());

		}
		return dbObj;
	}
}