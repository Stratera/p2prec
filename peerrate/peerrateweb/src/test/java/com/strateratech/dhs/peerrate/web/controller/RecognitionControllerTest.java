package com.strateratech.dhs.peerrate.web.controller;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.strateratech.dhs.peerrate.entity.repository.DepartmentRepository;
import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.testingsupport.BasicTestingGrantedAuthority;
import com.strateratech.dhs.peerrate.testingsupport.DatasetTestingService;
import com.strateratech.dhs.peerrate.testingsupport.WebMocker;
import com.strateratech.dhs.peerrate.web.utils.RestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-test.xml" })
public class RecognitionControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileControllerTest.class);
    @Inject
    private DatasetTestingService datasetTestingService;
    
    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private RecognitionController recognitonController;

    @Test
    @Transactional
    public void testGetById() {
        ResponseEntity<Recognition> resp = recognitonController.getById(1L);
        Assert.assertNotNull(resp.getBody());
        Assert.assertEquals((Long)1L, resp.getBody().getId());
        
    }

    @Test(expected=EntityNotFoundException.class)
    @Transactional
    public void testGetByIdNotFound() {
        ResponseEntity<Recognition> resp = recognitonController.getById(2000L); // this entity does not exist
        
    }

    @Test
    @Transactional
    public void testSave() {
        Recognition r = new Recognition();
        r.setAttachment("<root/>".getBytes());
        r.setAttachmentContentType("application/xml");
        r.setMessageText("Test message");
        r.setRecipientDepartmentId(3L);
        r.setSubmitTs(new Date());
        r.setSendingUserProfileId(2L);
       
        ResponseEntity<Recognition> resp = recognitonController.saveRecognition(r);
        LOGGER.debug("resp = {}", resp);
        Assert.assertNotNull(resp.getHeaders().getFirst(HttpHeaders.LOCATION));
        String loc = resp.getHeaders().getFirst(HttpHeaders.LOCATION);
        Long idGenerated = Long.parseLong(StringUtils.substringAfterLast(loc, RestUtils.URL_PATH_SEPARATOR));
        Assert.assertTrue(idGenerated > 999);
        
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
                new ServletRequestAttributes(WebMocker.mockHttpRequest("localhost", "/peerrateweb", "/recognitions")));
        
    }

}
