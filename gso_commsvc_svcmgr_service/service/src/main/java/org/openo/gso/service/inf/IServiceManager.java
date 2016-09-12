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

package org.openo.gso.service.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.model.servicemo.ServiceModel;
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
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    ServiceModel createService(String reqContent, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    void deleteService(String serviceId, HttpServletRequest httpRequest) throws ServiceException;

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    List<ServiceModel> getAllInstances() throws ServiceException;

    /**
     * Query all service segments instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    List<ServiceSegmentModel> getServiceSegments(String serviceId) throws ServiceException;

    /**
     * Create service segment.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    void createServiceSegment(String reqContent, HttpServletRequest httpRequest) throws ServiceException;
}
