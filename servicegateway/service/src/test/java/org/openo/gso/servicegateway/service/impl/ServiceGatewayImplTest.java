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

package org.openo.gso.servicegateway.service.impl;


import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicegateway.service.impl.ServiceGatewayImpl;

/**
 * Test ServiceGatewayImpl Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServiceGatewayImplTest {

    /**
     * Service manager.
     */
    ServiceGatewayImpl serviceManager = new ServiceGatewayImpl();

    /**
     * Http request.
     */
    HttpServletRequest httpRequest;

    @Before
    public void start() {
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
        serviceManager.deleteService(null, "", httpRequest);
    }

}
