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

package org.openo.gso.servicemgr.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.dao.inf.IServiceModelDao;
import org.openo.gso.servicemgr.exception.ErrorCode;
import org.openo.gso.servicemgr.mapper.ServiceModelMapper;
import org.openo.gso.servicemgr.mapper.ServicePackageMapper;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database operation class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/3
 */
public class ServiceModelDaoImpl implements IServiceModelDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModelDaoImpl.class);

    /**
     * Sql session.
     */
    private SqlSession session;

    /**
     * @return Returns the session.
     */
    public SqlSession getSession() {
        return session;
    }

    /**
     * @param session The session to set.
     */
    public void setSession(SqlSession session) {
        this.session = session;
    }

    /**
     * Insert service stances.<br/>
     * 
     * @param serviceModel service instance
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void insert(ServiceModel serviceModel) throws ServiceException {
        try {
            // 1. Check data validation.
            if((null == serviceModel) || (null == serviceModel.getServicePackage())) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Insert basic information of service instance.
            ServiceModelMapper serviceModelMapper = getMapper(ServiceModelMapper.class);
            serviceModelMapper.insert(serviceModel);

            // 3. Insert mapping relation between service instance an service package.
            ServicePackageMapper servicePackageMapper = getMapper(ServicePackageMapper.class);
            servicePackageMapper.insert(serviceModel.getServicePackage());
        } catch(Exception e) {
            LOGGER.error("Fail to insert service instance. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Delete service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) throws ServiceException {
        try {
            // 1. Delete service instance.
            ServiceModelMapper serviceModelMapper = getMapper(ServiceModelMapper.class);
            serviceModelMapper.delete(serviceId);

            // 2. Delete mapping relation between service instance and service package.
            ServicePackageMapper servicePackageMapper = getMapper(ServicePackageMapper.class);
            servicePackageMapper.delete(serviceId);

        } catch(Exception e) {
            LOGGER.error("Fail to delete service instance. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Query all service instances.<br/>
     * 
     * @return service instance
     * @throws ServiceException when database exception
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> queryAllServices() throws ServiceException {
        try {
            // 1. Query basic information of service instance.
            List<ServiceModel> services = getMapper(ServiceModelMapper.class).queryAllServices();
            if(CollectionUtils.isEmpty(services)) {
                return null;
            }

            // 2. Query relation data.
            List<ServicePackageMapping> serviceMappings = getMapper(ServicePackageMapper.class).queryAllMappings();

            // 3. Compose data.
            return composeData(services, serviceMappings);

        } catch(Exception e) {
            LOGGER.error("Fail to delete service instance. {}", e);
        }
        throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
    }

    /**
     * Get mapper to operate database.<br/>
     * 
     * @param type class type of Mapper
     * @return Mapper object
     * @since GSO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        return this.session.getMapper(type);
    }

    /**
     * Calculate the mapping relation of service.<br/>
     * 
     * @param services service instances.
     * @param mapings mapping relation
     * @return service instances
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private List<ServiceModel> composeData(List<ServiceModel> services, List<ServicePackageMapping> mapings) {
        if(CollectionUtils.isEmpty(mapings)) {
            return services;
        }

        Map<String, ServicePackageMapping> relationsMap = new HashedMap();
        for(ServicePackageMapping relation : mapings) {
            relationsMap.put(relation.getServiceId(), relation);
        }

        for(ServiceModel service : services) {
            ServicePackageMapping packageMapping = relationsMap.get(service.getServiceId());
            if(null != packageMapping) {
                service.setServicePackage(packageMapping);
            }
        }
        return services;
    }
}
