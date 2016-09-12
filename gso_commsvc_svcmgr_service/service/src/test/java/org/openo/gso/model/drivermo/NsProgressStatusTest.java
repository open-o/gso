
package org.openo.gso.model.drivermo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class NsProgressStatusTest {

    NsProgressStatus status = new NsProgressStatus();
    @Test
    public void testGetJobId() {
        status.getJobId();
    }

    @Test
    public void testSetJobId() {
        status.setJobId("id");
    }

    @Test
    public void testGetRspDescriptor() {
        status.getRspDescriptor();
    }

    @Test
    public void testSetRspDescriptor() {
        status.setRspDescriptor(new ResponseDescriptor());
    }

    @Test
    public void testGetRspHistoryList() {
        status.getRspHistoryList();
    }

    @Test
    public void testSetRspHistoryList() {
        List<ResponseDescriptor> rspHistoryList = new ArrayList<ResponseDescriptor>();
        status.setRspHistoryList(rspHistoryList);
    }

}
