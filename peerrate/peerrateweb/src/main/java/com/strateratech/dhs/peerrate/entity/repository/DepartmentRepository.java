package com.strateratech.dhs.peerrate.entity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.strateratech.dhs.peerrate.entity.Department;

/**
 * Repository for UserProfile
 * 
 * @author 2020
 * @date August 21, 2016
 * @version 
 *
 */
@Repository
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {

    /**
     * find by name. This is safe because there is a unique constraint on name
     * @param name
     * @return
     * @since Aug 24, 2016
     */
	@Query("from Department e where e.name = :name")
	Department findByName(@Param("name") String name);

}
