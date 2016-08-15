/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicemgr.dao.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
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
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;

/**
 * Test SubServiceDaoTmpl Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/2
 */
public class SubServiceDaoImplTest {

    /**
     * DAO object.
     */
    SubServiceDaoImpl subServiceDao = new SubServiceDaoImpl();;

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
        reader = Resources.getResourceAsReader("SubService.sql");
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
    public void testQuerySubServices() throws ServiceException {
        subServiceDao.setSession(session);
        List<SubServiceModel> subServices = subServiceDao.querySubServices("1");
        assertNotNull(subServices);
    }

    /**
     * Fail to query when service ID is null.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQuerySubServicesFail1() throws ServiceException {
        subServiceDao.querySubServices(null);
    }

    /**
     * Fail to query when session is null.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQuerySubServicesFail2() throws ServiceException {
        subServiceDao.querySubServices("1");
    }

    /**
     * Test case: List<SubServiceModel> is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testBatchInsertIsNull1() throws ServiceException {
        subServiceDao.batchInsert(null);
    }

    /**
     * Test case: session is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testBatchInsertIsNull2() throws ServiceException {
        List<SubServiceModel> subServices = new LinkedList<SubServiceModel>();
        SubServiceModel subService = new SubServiceModel();
        subServices.add(subService);
        subServiceDao.batchInsert(subServices);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testInsert() throws ServiceException {
        SubServiceModel subService = new SubServiceModel();
        subService.setNodeType("tosaca.nfv.node.POP");
        subService.setOwner("nfv");
        subService.setServiceId("2");
        subService.setSubServiceId("12345");
        subService.setSubServiceName("POP service");
        subService.setTemplateId("12345");
        subService.setTopoSeqNumber(1);

        List<SubServiceModel> subServices = new LinkedList<SubServiceModel>();
        subServices.add(subService);

        subServiceDao.setSession(session);
        subServiceDao.batchInsert(subServices);
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
        subServiceDao.setSession(session);
        subServiceDao.delete(serviceId);
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
        subServiceDao.delete(serviceId);
    }

    /**
     * Test getSession().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetSession() {
        subServiceDao.setSession(session);
        assertNotNull(subServiceDao.getSession());
    }
}
