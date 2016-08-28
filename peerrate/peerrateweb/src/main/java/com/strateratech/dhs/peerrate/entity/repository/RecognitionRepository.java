package com.strateratech.dhs.peerrate.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.strateratech.dhs.peerrate.entity.Recognition;

/**
 * Repository for Reconition records
 * 
 * @author 2020
 * @date August 21, 2016
 * @version 
 *
 */
@Repository
public interface RecognitionRepository extends PagingAndSortingRepository<Recognition, Long> {

    @Query("from Recognition where recipientUserProfile.id=:userId order by submitTs desc")
    List<Recognition> findByRecipientUserId(@Param("userId") Long userId);
    
    @Query("from Recognition where department.id=:userId order by submitTs desc")
    List<Recognition> findByRecipientDepartmentId(@Param("userId") Long userId);

    @Query("from Recognition where sendingUserProfile.id=:userId order by submitTs desc")
    List<Recognition> findBySendingUserId(@Param("userId") Long userId);

}
