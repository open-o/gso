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

package org.openo.gso.model.catalogmo;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.OperationModel;

/**
 * Test OperationModel.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/31
 */
public class OperationModelTest {

    @Test
    public void test() {
        OperationModel model = new OperationModel();
        model.setDescription("description");
        CatalogParameterModel parameter = new CatalogParameterModel();
        List<CatalogParameterModel> lstPara = new LinkedList<CatalogParameterModel>();
        lstPara.add(parameter);
        model.setInputs(lstPara);
        model.setName("name");
        model.setProcessId("processId");

        Assert.assertNotNull(model.getDescription());
        Assert.assertNotNull(model.getName());
        Assert.assertNotNull(model.getProcessId());
        Assert.assertNotNull(model.getInputs());
    }

}
