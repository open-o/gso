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

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.dao.inf.IInventoryDao;
import org.openo.gso.dao.multi.DatabaseSessionHandler;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.InvServiceModelMapper;
import org.openo.gso.mapper.InventoryMapper;
import org.openo.gso.model.servicemo.InvServiceModel;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement class of operating inventory database table.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/19
 */
public class InventoryDaoImpl implements IInventoryDao {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModelDaoImpl.class);

    /**
     * Session handler.
     */
    private DatabaseSessionHandler invSessionHandler;

    /**
     * @return Returns the invSessionHandler.
     */
    public DatabaseSessionHandler getInvSessionHandler() {
        return invSessionHandler;
    }

    /**
     * @param invSessionHandler The invSessionHandler to set.
     */
    public void setInvSessionHandler(DatabaseSessionHandler invSessionHandler) {
        this.invSessionHandler = invSessionHandler;
    }

    /**
     * Insert data.<br/>
     * 
     * @param <T> data type
     * @param <M> mapper type
     * @param data which is insert into database table.
     * @param mapperType class type
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public <T, M> void insert(T data, Class<M> mapperType) throws ApplicationException {
        try {
            InventoryMapper mapper = (InventoryMapper)getMapper(mapperType);
            mapper.insert(data);
        } catch(Exception exception) {
            LOGGER.error("Fail to insert data. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Delete data by key.<br/>
     * 
     * @param <M> mapper type
     * @param key delete key
     * @param mapperType class type
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public <M> void delete(String key, Class<M> mapperType) throws ApplicationException {
        try {
            InventoryMapper mapper = (InventoryMapper)getMapper(mapperType);
            mapper.delete(key);
        } catch(Exception exception) {
            LOGGER.error("Fail to delete data. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Get mapper to operate database.<br/>
     * 
     * @param <T> mapper type
     * @param type class type of Mapper
     * @return Mapper object
     * @since GSO 0.5
     */
    private <T> T getMapper(Class<T> type) {
        return invSessionHandler.getSqlSession().getMapper(type);
    }

    /**
     * Update service instance status.<br/>
     * 
     * @param serviceId service instance ID.
     * @param status service instance status
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    @Override
    public void updateServiceStatus(String serviceId, String status) throws ApplicationException {
        try {
            InvServiceModelMapper mapper = getMapper(InvServiceModelMapper.class);
            mapper.updateServiceStatus(serviceId, status);
        } catch(Exception exception) {
            LOGGER.error("Fail to update service instance status. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }

    /**
     * Batch update service instances.<br/>
     * 
     * @param services service instances
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    @Override
    public void batchUpdate(List<InvServiceModel> services) throws ApplicationException {
        try {
            LOGGER.info("Batch  update inventory services: {}", services);
            ValidateUtil.assertObjectNotNull(services);
            getMapper(InvServiceModelMapper.class).batchUpdate(services);
        } catch(Exception exception) {
            LOGGER.error("Fail to batch update inventory service instance. {}", exception);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, ErrorCode.OPER_DB_FAIL);
        }
    }
}
