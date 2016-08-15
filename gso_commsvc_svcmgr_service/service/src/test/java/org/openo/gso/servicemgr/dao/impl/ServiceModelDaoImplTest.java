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
import static org.junit.Assert.assertNull;

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
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;

/**
 * Test ServiceModelDaoImpl Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/2
 */
public class ServiceModelDaoImplTest {

    /**
     * ServiceMolde DAO object.
     */
    ServiceModelDaoImpl serviceModelDao = new ServiceModelDaoImpl();

    /**
     * Package DAO object.
     */
    ServicePackageDaoImpl packageDao = new ServicePackageDaoImpl();

    /**
     * Sql session
     */
    SqlSession session;

    /**
     * Before starting to test UT, prepare database data.<br/>
     * 
     * @throws IOException
     * @throws SQLException
     * @since GSO 0.5
     */
    @Before
    public void setup() throws IOException, SQLException {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();

        ScriptRunner runner = new ScriptRunner(conn);
        reader = Resources.getResourceAsReader("ServiceModel.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServicePackage.sql");
        runner.runScript(reader);

        reader.close();

        serviceModelDao.setSession(session);
        packageDao.setSession(session);
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
     * Query service successfully.<br/>
     * 
     * @throws ServiceException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServices() throws ServiceException {
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        assertNotNull(services);
    }

    /**
     * Query service that is null.<br/>
     * <p>
     * There is no service instance.
     * </p>
     * 
     * @throws ServiceException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServicesNull1() throws ServiceException {
        serviceModelDao.delete("1");
        serviceModelDao.delete("3");
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        assertNull(services);
    }

    /**
     * Query service that is null.<br/>
     * <p>
     * There is no mapping relation.
     * </p>
     * 
     * @throws ServiceException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServicesNull2() throws ServiceException {
        packageDao.delete("1");
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        assertNotNull(services);
    }

    /**
     * Fail to query service when session is close.<br/>
     * 
     * @throws ServiceException session is null
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQueryAllServicesFail() throws ServiceException {
        serviceModelDao.getSession().close();
        serviceModelDao.queryAllServices();
    }

    /**
     * Test case: object of ServiceModel is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testInsertServiceModelIsNull() throws ServiceException {
        serviceModelDao.insert(null);
    }

    /**
     * Test case: object of ServicePackageMapping is null.<br/>
     * 
     * @throws ServiceException ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testInsertPackageIsNull() throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceId("2");
        serviceModel.setName("testSucceed");
        serviceModel.setDescription("des");
        serviceModel.setActiveStatus("active");
        serviceModel.setStatus("createdSucceed");
        serviceModel.setCreator("tester");
        serviceModel.setCreateAt(1234567890);
        serviceModelDao.insert(serviceModel);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testInsert() throws ServiceException {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceId("2");
        serviceModel.setName("testSucceed");
        serviceModel.setDescription("des");
        serviceModel.setActiveStatus("active");
        serviceModel.setStatus("createdSucceed");
        serviceModel.setCreator("tester");
        serviceModel.setCreateAt(1234567890);

        ServicePackageMapping servicePackage = new ServicePackageMapping();
        servicePackage.setServiceDefId("12345");
        servicePackage.setServiceId("2");
        servicePackage.setTemplateId("123456");
        servicePackage.setTemplateName("gso");

        serviceModel.setServicePackage(servicePackage);
        serviceModelDao.insert(serviceModel);
    }

    /**
     * Delete successfully.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOk() throws ServiceException {
        serviceModelDao.setSession(session);
        serviceModelDao.delete("1");
    }

    /**
     * The Service ID is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testDeleteFail() throws ServiceException {
        serviceModelDao.delete(null);
    }

    /**
     * Test getSession().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetSession() {
        assertNotNull(serviceModelDao.getSession());
    }
}
