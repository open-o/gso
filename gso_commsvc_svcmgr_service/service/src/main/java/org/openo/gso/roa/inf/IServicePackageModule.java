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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Interface for operating service package.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
@Path("/gso/v1")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IServicePackageModule {

    /**
     * Set GSAR package on-boarding.<br/>
     * 
     * @param httpRequest http request
     * @return response
     * @since GSO 0.5
     */
    @POST
    @Path("/nspackages")
    @Produces({"application/json"})
    @Consumes({"application/json"})
    Response onBoardingPackage(@Context HttpServletRequest httpRequest);

    /**
     * Delete GSAR package.<br/>
     * 
     * @param serviceDefId GSAR ID
     * @param httpRequest http request
     * @return response
     * @since GSO 0.5
     */
    @DELETE
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/nspackages/{csarId}")
    Response deleteGsarPackage(@PathParam("csarId") String serviceDefId, @Context HttpServletRequest httpRequest);

}
