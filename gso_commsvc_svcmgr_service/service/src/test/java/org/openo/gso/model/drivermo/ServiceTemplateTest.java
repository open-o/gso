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

}
