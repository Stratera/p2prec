package com.strateratech.dhs.peerrate.dbmigrations;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;

/**
 * 
 * Migration Utils Class
 * 
 * TODO: refactor the db migration listener in the webapp to make sure it uses
 * common code (DRY)
 * 
 * @author 2020
 * @version 1.0
 * @date: 08/4/2015
 *
 */
public final class MigrationUtils {

    /**
     * Method that performs the DB Migrations
     * 
     * @param datasource
     */
    public static void performDbMigrations(DataSource datasource) {
        Flyway flyway = new Flyway();

        flyway.setDataSource(datasource);
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
        return;
    }

    /**
     * Lookup Method for Pending Migrations
     * 
     * @param datasource
     * @return MigrationInfo Array Object
     */
    public static MigrationInfo[] lookupPendingMigrations(DataSource datasource) {
        Flyway flyway = new Flyway();

        flyway.setDataSource(datasource);
        flyway.setBaselineOnMigrate(true);
        MigrationInfoService infoService = flyway.info();
        MigrationInfo[] infos = infoService.pending();
        return infos;
    }

}
