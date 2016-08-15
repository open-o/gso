/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.servicemgr.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.dao.inf.IServiceModelDao;
import org.openo.gso.servicemgr.dao.inf.ISubServiceDao;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;
import org.openo.gso.servicemgr.service.inf.IServiceManager;

/**
 * Service management class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServiceManagerImpl implements IServiceManager {

    /**
     * DAO to operate service instance.
     */
    private IServiceModelDao serviceModelDao;

    /**
     * DAO to operate sub-service instance.
     */
    private ISubServiceDao subServiceDao;

    /**
     * Create service instance.<br/>
     * 
     * @param serviceModel service instance
     * @param httpRequest http request
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public void createService(ServiceModel serviceModel, HttpServletRequest httpRequest) throws ServiceException {
        this.serviceModelDao.insert(serviceModel);
    }

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public void deleteService(String serviceId, HttpServletRequest httpRequest) throws ServiceException {
        this.serviceModelDao.delete(serviceId);
    }

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> getAllInstances() throws ServiceException {
        return this.serviceModelDao.queryAllServices();
    }

    /**
     * Query all sub-service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public List<SubServiceModel> getSubServices(String serviceId) throws ServiceException {
        return subServiceDao.querySubServices(serviceId);
    }

    /**
     * @return Returns the serviceModelDao.
     */
    public IServiceModelDao getServiceModelDao() {
        return serviceModelDao;
    }

    /**
     * @param serviceModelDao The serviceModelDao to set.
     */
    public void setServiceModelDao(IServiceModelDao serviceModelDao) {
        this.serviceModelDao = serviceModelDao;
    }

    /**
     * @return Returns the subServiceDao.
     */
    public ISubServiceDao getSubServiceDao() {
        return subServiceDao;
    }

    /**
     * @param subServiceDao The subServiceDao to set.
     */
    public void setSubServiceDao(ISubServiceDao subServiceDao) {
        this.subServiceDao = subServiceDao;
    }
}
