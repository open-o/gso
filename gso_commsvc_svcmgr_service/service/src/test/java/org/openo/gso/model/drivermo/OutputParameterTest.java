
package org.openo.gso.model.drivermo;

import org.junit.Test;


public class OutputParameterTest {

    OutputParameter oParam = new OutputParameter();
    @Test
    public void testGetName() {
        oParam.getName();
    }

    @Test
    public void testSetName() {
        oParam.setName("name");
    }

    @Test
    public void testGetDescription() {
        oParam.getDescription();
    }

    @Test
    public void testSetDescription() {
        oParam.setDescription("desc");
    }

    @Test
    public void testGetValue() {
        oParam.getValue();
    }

    @Test
    public void testSetValue() {
        oParam.setValue("value");
    }

}
