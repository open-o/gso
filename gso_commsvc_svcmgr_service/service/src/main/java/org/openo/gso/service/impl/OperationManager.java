/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.dao.inf.IServiceOperDao;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.service.inf.IOperationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service operation implementation class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public class OperationManager implements IOperationManager {

    /**
     * Log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationManager.class);

    /**
     * Service operation DAO.
     */
    private IServiceOperDao serviceOperDao;

    /**
     * @return Returns the serviceOperDao.
     */
    public IServiceOperDao getServiceOperDao() {
        return serviceOperDao;
    }

    /**
     * @param serviceOperDao The serviceOperDao to set.
     */
    public void setServiceOperDao(IServiceOperDao serviceOperDao) {
        this.serviceOperDao = serviceOperDao;
    }

    /**
     * Create service operation.<br/>
     * 
     * @param serviceId service instance ID.
     * @param operationType service operation type.
     * @throws ApplicationException when fail to operate database.
     * @since GSO 0.5
     */
    @Override
    public ServiceOperation createOperation(String serviceId, String operationType) throws ApplicationException {

        // construct data
        ServiceOperation operation = new ServiceOperation(serviceId, operationType, null);

        // Insert operation into DB
        LOGGER.error("Service operation data: {}", operation);
        serviceOperDao.insert(operation);

        return operation;
    }

    /**
     * Update service operation.<br/>
     * 
     * @param svcOperation service operation data.
     * @throws ApplicationException when fail to operate database.
     * @since GSO 0.5
     */
    @Override
    public void updateOperation(ServiceOperation svcOperation) throws ApplicationException {
        LOGGER.error("Update service operation data: {}", svcOperation);
        serviceOperDao.update(svcOperation);
    }
}
