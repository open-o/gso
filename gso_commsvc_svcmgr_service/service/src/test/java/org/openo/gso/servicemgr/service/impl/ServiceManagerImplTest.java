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

package org.openo.gso.servicemgr.service.impl;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.dao.impl.ServiceModelDaoImpl;
import org.openo.gso.servicemgr.dao.impl.SubServiceDaoImpl;

/**
 * Test ServiceManagerImpl Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServiceManagerImplTest {

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
     * Http request.
     */
    HttpServletRequest httpRequest;

    @Before
    public void start() {
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.setSubServiceDao(subServiceDao);
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @throws ServiceException when parameter is wrong.
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testCreateServiceFail() throws ServiceException {
        serviceManager.createService(null, httpRequest);
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @throws ServiceException when parameter is wrong.
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testDeleteServiceFail() throws ServiceException {
        serviceManager.deleteService(null, httpRequest);
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @throws ServiceException when operating database.
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testGetAllInstancesFail() throws ServiceException {
        serviceManager.getAllInstances();
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @throws ServiceException when operating database.
     * @since GSO 0.5
     */
    @Test(expected = ServiceException.class)
    public void testGetSubServicesFail() throws ServiceException {
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.getSubServices("1");
    }

    /**
     * Test getting DAO.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testDAO() {
        assertNotNull(serviceManager.getServiceModelDao());
        assertNotNull(serviceManager.getSubServiceDao());
    }
}
