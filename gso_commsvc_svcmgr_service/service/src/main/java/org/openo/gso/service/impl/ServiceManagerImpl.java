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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.Constant;
import org.openo.gso.dao.inf.IInventoryDao;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServicePackageDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.mapper.InvServiceModelMapper;
import org.openo.gso.mapper.InvServicePackageMapper;
import org.openo.gso.mapper.InvServiceParameterMapper;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.servicemo.ServiceDetailModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceParameter;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.restproxy.inf.IWorkflowProxy;
import org.openo.gso.service.inf.IOperationManager;
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
     * Proxy of interface with workflow.
     */
    private IWorkflowProxy workflowProxy;

    /**
     * DAO to operate inventory database.
     */
    private IInventoryDao inventoryDao;

    /**
     * Service operation manager.
     */
    private IOperationManager operationManager;

    /**
     * @return Returns the operationManager.
     */
    public IOperationManager getOperationManager() {
        return operationManager;
    }

    /**
     * @param operationManager The operationManager to set.
     */
    public void setOperationManager(IOperationManager operationManager) {
        this.operationManager = operationManager;
    }

    /**
     * Create service instance.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public ServiceDetailModel createService(String reqContent, HttpServletRequest httpRequest) {

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

        // Convert service data for database operation
        Map<String, Object> paramsMap = (Map<String, Object>)instanceParam;
        ServiceModel model = convertData(service);
        model.setName((String)service.get(Constant.SERVICE_NAME));
        model.setDescription((String)service.get(Constant.SERVICE_DESCRIPTION));
        model.setParameter(convertParam(model.getServiceId(), paramsMap));

        // Cache csar ID. When operating csar, need to check csar status.
        String csarId = (String)service.get(Constant.SERVICE_DEF_ID);
        ValidateUtil.assertStringNotNull(csarId);
        PackageOperationSingleton.getInstance().addBeingUsedCsarIds(csarId);

        ServiceDetailModel svcDetail = new ServiceDetailModel();
        ServiceOperation svcOperation = null;
        try {
            // Insert data into DB
            insertDB(model);

            // Create operation record
            svcOperation = operationManager.createOperation(model.getServiceId(), Constant.OPERATION_CREATE);

            // Start to create workflow
            startWorkFlow(templateId, Constant.WORK_FLOW_PLAN_CREATE, httpRequest,
                    DataConverter.getWorkFlowParams(paramsMap, model));
        } catch(ApplicationException exception) {
            LOGGER.error("Fail to create service instance. {}", exception);
            // update service instance status
            setStatus(model, svcOperation, CommonConstant.Status.ERROR, exception.getMessage());
            updateData(model, svcOperation);
            throw exception;
        } finally {

            // Delete csar ID from cache
            PackageOperationSingleton.getInstance().removeBeingUsedCsarId(csarId);
        }

        // If there is no service segment, operation is finished when gso-lcm finishes.
        if(0 == model.getSegmentNumber()) {
            // update service instance status
            setStatus(model, svcOperation, CommonConstant.Status.FINISHED, null);
            updateData(model, svcOperation);
        }

        // Assemble return data
        svcDetail.setServiceModel(model);
        svcDetail.setServiceOperation(svcOperation);

        return svcDetail;
    }

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @since GSO 0.5
     */
    @Override
    public void deleteService(String serviceId, HttpServletRequest httpRequest) {
        // Firstly delete the old operation record of this service instance
        // If not delete old operation record, it will influence timing task
        operationManager.delete(serviceId);

        ServiceOperation svcOperation = null;
        try {
            // 1. Create operation record
            svcOperation = operationManager.createOperation(serviceId, Constant.OPERATION_DELETE);
            // 2. Get service segments for workflow
            List<ServiceSegmentModel> serviceSegments = serviceSegmentDao.queryServiceSegments(serviceId);
            if(!CollectionUtils.isEmpty(serviceSegments)) {

                // 2.1 Get template id of service instance
                ServicePackageMapping servicePackage = servicePackageDao.queryPackageMapping(serviceId);
                ValidateUtil.assertObjectNotNull(servicePackage);
                String templateId = servicePackage.getTemplateId();
                ValidateUtil.assertStringNotNull(templateId);

                // 2.2 Fill in input parameters
                List<Object> segLst = addServiceSegment(serviceSegments);
                Map<String, Object> inputParam = new HashMap<>();
                inputParam.put(Constant.SERVICE_SEGMENTS, segLst);

                // 2.3 Start delete workflow
                startWorkFlow(templateId, Constant.WORK_FLOW_PLAN_DELETE, httpRequest, inputParam);

            } else {
                LOGGER.error("There is no service segment. The service ID is {}", serviceId);
                // 3. Update service operation record
                setStatus(null, svcOperation, CommonConstant.Status.FINISHED, null);
                updateData(null, svcOperation);
                // 4. Directly delete service which has no service segments.
                deleteDataFromDb(serviceId);
            }
        } catch(ApplicationException exception) {
            LOGGER.error("Fail to delete service. {}", exception);
            setStatus(null, svcOperation, CommonConstant.Status.ERROR, exception.getMessage());
            updateData(null, svcOperation);
            throw exception;
        }
    }

    /**
     * Query all service instances.<br/>
     * 
     * @return service instances
     * @since GSO 0.5
     */
    @Override
    public List<ServiceModel> getAllInstances() {
        return this.serviceModelDao.queryAllServices();
    }

    /**
     * Query all service segment instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return service segment instances
     * @since GSO 0.5
     */
    @Override
    public List<ServiceSegmentModel> getServiceSegments(String serviceId) {
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
     * @return Returns the workflowProxy.
     */
    public IWorkflowProxy getWorkflowProxy() {
        return workflowProxy;
    }

    /**
     * @param workflowProxy The workflowProxy to set.
     */
    public void setWorkflowProxy(IWorkflowProxy workflowProxy) {
        this.workflowProxy = workflowProxy;
    }

    /**
     * Insert data into DB.<br/>
     * 
     * @param model service instance data
     * @since GSO 0.5
     */
    private void insertDB(ServiceModel model) {

        // insert gso data
        serviceModelDao.insert(model);

        // insert inventory data
        inventoryDao.insert(DataConverter.convertToInvData(model), InvServiceModelMapper.class);
        inventoryDao.insert(model.getServicePackage(), InvServicePackageMapper.class);
        inventoryDao.insert(model.getParameter(), InvServiceParameterMapper.class);
        ;
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
     * @return Returns the inventoryDao.
     */
    public IInventoryDao getInventoryDao() {
        return inventoryDao;
    }

    /**
     * @param inventoryDao The inventoryDao to set.
     */
    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Get operation content by operation name.<br/>
     * 
     * @param templateId ID of template
     * @param key operation name
     * @param request http request
     * @return operation object
     * @since GSO 0.5
     */
    private OperationModel getOperation(String templateId, String key, HttpServletRequest request) {
        List<OperationModel> operations = catalogProxy.getOperationsByTemplateId(templateId, request);
        if(CollectionUtils.isEmpty(operations)) {
            LOGGER.error("There is no execution plan.");
            throw new ApplicationException(HttpCode.RESPOND_CONFLICT, "There is no execution plan.");
        }

        for(OperationModel keyOperation : operations) {
            if(keyOperation.getName().endsWith(key)) {
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
     * @return response status code
     * @since GSO 0.5
     */
    private int startWorkFlow(String templateId, String key, HttpServletRequest request, Object parameters) {
        OperationModel operation = getOperation(templateId, key, request);
        LOGGER.info("Start workflow. Operations from catalog: {} ", operation);
        ValidateUtil.assertObjectNotNull(operation);
        Map<String, Object> workflowBody = DataConverter.constructWorkflowBody(operation, parameters);
        return workflowProxy.startWorkFlow(workflowBody, request);
    }

    /**
     * Convert service parameter from map to list<ServiceParameter>.<br/>
     * 
     * @param serviceId service instance ID
     * @param params service parameter
     * @return parameters in the form of list
     * @since GSO 0.5
     */
    private ServiceParameter convertParam(String serviceId, Map<String, Object> params) {
        ServiceParameter parameter = new ServiceParameter();
        parameter.setServiceId(serviceId);
        parameter.setParamName(Constant.SERVICE_PARAMETERS);
        parameter.setParamValue(JsonUtil.marshal(params));

        return parameter;
    }

    /**
     * Create service segment.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @since GSO 0.5
     */
    @Override
    public void createServiceSegment(String reqContent, HttpServletRequest httpRequest) {
        ServiceSegmentModel serviceSegment = JsonUtil.unMarshal(reqContent, ServiceSegmentModel.class);
        String serviceId = serviceSegment.getServiceId();
        ValidateUtil.assertStringNotNull(serviceId);

        // Query service instance
        ServicePackageMapping pacakageInfo = servicePackageDao.queryPackageMapping(serviceId);
        if(null == pacakageInfo) {
            LOGGER.error("There is no package in DB. The service is ", serviceId);
            throw new ApplicationException(HttpCode.BAD_REQUEST, "There is no package in DB.");
        }

        // Query nodes of template
        List<NodeTemplateModel> nodes = catalogProxy.getNodeTemplate(pacakageInfo.getTemplateId(), httpRequest);
        int sequence = getSequenceOfNode(nodes, serviceSegment);
        serviceSegment.setTopoSeqNumber(sequence);

        // insert database
        serviceSegmentDao.insertSegment(serviceSegment);
    }

    /**
     * Get the sequence of node in topology.<br/>
     * 
     * @param nodes which are defined in template
     * @param seviceSegment service segment of some service instance
     * @return sequence
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private int getSequenceOfNode(List<NodeTemplateModel> nodes, ServiceSegmentModel seviceSegment) {

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
        if((null == nodeSequence) || !StringUtils.hasLength(nodeName)) {
            LOGGER.error("There is no sequence for node. The service instance is {}", seviceSegment.getServiceId());
            return 0;
        }

        return nodeSequence.indexOf(nodeName) + 1;
    }

    /**
     * Add service segments into workflow input parameters.<br/>
     * 
     * @param segments service segments
     * @return collection of service segments for workflow's input parameters
     * @since GSO 0.5
     */
    private List<Object> addServiceSegment(List<ServiceSegmentModel> segments) {
        List<Object> segLst = new ArrayList<>();
        for(ServiceSegmentModel segment : segments) {
            Map<String, String> segMap = new HashMap<>();
            segMap.put(Constant.SERVICE_ID, segment.getServiceId());
            segMap.put(Constant.SERVICE_SEGMENT_DOMAINHOST, segment.getServiceSegmentId());
            segMap.put(Constant.NODE_TEMPLATE_NAME, segment.getNodeTemplateName());
            segMap.put(Constant.SERVICE_SEGMENT_DOMAINHOST, segment.getDomainHost());
            segLst.add(segMap);
        }

        return segLst;
    }

    /**
     * Delete data from database.<br/>
     * 
     * @param key delete key
     * @since GSO 0.5
     */
    private void deleteDataFromDb(String key) {

        // Delete data from gso DB
        serviceModelDao.delete(key);

        // Delete data from inventory DB
        inventoryDao.delete(key, InvServicePackageMapper.class);
        inventoryDao.delete(key, InvServiceParameterMapper.class);
        inventoryDao.delete(key, InvServiceModelMapper.class);
    }

    /**
     * Query service instance.<br/>
     * 
     * @param serviceId service instance ID
     * @return service instance
     * @since GSO 0.5
     */
    @Override
    public ServiceModel getInstanceByInstanceId(String serviceId) {
        return serviceModelDao.queryServiceByInstanceId(serviceId);
    }

    /**
     * Update service instance status.<br/>
     * 
     * @param svcModel service instance
     * @param operation service operation
     * @param status
     * @since GSO 0.5
     */
    private void updateData(ServiceModel svcModel, ServiceOperation operation) {
        if(null != svcModel) {
            serviceModelDao.updateServiceStatus(svcModel.getServiceId(), svcModel.getStatus());
            inventoryDao.updateServiceStatus(svcModel.getServiceId(), svcModel.getStatus());
        }

        if(null != operation) {
            operationManager.updateOperation(operation);
        }
    }

    /**
     * Set status of service and operation when progress is 100.<br/>
     * 
     * @param model service instance
     * @param svcOperation service operation
     * @param status service and operation status
     * @param reason why status is error
     * @since GSO 0.5
     */
    private void setStatus(ServiceModel model, ServiceOperation svcOperation, String status, String reason) {
        if(null != model) {
            model.setStatus(status);
        }

        if(null != svcOperation) {
            svcOperation.setResult(status);
            svcOperation.setProgress(100);
            svcOperation.setReason(reason);
            svcOperation.setFinishedAt(System.currentTimeMillis());
        }
    }

    /**
     * Query service operation by service instance ID and operation ID.<br/>
     * 
     * @param serviceId service instance ID
     * @param operationId service operation ID
     * @return service operation
     * @since GSO 0.5
     */
    @Override
    public ServiceOperation getServiceOperation(String serviceId, String operationId) {
        return operationManager.queryOperation(serviceId, operationId);
    }
}
