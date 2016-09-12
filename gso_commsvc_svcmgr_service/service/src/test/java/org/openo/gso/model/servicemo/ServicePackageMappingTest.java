/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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
import org.openo.gso.model.servicemo.ServicePackageMapping;

/**
 * Test ServicePackageMapping Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServicePackageMappingTest {

    @Test
    public void test() {
        ServicePackageMapping servicePackage = new ServicePackageMapping();
        servicePackage.setServiceDefId("12345");
        servicePackage.setServiceId("2");
        servicePackage.setTemplateId("123456");
        servicePackage.setTemplateName("gso");

        assertEquals("12345", servicePackage.getServiceDefId());
        assertEquals("2", servicePackage.getServiceId());
        assertEquals("123456", servicePackage.getTemplateId());
        assertEquals("gso", servicePackage.getTemplateName());
    }

}
