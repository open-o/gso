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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.service.inf.IPackageManager;
import org.openo.gso.synchronization.PackageOperationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Package manager for catalog.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/23
 */
public class PackageManagerImpl implements IPackageManager {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PackageManagerImpl.class);

    /**
     * Catalog proxy.
     */
    private ICatalogProxy catalogProxy;

    /**
     * Service package dao.
     */
    private IServicePackageDao servicePackageDao;

    /**
     * Set status of GSAR to be onBoard<br/>
     * 
     * @param serviceDefId ID of service package
     * @param request http request
     * @since GSO 0.5
     */
    @Override
    public void updateOnBoardStatus(String serviceDefId, HttpServletRequest request) {
        if(PackageOperationSingleton.getInstance().isCsarBeingDeleted(serviceDefId)) {
            LOGGER.error("CSAR package is being deleted.");
            throw new ApplicationException(HttpCode.RESPOND_CONFLICT, "CSAR package is being deleted.");
        }
        catalogProxy.updateGsarStatus(serviceDefId, request);
    }

    /**
     * Delete service package.<br/>
     * 
     * @param serviceDefId ID of service package
     * @param request http request
     * @since GSO 0.5
     */
    @Override
    public void deletePackage(String serviceDefId, HttpServletRequest request) {

        // 1. Check whether service instance exists.
        List<ServicePackageMapping> mappings = servicePackageDao.queryPackageMappings(serviceDefId);
        if(!(CollectionUtils.isEmpty(mappings))) {
            LOGGER.error("There are service instances related to this package. Pacakge is {}", serviceDefId);
            throw new ApplicationException(HttpCode.BAD_REQUEST,
                    "There are service instances related with service template.");
        }

        // 2. Check whether it is creating service instance with this csar.
        if(PackageOperationSingleton.getInstance().isCsarBeingUsed(serviceDefId)) {
            LOGGER.error("The service instance related with this package is being created. Pacakge is {}",
                    serviceDefId);
            throw new ApplicationException(HttpCode.BAD_REQUEST,
                    "The service instance related with this package is being created.");
        }

        // 3. Check whether csar is being deleted.
        if(PackageOperationSingleton.getInstance().isCsarBeingDeleted(serviceDefId)) {
            LOGGER.error("The package is being deleted. Pacakge is {}", serviceDefId);
            throw new ApplicationException(HttpCode.BAD_REQUEST, "The package is being deleted.");
        }

        // 4. Notify catalog to delete csar package
        PackageOperationSingleton.getInstance().addBeingDeletedCsarIds(serviceDefId);
        try {
            catalogProxy.deleteGsarPackage(serviceDefId, request);
        } catch(ApplicationException exception) {
            LOGGER.error("fail to delete gsar package", exception);
            throw exception;
        } finally {
            PackageOperationSingleton.getInstance().removeDeletedCsarIds(serviceDefId);
        }
    }

    /**
     * @return Returns the catalogProxy.
     */
    public ICatalogProxy getCatalogProxy() {
        return catalogProxy;
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }

    /**
     * @return Returns the servicePackageDao.
     */
    public IServicePackageDao getServicePackageDao() {
        return servicePackageDao;
    }

    /**
     * @param servicePackageDao The servicePackageDao to set.
     */
    public void setServicePackageDao(IServicePackageDao servicePackageDao) {
        this.servicePackageDao = servicePackageDao;
    }
}
