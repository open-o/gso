
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
