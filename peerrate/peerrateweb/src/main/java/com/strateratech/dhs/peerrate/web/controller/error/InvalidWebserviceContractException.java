package com.strateratech.dhs.peerrate.web.controller.error;

/**
 * handles if multiple version support within this application encounters a
 * version that is not supported within our xsd version factory
 * 
 * @version 1.1
 * @author 2020
 * @date Oct 16, 2015
 */
public class InvalidWebserviceContractException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * no arg constructor
     */
    public InvalidWebserviceContractException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidWebserviceContractException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public InvalidWebserviceContractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public InvalidWebserviceContractException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public InvalidWebserviceContractException(Throwable cause) {
        super(cause);
    }

}
