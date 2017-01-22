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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.model.CreateParameterRspModel;
import org.openo.gso.servicegateway.model.DomainModel;
import org.openo.gso.servicegateway.model.OperationModel;
import org.openo.gso.servicegateway.model.OperationResult;
import org.openo.gso.servicegateway.model.ServiceModel;
import org.openo.gso.servicegateway.roa.inf.IServiceGatewayRoaModule;
import org.openo.gso.servicegateway.service.impl.ServiceGatewayImpl;
import org.openo.gso.servicegateway.service.inf.IServiceGateway;
import org.openo.gso.servicegateway.util.http.ResponseUtils;
import org.openo.gso.servicegateway.util.json.JsonUtil;
import org.openo.gso.servicegateway.util.validate.ValidateUtil;
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
public class ServiceGatewayRoaModuleImpl implements IServiceGatewayRoaModule {

    /**
     * Log server.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGatewayRoaModuleImpl.class);

    /**
     * Service manager.
     */
    private IServiceGateway serviceGateway = new ServiceGatewayImpl();

    /**
     * Create service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response createService(HttpServletRequest servletReq) {

        OperationResult operResult = null;
        try {
            // 1. Check validation
            String reqContent = RestUtils.getRequestBody(servletReq);
            ValidateUtil.assertStringNotNull(reqContent);
            LOGGER.info("create a new service, req:" + reqContent);
            // 2. Create service
            operResult = serviceGateway.createService(reqContent, servletReq);
        } catch(ApplicationException exception) {
            LOGGER.error("Fail to create service instance.");
            throw ResponseUtils.getException(exception, "Fail to create service instance");
        }
        Map<String, Map<String, String>> result = operResult.toResultMap();
        LOGGER.info("create a new service rsp:" + JsonUtil.marshal(result));
        return Response.accepted().entity(result).build();
    }

    /**
     * Delete service instance.<br/>
     * 
     * @param serviceId service instance id
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response deleteService(String serviceId, HttpServletRequest servletReq) {
        String operationId = null;
        LOGGER.info("delete a service, serviceId:" + serviceId);
        try {
            // Delete service
            operationId = serviceGateway.deleteService(serviceId, servletReq);
        } catch(ApplicationException exception) {
            LOGGER.error("Fail to delete service instance.");
            throw ResponseUtils.getException(exception, "Fail to delete service instance");
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put(FieldConstant.Delete.FIELD_RESPONSE_OPERATIONID, operationId);
        LOGGER.info("delete a service, rsp:" + JsonUtil.marshal(result));
        return Response.accepted().entity(result).build();
    }

    public IServiceGateway getServiceGateway() {
        return serviceGateway;
    }

    public void setServiceGateway(IServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    /**
     * <br>
     * query the operation status
     * 
     * @param serviceId the service id
     * @param operationId the operation id
     * @param servletReq http request
     * @return the operation model
     * @throws ApplicationException
     * @since GSO 0.5
     */
    public Response getOperation(String serviceId, String operationId, HttpServletRequest servletReq)
            throws ApplicationException {
        LOGGER.info("query an operation, serviceId:" + serviceId + " operation id:" + operationId);
        OperationModel operation = serviceGateway.getOperation(serviceId, operationId, servletReq);
        Map<String, Map<String, String>> result = operation.toResultMap();
        LOGGER.info("query an operation, resp:" + JsonUtil.marshal(result));
        return Response.accepted().entity(result).build();
    }

    /**
     * get create parameters by template id
     * <br>
     * 
     * @param tepmlateId the template id
     * @param servletReq http request
     * @return the parameters model for gui
     * @throws ApplicationException when inner error
     * @since GSO 0.5
     */
    public Response generateCreateParameters(String templateId, HttpServletRequest servletReq)
            throws ApplicationException {
        LOGGER.info("generate create parameters, template id:" + templateId);
        CreateParameterRspModel result = serviceGateway.generateCreateParameters(templateId, servletReq);
        String jsonStr = JsonUtil.marshal(result);
        LOGGER.info("generate create parameters, rsp:" + jsonStr);
        return Response.accepted().entity(jsonStr).build();
    }

    /**
     * query the service list
     * <br>
     * 
     * @param servletReq http request
     * @return the service list
     * @throws ApplicationException when inner error
     * @since GSO Mercury Release
     */
    public Response getServices(HttpServletRequest servletReq) throws ApplicationException {
        LOGGER.info("querr services start");
        List<ServiceModel> array = serviceGateway.getServices(servletReq);
        LOGGER.info("querr services rsp:" + JsonUtil.marshal(array));
        return Response.accepted().entity(array).build();
    }

    /**
     * <br>
     * 
     * @param serviceId the service id
     * @param servletReq the http request
     * @return the service model
     * @throws ApplicationException
     * @since GSO Mercury Release
     */
    public Response getService(String serviceId, HttpServletRequest servletReq) throws ApplicationException {
        LOGGER.info("querr service by service id start, service Id:" + serviceId);
        ServiceModel service = serviceGateway.getService(serviceId, servletReq);
        LOGGER.info("querr service rsp:" + JsonUtil.marshal(service));
        return Response.accepted().entity(service).build();
    }

    /**
     * query the domains
     * <br>
     * 
     * @param servletReq the http request
     * @return
     * @throws ApplicationException
     * @since GSO Mercury Release
     */
    public Response getDomains(@Context HttpServletRequest servletReq) throws ApplicationException {
        LOGGER.info("querr domains start");
        List<DomainModel> domains = serviceGateway.getDomains(servletReq);
        LOGGER.info("querr domains rsp:" + JsonUtil.marshal(domains));
        return Response.accepted().entity(domains).build();
    }
}
