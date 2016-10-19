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
@Path("/gso/v1")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface IDrivermgrRoaModule {

    /**
     * Terminate the NFVO or SDNO instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when operate database or parameter is wrong.
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/drivers/terminate")
    Response terminateNetworkService(@Context HttpServletRequest servletReq) throws ApplicationException;

    /**
     * <br>
     * Instantiate the NFVO or SDNO instance
     * 
     * @param serviceNode node of sub-service
     * @param servletReq http request
     * @return Response
     * @throws ApplicationException when fail to instantiate network service
     * @since GSO 0.5
     */
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    @Path("/drivers/instantiate")
    Response instantiateNetworkService(@Context HttpServletRequest servletReq)
            throws ApplicationException;

}
