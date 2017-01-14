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

package org.openo.gso.model.servicemo;

/**
 * Service detail information model class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public class ServiceDetailModel {

    /**
     * service model
     */
    private ServiceModel serviceModel;

    /**
     * Service operation information
     */
    private ServiceOperation serviceOperation;

    /**
     * @return Returns the serviceModel.
     */
    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    /**
     * @param serviceModel The serviceModel to set.
     */
    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    /**
     * @return Returns the serviceOperation.
     */
    public ServiceOperation getServiceOperation() {
        return serviceOperation;
    }

    /**
     * @param serviceOperation The serviceOperation to set.
     */
    public void setServiceOperation(ServiceOperation serviceOperation) {
        this.serviceOperation = serviceOperation;
    }
}
