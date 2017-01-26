/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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
 * @version GSO 0.5 2017/1/19
 */
@Path("/gso/v1/gsodrivers")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IGSODrivermgrRoaModule {
    
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
    @Path("/ns/create")
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
    @Path("/ns/delete")
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
    @Path("/jobs/{jobId}")
    Response queryGsoJobStatus(@PathParam("jobId") String jobId);

}
