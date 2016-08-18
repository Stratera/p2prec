package com.strateratech.dhs.peerrate.web.controller.error;

/**
 * This is a general Exception used by the rest layer any time there is a
 * failure to serialize data into a view model where the view model is one of
 * many implementations (strategy pattern)
 * 
 * @author 2020
 * @date Nov 10, 2015 11:05:27 AM
 * @version
 */
public class SerializationStrategyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SerializationStrategyException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SerializationStrategyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public SerializationStrategyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public SerializationStrategyException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SerializationStrategyException(Throwable cause) {
        super(cause);
    }

}
