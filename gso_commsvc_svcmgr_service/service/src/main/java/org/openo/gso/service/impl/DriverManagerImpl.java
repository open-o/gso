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
import org.openo.baseservice.remoteservice.exception.ServiceException;
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
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.drivermo.DomainInputParameter;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.ServiceNode;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.service.inf.IDriverManager;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.json.JsonUtil;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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

    

    

    @Override
    public RestfulResponse deleteNs(String nsInstanceId, String domain) throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }
    
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
            updateSegmentOperStatus(nsInstanceId, domain, rsp.getStatus());
            LOGGER.error("fail to instantiate ns");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.FAIL_TO_INSTANTIATE_NS);
        }
        updateSegmentOperJobId(nsInstanceId, domain, jobId);
        LOGGER.info("update segment info -> end");
        
        return rsp;
    }

    

    

    @Override
    public RestfulResponse terminateNs(String nsInstanceId, HttpServletRequest httpRequest, String domain)
            throws ApplicationException {
//        String body = RestUtils.getRequestBody(httpRequest);
//        LOGGER.warn("terminate request body is {}", body);
//        String jsonBody = body.replaceAll("\"\\{", "\\{").replaceAll("\\}\"", "\\}");
//        
//        LOGGER.warn("terminate json body is {}", jsonBody);
//        
//        // transfer the input into input parameters model
//        ServiceNode inputs = null;
//        inputs = JsonUtil.unMarshal(jsonBody, ServiceNode.class);
//
//        // get nodeType from the request body
//        String nodeType = inputs.getNodeType();
//
//        LOGGER.info("nodeType is {}", nodeType);
//        // get instaceId & serviceId value from the map
//        Map<String, String> instIdMap = inputs.getInputParameters();
//
//        String serviceId = instIdMap.get("serviceId");
//        StringBuilder builder = new StringBuilder(nodeType);
//        builder.append(".instanceId");
//        String instKey = builder.toString();
//        String instanceId = instIdMap.get(instKey);
//        LOGGER.info("id of instance to be deleted is {}", instanceId);
//
//        // invoke the SDNO or NFVO to delete the instance
//        LOGGER.info("start to delete the service instance");
//        String status = "fail";
//        try {
//            status = serviceInf.deleteNs(instanceId);
//        } catch(Exception e) {
//            LOGGER.error("fail to delete the sub-service", e);
//            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
//        }
//
//        LOGGER.info("end to delete the service instance");
//        
//
//        RestfulResponse rsp = new RestfulResponse();
//        if("success".equals(status)) {
//            // save the segment information into the database
//            ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
//            serviceSegment.setServiceId(serviceId);
//            serviceSegment.setServiceSegmentId(instanceId);
//
//            serviceSegmentDao.delete(serviceSegment);
//            LOGGER.warn("succeed to delete the servcie segment from t_lcm_service_segment");
//        }else{
//            rsp.setStatus(HttpCode.INTERNAL_SERVER_ERROR);
//        }
//        return rsp;
        return null;

    }

    @Override
    public RestfulResponse getNsProgress(String jobId, String domain) throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }
    
    //TODO make sure node sequence implementation
    /**
     * <br/>
     * 
     * @param nodes
     * @param seviceSegment
     * @return
     * @throws ServiceException
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private int getSequenceOfNode(List<NodeTemplateModel> nodes, ServiceSegmentModel seviceSegment)
            throws ApplicationException {

        // Check data
        if(CollectionUtils.isEmpty(nodes)) {
            LOGGER.error("There is no nodes in template. The type of node is ", seviceSegment.getNodeType());
            throw new ApplicationException(HttpCode.BAD_REQUEST, "Fail to get node from catalog.");
        }
        String type = seviceSegment.getNodeType();
        ValidateUtil.assertStringNotNull(type);

        // visit node
        String nodeName = null;
        List<String> nodeSequence = null;
        for(NodeTemplateModel node : nodes) {

            // get node name
            if(node.getType().equals(seviceSegment.getNodeType())) {
                nodeName = node.getName();
            }

            // get node sequence
            if(node.getName().equals(Constant.NODE_SEQUENCE)) {
                Map<String, Object> properties = node.getProperties();
                if(!CollectionUtils.isEmpty(properties)) {
                    Object sequence = properties.get(Constant.SEQUENCE_PROPERTY);
                    if(sequence instanceof List) {
                        nodeSequence = (List<String>)sequence;
                        break;
                    }
                } else {
                    LOGGER.error("There is no sequence for node. The service instance is {}",
                            seviceSegment.getServiceId());
                }
            }
        }

        // validate
        if((null == nodeSequence) || StringUtils.isEmpty(nodeName)) {
            LOGGER.error("There is no sequence for node. The service instance is {}", seviceSegment.getServiceId());
            return 0;
        }

        return nodeSequence.indexOf(nodeName) + 1;
    }

    private RestfulResponse createNetworkSubService(ServiceNode serviceNode, String templateId,
            HttpServletRequest httpRequest) throws ApplicationException {

//        LOGGER.warn("create ns : begin");
//        // Step 2:Make a list of parameters for the node Type
//        Map<String, String> createParamMap = getCreateParamsByNodeType(serviceNode);
//        
//        // Step 1: Create Network service
//        String nsInstanceId = serviceInf.createNS(templateId, createParamMap);
//        LOGGER.warn("nsInstanceId is {}", nsInstanceId);
//        if(StringUtils.isEmpty(nsInstanceId)) {
//            LOGGER.error("Invalid instanceId from create");
//            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
//                    DriverExceptionID.INVALID_VALUE_FROM_CREATE);
//        }
//        
//        LOGGER.warn("create ns : end");
//        
//        LOGGER.warn("instantiate ns : begin");
//        Map<String, String> instParamMap = getInstParamsByNodeType(serviceNode);
//
//        // Step 2: Instantiate Network service
//        String jobId = serviceInf.instantiateNS(nsInstanceId, instParamMap);
//        if(StringUtils.isEmpty(jobId)) {
//            LOGGER.error("Invalid jobId from instantiate");
//            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
//                    DriverExceptionID.INVALID_VALUE_FROM_INSTANTIATE);
//        }
//        LOGGER.warn("instantiate ns : end");
//
//        LOGGER.warn("query job : begin");
//        // Step 3: Wait for Job to complete
//        String status = "success";
//        try {
//            waitForJobToComplete(jobId);
//        } catch(ApplicationException e) {
//            LOGGER.error("fail to complete the job", e);
//            status = "fail";
//        }
//        LOGGER.warn("query job : end");
//
//        String serviceId = serviceNode.getInputParameters().get("serviceId");
//        LOGGER.warn("serviceId is {}", serviceId);
//        String serviceName = serviceNode.getInputParameters().get("serviceName");
//        LOGGER.warn("serviceName is {}", serviceName);
//        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
//        
//        serviceSegment.setServiceId(serviceId);
//        serviceSegment.setServiceSegmentName(serviceName);
//        serviceSegment.setServiceSegmentId(nsInstanceId);
//        serviceSegment.setNodeType(serviceNode.getNodeType());
//        serviceSegment.setStatus(status);
//        serviceSegment.setTemplateId(templateId);
//
//        ServicePackageMapping pacakageInfo = servicePackageDao.queryPackageMapping(serviceId);
//        if(null == pacakageInfo) {
//            LOGGER.error("There is no package in DB. The service Id is {}", serviceId);
//            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "There is no package in DB.");
//        }
//
//        // Query nodes of template
//        List<NodeTemplateModel> nodes = catalogProxy.getNodeTemplate(pacakageInfo.getTemplateId(), httpRequest);
//        int sequence = getSequenceOfNode(nodes, serviceSegment);
//        serviceSegment.setTopoSeqNumber(sequence);

        

        // Step 4: return the response
        RestfulResponse rsp = new RestfulResponse();
//        if("success".equals(status)) {
//            LOGGER.warn("store segment : begin");
//            // insert database
//            serviceSegmentDao.insert(serviceSegment);
//            
//            LOGGER.warn("store segment : end");
//            if(CommonConstant.NodeType.SDN_UNDERLAYVPN_TYPE.equals(serviceNode.getNodeType())
//                    || CommonConstant.NodeType.SDN_OVERLAYVPN_TYPE.equals(serviceNode.getNodeType())){
//                serviceModelDao.updateServiceResult(serviceId, "success");
//            }
//            
//        }else{
//            rsp.setStatus(HttpCode.INTERNAL_SERVER_ERROR);
//            LOGGER.error("fail to store the sub-service to LCM");
//            serviceModelDao.updateServiceResult(serviceId, "fail");
//        }
        return rsp;
    }


    private void waitForJobToComplete(String jobId) throws ApplicationException {

        ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        boolean queryFlag = true;

        while(queryFlag) {

            QueryProgress task = new QueryProgress(jobId);
            Future<NsProgressStatus> status = executor.submit(task);

            NsProgressStatus progress = null;
            try {
                progress = status.get();
            } catch(Exception e) {

                queryFlag = false;
                LOGGER.error("error in the result when query the operation status", e);
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
            }

            if("100".equals(progress.getResponseDescriptor().getProgress())
                    && "finished".equals(progress.getResponseDescriptor().getStatus())) {
                LOGGER.info("Success to create the sub-service");
                queryFlag = false;
            } else if("error".equals(progress.getResponseDescriptor().getStatus())) {
                LOGGER.error("Failed to create the sub service");
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INTERNAL_ERROR);
            } else {
                // do nothing
            }
        }

    }

    class QueryProgress implements Callable<NsProgressStatus> {

        String jobId;

        QueryProgress(String jobInfo) {
            jobId = jobInfo;
        }

        @Override
        public NsProgressStatus call() throws Exception {

            // For every 10 seconds query progress
            Thread.sleep(10000);
            return serviceInf.getNsProgress(jobId);
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
     * private method5 : get create params<br>
     * 
     * @param 
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
     * @param nsInstanceId instance id
     * @param domain nfvo or sdno
     * @param status status: processing, finished, error
     * @since  GSO 0.5
     */
    private void updateSegmentOperStatus(String nsInstanceId, String domain, int statusCode) {
        ServiceSegmentOperation segmentOper = new ServiceSegmentOperation();
        segmentOper.setServiceSegmentId(nsInstanceId);
        segmentOper.setServiceSegmentType(domain);
        segmentOper.setStatus(CommonConstant.Status.ERROR);
        segmentOper.setErrorCode(statusCode);
        serviceSegmentDao.updateSegmentOperStatus(segmentOper);
        
    }
    
    /**
     * private method 7: update segment operation job id<br>
     * 
     * @param nsInstanceId instance id
     * @param domain nfvo or sdno
     * @param jobId job id
     * @since  GSO 0.5
     */
    private void updateSegmentOperJobId(String nsInstanceId, String domain, String jobId) {
        ServiceSegmentOperation segmentOper = new ServiceSegmentOperation();
        segmentOper.setServiceSegmentId(nsInstanceId);
        segmentOper.setServiceSegmentType(domain);
        segmentOper.setJobId(jobId);
        serviceSegmentDao.updateSegmentOperJobId(segmentOper);
    }
    
    

}
