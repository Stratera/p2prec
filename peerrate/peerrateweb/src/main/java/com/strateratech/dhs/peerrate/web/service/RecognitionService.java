package com.strateratech.dhs.peerrate.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.Department;
import com.strateratech.dhs.peerrate.entity.Recognition;
import com.strateratech.dhs.peerrate.entity.UserProfile;
import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
import com.strateratech.dhs.peerrate.entity.repository.RecognitionRepository;
import com.strateratech.dhs.peerrate.entity.repository.UserProfileRepository;

/**
 * @author Matt Young
 * @date Aug 27, 2016
 * @version 
 *
 */
@Service 
public class RecognitionService {

    @Inject
    private RecognitionRepository recognitionRepository;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private UserProfileRepository userProfileRepository;

    
    /**
     * get recognition db entity by ID and map it to the rest contract recognition object
     */
    public com.strateratech.dhs.peerrate.rest.contract.Recognition getById(Long id) {
        Recognition recognition = recognitionRepository.findOne(id);
        
        return mapDbRecognitionToWebRecognition(recognition);
    }

    /**
     * mapping db recognition entity to rest contract entity
     * @param dbRecognition
     * @return
     * @since Aug 27, 2016
     */
    private com.strateratech.dhs.peerrate.rest.contract.Recognition mapDbRecognitionToWebRecognition(Recognition dbRecognition) {
        com.strateratech.dhs.peerrate.rest.contract.Recognition webRec = null;
        if (dbRecognition != null) {
            webRec = new com.strateratech.dhs.peerrate.rest.contract.Recognition();
            webRec.setId(dbRecognition.getId());
            webRec.setMessageText(dbRecognition.getMessage());
            webRec.setAttachment(dbRecognition.getAttachment());
            webRec.setAttachmentContentType(dbRecognition.getAttachmentContentType());
            if (dbRecognition.getRecipientUserProfile() != null) {
                webRec.setRecipientUserProfileId(dbRecognition.getRecipientUserProfile().getId());
            } else {
                if (dbRecognition.getDepartment() != null) {
                    webRec.setRecipientDepartmentId(dbRecognition.getDepartment().getId());
                }
            }
            if (dbRecognition.getSendingUserProfile() != null) {
                webRec.setSendingUserProfileId(dbRecognition.getSendingUserProfile().getId());
            }
            webRec.setSubmitTs(dbRecognition.getSubmitTs());
        }
        return webRec;
    }

    /**
     * Lists by recipient user id
     * @param userId
     * @return
     * @since Aug 27, 2016
     */
    public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listByRecipientUserId(Long userId) {
        
        List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
        List<Recognition> recs = recognitionRepository.findByRecipientUserId(userId);
        for (Recognition recognition: recs) {
            list.add(mapDbRecognitionToWebRecognition(recognition));
        }
        return list;
    }


    /**
     * list by recipient department id 
     * @param departmentId
     * @return
     * @since Aug 27, 2016
     */
    public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listByRecipientDepartmentId(Long departmentId) {
        
        List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
        List<Recognition> recs = recognitionRepository.findByRecipientDepartmentId(departmentId);
        for (Recognition recognition: recs) {
            list.add(mapDbRecognitionToWebRecognition(recognition));
        }
        return list;
    }


    /**
     * List by sending user id
     * @param userId
     * @return
     * @since Aug 27, 2016
     */
    public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listBySendingUserId(Long userId) {
        
        List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
        List<Recognition> recs = recognitionRepository.findBySendingUserId(userId);
        for (Recognition recognition: recs) {
            list.add(mapDbRecognitionToWebRecognition(recognition));
        }
        return list;
    }

    /**
     * List by Department
     * @param userId
     * @return
     * @since Aug 27, 2016
     */
    public List<com.strateratech.dhs.peerrate.rest.contract.Recognition> listByDepartment(Long departmentId) {
        
        List<com.strateratech.dhs.peerrate.rest.contract.Recognition> list = new ArrayList<>();
        List<Recognition> recs = recognitionRepository.findByRecipientDepartmentId(departmentId);
        for (Recognition recognition: recs) {
            list.add(mapDbRecognitionToWebRecognition(recognition));
        }
        return list;
    }

    
    public com.strateratech.dhs.peerrate.rest.contract.Recognition save(
            com.strateratech.dhs.peerrate.rest.contract.Recognition recognition, String email) {
        Recognition dbRecognition = mapToDbRecognition(recognition);
        dbRecognition = recognitionRepository.save(dbRecognition);
        
        return mapDbRecognitionToWebRecognition(dbRecognition);
    }

    private Recognition mapToDbRecognition(com.strateratech.dhs.peerrate.rest.contract.Recognition recognition) {
        Recognition dbObj = null;
        if (recognition != null) {
            dbObj = new Recognition();
            dbObj.setAttachment(recognition.getAttachment());
            dbObj.setAttachmentContentType(recognition.getAttachmentContentType());
            if (recognition.getRecipientDepartmentId() != null) {
                Department dep = departmentRepository.findOne(recognition.getRecipientDepartmentId());
                dbObj.setDepartment(dep);
            }
            else if ( recognition.getRecipientUserProfileId() != null) {
                UserProfile up = userProfileRepository.findOne(recognition.getRecipientUserProfileId());
                dbObj.setRecipientUserProfile(up);
            }
            
            dbObj.setMessage(recognition.getMessageText());
            dbObj.setSubmitTs(new Date());
            if (recognition.getSendingUserProfileId() != null) {
                UserProfile up = userProfileRepository.findOne(recognition.getSendingUserProfileId());
                dbObj.setSendingUserProfile(up);
            }
            
        }
        return dbObj;
    }

   
    
}
