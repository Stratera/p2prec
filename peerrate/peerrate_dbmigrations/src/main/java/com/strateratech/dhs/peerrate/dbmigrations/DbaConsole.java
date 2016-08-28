package com.strateratech.dhs.peerrate.dbmigrations;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Standalone program used to perform Database Migration.
 * 
 * This class uses System.out.println to get command line inputs.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public class DbaConsole {
    static {
        initLogging();
    }

    private static final Logger log = LoggerFactory.getLogger(DbaConsole.class);
    private Connection conn;

    private DataSource datasource;
    private String driverClassName = "oracle.jdbc.OracleDriver";
    private String dbUsername = "";
    private String dbPass = "";
    private String dbUrl = "";
    private String validationQuery = "";

    private InputStreamReader isr;

    private Scanner scanner;

    /**
     * Main Method for this class that does Database migration.
     * 
     * @param args
     */
    public static void main(String[] args) {
        DbaConsole console = null;
        try {
            console = new DbaConsole();
            console.collectConnectionDetails();
            console.constructConnection();
            console.testConnectionForDDLPermission();
            console.displayPendingMigrations();
            console.confirmAcceptanceOfPendingMigrations();
            console.performMigrations();
            log.info("db migrations complete. please review the dbmigration.log file to see what was done.");

        } catch (MigrationAbortException mae) {
            log.info(
                    "Migration aborted by user directive by either unusable inport or a \"N\" at the DBA Confirmation prompt, Reason="
                            + mae.getReason(),
                    mae);
            mae.printStackTrace();
        } catch (Exception e) {
            log.error("Unable to complete migrations:", e);
            e.printStackTrace();
        } finally {
            if (console != null) {
                console.cleanUp();
            }
        }

    }

    /**
     * Method used to initialize logging mechanism
     */
    private static void initLogging() {
        // TODO reset logging to local dba console file
//        LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
//
//        try {
//            JoranConfigurator configurator = new JoranConfigurator();
//            configurator.setContext(context);
//            context.reset();
//            configurator.doConfigure(getTargetDir() + configFileName);
//        } catch (JoranException e) {
//            // StatusPrinter will handle this
//        }
//        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

    }

    /**
     * Method to confirm acceptance of pending migrations.
     */
    private void confirmAcceptanceOfPendingMigrations() {
        try {
            String ans = "";
            do {
                System.out.print("Would you like to continue (Y/N): ");
                ans = scanner.nextLine();
            } while (StringUtils.isBlank(driverClassName) || !Arrays.asList("Y", "N", "y", "n").contains(ans));

            if (!ans.equalsIgnoreCase("Y")) {
                throw new MigrationAbortException("Negative response found (" + ans + ")");
            }
        } finally {
        }
    }

    /**
     * Method to test the connection for DDL Permission
     * 
     * @throws SQLException
     */
    private void testConnectionForDDLPermission() throws SQLException {
        String tableName = "test_" + new Date().getTime();
        String ddlCreateQuery = "create table " + tableName;
        String ddlDropQuery = "drop table " + tableName;
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(ddlCreateQuery);
            preparedStmt.execute();

            preparedStmt = conn.prepareStatement(ddlDropQuery);
            preparedStmt.execute();

        } finally {
            if (preparedStmt != null) {
                try {
                    preparedStmt.close();
                } catch (Exception e) {
                    log.error("Unable to close preparedStmt for DDL Test", e);
                }
            }
        }

    }

    /**
     * Method used to construct Database connection.
     * 
     * @throws SQLException
     */
    private void constructConnection() throws SQLException {
        PreparedStatement q = null;
        ResultSet rs = null;
        try {
            Properties connectionProps = new Properties();
            connectionProps.put("username", dbUsername);
            connectionProps.put("password", dbPass);
            connectionProps.put("driverClassName", driverClassName);
            connectionProps.put("url", dbUrl);
            datasource = BasicDataSourceFactory.createDataSource(connectionProps);
            conn = datasource.getConnection();
            log.info("Connected to database");
            q = conn.prepareStatement(validationQuery);
            rs = q.executeQuery();

        } catch (Exception e) {
            throw new SQLException("Exception occurred creating and testing connection", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log.error("Unable to close ResultSet", e);
                }
            }
            if (q != null) {
                try {
                    q.close();
                } catch (Exception e) {
                    log.error("Unable to close preparedStmt", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("Unable to close Connection", e);
                }
            }
        }
    }

    /**
     * Method used to collect connection details.
     */
    public void collectConnectionDetails() {
        try {
            isr = new InputStreamReader(System.in);
            scanner = new Scanner(isr);
            do {
                System.out.println("Please enter Driver Class [" + driverClassName + "]: ");
                driverClassName = scanner.nextLine();
            } while (StringUtils.isBlank(driverClassName));

            do {
                System.out.println("Please enter Username [" + dbUsername + "]: ");
                dbUsername = scanner.nextLine();
            } while (StringUtils.isBlank(dbUsername));

            do {
                System.out.println("Please enter Password [" + dbPass + "]: ");
                dbPass = scanner.nextLine();
            } while (StringUtils.isBlank(dbPass));

            do {
                System.out.println("Please enter JDBC Url [" + dbUrl + "]: ");
                dbUrl = scanner.nextLine();
            } while (StringUtils.isBlank(dbUrl));

            do {
                System.out.println("Please enter Validation Query [" + validationQuery + "]: ");
                validationQuery = scanner.nextLine();
            } while (StringUtils.isBlank(validationQuery));

        } finally {
        }
    }

    /**
     * Method to display pending migrations
     */
    public void displayPendingMigrations() {

        MigrationInfo[] migrationInfos = MigrationUtils.lookupPendingMigrations(datasource);
        if (migrationInfos != null && migrationInfos.length > 0) {
            log.info("Found the following migrations:");
            for (MigrationInfo info : migrationInfos) {
                log.info("Identified: " + info.getScript());
                System.out.println("         " + info.getScript() + " - " + info.getDescription());
            }
        } else {
            log.info("Found no migrations.  Aborting");
            throw new MigrationAbortException("No migrations found.");
        }
    }

    /**
     * Method to perform Migration
     */
    public void performMigrations() {
        MigrationUtils.performDbMigrations(datasource);

    }

    /**
     * Method to abort Migration
     */
    public void abortMigrations() {
        log.warn("Abort Requested");

    }

    /**
     * Method to close connection
     */
    public void cleanUp() {
        if (isr != null) {
            try {
                isr.close();
            } catch (Exception e) {
                log.error("Could not close Input Stream Reader for SysIn!", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                log.error("Could not close exception!", e);
            }
        }

    }
}
