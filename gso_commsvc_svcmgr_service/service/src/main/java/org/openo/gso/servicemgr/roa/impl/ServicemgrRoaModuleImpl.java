/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicemgr.roa.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.servicemgr.constant.Constant;
import org.openo.gso.servicemgr.exception.HttpCode;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;
import org.openo.gso.servicemgr.roa.inf.IServicemgrRoaModule;
import org.openo.gso.servicemgr.service.inf.IServiceManager;
import org.openo.gso.servicemgr.util.http.ResponseUtils;
import org.openo.gso.servicemgr.util.validate.ValidateUtil;
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
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response createService(HttpServletRequest servletReq) {
        Map<String, Object> operateStatus = null;
        Map<String, Object> result = null;
        ServiceModel serviceModel = null;
        try {
            // 1. Check validation
            String reqContent = RestUtils.getRequestBody(servletReq);
            ValidateUtil.assertStringNotNull(reqContent);

            // 2. Create service
            serviceModel = serviceManager.createService(reqContent, servletReq);
        } catch(ServiceException exception) {
            LOGGER.error("Fail to create service instance.");
            operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_FAIL, exception,
                    String.valueOf(exception.getHttpCode()));
            result = ResponseUtils.setResult(serviceModel, operateStatus);
            Response.accepted().entity(result).build();
        }

        operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_SUCCESS, null,
                String.valueOf(HttpCode.RESPOND_OK));
        result = ResponseUtils.setResult(serviceModel, operateStatus);

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
        Map<String, Object> operateStatus = null;
        try {
            ValidateUtil.assertStringNotNull(serviceId);
            serviceManager.deleteService(serviceId, servletReq);
        } catch(ServiceException exception) {
            LOGGER.error("Fail to delete service instance.");
            operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_FAIL, exception,
                    String.valueOf(exception.getHttpCode()));
            Response.accepted().entity(operateStatus).build();
        }

        operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_SUCCESS, null,
                String.valueOf(HttpCode.RESPOND_OK));

        return Response.accepted().entity(operateStatus).build();
    }

    /**
     * Query all service instances.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database.
     * @since GSO 0.5
     */
    @Override
    public Response getAllInstances(HttpServletRequest servletReq) throws ServiceException {
        List<ServiceModel> services = serviceManager.getAllInstances();
        return Response.accepted(services).build();
    }

    /**
     * Query represent sequence in topology.<br/>
     * 
     * @param serviceId service instance ID
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response getTopoSequence(String serviceId, HttpServletRequest servletReq) {
        List<SubServiceModel> subServices = null;
        Map<String, Object> operateStatus = null;
        try {
            subServices = serviceManager.getSubServices(serviceId);
        } catch(ServiceException exception) {
            LOGGER.error("Fail to query the sequence of topology.");
            operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_FAIL, exception,
                    String.valueOf(exception.getHttpCode()));
            Response.accepted().entity(operateStatus).build();
        }

        operateStatus = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_SUCCESS, null,
                String.valueOf(HttpCode.RESPOND_OK));

        return Response.accepted().entity(subServices).build();
    }

    /**
     * Storage sub-service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ServiceException when fail to storage sub-service instance.
     * @since GSO 0.5
     */
    @Override
    public Response storageSubService(HttpServletRequest servletReq) throws ServiceException {
        return null;
    }

}
