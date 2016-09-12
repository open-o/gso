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


public class NsResponseTest {

    NsResponse rsp = new NsResponse();
    @Test
    public void testGetServiceId() {
        rsp.getServiceId();
    }

    @Test
    public void testSetServiceId() {
        rsp.setServiceId("id");
    }

    @Test
    public void testGetSubServiceId() {
        rsp.getSubServiceId();
    }

    @Test
    public void testSetSubServiceId() {
        rsp.setSubServiceId("subId");
    }

    @Test
    public void testGetSubServiceType() {
        rsp.getSubServiceType();
    }

    @Test
    public void testSetSubServiceType() {
        rsp.setSubServiceType("pop");
    }

    @Test
    public void testGetSubServiceTmplId() {
        rsp.getSubServiceTmplId();
    }

    @Test
    public void testSetSubServiceTmplId() {
        rsp.setSubServiceTmplId("tmpId");
    }

    @Test
    public void testGetOperationType() {
        rsp.getOperationType();
    }

    @Test
    public void testSetOperationType() {
        rsp.setOperationType("delete");
    }

    @Test
    public void testGetStatus() {
        rsp.getStatus();
    }

    @Test
    public void testSetStatus() {
        rsp.setStatus("success");
    }

}
