/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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
 * Service segment model class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServiceSegmentModel {

    /**
     * Service instance ID.
     */
    private String serviceId;

    /**
     * Service segment instance ID.
     */
    private String serviceSegmentId;
    
    /**
     * Service segment type
     */
    private String serviceSegmentType;

    /**
     * Service segment instance name.
     */
    private String serviceSegmentName;

    /**
     * Service segment definition template ID.
     */
    private String templateId;

    /**
     * Node type of Service segment. For example tosca.vfv.node.POP
     */
    private String nodeType;

    /**
     * The sequence number that Service segment instance in topology.
     */
    private int topoSeqNumber;

    /**
     * domain host
     */
    private String domainHost;
    
    /**
     * node template name
     */
    private String nodeTemplateName;

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
     * @return Returns the serviceSegmentId.
     */
    public String getServiceSegmentId() {
        return serviceSegmentId;
    }

    /**
     * @param serviceSegmentId The serviceSegmentId to set.
     */
    public void setServiceSegmentId(String serviceSegmentId) {
        this.serviceSegmentId = serviceSegmentId;
    }
    
    /**
     * @return Returns the serviceSegmentType.
     */
    public String getServiceSegmentType() {
        return serviceSegmentType;
    }
    
    /**
     * @param serviceSegmentType The serviceSegmentType to set.
     */
    public void setServiceSegmentType(String serviceSegmentType) {
        this.serviceSegmentType = serviceSegmentType;
    }

    /**
     * @return Returns the serviceSegmentName.
     */
    public String getServiceSegmentName() {
        return serviceSegmentName;
    }

    /**
     * @param serviceSegmentName The serviceSegmentName to set.
     */
    public void setServiceSegmentName(String serviceSegmentName) {
        this.serviceSegmentName = serviceSegmentName;
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
     * @return Returns the topoSeqNumber.
     */
    public int getTopoSeqNumber() {
        return topoSeqNumber;
    }

    /**
     * @param topoSeqNumber The topoSeqNumber to set.
     */
    public void setTopoSeqNumber(int topoSeqNumber) {
        this.topoSeqNumber = topoSeqNumber;
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

}
