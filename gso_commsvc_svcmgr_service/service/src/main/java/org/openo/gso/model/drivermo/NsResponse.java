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
package org.openo.gso.model.drivermo;

/**
 * <br>
 * <p>
 * </p>
 * response model for create NS
 * @author
 * @version     GSO 0.5  2016/9/3
 */
public class NsResponse {

    String serviceId;

    String subServiceId;

    String subServiceType;

    String subServiceTmplId;

    String operationType;

    String status;

    
    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    
    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    
    /**
     * @return Returns the subServiceId.
     */
    public String getSubServiceId() {
        return subServiceId;
    }

    
    /**
     * @param subServiceId The subServiceId to set.
     */
    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
    }

    
    /**
     * @return Returns the subServiceType.
     */
    public String getSubServiceType() {
        return subServiceType;
    }

    
    /**
     * @param subServiceType The subServiceType to set.
     */
    public void setSubServiceType(String subServiceType) {
        this.subServiceType = subServiceType;
    }

    
    /**
     * @return Returns the subServiceTmplId.
     */
    public String getSubServiceTmplId() {
        return subServiceTmplId;
    }

    
    /**
     * @param subServiceTmplId The subServiceTmplId to set.
     */
    public void setSubServiceTmplId(String subServiceTmplId) {
        this.subServiceTmplId = subServiceTmplId;
    }

    
    /**
     * @return Returns the operationType.
     */
    public String getOperationType() {
        return operationType;
    }

    
    /**
     * @param operationType The operationType to set.
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    
    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    
    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }



}
