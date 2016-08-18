package com.strateratech.dhs.peerrate.dbmigrations;

/**
 * US5097 - Data Model - FINAL Scheme Navigator
 * 
 * Custom exposition that can occur during DB Migration
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public class MigrationAbortException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String reason;

    /**
     * Method that returns the reason for MigrationAbortException
     * 
     * @return String reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Method that set the reason for MigrationAbortException
     * 
     * @param message
     */
    public MigrationAbortException(String message) {
        super();

        this.reason = message;

    }

    public MigrationAbortException() {
        super();
    }

    /**
     * Parameterized Constructor for SchemeHierarchyId Class
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public MigrationAbortException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.reason = message;
    }

    /**
     * Parameterized Constructor for SchemeHierarchyId Class
     * 
     * @param message
     * @param cause
     */
    public MigrationAbortException(String message, Throwable cause) {
        super(message, cause);
        this.reason = message;
    }

    /**
     * Parameterized Constructor for SchemeHierarchyId Class
     * 
     * @param cause
     */
    public MigrationAbortException(Throwable cause) {
        super(cause);
    }

}
