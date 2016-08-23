package com.strateratech.dhs.peerrate.entity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.strateratech.dhs.peerrate.entity.UserProfile;

/**
 * Repository for UserProfile
 * 
 * @author 2020
 * @date August 21, 2016
 * @version 
 *
 */
@Repository
public interface UserProfileRepository extends PagingAndSortingRepository<UserProfile, Long> {

	@Query("from UserProfile e where e.email = :email")
	UserProfile findByEmail(@Param("email") String email);

}
