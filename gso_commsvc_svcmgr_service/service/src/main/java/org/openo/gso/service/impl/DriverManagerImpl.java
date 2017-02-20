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

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.CommonConstant.Step;
import org.openo.gso.constant.Constant;
import org.openo.gso.constant.DriverExceptionID;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.drivermo.NsCreateReq;
import org.openo.gso.model.drivermo.NsInstantiateReq;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.ResponseDescriptor;
import org.openo.gso.model.drivermo.SegmentInputParameter;
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
import org.openo.gso.util.RestfulUtil;
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
    
    /**
     * nfvo url map
     */
    private static Map nfvoUrlMap;
    
    static {
        nfvoUrlMap = new HashMap<String, String>();
        nfvoUrlMap.put(Step.CREATE, CommonConstant.NFVO_CREATE_URL);
        nfvoUrlMap.put(Step.INSTANTIATE, CommonConstant.NFVO_INSTANTIATE_URL);
        nfvoUrlMap.put(Step.TERMINATE, CommonConstant.NFVO_TERMINATE_URL);
        nfvoUrlMap.put(Step.DELETE, CommonConstant.NFVO_DELETE_URL);
        nfvoUrlMap.put(Step.QUERY, CommonConstant.NFVO_QUERY_URL);
    }
    
    /**
     * sdno url map
     */
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
     * @param segInput input parameters for current node from http request
     * @param segmentType NFVO or SDNO
     * @return restfulResponse
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createNs(SegmentInputParameter segInput, String segmentType) {
        
        // Step1: get service template by node type
        ServiceTemplate svcTmpl = catalogProxy.getSvcTmplByNodeType(segInput.getNodeType(), segInput.getDomainHost());
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }

        // nsdId for NFVO is "id" in the response, while for SDNO is "servcice template id"
        LOGGER.info("serviceTemplateId is {}, id is {}", svcTmpl.getServiceTemplateId(), svcTmpl.getId());
        String nsdId;
        if(CommonConstant.SegmentType.NFVO.equals(segmentType)){
            nsdId = svcTmpl.getId();
        }else {
            nsdId = svcTmpl.getServiceTemplateId();
        }
        LOGGER.info("segmentType is {}, and nsdId is {}", segmentType, nsdId);

        LOGGER.info("create ns -> begin");
        // Step2: Prepare url and method type
        String url = getUrl(segmentType, null, CommonConstant.Step.CREATE);
        String methodType = CommonConstant.MethodType.POST;
               
        // Step3: Prepare restful parameters and options
        NsCreateReq oRequest = new NsCreateReq();
        oRequest.setNsdId(nsdId);
        oRequest.setNsName(segInput.getSubServiceName());
        oRequest.setDescription(segInput.getSubServiceDesc());
        String createReq = JsonUtil.marshal(oRequest);

        RestfulParametes restfulParametes = RestfulUtil.setRestfulParameters(createReq, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        
        //Step4: Call NFVO or SDNO lcm to create ns        
        RestfulResponse createRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParametes, options);
        LOGGER.info("create ns response status is : {}", createRsp.getStatus());
        LOGGER.info("create ns response content is : {}", createRsp.getResponseContent());
        JSONObject obj = JSONObject.fromObject(createRsp.getResponseContent());
        String segmentId = obj.getString(CommonConstant.NS_INSTANCE_ID);
        if(StringUtils.isEmpty(segmentId)) {
            LOGGER.error("Invalid instanceId from create operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_CREATE_OPERATION);
        }
        LOGGER.info("create ns -> end");
        LOGGER.info("save segment and operaton info -> begin");
        //Step 5: save segment information 
        ServiceSegmentModel segmentInfo = buildSegmentInfo(segInput, segmentId, segmentType, svcTmpl.getServiceTemplateId());
        serviceSegmentDao.insertSegment(segmentInfo);
        //Step 6: save segment operation information
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE, segInput.getServiceId(), CommonConstant.Status.PROCESSING);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save segment and operation info -> end");
        
        if(!HttpCode.isSucess(createRsp.getStatus())){
            LOGGER.error("update segment operation status : fail to create ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, createRsp.getStatus(), CommonConstant.StatusDesc.CREATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_CREATE_NS);
        }
        
        return createRsp;
    }

    /**
     * delete network service<br>
     * 
     * @param segInput input parameters for current node from http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteNs(SegmentInputParameter segInput, String segmentType) {
        String segmentId = segInput.getSubServiceId();
        LOGGER.info("delete ns -> begin");
        //Step1: prepare url and methodType
        String url = getUrl(segmentType, segmentId, CommonConstant.Step.DELETE);
        String methodType = CommonConstant.MethodType.DELETE;
        
        //Step2: prepare restful parameters and options
        RestfulParametes restfulParameters = RestfulUtil.setRestfulParameters(null, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        RestfulResponse deleteRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParameters, options);
        LOGGER.info("delete ns response status is : {}", deleteRsp.getStatus());
        LOGGER.info("delete ns response content is : {}", deleteRsp.getResponseContent());
        LOGGER.info("delete ns -> end");
        if(!HttpCode.isSucess(deleteRsp.getStatus())){
            LOGGER.error("fail to delete ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, deleteRsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }

        //Step3: update service segment operation status
        ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
        updateSegmentOperStatus(statusSegOper, CommonConstant.Status.FINISHED, deleteRsp.getStatus(), null);
        LOGGER.info("update segment operaton status for delete -> end");
        
        //Step4: delete segment info
        deleteSegmentInfo(segmentId, segmentType);
        LOGGER.info("delete segment information -> end");
        return deleteRsp;
        
    }

    
    
    

    /**
     * instantiate network service<br>
     * 
     * @param segmentId instance id
     * @param segInput input parameters for current node from http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse instantiateNs(String segmentId, SegmentInputParameter segInput, String segmentType) {
        //Call the NFVO or SDNO service to instantiate service
        LOGGER.info("instantiate ns -> begin");
        // Step1: prepare url and 
        String url = getUrl(segmentType, segmentId, CommonConstant.Step.INSTANTIATE);
        String methodType = CommonConstant.MethodType.POST;
                
        // Step2: Prepare restful parameters and options
        NsInstantiateReq oRequest = new NsInstantiateReq();
        oRequest.setNsInstanceId(segmentId);
        oRequest.setAdditionalParamForNs(segInput.getAdditionalParamForNs());
        String instReq = JsonUtil.marshal(oRequest);
        
        RestfulParametes restfulParameters = RestfulUtil.setRestfulParameters(instReq, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        
        RestfulResponse instRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParameters, options);
        LOGGER.info("instantiate ns response status is : {}", instRsp.getStatus());
        LOGGER.info("instantiate ns response content is : {}", instRsp.getResponseContent());
        JSONObject obj = JSONObject.fromObject(instRsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from instantiate operation");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, instRsp.getStatus(), CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_INSTANTIATE_OPERATION);
        }
        LOGGER.info("instantiate ns -> end");
        
        if(!HttpCode.isSucess(instRsp.getStatus())){
            LOGGER.error("update segment operation status : fail to instantiate ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, instRsp.getStatus(), CommonConstant.StatusDesc.INSTANTIATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        
        // Step 3: update segment operation job id
        LOGGER.info("update segment operation job id -> begin");
        ServiceSegmentOperation jobSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.CREATE);
        jobSegOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOper(jobSegOper);
        LOGGER.info("update segment operation job id -> end");
        
        return instRsp;
    }

    /**
     * terminate network service<br>
     * 
     * @param nsInstanceId instance id
     * @param segInput input parameters for current node from http request
     * @param segmentType NFVO or SDNO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse terminateNs(SegmentInputParameter segInput, String segmentType) {
        String segmentId = segInput.getSubServiceId();
        //Step1: save segment operation info for delete process
        LOGGER.info("save segment operation for delete process");
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE, segInput.getServiceId(), CommonConstant.Status.PROCESSING);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);

        LOGGER.info("terminate ns -> begin");
        //Step2: prepare url and method type        
        String url = getUrl(segmentType, segmentId, CommonConstant.Step.TERMINATE);
        String methodType = CommonConstant.MethodType.POST;
        
        //Step3: prepare restful parameters and options
        RestfulParametes restfulParametes = RestfulUtil.setRestfulParameters(null, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        
        //Step4: Call the NFVO or SDNO service to terminate service
        RestfulResponse terminateRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParametes, options);
        LOGGER.info("terminate ns response status is : {}", terminateRsp.getStatus());
        LOGGER.info("terminate ns response content is : {}", terminateRsp.getResponseContent());
        JSONObject obj = JSONObject.fromObject(terminateRsp.getResponseContent());
        String jobId = obj.getString(CommonConstant.JOB_ID);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from terminate operation");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, terminateRsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSE_FROM_TERMINATE_OPERATION);
        }
        LOGGER.info("terminate ns -> end");
        
        // Step 3: update segment operation
        if(!HttpCode.isSucess(terminateRsp.getStatus())){
            LOGGER.error("fail to instantiate ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, terminateRsp.getStatus(), CommonConstant.StatusDesc.TERMINATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        LOGGER.info("update segment job id -> begin");
        ServiceSegmentOperation jobSegOper = new ServiceSegmentOperation(segmentId, segmentType, CommonConstant.OperationType.DELETE);
        jobSegOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOper(jobSegOper);
        LOGGER.info("update segment job id -> end");
        
        return terminateRsp;
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
        QueryProgress task = new QueryProgress(segmentType, null, jobId, segment.getDomainHost());
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
                updateSegmentOperStatus(statusSegOper, CommonConstant.Status.FINISHED, rsp.getStatus(), rspDesc.getStatusDescription());
            }
        } else if(CommonConstant.Status.ERROR.equals(rspDesc.getStatus())) {
            LOGGER.error("job result is failed, operType is {}", operType);
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segmentId, segmentType, operType);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, rsp.getStatus(), rspDesc.getStatusDescription());
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
        } else {
            // do nothing
        }
        LOGGER.info("query ns status -> end");
          
        return rsp;
    }
    
    

    class QueryProgress implements Callable<RestfulResponse> {

        String segmentType;
        String instanceId;
        String jobId;
        String domainHost;
        QueryProgress(String segmentType, String instanceId, String jobId, String domianHost) {
            this.segmentType = segmentType;
            this.instanceId = instanceId;
            this.jobId = jobId;
            this.domainHost = domianHost;
        }

        @Override
        public RestfulResponse call() throws Exception {
            // For every 10 seconds query progress
            Thread.sleep(TEN_SECONDS);
            //Step1: prepare url and method type
            String url;
            if(CommonConstant.SegmentType.GSO.equals(segmentType)) {
                url = String.format(CommonConstant.GSO_QUERY_URL, instanceId, jobId);
            } else {
                url = getUrl(segmentType, jobId, CommonConstant.Step.QUERY);
            }
            String methodType = CommonConstant.MethodType.GET;
            //Step2: prepare restful parameters and options
            RestfulParametes restfulParametes = RestfulUtil.setRestfulParameters(null, null);
            RestfulOptions options = RestfulUtil.setRestfulOptions(domainHost);
            RestfulResponse rsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParametes, options);
            LOGGER.info("query ns progress response status is : {}", rsp.getStatus());
            LOGGER.info("query ns progress response content is : {}", rsp.getResponseContent());
            return rsp;
        }

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
    private ServiceSegmentModel buildSegmentInfo(SegmentInputParameter currentInput, String segmentId, String segmentType,
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
     * @param segInput input parameters for current node from http request
     * @param segmentType GSO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse createGsoNs(SegmentInputParameter segInput, String segmentType) {
        // Step1: get service template id and csar id by node type
        ServiceTemplate svcTmpl = catalogProxy.getSvcTmplByNodeType(segInput.getNodeType(), segInput.getDomainHost());
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }
        LOGGER.info("create gso ns -> begin");
        
        // Step2: prepare url and method type
        String url = CommonConstant.GSO_CREATE_URL;
        String methodType = CommonConstant.MethodType.POST;
        
        // Step3: prepare restful parameters and options
        String createGsoReq = getCreateGsoParams(segInput, svcTmpl);
        RestfulParametes restfulParametes = RestfulUtil.setRestfulParameters(createGsoReq, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        RestfulResponse createGsoRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParametes, options);
        LOGGER.info("create gso ns response status is : {}", createGsoRsp.getStatus());
        LOGGER.info("create gso ns response content is : {}", createGsoRsp.getResponseContent());

        JSONObject obj = JSONObject.fromObject(createGsoRsp.getResponseContent());
        String service = obj.getString(Constant.SERVICE_INDENTIFY);
        JSONObject jsonSvc = JSONObject.fromObject(service);
        String subServiceId = jsonSvc.getString(Constant.SERVICE_INSTANCE_ID);
        String opertionId = jsonSvc.getString(Constant.SERVICE_OPERATION_ID);
        if(StringUtils.isEmpty(service) || StringUtils.isEmpty(subServiceId) || StringUtils.isEmpty(opertionId)) {
            LOGGER.error("Invalid response from create operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_CREATE_OPERATION);
        }
        
        if(!HttpCode.isSucess(createGsoRsp.getStatus())){
            LOGGER.error("update segment operation status : fail to create gso ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(subServiceId, segmentType, CommonConstant.OperationType.CREATE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, createGsoRsp.getStatus(), CommonConstant.StatusDesc.CREATE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_CREATE_GSO_NS);
        }
        LOGGER.info("create gso ns -> end");
        LOGGER.info("save segment and operaton info -> begin");
        //Step 4: save segment information 
        ServiceSegmentModel segmentInfo = buildSegmentInfo(segInput, subServiceId, segmentType, svcTmpl.getServiceTemplateId());
        serviceSegmentDao.insertSegment(segmentInfo);
        //Step 5: and segment operation information
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(subServiceId, segmentType, CommonConstant.OperationType.CREATE, segInput.getServiceId(), CommonConstant.Status.PROCESSING);
        segmentOperInfo.setJobId(opertionId);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save segment and operation info -> end");
        
        return createGsoRsp;
    }

    /**
     * get input parameters map for create gso service<br>
     * 
     * @param cInput input parameters for current node
     * @param svcTmpl service tempalte
     * @return body for create gso service
     * @since  GSO 0.5
     */
    private String getCreateGsoParams(SegmentInputParameter cInput, ServiceTemplate svcTmpl) {
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
        
        return JsonUtil.marshal(req);
    }

    /**
     * delete gso service<br>
     * 
     * @param segInput input parameters for current node from http request
     * @param segmentType GSO
     * @return response
     * @since   GSO 0.5
     */
    @Override
    public RestfulResponse deleteGsoNs(SegmentInputParameter segInput, String segmentType) {
        LOGGER.info("delete gso ns -> begin");
        //Step1: prepare url and method type
        String url = String.format(CommonConstant.GSO_DELETE_URL, segInput.getSubServiceId());
        String methodType = CommonConstant.MethodType.DELETE;
        
        //Step2: prepare restful parameters and options
        RestfulParametes restfulParameters = RestfulUtil.setRestfulParameters(null, null);
        RestfulOptions options = RestfulUtil.setRestfulOptions(segInput.getDomainHost());
        
        RestfulResponse delGsoRsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParameters, options);
        LOGGER.info("delete gso ns response status is : {}", delGsoRsp.getStatus());
        LOGGER.info("delete gso ns response content is : {}", delGsoRsp.getResponseContent());
        if(!HttpCode.isSucess(delGsoRsp.getStatus())){
            LOGGER.error("update segment operation status : fail to delete gso ns");
            ServiceSegmentOperation statusSegOper = new ServiceSegmentOperation(segInput.getSubServiceId(), segmentType, CommonConstant.OperationType.DELETE);
            updateSegmentOperStatus(statusSegOper, CommonConstant.Status.ERROR, delGsoRsp.getStatus(), CommonConstant.StatusDesc.DELETE_NS_FAILED);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_DELETE_GSO_NS);
        }
        JSONObject obj = JSONObject.fromObject(delGsoRsp.getResponseContent());
        String opertionId = obj.getString(Constant.SERVICE_OPERATION_ID);
        if(StringUtils.isEmpty(opertionId)) {
            LOGGER.error("Invalid response from delete operation");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_RESPONSEE_FROM_DELETE_OPERATION);
        }
        LOGGER.info("delete gso ns -> end");
        LOGGER.info("save operaton info -> begin");
        // Step 3 save operation info
        ServiceSegmentOperation segmentOperInfo = new ServiceSegmentOperation(segInput.getSubServiceId(), segmentType, CommonConstant.OperationType.DELETE, segInput.getServiceId(), CommonConstant.Status.PROCESSING);
        segmentOperInfo.setJobId(opertionId);
        serviceSegmentDao.insertSegmentOper(segmentOperInfo);
        LOGGER.info("save operation info -> end");
        
        return delGsoRsp;
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
        QueryProgress task = new QueryProgress(segmentType, segmentId, jobId, svcSegment.getDomainHost());
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
            if(CommonConstant.OperationType.DELETE.equals(operType)){
                //delete gso segment when query result is ok
                LOGGER.info("delete gso segment");
                serviceSegmentDao.delete(svcSegment);                
            }
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
        
        NsProgressStatus nsProgress = new NsProgressStatus();
        nsProgress.setJobId(jobId);
        ResponseDescriptor rspDesc = new ResponseDescriptor();
        rspDesc.setStatus(svcOper.getResult());
        rspDesc.setProgress(String.valueOf(svcOper.getProgress()));
        nsProgress.setResponseDescriptor(rspDesc);
        String responseString = JsonUtil.marshal(nsProgress);
        RestfulResponse jobRsp = new RestfulResponse();
        jobRsp.setStatus(rsp.getStatus());
        jobRsp.setResponseJson(responseString);
        return jobRsp;

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
        String originalUrl;

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

}
