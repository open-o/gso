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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.springframework.util.CollectionUtils;

import mockit.Mock;
import mockit.MockUp;

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
     * Database session handler.
     */
    DatabaseSessionHandler dbSessionHandler = new DatabaseSessionHandler();

    /**
     * Before starting to test UT, prepare database data.<br/>
     * 
     * @throws IOException
     * @throws SQLException
     * @since GSO 0.5
     */
    @Before
    public void setup() throws IOException, SQLException {
        serviceModelDao.setDbSessionHandler(dbSessionHandler);
        packageDao.setDbSessionHandler(dbSessionHandler);

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

        reader = Resources.getResourceAsReader("ServiceParameter.sql");
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
     * Query service successfully.<br/>
     * 
     * @throws ApplicationException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServices() throws ApplicationException {
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        assertNotNull(services);
    }

    /**
     * Query service that is null.<br/>
     * <p>
     * There is no service instance.
     * </p>
     * 
     * @throws ApplicationException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServicesNull1() throws ApplicationException {
        serviceModelDao.delete("1");
        serviceModelDao.delete("3");
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        Assert.assertTrue(CollectionUtils.isEmpty(services));
    }

    /**
     * Query service that is null.<br/>
     * <p>
     * There is no mapping relation.
     * </p>
     * 
     * @throws ApplicationException database exception
     * @since GSO 0.5
     */
    @Test
    public void testQueryAllServicesNull2() throws ApplicationException {
        packageDao.delete("1");
        List<ServiceModel> services = serviceModelDao.queryAllServices();
        assertNotNull(services);
    }

    /**
     * Fail to query service when session is close.<br/>
     * 
     * @throws ApplicationException session is null
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testQueryAllServicesFail() throws ApplicationException {
        serviceModelDao.getDbSessionHandler().getSqlSession().close();
        serviceModelDao.queryAllServices();
    }

    /**
     * Test case: object of ServiceModel is null.<br/>
     * 
     * @throws ApplicationException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testInsertServiceModelIsNull() throws ApplicationException {
        serviceModelDao.insert(null);
    }

    /**
     * Test case: object of ServicePackageMapping is null.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testInsertPackageIsNull() {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceId("2");
        serviceModel.setName("testSucceed");
        serviceModel.setDescription("des");
        serviceModel.setActiveStatus("active");
        serviceModel.setStatus("createdSucceed");
        serviceModel.setCreator("tester");
        serviceModel.setCreateAt(Long.valueOf(1234567890));
        serviceModelDao.insert(serviceModel);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @throws ApplicationException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testInsert() throws ApplicationException {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceId("2");
        serviceModel.setName("testSucceed");
        serviceModel.setDescription("des");
        serviceModel.setActiveStatus("active");
        serviceModel.setStatus("createdSucceed");
        serviceModel.setCreator("tester");
        serviceModel.setCreateAt(Long.valueOf(1234567890));

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
     * @throws ApplicationException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOk() throws ApplicationException {
        serviceModelDao.delete("1");
    }

    /**
     * The Service ID is null.<br/>
     * 
     * @throws ApplicationException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testDeleteFail() throws ApplicationException {
        serviceModelDao.delete(null);
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
}
