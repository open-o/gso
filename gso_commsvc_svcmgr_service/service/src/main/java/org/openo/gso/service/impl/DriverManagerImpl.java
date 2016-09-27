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

import java.util.ArrayList;
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
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.drivermo.NsProgressStatus;
import org.openo.gso.model.drivermo.ServiceNode;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.model.drivermo.TerminateParams;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
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

    private static String CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL = "/openoapi/catalog/v1/servicetemplate/nesting";

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
     * Create service instance.<br/>
     * 
     * @param serviceModel service instance
     * @param httpRequest http request
     * @throws ApplicationException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse terminateService(HttpServletRequest httpRequest) throws ApplicationException {
        String body = RestUtils.getRequestBody(httpRequest);

        // transfer the input into input parameters model
        TerminateParams inputs = null;
        inputs = JsonUtil.unMarshal(body, TerminateParams.class);

        // get nodeType from the request body
        String nodeType = inputs.getNodeType();

        // get instaceId & serviceId value from the map
        Map<String, String> instIdMap = inputs.getInputParameters();

        String serviceId = instIdMap.get("serviceId");
        StringBuilder builder = new StringBuilder(nodeType);
        builder.append(".instanceId");
        String instKey = builder.toString();
        String instanceId = instIdMap.get(instKey);

        // invoke the SDNO or NFVO to delete the instance
        String status = "fail";
        try {
            status = serviceInf.delete(nodeType, instanceId);
        } catch(Exception e) {
            LOGGER.error("fail to delete the sub-service", e);
        }

        // save the segment information into the database
        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setServiceId(serviceId);
        serviceSegment.setServiceSegmentId(instanceId);

        serviceSegmentDao.delete(serviceSegment);

        RestfulResponse rsp = new RestfulResponse();
        if("fail".equals(status)) {
            rsp.setStatus(HttpCode.INTERNAL_SERVER_ERROR);
            LOGGER.error("fail to store the sub-service to LCM");
        }
        return rsp;

    }

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

    /**
     * Instantiate service instance.<br/>
     * 
     * @param serviceNode service instance
     * @param httpRequest http request
     * @throws ApplicationException when parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public RestfulResponse instantiateService(HttpServletRequest httpRequest) throws ApplicationException {

        String body = RestUtils.getRequestBody(httpRequest);

        // Step 0: Transfer the input into input parameters model
        ServiceNode serviceNode = null;
        serviceNode = JsonUtil.unMarshal(body, ServiceNode.class);

        // Step 1:Validate input parameters
        String nodeType = serviceNode.getNodeType();

        if((null == nodeType) || (null == serviceNode.getInputParameters())) {
            LOGGER.error("Input parameters from lcm/workflow are empty");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }

        if(null == serviceInf) {
            LOGGER.error("Service interface not initialised");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, DriverExceptionID.INVALID_PARAM);
        }
        serviceInf.setNodeType(serviceNode.getNodeType());

        // Step 3: Call the Catalogue service to get service template id
        ServiceTemplate svcTmpl = getSvcTmplByNodeType(serviceNode);
        if(null == svcTmpl) {
            LOGGER.error("Failed to get service template from catalogue module");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.FAILED_TO_SVCTMPL_CATALOGUE);
        }

        return createNetworkSubService(serviceNode, svcTmpl.getServiceTemplateId(), httpRequest);
    }

    private RestfulResponse createNetworkSubService(ServiceNode serviceNode, String templateId,
            HttpServletRequest httpRequest) throws ApplicationException {

        // Step 2:Make a list of parameters for the node Type
        Map<String, String> mapParam = getParamsByNodeType(serviceNode);

        String serviceId = mapParam.get("serviceId");
        // Step 1: Create Network service
        String nsInstanceId = serviceInf.createNS(templateId, mapParam);
        if(StringUtils.isEmpty(nsInstanceId)) {
            LOGGER.error("Invalid instanceId from workflow");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.INVALID_VALUE_FROM_WORKFLOW);
        }

        // Step 2: Instantiate Network service
        String jobId = serviceInf.instantiateNS(nsInstanceId, mapParam);
        if(StringUtils.isEmpty(jobId)) {
            LOGGER.error("Invalid jobId from workflow");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    DriverExceptionID.INVALID_VALUE_FROM_WORKFLOW);
        }

        // Step 3: Wait for Job to complete
        String status = "success";
        try {
            waitForJobToComplete(jobId);
        } catch(ApplicationException e) {
            LOGGER.error("fail to complete the job", e);
            status = "fail";
        }

        ServiceSegmentModel serviceSegment = new ServiceSegmentModel();
        serviceSegment.setServiceId(serviceId);
        serviceSegment.setServiceSegmentId(nsInstanceId);
        serviceSegment.setNodeType(serviceNode.getNodeType());
        serviceSegment.setStatus(status);
        serviceSegment.setTemplateId(templateId);

        ServicePackageMapping pacakageInfo = servicePackageDao.queryPackageMapping(serviceId);
        if(null == pacakageInfo) {
            LOGGER.error("There is no package in DB. The service is ", serviceId);
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "There is no package in DB.");
        }

        // Query nodes of template
        List<NodeTemplateModel> nodes = catalogProxy.getNodeTemplate(pacakageInfo.getTemplateId(), httpRequest);
        int sequence = getSequenceOfNode(nodes, serviceSegment);
        serviceSegment.setTopoSeqNumber(sequence);

        // insert database
        serviceSegmentDao.insert(serviceSegment);

        // Step 4: return the response
        RestfulResponse rsp = new RestfulResponse();
        if("fail".equals(status)) {
            rsp.setStatus(HttpCode.INTERNAL_SERVER_ERROR);
            LOGGER.error("fail to store the sub-service to LCM");
        }
        return rsp;
    }

    private Map<String, String> getParamsByNodeType(ServiceNode serviceNode) {

        // Make a list of parameters for the node Type
        Map<String, String> mapParam = serviceNode.getInputParameters();

        return mapParam;
    }

    private ServiceTemplate getSvcTmplByNodeType(ServiceNode serviceNode) throws ApplicationException {

        Map<String, String> paramsMap = new HashMap<String, String>();

        // Step 1: Prepare url and method type
        paramsMap.put(CommonConstant.HttpContext.URL, CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);

        // Step 2: Prepare the query param
        List<String> lstNodeTypeIds = new ArrayList<String>();
        lstNodeTypeIds.add(serviceNode.getNodeType());

        String params = null;
        Map<String, String> mapParams = new HashMap<String, String>();
        params = JsonUtil.marshal(lstNodeTypeIds);
        mapParams.put("nodeTypeIds", params);

        // Step 3:Send the request and get response
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(paramsMap, params, mapParams);

        JSONArray array = JSONArray.fromObject(rsp.getResponseContent());
        return JsonUtil.unMarshal(array.getString(0), ServiceTemplate.class);

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

            if("100".equals(progress.getRspDescriptor().getProgress())
                    && "finished".equals(progress.getRspDescriptor().getStatus())) {
                LOGGER.info("Success to create the sub-service");
                queryFlag = false;
            } else if("error".equals(progress.getRspDescriptor().getStatus())) {
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

            // For every 5 seconds query progress
            Thread.sleep(5000);
            return serviceInf.getNsProgress(jobId);
        }

    }

}
