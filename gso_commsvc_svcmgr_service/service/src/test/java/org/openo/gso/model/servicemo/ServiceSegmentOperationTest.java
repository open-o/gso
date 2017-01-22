/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.model.servicemo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openo.gso.constant.CommonConstant;

public class ServiceSegmentOperationTest {

    @Test
    public void test() {
        ServiceSegmentOperation svcOper0 = new ServiceSegmentOperation("segmentId", "segmentType", "operType");

        ServiceSegmentOperation segOper = new ServiceSegmentOperation();
        segOper.setServiceSegmentId("1");
        segOper.setServiceSegmentType(CommonConstant.SegmentType.NFVO);
        segOper.setOperationType(CommonConstant.OperationType.CREATE);
        segOper.setServiceId("s1");
        segOper.setJobId("2");
        segOper.setErrorCode(200);
        segOper.setProgress(50);
        segOper.setStatus(CommonConstant.Status.PROCESSING);
        segOper.setStatusDescription("desc");

        assertEquals("1", segOper.getServiceSegmentId());
        assertEquals(CommonConstant.SegmentType.NFVO, segOper.getServiceSegmentType());
        assertEquals(CommonConstant.OperationType.CREATE, segOper.getOperationType());
        assertEquals("s1", segOper.getServiceId());
        assertEquals("2", segOper.getJobId());
        assertEquals(200, segOper.getErrorCode());
        assertEquals(50, segOper.getProgress());
        assertEquals(CommonConstant.Status.PROCESSING, segOper.getStatus());
        assertEquals("desc", segOper.getStatusDescription());
    }

}
