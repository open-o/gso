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

import java.util.ArrayList;
import java.util.List;

/**
 * the ns parameters for client to generate the UI
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO Mercury Release 2017年2月21日
 */
public class CreateNsParametersModel {

    private List<CreateLocationConstraintModel> locationConstraints = null;

    /**
     * the additionalParamForNs is used for the ns itself, both gso,sdno, nfvo
     * this params is defined in the template. and both with the location and sdn controller.
     */
    private List<ParameterDefineModel> additionalParamForNs = new ArrayList<>();

    /**
     * @return Returns the locationConstraints.
     */
    public List<CreateLocationConstraintModel> getLocationConstraints() {
        return locationConstraints;
    }

    /**
     * @param locationConstraints The locationConstraints to set.
     */
    public void setLocationConstraints(List<CreateLocationConstraintModel> locationConstraints) {
        this.locationConstraints = locationConstraints;
    }

    /**
     * @return Returns the additionalParamForNs.
     */
    public List<ParameterDefineModel> getAdditionalParamForNs() {
        return additionalParamForNs;
    }

    /**
     * @param additionalParamForNs The additionalParamForNs to set.
     */
    public void setAdditionalParamForNs(List<ParameterDefineModel> additionalParamForNs) {
        this.additionalParamForNs = additionalParamForNs;
    }

}
