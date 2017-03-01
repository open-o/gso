/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.model;

import java.util.HashMap;
import java.util.Map;

import org.openo.gso.servicegateway.constant.FieldConstant;

/**
 * operation result
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.5
 */
public class OperationResult {

    private String serviceId;

    private String operationId;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * get the result map
     * <br>
     * 
     * @return
     * @since GSO 0.5
     */
    public Map<String, Map<String, String>> toResultMap() {
        Map<String, Map<String, String>> result = new HashMap<>();
        Map<String, String> serviceRspInfo = new HashMap<>();
        serviceRspInfo.put(FieldConstant.Create.FIELD_RESPONSE_SERVICEID, serviceId);
        serviceRspInfo.put(FieldConstant.Create.FIELD_RESPONSE_OPERATIONID, operationId);
        result.put(FieldConstant.Create.FIELD_RESPONSE_SERVICE, serviceRspInfo);
        return result;
    }

}
