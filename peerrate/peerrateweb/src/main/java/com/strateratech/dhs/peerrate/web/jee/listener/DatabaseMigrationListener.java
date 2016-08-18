package com.strateratech.dhs.peerrate.web.jee.listener;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strateratech.dhs.peerrate.web.utils.JndiUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Using Flyway, connect to the database and install All necessary database
 * Migrations Migration files are expected to be in
 * src/main/resources/db/migration Migration files are basic sql files, named
 * appropriately. Please review the Flyway documentation for details about that.
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
@SuppressFBWarnings(value = "BAD_PRACTICE", justification = "If there is any exeption in this listener, We abort all"
        + " database activity, log as much as we can and kill the JVM so that the user can evaluate what"
        + " database activity was performed and what needs to be done manually to unwind it.")
public class DatabaseMigrationListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMigrationListener.class);
    private static final Long CONVERT_TO_MILLIS = 1000L;
    private static final Integer DEFAULT_SLEEP = 60;
    private static final String DB_MIGRATION_PROPERTIES_FILENAME = "migration.properties";
    private boolean doMigration = Boolean.FALSE;

    private int secondsToSleepBeforePerformingMigrations = DEFAULT_SLEEP;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // do nothing
    }

    /**
     * Method to initialize the context.
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
     *      ServletContextEvent)
     * 
     * 
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.debug("\n\n\n\n\n---------------Starting db migration listener");
        initProperties();
        try {
            if (doMigration) {
                DataSource datasource = (DataSource) JndiUtils
                        .getJndiProperty(servletContextEvent.getServletContext().getInitParameter("datasourceName"));
                Flyway flyway = new Flyway();
                flyway.setDataSource(datasource);
                flyway.setBaselineOnMigrate(true);
                MigrationInfoService infoService = flyway.info();
                MigrationInfo[] infos = infoService.pending();

                if (infos != null && infos.length > 0) {
                    log.info("Flyway Identified the following {} database migrations for install.", infos.length);
                    for (MigrationInfo info : infos) {
                        log.info(info.getScript());
                    }

                    try {
                        log.info("Sleeping {} seconds to give you a chance to kill this app if needed.",
                                secondsToSleepBeforePerformingMigrations);
                        Thread.sleep(CONVERT_TO_MILLIS * secondsToSleepBeforePerformingMigrations);
                        log.info("You didn't stop this process. So now I am installing all the above migrations");

                        flyway.migrate();
                    } catch (InterruptedException ie) {
                        log.info("Interrupt Request caught to stop the db migrations above listed from being run.", ie);
                        throw new RuntimeException("User elected to stop process in order to prevent db migrations from running.",
                                ie);
                    }

                    log.info("DB Migration analysis is complete");
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error.  Shutting app down!", e);

            System.exit(1);
        }
    }

    /**
     * Method to initialize properties.
     */
    private void initProperties() {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DB_MIGRATION_PROPERTIES_FILENAME);
            if (is != null) {
                Properties props = new Properties();
                props.load(is);

                try {
                    secondsToSleepBeforePerformingMigrations = Integer
                            .parseInt(props.getProperty("seconds_to_sleep_before_performing_migrations", "60"));
                } catch (Exception e1) {
                    log.warn("Encountered exception e parsing seconds to pause.  Using default", e1);
                }
                if (!StringUtils.isBlank(props.getProperty("do_migration"))) {
                    doMigration = Boolean.parseBoolean(props.getProperty("do_migration", "false").trim());
                }
            }
        } catch (Exception e) {
            log.warn("Exception encountered overriding default migration properties.  Moving on with convention.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
