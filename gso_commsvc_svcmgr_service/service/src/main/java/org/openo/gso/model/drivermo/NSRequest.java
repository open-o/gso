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
 * Network Service Request<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 Sep 2, 2016
 */
public class NSRequest {

    String nsdId;

    String nsName;

    String description;

    
    /**
     * @return Returns the nsdId.
     */
    public String getNsdId() {
        return nsdId;
    }


    
    /**
     * @param nsdId The nsdId to set.
     */
    public void setNsdId(String nsdId) {
        this.nsdId = nsdId;
    }


    /**
     * @return Returns the nsName.
     */
    public String getNsName() {
        return nsName;
    }

    
    /**
     * @param nsName The nsName to set.
     */
    public void setNsName(String nsName) {
        this.nsName = nsName;
    }



    
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }



    
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
