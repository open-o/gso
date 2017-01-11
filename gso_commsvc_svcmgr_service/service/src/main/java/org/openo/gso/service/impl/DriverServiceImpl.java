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
import org.openo.gso.constant.CommonConstant.Step;
import org.openo.gso.model.drivermo.NSRequest;
import org.openo.gso.model.drivermo.NsInstantiateReq;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String segmentType;
    
    private static Map nfvoUrlMap;
    
    static {
        nfvoUrlMap = new HashMap<String, String>();
        nfvoUrlMap.put(Step.CREATE, CommonConstant.NFVO_CREATE_URL);
        nfvoUrlMap.put(Step.INSTANTIATE, CommonConstant.NFVO_INSTANTIATE_URL);
        nfvoUrlMap.put(Step.TERMINATE, CommonConstant.NFVO_TERMINATE_URL);
        nfvoUrlMap.put(Step.DELETE, CommonConstant.NFVO_DELETE_URL);
        nfvoUrlMap.put(Step.QUERY, CommonConstant.NFVO_QUERY_URL);
    }
    
    private static Map sdnoUrlMap;

    static {
        sdnoUrlMap = new HashMap<String, String>();
        sdnoUrlMap.put(Step.CREATE, CommonConstant.SDNO_CREATE_URL);
        sdnoUrlMap.put(Step.INSTANTIATE, CommonConstant.SDNO_INSTANTIATE_URL);
        sdnoUrlMap.put(Step.TERMINATE, CommonConstant.SDNO_TERMINATE_URL);
        sdnoUrlMap.put(Step.DELETE, CommonConstant.SDNO_DELETE_URL);
        sdnoUrlMap.put(Step.QUERY, CommonConstant.SDNO_QUERY_URL);
    }

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
    public RestfulResponse terminateNs(Map<String, Object> inputMap) throws ApplicationException {
        // terminate action
        String nsInstanceId = (String) inputMap.get(CommonConstant.NS_INSTANCE_ID);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(segmentType, nsInstanceId, CommonConstant.Step.TERMINATE));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        
        RestfulResponse terminateRsp = RestfulUtil.getRemoteResponse(paramsMap, null, null);
        LOGGER.info("terminate ns response status is : {}", terminateRsp.getStatus());
        LOGGER.info("terminate ns response content is : {}", terminateRsp.getResponseContent());

        return terminateRsp;
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
    public RestfulResponse deleteNs(Map<String, Object> inputMap) throws ApplicationException {
        String nsInstanceId = (String) inputMap.get(CommonConstant.NS_INSTANCE_ID);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(segmentType, nsInstanceId, CommonConstant.Step.DELETE));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.DELETE);
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        
        RestfulResponse deleteRsp = RestfulUtil.getRemoteResponse(paramsMap, null, null);
        LOGGER.info("delete ns response status is : {}", deleteRsp.getStatus());
        LOGGER.info("delete ns response content is : {}", deleteRsp.getResponseContent());

        return deleteRsp;
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

        if(CommonConstant.SegmentType.NFVO.equals(domain)) {
            originalUrl = (String) nfvoUrlMap.get(step);
            url = String.format(originalUrl, variable);
        } else if(CommonConstant.SegmentType.SDNO.equals(domain)) {
            originalUrl = (String) sdnoUrlMap.get(step);
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
    public RestfulResponse createNs(Map<String, Object> inputMap) throws ApplicationException {

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(segmentType, null, CommonConstant.Step.CREATE));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        
        // Step 1: Prepare Network Service Request
        NSRequest oRequest = new NSRequest();
        oRequest.setNsdId((String) inputMap.get(CommonConstant.NSD_ID));
        oRequest.setNsName((String) inputMap.get(CommonConstant.NS_NAME));
        oRequest.setDescription((String) inputMap.get(CommonConstant.DESC));

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
     * @return jobId
     * @throws ApplicationException - when workflow engine returns invalid value
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public RestfulResponse instantiateNs(Map<String, Object> inputMap) throws ApplicationException {

        // Step 1: Prepare Network Service Instantiate Request
        NsInstantiateReq oRequest = new NsInstantiateReq();

        String nsInstanceId = (String) inputMap.get(CommonConstant.NS_INSTANCE_ID);
        oRequest.setNsInstanceId(nsInstanceId);
        oRequest.setAdditionalParamForNs((Map<String, String>)inputMap.get(CommonConstant.ADDITIONAL_PARAM_FOR_NS));

        // Get url based on node type

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(segmentType, nsInstanceId, CommonConstant.Step.INSTANTIATE));
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));

        // Step 2: Send Network Service Instantiate Request
        String networkSvcReq = "";
        networkSvcReq = JsonUtil.marshal(oRequest);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, networkSvcReq, null);
        LOGGER.info("instantiate ns response status is : {}", rsp.getStatus());
        LOGGER.info("instantiate ns response content is : {}", rsp.getResponseContent());

        return rsp;

    }

    /**
     * Get Network service instantiation progress<br/>
     * 
     * @param jobId - jobId of instantiation
     * @param inputMap parameters map
     * @return - Progress information
     * @throws ApplicationException - when the workflow returns invalid information
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse getNsProgress(String jobId, Map<String, Object> inputMap) throws ApplicationException {

        // Get url based on node type
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(CommonConstant.HttpContext.URL, getUrl(segmentType, jobId, CommonConstant.Step.QUERY));
        paramMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);
        paramMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramMap, null, null);
        LOGGER.info("query ns progress response status is : {}", rsp.getStatus());
        LOGGER.info("query ns progress response content is : {}", rsp.getResponseContent());
        return rsp;
    }

    @Override
    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

}
