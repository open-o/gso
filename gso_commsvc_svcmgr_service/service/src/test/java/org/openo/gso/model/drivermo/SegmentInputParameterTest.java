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
package org.openo.gso.model.drivermo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.model.servicemo.ServiceSegmentReq;


public class SegmentInputParameterTest {
    
    SegmentInputParameter input = new SegmentInputParameter();

    @Test
    public void testGetServiceId() {
        input.getServiceId();
    }

    @Test
    public void testSetServiceId() {
        input.setServiceId("1");
    }
    
    @Test
    public void testGetSubServiceId() {
        input.getSubServiceId();
    }

    @Test
    public void testSetSubServiceId() {
        input.setSubServiceId("1");
    }


    @Test
    public void testGetSubServiceName() {
        input.getSubServiceName();
    }

    @Test
    public void testSetSubServiceName() {
        input.setSubServiceName("subServiceName");
    }

    @Test
    public void testGetSubServiceDesc() {
        input.getSubServiceDesc();
    }

    @Test
    public void testSetSubServiceDesc() {
        input.setSubServiceDesc("subServiceDesc");
    }

    @Test
    public void testGetDomainHost() {
        input.getDomainHost();
    }

    @Test
    public void testSetDomainHost() {
        input.setDomainHost("10.111.1.1:24");
    }

    @Test
    public void testGetNodeTemplateName() {
        input.getNodeTemplateName();
    }

    @Test
    public void testSetNodeTemplateName() {
        input.setNodeTemplateName("nodeTemplateName");
    }

    @Test
    public void testGetNodeType() {
        input.getNodeType();
    }

    @Test
    public void testSetNodeType() {
        input.setNodeType(CommonConstant.NodeType.NFV_DC_TYPE);
    }

    @Test
    public void testGetParameters() {
        List<ServiceSegmentReq> segments = input.getSegments();
    }

    @Test
    public void testSetSegments() {
        List<ServiceSegmentReq> segments = new ArrayList<ServiceSegmentReq>();
        input.setSegments(segments);
    }

    @Test
    public void testGetNsParameters() {
        NsParameters nsParameters = input.getNsParameters();
    }

    @Test
    public void testSetNsParameters() {
        NsParameters nsParameters = new NsParameters();
        input.setNsParameters(nsParameters);
    }

}
