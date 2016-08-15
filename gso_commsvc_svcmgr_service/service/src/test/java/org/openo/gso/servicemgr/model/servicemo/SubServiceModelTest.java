/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicemgr.model.servicemo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test SubServiceModel Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class SubServiceModelTest {

    @Test
    public void test() {
        SubServiceModel subService = new SubServiceModel();
        subService.setNodeType("tosaca.nfv.node.POP");
        subService.setOwner("nfv");
        subService.setServiceId("2");
        subService.setSubServiceId("12345");
        subService.setSubServiceName("POP service");
        subService.setTemplateId("12345");
        subService.setTopoSeqNumber(1);

        assertEquals("tosaca.nfv.node.POP", subService.getNodeType());
        assertEquals("nfv", subService.getOwner());
        assertEquals("2", subService.getServiceId());
        assertEquals("POP service", subService.getSubServiceName());
        assertEquals("12345", subService.getSubServiceId());
        assertEquals("12345", subService.getTemplateId());
        assertEquals(String.valueOf(1), String.valueOf(subService.getTopoSeqNumber()));
    }

}
