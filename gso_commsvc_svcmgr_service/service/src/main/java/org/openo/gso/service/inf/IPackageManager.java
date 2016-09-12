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

package org.openo.gso.service.inf;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface of managing service package.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/23
 */
public interface IPackageManager {

    /**
     * Set status of GSAR to be onBoard<br/>
     * 
     * @param serviceDefId ID of service package
     * @param request http request
     * @throws ServiceException when fail to set status
     * @since GSO 0.5
     */
    void updateOnBoardStatus(String serviceDefId, HttpServletRequest request) throws ServiceException;

    /**
     * Delete service package.<br/>
     * 
     * @param serviceDefId ID of service package
     * @param request http request
     * @throws ServiceException when fail to delete service package
     * @since GSO 0.5
     */
    void deletePackage(String serviceDefId, HttpServletRequest request) throws ServiceException;
}
