package com.strateratech.dhs.peerrate.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;
import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
import com.strateratech.dhs.peerrate.rest.contract.Department;
import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.rest.contract.UserProfile;
import com.strateratech.dhs.peerrate.testingsupport.BasicTestingGrantedAuthority;
import com.strateratech.dhs.peerrate.testingsupport.DatasetTestingService;
import com.strateratech.dhs.peerrate.testingsupport.WebMocker;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-test.xml" })
public class RecognitionControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecognitionControllerTest.class);
    @Inject
    private DatasetTestingService datasetTestingService;
    

    @Inject
    private RecognitionController recognitionController;

    

    
    @Test
    @Transactional
    public void testListByRecipientUserIds() throws IOException {

        ResponseEntity<List<Recognition>> resp = recognitionController.listByRecipientUserId(1L);
        Assert.assertEquals(2, resp.getBody().size());
        resp = recognitionController.listByRecipientUserId(-11L);
        Assert.assertNull(resp.getBody());
        Assert.assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    @Transactional
    public void testListByRecipientDeptIds() throws IOException {

        ResponseEntity<List<Recognition>> resp = recognitionController.listByRecipientDepartmentId(1L);
        Assert.assertEquals(1, resp.getBody().size());
    }

    @Before
    public void setUp() throws Exception {
        IDatabaseConnection conn = datasetTestingService.getConnection();
        datasetTestingService.emptyTables(conn);
        datasetTestingService.loadAllDatasets(conn);


        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("user@user.com", "user@user.com",
                Arrays.asList(new BasicTestingGrantedAuthority("test")));

        SecurityContextHolder.getContext().setAuthentication(token);

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(WebMocker.mockHttpRequest("localhost", "/peerrateweb", "/userprofiles")));
        
    }


}
