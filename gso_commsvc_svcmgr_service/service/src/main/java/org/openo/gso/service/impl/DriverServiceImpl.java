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

package org.openo.gso.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.DriverExceptionID;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.drivermo.NSRequest;
import org.openo.gso.model.drivermo.NsInstantiateReq;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * Service implementation
 * 
 * @author
 * @version GSO 0.5 2016/9/3
 */
public class DriverServiceImpl implements IDriverService {

    /**
     * record log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);

    private String domain;

    /**
     * <br>
     * terminate the service instance
     * 
     * @param nodeType type of the node instance
     * @param instanceId id of the sub-service instance
     * @return restful response for the action
     * @throws ApplicationException when fail to complete the action
     * @since GSO 0.5
     */
    @Override
    public String terminateNs(String instanceId) throws ApplicationException {
        // terminate action
        String terminateUrl = getUrl(domain, instanceId, CommonConstant.Step.TERMINATE);
        RestfulResponse terminateRsp = getOperationResponse(terminateUrl, CommonConstant.MethodType.POST);
        LOGGER.info("create ns response content is : {}", terminateRsp.getResponseContent());
        // Process Network Service Response
        JSONObject obj = JSONObject.fromObject(terminateRsp.getResponseContent());
        return obj.getString("jobId");
    }   
        
    /**
     * <br>
     * delete the service instance
     * @param instanceId id of the sub service instance
     * @return result response for the action
     * @throws ApplicationException when fail to complete the action
     * @since   GSO 0.5
     */
    @Override
    public String deleteNs(String instanceId) throws ApplicationException {
        // delete action
        String deleteUrl = getUrl(domain, instanceId, CommonConstant.Step.DELETE);
        RestfulResponse deleteRsp = getOperationResponse(deleteUrl, CommonConstant.MethodType.DELETE);
        String result = "fail";
        if(HttpCode.isSucess(deleteRsp.getStatus())) {
            LOGGER.info("succeed to delete the segment");
            result = "success";
        } else {
            LOGGER.error("fail to delete the segment");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }
        return result;
    }    
            

    /**
     * <br>
     * operation of different methodType
     * 
     * @param url url of the action
     * @param methodType method type for terminate action
     * @return result of the terminate operation(jobId for query)
     * @since GSO 0.5
     */
    private RestfulResponse getOperationResponse(String url, String methodType) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, url);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, methodType);
        return RestfulUtil.getRemoteResponse(paramsMap, null, null);

    }

    /**
     * <br>
     * get url for the operation
     * 
     * @param domain of the node instance
     * @param variable variable should be put in the url
     * @param step step of the operation (terminate,query,delete)
     * @return url can be used to invoke corresponding service
     * @since GSO 0.5
     */
    private String getUrl(String domain, String variable, String step) {

        String url = StringUtils.EMPTY;
        String originalUrl = StringUtils.EMPTY;

        if(CommonConstant.Domain.NFVO.equals(domain)) {
            originalUrl = (String) CommonConstant.nfvoUrlMap.get(step);
            url = String.format(originalUrl, variable);
        } else if(CommonConstant.Domain.SDNO.equals(domain)) {
            originalUrl = (String) CommonConstant.sdnoUrlMap.get(step);
            url = String.format(originalUrl, variable);
        } else {
            // do nothing
        }

        return url;

    }

    /**
     * Create Network service<br/>
     * 
     * @param svcTmpl - Service template
     * @return InstanceId
     * @throws ServiceException -when workflow engine do not return instanceId
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse createNS(Map<String, String> inputMap) throws ApplicationException {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(domain, null, CommonConstant.Step.CREATE));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        
        // Step 1: Prepare Network Service Request
        NSRequest oRequest = new NSRequest();
        oRequest.setNsdId(inputMap.get(CommonConstant.NSD_ID));
        oRequest.setNsName(inputMap.get(CommonConstant.NS_NAME));
        oRequest.setDescription(inputMap.get(CommonConstant.DESC));

        // Step 2: Send Network Service Request
        String req = "";
        req = JsonUtil.marshal(oRequest);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, req, null);
        LOGGER.info("create ns response status is : {}", rsp.getStatus());
        LOGGER.info("create ns response content is : {}", rsp.getResponseContent());

        return rsp;
    }

    /**
     * Instantiate Network service and get the jobId<br/>
     * 
     * @param instanceId - Network service to be instantiated
     * @param lstParams - List of user input parameters
     * @return jobID
     * @throws ApplicationException - when workflow engine returns invalid value
     * @since GSO 0.5
     */
    @Override
    public String instantiateNS(String instanceId, Map<String, String> mapParams) throws ApplicationException {

        // Step 1: Prepare Network Service Instantiate Request
        NsInstantiateReq oRequest = new NsInstantiateReq();

        oRequest.setNsInstanceId(instanceId);
        oRequest.setAdditionalParamForNs(mapParams);

        // Get url based on node type

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(domain, instanceId, CommonConstant.Step.INSTANTIATE));

        // Step 2: Send Network Service Instantiate Request
        String networkSvcReq = "";
        networkSvcReq = JsonUtil.marshal(oRequest);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, networkSvcReq, null);
        LOGGER.info("instantiate ns response content is : {}", rsp.getResponseContent());

        // Step 3: Process Network Service Instantiate Response
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        return obj.getString("jobId");

    }

    /**
     * Get Network service instantiation progress<br/>
     * 
     * @param jobId - jobId of instantiation
     * @return - Progress information
     * @throws ApplicationException - when the workflow returns invalid information
     * @since GSO 0.5
     */
    @Override
    public NsProgressStatus getNsProgress(String jobId) throws ApplicationException {

        // Get url based on node type
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(domain, jobId, CommonConstant.Step.QUERY));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, null, null);

        // Process Network Service Instantiate Response
        NsProgressStatus nsProgress = null;
        nsProgress = JsonUtil.unMarshal(rsp.getResponseContent(), NsProgressStatus.class);

        return nsProgress;
    }

    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

}
