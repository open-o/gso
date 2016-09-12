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

import org.junit.Test;
import org.openo.gso.model.drivermo.InputParameter.Type;


public class InputParameterTest {
    
    InputParameter param = new InputParameter();
    
    @Test
    public void testGetName() {
        param.getName();
    }

    @Test
    public void testSetName() {
        param.setName("name");
    }

    @Test
    public void testGetParamType() {
        param.getParamType();
    }

    @Test
    public void testSetParamType() {
        param.setParamType(Type.STRING);
    }

    @Test
    public void testGetDescription() {
        param.getDescription();
    }

    @Test
    public void testSetDescription() {
        param.setDescription("desc");
    }

    @Test
    public void testGetDefaultValue() {
        param.getDefaultValue();
    }

    @Test
    public void testSetDefaultValue() {
        param.setDefaultValue("default");
    }

    @Test
    public void testIsRequired() {
        param.isRequired();
    }

    @Test
    public void testSetRequired() {
        param.setRequired(true);;
    }

}
