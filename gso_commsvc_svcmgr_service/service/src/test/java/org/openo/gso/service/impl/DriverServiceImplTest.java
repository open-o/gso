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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.ResponseDescriptor;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;

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
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
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
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
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
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
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

        
        DriverServiceImpl getImpl = new DriverServiceImpl();
        getImpl.setNodeType("tosca.nodes.nfv.dc");
        NsProgressStatus progress = new NsProgressStatus();
        progress.setJobId("jobId");
        ResponseDescriptor rspDescriptor = new ResponseDescriptor();
        rspDescriptor.setStatus("finished");
        rspDescriptor.setProgress(100);
        progress.setRspDescriptor(rspDescriptor);
        String body = "";
        try {
            body = JsonUtil.marshal(progress);
        } catch(ServiceException e2) {
            // TODO Auto-generated catch block
        }
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setResponseJson(body);
        try {
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
                    return rsp;
                }
            };
            getImpl.getNsProgress("jobId");
        } catch(ServiceException e) {
            // TODO Auto-generated catch block

        }
        
        DriverServiceImpl getImplExp = new DriverServiceImpl();
        getImplExp.setNodeType("tosca.nodes.nfv.dc");
        NsProgressStatus progressExp = new NsProgressStatus();
        progressExp.setJobId("jobId");
        ResponseDescriptor rspDescriptorExp = new ResponseDescriptor();
        rspDescriptorExp.setStatus("finished");
        rspDescriptorExp.setProgress(100);
        progressExp.setRspDescriptor(rspDescriptorExp);
        String bodyExp = "";
        try {
            bodyExp = JsonUtil.marshal(progressExp);
        } catch(ServiceException e1) {
            // TODO Auto-generated catch block
        }
        final RestfulResponse rspExp = new RestfulResponse();
        rspExp.setResponseJson(bodyExp);
        try {
            new MockUp<JsonUtil>()
            {
                @Mock
                public String unMarshal(String jsonstr, Class<NsProgressStatus> type) throws IOException
                {
                    throw new IOException();
                }
            };
            getImplExp.getNsProgress("jobId");
        } catch(ServiceException e) {
            // TODO Auto-generated catch block

        }
        
    }
    
    

    @Test
    public void testInstantiateNS() {
        
        DriverServiceImpl instImpl = new DriverServiceImpl();
        instImpl.setNodeType("tosca.nodes.nfv.dc");
        Map<String, String> mapParams = new HashMap<String, String>();
        final RestfulResponse rsp = new RestfulResponse();
        String str = "{\"jobId\":\"1\"}";
        rsp.setResponseJson(str);
        try {
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
                    return rsp;
                }
            };
            instImpl.instantiateNS("instanceId", mapParams);
        } catch(ServiceException e) {
            // TODO Auto-generated catch block

        }
        
        DriverServiceImpl instImplExp = new DriverServiceImpl();
        instImplExp.setNodeType("tosca.nodes.nfv.dc");
        Map<String, String> mapParamsExp = new HashMap<String, String>();
        final RestfulResponse rspExp = new RestfulResponse();
        String strExp = "{\"jobId\":\"1\"}";
        rspExp.setResponseJson(strExp);
        try {
            new MockUp<JsonUtil>()
            {
                @Mock
                public String marshal(Object srcObj) throws IOException
                {
                    throw new IOException();
                }
            };
            instImplExp.instantiateNS("instanceId", mapParamsExp);
        } catch(ServiceException e) {

        }
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
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
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
