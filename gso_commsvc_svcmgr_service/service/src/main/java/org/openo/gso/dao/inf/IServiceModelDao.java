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

package org.openo.gso.dao.inf;

import java.util.List;

import org.openo.gso.commsvc.common.Exception.ApplicationException;
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
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    void insert(ServiceModel serviceModel) throws ApplicationException;

    /**
     * Delete service instance by service ID.<br/>
     * 
     * @param serviceId service ID
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    void delete(String serviceId) throws ApplicationException;

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    List<ServiceModel> queryAllServices() throws ApplicationException;

    /**
     * Query some service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    ServiceModel queryServiceByInstanceId(String serviceId) throws ApplicationException;

    /**
     * <br>
     * 
     * @param serviceId
     * @param string
     * @since  SDNO 0.5
     */
    void updateServiceResult(String serviceId, String result);
}
