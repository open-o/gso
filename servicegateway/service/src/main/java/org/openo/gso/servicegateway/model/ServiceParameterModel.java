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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the service parameter model for query
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO Mercury Release  2017年1月22日
 */
public class ServiceParameterModel {

    /**
     * the domain for the subobject of the template
     */
    private String domainHost = "";

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
    private List<ServiceParameterModel> segments = new ArrayList<ServiceParameterModel>();

    /**
     * the additionalParamForNs is used for the ns itself, both gso,sdno, nfvo
     * this params is defined in the template. and both with the location and sdn controller.
     */
    private Map<String, String> additionalParamForNs = new HashMap<String, String>();

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
     * @return Returns the segments.
     */
    public List<ServiceParameterModel> getSegments() {
        return segments;
    }

    
    /**
     * @param segments The segments to set.
     */
    public void setSegments(List<ServiceParameterModel> segments) {
        this.segments = segments;
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
