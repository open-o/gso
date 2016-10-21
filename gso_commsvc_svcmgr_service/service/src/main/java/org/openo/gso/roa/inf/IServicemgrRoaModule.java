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

package org.openo.gso.roa.inf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openo.gso.commsvc.common.Exception.ApplicationException;

/**
 * API for restful interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
@Path("/gso/v1")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IServicemgrRoaModule {

    /**
     * Create service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @POST
    @Path("/services")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response createService(@Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * Delete service instance.<br/>
     * 
     * @param serviceId service instance id
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/services/{serviceId}")
    Response deleteService(@PathParam("serviceId") String serviceId, @Context HttpServletRequest servletReq)
            throws ApplicationException;

    /**
     * Query all service instances.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database.
     * @since GSO 0.5
     */
    @GET
    @Path("/services")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response getAllInstances(@Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * Query represent sequence in topology.<br/>
     * 
     * @param serviceId service instance ID
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/services/toposequence/{serviceId}")
    Response getTopoSequence(@PathParam("serviceId") String serviceId, @Context HttpServletRequest servletReq)
            throws ApplicationException;

    /**
     * Create service segment instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to storage service segment instance.
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/services/service-segments")
    Response createServiceSegment(@Context HttpServletRequest servletReq) throws ApplicationException;
}
