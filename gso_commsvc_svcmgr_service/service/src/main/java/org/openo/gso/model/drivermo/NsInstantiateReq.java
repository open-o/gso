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

import java.util.Map;

/**
 * <br>
 * <p>
 * </p>
 * request model for instatiate
 * @author
 * @version     GSO 0.5  2016/9/3
 */
public class NsInstantiateReq {

    String nsInstanceId;

    Map<String, String> additionalParamForNs;
    
    
    /**
     * @return Returns the nsInstanceId.
     */
    public String getNsInstanceId() {
        return nsInstanceId;
    }


    
    /**
     * @param nsInstanceId The nsInstanceId to set.
     */
    public void setNsInstanceId(String nsInstanceId) {
        this.nsInstanceId = nsInstanceId;
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
