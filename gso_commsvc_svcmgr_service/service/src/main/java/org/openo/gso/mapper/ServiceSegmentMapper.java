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

package org.openo.gso.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.servicemo.ServiceSegmentModel;

/**
 * Mapping Class of data operation.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface ServiceSegmentMapper {

    /**
     * Insert service segment instances in batch.<br/>
     * 
     * @param serviceSegments service segment instances
     * @since GSO 0.5
     */
    void batchInsert(@Param("serviceSegments") List<ServiceSegmentModel> serviceSegments);

    /**
     * Delete service segment instance by service instance ID and segment id and segment type.<br/>
     * 
     * @param serviceSegment service segment object
     * @since GSO 0.5
     */
    void delete(ServiceSegmentModel serviceSegment);

    /**
     * Query service segment instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return service segment instances.
     * @since GSO 0.5
     */
    List<ServiceSegmentModel> queryServiceSegments(@Param("serviceId") String serviceId);
    
    /**
     * Insert service segment instance.<br/>
     * 
     * @param serviceSegment service segment instance
     * @since GSO 0.5
     */
    void insert(ServiceSegmentModel serviceSegment);

    /**
     * query service segment by id and type<br>
     * 
     * @param serviceSegmentId instance id
     * @param serviceSegmentType NFVO or SDNO
     * @return service segment
     * @since  GSO 0.5
     */
    ServiceSegmentModel queryServiceSegmentByIdAndType(@Param("serviceSegmentId") String serviceSegmentId, @Param("serviceSegmentType") String serviceSegmentType);

    /**
     * delete service segment by segment id and segment type<br>
     * 
     * @param serviceSegment service segment
     * @throws ApplicationException when fail to delete service segment
     * @since   GSO 0.5
     */
    void deleteSegmentByIdAndType(ServiceSegmentModel serviceSegment);

    
}
