/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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
import org.openo.gso.model.servicemo.ServiceSegmentOperation;

/**
 * Mapping Class of data operation.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface ServiceSegmentOperMapper {

    /**
     * Insert service segment operation.<br/>
     * 
     * @param serviceSegment service segment instance
     * @since GSO 0.5
     */
    void insertSegmentOper(ServiceSegmentOperation svcSegmentOper);

    /**
     * Update service segment operation<br>
     * 
     * @param segmentOper service operation
     * @since GSO 0.5
     */
    void updateSegmentOper(ServiceSegmentOperation segmentOper);

    /**
     * Query service segment operation by job id and segment type<br>
     * 
     * @param jobId job id
     * @param serviceSegmentType segment type
     * @return service segment operation instance
     * @since GSO 0.5
     */
    ServiceSegmentOperation querySegmentOperByJobIdAndType(@Param("jobId") String jobId,
            @Param("serviceSegmentType") String serviceSegmentType);

    /**
     * Delete operations by service instance ID.<br/>
     * 
     * @param serviceId service instance ID
     * @since GSO 0.5
     */
    void delete(@Param("serviceId") String serviceId);

    /**
     * Query segment operations by service instance ids.<br/>
     * 
     * @param svcIds service instance ids.
     * @return service segment operations.
     * @since GSO 0.5
     */
    List<ServiceSegmentOperation> querySegmentOperByIds(@Param("svcIds") List<String> svcIds);

    /**
     * Delete old segment operation records which are generated for 15 days.<br/>
     * 
     * @param svcIds service instance id
     * @since GSO 0.5
     */
    void deleteHistory(@Param("svcIds") List<String> svcIds);
}
