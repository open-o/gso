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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;
import org.openo.gso.servicemgr.roa.inf.IServicemgrRoaModule;
import org.openo.gso.servicemgr.service.inf.IServiceManager;

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
     * @param serviceModle content of creating service
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response createService(ServiceModel serviceModle, HttpServletRequest servletReq) throws ServiceException {
        serviceManager.createService(serviceModle, servletReq);
        return Response.accepted().entity("success").build();
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
    public Response deleteService(String serviceId, HttpServletRequest servletReq) throws ServiceException {
        serviceManager.deleteService(serviceId, servletReq);
        return Response.accepted().entity("success").build();
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
    public Response getTopoSequence(String serviceId, HttpServletRequest servletReq) throws ServiceException {
        List<SubServiceModel> subServices = serviceManager.getSubServices(serviceId);

        return Response.accepted(subServices).build();
    }

}
