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

import java.util.Map;

/**
 * Node Template Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/7
 */
public class NodeTemplateModel {

    /**
     * Node ID.
     */
    private String id;

    /**
     * Node name.
     */
    private String name;

    /**
     * Node type.
     */
    private String type;

    /**
     * Node property.
     */
    private Map<String, Object> properties;

    /**
     * Relation between nodes.
     */
    private Object relationShips;

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
     * @return Returns the properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties The properties to set.
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * @return Returns the relationShips.
     */
    public Object getRelationShips() {
        return relationShips;
    }

    /**
     * @param relationShips The relationShips to set.
     */
    public void setRelationShips(Object relationShips) {
        this.relationShips = relationShips;
    }
}
