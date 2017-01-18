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

package org.openo.gso.dao.inf;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.servicemo.ServiceOperation;

/**
 * Service operation interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public interface IServiceOperDao {

    /**
     * Insert service operation.<br/>
     * 
     * @param serviceOperation operation record
     * @throws ApplicationException when failing to operation database.
     * @since GSO 0.5
     */
    void insert(ServiceOperation serviceOperation) throws ApplicationException;

    /**
     * Delete service operation.<br/>
     * 
     * @param serviceId service instance ID
     * @throws ApplicationException when failing to operation database.
     * @since GSO 0.5
     */
    void delete(@Param("serviceId") String serviceId) throws ApplicationException;

    /**
     * Query service operation progress by service ID and operation ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId operation ID
     * @return service operation Detail
     * @throws ApplicationException when failing to operation database.
     * @since GSO 0.5
     */
    ServiceOperation queryOperationById(@Param("serviceId") String serviceId, @Param("operationId") String operationId)
            throws ApplicationException;

    /**
     * Update service operation.<br/>
     * 
     * @param serviceOperation service operation
     * @throws ApplicationException when parameter error or fail to operate DB.
     * @since GSO 0.5
     */
    void update(ServiceOperation serviceOperation) throws ApplicationException;

    /**
     * Delete old operation records which are generated for 15 days.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    void deleteHistory() throws ApplicationException;

    /**
     * Get operations by service progress type.<br/>
     * 
     * @param svcIds service instance ID
     * @return service operations
     * @since GSO 0.5
     */
    List<ServiceOperation> queryOperByIds(List<String> svcIds) throws ApplicationException;

    /**
     * Batch update service operations.<br/>
     * 
     * @param svcOperations service operations
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    void batchUpdate(@Param("svcOperations") List<ServiceOperation> svcOperations) throws ApplicationException;
}
