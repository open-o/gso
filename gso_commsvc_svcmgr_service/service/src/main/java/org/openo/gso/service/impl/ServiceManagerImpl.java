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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.constant.Constant;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.ErrorCode;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceParameter;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.restproxy.inf.IWsoProxy;
import org.openo.gso.service.inf.IServiceManager;
import org.openo.gso.synchronization.PackageOperationSingleton;
import org.openo.gso.util.convertor.DataConverter;
import org.openo.gso.util.json.JsonUtil;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Service management class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServiceManagerImpl implements IServiceManager {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManagerImpl.class);

    /**
     * DAO to operate service instance.
     */
    private IServiceModelDao serviceModelDao;

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
     * Proxy of interface with WSO2.
     */
    private IWsoProxy wsoProxy;

    /**
     * Create service instance.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public ServiceModel createService(String reqContent, HttpServletRequest httpRequest) throws ServiceException {

        // Parse request
        Map<String, Object> requestBody = JsonUtil.unMarshal(reqContent, Map.class);
        Map<String, Object> service = (Map<String, Object>)requestBody.get(Constant.SERVICE_INDENTIFY);
        ValidateUtil.assertObjectNotNull(service);

        // Validate data
        String templateId = (String)service.get(Constant.SERVICE_TEMPLATE_ID);
        ValidateUtil.assertStringNotNull(templateId);
        List<CatalogParameterModel> defineParams = catalogProxy.getParamsByTemplateId(templateId, httpRequest);
        Object instanceParam = service.get(Constant.SERVICE_PARAMETERS);
        ValidateUtil.validate(defineParams, instanceParam);

        // Convert service data
        Map<String, Object> paramsMap = (Map<String, Object>)instanceParam;
        ServiceModel model = convertData(service);
        List<ServiceParameter> paramList = convertParam(model.getServiceId(), paramsMap);
        model.setParameters(paramList);

        // Cache csar ID. When operating csar, need to check csar status.
        String csarId = (String)service.get(Constant.SERVICE_DEF_ID);
        ValidateUtil.assertStringNotNull(csarId);
        PackageOperationSingleton.getInstance().addBeingUsedCsarIds(csarId);

        try {
            // Insert data into DB
            insertDB(model);

            // Start to create workflow
            paramsMap.put(Constant.PREDEFINE_GSO_ID, model.getServiceId());
            startWorkFlow(templateId, Constant.WORK_FLOW_PLAN_CREATE, httpRequest, paramsMap);
        } catch(ServiceException e) {
            throw e;
        } finally {
            // Delete csar ID from cache
            PackageOperationSingleton.getInstance().removeBeingUsedCsarId(csarId);
        }

        return model;
    }

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public void deleteService(String serviceId, HttpServletRequest httpRequest) throws ServiceException {
        List<ServiceSegmentModel> serviceSegments = serviceSegmentDao.queryServiceSegments(serviceId);
        if(!CollectionUtils.isEmpty(serviceSegments)) {

            // Get template id of service instance
            ServicePackageMapping servicePackage = servicePackageDao.queryPackageMapping(serviceId);
            String templateId = servicePackage.getTemplateId();
            ValidateUtil.assertStringNotNull(templateId);

            // Fill in input parameters
            Map<String, String> inputParam = new HashMap<String, String>();
            inputParam.put(Constant.PREDEFINE_GSO_ID, serviceId);
            addServiceSegment(inputParam, serviceSegments);

            // Start delete workflow
            startWorkFlow(templateId, Constant.WORK_FLOW_PLAN_DELETE, httpRequest, inputParam);

        } else {
            LOGGER.error("There is no service segment. The service ID is {}", serviceId);
        }

        // Delete data from database
        serviceModelDao.delete(serviceId);
    }

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> getAllInstances() throws ServiceException {
        return this.serviceModelDao.queryAllServices();
    }

    /**
     * Query all service segment instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return service segment instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public List<ServiceSegmentModel> getServiceSegments(String serviceId) throws ServiceException {
        return serviceSegmentDao.queryServiceSegments(serviceId);
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
     * @return Returns the wsoProxy.
     */
    public IWsoProxy getWsoProxy() {
        return wsoProxy;
    }

    /**
     * @param wsoProxy The wsoProxy to set.
     */
    public void setWsoProxy(IWsoProxy wsoProxy) {
        this.wsoProxy = wsoProxy;
    }

    /**
     * Insert data into database.<br/>
     * 
     * @since GSO 0.5
     */
    private void insertDB(ServiceModel model) throws ServiceException {
        serviceModelDao.insert(model);
    }

    /**
     * Convert data model.<br/>
     * 
     * @param request request content
     * @return service instance object
     * @since GSO 0.5
     */
    private ServiceModel convertData(Map<String, Object> request) {
        ServiceModel model = DataConverter.convertServiceModel(request);
        DataConverter.addDynamicData(model);

        return model;
    }

    /**
     * Get operation content by operation name.<br/>
     * 
     * @param templateId ID of template
     * @param key operation name
     * @param request http request
     * @return operation object
     * @throws ServiceException when operations is invalid.
     * @since GSO 0.5
     */
    private OperationModel getOperation(String templateId, String key, HttpServletRequest request)
            throws ServiceException {
        List<OperationModel> operations = catalogProxy.getOperationsByTemplateId(templateId, request);
        if(CollectionUtils.isEmpty(operations)) {
            LOGGER.error("There is no execution plan.");
            throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "There is no execution plan.");
        }

        for(OperationModel keyOperation : operations) {
            if(keyOperation.getName().equals(key)) {
                return keyOperation;
            }
        }

        return null;
    }

    /**
     * Start work flow.<br/>
     * 
     * @param templateId template ID.
     * @param key name of work flow
     * @param request http request
     * @param parameters request parameters
     * @throws ServiceException when fail to execute.
     * @since GSO 0.5
     */
    private void startWorkFlow(String templateId, String key, HttpServletRequest request, Object parameters)
            throws ServiceException {
        OperationModel operation = getOperation(templateId, key, request);
        ValidateUtil.assertObjectNotNull(operation);
        Map<String, Object> wsoBody = DataConverter.constructWsoBody(operation, parameters);
        wsoProxy.startWorkFlow(wsoBody, request);
    }

    /**
     * Convert service parameter from map to list<ServiceParameter>.<br/>
     * 
     * @param serviceId service instance ID
     * @param params service parameter
     * @return parameters in the form of list
     * @throws ServiceException when type of inputParams are wrong
     * @since GSO 0.5
     */
    private List<ServiceParameter> convertParam(String serviceId, Map<String, Object> params) throws ServiceException {
        List<ServiceParameter> paramsList = new LinkedList<ServiceParameter>();
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            ServiceParameter param = new ServiceParameter();
            param.setServiceId(serviceId);
            param.setParamName(entry.getKey());
            param.setParamValue(JsonUtil.marshal(entry.getValue()));
            paramsList.add(param);
        }

        return paramsList;
    }

    /**
     * Create service segment.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @throws ServiceException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public void createServiceSegment(String reqContent, HttpServletRequest httpRequest) throws ServiceException {
        ServiceSegmentModel serviceSegment = JsonUtil.unMarshal(reqContent, ServiceSegmentModel.class);
        String serviceId = serviceSegment.getServiceId();
        ValidateUtil.assertStringNotNull(serviceId);

        // Query service instance
        ServicePackageMapping pacakageInfo = servicePackageDao.queryPackageMapping(serviceId);
        if(null == pacakageInfo) {
            LOGGER.error("There is no package in DB. The service is ", serviceId);
            throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "There is no package in DB.");
        }

        // Query nodes of template
        List<NodeTemplateModel> nodes = catalogProxy.getNodeTemplate(pacakageInfo.getTemplateId(), httpRequest);
        int sequence = getSequenceOfNode(nodes, serviceSegment);
        serviceSegment.setTopoSeqNumber(sequence);

        // insert database
        serviceSegmentDao.insert(serviceSegment);
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
            throws ServiceException {

        // Check data
        if(CollectionUtils.isEmpty(nodes)) {
            LOGGER.error("There is no nodes in template. The type of node is ", seviceSegment.getNodeType());
            throw new ServiceException(ErrorCode.SVCMGR_SERVICEMGR_BAD_PARAM, "Fail to get node from catalog.");
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
        if((null == nodeSequence) || !StringUtils.hasLength(nodeName)) {
            LOGGER.error("There is no sequence for node. The service instance is {}", seviceSegment.getServiceId());
            return 0;
        }

        return nodeSequence.indexOf(nodeName) + 1;
    }

    /**
     * Add service segments into wso2 input parameters.<br/>
     * 
     * @param inputParam wso2's input paramters
     * @param segments service segments
     * @since GSO 0.5
     */
    private void addServiceSegment(Map<String, String> inputParam, List<ServiceSegmentModel> segments) {
        String segmentId = null;
        for(ServiceSegmentModel segment : segments) {
            segmentId = segment.getNodeType() + Constant.SERVICE_SEGMENT_INSTANCE_ID;
            inputParam.put(segmentId, segment.getServiceSegmentId());
        }
    }
}