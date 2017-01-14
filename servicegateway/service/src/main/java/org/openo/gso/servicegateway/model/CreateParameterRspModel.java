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
 * the response model for the generate parameters restful request
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.10
 */
public class CreateParameterRspModel {

    /**
     * the template id
     */
    String templateId;

    /**
     * the parameters generated
     */
    CreateParameterModel parameters;

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
    public CreateParameterModel getParameters() {
        return parameters;
    }

    /**
     * @param parameters The parameters to set.
     */
    public void setParameters(CreateParameterModel parameters) {
        this.parameters = parameters;
    }

}
