/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public void init() {
        impl = new DrivermgrRoaModuleImpl();
    }

    @Test
    public void testGetDriver() {
        impl.getDriverMgr();
    }

    @Test
    public void testSetDriver() {
        impl.setDriverMgr(new DriverManagerImpl());
    }

    @Test
    public void testCreateNFVONs() {
        impl.createNfvoNs(servletReq);
    }
    
    @Test
    public void testDeleteNFVONs() {
        impl.deleteNfvoNs(servletReq);
    }
    
    @Test
    public void testTerminateNFVONs() {
        impl.terminateNfvoNs(servletReq);
    }
    
    @Test
    public void testInstantiateNFVONs() {
        String nsInstanceId = "1";
        impl.instantiateNfvoNs(nsInstanceId, servletReq);
    }
    
    @Test
    public void testQueryNFVONsProgress() {
        String jobId = "1";
        impl.queryNfvoJobStatus(jobId);
    }
    
    @Test
    public void testCreateSDNONs() {
        impl.createSdnoNs(servletReq);
    }
    
    @Test
    public void testDeleteSDNONs() {
        impl.deleteSdnoNs(servletReq);
    }
    
    @Test
    public void testTerminateSDNONs() {
        impl.terminateSdnoNs(servletReq);
    }
    
    @Test
    public void testInstantiateSDNONs() {
        String nsInstanceId = "1";
        impl.instantiateSdnoNs(nsInstanceId, servletReq);
    }
    
    @Test
    public void testQuerySDNONsProgress() {
        String jobId = "1";
        impl.querySdnoJobStatus(jobId);
    }
    
    @Test
    public void testCreateGSONs() {
        impl.createGsoNs(servletReq);
    }
    
    @Test
    public void testDeleteGSONs() {
        impl.deleteGsoNs(servletReq);
    }
    
    @Test
    public void testQueryGSONsProgress() {
        String jobId = "1";
        impl.queryGsoJobStatus(jobId);
    }

}
