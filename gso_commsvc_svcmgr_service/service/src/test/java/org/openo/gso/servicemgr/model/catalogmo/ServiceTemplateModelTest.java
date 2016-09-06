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

package org.openo.gso.servicemgr.model.catalogmo;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test ServiceTemplateModel.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/31
 */
public class ServiceTemplateModelTest {

    @Test
    public void test() {
        ServiceTemplateModel model = new ServiceTemplateModel();
        model.setCsarid("123456789");
        model.setDownloadUri("http://localhost:8080/test");
        ParameterModel parameter = new ParameterModel();
        List<ParameterModel> lstPara = new LinkedList<ParameterModel>();
        lstPara.add(parameter);
        model.setInputs(lstPara);
        model.setSterviceTemplateId("1234567890");
        model.setTemplateName("gso");
        model.setType("gso");
        model.setVendor("gso");
        model.setVersion("v1r1");

        Assert.assertNotNull(model.getCsarid());
        Assert.assertNotNull(model.getDownloadUri());
        Assert.assertNotNull(model.getSterviceTemplateId());
        Assert.assertNotNull(model.getTemplateName());
        Assert.assertNotNull(model.getType());
        Assert.assertNotNull(model.getVendor());
        Assert.assertNotNull(model.getVersion());
        Assert.assertNotNull(model.getInputs());
    }

}
