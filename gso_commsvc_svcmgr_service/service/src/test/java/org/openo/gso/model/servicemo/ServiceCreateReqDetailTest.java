/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.model.servicemo;

import org.junit.Test;


/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/1/24
 */
public class ServiceCreateReqDetailTest {

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#getName()}.
     */
    @Test
    public void testGetName() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.getName();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.setName("name");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.getDescription();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#setDescription(java.lang.String)}.
     */
    @Test
    public void testSetDescription() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.setDescription("description");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#getServiceDefId()}.
     */
    @Test
    public void testGetServiceDefId() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.getServiceDefId();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#setServiceDefId(java.lang.String)}.
     */
    @Test
    public void testSetServiceDefId() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.setServiceDefId("serviceDefId");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#getTemplateId()}.
     */
    @Test
    public void testGetTemplateId() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.getTemplateId();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#setTemplateId(java.lang.String)}.
     */
    @Test
    public void testSetTemplateId() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.setTemplateId("templateId");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.getParameters();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceCreateReqDetail#setParameters(org.openo.gso.model.servicemo.ServiceSegmentReq)}.
     */
    @Test
    public void testSetParameters() {
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        ServiceSegmentReq parameters = new ServiceSegmentReq();
        service.setParameters(parameters);
    }

}
