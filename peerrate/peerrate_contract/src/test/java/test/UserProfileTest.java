package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strateratech.dhs.peerrate.rest.contract.Recognition;
import com.strateratech.dhs.peerrate.rest.contract.UserProfile;

public class UserProfileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileTest.class);
    private static final JAXBContext CTX = initJAXBContext();

    @Test
    public void test() throws JAXBException, JsonGenerationException, JsonMappingException, IOException {
        InputStream is = null; 
        try {
            
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("me.png");
            
            
        UserProfile user = new UserProfile();
        user.setFullName("Matt Young");
        user.setProfilePicBytes(IOUtils.toByteArray(is));
        user.setProfilePicContentType("image/png");
        Recognition r = new Recognition();
        user.getRecievedRecognitions().add(r);
        
        r = new Recognition();
        user.getRecievedRecognitions().add(r);
        
        r = new Recognition();
        user.getSentRecognitions().add(r);
        
        

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Marshaller m =CTX.createMarshaller();
        m.marshal(user, baos);
        LOGGER.debug("XML = {}",baos.toString());
        
        ObjectMapper mapper = new ObjectMapper();
        baos = new ByteArrayOutputStream();
        mapper.writeValue(baos, user);
        LOGGER.debug("JSON = {}",baos.toString());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private static JAXBContext initJAXBContext() {
        JAXBContext ctx = null;
        try {
            ctx = JAXBContext.newInstance(UserProfile.class);
        } catch (Exception e) {
            LOGGER.debug("Cannot init context ...", e);
        }
        return ctx;
    }
}
