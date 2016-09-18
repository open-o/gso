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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class NsInstantiateReqTest {

    NsInstantiateReq req = new NsInstantiateReq();
    @Test
    public void testGetStrInstanceId() {
        req.getNsInstanceId();
    }

    @Test
    public void testSetStrInstanceId() {
        req.setNsInstanceId("id");
    }

    @Test
    public void testGetAdditionalParamForNs() {
        req.getAdditionalParamForNs();
    }

    @Test
    public void testSetAdditionalParamForNs() {
        Map<String, String> additionalParamForNs = new HashMap<String, String>();
        additionalParamForNs.put("key", "value");
        req.setAdditionalParamForNs(additionalParamForNs);
    }

}
