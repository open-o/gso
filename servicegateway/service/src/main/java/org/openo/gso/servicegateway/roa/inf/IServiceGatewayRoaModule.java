/*
 * Copyright (c) 2016-2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.roa.inf;

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
@Path("/servicegateway/v1")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IServiceGatewayRoaModule {

    /**
     * Create service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/services")
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
     * Query operation by operationId<br>
     * 
     * @param servletReq http request
     * @return the operation progress model
     * @throws ApplicationException when inner error
     * @since GSO 0.5
     */
    @GET
    @Path("/services/{serviceId}/operations/{operationId}")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response getOperation(@PathParam("serviceId") String serviceId, @PathParam("operationId") String operationId,
            @Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * Query create parameters by templateId<br>
     * 
     * @param servletReq http request
     * @return the parameters model for gui
     * @throws ApplicationException when inner error
     * @since GSO 0.5
     */
    @GET
    @Path("/createparameters/{tepmlateId}")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response generateCreateParameters(@PathParam("tepmlateId") String tepmlateId,
            @Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * query the services list
     * <br>
     * 
     * @param servletReq
     * @return
     * @throws ApplicationException
     * @since GSO Mercury Release
     */
    @GET
    @Path("/services")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response getServices(@Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * query the service
     * <br>
     * 
     * @param service Id the service id
     * @param servletReq the http request
     * @return
     * @throws ApplicationException
     * @since GSO Mercury Release
     */
    @GET
    @Path("/services/{serviceId}")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response getService(@PathParam("serviceId") String serviceId, @Context HttpServletRequest servletReq)
            throws ApplicationException;
    
    /**
     * query the domains
     * <br>
     * 
     * @param servletReq the http request
     * @return
     * @throws ApplicationException
     * @since GSO Mercury Release
     */
    @GET
    @Path("/domains")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response getDomains(@Context HttpServletRequest servletReq) throws ApplicationException;
}
