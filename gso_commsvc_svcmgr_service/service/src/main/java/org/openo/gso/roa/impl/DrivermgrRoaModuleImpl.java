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

package org.openo.gso.roa.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.roa.inf.INFVODrivermgrRoaModule;
import org.openo.gso.roa.inf.ISDNODrivermgrRoaModule;
import org.openo.gso.service.inf.IDriverManager;

/**
 * Implement class for restful interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class DrivermgrRoaModuleImpl implements INFVODrivermgrRoaModule,ISDNODrivermgrRoaModule {

    /**
     * DriverManager.
     */
    private IDriverManager driverMgr;

    /**
     * @return Returns the driverMgr.
     */
    public IDriverManager getDriverMgr() {
        return driverMgr;
    }

    /**
     * @param driverMgr The driverMgr to set.
     */
    public void setDriverMgr(IDriverManager driverMgr) {
        this.driverMgr = driverMgr;
    }


    /**
     * Create NFVO service instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to create service instance
     * @since   GSO 0.5
     */
    @Override
    public Response createNfvoNs(HttpServletRequest servletReq) throws ApplicationException {
        RestfulResponse rsp = driverMgr.createNs(servletReq, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }



    /**
     * Delete NFVO service instance<br>
     *
     * @param nsInstanceId uuid of service instance
     * @return response
     * @throws ApplicationException when fail to delete service instance
     * @since   GSO 0.5
     */
    @Override
    public Response deleteNfvoNs(String nsInstanceId) throws ApplicationException {
        RestfulResponse rsp = driverMgr.deleteNs(nsInstanceId, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }

    /**
     * Query NFVO job status<br>
     * 
     * @param jobId uuid of NFVO job
     * @return response 
     * @throws ApplicationException fail to query job status
     * @since   GSO 0.5
     */
    @Override
    public Response queryNfvoJobStatus(String jobId) throws ApplicationException {
        RestfulResponse rsp = driverMgr.getNsProgress(jobId, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }

    /**
     * Instantiate NFVO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to instantiate the service instance
     * @since   GSO 0.5
     */
    @Override
    public Response instantiateNfvoNs(String nsInstanceId, HttpServletRequest servletReq) throws ApplicationException {

        RestfulResponse rsp = driverMgr.instantiateNs(nsInstanceId, servletReq, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }
    

    /**
     * Terminate NFVO service instance.<br/>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to terminate the service instance
     * @since GSO 0.5
     */
    @Override
    public Response terminateNfvoNs(String nsInstanceId, HttpServletRequest servletReq) throws ApplicationException {
        RestfulResponse rsp = driverMgr.terminateNs(nsInstanceId, servletReq, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }
    
    /**
     * Create SDNO service instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to create service instance
     * @since   GSO 0.5
     */
    @Override
    public Response createSdnoNs(HttpServletRequest servletReq) throws ApplicationException {
        RestfulResponse rsp = driverMgr.createNs(servletReq, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Delete SDNO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @return response
     * @throws ApplicationException when fail to delete service instance
     * @since   GSO 0.5
     */
    @Override
    public Response deleteSdnoNs(String nsInstanceId) throws ApplicationException {
        RestfulResponse rsp = driverMgr.deleteNs(nsInstanceId, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Query SDNO job status<br>
     * 
     * @param jobId uuid of SDNO service job
     * @return response
     * @throws ApplicationException when fail to query job status
     * @since   GSO 0.5
     */
    @Override
    public Response querySdnoJobStatus(String jobId) throws ApplicationException {
        RestfulResponse rsp = driverMgr.getNsProgress(jobId, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Instantiate SDNO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to instantiate the service instance
     * @since   GSO 0.5
     */
    @Override
    public Response instantiateSdnoNs(String nsInstanceId, HttpServletRequest servletReq) throws ApplicationException {
        RestfulResponse rsp = driverMgr.instantiateNs(nsInstanceId, servletReq, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Terminate NFVO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @throws ApplicationException when fail to terminate the service instance
     * @since   GSO 0.5
     */
    @Override
    public Response terminateSdnoNs(String nsInstanceId, HttpServletRequest servletReq) throws ApplicationException {
        RestfulResponse rsp = driverMgr.terminateNs(nsInstanceId, servletReq, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * build response from restful reponse<br>
     * 
     * @param rsp restful response
     * @return response instance
     * @since  GSO 0.5
     */
    private Response buildResponse(RestfulResponse rsp) {
        ResponseBuilder rspBuilder = Response.status(rsp.getStatus());
        rspBuilder.entity(rsp.getResponseContent());
        return rspBuilder.build();
    }
}
