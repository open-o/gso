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
        String nsInstanceId = "1";
        impl.deleteNfvoNs(nsInstanceId);
    }
    
    @Test
    public void testTerminateNFVONs() {
        String nsInstanceId = "1";
        impl.terminateNfvoNs(nsInstanceId, servletReq);
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
        String nsInstanceId = "1";
        impl.deleteSdnoNs(nsInstanceId);
    }
    
    @Test
    public void testTerminateSDNONs() {
        String nsInstanceId = "1";
        impl.terminateSdnoNs(nsInstanceId, servletReq);
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

}
