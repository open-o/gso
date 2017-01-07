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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.openo.gso.commsvc.common.Exception.ApplicationException;

/**
 * API for restful interface.<br/>
 * 
 * @version GSO 0.5 2016/8/30
 */
@Path("/gso/v1/nfvodrivers")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface INFVODrivermgrRoaModule {

    /**
     * Create the NFVO instance.<br/>
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
    Response createNfvoNs(@Context HttpServletRequest servletReq) throws ApplicationException;
    
    /**
     * Delete the NFVO instance<br>
     
     * @param nsInstanceId uuid of service instance
     * @return response
     * @throws ApplicationException when fail to delete the network service
     * @since  GSO 0.5
     */
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/ns/{nsInstanceId}")
    Response deleteNfvoNs(@PathParam("nsInstanceId") String nsInstanceId) throws ApplicationException;

    /**
     * Query status of the NFVO instance<br>
     * 
     * @param jobId uuid of NFVO job
     * @return status of the NFVO instance
     * @throws ApplicationException when fail to query status of the NFVO instance
     * @since  GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/jobs/{jobId}")
    Response queryNfvoJobStatus(@PathParam("jobId") String jobId) throws ApplicationException;
    
    /**
     * <br>
     * Instantiate the NFVO instance
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return Response
     * @throws ApplicationException when fail to instantiate network service
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{nsInstanceId}/instantiate")
    Response instantiateNfvoNs(@PathParam("nsInstanceId") String nsInstanceId, @Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * Terminate the NFVO instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response 
     * @throws ApplicationException when fail to terminate the NFVO instance
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/{nsInstanceId}/terminate")
    Response terminateNfvoNs(@PathParam("nsInstanceId") String  nsInstanceId, @Context HttpServletRequest servletReq) throws ApplicationException;

}
