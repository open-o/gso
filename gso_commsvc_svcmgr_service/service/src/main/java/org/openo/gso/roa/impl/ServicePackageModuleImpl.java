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

package org.openo.gso.roa.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpStatus;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.commsvc.common.exception.ErrorCode;
import org.openo.gso.commsvc.common.exception.HttpCode;
import org.openo.gso.commsvc.common.util.ValidateUtil;
import org.openo.gso.commsvc.common.util.JsonUtil;
import org.openo.gso.constant.Constant;
import org.openo.gso.roa.inf.IServicePackageModule;
import org.openo.gso.service.inf.IPackageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for interface of operating service package.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public class ServicePackageModuleImpl implements IServicePackageModule {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicePackageModuleImpl.class);

    /**
     * Package manager.
     */
    private IPackageManager packageMgr;

    /**
     * Set GSAR package on-boarding.<br/>
     * 
     * @param httpRequest http request
     * @return response
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public Response onBoardingPackage(HttpServletRequest httpRequest) {
        LOGGER.info("Start to upload package status.");

        // 1. Get request body
        String body = RestUtils.getRequestBody(httpRequest);
        ValidateUtil.assertStringNotNull(body, Constant.RESPONSE_CONTENT_MESSAGE);

        // 2. Parse json to get csarId
        Map<String, Object> bodyMap = JsonUtil.unMarshal(body, Map.class);
        Object serviceDefId = bodyMap.get(Constant.CSAR_ID);
        if(!(serviceDefId instanceof String)) {
            LOGGER.error("serviceDefId is not String: {}", String.valueOf(serviceDefId));
            throw new ApplicationException(HttpCode.BAD_REQUEST, ErrorCode.DATA_IS_WRONG);
        }
        ValidateUtil.assertStringNotNull((String)serviceDefId, Constant.SERVICE_DEF_ID);

        // 3. Update state
        packageMgr.updateOnBoardStatus((String)serviceDefId, httpRequest);

        return Response.status(HttpStatus.SC_OK).entity(Constant.RESPONSE_STATUS_SUCCESS).build();
    }

    /**
     * Delete GSAR package.<br/>
     * 
     * @param serviceDefId GSAR ID
     * @param httpRequest http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response deleteGsarPackage(String serviceDefId, HttpServletRequest httpRequest) {
        LOGGER.info("Start to delete package.");
        ValidateUtil.assertStringNotNull(serviceDefId, Constant.SERVICE_DEF_ID);
        packageMgr.deletePackage(serviceDefId, httpRequest);
        return Response.status(HttpStatus.SC_OK).entity(Constant.RESPONSE_STATUS_SUCCESS).build();
    }

    /**
     * @return Returns the packageMgr.
     */
    public IPackageManager getPackageMgr() {
        return packageMgr;
    }

    /**
     * @param packageMgr The packageMgr to set.
     */
    public void setPackageMgr(IPackageManager packageMgr) {
        this.packageMgr = packageMgr;
    }
}
