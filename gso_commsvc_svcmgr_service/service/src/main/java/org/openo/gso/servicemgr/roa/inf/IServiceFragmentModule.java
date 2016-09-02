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

package org.openo.gso.servicemgr.roa.inf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface to manage database data of service segment.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/2
 */
@Path("/lifecyclemgr/v1/services")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IServiceFragmentModule {

    /**
     * Create service segment.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ServiceException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/service-segment")
    Response createServiceSegment(@Context HttpServletRequest servletReq) throws ServiceException;

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
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{serviceId}/service-segment/{segmentId}")
    Response deleteServiceSegment(@PathParam("serviceId") String serviceId, @PathParam("segmentId") String segmentId,
            @Context HttpServletRequest servletReq) throws ServiceException;

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
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{serviceId}/service-segment/{segmentId}")
    Response updateServiceSegment(@PathParam("serviceId") String serviceId, @PathParam("segmentId") String segmentId,
            @Context HttpServletRequest servletReq) throws ServiceException;

}
