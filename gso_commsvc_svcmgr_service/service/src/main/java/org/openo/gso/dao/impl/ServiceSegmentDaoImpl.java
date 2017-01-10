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
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.ServiceSegmentMapper;
import org.openo.gso.mapper.ServiceSegmentOperMapper;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
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
     * Insert service segment instances.<br/>
     * 
     * @param serviceSegments service segment instances
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void insertSegment(ServiceSegmentModel serviceSegment) throws ApplicationException {
        try {
            ValidateUtil.assertObjectNotNull(serviceSegment);
            ServiceSegmentMapper serviceSegmentMapper = getMapper(ServiceSegmentMapper.class);
            serviceSegmentMapper.insert(serviceSegment);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert service segment instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
    
    /**
     * Insert service segment operation<br>
     * 
     * @param svcSegmentOper service segment operation
     * @throws ApplicationException when database exception or parameter is wrong
     * @since   GSO 0.5
     */
    @Override
    public void insertSegmentOper(ServiceSegmentOperation svcSegmentOper) throws ApplicationException {
        try {
            ValidateUtil.assertObjectNotNull(svcSegmentOper);
            ServiceSegmentOperMapper svcSegmentOperMapper = getMapper(ServiceSegmentOperMapper.class);
            svcSegmentOperMapper.insert(svcSegmentOper);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert service segment operation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
        
    }

    /**
     * Delete service segment instance by service ID.<br/>
     * 
     * @param serviceSegment service segment information
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void delete(ServiceSegmentModel serviceSegment) throws ApplicationException {
        try {
            // Check data validation.
            if(StringUtils.isEmpty(serviceSegment.getServiceId())
                    || StringUtils.isEmpty(serviceSegment.getServiceSegmentId())) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
            }

            // Delete service segment instances.
            ServiceSegmentMapper mapper = getMapper(ServiceSegmentMapper.class);
            mapper.delete(serviceSegment);
        } catch(Exception e) {
            LOGGER.error("Fail to delete service segment instances. {}", e);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query service segment instances by service ID.<br/>
     * 
     * @param serviceId service instance ID
     * @return service segment instances.
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public List<ServiceSegmentModel> queryServiceSegments(String serviceId) throws ApplicationException {
        try {
            // 1. Check data validation.
            if(StringUtils.isEmpty(serviceId)) {
                throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
            }

            // 2. Query service segment instances.
            ServiceSegmentMapper serviceSegment = getMapper(ServiceSegmentMapper.class);
            return serviceSegment.queryServiceSegments(serviceId);
        } catch(Exception e) {
            LOGGER.error("Fail to query service segment instances. {}", e);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
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
        SqlSession session = dbSessionHandler.getSqlSession();
        return session.getMapper(type);
    }

    /**
     * Update jobId of the service segment<br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    @Override
    public void updateSegmentOperJobId(ServiceSegmentOperation segmentOper) {
        try {
            ValidateUtil.assertObjectNotNull(segmentOper);
            ServiceSegmentOperMapper svcSegmentOperMapper = getMapper(ServiceSegmentOperMapper.class);
            svcSegmentOperMapper.updateJobId(segmentOper);
        } catch(Exception exception) {
            LOGGER.error("Fail to update service segment operation job id. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
        
    }

    
    /**
     * Update status of the service segment<br><br>
     * 
     * @param segmentOper service segment operation
     * @since  GSO 0.5
     */
    @Override
    public void updateSegmentOperStatus(ServiceSegmentOperation segmentOper) {
        try {
            ValidateUtil.assertObjectNotNull(segmentOper);
            ServiceSegmentOperMapper svcSegmentOperMapper = getMapper(ServiceSegmentOperMapper.class);
            svcSegmentOperMapper.updateStatus(segmentOper);
        } catch(Exception exception) {
            LOGGER.error("Fail to update service segment operation status. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
        
    }

    
}
