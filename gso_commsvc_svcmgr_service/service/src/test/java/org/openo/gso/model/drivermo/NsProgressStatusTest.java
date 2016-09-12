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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class NsProgressStatusTest {

    NsProgressStatus status = new NsProgressStatus();
    @Test
    public void testGetJobId() {
        status.getJobId();
    }

    @Test
    public void testSetJobId() {
        status.setJobId("id");
    }

    @Test
    public void testGetRspDescriptor() {
        status.getRspDescriptor();
    }

    @Test
    public void testSetRspDescriptor() {
        status.setRspDescriptor(new ResponseDescriptor());
    }

    @Test
    public void testGetRspHistoryList() {
        status.getRspHistoryList();
    }

    @Test
    public void testSetRspHistoryList() {
        List<ResponseDescriptor> rspHistoryList = new ArrayList<ResponseDescriptor>();
        status.setRspHistoryList(rspHistoryList);
    }

}
