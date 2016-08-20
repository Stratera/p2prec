package com.strateratech.dhs.peerrate.web.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class TokenUtilsTest {

    @Test
    public void testSplit() {
        List<String> list = TokenUtils.split("hi,,,Mom, welcome,       home,,|test;token:split,,,,");
        Assert.assertEquals(7, list.size());
        Assert.assertEquals("hi", list.get(0));

        Assert.assertEquals("Mom", list.get(1));
        Assert.assertEquals("welcome", list.get(2));
        Assert.assertEquals("home", list.get(3));
        Assert.assertEquals("test", list.get(4));
        Assert.assertEquals("token", list.get(5));
        Assert.assertEquals("split", list.get(6));
    }

}
