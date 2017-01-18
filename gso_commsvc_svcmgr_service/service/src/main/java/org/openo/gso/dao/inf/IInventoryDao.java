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

package org.openo.gso.dao.inf;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.servicemo.InvServiceModel;

/**
 * Interface to operate inventory DB.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/19
 */
public interface IInventoryDao {

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
    <T, M> void insert(T data, Class<M> mapperType) throws ApplicationException;

    /**
     * Delete data by key.<br/>
     * 
     * @param <M> mapper type
     * @param key delete key
     * @param mapperType class type
     * @throws ApplicationException when database exception or parameter is wrong
     * @since GSO 0.5
     */
    <M> void delete(String key, Class<M> mapperType) throws ApplicationException;

    /**
     * Update service instance status.<br/>
     * 
     * @param serviceId service instance ID
     * @param status service execution status. finished
     * @throws ApplicationException
     * @since GSO 0.5
     */
    void updateServiceStatus(String serviceId, String status) throws ApplicationException;

    /**
     * Batch update service instances.<br/>
     * 
     * @param services service instances
     * @throws ApplicationException when database exception
     * @since GSO 0.5
     */
    void batchUpdate(@Param("services") List<InvServiceModel> services) throws ApplicationException;
}
