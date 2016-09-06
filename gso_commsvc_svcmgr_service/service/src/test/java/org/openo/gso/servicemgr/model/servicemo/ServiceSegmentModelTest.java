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
 * Test ServiceSegmentModel Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServiceSegmentModelTest {

    @Test
    public void test() {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setNodeType("tosaca.nfv.node.POP");
        serviceSegment.setOwner("nfv");
        serviceSegment.setServiceId("2");
        serviceSegment.setServiceSegmentId("12345");
        serviceSegment.setServiceSegmentName("POP service");
        serviceSegment.setTemplateId("12345");
        serviceSegment.setTopoSeqNumber(1);

        assertEquals("tosaca.nfv.node.POP", serviceSegment.getNodeType());
        assertEquals("nfv", serviceSegment.getOwner());
        assertEquals("2", serviceSegment.getServiceId());
        assertEquals("POP service", serviceSegment.getServiceSegmentName());
        assertEquals("12345", serviceSegment.getServiceSegmentId());
        assertEquals("12345", serviceSegment.getTemplateId());
        assertEquals(String.valueOf(1), String.valueOf(serviceSegment.getTopoSeqNumber()));
    }

}
