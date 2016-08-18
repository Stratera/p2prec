package com.strateratech.dhs.peerrate.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Utility class to handle common JSON serialization tasks.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public final class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    /**
     * Serialize and object to JSON.
     * 
     * @param o
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String toJson(Object o) throws JsonGenerationException, JsonMappingException, IOException {
        String json = null;
        ByteArrayOutputStream baos = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            baos = new ByteArrayOutputStream();
            mapper.writer().writeValue(baos, o);
            json = baos.toString(CharEncoding.UTF_8);
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                log.error("Exception closing byte array output stream", e);
            }
        }
        return json;
    }
}
