
package org.openo.gso.model.drivermo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class ServiceNodeTest {

    ServiceNode node = new ServiceNode();
    @Test
    public void testGetNodeType() {
        node.getNodeType();
    }

    @Test
    public void testSetNodeType() {
        node.setNodeType("vnf");
    }

    @Test
    public void testGetStNodeParam() {
        node.getStNodeParam();
    }

    @Test
    public void testSetStNodeParam() {
        Map<String, String> map = new HashMap<String, String>();
        List<Map<String, String>> stNodeParam = new ArrayList<Map<String, String>>();
        stNodeParam.add(map);
        node.setStNodeParam(stNodeParam);
    }

}
