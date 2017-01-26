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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.service.inf.IDriverService;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;

public class DriverManagerImplTest {

    @Mocked
    IDriverService serviceInf;

    @Mocked
    IServiceSegmentDao serviceSegmentDao;
    
    @Mocked
    IServiceModelDao serviceModelDao;
    
    @Mocked
    IServicePackageDao servicePackageDao;
    
    @Mocked
    ICatalogProxy catalogProxy;

    HttpServletRequest httpRequest;
    
    @Test
    public void test() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceInf(serviceInf);
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceModelDao(serviceModelDao);
        impl.setServicePackageDao(servicePackageDao);
        impl.setCatalogProxy(catalogProxy);
        impl.getServiceInf();
        impl.getServiceSegmentDao();
        impl.getServiceModelDao();
        impl.getServicePackageDao();
        impl.getCatalogProxy();
    }

    @Test
    public void testCreateService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";
        
        final ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setServiceTemplateId("1");
        svcTmpl.setId("2");
        
        final RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(200);
        restRsp.setResponseJson("{\"nsInstanceId\":\"1\"}");
        try {
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            
            new NonStrictExpectations() {
                {
                    
                    serviceInf.getSvcTmplByNodeType(anyString, anyString);
                    returns(svcTmpl);
                }
            }; 
            
            new NonStrictExpectations() {
                {
                    
                    serviceInf.createNs((Map<String, Object>)any);
                    returns(restRsp);
                }
            }; 

            impl.createNs(httpRequest, CommonConstant.SegmentType.NFVO);
        } catch(ApplicationException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDeleteService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";

        final RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(200);
        try{
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            new NonStrictExpectations() {
                {
                    
                    serviceInf.deleteNs((Map<String, Object>)any);
                    returns(restRsp);
                }
            };
            impl.deleteNs(httpRequest, CommonConstant.SegmentType.NFVO);
        } catch(ApplicationException e) {
            e.printStackTrace();
        }
        
            
        
    }

    @Test
    public void testInstantiateService() {

        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(200);
        String tempStr = "{\"jobId\":\"1\"}";
        rsp.setResponseJson(tempStr);
        try {
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            
            new NonStrictExpectations() {
                {
                    
                    serviceInf.instantiateNs((Map<String, Object>)any);
                    returns(rsp);
                }
            };
                 
            impl.instantiateNs("1", httpRequest, CommonConstant.SegmentType.NFVO);
        } catch(ApplicationException e) {

        }
    }
    
    @Test
    public void testTerminateService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(200);
        String tempStr = "{\"jobId\":\"1\"}";
        rsp.setResponseJson(tempStr);
        try {
            new MockUp<RestUtils>() {

                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            
            new NonStrictExpectations() {
                {
                    
                    serviceInf.terminateNs((Map<String, Object>)any);
                    returns(rsp);
                }
            };
                 
            impl.terminateNs(httpRequest, CommonConstant.SegmentType.NFVO);
        } catch(ApplicationException e) {

        }
    }
    
    @Test
    public void testGetNsProgress() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        ServiceSegmentOperation svcSegOper = new ServiceSegmentOperation("1", CommonConstant.SegmentType.NFVO, CommonConstant.OperationType.CREATE);
        try {
            new NonStrictExpectations() {
                {
                    serviceSegmentDao.querySegmentOperByJobIdAndType(anyString, anyString);
                    returns(svcSegOper);
                }
            };
            impl.getNsProgress("j1", CommonConstant.SegmentType.NFVO);
        } catch(ApplicationException e) {

        }
    }
    
    @Test
    public void testCreateGsoNs() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";
        try {
            new MockUp<RestUtils>() {
                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            impl.createGsoNs(httpRequest, CommonConstant.SegmentType.NFVO);
        } catch (ApplicationException e) {
            //handle exception
        }
            
        
    }
    
    @Test
    public void testDeleteGsoNs() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        final String str =
                "{\"nodeTemplateName\":\"POP\",\"segments\":\"[{\"serviceId\":\"1\",\"subServiceName\":\"name\",\"subServiceDesc\":\"desc\",\"domainHost\":\"10.11.11.1:80\",\"nodeTemplateName\":\"POP\",\"nodeType\":\"tosca.nodes.nfv.NS.POP_NS\"}]\"}";
        try {
            new MockUp<RestUtils>() {
                @Mock
                public String getRequestBody(HttpServletRequest request) {
                    return str;
                }
            };
            impl.deleteGsoNs(httpRequest, CommonConstant.SegmentType.NFVO);
        } catch (ApplicationException e) {
            //handle exception
        }
    }
    
    @Test
    public void testQueryGsoNsProgress() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceInf(serviceInf);
        impl.getGsoNsProgress("j2", CommonConstant.SegmentType.NFVO);
    }

}
