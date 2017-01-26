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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.ServicePackageMapper;
import org.openo.gso.model.servicemo.ServicePackageMapping;
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
public class ServicePackageDaoImpl implements IServicePackageDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicePackageDaoImpl.class);

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
     * Insert relation instance.<br/>
     * 
     * @param packageMapping service package mapping data
     * @since GSO 0.5
     */
    @Override
    public void insert(ServicePackageMapping packageMapping) {
        try {
            // 1. Check data validation.
            if(null == packageMapping) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
            }

            // 2. Insert mapping relation between service instance and service package.
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            packageMapper.insert(packageMapping);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert mapping relation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Delete relation instance by service ID.<br/>
     * 
     * @param serviceId service ID
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
            }

            // 2. Delete relation instance.
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            packageMapper.delete(serviceId);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert mapping relation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query all relation instances.<br/>
     * 
     * @return service instances
     * @since GSO 0.5
     */
    @Override
    public List<ServicePackageMapping> queryAllMappings() {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryAllMappings();
        } catch(Exception exception) {
            LOGGER.error("Fail to query all of mapping relations. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Get sql mapper.<br/>
     * 
     * @param type mapper class
     * @return mapp object
     * @since GSO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        SqlSession session = dbSessionHandler.getSqlSession();
        return session.getMapper(type);
    }

    /**
     * Query service package by service instance ID.<br/>
     * 
     * @param serviceId service instance ID.
     * @return service package
     *         .
     * @since GSO 0.5
     */
    @Override
    public ServicePackageMapping queryPackageMapping(String serviceId) {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryPackageMapping(serviceId);
        } catch(Exception exception) {
            LOGGER.error("Fail to query mapping relation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query service package by package ID.<br/>
     * 
     * @param serviceDefId service package ID
     * @return service packages
     * @since GSO 0.5
     */
    @Override
    public List<ServicePackageMapping> queryPackageMappings(String serviceDefId) {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryPackageMappings(serviceDefId);
        } catch(Exception exception) {
            LOGGER.error("Fail to query mapping relations. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
}
