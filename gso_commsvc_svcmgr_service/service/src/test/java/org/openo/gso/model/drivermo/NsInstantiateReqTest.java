
package org.openo.gso.model.drivermo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class NsInstantiateReqTest {

    NsInstantiateReq req = new NsInstantiateReq();
    @Test
    public void testGetStrInstanceId() {
        req.getStrInstanceId();
    }

    @Test
    public void testSetStrInstanceId() {
        req.setStrInstanceId("id");
    }

    @Test
    public void testGetAdditionalParamForNs() {
        req.getAdditionalParamForNs();
    }

    @Test
    public void testSetAdditionalParamForNs() {
        Map<String, String> additionalParamForNs = new HashMap<String, String>();
        additionalParamForNs.put("key", "value");
        req.setAdditionalParamForNs(additionalParamForNs);
    }

}
