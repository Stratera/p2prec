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
public class UserProfileControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileControllerTest.class);
    @Inject
    private DatasetTestingService datasetTestingService;
    
    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private UserProfileController userProfileController;

    

    @Test
    @Transactional
    public void testSave() throws IOException {
        InputStream is = null; 
        UserProfile user = new UserProfile();
        try {
            
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/images/me.png");
            
            
            Department dep = new Department();
            dep.setId(1L);
            dep.setName("SALES");
            user.setDepartment(dep);
        user.setFullName("Matt Young");
        user.setEmail("matt@youngdev.net");
        user.setProfilePicBytes(IOUtils.toByteArray(is));
        user.setProfilePicContentType("image/png");
        Recognition r = new Recognition();
        r.setName("recieved 1");
        user.getRecievedRecognitions().add(r);
        
        r = new Recognition();
        r.setName("recieved 2");
        user.getRecievedRecognitions().add(r);
        
        r = new Recognition();
        r.setName("name 2");
        user.getSentRecognitions().add(r);
        
        
        } finally {
            IOUtils.closeQuietly(is);
        }

        ResponseEntity<UserProfile> resp = userProfileController.saveUserProfile(user);
        Assert.assertTrue(resp.getHeaders().containsKey(HttpHeaders.LOCATION));
    }



    @Test
    @Transactional
    public void testList() throws IOException {
       

        ResponseEntity<List<UserProfile>> resp = userProfileController.listUserProfiles();
        Assert.assertEquals(2,resp.getBody().size());
    }

    @Test
    @Transactional
    public void testGet() throws IOException {
       

        ResponseEntity<UserProfile> resp = userProfileController.getByID(2L);
        Assert.assertEquals("jpierce@strateratech.com",resp.getBody().getEmail());
        resp = userProfileController.getByID(-1L);
        Assert.assertNull(resp.getBody());
        Assert.assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Transactional
    @Test
    public void testGetProfilePic() throws IOException {
        InputStream is = null; 
        UserProfile user = new UserProfile();

        HttpServletRequest req = WebMocker.mockHttpRequest("localhost", "/peerrate", "/userprofiles/1/profilepic");
        HttpServletResponse resp = WebMocker.mockHttpResponse();
        byte[] myPicBytes = null;
        try {
            
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/images/me.png");
            myPicBytes = IOUtils.toByteArray(is);
            userProfileController.getProfilePic(1L, req, resp);
        
        } finally {
            IOUtils.closeQuietly(is);
        }

        byte[] imageBytesFromService =((MockHttpServletResponse)resp).getContentAsByteArray();
        Assert.assertEquals(myPicBytes.length, imageBytesFromService.length);
        Assert.assertEquals(new String(Base64.encodeBase64(myPicBytes)), 
                new String(Base64.encodeBase64(imageBytesFromService)));

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
