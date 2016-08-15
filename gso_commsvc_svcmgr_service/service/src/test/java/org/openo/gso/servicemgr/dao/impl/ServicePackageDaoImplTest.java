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
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;

/**
 * Test ServicePackageDaoImpl Class<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/2
 */
public class ServicePackageDaoImplTest {

    /**
     * DAO object.
     */
    ServicePackageDaoImpl servicePackageDao = new ServicePackageDaoImpl();;

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
        reader = Resources.getResourceAsReader("ServicePackage.sql");
        ScriptRunner runner = new ScriptRunner(conn);
        runner.runScript(reader);
        reader.close();

        servicePackageDao.setSession(session);
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
    public void testQueryAllMappings() throws ServiceException {
        List<ServicePackageMapping> serviceMappings = servicePackageDao.queryAllMappings();
        assertNotNull(serviceMappings);
    }

    /**
     * Fail to query when session is close.<br/>
     * 
     * @throws ServiceException session is null
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testQueryAllMappingsFail() throws ServiceException {
        servicePackageDao.getSession().close();
        servicePackageDao.queryAllMappings();
    }

    /**
     * Test case: object of ServicePackageMapping is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testInsertServicePackageMappingIsNull() throws ServiceException {
        servicePackageDao.insert(null);
    }

    /**
     * Test Succeed scene.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testInsert() throws ServiceException {
        ServicePackageMapping servicePackage = new ServicePackageMapping();
        servicePackage.setServiceDefId("12345");
        servicePackage.setServiceId("2");
        servicePackage.setTemplateId("123456");
        servicePackage.setTemplateName("gso");

        servicePackageDao.insert(servicePackage);

    }

    /**
     * Delete successfully.<br/>
     * 
     * @throws ServiceException IO or parameter wrong exception
     * @since GSO 0.5
     */
    @Test
    public void testDeleteOk() throws ServiceException {
        servicePackageDao.delete("1");
    }

    /**
     * The Service ID is null.<br/>
     * 
     * @throws ServiceException parameter wrong exception
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testDeleteFail() throws ServiceException {
        servicePackageDao.delete(null);
    }

    /**
     * Test getSession().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetSession() {
        assertNotNull(servicePackageDao.getSession());
    }
}
