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

package org.openo.gso.servicemgr.roa.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.servicemgr.constant.Constant;
import org.openo.gso.servicemgr.exception.ErrorCode;
import org.openo.gso.servicemgr.exception.HttpCode;
import org.openo.gso.servicemgr.roa.inf.IServicePackageModule;
import org.openo.gso.servicemgr.service.inf.IPackageManager;
import org.openo.gso.servicemgr.util.http.ResponseUtils;
import org.openo.gso.servicemgr.util.json.JsonUtil;
import org.openo.gso.servicemgr.util.validate.ValidateUtil;
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
     * @throws ServiceException when fail to set
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public Response onBoardingPackage(HttpServletRequest httpRequest) {
        Map<String, Object> result = null;
        try {
            // 1. Get request body
            String body = RestUtils.getRequestBody(httpRequest);
            ValidateUtil.assertStringNotNull(body);
            // 2. Parse json to get csarId
            Map<String, Object> bodyMap = JsonUtil.unMarshal(body, Map.class);
            Object serviceDefId = bodyMap.get(Constant.CSAR_ID);
            if(!(serviceDefId instanceof String)) {
                throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, HttpCode.BAD_REQUEST);
            }
            ValidateUtil.assertStringNotNull((String)serviceDefId);
            // 3. Update state
            packageMgr.updateOnBoardStatus((String)serviceDefId, httpRequest);
        } catch(ServiceException exception) {
            LOGGER.error("Faile to on board package, {}", exception);
            result = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_FAIL, exception,
                    String.valueOf(exception.getHttpCode()));

            return Response.accepted(result).build();
        }

        result = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_SUCCESS, null,
                String.valueOf(HttpCode.RESPOND_OK));

        return Response.accepted(result).build();
    }

    /**
     * Delete GSAR package.<br/>
     * 
     * @param serviceDefId GSAR ID
     * @param httpRequest http request
     * @return response
     * @throws ServiceException when fail to delete GSAR package.
     * @since GSO 0.5
     */
    @Override
    public Response deleteGsarPackage(String serviceDefId, HttpServletRequest httpRequest) {
        Map<String, Object> result = null;
        try {
            packageMgr.deletePackage(serviceDefId, httpRequest);
        } catch(ServiceException exception) {
            LOGGER.error("Faile to delete csar package, {}", exception);
            result = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_FAIL, exception,
                    String.valueOf(exception.getHttpCode()));
            return Response.accepted(result).build();
        }

        result = ResponseUtils.setOperateStatus(Constant.RESPONSE_STATUS_SUCCESS, null,
                String.valueOf(HttpCode.RESPOND_OK));

        return Response.accepted(result).build();
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
