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

package org.openo.gso.model.servicemo;

/**
 * Parameters of service instance.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/23
 */
public class ServiceParameter {

    /**
     * Service instance ID.
     */
    private String serviceId;

    /**
     * Parameter name.
     */
    private String paramName;

    /**
     * Parameter value;
     */
    private String paramValue;

    /**
     * @return Returns the paramValue.
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * @param paramValue The paramValue to set.
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

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
     * @return Returns the paramName.
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName The paramName to set.
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

}
