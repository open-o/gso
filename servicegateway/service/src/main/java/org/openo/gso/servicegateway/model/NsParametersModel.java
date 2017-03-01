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
import java.util.List;
import java.util.Map;

/**
 * the ns parameters model
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO Mercury Release 2017年2月21日
 */
public class NsParametersModel {

    /**
     * the location contrains, now only nfvo use this
     */
    private List<LocationConstraintModel> locationConstraints = null;

    /**
     * the additionalParamForNs is used for the ns itself, both gso,sdno, nfvo
     * this params is defined in the template. and both with the sdn controller.
     */
    private Map<String, String> additionalParamForNs = new HashMap<>();

    /**
     * @return Returns the locationConstraints.
     */
    public List<LocationConstraintModel> getLocationConstraints() {
        return locationConstraints;
    }

    /**
     * @param locationConstraints The locationConstraints to set.
     */
    public void setLocationConstraints(List<LocationConstraintModel> locationConstraints) {
        this.locationConstraints = locationConstraints;
    }

    /**
     * @return Returns the additionalParamForNs.
     */
    public Map<String, String> getAdditionalParamForNs() {
        return additionalParamForNs;
    }

    /**
     * @param additionalParamForNs The additionalParamForNs to set.
     */
    public void setAdditionalParamForNs(Map<String, String> additionalParamForNs) {
        this.additionalParamForNs = additionalParamForNs;
    }

}
