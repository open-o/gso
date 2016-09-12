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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.exception.ErrorCode;
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
     * Insert relation instance.<br/>
     * 
     * @param packageMapping service package mapping data
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void insert(ServicePackageMapping packageMapping) throws ServiceException {
        try {
            // 1. Check data validation.
            if(null == packageMapping) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Insert mapping relation between service instance and service package.
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            packageMapper.insert(packageMapping);
        } catch(Exception e) {
            LOGGER.error("Fail to insert mapping relation. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Delete relation instance by service ID.<br/>
     * 
     * @param serviceId service ID
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) throws ServiceException {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Delete relation instance.
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            packageMapper.delete(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to insert mapping relation. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Query all relation instances.<br/>
     * 
     * @return service instances
     * @throws ServiceException when database exception
     * @since GSO 0.5
     */
    @Override
    public List<ServicePackageMapping> queryAllMappings() throws ServiceException {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryAllMappings();
        } catch(Exception e) {
            LOGGER.error("Fail to query all of mapping relations. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
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
        return this.session.getMapper(type);
    }

    /**
     * Query service package by service instance ID.<br/>
     * 
     * @param serviceId service instance ID.
     * @return service package
     * @throws ServiceException when fail to query.
     * @since GSO 0.5
     */
    @Override
    public ServicePackageMapping queryPackageMapping(String serviceId) throws ServiceException {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryPackageMapping(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query mapping relation. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Query service package by package ID.<br/>
     * 
     * @param serviceDefId service package ID
     * @return service packages
     * @throws ServiceException when fail to query
     * @since GSO 0.5
     */
    @Override
    public List<ServicePackageMapping> queryPackageMappings(String serviceDefId) throws ServiceException {
        try {
            ServicePackageMapper packageMapper = getMapper(ServicePackageMapper.class);
            return packageMapper.queryPackageMappings(serviceDefId);
        } catch(Exception e) {
            LOGGER.error("Fail to query mapping relations. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }
}
