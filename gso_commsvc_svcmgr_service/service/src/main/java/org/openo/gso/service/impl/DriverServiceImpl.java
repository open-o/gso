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

    private static final String SDNO_TERMINATE_URL = "/openoapi/sdnonslcm/v1/sss/%s/terminate";

    private static final String NFVO_TERMINATE_URL = "/openoapi/nslcm/v1/ns/%s/terminate";

    private static final String SDNO_QUERY_URL = "/openoapi/sdnonslcm/v1/jobs/%s";

    private static final String NFVO_QUERY_RUL = "/openoapi/nslcm/v1/jobs/%s";

    private static final String SDNO_DELETE_URL = "/openoapi/sdnonslcm/v1/sss/%s";

    private static final String NFVO_DELETE_URL = "/openoapi/nslcm/v1/ns/%s";

    private static final String SDNO_CREATE_URL = "/openoapi/sdnonslcm/v1/ns";

    private static final String NFVO_CREATE_URL = "/openoapi/nslcm/v1/ns";

    private static final String SDNO_INSTANTIATE_URL = "/openoapi/sdnonslcm/v1/ns/%s/instantiate";

    private static final String NFVO_INSTANTIATE_URL = "/openoapi/nslcm/v1/ns/%s/instantiate";

    private String nodeType;

    /**
     * <br>
     * delete the service instance
     * 
     * @param nodeType type of the node instance
     * @param instanceId id of the sub-service instance
     * @return restful response for the action
     * @throws ApplicationException when fail to complete the action
     * @since GSO 0.5
     */
    @Override
    public String delete(String nodeType, String instanceId) throws ApplicationException {

        if(StringUtils.isEmpty(nodeType)) {
            LOGGER.error("invalid pramerter: nodeType");
            throw new ApplicationException(HttpCode.BAD_REQUEST, DriverExceptionID.INVALID_PARAM);
        }

        if(StringUtils.isEmpty(instanceId)) {
            LOGGER.error("invalid parameter:instanceId");
            throw new ApplicationException(HttpCode.BAD_REQUEST, DriverExceptionID.INVALID_PARAM);
        }

        // 1. terminate action
        String terminateUrl = getUrl(nodeType, instanceId, CommonConstant.Step.TERMINATE);
        RestfulResponse terminateRsp = getOperationResponse(terminateUrl, CommonConstant.MethodType.POST);
        String terminateContent = null;
        if(HttpCode.isSucess(terminateRsp.getStatus())) {
            terminateContent = terminateRsp.getResponseContent();
        } else {
            LOGGER.error("fail to terminate the sub-service:{}", nodeType);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }

        // 2. query operation (get operation status)
        JSONObject terminateObj = JSONObject.fromObject(terminateContent);
        String jobId = terminateObj.getString("jobId");
        String queryUrl = getUrl(nodeType, jobId, CommonConstant.Step.QUERY);

        // when the progress is 100% & status is finished, exit the loop query

        while(true) {
            boolean finished = finishedQuerying(nodeType, queryUrl);
            if(finished) {
                break;
            }
        }

        // 3. delete operation
        String deleteUrl = getUrl(nodeType, instanceId, CommonConstant.Step.DELETE);
        RestfulResponse deleteRsp = getOperationResponse(deleteUrl, CommonConstant.MethodType.DELETE);
        String result = "fail";
        if(HttpCode.isSucess(deleteRsp.getStatus())) {
            LOGGER.info("succeed to delete the sub-service:{}", nodeType);
            result = "success";
        } else {
            LOGGER.error("fail to delete the sub-service:{}", nodeType);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }
        return result;
    }

    /**
     * <br>
     * query operation
     * 
     * @param nodeType type of the node instance
     * @param queryUrl url for query operation
     * @param queryFlag flag to do the query if true
     * @return whether the query operation is ok
     * @throws ApplicationException when fail to query
     * @since GSO 0.5
     */
    private boolean finishedQuerying(String nodeType, String queryUrl) throws ApplicationException {
        RestfulResponse queryRsp = getOperationResponse(queryUrl, CommonConstant.MethodType.GET);
        String queryContent = null;
        if(HttpCode.isSucess(queryRsp.getStatus())) {
            queryContent = queryRsp.getResponseContent();
        } else {
            LOGGER.error("fail to query the operation stuas:{}", nodeType);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }

        // process the query result
        JSONObject queryObj = JSONObject.fromObject(queryContent);
        JSONObject rspDesc = queryObj.getJSONObject("responseDescriptor");

        boolean flag = false;
        if("100".equals(rspDesc.get("progress")) && "finished".equals(rspDesc.get("status"))) {
            LOGGER.info("succeed to terminate the sub-service:{}", nodeType);
            flag = true;
        } else if("error".equals(rspDesc.get("status"))) {
            LOGGER.error("error in the result when query the operation status");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        } else {
            // do nothing
        }
        return flag;
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
     * @param nodeType type of the node instance
     * @param variable variable should be put in the url
     * @param step step of the operation (terminate,query,delete)
     * @return url can be used to invoke corresponding service
     * @since GSO 0.5
     */
    private String getUrl(String nodeType, String variable, String step) {

        String url = StringUtils.EMPTY;
        String nfvoUrl = StringUtils.EMPTY;
        String sdnoUrl = StringUtils.EMPTY;

        if(CommonConstant.Step.CREATE.equals(step)) {
            nfvoUrl = NFVO_CREATE_URL;
            sdnoUrl = SDNO_CREATE_URL;
        } else if(CommonConstant.Step.INSTANTIATE.equals(step)) {
            nfvoUrl = NFVO_INSTANTIATE_URL;
            sdnoUrl = SDNO_INSTANTIATE_URL;
        } else if(CommonConstant.Step.TERMINATE.equals(step)) {
            nfvoUrl = NFVO_TERMINATE_URL;
            sdnoUrl = SDNO_TERMINATE_URL;
        } else if(CommonConstant.Step.QUERY.equals(step)) {
            nfvoUrl = NFVO_QUERY_RUL;
            sdnoUrl = SDNO_QUERY_URL;
        } else if(CommonConstant.Step.DELETE.equals(step)) {
            nfvoUrl = NFVO_DELETE_URL;
            sdnoUrl = SDNO_DELETE_URL;
        } else {
            // do nothing
        }

        if(CommonConstant.NodeType.NFV_DC_TYPE.equals(nodeType)
                || CommonConstant.NodeType.NFV_POP_TYPE.equals(nodeType)
                || CommonConstant.NodeType.NFV_VBRAS_TYPE.equals(nodeType)) {
            url = String.format(nfvoUrl, variable);
        } else if(CommonConstant.NodeType.SDN_OVERLAYVPN_TYPE.equals(nodeType)
                || CommonConstant.NodeType.SDN_UNDERLAYVPN_TYPE.equals(nodeType)) {
            url = String.format(sdnoUrl, variable);
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
    public String createNS(String templateId, Map<String, String> inputMap) throws ApplicationException {

        // Step 1: Prepare Network Service Request
        NSRequest oRequest = new NSRequest();

        String nsNameKey = "serviceName";

        String descKey = "serviceDescription";

        oRequest.setNsdId(templateId);
        oRequest.setNsName(inputMap.get(nsNameKey));
        oRequest.setNsDescription(inputMap.get(descKey));

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(nodeType, null, CommonConstant.Step.CREATE));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);

        // Step 2: Send Network Service Request
        String req = "";
        req = JsonUtil.marshal(oRequest);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, req, null);

        // Step 3: Process Network Service Response
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        return obj.getString("nsInstanceId");

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
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(nodeType, instanceId, CommonConstant.Step.INSTANTIATE));

        // Step 2: Send Network Service Instantiate Request
        String networkSvcReq = "";
        networkSvcReq = JsonUtil.marshal(oRequest);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, networkSvcReq, null);

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
        paramsMap.put(CommonConstant.HttpContext.URL, getUrl(nodeType, jobId, CommonConstant.Step.QUERY));
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);

        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, null, null);

        // Process Network Service Instantiate Response
        NsProgressStatus nsProgress = null;
        nsProgress = JsonUtil.unMarshal(rsp.getResponseContent(), NsProgressStatus.class);

        return nsProgress;
    }

    @Override
    public void setNodeType(String type) {
        nodeType = type;

    }

}
