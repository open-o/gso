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
import org.openo.gso.model.drivermo.NsCreateReq;
import org.openo.gso.model.drivermo.NsInstantiateReq;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

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
        NsCreateReq oRequest = new NsCreateReq();
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
    public RestfulResponse getNsProgress(Map<String, Object> inputMap) throws ApplicationException {

        // Get url based on node type
        String url = StringUtils.EMPTY;
        if(CommonConstant.SegmentType.GSO.equals(segmentType)) {
            url = String.format(CommonConstant.GSO_QUERY_URL, inputMap.get(CommonConstant.NS_INSTANCE_ID), inputMap.get(CommonConstant.JOB_ID));
        } else {
            url = getUrl(segmentType, (String) inputMap.get(CommonConstant.JOB_ID), CommonConstant.Step.QUERY);
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(CommonConstant.HttpContext.URL, url);
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
    
    /**
     * get service template by node type<br>
     * 
     * @param nodeType node type
     * @return service template
     * @throws ApplicationException when fail to get service template
     * @since  GSO 0.5
     */
    public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost) throws ApplicationException {

        // Step 1: Prepare url and method type
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, CommonConstant.CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);
        parseDomainHost(domainHost, paramsMap);

        // Step 2: Prepare the query param
        LOGGER.info("node Type is {}", nodeType);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("nodeTypeIds", nodeType);
        

        // Step 3:Send the request and get response
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, null, queryParams);
        LOGGER.info("response status is {}", rsp.getStatus());
        LOGGER.info("response content is {}", rsp.getResponseContent());
        JSONArray array = JSONArray.fromObject(rsp.getResponseContent());
        return JsonUtil.unMarshal(array.getString(0), ServiceTemplate.class);
    }
    
    /**
     * private method3: parse domain host<br>
     * 
     * @param domainHost host & port which httprequest should send to
     * @param paramsMap params map
     * @since  GSO 0.5
     */
    private void parseDomainHost(String domainHost, Map<String, Object> paramsMap) {
        if(StringUtils.isEmpty(domainHost) || CommonConstant.LOCAL_HOST.equals(domainHost)){
            LOGGER.info("domainHost is {}", domainHost);
            return;
        }
        LOGGER.info("domainHost is {}", domainHost);
        String ip = domainHost.substring(0, domainHost.indexOf(":"));
        String port = domainHost.substring(domainHost.indexOf(":") + 1);
        paramsMap.put(CommonConstant.HttpContext.IP, ip);
        paramsMap.put(CommonConstant.HttpContext.PORT, port);
    }

    /**
     * create gso service<br>
     * 
     * @param inputMap input parameters map
     * @return restful response
     * @throws ApplicationException when fail to delete gso serivce
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createGsoNs(Map<String, Object> inputMap) throws ApplicationException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, CommonConstant.GSO_CREATE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        
        String req = (String) inputMap.get(CommonConstant.HttpContext.RAW_DATA);
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, req, null);
        LOGGER.info("create gso ns response status is : {}", rsp.getStatus());
        LOGGER.info("create gso ns response content is : {}", rsp.getResponseContent());

        return rsp;
    }

    /**
     * delete gso service<br>
     * 
     * @param inputMap input parameters map
     * @return restful response
     * @throws ApplicationException when fail to delete gso serivce
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteGsoNs(Map<String, Object> inputMap) throws ApplicationException {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(CommonConstant.HttpContext.URL, String.format(CommonConstant.GSO_DELETE_URL, inputMap.get(CommonConstant.NS_INSTANCE_ID)));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.DELETE);
        paramsMap.put(CommonConstant.HttpContext.IP, inputMap.get(CommonConstant.HttpContext.IP));
        paramsMap.put(CommonConstant.HttpContext.PORT, inputMap.get(CommonConstant.HttpContext.PORT));
        
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, null, null);
        LOGGER.info("delete gso ns response status is : {}", rsp.getStatus());
        LOGGER.info("delete gso ns response content is : {}", rsp.getResponseContent());

        return rsp;
    }

}
