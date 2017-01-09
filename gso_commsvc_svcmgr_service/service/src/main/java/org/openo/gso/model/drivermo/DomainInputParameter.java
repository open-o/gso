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
package org.openo.gso.model.drivermo;

import java.util.List;
import java.util.Map;

/**
 * Input Parameter For Each Domain<br>
 * <p>
 * </p>
 * @version     GSO 0.5  2017/1/7
 */
public class DomainInputParameter {
    
    private String serviceId;
    
    private String subServiceName;
    
    private String subServiceDesc;
    
    private String domainHost;
    
    private String nodeTemplateName;
    
    private String nodeType;
    
    private List<Object> parameters;
    
    private Map<String, String> additionalParamForNs;

    
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
     * @return Returns the subServiceName.
     */
    public String getSubServiceName() {
        return subServiceName;
    }

    
    /**
     * @param subServiceName The subServiceName to set.
     */
    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    
    /**
     * @return Returns the subServiceDesc.
     */
    public String getSubServiceDesc() {
        return subServiceDesc;
    }

    
    /**
     * @param subServiceDesc The subServiceDesc to set.
     */
    public void setSubServiceDesc(String subServiceDesc) {
        this.subServiceDesc = subServiceDesc;
    }

    
    /**
     * @return Returns the domainHost.
     */
    public String getDomainHost() {
        return domainHost;
    }

    
    /**
     * @param domainHost The domainHost to set.
     */
    public void setDomainHost(String domainHost) {
        this.domainHost = domainHost;
    }

    
    /**
     * @return Returns the nodeTemplateName.
     */
    public String getNodeTemplateName() {
        return nodeTemplateName;
    }

    
    /**
     * @param nodeTemplateName The nodeTemplateName to set.
     */
    public void setNodeTemplateName(String nodeTemplateName) {
        this.nodeTemplateName = nodeTemplateName;
    }

    
    /**
     * @return Returns the nodeType.
     */
    public String getNodeType() {
        return nodeType;
    }

    
    /**
     * @param nodeType The nodeType to set.
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    
    /**
     * @return Returns the parameters.
     */
    public List<Object> getParameters() {
        return parameters;
    }

    
    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
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
