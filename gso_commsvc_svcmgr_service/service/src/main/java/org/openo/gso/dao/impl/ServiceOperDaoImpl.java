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

package org.openo.gso.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.dao.inf.IServiceOperDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.ServiceOperMapper;
import org.openo.gso.mapper.ServiceSegmentOperMapper;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Service operation implementation class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public class ServiceOperDaoImpl implements IServiceOperDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOperDaoImpl.class);

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
     * Insert service operation.<br/>
     * 
     * @param serviceOperation operation record
     * @since GSO 0.5
     */
    @Override
    public void insert(ServiceOperation serviceOperation) {
        try {
            ServiceOperMapper mapper = getMapper(ServiceOperMapper.class);
            mapper.insert(serviceOperation);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert service operation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Delete operations of service.<br/>
     * 
     * @param serviceId service instance ID
     * @since GSO 0.5
     */
    @Override
    public void delete(String serviceId) {
        try {
            getMapper(ServiceSegmentOperMapper.class).delete(serviceId);
            getMapper(ServiceOperMapper.class).delete(serviceId);

        } catch(Exception exception) {
            LOGGER.error("Fail to delete service operation by service instance ID. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query service operation progress by service ID and operation ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId operation ID
     * @return service operation Detail
     * @since GSO 0.5
     */
    @Override
    public ServiceOperation queryOperationById(String serviceId, String operationId) {
        try {
            ServiceOperMapper mapper = getMapper(ServiceOperMapper.class);
            return mapper.queryOperationById(serviceId, operationId);
        } catch(Exception exception) {
            LOGGER.error("Fail to query service operation by service instanceID and operationID. {}", exception);
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
     * Update service operation.<br/>
     * 
     * @param serviceOperation service operation
     * @since GSO 0.5
     */
    @Override
    public void update(ServiceOperation serviceOperation) {
        try {
            ValidateUtil.assertObjectNotNull(serviceOperation);
            getMapper(ServiceOperMapper.class).update(serviceOperation);
        } catch(Exception exception) {
            LOGGER.error("Fail to update service operation. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Delete old operation records which are generated for 15 days.<br/>
     * 
     * @param svcIds service instance ids
     * @since GSO 0.5
     */
    @Override
    public void deleteHistory(List<String> svcIds) {
        try {
            getMapper(ServiceOperMapper.class).deleteHistory(svcIds);
        } catch(Exception exception) {
            LOGGER.error("Fail to delete old operation records. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Get operations by service progress type.<br/>
     * 
     * @param progress service progress type,finished|processing|error
     * @return service operations
     * @since GSO 0.5
     */
    @Override
    public List<ServiceOperation> queryOperByIds(List<String> svcIds) {
        try {
            return getMapper(ServiceOperMapper.class).queryOperByIds(svcIds);
        } catch(Exception exception) {
            LOGGER.error("Fail to query operations by service instance ids. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Batch update service operations.<br/>
     * 
     * @param svcOperations service operations
     * @since GSO 0.5
     */
    @Override
    public void batchUpdate(List<ServiceOperation> svcOperations) {
        if(CollectionUtils.isEmpty(svcOperations)) {
            LOGGER.info("There is no service operation which need to update.");
            return;
        }

        try {
            LOGGER.info("Batch update service operations: {}", svcOperations);
            getMapper(ServiceOperMapper.class).batchUpdate(svcOperations);
        } catch(Exception exception) {
            LOGGER.error("Fail to batch update service operations. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Query old operations.<br/>
     * 
     * @return service operations
     * @since GSO 0.5
     */
    @Override
    public List<ServiceOperation> queryHistory() {
        try {
            return getMapper(ServiceOperMapper.class).queryHistory();

        } catch(Exception exception) {
            LOGGER.error("Fail to query old service operations. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
}
