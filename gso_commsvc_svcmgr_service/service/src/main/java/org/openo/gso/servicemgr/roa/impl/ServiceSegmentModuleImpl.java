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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.servicemgr.roa.inf.IServiceFragmentModule;
import org.openo.gso.servicemgr.util.validate.ValidateUtil;

/**
 * see {@IServiceFragmentModule}<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/2
 */
public class ServiceSegmentModuleImpl implements IServiceFragmentModule {

    /**
     * Create service segment.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response createServiceSegment(HttpServletRequest servletReq) throws ServiceException {

        try {
            String requestBody = RestUtils.getRequestBody(servletReq);
            ValidateUtil.assertStringNotNull(requestBody);
        } catch(ServiceException exception) {

        }

        return null;
    }

    /**
     * Delete service segment.<br/>
     * 
     * @param serviceId service instance id
     * @param segmentId service segment id
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response deleteServiceSegment(String serviceId, String segmentId, HttpServletRequest servletReq)
            throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * update service segment.<br/>
     * 
     * @param serviceId service instance id
     * @param segmentId service segment id
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public Response updateServiceSegment(String serviceId, String segmentId, HttpServletRequest servletReq)
            throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
