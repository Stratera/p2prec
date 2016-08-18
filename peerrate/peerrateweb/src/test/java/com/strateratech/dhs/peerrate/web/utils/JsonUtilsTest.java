package com.strateratech.dhs.peerrate.web.utils;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonUtilsTest {

    @Test
    public void test() throws JsonGenerationException, JsonMappingException, IOException {
        String[] arr = new String[] { "a", "b", "c" };
        String json = JsonUtils.toJson(arr);
        Assert.assertEquals("[\"a\",\"b\",\"c\"]", json);
    }

}
