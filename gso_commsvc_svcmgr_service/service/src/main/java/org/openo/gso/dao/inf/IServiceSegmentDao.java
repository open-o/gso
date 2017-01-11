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

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;

/**
 * Interface to operate database.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public interface IServiceSegmentDao {

    /**
     * Insert service segment instance.<br/>
     * 
     * @param serviceSegment service segment instance
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    void insertSegment(ServiceSegmentModel serviceSegment) throws ApplicationException;
    
    

    /**
     * Delete service segment instance by service ID.<br/>
     * 
     * @param serviceSegment service segment information
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    void delete(ServiceSegmentModel serviceSegment) throws ApplicationException;

    /**
     * Query service segment instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances.
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    List<ServiceSegmentModel> queryServiceSegments(String serviceId) throws ApplicationException;
    
    /**
     * get service segment by segmentId and segmentType<br>
     * 
     * @param segmentId id of the service segment
     * @param segmengType type of the service segment
     * @return service segment model
     * @since  GSO 0.5
     */
    ServiceSegmentModel queryServiceSegment(String jobId) throws ApplicationException;
    
    
    /**
     * Insert service segment operation<br>
     * 
     * @param svcSegmentOper service segment operation
     * @throws ApplicationException when database exception or parameter is wrong
     * @since  GSO 0.5
     */
    void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) throws ApplicationException;

    /**
     * Update jobId of the service segment<br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    void updateSegmentOperJobId(ServiceSegmentOperation segmentOper) throws ApplicationException;

    /**
     * Update status of the service segment<br><br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    void updateSegmentOperStatus(ServiceSegmentOperation segmentOper) throws ApplicationException;


    /**
     * Update progress of the service segment<br><br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    void updateSegmentOperProgress(ServiceSegmentOperation segmentOper) throws ApplicationException;



    /**
     * query service segment by id and type<br>
     * 
     * @param serviceSegmentId instance id
     * @param serviceSegmentType nfvo or sdno
     * @return service segment
     * @since  GSO 0.5
     */
    ServiceSegmentModel queryServiceSegmentByIdAndType(String serviceSegmentId, String serviceSegmentType) throws ApplicationException;



    /**
     * delete segment by segment id and segment type<br>
     * 
     * @param serviceSegment service segment
     * @since  GSO 0.5
     */
    void deleteSegmentByIdAndType(ServiceSegmentModel serviceSegment) throws ApplicationException;



    /**
     * delete segment operation by segment id and segment type<br>
     * 
     * @param svcSegmentOper service segment operation
     * @since  GSO 0.5
     */
    void deleteSegmentOperByIdAndType(ServiceSegmentOperation svcSegmentOper);
    
}
