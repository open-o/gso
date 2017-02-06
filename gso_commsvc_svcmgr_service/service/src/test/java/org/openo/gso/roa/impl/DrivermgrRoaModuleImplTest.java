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

package org.openo.gso.roa.impl;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.dao.impl.ServiceSegmentDaoImpl;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.restproxy.impl.CatalogProxyImpl;
import org.openo.gso.service.impl.DriverManagerImpl;
import org.openo.gso.util.RestfulUtil;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import net.sf.json.JSONObject;

public class DrivermgrRoaModuleImplTest {

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";
    
    @Mocked
    HttpServletRequest servletReq;

    ServiceSegmentDaoImpl serviceSegmentDao;

    DriverManagerImpl driverMgr;
    
    DrivermgrRoaModuleImpl impl;
    
    CatalogProxyImpl catalogProxy;

    @Before
    public void init() {
        impl = new DrivermgrRoaModuleImpl();
        driverMgr = new DriverManagerImpl();
        catalogProxy = new CatalogProxyImpl();
        serviceSegmentDao = new ServiceSegmentDaoImpl();
        impl.setDriverMgr(driverMgr);
        driverMgr.setCatalogProxy(catalogProxy);
        driverMgr.setServiceSegmentDao(serviceSegmentDao);
    }

    @Test
    public void testGetDriver() {
        assertNotNull(impl.getDriverMgr());
    }

    @Test
    public void testSetDriver() {
        impl.setDriverMgr(driverMgr);
    }

    @Test
    public void testCreateNFVONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "createNfvoNsReq.json");
        // get service template
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setId("id");
        svcTmpl.setServiceTemplateId("svcTmplId");
        new MockUp<CatalogProxyImpl>() {
            @Mock
            public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost){
                return svcTmpl;
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "createNfvoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // insert data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void insertSegment(ServiceSegmentModel serviceSegment) {
                // do nothing
            }
            @Mock
            public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.createNfvoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertSame(1, Integer.valueOf(obj.getString("nsInstanceId")));
    }
    
    @Test
    public void testDeleteNFVONs() {
        //impl.deleteNfvoNs(servletReq);
    }
    
    @Test
    public void testTerminateNFVONs() {
       //impl.terminateNfvoNs(servletReq);
    }
    
    @Test
    public void testInstantiateNFVONs() {
        String nsInstanceId = "1";
        //impl.instantiateNfvoNs(nsInstanceId, servletReq);
    }
    
    @Test
    public void testQueryNFVONsProgress() {
        String jobId = "1";
        //impl.queryNfvoJobStatus(jobId);
    }
    
    @Test
    public void testCreateSDNONs() {
     // get request
        mockGetRequestBody(FILE_PATH + "createSdnoNsReq.json");
        // get service template
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setId("id");
        svcTmpl.setServiceTemplateId("svcTmplId");
        new MockUp<CatalogProxyImpl>() {
            @Mock
            public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost){
                return svcTmpl;
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "createSdnoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // insert data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void insertSegment(ServiceSegmentModel serviceSegment) {
                // do nothing
            }
            @Mock
            public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.createSdnoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertSame(1, Integer.valueOf(obj.getString("nsInstanceId")));
    }
    
    @Test
    public void testDeleteSDNONs() {
        //impl.deleteSdnoNs(servletReq);
    }
    
    @Test
    public void testTerminateSDNONs() {
        //impl.terminateSdnoNs(servletReq);
    }
    
    @Test
    public void testInstantiateSDNONs() {
        String nsInstanceId = "1";
        //impl.instantiateSdnoNs(nsInstanceId, servletReq);
    }
    
    @Test
    public void testQuerySDNONsProgress() {
        String jobId = "1";
        //impl.querySdnoJobStatus(jobId);
    }
    
    @Test
    public void testCreateGSONs() {
        //impl.createGsoNs(servletReq);
    }
    
    @Test
    public void testDeleteGSONs() {
        //impl.deleteGsoNs(servletReq);
    }
    
    @Test
    public void testQueryGSONsProgress() {
        String jobId = "1";
        //impl.queryGsoJobStatus(jobId);
    }
    
    /**
     * Mock to get request body.<br/>
     * 
     * @param file json file path.
     * @since GSO 0.5
     */
    private void mockGetRequestBody(final String file) {
        new MockUp<RestUtils>() {

            @Mock
            public String getRequestBody(HttpServletRequest request) {
                return getJsonString(file);
            }
        };
    }
    
    /**
     * Mock to get rsp<br>
     * 
     * @param rsp restful response
     * @since  GSO 0.5
     */
    private void mockGetRestfulRsp(final RestfulResponse rsp) {
        new MockUp<RestfulUtil>() {
            @Mock
            public RestfulResponse getRemoteResponse(String url, String methodType,
                    RestfulParametes restfulParametes, RestfulOptions options) {
                return rsp;
            }
        };
    }

    /**
     * Get json string from file.<br/>
     * 
     * @param file the path of file
     * @return json string
     * @throws IOException when fail to read
     * @since GSO 0.5
     */
    private String getJsonString(final String file) {
        if(StringUtils.isEmpty(file)) {
            return "";
        }

        String json = null;
        try {
            FileInputStream fileStream = new FileInputStream(new File(file));
            json = IOUtils.toString(fileStream);
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }

        return json;
    }

}
