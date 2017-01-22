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
 * serivce create request detail<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/1/20
 */
public class ServiceCreateReqDetail {
    
    /**
     * service name
     */
    private String name;
    
    /**
     * service description
     */
    private String description;
    
    /**
     * service def id
     */
    private String serviceDefId;
    
    /**
     * service template id
     */
    private String templateId;
    
    /**
     * parameters for for the service
     */
    private ServiceSegmentReq parameters;
    
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
     * @return Returns the serviceDefId.
     */
    public String getServiceDefId() {
        return serviceDefId;
    }
    
    /**
     * @param serviceDefId The serviceDefId to set.
     */
    public void setServiceDefId(String serviceDefId) {
        this.serviceDefId = serviceDefId;
    }
    
    /**
     * @return Returns the templateId.
     */
    public String getTemplateId() {
        return templateId;
    }
    
    /**
     * @param templateId The templateId to set.
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    /**
     * @return Returns the parameters.
     */
    public ServiceSegmentReq getParameters() {
        return parameters;
    }
    
    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(ServiceSegmentReq parameters) {
        this.parameters = parameters;
    }
    
}
