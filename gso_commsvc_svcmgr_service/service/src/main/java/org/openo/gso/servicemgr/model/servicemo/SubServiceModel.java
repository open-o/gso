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

package org.openo.gso.servicemgr.model.servicemo;

/**
 * Sub-Service model class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class SubServiceModel {

    /**
     * Service instance ID.
     */
    private String serviceId;

    /**
     * Sub-service instance ID.
     */
    private String subServiceId;

    /**
     * Sub-service instance name.
     */
    private String subServiceName;

    /**
     * Sub-service definition template ID.
     */
    private String templateId;

    /**
     * Node type of sub-service. For example tosca.vfv.node.POP
     */
    private String nodeType;

    /**
     * Owner that sub-service instance belongs to.
     */
    private String owner;

    /**
     * The sequence number that sub-service instance in topology.
     */
    private int topoSeqNumber;

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
     * @return Returns the subServiceId.
     */
    public String getSubServiceId() {
        return subServiceId;
    }

    /**
     * @param subServiceId The subServiceId to set.
     */
    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
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
     * @return Returns the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
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

}
