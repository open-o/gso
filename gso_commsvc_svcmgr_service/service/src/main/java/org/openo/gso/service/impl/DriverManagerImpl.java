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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.DriverExceptionID;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.NsResponse;
import org.openo.gso.model.drivermo.ServiceNode;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.drivermo.TerminateParams;
import org.openo.gso.service.inf.IDriverManager;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service management class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class DriverManagerImpl implements IDriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerImpl.class);

    private static String CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL = "/openoapi/catalog/v1/servicetemplate/nesting";

    private static final String LCM_STORE_SUBSERVICE_URL = "/openoapi/lifecyclemgr/v1/services/subservices";

    private IDriverService serviceInf;
    
    /**
     * @return Returns the serviceInf.
     */
    public IDriverService getServiceInf() {
        return serviceInf;
    }

    
    /**
     * @param serviceInf The serviceInf to set.
     */
    public void setServiceInf(IDriverService serviceInf) {
        this.serviceInf = serviceInf;
    }

    /**
     * Create service instance.<br/>
     * 
     * @param serviceModel service instance
     * @param httpRequest http request
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse terminateService(HttpServletRequest httpRequest) throws ServiceException {
        String body = RestUtils.getRequestBody(httpRequest);

        // transfer the input into input parameters model
        TerminateParams inputs = null;
        inputs = JsonUtil.unMarshal(body, TerminateParams.class);

        // get nodeType from the request body
        String nodeType = inputs.getNodeType();

        // get instaceId & serviceId value from the map
        Map<String, String> instIdMap = inputs.getInputParameters();

        String serviceId = instIdMap.get("serviceId");
        StringBuilder builder = new StringBuilder(nodeType);
        builder.append(".instanceId");
        String instKey = builder.toString();
        String instanceId = instIdMap.get(instKey);

        // invoke the SDNO or NFVO to delete the instance
        String status = "fail";
        try {
            status = serviceInf.delete(nodeType, instanceId);
        } catch(Exception e) {
            LOGGER.error("fail to delete the sub-service", e);
        }

        // construct the URL and the method type
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, LCM_STORE_SUBSERVICE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);

        // construct the request body to store the sub-service
        Map<String, String> reqBodyMap = new HashMap<String, String>();
        reqBodyMap.put("serviceId", serviceId);
        reqBodyMap.put("subServiceId", instanceId);
        reqBodyMap.put("subServiceTempalteId", "");
        reqBodyMap.put("operationType", "delete");
        reqBodyMap.put("status", status);

        ObjectMapper mapper = new ObjectMapper();
        String params = StringUtils.EMPTY;
        try {
            params = mapper.writeValueAsString(reqBodyMap);
        } catch(JsonGenerationException e) {
            LOGGER.error("fail to write value as string", e);
        } catch(JsonMappingException e) {
            LOGGER.error("fail to write value as string", e);
        } catch(IOException e) {
            LOGGER.error("fail to write value as string", e);
        }

        RestfulResponse lcmRsp = RestfulUtil.getRemoteResponse(paramsMap, params, null);

        RestfulResponse rsp = new RestfulResponse();
        if(!HttpStatus.isSuccess(lcmRsp.getStatus())) {
            rsp.setStatus(HttpCode.INTERNAL_SERVER_ERROR);
            LOGGER.error("fail to store the sub-service to LCM");
        }
        return rsp;

    }

    /**
     * Instantiate service instance.<br/>
     * 
     * @param serviceNode service instance
     * @param httpRequest http request
     * @throws ServiceException when parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse instantiateService(HttpServletRequest httpRequest) throws ServiceException {

        String body = RestUtils.getRequestBody(httpRequest);

        // Step 0: Transfer the input into input parameters model
        ServiceNode serviceNode = null;
        serviceNode = JsonUtil.unMarshal(body, ServiceNode.class);

        // Step 1:Validate input parameters
        String nodeType = serviceNode.getNodeType();

        if((null == nodeType) || (null == serviceNode.getStNodeParam())) {
            LOGGER.error("Input parameters from lcm/workflow are empty");
            throw new ServiceException(DriverExceptionID.INVALID_PARAM, HttpCode.INTERNAL_SERVER_ERROR);
        }

        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ServiceException(DriverExceptionID.INVALID_PARAM, HttpCode.INTERNAL_SERVER_ERROR);
        }
        serviceInf.setNodeType(serviceNode.getNodeType());

        // Step 2:Make a list of parameters for the node Type
        Map<String, String> mapParam = getParamsByNodeType(serviceNode);
        
        String serviceId = mapParam.get("serviceId");

        // Step 3: Call the Catalogue service to get service template id
        ServiceTemplate svcTmpl = getSvcTmplByNodeType(serviceNode);
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ServiceException(DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE, HttpCode.INTERNAL_SERVER_ERROR);
        }

        return createNetworkSubService(serviceId, svcTmpl.getServiceTemplateId(), mapParam);
    }

    private RestfulResponse createNetworkSubService(String serviceId, String templateId, Map<String, String> mapParam)
            throws ServiceException {

        // Step 1: Create Network service
        String nsInstanceId = serviceInf.createNS(templateId, mapParam);
        if(StringUtils.isEmpty(nsInstanceId)) {
            LOGGER.error("Invalid instanceId from workflow");
            throw new ServiceException(DriverExceptionID.INVALID_VALUE_FROM_WORKFLOW, HttpCode.INTERNAL_SERVER_ERROR);
        }

        // Step 2: Instantiate Network service
        String jobId = serviceInf.instantiateNS(nsInstanceId, mapParam);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from workflow");
            throw new ServiceException(DriverExceptionID.INVALID_VALUE_FROM_WORKFLOW, HttpCode.INTERNAL_SERVER_ERROR);
        }

        // Step 3: Wait for Job to complete
        String status = "success";
        try {
            waitForJobToComplete(jobId);
        } catch(ServiceException e) {
            LOGGER.error("fail to complete the job", e);
            status = "fail";
        }

        NsResponse oResponse = new NsResponse();
        oResponse.setServiceId(serviceId);
        oResponse.setSubServiceId(nsInstanceId);

        oResponse.setSubServiceTmplId(templateId);
        oResponse.setOperationType("create");
        oResponse.setStatus(status);

        // Step 4: Send the response to LCM
        return sendResponse(oResponse);
    }

    private Map<String, String> getParamsByNodeType(ServiceNode serviceNode) {

        // Make a list of parameters for the node Type
        Map<String, String> mapParam = new HashMap<String, String>();
        for(Map<String, String> mapNodeparam : serviceNode.getStNodeParam()) {

            for(Map.Entry<String, String> param : mapNodeparam.entrySet()) {

                // The Parameter name will start with node type as prefix
                String paramValue = param.getValue();
                if(paramValue.startsWith(serviceNode.getNodeType())) {
                    mapParam.put(param.getKey(), paramValue);
                }
            }
        }

        return mapParam;
    }

    private ServiceTemplate getSvcTmplByNodeType(ServiceNode serviceNode) throws ServiceException {

        Map<String, String> paramsMap = new HashMap<String, String>();

        // Step 1: Prepare url and method type
        paramsMap.put(CommonConstant.HttpContext.URL, CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);

        // Step 2: Prepare the query param
        List<String> lstNodeTypeIds = new ArrayList<String>();
        lstNodeTypeIds.add(serviceNode.getNodeType());

        String params = null;
        Map<String, String> mapParams = new HashMap<String, String>();
        params = JsonUtil.marshal(lstNodeTypeIds);
        mapParams.put("nodeTypeIds", params);

        // Step 3:Send the request and get response
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, params, mapParams);

        return JsonUtil.unMarshal(rsp.getResponseContent(), ServiceTemplate.class);

    }

    private void waitForJobToComplete(String jobId) throws ServiceException {

        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        boolean queryFlag = true;

        while(queryFlag) {

            QueryProgress task = new QueryProgress(jobId);
            Future<NsProgressStatus> status = executor.submit(task);

            NsProgressStatus progress = null;
            try {
                progress = status.get();
            } catch(Exception e) {

                queryFlag = false;
                LOGGER.error("error in the result when query the operation status", e);
                throw new ServiceException(DriverExceptionID.INTERNAL_ERROR, HttpCode.INTERNAL_SERVER_ERROR);
            }

            if(progress.getRspDescriptor().getProgress().equals(100)
                    && "finished".equals(progress.getRspDescriptor().getStatus())) {
                LOGGER.info("Success to create the sub-service");
                queryFlag = false;
            } else if("error".equals(progress.getRspDescriptor().getStatus())) {
                LOGGER.error("Failed to create the sub service");
                throw new ServiceException(DriverExceptionID.INTERNAL_ERROR, HttpCode.INTERNAL_SERVER_ERROR);
            } else {
                // do nothing
            }
        }

    }

    private RestfulResponse sendResponse(NsResponse rsp) throws ServiceException {
        Map<String, String> paramsMap = new HashMap<String, String>();

        // Step 1: Prepare url and method type
        paramsMap.put(CommonConstant.HttpContext.URL, LCM_STORE_SUBSERVICE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);

        String response = null;
        response = JsonUtil.marshal(rsp);

        // Step 2:Send the request and get response
        return RestfulUtil.getRemoteResponse(paramsMap, response, null);

    }

    class QueryProgress implements Callable<NsProgressStatus> {

        String jobId;

        QueryProgress(String jobInfo) {
            jobId = jobInfo;
        }

        @Override
        public NsProgressStatus call() throws Exception {

            // For every 5 seconds query progress
            Thread.sleep(5000);
            return serviceInf.getNsProgress(jobId);
        }

    }

}
