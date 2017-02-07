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
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
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

    /**
     * initial<br>
     * 
     * @since  GSO 0.5
     */
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

    /**
     * test get driver manager<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testGetDriver() {
        assertNotNull(impl.getDriverMgr());
    }

    /**
     * test set driver manager<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testSetDriver() {
        impl.setDriverMgr(driverMgr);
    }

    /**
     * test create nfvo network service<br>
     * 
     * @since  GSO 0.5
     */
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
        Assert.assertEquals(null, "1", obj.getString("nsInstanceId"));
    }
    
    /**
     * test delete nfvo network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteNFVONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "deleteNfvoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "deleteNfvoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // update segment operation and delete segment
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
            @Mock
            public void deleteSegmentByIdAndType(ServiceSegmentModel serviceSegment) {
                // do nothing
            }
        };
        Response rsp = impl.deleteNfvoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("result"));
        Assert.assertEquals(null, "success", subObj.getString("status"));
    }
    
    /**
     * test terminate nfvo network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testTerminateNFVONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "terminateNfvoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "terminateNfvoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // update segment operation and delete segment
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.terminateNfvoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertEquals(null, "1", obj.getString("jobId"));
    }
    
    /**
     * test instantiate nfvo network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testInstantiateNFVONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "instantiateNfvoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "instantiateNfvoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // insert data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.instantiateNfvoNs("1", servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertEquals(null, "1", obj.getString("jobId"));
    }
    
    /**
     * test query nfvo network service progress<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testQueryNFVONsProgress() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg1");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "queryNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        Response rsp = impl.queryNfvoJobStatus(jobId);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("responseDescriptor"));
        Assert.assertEquals(null, "100", subObj.getString("progress"));
        Assert.assertEquals(null, "finished", subObj.getString("status"));
        
    }
    
    /**
     * test create sdno network service<br>
     * 
     * @since  GSO 0.5
     */
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
        Assert.assertEquals(null, "1", obj.getString("nsInstanceId"));
    }
    
    /**
     * test delete sdno network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteSDNONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "deleteSdnoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "deleteSdnoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // update segment operation and delete segment
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
            @Mock
            public void deleteSegmentByIdAndType(ServiceSegmentModel serviceSegment) {
                // do nothing
            }
        };
        Response rsp = impl.deleteSdnoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("result"));
        Assert.assertEquals(null, "success", subObj.getString("status"));
    }
    
    /**
     * test terminate sdno network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testTerminateSDNONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "terminateSdnoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "terminateSdnoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // update segment operation and delete segment
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.terminateSdnoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertEquals(null, "1", obj.getString("jobId"));
    }
    
    /**
     * test instantiate sdno network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testInstantiateSDNONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "instantiateSdnoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "instantiateSdnoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // insert data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.instantiateSdnoNs("1", servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertEquals(null, "1", obj.getString("jobId"));
    }
    
    /**
     * test query sdno network service progress<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testQuerySDNONsProgress() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg2");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "queryNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        Response rsp = impl.querySdnoJobStatus(jobId);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("responseDescriptor"));
        Assert.assertEquals(null, "100", subObj.getString("progress"));
        Assert.assertEquals(null, "finished", subObj.getString("status"));
    }
    
    /**
     * test create gso network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testCreateGSONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "createGsoNsReq.json");
        // get service template
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setCsarId("csarId");
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
        restRsp.setResponseJson(getJsonString(FILE_PATH + "createGsoNsRsp.json"));
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
        Response rsp = impl.createGsoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("service"));
        Assert.assertEquals(null, "o1", subObj.getString("operationId"));
    }
    
    /**
     * test create gso network service<br>
     * fail1: service template is null
     * @since  GSO 0.5
     */
    @Test
    public void testCreateGSONsFail1() {
        // get request
        mockGetRequestBody(FILE_PATH + "createGsoNsReq.json");
        // get service template null
        new MockUp<CatalogProxyImpl>() {
            @Mock
            public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost){
                return null;
            }
        };
        try{
            Response rsp = impl.createGsoNs(servletReq);
        } catch(ApplicationException e) {
            Assert.assertNotNull(e);
        }
    }
    
    /**
     * test create gso network service<br>
     * fail2: response content is invalid
     * @since  GSO 0.5
     */
    @Test
    public void testCreateGSONsFail2() {
        // get request
        mockGetRequestBody(FILE_PATH + "createGsoNsReq.json");
        // get service template
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setCsarId("csarId");
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
        restRsp.setResponseJson(getJsonString(FILE_PATH + "createGsoNsFailedRsp.json"));
        mockGetRestfulRsp(restRsp);
        try{
            Response rsp = impl.createGsoNs(servletReq);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
    }
    
    /**
     * test create gso network service<br>
     * fail3: response status is 400, bad request
     * @since  GSO 0.5
     */
    @Test
    public void testCreateGSONsFail3() {
        // get request
        mockGetRequestBody(FILE_PATH + "createGsoNsReq.json");
        // get service template
        ServiceTemplate svcTmpl = new ServiceTemplate();
        svcTmpl.setCsarId("csarId");
        svcTmpl.setServiceTemplateId("svcTmplId");
        new MockUp<CatalogProxyImpl>() {
            @Mock
            public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost){
                return svcTmpl;
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_BAD_REQUEST);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "createGsoNsRsp.json"));
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
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        try{
            Response rsp = impl.createGsoNs(servletReq);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
    }
    
        
    /**
     * test delete gso network service<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteGSONs() {
        // get request
        mockGetRequestBody(FILE_PATH + "deleteGsoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "deleteGsoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // insert data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        Response rsp = impl.deleteGsoNs(servletReq);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        Assert.assertEquals(null, "o1", obj.getString("operationId"));
    }
    
    /**
     * test delete gso network service<br>
     * fail1: response status is 400, bad request
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteGSONsFail1() {
        // get request
        mockGetRequestBody(FILE_PATH + "deleteGsoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_BAD_REQUEST);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "deleteGsoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        // update data
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        try{
            Response rsp = impl.deleteGsoNs(servletReq);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
        
    }
    
    /**
     * test delete gso network service<br>
     * fail2: response content is invalid
     * @since  GSO 0.5
     */
    @Test
    public void testDeleteGSONsFail2() {
        // get request
        mockGetRequestBody(FILE_PATH + "deleteGsoNsReq.json");
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "deleteGsoNsFailedRsp.json"));
        mockGetRestfulRsp(restRsp);
        try{
            Response rsp = impl.deleteGsoNs(servletReq);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
        
    }
    

    
    /**
     * test query gso network service progress<br>
     * 
     * @since  GSO 0.5
     */
    @Test
    public void testQueryGSONsProgress() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg1");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "queryGsoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        Response rsp = impl.queryGsoJobStatus(jobId);
        JSONObject obj = JSONObject.fromObject(rsp.getEntity());
        JSONObject subObj = JSONObject.fromObject(obj.get("operation"));
        Assert.assertEquals(null, "100", subObj.getString("progress"));
        Assert.assertEquals(null, "finished", subObj.getString("result"));
    }
    
    /**
     * test query gso network service progress<br>
     * fail1: fail to operate db
     * @since  GSO 0.5
     */
    @Test
    public void testQueryGSONsProgressFail1() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg1");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        mockGetRestfulRspException();
        try{
            Response rsp = impl.queryGsoJobStatus(jobId);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
    }
    
    /**
     * test query gso network service progress<br>
     * fail2: response status is 400, bad request
     * @since  GSO 0.5
     */
    @Test
    public void testQueryGSONsProgressFail2() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg1");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_BAD_REQUEST);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "queryGsoNsRsp.json"));
        mockGetRestfulRsp(restRsp);
        try{
            Response rsp = impl.queryGsoJobStatus(jobId);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
    }
    
    /**
     * test query gso network service progress<br>
     * fail3: response result is error
     * @since  GSO 0.5
     */
    @Test
    public void testQueryGSONsProgressFail3() {
        String jobId = "1";
        // get data
        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceSegmentId("seg1");
        ServiceSegmentModel segment = new ServiceSegmentModel();
        segment.setDomainHost("10.100.1.1:80");
        new MockUp<ServiceSegmentDaoImpl>() {
            @Mock
            public ServiceSegmentOperation querySegmentOperByJobIdAndType(String jobId, String segmentType) {
                return segOper;
            }
            @Mock
            public ServiceSegmentModel queryServiceSegmentByIdAndType(String segmentId, String segmentType) {
                return segment;
            }
            @Mock
            public void updateSegmentOper(ServiceSegmentOperation svcSegmentOper) {
                // do nothing
            }
        };
        // get response
        RestfulResponse restRsp = new RestfulResponse();
        restRsp.setStatus(HttpStatus.SC_OK);
        restRsp.setResponseJson(getJsonString(FILE_PATH + "queryGsoNsErrorRsp.json"));
        mockGetRestfulRsp(restRsp);
        try{
            Response rsp = impl.queryGsoJobStatus(jobId);
        } catch (ApplicationException e) {
            Assert.assertNotNull(e);
        }
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
     * Mock to get rsp throw new application exception<br>
     * 
     * @since  GSO 0.5
     */
    private void mockGetRestfulRspException() {
        new MockUp<RestfulUtil>() {
            @Mock
            public RestfulResponse getRemoteResponse(String url, String methodType,
                    RestfulParametes restfulParametes, RestfulOptions options) {
                throw new ApplicationException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "failed");
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
