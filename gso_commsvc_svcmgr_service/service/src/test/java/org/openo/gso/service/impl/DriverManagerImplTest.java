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

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.model.drivermo.TerminateParams;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class DriverManagerImplTest {

    @Mocked
    IDriverService serviceInf;

    @Mocked
    IServiceSegmentDao serviceSegmentDao;

    HttpServletRequest httpRequest;

    @Test
    public void testTerminateService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);

        TerminateParams params = new TerminateParams();
        params.setNodeType("tosca.nodes.nfv.dc");
        Map<String, String> inputParameters = new HashMap<String, String>();
        inputParameters.put("serviceId", "1");
        inputParameters.put("tosca.nodes.nfv.dc.instanceId", "sub1");
        params.setInputParameters(inputParameters);
        final String str =
                "{\"nodeType\":\"tosca.nodes.nfv.dc\",\"inputParameters\":{\"tosca.nodes.nfv.dc.instanceId\":\"sub1\",\"serviceId\":\"1\"}}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(200);
        try {
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rsp;
                }
            };

            impl.terminateService(httpRequest);
        } catch(ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstantiateService() {

        final String str =
                "{\"nodeType\":\"tosca.nodes.nfv.pop\",\"stNodeParam\":[{\"tosca.nodes.nfv.pop.param1\":\"value1\"}]}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(202);
        String tempStr = "{\"jobId\":\"1\",\"responseDescriptor\":{\"status\":\"error\",\"progress\":\"100\"}}";
        rsp.setResponseJson(tempStr);
        try {
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            new MockUp<RestfulUtil>() {

                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                        Map<String, String> queryParam) {
                    return rsp;
                }
            };
            DriverManagerImpl impl = new DriverManagerImpl();
            impl.instantiateService(httpRequest);
        } catch(ServiceException e) {

        }
    }

}
