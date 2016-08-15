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

package org.openo.gso.servicemgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;

/**
 * Mapping Class of data operation.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface SubServiceMapper {

    /**
     * Insert sub-service instances in batch.<br/>
     * 
     * @param subServices sub-service instances
     * @since GSO 0.5
     */
    void batchInsert(@Param("subServices") List<SubServiceModel> subServices);

    /**
     * Delete sub-service instance by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @since GSO 0.5
     */
    void delete(@Param("serviceId") String serviceId);

    /**
     * Query sub-service instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances.
     * @since GSO 0.5
     */
    List<SubServiceModel> querySubServices(@Param("serviceId") String serviceId);
}
