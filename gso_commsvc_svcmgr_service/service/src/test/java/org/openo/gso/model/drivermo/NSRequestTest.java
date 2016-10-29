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


public class NSRequestTest {

    NSRequest request = new NSRequest();
    @Test
    public void testGetStrInstanceId() {
        request.getNsdId();
    }

    @Test
    public void testSetStrInstanceId() {
        request.setNsdId("id");
    }

    @Test
    public void testGetNsName() {
        request.getNsName();
    }

    @Test
    public void testSetNsName() {
        request.setNsName("nsName");
    }

    @Test
    public void testGetDescription() {
        request.getDescription();
    }

    @Test
    public void testSetDescription() {
        request.setDescription("nsDesc");
    }

}
