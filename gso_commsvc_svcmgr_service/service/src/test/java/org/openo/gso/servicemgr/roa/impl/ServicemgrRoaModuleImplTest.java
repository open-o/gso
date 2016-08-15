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

package org.openo.gso.servicemgr.roa.impl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.dao.impl.ServiceModelDaoImpl;
import org.openo.gso.servicemgr.dao.impl.ServicePackageDaoImpl;
import org.openo.gso.servicemgr.dao.impl.SubServiceDaoImpl;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;
import org.openo.gso.servicemgr.service.impl.ServiceManagerImpl;

/**
 * Test ServicemgrRoaModuleImpl class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServicemgrRoaModuleImplTest {

    /**
     * Service ROA.
     */
    ServicemgrRoaModuleImpl serviceRoa = new ServicemgrRoaModuleImpl();

    /**
     * Service manager.
     */
    ServiceManagerImpl serviceManager = new ServiceManagerImpl();

    /**
     * Service model DAO.
     */
    ServiceModelDaoImpl serviceDao = new ServiceModelDaoImpl();

    /**
     * Sub-Service DAO.
     */
    SubServiceDaoImpl subServiceDao = new SubServiceDaoImpl();

    /**
     * Package DAO.
     */
    ServicePackageDaoImpl packageDao = new ServicePackageDaoImpl();

    /**
     * SQL session.
     */
    SqlSession session;

    /**
     * Http request.
     */
    HttpServletRequest httpRequest;

    /**
     * Before executing UT, start sql.<br/>
     * 
     * @since GSO 0.5
     */
    @Before
    public void start() throws IOException, SQLException {
        prepareSQL();
        serviceDao.setSession(session);
        subServiceDao.setSession(session);
        packageDao.setSession(session);
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.setSubServiceDao(subServiceDao);
        serviceRoa.setServicemanager(serviceManager);
    }

    /**
     * Prepare SQL environment<br/>
     * 
     * @throws IOException
     * @throws SQLException
     * @since GSO 0.5
     */
    private void prepareSQL() throws IOException, SQLException {
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

        reader = Resources.getResourceAsReader("SubService.sql");
        runner.runScript(reader);

        reader.close();
    }

    /**
     * After executing UT, close session<br/>
     * 
     * @since GSO 0.5
     */
    @After
    public void stop() {
        session.close();
    }

    /**
     * Test create service.<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testCreateService() throws ServiceException {
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
        serviceRoa.createService(serviceModel, httpRequest);
    }

    /**
     * Test delete service.<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testTeleteService() throws ServiceException {
        serviceRoa.deleteService("1", httpRequest);
    }

    /**
     * Test method getAllInstances.<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testGetAllInstances() throws ServiceException {
        serviceRoa.getAllInstances(httpRequest);
    }

    /**
     * Test method getTopoSequence().<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testGetTopoSequence() throws ServiceException {
        serviceRoa.getTopoSequence("1", httpRequest);
    }

    /**
     * Test method getServiceManager().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetServiceManager() {
        assertNotNull(serviceRoa.getServiceManager());
    }
}
