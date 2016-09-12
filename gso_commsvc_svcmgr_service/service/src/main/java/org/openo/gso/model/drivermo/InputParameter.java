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

/**
 * Input parameter for the service template<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 Sep 1, 2016
 */
public class InputParameter {

    String name;

    Type paramType;

    String description;

    String defaultValue;

    boolean required;

    /**
     * <br>
     * <p>
     * </p>
     * parameter type
     * @author
     * @version     GSO 0.5  2016/9/3
     */
    public enum Type {
        STRING, INTEGER, FLOAT, BOOLEAN
    }

    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @return Returns the paramType.
     */
    public Type getParamType() {
        return paramType;
    }

    
    /**
     * @param paramType The paramType to set.
     */
    public void setParamType(Type paramType) {
        this.paramType = paramType;
    }

    
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    
    /**
     * @return Returns the required.
     */
    public boolean isRequired() {
        return required;
    }

    
    /**
     * @param required The required to set.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }



}
