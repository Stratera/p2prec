package com.strateratech.dhs.peerrate.web.service;

import java.util.Date;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.Department;
import com.strateratech.dhs.peerrate.entity.UserProfile;
import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
/**
 * General Service for interacting with repositories and converting between web layer and data layer objects
 * @author 2020
 * @date Aug 23, 2016
 * @version 
 *
 */
@Service
public class DepartmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);
    

    @Inject
    private DepartmentRepository departmentRepository;


    
    /**
     * 
     * @param dbObj
     * @return
     * @since Aug 24, 2016
     */
    public com.strateratech.dhs.peerrate.rest.contract.Department mapDbDepartmentToWebDepartment(Department dbObj) {
        com.strateratech.dhs.peerrate.rest.contract.Department dept = null;
        if (dbObj != null) {
            dept = new com.strateratech.dhs.peerrate.rest.contract.Department();
            dept.setId(dbObj.getId());
            dept.setName(dbObj.getName());
        }
        return dept;
    }

}
