
package org.openo.gso.model.drivermo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class TerminateParamsTest {

    TerminateParams param = new TerminateParams();
    @Test
    public void testGetNodeType() {
        param.getNodeType();
    }

    @Test
    public void testSetNodeType() {
        param.setNodeType("dc");
    }

    @Test
    public void testGetInputParameters() {
        param.getInputParameters();
    }

    @Test
    public void testSetInputParameters() {
        Map<String, String> inputParameters = new HashMap<String, String>();
        param.setInputParameters(inputParameters);
    }

}
