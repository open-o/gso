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
    void insert(ServiceSegmentOperation svcSegmentOper);

    /**
     * Update job id of the service segment operation<br>
     * 
     * @param segmentOper service operation
     * @since  GSO 0.5
     */
    void updateJobId(ServiceSegmentOperation segmentOper);

    /**
     * Update status of the service segment<br><br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    void updateStatus(ServiceSegmentOperation segmentOper);

    /**
     * Update progress of the service segment<br><br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    void updateProgress(ServiceSegmentOperation segmentOper);

}
