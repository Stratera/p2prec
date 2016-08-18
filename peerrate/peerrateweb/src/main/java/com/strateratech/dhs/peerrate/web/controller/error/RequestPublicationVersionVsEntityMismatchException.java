package com.strateratech.dhs.peerrate.web.controller.error;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;

/**
 * Any time this error is encountered, it means the API attempted to fetch by ID
 * an entity whose scheme_publication_date is different from that of the
 * requested headers
 * 
 * @author 2020
 * @date Nov 30, 2015 5:18:55 PM
 * @version 1.2
 */
public class RequestPublicationVersionVsEntityMismatchException extends RuntimeException {

    /**
     * making the serializable interface happy
     */
    private static final long serialVersionUID = 1L;

    /**
     * Parameterized Constructor
     * 
     * @param entityDate
     * @param requestedTreeDate
     */
    public RequestPublicationVersionVsEntityMismatchException(Date entityDate, Date requestedTreeDate) {
        super("Entity Identifed its date as "
                + new DateTime(entityDate).toString(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern())
                + " while this request is set to expect a scheme publicaiton Date of "
                + new DateTime(entityDate).toString(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern()));
    }

    /**
     * Default constructor
     */
    public RequestPublicationVersionVsEntityMismatchException() {
        super();
    }

    /**
     * Parameterized Constructor
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public RequestPublicationVersionVsEntityMismatchException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Parameterized Constructor
     * 
     * @param message
     * @param cause
     */
    public RequestPublicationVersionVsEntityMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized Constructor
     * 
     * @param message
     */
    public RequestPublicationVersionVsEntityMismatchException(String message) {
        super(message);
    }

    /**
     * Parameterized Constructor
     * 
     * @param cause
     */
    public RequestPublicationVersionVsEntityMismatchException(Throwable cause) {
        super(cause);
    }

}
