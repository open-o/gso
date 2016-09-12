
package org.openo.gso.roa.impl;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.openo.gso.service.impl.DriverManagerImpl;
import org.openo.gso.service.inf.IDriverManager;

import mockit.Mocked;


public class DrivermgrRoaModuleImplTest {

    @Mocked
    IDriverManager driver;
    
    @Mocked
    HttpServletRequest servletReq;
    
    DrivermgrRoaModuleImpl impl;
    
    @Before
    public void init(){
        impl = new DrivermgrRoaModuleImpl();
    }
    
    @Test
    public void testGetDriver() {
        impl.getDriverManager();
    }

    @Test
    public void testSetDriver() {
       impl.setDriverManager(new DriverManagerImpl());
    }

    @Test
    public void testTerminateNetworkService() {

    }


    @Test
    public void testInstantiateNetworkService() {

    }

}
