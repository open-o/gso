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

package org.openo.gso.service.inf;

import org.openo.gso.model.servicemo.ServiceOperation;

/**
 * Service operation management interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public interface IOperationManager {

    /**
     * Create service operation.<br/>
     * 
     * @param serviceId service instance ID.
     * @param operationType service operation type, create|delete
     * @since GSO 0.5
     */
    ServiceOperation createOperation(String serviceId, String operationType);

    /**
     * Update service operation.<br/>
     * 
     * @param svcOperation service operation data.
     * @since GSO 0.5
     */
    void updateOperation(ServiceOperation svcOperation);

    /**
     * Delete service operation by service instance ID.<br/>
     * 
     * @param serviceId service instance ID
     * @since GSO 0.5
     */
    void delete(String serviceId);

    /**
     * Query service operation result.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId service operation ID
     * @return service operation result
     * @since GSO 0.5
     */
    ServiceOperation queryOperation(String serviceId, String operationId);
}
