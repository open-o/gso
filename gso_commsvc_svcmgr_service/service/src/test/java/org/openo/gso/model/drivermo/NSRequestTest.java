
package org.openo.gso.model.drivermo;

import org.junit.Test;


public class NSRequestTest {

    NSRequest request = new NSRequest();
    @Test
    public void testGetStrInstanceId() {
        request.getNsdId();
    }

    @Test
    public void testSetStrInstanceId() {
        request.setNsdId("id");
    }

    @Test
    public void testGetNsName() {
        request.getNsName();
    }

    @Test
    public void testSetNsName() {
        request.setNsName("nsName");
    }

    @Test
    public void testGetNsDescription() {
        request.getNsDescription();
    }

    @Test
    public void testSetNsDescription() {
        request.setNsDescription("nsDesc");
    }

}
