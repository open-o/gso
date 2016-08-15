/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicemgr.model.servicemo;

/**
 * Service model class<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServiceModel extends BaseServiceModel {

    /**
     * Mapping relation object.
     */
    private ServicePackageMapping servicePackage;

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    public ServiceModel() {
        super();
    }

    /**
     * @return Returns the servicePackage.
     */
    public ServicePackageMapping getServicePackage() {
        return servicePackage;
    }

    /**
     * @param servicePackage The servicePackage to set.
     */
    public void setServicePackage(ServicePackageMapping servicePackage) {
        this.servicePackage = servicePackage;
    }

}
