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

package org.openo.gso.servicegateway.service.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.gso.servicegateway.model.CreateParameterRspModel;
import org.openo.gso.servicegateway.model.DomainModel;
import org.openo.gso.servicegateway.model.OperationModel;
import org.openo.gso.servicegateway.model.OperationResult;
import org.openo.gso.servicegateway.model.ServiceModel;

/**
 * Interface to operate service.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface IServiceGateway {

    /**
     * Create service instance.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @return service instance
     * @since GSO 0.5
     */
    OperationResult createService(String reqContent, HttpServletRequest httpRequest);

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @since GSO 0.5
     */
    String deleteService(String serviceId, HttpServletRequest httpRequest);

    /**
     * query the operation information
     * <br>
     * 
     * @param serviceId the service id
     * @param operationId the operation id
     * @return the operation progress model
     * @since GSO 0.5
     */
    OperationModel getOperation(String serviceId, String operationId, HttpServletRequest httpRequest);

    /**
     * <br>
     * generate the parameters for create service
     * 
     * @param templateId the template id
     * @param servletReq the http request
     * @return the parameter model for gui
     * @since GSO 0.5
     */
    CreateParameterRspModel generateCreateParameters(String templateId, HttpServletRequest servletReq);

    /**
     * <br>
     * get the services
     * 
     * @param servletReq http request
     * @return the services array
     * @since GSO Mercury Release
     */
    List<ServiceModel> getServices(HttpServletRequest servletReq);

    /**
     * get the service from inventory by service id
     * <br>
     * 
     * @param serviceId the service id
     * @param servletReq http request
     * @return
     * @since GSO Mercury Release
     */
    ServiceModel getService(String serviceId, HttpServletRequest servletReq);

    /**
     * get the domains
     * <br>
     * 
     * @param servletReq the http request
     * @return
     * @since GSO Mercury Release
     */
    List<DomainModel> getDomains(HttpServletRequest servletReq);
}
