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

package org.openo.gso.service.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.gso.model.servicemo.ServiceDetailModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.model.servicemo.ServiceSegmentModel;

/**
 * Interface to operate service.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface IServiceManager {

    /**
     * Create service instance.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @return service instance
     * @since GSO 0.5
     */
    ServiceDetailModel createService(String reqContent, HttpServletRequest httpRequest);

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @since GSO 0.5
     */
    void deleteService(String serviceId, HttpServletRequest httpRequest);

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @since GSO 0.5
     */
    List<ServiceModel> getAllInstances();

    /**
     * Query all service segments instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances
     * @since GSO 0.5
     */
    List<ServiceSegmentModel> getServiceSegments(String serviceId);

    /**
     * Create service segment.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @since GSO 0.5
     */
    void createServiceSegment(String reqContent, HttpServletRequest httpRequest);

    /**
     * Query service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @since GSO 0.5
     */
    ServiceModel getInstanceByInstanceId(String serviceId);

    /**
     * Query service operation by service instance ID and operation ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId service operation ID
     * @return service operation
     * @since GSO 0.5
     */
    ServiceOperation getServiceOperation(String serviceId, String operationId);
}
