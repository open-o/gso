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

package org.openo.gso.service.impl;

import org.junit.Test;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.restproxy.inf.ICatalogProxy;

import junit.framework.Assert;
import mockit.Mocked;

public class DriverManagerImplTest {

    @Mocked
    IServiceSegmentDao serviceSegmentDao;
    
    @Mocked
    IServiceModelDao serviceModelDao;
    
    @Mocked
    IServicePackageDao servicePackageDao;
    
    @Mocked
    ICatalogProxy catalogProxy;
   
    @Test
    public void test() {
        DriverManagerImpl impl = new DriverManagerImpl();
        impl.setServiceSegmentDao(serviceSegmentDao);
        impl.setServiceModelDao(serviceModelDao);
        impl.setServicePackageDao(servicePackageDao);
        impl.setCatalogProxy(catalogProxy);

        Assert.assertNotNull(impl.getServiceSegmentDao());
        Assert.assertNotNull(impl.getServiceModelDao());
        Assert.assertNotNull(impl.getServicePackageDao());
        Assert.assertNotNull(impl.getCatalogProxy());
    }

}
