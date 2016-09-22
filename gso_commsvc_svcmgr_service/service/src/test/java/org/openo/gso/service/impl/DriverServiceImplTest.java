/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.util.RestfulUtil;

import mockit.Mock;
import mockit.MockUp;

public class DriverServiceImplTest {

    DriverServiceImpl svcImpl = new DriverServiceImpl();

    @Test
    public void testDelete() {

        final RestfulResponse rspErr = new RestfulResponse();
        rspErr.setStatus(202);
        String strErr = "{\"jobId\":\"1\",\"responseDescriptor\":{\"status\":\"error\",\"progress\":\"100\"}}";
        rspErr.setResponseJson(strErr);
        try {
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rspErr;
                }
            };
            svcImpl.delete("tosca.nodes.nfv.dc", "instanceId");
            svcImpl.delete("tosca.nodes.sdn.overlayvpn", "instanceId");
            svcImpl.delete("tosca.nodes.nfv.pop", "");

        } catch(ServiceException e) {

        }

        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(202);
        String str = "{\"jobId\":\"1\",\"responseDescriptor\":{\"status\":\"finished\",\"progress\":\"100\"}}";
        rsp.setResponseJson(str);
        try {
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rsp;
                }
            };
            svcImpl.delete("tosca.nodes.nfv.dc", "instanceId");
            svcImpl.delete("tosca.nodes.sdn.overlayvpn", "instanceId");
            svcImpl.delete("tosca.nodes.nfv.pop", "");
            svcImpl.delete("", "instanceId");

        } catch(ServiceException e) {

        }

        final RestfulResponse rspExp = new RestfulResponse();
        rsp.setStatus(500);
        String strExp = "{\"jobId\":\"1\",\"responseDescriptor\":{\"status\":\"error\",\"progress\":\"100\"}}";
        rsp.setResponseJson(strExp);
        try {
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rspExp;
                }
            };
            svcImpl.delete("tosca.nodes.nfv.dc", "instanceId");

        } catch(ServiceException e) {

        }

        try {
            svcImpl.delete("", "instanceId");

        } catch(ServiceException e) {

        }
    }

    @Test
    public void testGetNsProgress() {

    }

    @Test
    public void testInstantiateNS() {

    }

    @Test
    public void testCreateNS() {

        DriverServiceImpl nsImpl = new DriverServiceImpl();
        nsImpl.setNodeType("tosca.nodes.nfv.dc");
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setServiceTemplateId("id1");
        svcTmpl.setServiceTemplateName("service1");
        Map<String, String> paramMap = new HashMap<String, String>();
        final RestfulResponse rsp = new RestfulResponse();
        String responseString = "{\"nsInstanceId\":\"1\"}";
        rsp.setResponseJson(responseString);
        try {
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rsp;
                }
            };
            nsImpl.createNS(svcTmpl.getServiceTemplateId(), paramMap);
        } catch(ServiceException e) {

        }
    }

    @Test
    public void testSetNodeType() {
        svcImpl.setNodeType(CommonConstant.NodeType.NFV_DC_TYPE);
    }

}
