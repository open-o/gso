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

package org.openo.gso.servicemgr.model.catalogmo;

import java.util.List;

/**
 * Structure of operation for work flow.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2016/8/12
 */
public class OperationModel {
    
    /**
     * Name of operation.
     */
    private String name;
    
    /**
     * Description of operation.
     */
    private String description;
    
    /**
     * Process id of operation.
     */
    private String processId;
    
    /**
     * Input parameters of operation.
     */
    private List<ParameterModel> inputs;
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
    
    /**
     * @return Returns the processId.
     */
    public String getProcessId() {
        return processId;
    }
    
    /**
     * @param processId The processId to set.
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    
    /**
     * @return Returns the inputs.
     */
    public List<ParameterModel> getInputs() {
        return inputs;
    }
    
    /**
     * @param inputs The inputs to set.
     */
    public void setInputs(List<ParameterModel> inputs) {
        this.inputs = inputs;
    }
    
}
