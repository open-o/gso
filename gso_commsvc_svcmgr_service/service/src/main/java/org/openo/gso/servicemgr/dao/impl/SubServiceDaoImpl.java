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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.dao.inf.ISubServiceDao;
import org.openo.gso.servicemgr.exception.ErrorCode;
import org.openo.gso.servicemgr.mapper.SubServiceMapper;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database operation Class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class SubServiceDaoImpl implements ISubServiceDao {

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
     * Insert sub-service instances in batch.<br/>
     * 
     * @param subServices sub-service instances
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void batchInsert(List<SubServiceModel> subServices) throws ServiceException {
        try {
            // 1. Check data validation.
            if(CollectionUtils.isEmpty(subServices)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Insert instances in batch.
            SubServiceMapper subServiceMapper = getMapper(SubServiceMapper.class);
            subServiceMapper.batchInsert(subServices);
        } catch(Exception e) {
            LOGGER.error("Fail to insert sub-service instances.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Delete sub-service instance by service ID.<br/>
     * 
     * @param serviceId service instance ID
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

            // 2.Delete sub-service instances.
            SubServiceMapper subServiceMapper = getMapper(SubServiceMapper.class);
            subServiceMapper.delete(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to delete sub-service instances.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Query sub-service instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances.
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public List<SubServiceModel> querySubServices(String serviceId) throws ServiceException {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Query sub-service instances.
            SubServiceMapper subServiceMapper = getMapper(SubServiceMapper.class);
            return subServiceMapper.querySubServices(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query sub-service instances.", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Get Sql mapper.<br/>
     * 
     * @param type Mapper class type
     * @return Mapper object
     * @since GSO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        return this.session.getMapper(type);
    }
}
