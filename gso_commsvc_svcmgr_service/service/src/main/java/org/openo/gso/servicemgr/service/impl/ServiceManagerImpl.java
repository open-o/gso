/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.gso.servicemgr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.constant.Constant;
import org.openo.gso.servicemgr.dao.inf.IServiceModelDao;
import org.openo.gso.servicemgr.dao.inf.IServicePackageDao;
import org.openo.gso.servicemgr.dao.inf.ISubServiceDao;
import org.openo.gso.servicemgr.exception.ErrorCode;
import org.openo.gso.servicemgr.model.catalogmo.OperationModel;
import org.openo.gso.servicemgr.model.catalogmo.ParameterModel;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;
import org.openo.gso.servicemgr.model.servicemo.SubServiceModel;
import org.openo.gso.servicemgr.restproxy.inf.ICatalogProxy;
import org.openo.gso.servicemgr.restproxy.inf.IWsoProxy;
import org.openo.gso.servicemgr.service.inf.IServiceManager;
import org.openo.gso.servicemgr.synchronization.PackageOperationSingleton;
import org.openo.gso.servicemgr.util.convertor.DataConverter;
import org.openo.gso.servicemgr.util.json.JsonUtil;
import org.openo.gso.servicemgr.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
     * DAO to operate sub-service instance.
     */
    private ISubServiceDao subServiceDao;

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
        ServiceModel model = null;
        Map<String, Object> requestBody = JsonUtil.unMarshal(reqContent, Map.class);
        Map<String, Object> service = (Map<String, Object>)requestBody.get(Constant.SERVICE_INDENTIFY);
        ValidateUtil.assertObjectNotNull(service);

        // 1. Validate data
        String templateId = (String)service.get(Constant.SERVICE_TEMPLATE_ID);
        ValidateUtil.assertStringNotNull(templateId);
        List<ParameterModel> defineParams = catalogProxy.getParamsByTemplateId(templateId, httpRequest);
        Object instanceParam = service.get(Constant.SERVICE_PARAMETERS);
        ValidateUtil.validate(defineParams, instanceParam);

        // 2. Cache csar ID. When operating csar, need to check csar status.
        String csarId = (String)service.get(Constant.SERVICE_DEF_ID);
        ValidateUtil.assertStringNotNull(csarId);
        PackageOperationSingleton.getInstance().addBeingUsedCsarIds(csarId);

        try {
            // 3. Insert data into DB
            model = convertData(service);
            insertDB(model);

            // 4. start create workflow
            startWorkFlow(templateId, Constant.WORK_FLOW_PLAN_CREATE, httpRequest, instanceParam);
        } catch(ServiceException e) {
            throw e;
        } finally {
            // 5. Delete csar ID from cache
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

        // 1. Get csar ID of service instance
        ServicePackageMapping servicePackage = servicePackageDao.queryPackageMapping(serviceId);
        String csarId = servicePackage.getServiceDefId();
        ValidateUtil.assertStringNotNull(csarId);

        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put(Constant.PREDEFINE_GSO_ID, serviceId);

        // 2. Start delete workflow
        startWorkFlow(csarId, Constant.WORK_FLOW_PLAN_DELETE, httpRequest, inputParam);

        // 3. Delete data from database
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
     * Query all sub-service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @return sub-service instances
     * @throws ServiceException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public List<SubServiceModel> getSubServices(String serviceId) throws ServiceException {
        return subServiceDao.querySubServices(serviceId);
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
     * @return Returns the subServiceDao.
     */
    public ISubServiceDao getSubServiceDao() {
        return subServiceDao;
    }

    /**
     * @param subServiceDao The subServiceDao to set.
     */
    public void setSubServiceDao(ISubServiceDao subServiceDao) {
        this.subServiceDao = subServiceDao;
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
        servicePackageDao.insert(model.getServicePackage());
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
}
