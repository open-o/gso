/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class ServiceNodeTest {

    ServiceNode node = new ServiceNode();
    @Test
    public void testGetNodeTemplateName() {
        node.getNodeTemplateName();
    }

    @Test
    public void testSetNodeTemplateName() {
        node.setNodeTemplateName("pop1");
    }

    @Test
    public void testGetInputParameters() {
        node.getInputParameters();
    }

    @Test
    public void testSetInputParameters() {
        List<DomainInputParameter> paramsList= new ArrayList<DomainInputParameter>();
        node.setInputParameters(paramsList);
    }

}
