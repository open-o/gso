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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
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

        } catch(ApplicationException e) {

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

        } catch(ApplicationException e) {

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

        } catch(ApplicationException e) {

        }

        try {

        } catch(ApplicationException e) {

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
        nsImpl.setDomain("sdno");
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setServiceTemplateId("id1");
        svcTmpl.setServiceTemplateName("service1");
        Map<String, Object> paramMap = new HashMap<String, Object>();
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
            nsImpl.createNs(paramMap);
        } catch(ApplicationException e) {

        }
    }

    @Test
    public void testSetNodeType() {
        svcImpl.setDomain(CommonConstant.Domain.NFVO);
    }

}
