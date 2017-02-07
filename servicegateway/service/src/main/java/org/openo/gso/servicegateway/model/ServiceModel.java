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
 * the service model for query
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO Mercury Release 2017.1.22
 */
public class ServiceModel {

    /**
     * service id
     */
    private String serviceId = "";

    /**
     * service name
     */
    private String serviceName = "";

    /**
     * description
     */
    private String description = "";

    /**
     * create time
     */
    private String createTime = "";

    /**
     * creator
     */
    private String creator = "";

    /**
     * service type:GSO,SDNO, NFVO
     */
    private String serviceType = "";

    /**
     * template name
     */
    private String templateName = "";

    /**
     * the parameters
     */
    private ServiceParameterModel inputParameters;

    
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
     * @return Returns the serviceName.
     */
    public String getServiceName() {
        return serviceName;
    }

    
    /**
     * @param serviceName The serviceName to set.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
     * @return Returns the createTime.
     */
    public String getCreateTime() {
        return createTime;
    }

    
    /**
     * @param createTime The createTime to set.
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    /**
     * @return Returns the creator.
     */
    public String getCreator() {
        return creator;
    }

    
    /**
     * @param creator The creator to set.
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    
    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    
    /**
     * @param serviceType The serviceType to set.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    
    /**
     * @return Returns the templateName.
     */
    public String getTemplateName() {
        return templateName;
    }

    
    /**
     * @param templateName The templateName to set.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    
    /**
     * @return Returns the inputParameters.
     */
    public ServiceParameterModel getInputParameters() {
        return inputParameters;
    }


    
    /**
     * @param inputParameters The inputParameters to set.
     */
    public void setInputParameters(ServiceParameterModel inputParameters) {
        this.inputParameters = inputParameters;
    }
    
}
