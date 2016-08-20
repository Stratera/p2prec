package com.strateratech.dhs.peerrate.web.service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.strateratech.dhs.peerrate.testingsupport.DatasetTestingService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-test.xml" })
public class PersistenceServiceTest {
    @Inject
    private DatasetTestingService datasetTestingService;

    @Inject
    private PersistenceService persistenceService;

    @Transactional
    @Test
    public void test() {
        Long id = persistenceService.getNextSequenceValue("test_seq1");
        Assert.assertEquals((Long)1000L, (Long)id);
        id = persistenceService.getNextSequenceValue("test_seq1");
        Assert.assertEquals((Long)1001L, (Long)id);
    
    }

    @Before
    public void setUp() throws Exception {
        IDatabaseConnection conn = datasetTestingService.getConnection();
        datasetTestingService.emptyTables(conn);
        datasetTestingService.loadAllDatasets(conn);

    }

}
