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

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.Constant;
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
import org.openo.gso.model.servicemo.ServiceCreateReq;
import org.openo.gso.model.servicemo.ServiceCreateReqDetail;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.model.servicemo.ServiceOperationRsp;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.model.servicemo.ServiceSegmentReq;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.service.inf.IDriverManager;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final long TEN_SECONDS = 10 * 1000L;
    
    private String svcSegmentType;

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
     * @param segmentType NFVO or SDNO
     * @return restfulResponse
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createNs(HttpServletRequest servletReq, String segmentType) {
        // Step 1: get parameters from request for current node
        DomainInputParameter currentInput = getParamsForCurrentNode(servletReq);

        // Step 2: Call the Catalogue service to get service template id
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setSegmentType(segmentType);
        ServiceTemplate svcTmpl = serviceInf.getSvcTmplByNodeType(currentInput.getNodeType(), currentInput.getDomainHost());
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }

        //nsdId for NFVO is "id" in the response, while for SDNO is "servcice template id"
        LOGGER.info("serviceTemplateId is {}, id is {}", svcTmpl.getServiceTemplateId(), svcTmpl.getId());
        String nsdId;
        if(CommonConstant.SegmentType.NFVO.equals(segmentType)){
            nsdId = svcTmpl.getId();
        }else {
            nsdId = svcTmpl.getServiceTemplateId();
        }
        LOGGER.info("segmentType is {}, and nsdId is {}", segmentType, nsdId);
        
        //Step 3: Call NFVO or SDNO lcm to create ns
        LOGGER.info("create ns -> begin");
        Map<String, Object> createParamMap = getCreateParams(nsdId, currentInput);
        RestfulResponse restRsp = serviceInf.createNs(createParamMap);
        JSONObject obj = JSONObject.fromObject(restRsp.getResponseContent());
        String segmentId = obj.getString(CommonConstant.NS_INSTANCE_ID);
        if(StringUtils.isEmpty(segmentId)) {
            LOGGER.error("Invalid instanceId from create operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_CREATE_OPERATION);
        }
        LOGGER.info("create ns -> end");
        LOGGER.info("save segment and operaton info -> begin");
        //Step 4: save segment information 
        ServiceSegmentModel segmentInfo = buildSegmentInfo(currentInput, segmentId, segmentType, svcTmpl.getServiceTemplateId());
        serviceSegmentDao.insertSegment(segmentInfo);
        //Step 5: and segment operation information
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE, currentInput.getServiceId(), CommonConstant.Status.PROCESSING);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save segment and operation info -> end");
        
        if(!HttpCode.isSucess(restRsp.getStatus())){
            LOGGER.error("update segment operation status : fail to create ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, restRsp.getStatus(), CommonConstant.StatusDesc.CREATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_CREATE_NS);
        }
        
        return restRsp;
    }

    /**
     * delete network service<br>
     * 
     * @param servletReq http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteNs(HttpServletRequest servletReq, String segmentType) {
        DomainInputParameter currentInput = getParamsForCurrentNode(servletReq);
        String segmentId = currentInput.getSubServiceId();
        //Step 1: get service segment by segmentId and segmentType
        ServiceSegmentModel segment = serviceSegmentDao.queryServiceSegmentByIdAndType(segmentId, segmentType);
        Map<String, Object> delParamMap = getDelParams(segmentId, segment.getDomainHost());
        
        // Step 2: Call the NFVO or SDNO service to instantiate service
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setSegmentType(segmentType);
        
        LOGGER.info("delete ns -> begin");
        RestfulResponse rsp = serviceInf.deleteNs(delParamMap);
        LOGGER.info("delete ns -> end");
        if(!HttpCode.isSucess(rsp.getStatus())){
            LOGGER.error("fail to delete ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }

        // update service segment operation status
        ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
        updateSegmentOperStatus(statusSegOper, CommonConstant.Status.FINISHED, rsp.getStatus(), null);
        LOGGER.info("update segment operaton status for delete -> end");
        
        // delete segment info
        deleteSegmentInfo(segmentId, segmentType);
        LOGGER.info("delete segment information -> end");
        return rsp;
        
    }

    
    
    

    /**
     * instantiate network service<br>
     * 
     * @param segmentId instance id
     * @param httpRequest http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse instantiateNs(String segmentId, HttpServletRequest httpRequest, String segmentType) {
        // Step 1: get parameters from request for current node
        DomainInputParameter currentInput = getParamsForCurrentNode(httpRequest);
        // Step 2: Call the NFVO or SDNO service to instantiate service
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setSegmentType(segmentType);
        
        LOGGER.info("instantiate ns -> begin");
        Map<String, Object> instParamMap = getInstParams(segmentId, currentInput);
        RestfulResponse rsp = serviceInf.instantiateNs(instParamMap);
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from instantiate operation");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_INSTANTIATE_OPERATION);
        }
        LOGGER.info("instantiate ns -> end");
        
        if(!HttpCode.isSucess(rsp.getStatus())){
            LOGGER.error("update segment operation status : fail to instantiate ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        
        // Step 3: update segment operation job id
        LOGGER.info("update segment operation job id -> begin");
        ServiceSegmentOperation jobSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
        jobSegOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOper(jobSegOper);
        LOGGER.info("update segment operation job id -> end");
        
        return rsp;
    }

    /**
     * terminate network service<br>
     * 
     * @param nsInstanceId instance id
     * @param httpRequest http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse terminateNs(HttpServletRequest httpRequest, String segmentType) {
        //Step1: get input for current node
        DomainInputParameter input = getParamsForCurrentNode(httpRequest);
        String segmentId = input.getSubServiceId();
        // save segment operation info for delete process
        LOGGER.info("save segment operation for delete process");
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE, input.getServiceId(), CommonConstant.Status.PROCESSING);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        
        // Step 2: Call the NFVO or SDNO service to terminate service
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setSegmentType(segmentType);

        LOGGER.info("terminate ns -> begin");
        Map<String, Object> termParamMap = getTermParams(segmentId, input);
        RestfulResponse rsp = serviceInf.terminateNs(termParamMap);
        JSONObject obj = JSONObject.fromObject(rsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from terminate operation");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_TERMINATE_OPERATION);
        }
        LOGGER.info("terminate ns -> end");
        
        // Step 3: update segment operation
        if(!HttpCode.isSucess(rsp.getStatus())){
            LOGGER.error("fail to instantiate ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        LOGGER.info("update segment job id -> begin");
        ServiceSegmentOperation jobSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
        jobSegOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOper(jobSegOper);
        LOGGER.info("update segment job id -> end");
        
        return rsp;
    }

    

    /**
     * get ns progress by job Id<br>
     * 
     * @param jobId job id
     * @param segmentType NFVO or SDNO
     * @return ns progress
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse getNsProgress(String jobId, String segmentType) {
        ServiceSegmentOperation segmentOper = serviceSegmentDao.querySegmentOperByJobIdAndType(jobId, segmentType);
        String segmentId = segmentOper.getServiceSegmentId();
        String operType = segmentOper.getOperationType();
        //Step 1: get service segmemt operation by segment id and segment type
        ServiceSegmentModel segment = serviceSegmentDao.queryServiceSegmentByIdAndType(segmentId, segmentType);
        //Step 2 : build query task
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put(CommonConstant.JOB_ID, jobId);
        String domainHost = segment.getDomainHost();
        parseDomainHost(domainHost, paramMap);
        QueryProgress task = new QueryProgress(paramMap);
        this.svcSegmentType = segmentType;
        //Step 3: start query
        LOGGER.info("query ns status -> begin");
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        RestfulResponse rsp = null;
        try {
            Future<RestfulResponse> status = executor.submit(task);
            rsp = status.get();
        } catch(Exception e) {
            LOGGER.error("fail to query the operation status: {}", e);
            executor.shutdown();
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }

        if(!HttpCode.isSucess(rsp.getStatus())) {
            LOGGER.info("fail to query job status");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }
        // Step 4: Process Network Service Instantiate Response
        NsProgressStatus nsProgress = JsonUtil.unMarshal(rsp.getResponseContent(), NsProgressStatus.class);
        ResponseDescriptor rspDesc = nsProgress.getResponseDescriptor();
        // Step 5: update segment operation progress
        ServiceSegmentOperation progressSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
        progressSegOper.setProgress(Integer.valueOf(rspDesc.getProgress()));
        serviceSegmentDao.updateSegmentOper(progressSegOper);

        // Step 6: update segment operation status
        if(CommonConstant.Progress.ONE_HUNDRED.equals(rspDesc.getProgress()) && CommonConstant.Status.FINISHED.equals(rspDesc.getStatus())) {
            LOGGER.info("job result is succeeded, operType is {}", operType);
            if(CommonConstant.OperationType.CREATE.equals(operType)) {
                ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
                updateSegmentOperStatus(statusSegOper, CommonConstant.Status.FINISHED, rspDesc.getErrorCode(), rspDesc.getStatusDescription());
            }
        } else if(CommonConstant.Status.ERROR.equals(rspDesc.getStatus())) {
            LOGGER.error("job result is failed, operType is {}", operType);
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rspDesc.getErrorCode(), rspDesc.getStatusDescription());
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        } else {
            // do nothing
        }
        LOGGER.info("query ns status -> end");
          
        return rsp;
    }
    
    

    class QueryProgress implements Callable<RestfulResponse> {

        Map<String, Object> map;
        QueryProgress(Map<String, Object> mapInfo) {
            map = mapInfo;
        }

        @Override
        public RestfulResponse call() throws Exception {
            // For every 10 seconds query progress
            Thread.sleep(TEN_SECONDS);
            serviceInf.setSegmentType(svcSegmentType);
            return serviceInf.getNsProgress(map);
        }

    }

    /**
     * private method 1:get input parameters for current node<br>
     * 
     * @param servletReq http request
     * @return input parameters for current node
     * @since  GSO 0.5
     */
    private DomainInputParameter getParamsForCurrentNode(HttpServletRequest servletReq) {
        // Step 0: get request model
        String body = RestUtils.getRequestBody(servletReq);
        LOGGER.info("body from request is {}", body);
        String jsonBody = body.replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}").replaceAll("\"\\[", "\\[").replaceAll("\\]\"", "\\]");
        LOGGER.warn("json body from request is {}", jsonBody);
        ServiceNode serviceNode = JsonUtil.unMarshal(jsonBody, ServiceNode.class);

        // Step 1:Validate input parameters
        if((null == serviceNode.getNodeTemplateName()) || (null == serviceNode.getSegments())) {
            LOGGER.error("Input parameters from lcm/workflow are empty");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        
        // Step 2:Get input parameters for current node
        List<DomainInputParameter> inputList = serviceNode.getSegments();
        Map<String, DomainInputParameter> map = new HashMap<String, DomainInputParameter>();
        for(DomainInputParameter input : inputList) {
            map.put(input.getNodeTemplateName(), input);
        }
        DomainInputParameter currentInput = map.get(serviceNode.getNodeTemplateName());
        return currentInput;
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
     * private method 4: save segment information<br>
     * 
     * @param currentInput domain input parameters
     * @param segmentId segment id
     * @param segmentType NFVO|SDNO|GSO
     * @param svcTmplId service template id
     * @param status http status
     * @return service segment instance
     * @since  GSO 0.5
     */
    private ServiceSegmentModel buildSegmentInfo(DomainInputParameter currentInput, String segmentId, String segmentType,
            String svcTmplId) {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        
        serviceSegment.setServiceId(currentInput.getServiceId());
        serviceSegment.setServiceSegmentId(segmentId);
        serviceSegment.setServiceSegmentType(segmentType);
        serviceSegment.setServiceSegmentName(currentInput.getSubServiceName());
        serviceSegment.setTemplateId(svcTmplId);
        serviceSegment.setNodeType(currentInput.getNodeType());
        serviceSegment.setDomainHost(currentInput.getDomainHost());
        serviceSegment.setNodeTemplateName(currentInput.getNodeTemplateName());
        
        return serviceSegment;
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
     * @param segmentOper service segment operation instance
     * @param status status: processing, finished, error
     * @param errCode http code
     * @param statusDesc status description
     * @since  GSO 0.5
     */
    private void updateSegmentOperStatus(ServiceSegmentOperation segmentOper, String status, int errCode, String statusDesc) {

        segmentOper.setStatus(status);
        segmentOper.setErrorCode(errCode);
        segmentOper.setStatusDescription(statusDesc);
        
        serviceSegmentDao.updateSegmentOper(segmentOper);
        
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
    
    /**
     * private method10 : get delete params<br>
     * 
     * @param nsInstanceId instance id
     * @param domainHost domain host
     * @return param map for delete
     * @since  GSO 0.5
     */
    private Map<String, Object> getDelParams(String nsInstanceId, String domainHost) {
        Map<String, Object> delParamMap = new HashMap<String, Object>();
        delParamMap.put(CommonConstant.NS_INSTANCE_ID, nsInstanceId);
        parseDomainHost(domainHost, delParamMap);
        return delParamMap;
    }
    
    /**
     * delete service segment<br>
     * 
     * @param segmentId instance id
     * @param segmentType NFVO or SDNO
     * @since  GSO 0.5
     */
    private void deleteSegmentInfo(String segmentId, String segmentType) {
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setServiceSegmentId(segmentId);
        serviceSegment.setServiceSegmentType(segmentType);
        serviceSegmentDao.deleteSegmentByIdAndType(serviceSegment);
    }

    /**
     * create gso service<br>
     * 
     * @param servletReq http request
     * @param segmentType GSO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createGsoNs(HttpServletRequest servletReq, String segmentType) {
        // Step1 get input parameters for current node
        DomainInputParameter cInput = getParamsForCurrentNode(servletReq);
        // Step2 get service template id and csar id by node type
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setSegmentType(segmentType);
        ServiceTemplate svcTmpl = serviceInf.getSvcTmplByNodeType(cInput.getNodeType(), cInput.getDomainHost());
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }
        LOGGER.info("create gso ns -> begin");
        Map<String, Object> inputMap = getCreateGsoParams(cInput, svcTmpl);
        // Step3 invoke lcm
        serviceInf.setSegmentType(segmentType);
        RestfulResponse resp = serviceInf.createGsoNs(inputMap);
        JSONObject obj = JSONObject.fromObject(resp.getResponseContent());
        Object service = obj.get(Constant.SERVICES_INDENTIRY);
        JSONObject jsonSvc = JSONObject.fromObject(service);
        String subServiceId = jsonSvc.getString(Constant.SERVICE_INSTANCE_ID);
        String opertionId = jsonSvc.getString(Constant.SERVICE_OPERATION_ID);
        if(StringUtils.isEmpty(subServiceId) || StringUtils.isEmpty(opertionId)) {
            LOGGER.error("Invalid response from create operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_CREATE_OPERATION);
        }
        LOGGER.info("create gso ns -> end");
        LOGGER.info("save segment and operaton info -> begin");
        //Step 4: save segment information 
        ServiceSegmentModel segmentInfo = buildSegmentInfo(cInput, subServiceId, segmentType, svcTmpl.getServiceTemplateId());
        serviceSegmentDao.insertSegment(segmentInfo);
        //Step 5: and segment operation information
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(subServiceId, segmentType, CommonConstant.OperationType.CREATE, cInput.getServiceId(), CommonConstant.Status.PROCESSING);
        segmentOperInfo.setJobId(opertionId);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save segment and operation info -> end");
        
        if(!HttpCode.isSucess(resp.getStatus())){
            LOGGER.error("update segment operation status : fail to create gso ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(subServiceId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, resp.getStatus(), CommonConstant.StatusDesc.CREATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_CREATE_GSO_NS);
        }
        return resp;
    }

    /**
     * get input parameters map for create gso service<br>
     * 
     * @param cInput input parameters for current node
     * @param svcTmpl service tempalte
     * @return input parameters map for create gso service
     * @since  GSO 0.5
     */
    private Map<String, Object> getCreateGsoParams(DomainInputParameter cInput, ServiceTemplate svcTmpl) {
        //build sub segments
        List<ServiceSegmentReq> segments = cInput.getSegments();
        // build parameters
        ServiceSegmentReq parameters = new ServiceSegmentReq();
        parameters.setDomainHost(cInput.getDomainHost());
        parameters.setNodeTemplateName(cInput.getNodeTemplateName());
        parameters.setNodeType(cInput.getNodeType());
        parameters.setSegments(segments);
        parameters.setAdditionalParamForNs(cInput.getAdditionalParamForNs());
        
        //build request detail
        ServiceCreateReqDetail service = new ServiceCreateReqDetail();
        service.setName(cInput.getSubServiceName());
        service.setDescription(cInput.getSubServiceDesc());
        service.setServiceDefId(svcTmpl.getCsarId());
        service.setTemplateId(svcTmpl.getServiceTemplateId());
        service.setParameters(parameters);
        
        //build request
        ServiceCreateReq req = new ServiceCreateReq();
        req.setService(service);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CommonConstant.HttpContext.RAW_DATA, JsonUtil.marshal(req));
        
        parseDomainHost(cInput.getDomainHost(), map);
        
        return map;
    }

    /**
     * delete gso service<br>
     * 
     * @param servletReq http request
     * @param segmentType GSO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteGsoNs(HttpServletRequest servletReq, String segmentType) {
        // Step1 get input parameters for current node
        DomainInputParameter dInput = getParamsForCurrentNode(servletReq);
        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        // Step2 invoke lcm
        LOGGER.info("delete gso ns -> begin");
        Map<String, Object> inputMap = getDeleteGsoParams(dInput);
        // Step3 invoke lcm
        serviceInf.setSegmentType(segmentType);
        RestfulResponse resp = serviceInf.deleteGsoNs(inputMap);
        JSONObject obj = JSONObject.fromObject(resp.getResponseContent());
        String opertionId = obj.getString(Constant.SERVICE_OPERATION_ID);
        if(StringUtils.isEmpty(opertionId)) {
            LOGGER.error("Invalid response from delete operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_DELETE_OPERATION);
        }
        LOGGER.info("delete gso ns -> end");
        LOGGER.info("save operaton info -> begin");
        // Step 3 save operation info
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(dInput.getSubServiceId(), segmentType, CommonConstant.OperationType.DELETE, dInput.getServiceId(), CommonConstant.Status.PROCESSING);
        segmentOperInfo.setJobId(opertionId);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save operation info -> end");
        if(!HttpCode.isSucess(resp.getStatus())){
            LOGGER.error("update segment operation status : fail to delete gso ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(dInput.getSubServiceId(), segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, resp.getStatus(), CommonConstant.StatusDesc.DELETE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_DELETE_GSO_NS);
        }
        return resp;
    }

    /**
     * get input parameters for delete gso service<br>
     * 
     * @param dInput input parameters map for delete gso service
     * @return parameters map
     * @since  GSO 0.5
     */
    private Map<String, Object> getDeleteGsoParams(DomainInputParameter dInput) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CommonConstant.NS_INSTANCE_ID, dInput.getSubServiceId());
        parseDomainHost(dInput.getDomainHost(), map);
        
        return map;
    }

    /**
     * query gso service job status<br>
     * 
     * @param jobId job id
     * @param segmentType GSO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse getGsoNsProgress(String jobId, String segmentType) {
        ServiceSegmentOperation segmentOperation = serviceSegmentDao.querySegmentOperByJobIdAndType(jobId, segmentType);
        String segmentId = segmentOperation.getServiceSegmentId();
        String operType = segmentOperation.getOperationType();
        //Step 1: get service segmemt operation by segment id and segment type
        ServiceSegmentModel svcSegment = serviceSegmentDao.queryServiceSegmentByIdAndType(segmentId, segmentType);
        //Step 2 : build query task
        Map<String, Object> inputParamMap = new HashMap<String, Object>();
        inputParamMap.put(CommonConstant.JOB_ID, jobId);
        inputParamMap.put(CommonConstant.NS_INSTANCE_ID, segmentId);
        String domainHost = svcSegment.getDomainHost();
        parseDomainHost(domainHost, inputParamMap);
        QueryProgress task = new QueryProgress(inputParamMap);
        this.svcSegmentType = segmentType;
        //Step 3: start query
        LOGGER.info("query gso ns status -> begin");
        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        RestfulResponse rsp = null;
        try {
            Future<RestfulResponse> nsStatus = executor.submit(task);
            rsp = nsStatus.get();
        } catch(Exception e) {
            LOGGER.error("fail to query the gso operation status: {}", e);
            executor.shutdown();
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR, CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }

        if(!HttpCode.isSucess(rsp.getStatus())) {
            LOGGER.info("fail to query gso job status");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), CommonConstant.StatusDesc.QUERY_JOB_STATUS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        }

        // Step 4: Process Response
        ServiceOperationRsp svcOperRsp = JsonUtil.unMarshal(rsp.getResponseContent(), ServiceOperationRsp.class);
        ServiceOperation svcOper = svcOperRsp.getOperation();
        // Step 5: update segment operation progress
        ServiceSegmentOperation progressSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
        progressSegOper.setProgress(svcOper.getProgress());
        serviceSegmentDao.updateSegmentOper(progressSegOper);

        // Step 6: update segment operation status
        if(CommonConstant.Progress.ONE_HUNDRED.equals(String.valueOf(svcOper.getProgress())) && CommonConstant.Status.FINISHED.equals(svcOper.getResult())) {
            LOGGER.info("job result is succeeded, operType is {}", operType);
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.FINISHED, rsp.getStatus(), svcOper.getOperationContent());

        } else if(CommonConstant.Status.ERROR.equals(svcOper.getResult())) {
            LOGGER.error("job result is failed, operType is {}", operType);
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), svcOper.getReason());
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        } else {
            // do nothing
        }
        LOGGER.info("query gso ns status -> end");
          
        return rsp;

    }
    
    

}
