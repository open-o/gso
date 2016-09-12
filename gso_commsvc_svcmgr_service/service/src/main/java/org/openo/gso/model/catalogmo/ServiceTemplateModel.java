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

package org.openo.gso.model.catalogmo;

import java.util.List;

/**
 * Structure of service template.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2016/8/12
 */
public class ServiceTemplateModel {
    
    /**
     * Uuid of service template.
     */
    private String sterviceTemplateId;
    
    /**
     * Name of service template.
     */
    private String templateName;
    
    /**
     * Vendor of service template.
     */
    private String vendor;
    
    /**
     * Version of service template.
     */
    private String version;
    
    /**
     * Csar id which is related with service template.
     */
    private String csarid;
    
    /**
     * Type of service template.
     */
    private String type;
    
    /**
     * Download uri of service template.
     */
    private String downloadUri;
    
    /**
     * Input parameters of service template.
     */
    private List<CatalogParameterModel> inputs;
    
    /**
     * @return Returns the sterviceTemplateId.
     */
    public String getSterviceTemplateId() {
        return sterviceTemplateId;
    }
    
    /**
     * @param sterviceTemplateId The sterviceTemplateId to set.
     */
    public void setSterviceTemplateId(String sterviceTemplateId) {
        this.sterviceTemplateId = sterviceTemplateId;
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
     * @return Returns the vendor.
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param vendor The vendor to set.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * @return Returns the csarid.
     */
    public String getCsarid() {
        return csarid;
    }
    
    /**
     * @param csarid The csarid to set.
     */
    public void setCsarid(String csarid) {
        this.csarid = csarid;
    }
    
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
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
     * @return Returns the inputs.
     */
    public List<CatalogParameterModel> getInputs() {
        return inputs;
    }
    
    /**
     * @param inputs The inputs to set.
     */
    public void setInputs(List<CatalogParameterModel> inputs) {
        this.inputs = inputs;
    }
    
}
