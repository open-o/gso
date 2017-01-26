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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017年1月24日
 */
public class ServiceSegmentReqTest {

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#getDomainHost()}.
     */
    @Test
    public void testGetDomainHost() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.getDomainHost();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#setDomainHost(java.lang.String)}.
     */
    @Test
    public void testSetDomainHost() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.setDomainHost("1.1.1.1:24");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#getNodeTemplateName()}.
     */
    @Test
    public void testGetNodeTemplateName() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.getNodeTemplateName();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#setNodeTemplateName(java.lang.String)}.
     */
    @Test
    public void testSetNodeTemplateName() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.setNodeTemplateName("POP1");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#getNodeType()}.
     */
    @Test
    public void testGetNodeType() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.getNodeType();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#setNodeType(java.lang.String)}.
     */
    @Test
    public void testSetNodeType() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.setNodeType("tosca.nodes.nfv.POP");
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#getSegments()}.
     */
    @Test
    public void testGetSegments() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.getSegments();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#setSegments(java.util.List)}.
     */
    @Test
    public void testSetSegments() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        List<ServiceSegmentReq> segments = new ArrayList<ServiceSegmentReq>();
        req.setSegments(segments);
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#getAdditionalParamForNs()}.
     */
    @Test
    public void testGetAdditionalParamForNs() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        req.getAdditionalParamForNs();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceSegmentReq#setAdditionalParamForNs(java.util.Map)}.
     */
    @Test
    public void testSetAdditionalParamForNs() {
        ServiceSegmentReq req = new ServiceSegmentReq();
        Map<String, String> additionalParamForNs = new HashMap<String, String>();
        req.setAdditionalParamForNs(additionalParamForNs);
    }

}
