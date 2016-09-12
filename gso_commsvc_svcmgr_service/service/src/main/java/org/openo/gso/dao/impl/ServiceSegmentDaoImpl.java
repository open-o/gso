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
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.mapper.ServiceSegmentMapper;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.util.validate.ValidateUtil;
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
public class ServiceSegmentDaoImpl implements IServiceSegmentDao {

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
     * Insert service segment instances.<br/>
     * 
     * @param serviceSegments service segment instances
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void insert(ServiceSegmentModel serviceSegment) throws ServiceException {
        try {
            ValidateUtil.assertObjectNotNull(serviceSegment);
            ServiceSegmentMapper serviceSegmentMapper = getMapper(ServiceSegmentMapper.class);
            serviceSegmentMapper.insert(serviceSegment);
        } catch(Exception e) {
            LOGGER.error("Fail to insert service segment instance. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Delete service segment instance by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) throws ServiceException {
        try {
            // Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // Delete service segment instances.
            ServiceSegmentMapper serviceSegment = getMapper(ServiceSegmentMapper.class);
            serviceSegment.delete(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to delete service segment instances. {}", e);
            throw new ServiceException(ErrorCode.SVCMGR_OPER_MYSQL_DB_ERROR, "Fail to operate database!");
        }
    }

    /**
     * Query service segment instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return service segment instances.
     * @throws ServiceException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public List<ServiceSegmentModel> queryServiceSegments(String serviceId) throws ServiceException {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Data is wrong");
            }

            // 2. Query service segment instances.
            ServiceSegmentMapper serviceSegment = getMapper(ServiceSegmentMapper.class);
            return serviceSegment.queryServiceSegments(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query service segment instances. {}", e);
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
