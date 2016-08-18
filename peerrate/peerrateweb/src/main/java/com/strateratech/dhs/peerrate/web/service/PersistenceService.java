package com.strateratech.dhs.peerrate.web.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.stereotype.Service;

/**
 * WARNING: This class makes the entire application dependant on HIBERNATE specifically as a 
 * JPA provider.  If we ever switch JPA providers, this class's methods will have to be updated.
 * @author 2020
 * @date May 27, 2016
 * @version 1.3.5
 *
 */
@Service("persistenceService")
public class PersistenceService {
	@Inject
	private EntityManager entityManager;
	 /**
	  * Database agnostic way (using hibernate dialect class) to get the next sequence value
	  * @param sequenceName
	  * @return Long representing the next sequence.  It should never return the same ID regardless of transaction rollback or concurrency
	  * @since May 27, 2016
	  */
    public Long getNextSequenceValue(final String sequenceName) {
            ReturningWork<Long> maxReturningWork = new ReturningWork<Long>() {
                @Override
                public Long execute(Connection connection) throws SQLException {
                    DialectResolver dialectResolver = new StandardDialectResolver();
                    Dialect dialect =  dialectResolver.resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    try {
                        preparedStatement = connection.prepareStatement( dialect.getSequenceNextValString(sequenceName));
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        return resultSet.getLong(1);
                    }catch (SQLException e) {
                        throw e;
                    } finally {
                        if(preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if(resultSet != null) {
                            resultSet.close();
                        }
                    }

                }
            };
            Long maxRecord = entityManager.unwrap(Session.class).doReturningWork(maxReturningWork);
            return maxRecord;
    }

}
