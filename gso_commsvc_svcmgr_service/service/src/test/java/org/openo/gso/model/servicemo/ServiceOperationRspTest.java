/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.model.servicemo;

import org.junit.Test;


/**
 * Service Operation Response Test<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/1/24
 */
public class ServiceOperationRspTest {

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceOperationRsp#getOperation()}.
     */
    @Test
    public void testGetOperation() {
        ServiceOperationRsp rsp = new ServiceOperationRsp();
        rsp.getOperation();
    }

    /**
     * Test method for {@link org.openo.gso.model.servicemo.ServiceOperationRsp#setOperation(org.openo.gso.model.servicemo.ServiceOperation)}.
     */
    @Test
    public void testSetOperation() {
        ServiceOperationRsp rsp = new ServiceOperationRsp();
        ServiceOperation operation = new ServiceOperation();
        rsp.setOperation(operation);
    }

}
