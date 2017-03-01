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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.commsvc.common.util.ValidateUtil;
import org.openo.gso.constant.Constant;
import org.openo.gso.model.servicemo.ServiceDetailModel;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.roa.inf.IServicemgrRoaModule;
import org.openo.gso.service.inf.IServiceManager;
import org.openo.gso.util.convertor.DataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement class for restful interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServicemgrRoaModuleImpl implements IServicemgrRoaModule {

    /**
     * Log server.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicemgrRoaModuleImpl.class);

    /**
     * Service manager.
     */
    private IServiceManager serviceManager;

    /**
     * @return Returns the serviceManager.
     */
    public IServiceManager getServiceManager() {
        return serviceManager;
    }

    /**
     * @param serviceManager The serviceManager to set.
     */
    public void setServicemanager(IServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    /**
     * Create service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response createService(HttpServletRequest servletReq) {
        LOGGER.info("Start to create service instance.");

        // 1. Check validation
        String reqContent = RestUtils.getRequestBody(servletReq);
        ValidateUtil.assertStringNotNull(reqContent, Constant.RESPONSE_CONTENT_MESSAGE);
        LOGGER.info("create service instance: {}", reqContent);

        // 2. Create service
        ServiceDetailModel svcDetail = serviceManager.createService(reqContent, servletReq);

        // 3. assemble response data.
        String serviceId = ((null != svcDetail) && (null != svcDetail.getServiceModel()))
                ? svcDetail.getServiceModel().getServiceId() : null;
        String operationId = ((null != svcDetail) && (null != svcDetail.getServiceOperation()))
                ? svcDetail.getServiceOperation().getOperationId() : null;
        Map<String, String> ids = new HashMap<>();
        ids.put(Constant.SERVICE_INSTANCE_ID, serviceId);
        ids.put(Constant.SERVICE_OPERATION_ID, operationId);
        Map<String, Object> rsp = new HashMap<>();
        rsp.put(Constant.SERVICE_INDENTIFY, ids);

        LOGGER.info("create service response: {}", rsp);

        return Response.status(HttpStatus.SC_ACCEPTED).entity(rsp).build();
    }

    /**
     * Delete service instance.<br/>
     * 
     * @param serviceId service instance id
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response deleteService(String serviceId, HttpServletRequest servletReq) {
        LOGGER.info("Start to delete service instance.");

        // 1. Delete service and return operation id
        ValidateUtil.assertStringNotNull(serviceId, Constant.SERVICE_ID);
        String operationId = serviceManager.deleteService(serviceId, servletReq);
        LOGGER.info("delete service, the operation id is: {}", operationId);

        // 2. Build response
        Map<String, String> rps = new HashMap<>();
        rps.put(Constant.SERVICE_OPERATION_ID, operationId);
        return Response.status(HttpStatus.SC_ACCEPTED).entity(rps).build();
    }

    /**
     * Query all service instances.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database.
     * @since GSO 0.5
     */
    @Override
    public Response getAllInstances(HttpServletRequest servletReq) {
        LOGGER.info("Start to get all service instances.");
        Object response = DataConverter.getAllSvcIntancesResult(serviceManager.getAllInstances());
        return Response.status(HttpStatus.SC_OK).entity(response).build();
    }

    /**
     * Query represent sequence in topology.<br/>
     * 
     * @param serviceId service instance ID
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response getTopoSequence(String serviceId, HttpServletRequest servletReq) {
        LOGGER.info("Start to get top sequence number of service.");
        ValidateUtil.assertStringNotNull(serviceId, Constant.SERVICE_ID);
        Object response = DataConverter.getSegments(serviceManager.getServiceSegments(serviceId), serviceId);
        return Response.status(HttpStatus.SC_OK).entity(response).build();
    }

    /**
     * Get service instance by service instance ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param servletReq request
     * @return service instance detail
     * @since GSO 0.5
     */
    @Override
    public Response getInstanceByInstanceId(String serviceId, HttpServletRequest servletReq) {
        LOGGER.info("Start to get service instance by instanceId.");
        ValidateUtil.assertStringNotNull(serviceId, Constant.SERVICE_ID);
        Object response = DataConverter.getSvcInstanceResult(serviceManager.getInstanceByInstanceId(serviceId));
        return Response.status(HttpStatus.SC_OK).entity(response).build();
    }

    /**
     * Query service operation result.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId service operation ID
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response getServiceOperation(String serviceId, String operationId, HttpServletRequest servletReq) {
        LOGGER.info("Start to get service operation result by instanceId and opertionId.");

        // 1. Check input
        ValidateUtil.assertStringNotNull(serviceId, Constant.SERVICE_ID);
        ValidateUtil.assertStringNotNull(operationId, Constant.SERVICE_OPERATION_ID);

        // 2. Query operation result
        ServiceOperation svcOperation = serviceManager.getServiceOperation(serviceId, operationId);
        LOGGER.info("Get service Operation rsp: {}", svcOperation);

        // 3. Build response.
        Map<String, Object> operMap = new HashMap<>();
        operMap.put(Constant.OPERATION_IDENTIFY, svcOperation);

        return Response.status(HttpStatus.SC_OK).entity(operMap).build();
    }
}
