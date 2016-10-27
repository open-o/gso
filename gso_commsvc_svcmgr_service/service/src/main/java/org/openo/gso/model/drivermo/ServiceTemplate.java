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

import java.util.List;

/**
 * Service Template definition<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 Sep 1, 2016
 */
public class ServiceTemplate {

    String serviceTemplateId;
    
    String id;

    String serviceTemplateName;

    String vendorName;

    String templateVersion;

    TemplateType tmplType;

    String csarId;

    String downloadUri;

    List<InputParameter> lstInputs;

    List<OutputParameter> lstOutputs;

    /**
     * <br>
     * <p>
     * </p>
     * service template type
     * @author
     * @version     GSO 0.5  2016/9/3
     */
    public enum TemplateType {
        NS, VNF
    }

    
    /**
     * @return Returns the serviceTemplateId.
     */
    public String getServiceTemplateId() {
        return serviceTemplateId;
    }

    
    /**
     * @param serviceTemplateId The serviceTemplateId to set.
     */
    public void setServiceTemplateId(String serviceTemplateId) {
        this.serviceTemplateId = serviceTemplateId;
    }
    
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return Returns the serviceTemplateName.
     */
    public String getServiceTemplateName() {
        return serviceTemplateName;
    }

    
    /**
     * @param serviceTemplateName The serviceTemplateName to set.
     */
    public void setServiceTemplateName(String serviceTemplateName) {
        this.serviceTemplateName = serviceTemplateName;
    }

    
    /**
     * @return Returns the vendorName.
     */
    public String getVendorName() {
        return vendorName;
    }

    
    /**
     * @param vendorName The vendorName to set.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    
    /**
     * @return Returns the templateVersion.
     */
    public String getTemplateVersion() {
        return templateVersion;
    }

    
    /**
     * @param templateVersion The templateVersion to set.
     */
    public void setTemplateVersion(String templateVersion) {
        this.templateVersion = templateVersion;
    }

    
    /**
     * @return Returns the tmplType.
     */
    public TemplateType getTmplType() {
        return tmplType;
    }

    
    /**
     * @param tmplType The tmplType to set.
     */
    public void setTmplType(TemplateType tmplType) {
        this.tmplType = tmplType;
    }

    
    /**
     * @return Returns the csarId.
     */
    public String getCsarId() {
        return csarId;
    }

    
    /**
     * @param csarId The csarId to set.
     */
    public void setCsarId(String csarId) {
        this.csarId = csarId;
    }

    
    /**
     * @return Returns the downloadUri.
     */
    public String getDownloadUri() {
        return downloadUri;
    }

    
    /**
     * @param downloadUri The downloadUri to set.
     */
    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    
    /**
     * @return Returns the lstInputs.
     */
    public List<InputParameter> getLstInputs() {
        return lstInputs;
    }

    
    /**
     * @param lstInputs The lstInputs to set.
     */
    public void setLstInputs(List<InputParameter> lstInputs) {
        this.lstInputs = lstInputs;
    }

    
    /**
     * @return Returns the lstOutputs.
     */
    public List<OutputParameter> getLstOutputs() {
        return lstOutputs;
    }

    
    /**
     * @param lstOutputs The lstOutputs to set.
     */
    public void setLstOutputs(List<OutputParameter> lstOutputs) {
        this.lstOutputs = lstOutputs;
    }



}
