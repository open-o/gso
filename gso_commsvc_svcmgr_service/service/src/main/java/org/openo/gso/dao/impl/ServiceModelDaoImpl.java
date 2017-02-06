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

package org.openo.gso.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.SqlSession;
import org.openo.gso.commsvc.common.exception.ApplicationException;
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
import org.openo.gso.util.convertor.DataConverter;
import org.openo.gso.util.json.JsonUtil;
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
     * @since GSO 0.5
     */
    @Override
    public void insert(ServiceModel serviceModel) {
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
            if(null != serviceModel.getParameter()) {
                getMapper(ServiceParameterMapper.class)
                        .insert(DataConverter.convertDBParam(serviceModel.getServiceId(), serviceModel.getParameter()));
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
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) {
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
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> queryAllServices() {
        try {
            List<ServiceModel> services;
            // 1. Query basic information of service instance.
            services = getMapper(ServiceModelMapper.class).queryAllServices();
            if(CollectionUtils.isEmpty(services)) {
                return services;
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
     * @since GSO 0.5
     */
    @Override
    public ServiceModel queryServiceByInstanceId(String serviceId) {
        try {
            ServiceModel model = getMapper(ServiceModelMapper.class).queryServiceByInstanceId(serviceId);
            model.setServicePackage(getMapper(ServicePackageMapper.class).queryPackageMapping(serviceId));
            ServiceParameter param = getMapper(ServiceParameterMapper.class).query(serviceId);
            if(null != param) {
                model.setParameter(JsonUtil.unMarshal(param.getParamValue(), Map.class));
            }
            return model;
        } catch(Exception exception) {
            LOGGER.error("Fail to query service instance by id : {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Update service instance execution result.<br/>
     * 
     * @param serviceId service instance ID
     * @param status service instance execution status
     * @since GSO 0.5
     */
    @Override
    public void updateServiceStatus(String serviceId, String status) {
        try {
            getMapper(ServiceModelMapper.class).updateServiceStatus(serviceId, status);
        } catch(Exception exception) {
            LOGGER.error("Fail to update the service : {}, result : {}", serviceId, exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }

    }

    /**
     * Query service instances by status.<br/>
     * 
     * @param status service instance status, finished|processing|error
     * @return service instances
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> queryServiceByStatus(String status) {
        try {
            return getMapper(ServiceModelMapper.class).queryServiceByStatus(status);
        } catch(Exception exception) {
            LOGGER.error("Fail to query service instances by status: {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Batch update service instances.<br/>
     * 
     * @param services service instances
     * @since GSO 0.5
     */
    @Override
    public void batchUpdate(List<ServiceModel> services) {
        if(CollectionUtils.isEmpty(services)) {
            LOGGER.info("There is no service which need to update.");
            return;
        }

        try {
            LOGGER.info("Batch  update services: {}", services);
            getMapper(ServiceModelMapper.class).batchUpdate(services);
        } catch(Exception exception) {
            LOGGER.error("Fail to batch update service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Batch delete service data, including service instance, service parameters and package
     * mapping.<br/>
     * 
     * @param svcIds service instance ids
     * @since GSO 0.5
     */
    @Override
    public void batchDelete(List<String> svcIds) {
        if(CollectionUtils.isEmpty(svcIds)) {
            LOGGER.info("There is no service which need to delete.");
            return;
        }
        try {
            LOGGER.info("Batch delete services, the service ids are: {}", svcIds);
            getMapper(ServiceModelMapper.class).batchDelete(svcIds);
        } catch(Exception exception) {
            LOGGER.error("Fail to batch delete service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
}
