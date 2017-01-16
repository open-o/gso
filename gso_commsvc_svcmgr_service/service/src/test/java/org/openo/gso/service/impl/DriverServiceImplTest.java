/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

public class DriverServiceImplTest {

    @Test
    public void testCreateNs() {

        DriverServiceImpl impl = new DriverServiceImpl();
        impl.setSegmentType(CommonConstant.SegmentType.NFVO);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        impl.createNs(inputMap);
    }
    
    @Test
    public void testDeleteNs() {

        DriverServiceImpl impl = new DriverServiceImpl();
        impl.setSegmentType(CommonConstant.SegmentType.NFVO);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        impl.deleteNs(inputMap);
    }
    
    @Test
    public void testInstantiateNs() {
        DriverServiceImpl impl = new DriverServiceImpl();
        impl.setSegmentType(CommonConstant.SegmentType.NFVO);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        impl.instantiateNs(inputMap);

    }
    
    @Test
    public void testTerminateNs() {
        DriverServiceImpl impl = new DriverServiceImpl();
        impl.setSegmentType(CommonConstant.SegmentType.NFVO);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        impl.terminateNs(inputMap);

    }

    @Test
    public void testGetNsProgress() {
        DriverServiceImpl impl = new DriverServiceImpl();
        impl.setSegmentType(CommonConstant.SegmentType.NFVO);
        Map<String, Object> inputMap = new HashMap<String, Object>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        impl.getNsProgress("1", inputMap);

    }
    
    @Test
    public void testGetSvcTemplByNodeType() {
        DriverServiceImpl svcImpl = new DriverServiceImpl();
        svcImpl.setSegmentType(CommonConstant.SegmentType.NFVO);
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setServiceTemplateId("1");
        List<ServiceTemplate> list = new ArrayList<ServiceTemplate>();
        list.add(svcTmpl);
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(HttpStatus.SC_OK);
        rsp.setResponseJson(JsonUtil.marshal(list));
        new MockUp<RestfulUtil>() {
            
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
                    Map<String, String> queryParam) {
            return rsp;
            }
        };
        svcImpl.getSvcTmplByNodeType(CommonConstant.NodeType.NFV_POP_TYPE, "1.1.1.1:24");
    }

    @Test
    public void testSetSegmentType() {
        DriverServiceImpl svcImpl = new DriverServiceImpl();
        svcImpl.setSegmentType(CommonConstant.SegmentType.NFVO);
    }

}
