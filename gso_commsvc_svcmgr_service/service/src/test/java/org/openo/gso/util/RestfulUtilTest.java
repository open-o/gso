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

package org.openo.gso.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.HttpRest;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;

import mockit.Mock;
import mockit.MockUp;

public class RestfulUtilTest {

    @Test
    public void testGetRemoteResponse() {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, "");
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        String params = "";
        
        Map<String, String> queryParamMap = new HashMap<String, String>();
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<HttpRest>() {

            @Mock
            public RestfulResponse get(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }

            @Mock
            public RestfulResponse put(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }

            @Mock
            public RestfulResponse post(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }

            @Mock
            public RestfulResponse delete(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }
        };
    }

}
