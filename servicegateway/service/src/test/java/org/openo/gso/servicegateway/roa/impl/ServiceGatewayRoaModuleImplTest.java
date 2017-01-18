/*
 * Copyright (c) 2016-2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.roa.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.openo.gso.servicegateway.service.impl.ServiceGatewayImpl;
import org.openo.gso.servicegateway.util.http.HttpUtil;
import org.openo.gso.servicegateway.util.json.JsonUtil;

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
public class ServiceGatewayRoaModuleImplTest {

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";

    /**
     * Service ROA.
     */
    ServiceGatewayRoaModuleImpl serviceRoa = new ServiceGatewayRoaModuleImpl();

    /**
     * Service manager.
     */
    ServiceGatewayImpl serviceManager = new ServiceGatewayImpl();

    /**
     * Http request.
     */
    HttpServletRequest httpRequest;

    /**
     * for query job
     */
    private Map<String, Integer> queryStepMap = new HashMap<String, Integer>();

    /**
     * Before executing UT, start sql.<br/>
     * 
     * @since GSO 0.5
     */
    @Before
    public void start() throws IOException, SQLException {
        queryStepMap.put("5212b49f-fe70-414f-9519-88bec35b3191", 0);
        queryStepMap.put("5212b49f-fe70-414f-9519-88bec35b3192", 0);
        queryStepMap.put("5212b49f-fe70-414f-9519-88bec35b3193", 0);
        queryStepMap.put("5212b49f-fe70-414f-9519-88bec35b3194", 0);
    }

    /**
     * After executing UT, close session<br/>
     * 
     * @since GSO 0.5
     */
    @After
    public void stop() {

    }

    /**
     * Test create service.<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testCreateService() throws ServiceException {
        // mock request body
        mockGetRequestBody(FILE_PATH + "createServiceInstance.json");

        mockHttpUtil();

        serviceRoa.createService(httpRequest);

    }

    /**
     * test create nfvo service, for sdno,its the same. so one test enough.
     * <br>
     * 
     * @throws ServiceException
     * @since GSO Mecury Release
     */
    @Test
    public void testCreateNFVOService() throws ServiceException {
        // mock request body
        mockGetRequestBody(FILE_PATH + "createNFVOServiceInstance.json");

        mockHttpUtil();

        serviceRoa.createService(httpRequest);
    }

    /**
     * Test delete service.<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testDeleteService() throws ServiceException {
        mockHttpUtil();
        serviceRoa.deleteService("5212b49f-fe70-414f-9519-88bec35b3190", httpRequest);
    }

    /**
     * Test delete nfvo service. sdno logic is the same as nfvo<br/>
     * 
     * @throws ServiceException when fail to operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Test
    public void testDeleteNFVOService() throws ServiceException {
        mockHttpUtil();
        serviceRoa.deleteService("5212b49f-fe70-414f-9519-88bec35b3191", httpRequest);
    }

    /**
     * test generate create prarameters
     * <br>
     * 
     * @throws ServiceException
     * @since GSO Mecury Release
     */
    @Test
    public void testGenerateCreateParameters() throws ServiceException {
        mockHttpUtil();
        serviceRoa.generateCreateParameters("592f9437-a9c0-4303-b9f6-c445bb7e9814", httpRequest);
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
     * Mock rest request for post.<br/>
     * 
     * @param response rest response
     * @since GSO 0.5
     */
    private void mockHttpUtil() {
        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse delete(final String url) {
                if(url.contains("/openoapi/gso/v1/services/5212b49f-fe70-414f-9519-88bec35b3190")) {
                    // moke delete gso rsp
                    return getResponse("deleteGSOInstanceRsp.json");
                } else if(url.contains("/openoapi/nslcm/v1/ns/5212b49f-fe70-414f-9519-88bec35b3191")) {
                    // moke instantiate nfov rsp
                    return getResponse(null);
                }
                return null;
            }

            @Mock
            public RestfulResponse post(final String url, Object sendObj) {
                if(url.contains("/openoapi/gso/v1/services")) {
                    // moke create gso rsp.
                    return getResponse("createGSOInstanceRsp.json");
                } else if(url.equals("/openoapi/nslcm/v1/ns")) {
                    // moke create nfvo rsp
                    return getResponse("createNFVOInstanceRsp.json");
                } else if(url.contains("/openoapi/nslcm/v1/ns/5212b49f-fe70-414f-9519-88bec35b3191/instantiate")) {
                    // moke instantiate nfov rsp
                    return getResponse("instantiateNFVOInstanceRsp.json");
                } else if(url.contains("/openoapi/nslcm/v1/ns/5212b49f-fe70-414f-9519-88bec35b3191/terminate")) {
                    // moke instantiate nfov rsp
                    return getResponse("terminateNFVOInstanceRsp.json");
                }
                return null;
            }

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders) {
                if(url.equals("/openoapi/catalog/v1/servicetemplates/592f9437-a9c0-4303-b9f6-c445bb7e9814")) {
                    // mock gso template
                    return getResponse("createGSOTemplate.json");
                } else if(url.contains("/openoapi/catalog/v1/servicetemplates/592f9437-a9c0-4303-b9f6-c445bb7e9816")) {
                    // mock nfvo template
                    return getResponse("createNFVOTemplate.json");
                } else if(url.contains(
                        "/openoapi/gso/v1/services/5212b49f-fe70-414f-9519-88bec35b3190/operations/5212b49f-fe70-414f-9519-88bec35b3191")) {
                    // moke query gso create progress
                    return mokeGSOProgress("5212b49f-fe70-414f-9519-88bec35b3191");
                } else if(url.contains("/openoapi/nslcm/v1/jobs/5212b49f-fe70-414f-9519-88bec35b3192")) {
                    // moke query nvo create progress
                    return modeNonGSOProgress("5212b49f-fe70-414f-9519-88bec35b3192");
                } else if(url.contains(
                        "/openoapi/gso/v1/services/5212b49f-fe70-414f-9519-88bec35b3190/operations/5212b49f-fe70-414f-9519-88bec35b3193")) {
                    // moke query gso delete progress
                    return mokeGSOProgress("5212b49f-fe70-414f-9519-88bec35b3193");
                } else if(url.contains("/openoapi/nslcm/v1/jobs/5212b49f-fe70-414f-9519-88bec35b3194")) {
                    // moke query nfvo delete progress
                    return modeNonGSOProgress("5212b49f-fe70-414f-9519-88bec35b3194");
                } else if(url.contains("/openoapi/catalog/v1/csars/8937a94d-1799-47a5-971d-58ff7d35def6")) {
                    // moke gso csar msg
                    return getResponse("createGSOCsar.json");
                } else if(url.contains("/openoapi/catalog/v1/csars/eea33156-514b-48ad-b165-0a38d0baa623")) {
                    // moke nfvo csar msg
                    return getResponse("createNFVOCsar.json");
                } else if(url.contains("/openoapi/inventory/v1/services/5212b49f-fe70-414f-9519-88bec35b3190")) {
                    // moke query gso service from inventory
                    return getResponse("queryGSOService.json");
                } else if(url.contains("/openoapi/inventory/v1/services/5212b49f-fe70-414f-9519-88bec35b3191")) {
                    // moke query nfvo service from inventory
                    return getResponse("queryNFVOService.json");
                } else if(url.contains("/openoapi/extsys/v1/vims")) {
                    // moke query vims
                    return getResponse("queryVims.json");
                } else if(url.contains("/openoapi/extsys/v1/sdncontrollers")) {
                    // moke query sdncontrollers
                    return getResponse("querySDNControllers.json");
                } else if(url.contains(
                        "/openoapi/catalog/v1/servicetemplates/592f9437-a9c0-4303-b9f6-c445bb7e9814/nodetemplates")) {
                    return getResponse("getGSONodeTypes.json");
                }else if(url.contains(
                        "/openoapi/catalog/v1/servicetemplates/nesting?nodeTypeIds")) {
                    return getResponse("getTemplatesByNodeTypes.json");
                }
                return null;
            }
        };
    }

    /**
     * moke gso operation query
     * <br>
     * 
     * @param operationId the operation id
     * @return response model
     * @since GSO Mecury Release
     */
    @SuppressWarnings("unchecked")
    private RestfulResponse mokeGSOProgress(String operationId) {
        // moke query gso operations, step + 10 every time
        String jsonStr = getJsonString(FILE_PATH + "queryGSOOperation.json");
        Map<String, Object> rspModel = JsonUtil.unMarshal(jsonStr, Map.class);
        Map<String, String> operation = (Map<String, String>)rspModel.get(FieldConstant.QueryOperation.FIELD_OPERATION);
        int queryStep = queryStepMap.get(operationId);
        operation.put(FieldConstant.QueryOperation.FIELD_PROGRESS, String.valueOf(queryStep * 10));
        if(queryStep == 10) {
            // if the step exceed to 10, finish the job
            operation.put(FieldConstant.QueryOperation.FIELD_RESULT, FieldConstant.QueryOperation.RESULT_FINISHED);
            queryStepMap.put(operationId, 0);
        }
        queryStepMap.put(operationId, queryStep + 1);
        String jsonStrTmp = JsonUtil.marshal(rspModel);
        RestfulResponse responseSuccess = new RestfulResponse();
        responseSuccess.setStatus(HttpCode.RESPOND_OK);
        responseSuccess.setResponseJson(jsonStrTmp);
        return responseSuccess;
    }

    /**
     * moke sdno,nfvo job query
     * <br>
     * 
     * @param jobId the job id
     * @return response model
     * @since GSO Mecury Release
     */
    @SuppressWarnings("unchecked")
    private RestfulResponse modeNonGSOProgress(String jobId) {
        // moke query nfvo job, step + 10 every time
        String jsonStr = getJsonString(FILE_PATH + "queryNSJob.json");
        Map<String, Object> rspModel = JsonUtil.unMarshal(jsonStr, Map.class);
        Map<String, String> descriptor =
                (Map<String, String>)rspModel.get(FieldConstant.QueryJob.FIELD_RESPONSEDESCRIPTOR);
        int queryStep = queryStepMap.get(jobId);
        descriptor.put(FieldConstant.QueryJob.FIELD_PROGRESS, String.valueOf(queryStep * 10));
        if(queryStep == 10) {
            // if the step exceed to 10, finish the job
            descriptor.put(FieldConstant.QueryJob.FIELD_STATUS, FieldConstant.QueryJob.STATUS_FINISHED);
            queryStepMap.put(jobId, 0);
        }
        queryStepMap.put(jobId, queryStep + 1);
        String jsonStrTmp = JsonUtil.marshal(rspModel);
        RestfulResponse responseSuccess = new RestfulResponse();
        responseSuccess.setStatus(HttpCode.RESPOND_OK);
        responseSuccess.setResponseJson(jsonStrTmp);
        return responseSuccess;
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
     * get the response from file
     * <br>
     * 
     * @param fileName
     * @return
     * @since GSO Mecury Release
     */
    private RestfulResponse getResponse(String fileName) {
        RestfulResponse responseSuccess = new RestfulResponse();
        responseSuccess.setStatus(HttpCode.RESPOND_OK);
        if(null != fileName) {
            String jsonStr = getJsonString(FILE_PATH + fileName);
            responseSuccess.setResponseJson(jsonStr);
        }
        return responseSuccess;
    }
}
