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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.DriverExceptionID;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.drivermo.SegmentInputParameter;
import org.openo.gso.model.drivermo.ServiceNode;
import org.openo.gso.roa.inf.IGSODrivermgrRoaModule;
import org.openo.gso.roa.inf.INFVODrivermgrRoaModule;
import org.openo.gso.roa.inf.ISDNODrivermgrRoaModule;
import org.openo.gso.service.inf.IDriverManager;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement class for restful interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class DrivermgrRoaModuleImpl implements INFVODrivermgrRoaModule,ISDNODrivermgrRoaModule,IGSODrivermgrRoaModule {
    
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DrivermgrRoaModuleImpl.class);

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
     * @since   GSO 0.5
     */
    @Override
    public Response createNfvoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter nfvSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.createNs(nfvSegInput, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }



    /**
     * Delete NFVO service instance<br>
     *
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response deleteNfvoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter nfvSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.deleteNs(nfvSegInput, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }

    /**
     * Query NFVO job status<br>
     * 
     * @param jobId uuid of NFVO job
     * @return response 
     * @since   GSO 0.5
     */
    @Override
    public Response queryNfvoJobStatus(String jobId) {
        RestfulResponse rsp = driverMgr.getNsProgress(jobId, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }

    /**
     * Instantiate NFVO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response instantiateNfvoNs(String nsInstanceId, HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter nfvSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.instantiateNs(nsInstanceId, nfvSegInput, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }
    

    /**
     * Terminate NFVO service instance.<br/>
     * 
     * @param servletReq http request
     * @return response
     * @since GSO 0.5
     */
    @Override
    public Response terminateNfvoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter nfvSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.terminateNs(nfvSegInput, CommonConstant.SegmentType.NFVO);
        return buildResponse(rsp);
    }
    
    /**
     * Create SDNO service instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response createSdnoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter sdnSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.createNs(sdnSegInput, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Delete SDNO service instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response deleteSdnoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter sdnSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.deleteNs(sdnSegInput, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Query SDNO job status<br>
     * 
     * @param jobId uuid of SDNO service job
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response querySdnoJobStatus(String jobId) {
        RestfulResponse rsp = driverMgr.getNsProgress(jobId, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Instantiate SDNO service instance<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response instantiateSdnoNs(String nsInstanceId, HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter sdnSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.instantiateNs(nsInstanceId, sdnSegInput, CommonConstant.SegmentType.SDNO);
        return buildResponse(rsp);
    }

    /**
     * Terminate NFVO service instance<br>
     * 
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response terminateSdnoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter sdnSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.terminateNs(sdnSegInput, CommonConstant.SegmentType.SDNO);
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

    /**
     * Create GSO Service<br>
     * 
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response createGsoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter gsoSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.createGsoNs(gsoSegInput, CommonConstant.SegmentType.GSO);
        return buildResponse(rsp);
    }

    /**
     * Delete GSO Service<br>
     * 
     * @param servletReq http request
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response deleteGsoNs(HttpServletRequest servletReq) {
        // Step 1: get parameters from request for current node
        SegmentInputParameter gsoSegInput = getParamsForCurrentNode(servletReq);
        RestfulResponse rsp = driverMgr.deleteGsoNs(gsoSegInput, CommonConstant.SegmentType.GSO);
        return buildResponse(rsp);
    }

    /**
     * Query GSO Service Job Status<br>
     * 
     * @param jobId job id
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public Response queryGsoJobStatus(String jobId) {
        RestfulResponse rsp = driverMgr.getGsoNsProgress(jobId, CommonConstant.SegmentType.GSO);
        return buildResponse(rsp);
    }
    
    /**
     * private method 1:get input parameters for current node<br>
     * 
     * @param servletReq http request
     * @return input parameters for current node
     * @since  GSO 0.5
     */
    private SegmentInputParameter getParamsForCurrentNode(HttpServletRequest servletReq) {
        // Step 0: get request model
        String body = RestUtils.getRequestBody(servletReq);
        LOGGER.info("body from request is {}", body);
        String jsonBody = body.replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}").replaceAll("\"\\[", "\\[").replaceAll("\\]\"", "\\]");
        LOGGER.info("json body from request is {}", jsonBody);
        ServiceNode serviceNode = JsonUtil.unMarshal(jsonBody, ServiceNode.class);

        // Step 1:Validate input parameters
        if((null == serviceNode.getNodeTemplateName()) || (null == serviceNode.getSegments())) {
            LOGGER.error("Input parameters from lcm/workflow are empty");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        
        // Step 2:Get input parameters for current node
        List<SegmentInputParameter> inputList = serviceNode.getSegments();
        Map<String, SegmentInputParameter> map = new HashMap<String, SegmentInputParameter>();
        for(SegmentInputParameter input : inputList) {
            map.put(input.getNodeTemplateName(), input);
        }
        
        return map.get(serviceNode.getNodeTemplateName());
    }
}
