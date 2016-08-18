package com.strateratech.dhs.peerrate.web.controller.error;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Controller Exception thrown when layered validator encounters additional validation errors beyond
 * normal JSR303 annotations on rest contract objects.  This exception is specifically thrown by
 * controller methods to trigger normal exception handling/response mapping in the 
 * @see gov.uspto.pe2e.cpc.ipc.rest.web.controller.error.GlobalControllerExceptionHandler
 * 
 * Note: this class is only required because spring does not allow customm validators to be layered
 * for a given controller method* 
 * 
 * 
 * @version 1.4
 * @author 2020
 * @date  7/13/2016
 */
public class CustomValidationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<Pair<String,List<Object>>> errors;

    /**
     * no arg constructor
     */
    public CustomValidationException() {
        super();
    }
    

    public CustomValidationException(List<Pair<String, List<Object>>> errors) {
		super();
		this.errors = errors;
	}


	/**
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public CustomValidationException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public CustomValidationException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public CustomValidationException(Throwable cause) {
        super(cause);
    }


	/**
	 * @return the errors
	 */
	public List<Pair<String, List<Object>>> getErrors() {
		return errors;
	}
    
    

}
