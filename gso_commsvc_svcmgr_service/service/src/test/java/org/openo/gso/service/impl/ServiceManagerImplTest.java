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

package org.openo.gso.service.impl;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.dao.impl.ServiceModelDaoImpl;
import org.openo.gso.dao.impl.ServiceSegmentDaoImpl;

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
    ServiceManagerImpl serviceManager;

    /**
     * Service model DAO.
     */
    ServiceModelDaoImpl serviceDao;

    /**
     * Sub-Service DAO.
     */
    ServiceSegmentDaoImpl serviceSegmentDao;

    /**
     * Http request.
     */
    HttpServletRequest httpRequest;

    @Before
    public void start() {
        serviceManager = new ServiceManagerImpl();
        serviceDao = new ServiceModelDaoImpl();
        serviceSegmentDao = new ServiceSegmentDaoImpl();
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.setServiceSegmentDao(serviceSegmentDao);
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testGetAllInstancesFail() {
        serviceManager.getAllInstances();
    }

    /**
     * Invalid parameter.<br/>
     * 
     * @since GSO 0.5
     */
    @Test(expected = ApplicationException.class)
    public void testGetServiceSegmentssFail() {
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.getServiceSegments("1");
    }

    /**
     * Test getting DAO.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testDAO() {
        assertNotNull(serviceManager.getServiceModelDao());
        assertNotNull(serviceManager.getServiceSegmentDao());
    }
}
