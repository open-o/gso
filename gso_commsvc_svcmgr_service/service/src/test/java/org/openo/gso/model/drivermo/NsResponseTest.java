
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
