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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.DriverExceptionID;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.drivermo.DomainInputParameter;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.ResponseDescriptor;
import org.openo.gso.model.drivermo.ServiceNode;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.service.inf.IDriverManager;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Service management class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class DriverManagerImpl implements IDriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerImpl.class);
    
    /**
     * 10s
     */
    private static final long TEN_SECONDS = 10 * 1000;
    
    private String domain;

    /**
     * driver service interface
     */
    private IDriverService serviceInf;

    /**
     * DAO to operate service segment instance.
     */
    private IServiceSegmentDao serviceSegmentDao;

    /**
     * DAO to operate service package.
     */
    private IServicePackageDao servicePackageDao;
    
    /**
     * DAO to operate service model
     */
    private IServiceModelDao serviceModelDao;

    /**
     * Proxy of interface with catalog.
     */
    private ICatalogProxy catalogProxy;

    /**
     * @return Returns the serviceInf.
     */
    public IDriverService getServiceInf() {
        return serviceInf;
    }

    /**
     * @param serviceInf The serviceInf to set.
     */
    public void setServiceInf(IDriverService serviceInf) {
        this.serviceInf = serviceInf;
    }

    /**
     * @return Returns the serviceSegmentDao.
     */
    public IServiceSegmentDao getServiceSegmentDao() {
        return serviceSegmentDao;
    }

    /**
     * @param serviceSegmentDao The serviceSegmentDao to set.
     */
    public void setServiceSegmentDao(IServiceSegmentDao serviceSegmentDao) {
        this.serviceSegmentDao = serviceSegmentDao;
    }

    /**
     * @return Returns the servicePackageDao.
     */
    public IServicePackageDao getServicePackageDao() {
        return servicePackageDao;
    }

    /**
     * @param servicePackageDao The servicePackageDao to set.
     */
    public void setServicePackageDao(IServicePackageDao servicePackageDao) {
        this.servicePackageDao = servicePackageDao;
    }

    /**
     * @return Returns the catalogProxy.
     */
    public ICatalogProxy getCatalogProxy() {
        return catalogProxy;
    }

    /**
     * @param catalogProxy The catalogProxy to set.
     */
    public void setCatalogProxy(ICatalogProxy catalogProxy) {
        this.catalogProxy = catalogProxy;
    }
    
    /**
     * @return Returns the serviceModelDao.
     */
    public IServiceModelDao getServiceModelDao() {
        return serviceModelDao;
    }
    
    /**
     * @param serviceModelDao The serviceModelDao to set.
     */
    public void setServiceModelDao(IServiceModelDao serviceModelDao) {
        this.serviceModelDao = serviceModelDao;
    }

    /**
     * create network service<br>
     * 
     * @param servletReq http request
     * @param domain NFVO or SDNO
     * @return restfulResponse
     * @throws ApplicationException when fail to create network service
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createNs(HttpServletRequest servletReq, String domain) throws ApplicationException {
        // Step 1: get parameters from request for current node
        DomainInputParameter currentInput = getParamsForCurrentNode(servletReq);

        // Step 2: Call the Catalogue service to get service template id
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setDomain(domain);
        ServiceTemplate svcTmpl = getSvcTmplByNodeType(currentInput.getNodeType(), currentInput.getDomainHost());
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }

        //nsdId for nfvo is "id" in the response, while for sndo is "servcice template id"
        LOGGER.info("serviceTemplateId is {}, id is {}", svcTmpl.getServiceTemplateId(), svcTmpl.getId());
        String nsdId = StringUtils.EMPTY;
        if(CommonConstant.Domain.NFVO.equals(domain)){
            nsdId = svcTmpl.getId();
        }else {
            nsdId = svcTmpl.getServiceTemplateId();
        }
        LOGGER.info("domain is {}, and nsdId is {}", domain, nsdId);
        
        //Step 3: Call NFVO or SDNO lcm to create ns
        LOGGER.info("create ns -> begin");
        Map<String, Object> createParamMap = getCreateParams(nsdId, currentInput);
        RestfulResponse restRsp = serviceInf.createNS(createParamMap);
        JSONObject obj = JSONObject.fromObject(restRsp.getResponseContent());
        String nsInstanceId = obj.getString(CommonConstant.NS_INSTANCE_ID);
        if(StringUtils.isEmpty(nsInstanceId)) {
            LOGGER.error("Invalid instanceId from create operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_CREATE_OPERATION);
        }
        LOGGER.info("create ns -> end");
        LOGGER.info("save segment info -> begin");
        //Step 4: save segment information
        saveSegmentInfo(currentInput, nsInstanceId, domain, svcTmpl.getServiceTemplateId(), restRsp.getStatus());
        LOGGER.info("save segment info -> end");
        if(!HttpCode.isSucess(restRsp.getStatus())){
            LOGGER.error("fail to create ns");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_CREATE_NS);
        }
        
        return restRsp;
    }

    

    

    /**
     * delete network service<br>
     * 
     * @param nsInstanceId instance id
     * @param domain nfvo or sdno
     * @return response
     * @throws ApplicationException when fail to delete network service
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteNs(String nsInstanceId, String domain) throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * instantiate network service<br>
     * 
     * @param nsInstanceId instance id
     * @param httpRequest http request
     * @param domain sdno or nfvo
     * @return response
     * @throws ApplicationException when fail to instantiate network service
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse instantiateNs(String nsInstanceId, HttpServletRequest httpRequest, String domain)
            throws ApplicationException {
        // Step 1: get parameters from request for current node
        DomainInputParameter currentInput = getParamsForCurrentNode(httpRequest);
        // Step 2: Call the NFVO or SDNO service to instantiate service
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setDomain(domain);
        
        LOGGER.info("instantiate ns -> begin");
        Map<String, Object> instParamMap = getInstParams(nsInstanceId, currentInput);
        RestfulResponse rsp = serviceInf.instantiateNS(instParamMap);
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from instantiate operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_INSTANTIATE_OPERATION);
        }
        LOGGER.info("instantiate ns -> end");
        
        // Step 3: update segment operation
        LOGGER.info("update segment info -> begin");
        if(!HttpCode.isSucess(rsp.getStatus())){
            updateSegmentOperStatusInfoAndErrCode(nsInstanceId, domain, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            LOGGER.error("fail to instantiate ns");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        updateSegmentOperJobId(nsInstanceId, domain, jobId);
        LOGGER.info("update segment info -> end");
        
        return rsp;
    }

    /**
     * terminate network service<br>
     * 
     * @param nsInstanceId instance id
     * @param httpRequest http request
     * @param domain nfvo or sdno
     * @return response
     * @throws ApplicationException when fail to terminate network service
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse terminateNs(String nsInstanceId, HttpServletRequest httpRequest, String domain)
            throws ApplicationException {
        //get input for current node
        DomainInputParameter input = getParamsForCurrentNode(httpRequest);
        
        // Step 2: Call the NFVO or SDNO service to terminate service
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setDomain(domain);

        LOGGER.info("terminate ns -> begin");
        Map<String, Object> termParamMap = getTermParams(nsInstanceId, input);
        RestfulResponse rsp = serviceInf.terminateNs(termParamMap);
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from terminate operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_INSTANTIATE_OPERATION);
        }
        LOGGER.info("terminate ns -> end");
        
        // Step 3: update segment operation
        LOGGER.info("update segment info -> begin");
        if(!HttpCode.isSucess(rsp.getStatus())){
            updateSegmentOperStatusInfoAndErrCode(nsInstanceId, domain, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            LOGGER.error("fail to instantiate ns");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        updateSegmentOperJobId(nsInstanceId, domain, jobId);
        LOGGER.info("update segment info -> end");
        
        return rsp;
    }

    

    /**
     * get ns progress by job Id<br>
     * 
     * @param jobId job id
     * @param domain NFVO or SDNO
     * @return ns progress
     * @throws ApplicationException when fail to get ns progress
     * @since   SDNO 0.5
     */
    @Override
    public RestfulResponse getNsProgress(String jobId, String domain) throws ApplicationException {
        //Step 1: get service segmemt operation by job id
        ServiceSegmentModel segment = serviceSegmentDao.queryServiceSegment(jobId);
        //Step 2 : build query task
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String segmentId = segment.getServiceSegmentId();
        String segmentType = domain;
        String domainHost = segment.getDomainHost();
        parseDomainHost(domainHost, paramMap);
        QueryProgress task = new QueryProgress(jobId, paramMap);
        this.domain = domain;
        //Step 3: start query
        LOGGER.info("query ns status -> begin");
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        boolean queryFlag = true;
        RestfulResponse rsp = null;
        while(queryFlag) {
            try {
                Future<RestfulResponse> status = executor.submit(task);
                rsp = status.get();
            } catch(Exception e) {
                LOGGER.error("fail to query the operation status: {}", e);
                executor.shutdown();
                updateSegmentOperStatusInfoAndErrCode(segmentId, segmentType, CommonConstant.Status.ERROR, HttpStatus.INTERNAL_SERVER_ERROR_500, CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
            }
            
            if(!HttpCode.isSucess(rsp.getStatus())){
                LOGGER.info("fail to query job status");
                updateSegmentOperStatusInfoAndErrCode(segment.getServiceSegmentId(), domain, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
            }
            //Step 4: Process Network Service Instantiate Response
            NsProgressStatus nsProgress = null;
            nsProgress = JsonUtil.unMarshal(rsp.getResponseContent(), NsProgressStatus.class);
            ResponseDescriptor rspDesc = nsProgress.getResponseDescriptor();
            updateSegmentOperProgress(segmentId, segmentType, rspDesc.getProgress());
            if(CommonConstant.Progress.ONE_HUNDRED.equals(rspDesc.getProgress()) && CommonConstant.Status.FINISHED.equals(rspDesc.getStatus())) {
                LOGGER.info("job complete : create ns result is succeeded");
                updateSegmentOperStatusInfoAndErrCode(segmentId, segmentType, CommonConstant.Status.FINISHED, rspDesc.getErrorCode(), rspDesc.getStatusDescription());
                queryFlag = false;
            } else if(CommonConstant.Status.ERROR.equals(rspDesc.getStatus())) {
                LOGGER.error("job complete : create ns result is failed");
                updateSegmentOperStatusInfoAndErrCode(segmentId, segmentType, CommonConstant.Status.ERROR, rspDesc.getErrorCode(), rspDesc.getStatusDescription());
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
            } else {
                // do nothing
            }
        }
        LOGGER.info("query ns status -> end");
          
        return rsp;
    }
    
    

    class QueryProgress implements Callable<RestfulResponse> {

        String jobId;
        Map<String, Object> map;
        QueryProgress(String jobInfo, Map<String, Object> mapInfo) {
            jobId = jobInfo;
            map = mapInfo;
        }

        @Override
        public RestfulResponse call() throws Exception {
            // For every 10 seconds query progress
            Thread.sleep(TEN_SECONDS);
            serviceInf.setDomain(domain);
            return serviceInf.getNsProgress(jobId, map);
        }

    }

    /**
     * private method 0:get input parameters for current node<br>
     * 
     * @param servletReq http request
     * @return input parameters for current node
     * @since  GSO 0.5
     */
    private DomainInputParameter getParamsForCurrentNode(HttpServletRequest servletReq) {
        // Step 0: get request model
        String body = RestUtils.getRequestBody(servletReq);
        LOGGER.info("body from request is {}", body);
        String jsonBody = body.replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}").replaceAll("\"\\[", "\\]").replaceAll("\\]\"", "\\]");
        LOGGER.warn("json body from request is {}", jsonBody);
        ServiceNode serviceNode = null;
        serviceNode = JsonUtil.unMarshal(jsonBody, ServiceNode.class);

        // Step 1:Validate input parameters
        if((null == serviceNode.getNodeTemplateName()) || (null == serviceNode.getInputParameters())) {
            LOGGER.error("Input parameters from lcm/workflow are empty");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        
        // Step 2:Get input parameters for current node
        List<DomainInputParameter> inputList = serviceNode.getInputParameters();
        Map<String, DomainInputParameter> map = new HashMap<String, DomainInputParameter>();
        for(DomainInputParameter input : inputList) {
            map.put(input.getNodeTemplateName(), input);
        }
        DomainInputParameter currentInput = map.get(serviceNode.getNodeTemplateName());
        return currentInput;
    }
    
    /**
     * private method 1: get service template by node type<br>
     * 
     * @param nodeType node type
     * @return service template
     * @throws ApplicationException when fail to get service template
     * @since  GSO 0.5
     */
    private ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost) throws ApplicationException {

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
        LOGGER.info("response content is {}", rsp.getResponseContent());
        JSONArray array = JSONArray.fromObject(rsp.getResponseContent());
        return JsonUtil.unMarshal(array.getString(0), ServiceTemplate.class);
    }

    /**
     * private method2 : get create params<br>
     * 
     * @param currentIput input params for current node
     * @return param map for creating
     * @since  GSO 0.5
     */
    private Map<String, Object> getCreateParams(String nsdId, DomainInputParameter currentIput) {

        Map<String, Object> createParamMap = new HashMap<String, Object>();
        createParamMap.put(CommonConstant.NSD_ID, nsdId);
        createParamMap.put(CommonConstant.NS_NAME, currentIput.getSubServiceName());
        createParamMap.put(CommonConstant.DESC, currentIput.getSubServiceDesc());
        
        String domainHost = currentIput.getDomainHost();
        parseDomainHost(domainHost, createParamMap);

        return createParamMap;
    }
    
    /**
     * private method3: parse domain host<br>
     * 
     * @param domainHost host & port which httprequest should send to
     * @param paramsMap params map
     * @since  GSO 0.5
     */
    private void parseDomainHost(String domainHost, Map<String, Object> paramsMap) {
        if(StringUtils.isEmpty(domainHost)){
            LOGGER.info("domainHost is empty");
            return;
        }
        LOGGER.info("domainHost is {}", domainHost);
        String ip = domainHost.substring(0, domainHost.indexOf(":"));
        String port = domainHost.substring(domainHost.indexOf(":") + 1);
        paramsMap.put(CommonConstant.HttpContext.IP, ip);
        paramsMap.put(CommonConstant.HttpContext.PORT, port);
    }
    
    /**
     * private method 4: save segment information<br>
     * 
     * @param currentInput domain input parameters
     * @param nsInstanceId instantce id
     * @param domain nfvo or sdno
     * @param svcTmplId service template id
     * @param status http status
     * @since  GSO 0.5
     */
    private void saveSegmentInfo(DomainInputParameter currentInput, String nsInstanceId, String domain,
            String svcTmplId, int statusCode) {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        
        serviceSegment.setServiceId(currentInput.getServiceId());
        serviceSegment.setServiceSegmentId(nsInstanceId);
        serviceSegment.setServiceSegmentType(domain);
        serviceSegment.setServiceSegmentName(currentInput.getSubServiceName());
        serviceSegment.setTemplateId(svcTmplId);
        serviceSegment.setNodeType(currentInput.getNodeType());
        serviceSegment.setDomainHost(currentInput.getDomainHost());
        serviceSegment.setNodeTemplateName(currentInput.getNodeTemplateName());
        
        serviceSegmentDao.insertSegment(serviceSegment);
        
        ServiceSegmentOperation svcSegmentOper = new ServiceSegmentOperation();
        svcSegmentOper.setServiceSegmentId(nsInstanceId);
        svcSegmentOper.setServiceSegmentType(domain);
        if(HttpCode.isSucess(statusCode)){
            svcSegmentOper.setStatus(CommonConstant.Status.PROCESSING);
        } else {
            svcSegmentOper.setStatus(CommonConstant.Status.ERROR);
            svcSegmentOper.setErrorCode(statusCode);
        }
        
        serviceSegmentDao.insertSegmentOper(svcSegmentOper);
    }
    
    /**
     * private method5 : get instantiate params<br>
     * 
     * @param nsInstanceId instance id
     * @param currentIput input params for current node
     * @return param map for instantiate
     * @since  GSO 0.5
     */
    private Map<String, Object> getInstParams(String nsInstanceId, DomainInputParameter currentIput) {

        Map<String, Object> instParamMap = new HashMap<String, Object>();
        instParamMap.put(CommonConstant.NS_INSTANCE_ID, nsInstanceId);
        instParamMap.put(CommonConstant.ADDITIONAL_PARAM_FOR_NS, currentIput.getAdditionalParamForNs());
        
        String domainHost = currentIput.getDomainHost();
        parseDomainHost(domainHost, instParamMap);

        return instParamMap;
    }
    
    
    /**
     * private method 6: update segment operation status<br>
     * 
     * @param segmentId instance id
     * @param segmentType nfvo or sdno
     * @param status status: processing, finished, error
     * @param errCode http code
     * @since  GSO 0.5
     */
    private void updateSegmentOperStatusInfoAndErrCode(String segmentId, String segmentType, String status, int errCode, String statusDesc) {
        ServiceSegmentOperation segmentOper = new ServiceSegmentOperation();
        segmentOper.setServiceSegmentId(segmentId);
        segmentOper.setServiceSegmentType(segmentType);
        segmentOper.setStatus(status);
        segmentOper.setErrorCode(errCode);
        segmentOper.setStatusDescription(statusDesc);
        serviceSegmentDao.updateSegmentOperStatus(segmentOper);
        
    }
    
    /**
     * private method 7: update segment operation job id<br>
     * 
     * @param segmentId instance id
     * @param segmentType nfvo or sdno
     * @param jobId job id
     * @since  GSO 0.5
     */
    private void updateSegmentOperJobId(String segmentId, String segmentType, String jobId) {
        ServiceSegmentOperation segmentOper = new ServiceSegmentOperation();
        segmentOper.setServiceSegmentId(segmentId);
        segmentOper.setServiceSegmentType(segmentType);
        segmentOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOperJobId(segmentOper);
    }
    
    /**
     * private method8: update segment operation progress<br>
     * 
     * @param segmentId instance id
     * @param segmentType nfvo or sdno
     * @param progress prgress
     * @since  GSO 0.5
     */
    private void updateSegmentOperProgress(String segmentId, String segmentType, String progress) {
        ServiceSegmentOperation segmentOper = new ServiceSegmentOperation();
        segmentOper.setServiceSegmentId(segmentId);
        segmentOper.setServiceSegmentType(segmentType);
        segmentOper.setProcess(Integer.valueOf(progress));
        serviceSegmentDao.updateSegmentOperProgress(segmentOper);
        
    }
    
    /**
     * private method9 : get terminate params<br>
     * 
     * @param nsInstanceId instance id
     * @param input input params for current node
     * @return param map for terminate
     * @since  GSO 0.5
     */
    private Map<String, Object> getTermParams(String nsInstanceId, DomainInputParameter input) {
        
        Map<String, Object> termParamMap = new HashMap<String, Object>();
        termParamMap.put(CommonConstant.NS_INSTANCE_ID, nsInstanceId);
        
        String domainHost = input.getDomainHost();
        parseDomainHost(domainHost, termParamMap);

        return termParamMap;
    }
    
    

}
