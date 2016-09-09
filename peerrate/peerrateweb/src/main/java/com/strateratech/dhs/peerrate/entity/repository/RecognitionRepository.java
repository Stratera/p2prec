package com.strateratech.dhs.peerrate.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.strateratech.dhs.peerrate.entity.Recognition;

/**
 * Repository for Recognition
 * 
 * @author myoung
 * @date Sept 8, 2016
 * @version 
 *
 */
@Repository
public interface RecognitionRepository extends PagingAndSortingRepository<Recognition, Long> {


    /**
     * find all recognition objects assigned to a given user ordered by submit ts (newest first)
     * @param userProfileId
     * @return
     */
	@Query("from Recognition r where r.recipientUserProfile.id=:userProfileId order by r.submitTs desc")
    List<Recognition> findByRecipientUserProfileId(@Param("userProfileId") Long userProfileId);


    /**
     * find all recognition objects assigned to a given department ordered by submit ts (newest first)
     * @param deparmentId
     * @return
     */
    @Query("from Recognition r where r.department.id=:deptId order by r.submitTs desc")
    List<Recognition> findByRecipientDepartmentId(@Param("deptId") Long deptId);

}
