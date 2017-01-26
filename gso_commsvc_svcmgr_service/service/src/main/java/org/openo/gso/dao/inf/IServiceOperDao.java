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
     * @since GSO 0.5
     */
    void insert(ServiceOperation serviceOperation);

    /**
     * Delete operations of service.<br/>
     * 
     * @param serviceId service instance ID
     * @since GSO 0.5
     */
    void delete(String serviceId);

    /**
     * Query service operation progress by service ID and operation ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId operation ID
     * @return service operation Detail
     * @since GSO 0.5
     */
    ServiceOperation queryOperationById(String serviceId, String operationId);

    /**
     * Update service operation.<br/>
     * 
     * @param serviceOperation service operation
     * @since GSO 0.5
     */
    void update(ServiceOperation serviceOperation);

    /**
     * Delete old operation records which are generated for 15 days.<br/>
     * 
     * @param svcIds service instance ids
     * @since GSO 0.5
     */
    void deleteHistory(List<String> svcIds);

    /**
     * Get operations by service progress type.<br/>
     * 
     * @param svcIds service instance ID
     * @return service operations
     * @since GSO 0.5
     */
    List<ServiceOperation> queryOperByIds(List<String> svcIds);

    /**
     * Batch update service operations.<br/>
     * 
     * @param svcOperations service operations
     * @since GSO 0.5
     */
    void batchUpdate(List<ServiceOperation> svcOperations);

    /**
     * Query old operations.<br/>
     * 
     * @return service operations
     * @since GSO 0.5
     */
    List<ServiceOperation> queryHistory();
}
