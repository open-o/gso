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

package org.openo.gso.dao.inf;

import java.util.List;

import org.openo.gso.model.servicemo.ServiceModel;

/**
 * Operate database interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public interface IServiceModelDao {

    /**
     * Insert service instance.<br/>
     * 
     * @param serviceModel service package mapping data
     * @since GSO 0.5
     */
    void insert(ServiceModel serviceModel);

    /**
     * Delete service instance by service ID.<br/>
     * 
     * @param serviceId service ID
     * @since GSO 0.5
     */
    void delete(String serviceId);

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @since GSO 0.5
     */
    List<ServiceModel> queryAllServices();

    /**
     * Query some service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @since GSO 0.5
     */
    ServiceModel queryServiceByInstanceId(String serviceId);

    /**
     * Update service instance execution status.<br>
     * 
     * @param serviceId service instance ID
     * @param status execution status
     * @since GSO 0.5
     */
    void updateServiceStatus(String serviceId, String status);

    /**
     * Query service instances by status.<br/>
     * 
     * @param status service instance status, finished|processing|error
     * @return service instances
     * @since GSO 0.5
     */
    List<ServiceModel> queryServiceByStatus(String status);

    /**
     * Batch update service instances.<br/>
     * 
     * @param services service instances
     * @since GSO 0.5
     */
    void batchUpdate(List<ServiceModel> services);

    /**
     * Batch delete service data, including service instance, service parameters and package
     * mapping.<br/>
     * 
     * @param svcIds service instance ids
     * @since GSO 0.5
     */
    void batchDelete(List<String> svcIds);
}
