package com.strateratech.dhs.peerrate.web.enumeration;

/**
 * I18nErrorKey represents Keys used for Localized error messages within error
 * responses.
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version
 */
public enum I18nErrorKey {
    /**
     * HTTP 400 caused by api usage errors (invalid document, improper formatted
     * request, unsupported headers). Do not resubmit without modifying request
     */
    BAD_REQUEST, 
    /**
     * Http 415 
     */
    UNSUPPORTED_MEDIA_TYPE,
    /**
     * HTTP 400 caused by request document validation errors. Do
     * not resubmit without modifying request
     */
    VALIDATION_ERROR_PREFIX, 
    /**
     * HTTP 404 Item not found
     */
    NOT_FOUND,
    /**
     * HTTP 500 caused by uncategorized error. In
     * general, 500 represents an error that is
     * entirely unexpected and likely so rare it was
     * not anticipated during coding or encountered
     * during testing. If, in the course of
     * development, a recoverable error is identified
     * as regularly falling into this category, then it
     * needs to get handled and classified into the 4XX
     * range of errors not 5XX.
     */
    UNCAUGHT_ERROR, 
    /**
     * HTTP 403 Insufficient privilege or missing roles
     */
    FORBIDDEN, 

    /**
     * 400 error
     */
    SEARCH_SYNTAX_ERROR;
}
