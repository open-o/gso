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

import java.util.List;

import org.openo.gso.model.drivermo.NsParameters;

/**
 * service segment request model<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/1/20
 */
public class ServiceSegmentReq {

    /**
     * ip and port for the current domain
     */
    private String domainHost;
    
    /**
     * node template name
     */
    private String nodeTemplateName;
    
    /**
     * node type
     */
    private String nodeType;
    
    /**
     * sub segment list
     */
    private List<ServiceSegmentReq> segments;
    
    /**
     * parameters for ns
     */
    private NsParameters nsParameters;
    
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
    public List<ServiceSegmentReq> getSegments() {
        return segments;
    }
    
    /**
     * @param segments The segments to set.
     */
    public void setSegments(List<ServiceSegmentReq> segments) {
        this.segments = segments;
    }
    
    /**
     * @return Returns the nsParameters.
     */
    public NsParameters getNsParameters() {
        return nsParameters;
    }
    
    /**
     * @param nsParameters The nsParameters to set.
     */
    public void setNsParameters(NsParameters nsParameters) {
        this.nsParameters = nsParameters;
    }
}
