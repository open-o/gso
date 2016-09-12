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


public class ResponseDescriptorTest {

    ResponseDescriptor desc = new ResponseDescriptor();
    @Test
    public void testGetStatus() {
        desc.getStatus();
    }

    @Test
    public void testSetStatus() {
        desc.setStatus("finished");
    }

    @Test
    public void testGetProgress() {
        desc.getProgress();
    }

    @Test
    public void testSetProgress() {
        desc.setProgress(100);
    }

    @Test
    public void testGetStatusDescription() {
        desc.getStatusDescription();
    }

    @Test
    public void testSetStatusDescription() {
        desc.setStatusDescription("description");
    }

    @Test
    public void testGetErrorCode() {
        desc.getErrorCode();
    }

    @Test
    public void testSetErrorCode() {
        desc.setErrorCode(500);
    }

    @Test
    public void testGetResponseId() {
        desc.getResponseId();
    }

    @Test
    public void testSetResponseId() {
        desc.setResponseId(1);
    }

}
