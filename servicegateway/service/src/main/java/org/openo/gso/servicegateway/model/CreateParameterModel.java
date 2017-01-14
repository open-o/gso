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
 * the parameters for one object
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017.1.10
 */
public class CreateParameterModel {

    /**
     * the domain for the subobject of the template
     */
    private ParameterDefineModel domainHost;

    /**
     * the nodeTemplateName defined in the template
     */
    private String nodeTemplateName = "";

    /**
     * the nodeType defined in the template
     */
    private String nodeType = "";

    /**
     * if the subobject is gso. the parameters is used for its all subobjects
     */
    private List<CreateParameterModel> segments = new ArrayList<CreateParameterModel>();

    /**
     * the additionalParamForNs is used for the ns itself, both gso,sdno, nfvo
     * this params is defined in the template. and both with the location and sdn controller.
     */
    private List<ParameterDefineModel> additionalParamForNs = new ArrayList<ParameterDefineModel>();

    /**
     * @return Returns the domainHost.
     */
    public ParameterDefineModel getDomainHost() {
        return domainHost;
    }

    /**
     * @param domainHost The domainHost to set.
     */
    public void setDomainHost(ParameterDefineModel domainHost) {
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
     * @return Returns the segments.
     */
    public List<CreateParameterModel> getSegments() {
        return segments;
    }

    
    /**
     * @param segments The segments to set.
     */
    public void setSegments(List<CreateParameterModel> segments) {
        this.segments = segments;
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
