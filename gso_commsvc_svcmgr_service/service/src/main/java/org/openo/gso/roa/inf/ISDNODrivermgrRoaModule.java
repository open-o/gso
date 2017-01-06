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

package org.openo.gso.roa.inf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openo.gso.commsvc.common.Exception.ApplicationException;

/**
 * API for restful interface.<br/>
 * 
 * @version GSO 0.5 2016/8/30
 */
@Path("/gso/v1/sdnodrivers")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface ISDNODrivermgrRoaModule {

    /**
     * Terminate the SDNO instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to create network service.
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/ns")
    Response createSdnoNs(@Context HttpServletRequest servletReq) throws ApplicationException;
    
    /**
     * Delete the SDNO instance<br>
     * 
     * @return response
     * @throws ApplicationException when fail to delete the network service
     * @since  GSO 0.5
     */
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/ns/{nsInstanceId}")
    Response deleteSdnoNs() throws ApplicationException;

    /**
     * Query status of the SDNO instance<br>
     * 
     * @return status of the SDNO instance
     * @throws ApplicationException when fail to query status of the SDNO instance
     * @since  GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/jobs/{jobId}")
    Response querySdnoJobStatus() throws ApplicationException;
    
    /**
     * <br>
     * Instantiate the SDNO instance
     * 
     * @param servletReq http request
     * @return Response
     * @throws ApplicationException when fail to instantiate network service
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{nsInstanceId}/instantiate")
    Response instantiateSdnoNs(@Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * Terminate the SDNO instance<br>
     * 
     * @param servletReq http request
     * @return response 
     * @throws ApplicationException when fail to terminate the SDNO instance
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{nsInstanceId}/terminate")
    Response terminateSdnoNs(@Context HttpServletRequest servletReq) throws ApplicationException;

}
