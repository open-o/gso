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

package org.openo.gso.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.gso.model.servicemo.ServiceModel;

/**
 * Mapping Class of data operation.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface ServiceModelMapper {

    /**
     * Insert service instance.<br/>
     * 
     * @param serviceModel service instance.
     * @since GSO 0.5
     */
    void insert(ServiceModel serviceModel);

    /**
     * Delete service instance by service ID.<br/>
     * 
     * @param serviceId service ID
     * @since GSO 0.5
     */
    void delete(@Param("serviceId") String serviceId);

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @since GSO 0.5
     */
    List<ServiceModel> queryAllServices();

    /**
     * <br>
     * 
     * @param serviceId
     * @return
     * @since  SDNO 0.5
     */
    ServiceModel queryServiceByInstanceId(@Param("serviceId") String serviceId);

    /**
     * <br>
     * 
     * @param serviceId
     * @param result
     * @since  SDNO 0.5
     */
    void updateServiceResult(@Param("serviceId") String serviceId, @Param("result") String result);
    
}
