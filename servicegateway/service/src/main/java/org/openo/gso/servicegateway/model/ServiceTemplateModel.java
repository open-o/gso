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

import java.util.Map;

/**
 * the service template model
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017.1.11
 */
public class ServiceTemplateModel {

    EnumServiceType templateType = EnumServiceType.UNKNOWN;

    Map<String, Object> templateDetail;

    public EnumServiceType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(EnumServiceType templateType) {
        this.templateType = templateType;
    }

    public Map<String, Object> getTemplateDetail() {
        return templateDetail;
    }

    public void setTemplateDetail(Map<String, Object> templateDetail) {
        this.templateDetail = templateDetail;
    }
}
