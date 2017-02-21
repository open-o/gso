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

/**
 * the create location constraint parameters model
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO Mercury Release  2017.2.21
 */
public class CreateLocationConstraintModel {
    // the vnf profile id
    private String vnfProfileId;
    
    // the location constraint information
    private CreateLocationConstraintModelForVim locationConstraints;

    
    /**
     * @return Returns the vnfProfileId.
     */
    public String getVnfProfileId() {
        return vnfProfileId;
    }

    
    /**
     * @param vnfProfileId The vnfProfileId to set.
     */
    public void setVnfProfileId(String vnfProfileId) {
        this.vnfProfileId = vnfProfileId;
    }

    
    /**
     * @return Returns the locationConstraints.
     */
    public CreateLocationConstraintModelForVim getLocationConstraints() {
        return locationConstraints;
    }

    
    /**
     * @param locationConstraints The locationConstraints to set.
     */
    public void setLocationConstraints(CreateLocationConstraintModelForVim locationConstraints) {
        this.locationConstraints = locationConstraints;
    }

    
}
