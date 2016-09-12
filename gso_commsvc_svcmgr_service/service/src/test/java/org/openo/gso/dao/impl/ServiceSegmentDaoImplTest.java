/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.dao.impl.ServiceSegmentDaoImpl;
import org.openo.gso.model.servicemo.ServiceSegmentModel;

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
    ServiceSegmentDaoImpl serviceSegmentDao = new ServiceSegmentDaoImpl();;

    /**
     * Sql session.
     */
    SqlSession session;

    @Before
    public void setup() throws IOException, SQLException {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();
        reader = Resources.getResourceAsReader("ServiceSegment.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.runScript(reader);
        reader.close();
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
     * @throws ServiceException
     * @since GSO 0.5
     */
    @Test
    public void testQueryServiceSegments() throws ServiceException {
        serviceSegmentDao.setSession(session);
        List<ServiceSegmentModel> serviceSegments = serviceSegmentDao.queryServiceSegments("1");
        assertNotNull(serviceSegments);
    }

    /**
     * Fail to query when service ID is null.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQueryServiceSegmentsFail1() throws ServiceException {
        serviceSegmentDao.queryServiceSegments(null);
    }

    /**
     * Fail to query when session is null.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQueryServiceSegmentsFail2() throws ServiceException {
        serviceSegmentDao.queryServiceSegments("1");
    }

    /**
     * Test case: ServiceSegmentModel is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testInsertIsNull1() throws ServiceException {
        serviceSegmentDao.insert(null);
    }

    /**
     * Test case: session is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testInsertIsNull2() throws ServiceException {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegmentDao.insert(serviceSegment);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testInsert() throws ServiceException {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setNodeType("tosaca.nfv.node.POP");
        serviceSegment.setServiceId("2");
        serviceSegment.setServiceSegmentId("12345");
        serviceSegment.setServiceSegmentName("POP service");
        serviceSegment.setTemplateId("12345");
        serviceSegment.setTopoSeqNumber(1);
        serviceSegment.setStatus("createSucceed");

        serviceSegmentDao.setSession(session);
        serviceSegmentDao.insert(serviceSegment);
    }

    /**
     * Delete successfully.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOk() throws ServiceException {
        String serviceId = "1";
        serviceSegmentDao.setSession(session);
        serviceSegmentDao.delete(serviceId);
    }

    /**
     * The Service ID is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testDeleteFail() throws ServiceException {
        String serviceId = null;
        serviceSegmentDao.delete(serviceId);
    }

    /**
     * Test getSession().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetSession() {
        serviceSegmentDao.setSession(session);
        assertNotNull(serviceSegmentDao.getSession());
    }
}
