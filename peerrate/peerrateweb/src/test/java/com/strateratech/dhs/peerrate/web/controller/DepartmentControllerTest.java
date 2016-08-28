package com.strateratech.dhs.peerrate.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.strateratech.dhs.peerrate.rest.contract.Department;
import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.testingsupport.BasicTestingGrantedAuthority;
import com.strateratech.dhs.peerrate.testingsupport.DatasetTestingService;
import com.strateratech.dhs.peerrate.testingsupport.WebMocker;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-test.xml" })
public class DepartmentControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentControllerTest.class);
    @Inject
    private DatasetTestingService datasetTestingService;

    @Inject
    private DepartmentController departmentController;


    @Test
    @Transactional
    public void testListSentRecognitions() {
        ResponseEntity<List<Recognition>> resp =   departmentController.listRecognitions(1L);
        Assert.assertEquals(1, resp.getBody().size());
        LOGGER.debug("{}",resp.getBody());
        
        resp = departmentController.listRecognitions(3L);
        Assert.assertNull(resp.getBody());
        LOGGER.debug("{}",resp.getBody());
        
    }


    @Transactional
    @Test
    public void testListDepartments() {
        ResponseEntity<List<Department>> resp = departmentController.listDepartments();
        Assert.assertEquals(resp.getBody().size(), 2);
        Assert.assertEquals(resp.getBody().get(0).getName(), "ADVERTISING");
        Assert.assertEquals(resp.getBody().get(1).getName(), "SALES");
    }
    
    @Before
    public void setUp() throws Exception {
        IDatabaseConnection conn = datasetTestingService.getConnection();
        datasetTestingService.emptyTables(conn);
        datasetTestingService.loadAllDatasets(conn);


        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user1", "user@user.com",
                Arrays.asList(new BasicTestingGrantedAuthority("test")));

        SecurityContextHolder.getContext().setAuthentication(token);

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(WebMocker.mockHttpRequest("localhost", "/peerrateweb", "/departments")));
        
    }


}
