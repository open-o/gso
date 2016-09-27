/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.gso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.ServiceModelMapper;
import org.openo.gso.mapper.ServicePackageMapper;
import org.openo.gso.mapper.ServiceParameterMapper;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceParameter;
import org.openo.gso.util.validate.ValidateUtil;
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
     * Session handler.
     */
    private DatabaseSessionHandler dbSessionHandler;

    /**
     * @return Returns the dbSessionHandler.
     */
    public DatabaseSessionHandler getDbSessionHandler() {
        return dbSessionHandler;
    }

    /**
     * @param dbSessionHandler The dbSessionHandler to set.
     */
    public void setDbSessionHandler(DatabaseSessionHandler dbSessionHandler) {
        this.dbSessionHandler = dbSessionHandler;
    }

    /**
     * Insert service stances.<br/>
     * 
     * @param serviceModel service instance
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void insert(ServiceModel serviceModel) throws ApplicationException {
        try {
            // 1. Check data validation.
            if((null == serviceModel) || (null == serviceModel.getServicePackage())) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
            }

            // 2. Insert basic information of service instance.
            ServiceModelMapper serviceModelMapper = getMapper(ServiceModelMapper.class);
            serviceModelMapper.insert(serviceModel);

            // 3. Insert mapping relation between service instance an service package.
            ServicePackageMapper servicePackageMapper = getMapper(ServicePackageMapper.class);
            servicePackageMapper.insert(serviceModel.getServicePackage());

            // 4. Insert service parameters
            List<ServiceParameter> parameters = serviceModel.getParameters();
            if(!CollectionUtils.isEmpty(parameters)) {
                ServiceParameterMapper paramMapper = getMapper(ServiceParameterMapper.class);
                paramMapper.batchInsert(parameters);
            }
        } catch(Exception exception) {
            LOGGER.error("Fail to insert service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Delete service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) throws ApplicationException {
        try {
            ValidateUtil.assertStringNotNull(serviceId);

            // 1. Delete service instance.
            ServiceModelMapper serviceModelMapper = getMapper(ServiceModelMapper.class);
            serviceModelMapper.delete(serviceId);

            // 2. Delete mapping relation between service instance and service package.
            ServicePackageMapper servicePackageMapper = getMapper(ServicePackageMapper.class);
            servicePackageMapper.delete(serviceId);

            // 3. Delete service parameters
            ServiceParameterMapper parameterMapper = getMapper(ServiceParameterMapper.class);
            parameterMapper.delete(serviceId);

        } catch(Exception exception) {
            LOGGER.error("Fail to delete service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query all service instances.<br/>
     * 
     * @return service instance
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> queryAllServices() throws ApplicationException {
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

        } catch(Exception exception) {
            LOGGER.error("Fail to delete service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Get mapper to operate database.<br/>
     * 
     * @param type class type of Mapper
     * @return Mapper object
     * @since GSO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        SqlSession session = dbSessionHandler.getSqlSession();
        return session.getMapper(type);
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

    /**
     * Query some service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    @Override
    public ServiceModel queryServiceById(String serviceId) throws ApplicationException {
        try {
            return getMapper(ServicePackageMapper.class).queryServiceById(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to delete service instance. {}", e);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
}
