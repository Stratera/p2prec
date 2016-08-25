package com.strateratech.dhs.peerrate.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.strateratech.dhs.peerrate.entity.Department;
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


    @Transactional
    public List<com.strateratech.dhs.peerrate.rest.contract.Department> list() {
        List<Department> dbList = departmentRepository.findAllOrderByName();
        List<com.strateratech.dhs.peerrate.rest.contract.Department> webList = new ArrayList<>();
        for (Department dep: dbList) {
            webList.add(mapDbDepartmentToWebDepartment(dep));
        }
        return webList;
    }

}
