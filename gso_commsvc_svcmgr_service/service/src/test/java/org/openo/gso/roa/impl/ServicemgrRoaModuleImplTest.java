/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.roa.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.dao.impl.InventoryDaoImpl;
import org.openo.gso.dao.impl.ServiceModelDaoImpl;
import org.openo.gso.dao.impl.ServiceOperDaoImpl;
import org.openo.gso.dao.impl.ServicePackageDaoImpl;
import org.openo.gso.dao.impl.ServiceSegmentDaoImpl;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.restproxy.impl.CatalogProxyImpl;
import org.openo.gso.restproxy.impl.WorkflowProxyImpl;
import org.openo.gso.service.impl.OperationManager;
import org.openo.gso.service.impl.ServiceManagerImpl;
import org.openo.gso.util.http.HttpUtil;
import org.openo.gso.util.http.ResponseUtils;
import org.openo.gso.util.json.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * Test ServicemgrRoaModuleImpl class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServicemgrRoaModuleImplTest {

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";

    /**
     * Service ROA.
     */
    ServicemgrRoaModuleImpl serviceRoa = new ServicemgrRoaModuleImpl();

    /**
     * Service manager.
     */
    ServiceManagerImpl serviceManager = new ServiceManagerImpl();

    /**
     * Service model DAO.
     */
    ServiceModelDaoImpl serviceDao = new ServiceModelDaoImpl();

    /**
     * Service segment DAO.
     */
    ServiceSegmentDaoImpl serviceSegmentDao = new ServiceSegmentDaoImpl();

    /**
     * Package DAO.
     */
    ServicePackageDaoImpl packageDao = new ServicePackageDaoImpl();

    /**
     * Catalog proxy.
     */
    CatalogProxyImpl catalogProxy = new CatalogProxyImpl();

    /**
     * SQL session.
     */
    SqlSession session;

    /**
     * Http request.
     */
    HttpServletRequest httpRequest;

    /**
     * Rest response.
     */
    RestfulResponse responseSuccess = new RestfulResponse();

    /**
     * workflow proxy.
     */
    WorkflowProxyImpl workflowProxy = new WorkflowProxyImpl();

    /**
     * Inventory DAO.
     */
    InventoryDaoImpl inventoryDao = new InventoryDaoImpl();

    /**
     * Database session handler.
     */
    DatabaseSessionHandler dbSessionHandler = new DatabaseSessionHandler();

    /**
     * Operation manager.
     */
    OperationManager operationManager = new OperationManager();

    /**
     * Service operation DAO.
     */
    ServiceOperDaoImpl serviceOperDao = new ServiceOperDaoImpl();

    /**
     * Before executing UT, start sql.<br/>
     * 
     * @since GSO 0.5
     */
    @Before
    public void start() throws IOException, SQLException {

        // set session handler
        serviceDao.setDbSessionHandler(dbSessionHandler);
        serviceSegmentDao.setDbSessionHandler(dbSessionHandler);
        packageDao.setDbSessionHandler(dbSessionHandler);
        inventoryDao.setInvSessionHandler(dbSessionHandler);
        serviceOperDao.setDbSessionHandler(dbSessionHandler);

        prepareSQL();

        operationManager.setServiceOperDao(serviceOperDao);
        serviceManager.setOperationManager(operationManager);
        serviceManager.setServiceModelDao(serviceDao);
        serviceManager.setServiceSegmentDao(serviceSegmentDao);
        serviceManager.setServicePackageDao(packageDao);
        serviceManager.setCatalogProxy(catalogProxy);
        serviceManager.setWorkflowProxy(workflowProxy);
        serviceManager.setInventoryDao(inventoryDao);
        serviceRoa.setServicemanager(serviceManager);
        responseSuccess.setStatus(HttpCode.RESPOND_OK);
    }

    /**
     * Prepare SQL environment<br/>
     * 
     * @throws IOException
     * @throws SQLException
     * @since GSO 0.5
     */
    private void prepareSQL() throws IOException, SQLException {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        session = sqlSessionFactory.openSession();
        Connection conn = session.getConnection();

        ScriptRunner runner = new ScriptRunner(conn);

        reader = Resources.getResourceAsReader("ServiceModel.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServicePackage.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServiceSegment.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("ServiceParameter.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("inventory.sql");
        runner.runScript(reader);

        reader = Resources.getResourceAsReader("Operation.sql");
        runner.runScript(reader);

        reader.close();

        // mock session
        mockSession();
    }

    /**
     * After executing UT, close session<br/>
     * 
     * @since GSO 0.5
     */
    @After
    public void stop() {
        session.close();
    }

    /**
     * Test to create service successfully.<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testCreateServiceSuccess() throws ApplicationException {
        // mock request body
        mockGetRequestBody(FILE_PATH + "createServiceInstance.json");

        // mock get catalog parameters
        new MockUp<CatalogProxyImpl>() {

            @Mock
            public List<CatalogParameterModel> getParamsByTemplateId(String templateId, HttpServletRequest request)
                    throws ApplicationException {
                return ResponseUtils.getDataModelFromRsp(getJsonString(FILE_PATH + "getTemplateParamters.json"),
                        "inputs", CatalogParameterModel.class);
            }

        };

        // mock get catalog plans
        RestfulResponse responsePlan = new RestfulResponse();
        responsePlan.setResponseJson(getJsonString(FILE_PATH + "getPlans.json"));
        responsePlan.setStatus(HttpCode.RESPOND_OK);
        mockGet(responsePlan);

        // mock start workflow bpel workflow
        mockPost(responseSuccess);

        Response response = serviceRoa.createService(httpRequest);
        assertEquals(HttpCode.RESPOND_ACCEPTED, response.getStatus());
    }

    /**
     * Test to create service without segments successfully.<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testCreateServiceWithoutSegSuccess() throws ApplicationException {
        // mock request body
        mockGetRequestBody(FILE_PATH + "createServiceInstanceWithoutSegs.json");

        // mock get catalog parameters
        new MockUp<CatalogProxyImpl>() {

            @Mock
            public List<CatalogParameterModel> getParamsByTemplateId(String templateId, HttpServletRequest request)
                    throws ApplicationException {
                return ResponseUtils.getDataModelFromRsp(getJsonString(FILE_PATH + "getTemplateParamters.json"),
                        "inputs", CatalogParameterModel.class);
            }

        };

        // mock get catalog plans
        RestfulResponse responsePlan = new RestfulResponse();
        responsePlan.setResponseJson(getJsonString(FILE_PATH + "getPlans.json"));
        responsePlan.setStatus(HttpCode.RESPOND_OK);
        mockGet(responsePlan);

        // mock start workflow bpel workflow
        mockPost(responseSuccess);

        Response response = serviceRoa.createService(httpRequest);
        assertEquals(HttpCode.RESPOND_ACCEPTED, response.getStatus());
    }

    /**
     * Test to create service successfully.<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testCreateServiceSegmentSuccess() throws ApplicationException {
        // mock request body
        mockGetRequestBody(FILE_PATH + "createServiceSegment.json");

        // mock get template nodes
        new MockUp<CatalogProxyImpl>() {

            @Mock
            List<NodeTemplateModel> getNodeTemplate(String templateId, HttpServletRequest request)
                    throws ApplicationException {
                return ResponseUtils.getDataModelFromRspList(getJsonString(FILE_PATH + "getTemplateNodes.json"),
                        new TypeReference<List<NodeTemplateModel>>() {});
            }

        };

        serviceRoa.createServiceSegment(httpRequest);
    }

    /**
     * Test delete service.<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testDeleteService() throws ApplicationException {
        // mock get catalog plans
        RestfulResponse responsePlan = new RestfulResponse();
        responsePlan.setResponseJson(getJsonString(FILE_PATH + "getPlans.json"));
        responsePlan.setStatus(HttpCode.RESPOND_OK);
        mockGet(responsePlan);

        // mock start workflow bpel workflow
        mockPost(responseSuccess);
        serviceRoa.deleteService("1", httpRequest);
    }

    /**
     * Test method getAllInstances.<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testGetAllInstances() throws ApplicationException {
        serviceRoa.getAllInstances(httpRequest);
    }

    /**
     * Test method getTopoSequence().<br/>
     * 
     * @throws ApplicationException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testGetTopoSequence() throws ApplicationException {
        serviceRoa.getTopoSequence("1", httpRequest);
    }

    /**
     * Test method getServiceManager().<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetServiceManager() {
        assertNotNull(serviceRoa.getServiceManager());
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
     * Mock rest request for get.<br/>
     * 
     * @param response rest response
     * @since GSO 0.5
     */
    private void mockGet(final RestfulResponse response) {
        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders,
                    HttpServletRequest httpRequest) throws ApplicationException {
                return response;
            }
        };
    }

    /**
     * Mock rest request for post.<br/>
     * 
     * @param response rest response
     * @since GSO 0.5
     */
    private void mockPost(final RestfulResponse response) {
        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse post(final String url, Object sendObj, HttpServletRequest httpRequest) {
                return response;
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

    /**
     * Mock database session.<br/>
     * 
     * @since GSO 0.5
     */
    private void mockSession() {
        new MockUp<DatabaseSessionHandler>() {

            @Mock
            public SqlSession getSqlSession() {
                return session;
            }
        };
    }

    /**
     * Test that query service instance by service instance ID.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetInstanceByInstanceId() {
        Response rsp = serviceRoa.getInstanceByInstanceId("1", httpRequest);
        String strExpect = JsonUtil
                .marshal(JsonUtil.unMarshal(getJsonString(FILE_PATH + "getServiceInstanceById.json"), Map.class));
        Assert.assertTrue(strExpect.equals(JsonUtil.marshal(rsp.getEntity())));
    }

    /**
     * Test service operation.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetServiceOperation() {
        Response rsp = serviceRoa.getServiceOperation("1", "123", httpRequest);
        String strExpect =
                JsonUtil.marshal(JsonUtil.unMarshal(getJsonString(FILE_PATH + "queryOperationById.json"), Map.class));
        Assert.assertTrue(strExpect.equals(JsonUtil.marshal(rsp.getEntity())));
    }
}
