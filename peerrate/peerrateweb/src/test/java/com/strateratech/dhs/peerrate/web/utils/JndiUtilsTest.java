package com.strateratech.dhs.peerrate.web.utils;

import java.util.Date;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class JndiUtilsTest {

    @Test
    public void testGetJndiProperty() throws NamingException {

        Object o = JndiUtils.getJndiProperty("test");
        Assert.assertNotNull(o);
        Assert.assertTrue((o instanceof java.util.Date));
    }

    @BeforeClass
    public static void setup() throws NamingException {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("java:/jboss/test", new Date());
        builder.activate();

    }

}
