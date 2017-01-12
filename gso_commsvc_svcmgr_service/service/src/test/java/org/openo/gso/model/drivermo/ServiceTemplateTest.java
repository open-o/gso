/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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
import org.openo.gso.model.drivermo.ServiceTemplate.TemplateType;


public class ServiceTemplateTest {

    ServiceTemplate tmp = new ServiceTemplate();
    @Test
    public void testGetServiceTemplateId() {
        tmp.getServiceTemplateId();
    }

    @Test
    public void testSetServiceTemplateId() {
        tmp.setServiceTemplateId("id");
    }

    @Test
    public void testGetServiceTemplateName() {
        tmp.getServiceTemplateName();
    }

    @Test
    public void testSetServiceTemplateName() {
        tmp.setServiceTemplateName("name");
    }

    @Test
    public void testGetVendorName() {
        tmp.getVendorName();
    }

    @Test
    public void testSetVendorName() {
        tmp.setVendorName("huawei");
    }

    @Test
    public void testGetTemplateVersion() {
        tmp.getTemplateVersion();
    }

    @Test
    public void testSetTemplateVersion() {
        tmp.setTemplateVersion("v1");
    }

    @Test
    public void testGetTmplType() {
        tmp.getTmplType();
    }

    @Test
    public void testSetTmplType() {
        tmp.setTmplType(TemplateType.NS);
    }

    @Test
    public void testGetCsarId() {
        tmp.getCsarId();
    }

    @Test
    public void testSetCsarId() {
        tmp.setCsarId("csarId");
    }

    @Test
    public void testGetDownloadUri() {
        tmp.getDownloadUri();
    }

    @Test
    public void testSetDownloadUri() {
        tmp.setDownloadUri("downloadUri");
    }

    @Test
    public void testGetLstInputs() {
        tmp.getLstInputs();
    }

    @Test
    public void testSetLstInputs() {
        List<InputParameter> lstInputs = new ArrayList<InputParameter>();
        tmp.setLstInputs(lstInputs);
    }

    @Test
    public void testGetLstOutputs() {
        tmp.getLstOutputs();
    }

    @Test
    public void testSetLstOutputs() {
        List<OutputParameter> lstOutputs = new ArrayList<OutputParameter>();
        tmp.setLstOutputs(lstOutputs);
    }
    
    @Test
    public void testSetId() {
        tmp.setId("1");
    }
    
    @Test
    public void testGetId() {
        tmp.getId();
    }

}
