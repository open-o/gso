/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.dao.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;

import mockit.Mock;
import mockit.MockUp;

/**
 * Test ServiceSegmentDaoImpl Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/2
 */
public class ServiceSegmentDaoImplTest {

    /**
     * DAO object.
     */
    ServiceSegmentDaoImpl serviceSegmentDao = new ServiceSegmentDaoImpl();

    /**
     * Database session handler.
     */
    DatabaseSessionHandler dbSessionHandler = new DatabaseSessionHandler();

    /**
     * Sql session.
     */
    SqlSession session;

    @Before
    public void setup() throws IOException, SQLException {
        serviceSegmentDao.setDbSessionHandler(dbSessionHandler);

        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();
        reader = Resources.getResourceAsReader("ServiceSegment.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.runScript(reader);
        reader.close();

        // mock session
        mockSession();
    }

    /**
     * After executing UT, close session.<br/>
     * 
     * @since GSO 0.5
     */
    @After
    public void stop() {
        session.close();
    }

    /**
     * Query successfully.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test
    public void testQueryServiceSegments() throws ApplicationException {
        List<ServiceSegmentModel> serviceSegments = serviceSegmentDao.queryServiceSegments("1");
        assertNotNull(serviceSegments);
    }

    /**
     * Fail to query when service ID is null.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testQueryServiceSegmentsFail1() throws ApplicationException {
        serviceSegmentDao.queryServiceSegments(null);
    }

    /**
     * Fail to query when session is null.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testQueryServiceSegmentsFail2() throws ApplicationException {
        serviceSegmentDao.getDbSessionHandler().getSqlSession().close();
        serviceSegmentDao.queryServiceSegments("1");
    }

    /**
     * Test case: ServiceSegmentModel is null.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testInsertIsNull1() {
        serviceSegmentDao.insertSegment(null);
    }

    /**
     * Test case: session is null.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testInsertIsNull2() {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegmentDao.insertSegment(serviceSegment);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testInsert() {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setNodeType("tosaca.nfv.node.POP");
        serviceSegment.setServiceId("2");
        serviceSegment.setServiceSegmentId("12345");
        serviceSegment.setServiceSegmentName("POP service");
        serviceSegment.setTemplateId("12345");
        serviceSegment.setTopoSeqNumber(1);

        serviceSegmentDao.insertSegment(serviceSegment);
    }
    
    /**
     * Delete successfully.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOk() {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setServiceId("1");
        serviceSegment.setServiceSegmentId("12345");
        serviceSegmentDao.delete(serviceSegment);
    }

    /**
     * The Service ID is null.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testDeleteFail() {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegmentDao.delete(serviceSegment);
    }

    /**
     * Mock database session.<br/>
     * 
     * @since GSO 0.5
     */
    private void mockSession() {
        new MockUp<DatabaseSessionHandler>() {

            @Mock
            public SqlSession getSqlSession() {
                return session;
            }
        };
    }
    
   
    /**
     * Insert Service Segment Operation<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testInsertSegmentOper() {
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setServiceSegmentId("1");
        segOper.setServiceSegmentType(CommonConstant.SegmentType.NFVO);
        serviceSegmentDao.insertSegmentOper(segOper);
    }
    
    /**
     * Update Service Segment Operation<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testUpdateSegmentOper() {
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setServiceSegmentId("1");
        segOper.setServiceSegmentType(CommonConstant.SegmentType.NFVO);
        segOper.setJobId("1");
        serviceSegmentDao.updateSegmentOper(segOper);
    }
    
    /**
     * Query Segment By Id And Type<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testQuerySegmentByIdAndType() {
        String segmentId = "1";
        serviceSegmentDao.queryServiceSegmentByIdAndType(segmentId, CommonConstant.SegmentType.NFVO);
    }
    
    /**
     * Delete Segment By Id And Type<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteSegmentByIdAndType() {
        ServiceSegmentModel seg = new ServiceSegmentModel();
        seg.setServiceSegmentId("1");
        seg.setServiceSegmentType(CommonConstant.SegmentType.NFVO);
        serviceSegmentDao.deleteSegmentByIdAndType(seg);
    }
    
    /**
     * Query Segment Oper By Job Id And Type<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testQuerySegmentOperByJobIdAndType() {
        String jobId = "1";
        String segmentType = CommonConstant.SegmentType.NFVO;
        serviceSegmentDao.querySegmentOperByJobIdAndType(jobId, segmentType);
    }
}
