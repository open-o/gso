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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * API for restful interface.<br/>
 * 
 * @version GSO 0.5 2016/8/30
 */
@Path("/gso/v1")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IDrivermgrRoaModule {

    /**
     * Create the NFVO instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nfvodrivers/ns")
    Response createNfvoNs(@Context HttpServletRequest servletReq);
    
    /**
     * Delete the NFVO instance<br>
     
     * @param servletReq http request
     * @return response
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nfvodrivers/ns/delete")
    Response deleteNfvoNs(@Context HttpServletRequest servletReq);

    /**
     * Query status of the NFVO instance<br>
     * 
     * @param jobId uuid of NFVO job
     * @return status of the NFVO instance
     * @since  GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nfvodrivers/jobs/{jobId}")
    Response queryNfvoJobStatus(@PathParam("jobId") String jobId);
    
    /**
     * <br>
     * Instantiate the NFVO instance
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return Response
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nfvodrivers/{nsInstanceId}/instantiate")
    Response instantiateNfvoNs(@PathParam("nsInstanceId") String nsInstanceId, @Context HttpServletRequest servletReq);

    /**
     * Terminate the NFVO instance<br>
     * 
     * @param servletReq http request
     * @return response 
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nfvodrivers/ns/terminate")
    Response terminateNfvoNs(@Context HttpServletRequest servletReq);
    
    /**
     * Create the SDNO instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/sdnodrivers/ns")
    Response createSdnoNs(@Context HttpServletRequest servletReq);
    
    /**
     * Delete the SDNO instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/sdnodrivers/ns/delete")
    Response deleteSdnoNs(@Context HttpServletRequest servletReq);

    /**
     * Query status of the SDNO instance<br>
     * 
     * @param jobId uuid for SDNO job
     * @return status of the SDNO instance
     * @since  GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/sdnodrivers/jobs/{jobId}")
    Response querySdnoJobStatus(@PathParam("jobId") String jobId);
    
    /**
     * <br>
     * Instantiate the SDNO instance
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return Response
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/sdnodrivers/{nsInstanceId}/instantiate")
    Response instantiateSdnoNs(@PathParam("nsInstanceId") String nsInstanceId, @Context HttpServletRequest servletReq);

    /**
     * Terminate the SDNO instance<br>
     * 
     * @param servletReq http request
     * @return response 
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/sdnodrivers/ns/terminate")
    Response terminateSdnoNs(@Context HttpServletRequest servletReq);
    
    /**
     * create gso service<br>
     * 
     * @param servletReq http request
     * @return response
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/gsodrivers/ns/create")
    Response createGsoNs(@Context HttpServletRequest servletReq);
    
    /**
     * delete gso service<br>
     * 
     * @param servletReq http request
     * @return response
     * @since  GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/gsodrivers/ns/delete")
    Response deleteGsoNs(@Context HttpServletRequest servletReq);
    
    /**
     * query gso service job status<br>
     * 
     * @param jobId job id
     * @return response
     * @since  GSO 0.5
     */
    @GET
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/gsodrivers/jobs/{jobId}")
    Response queryGsoJobStatus(@PathParam("jobId") String jobId);

}
