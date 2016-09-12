
package org.openo.gso.model.drivermo;

import org.junit.Test;
import org.openo.gso.model.drivermo.InputParameter.Type;


public class InputParameterTest {
    
    InputParameter param = new InputParameter();
    
    @Test
    public void testGetName() {
        param.getName();
    }

    @Test
    public void testSetName() {
        param.setName("name");
    }

    @Test
    public void testGetParamType() {
        param.getParamType();
    }

    @Test
    public void testSetParamType() {
        param.setParamType(Type.STRING);
    }

    @Test
    public void testGetDescription() {
        param.getDescription();
    }

    @Test
    public void testSetDescription() {
        param.setDescription("desc");
    }

    @Test
    public void testGetDefaultValue() {
        param.getDefaultValue();
    }

    @Test
    public void testSetDefaultValue() {
        param.setDefaultValue("default");
    }

    @Test
    public void testIsRequired() {
        param.isRequired();
    }

    @Test
    public void testSetRequired() {
        param.setRequired(true);;
    }

}
