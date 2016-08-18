package com.strateratech.dhs.peerrate.contract.adapter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * Class does the conversion of String Date-Time to Date Object.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public final class XsdDateTimeUtils extends JsonSerializer<Date> {
    private static final Logger log = LoggerFactory.getLogger(XsdDateTimeUtils.class);

    /**
     * Method is used to convert String Date to Date Object.
     * 
     * @param date
     * @return Date Object
     */
    public static Date parseDate(String date) {
        Date d = null;
        try {
            d = DateUtils.parseDate(date, DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
                    DateFormatUtils.ISO_DATETIME_FORMAT.getPattern(), DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        return d;
    }

    /**
     * Method is used to convert Date to String.
     * 
     * @param date
     * @return String
     */
    public static String printDate(Date date) {
        String formatted = null;
        if (date != null) {
            formatted = new DateTime(date).toString(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());
        }
        log.debug("Formatted Date as {}", formatted);
        return formatted;
    }

    /**
     * Method is used to serialize the Date
     * 
     * @param date
     * @param gen
     * @param provider
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        String formattedDate = XsdDateTimeUtils.printDate(date);
        gen.writeString(formattedDate);
    }
}