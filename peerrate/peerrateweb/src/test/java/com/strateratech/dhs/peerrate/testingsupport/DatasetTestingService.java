package com.strateratech.dhs.peerrate.testingsupport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

/**
 * Spring manageable bean that provides DBUnit integration support. This class
 * is only to be used in TEST scope and no other. DBUnit is only available in
 * test scope and imports/dependencies need to be isolated to classes only on
 * test classpath for use with maven.
 * 
 * @author 2020
 * @date Oct 19, 2015
 * @version 1.0
 */
@Component
public class DatasetTestingService {
    private static final Logger log = LoggerFactory.getLogger(DatasetTestingService.class);

    /**
     * JPA Applications should never access the datasource/connection pool
     * directly That being said, applications using dbunit need to a connection
     * to load and unload unit test data.
     */
    @Inject
    private DataSource dataSource;
    private Connection conn;

    /**
     * Get a DBUnit connection object from the inject datasource!
     * 
     * @return IDatabaseConnection
     * @throws BeansException
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public IDatabaseConnection getConnection() throws BeansException, SQLException, DatabaseUnitException,
            FileNotFoundException, IOException {
        log.debug("Max Connections=" + ((BasicDataSource) dataSource).getMaxActive() + " ActiveConnections="
                + ((BasicDataSource) dataSource).getNumActive());
        if (conn == null) {
            conn = (dataSource).getConnection();
        }
        DatabaseConnection dbConn = new DatabaseConnection(conn);
        return dbConn;
    }

    /**
     * Loads individual dataset (relative to classpath root) into the tables
     * specified in the dataset files with associated field data
     * 
     * @param connection
     * @param classpathFile
     * @throws Exception
     */
    public void loadDataset(IDatabaseConnection connection, String classpathFile) throws Exception {
        IDataSet dataset = getDataSet(classpathFile);
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);

    }

    /**
     * This method loads the standard BASE state expected for the unit tests.
     * Individual tests may use different datasets as necessary but this method
     * reflects the "standard" data for tests.
     * 
     * @param conn
     * @throws Exception
     */
    public void loadAllDatasets(IDatabaseConnection conn) throws Exception {
        loadDataset(conn, "dbunit/department.xml");
        loadDataset(conn, "dbunit/user_profile.xml");
        loadDataset(conn, "dbunit/recognition.xml");

    }

    
    
    /**
     * This method "cleans" the dataset XML so nulls are properly "represented"
     * for the way that DBUnit expects them to be ("{null}")
     * 
     * @param classpathFile
     * @return
     * @throws Exception
     */
    private IDataSet getDataSet(String classpathFile) throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(classpathFile);
        IDataSet dataset = new FlatXmlDataSet(inputStream);
        ReplacementDataSet dataSet = new ReplacementDataSet(dataset);
        dataSet.addReplacementObject("{null}", null);
        return dataset;
    }

    /**
     * Walks over a list of files (relative to classpath as root) in order and
     * deletes the records from each table in order listed in the dataset
     * 
     * @param connection
     * @param filenames
     * @throws Exception
     */
    public void emptyTables(IDatabaseConnection connection, String... filenames) throws Exception {
        emptyTables(connection);
    }

    /**
     * This method calls a the above emptyTables() method with a specific
     * dataset file that is carefully crafted so that referential integrity is
     * acknowledged by the ordering of the table references listed. This method
     * is expected to be run at the end of every test where data is loaded into
     * test tables. NOTE: If new tables are added to the project, make sure you
     * add them to the dataset below. The order is very important. If the tables
     * are not listed in a way that honors referential integrity, then the
     * generated delete statements will fail and dirty data will be left behind
     * after the tests!!!! I cannot stress how important it is that this method
     * successfully complete after the test. Stability of unit tests is directly
     * dependent on how well these dataset files are maintained!
     * 
     * @param connection
     * @throws Exception
     */
    public void emptyTables(IDatabaseConnection connection) throws Exception {
        IDataSet dataset = getDataSet("dbunit/delete_tables_dataset.xml");
        DatabaseOperation.DELETE_ALL.execute(connection, dataset);
    }
}
