/*
 * Copyright (c) 2016-2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openo.baseservice.remoteservice.exception.ExceptionArgs;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.servicegateway.common.CommonUtil;
import org.openo.gso.servicegateway.constant.Constant;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.openo.gso.servicegateway.model.CreateParameterModel;
import org.openo.gso.servicegateway.model.CreateParameterRspModel;
import org.openo.gso.servicegateway.model.DomainModel;
import org.openo.gso.servicegateway.model.EnumServiceType;
import org.openo.gso.servicegateway.model.OperationModel;
import org.openo.gso.servicegateway.model.OperationResult;
import org.openo.gso.servicegateway.model.ParameterDefineModel;
import org.openo.gso.servicegateway.model.SegmentTemplateModel;
import org.openo.gso.servicegateway.model.ServiceModel;
import org.openo.gso.servicegateway.model.ServiceTemplateModel;
import org.openo.gso.servicegateway.service.inf.IServiceGateway;
import org.openo.gso.servicegateway.util.http.HttpUtil;
import org.openo.gso.servicegateway.util.http.ResponseUtils;
import org.openo.gso.servicegateway.util.json.JsonUtil;
import org.openo.gso.servicegateway.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ServiceGateway service class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */
public class ServiceGatewayImpl implements IServiceGateway {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGatewayImpl.class);

    /**
     * Create service instance.<br/>
     * 
     * @param reqContent content of request
     * @param httpRequest http request
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public OperationResult createService(String reqContent, HttpServletRequest httpRequest) {
        // check the value
        if(StringUtils.isEmpty(reqContent)) {
            LOGGER.error("ServiceGatewayImpl createService reqContent is null.");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    "ServiceGatewayImpl createService reqContent is null.");
        }

        // Parse request
        Map<String, Object> requestBody = JsonUtil.unMarshal(reqContent, Map.class);
        Map<String, Object> service = (Map<String, Object>)requestBody.get(FieldConstant.Create.FIELD_SERVICE);
        if(null == service) {
            service = requestBody;
        }
        ValidateUtil.assertObjectNotNull(requestBody);
        String templateId = (String)service.get(FieldConstant.Create.FIELD_TEMPLATEID);
        // query the template information
        ServiceTemplateModel templateDetail = CommonUtil.getServiceTemplateByTemplateId(templateId);
        // check the template type
        OperationResult result = null;
        switch(templateDetail.getTemplateType()) {
            case GSO:
                // for GSO， the request body is same as servicegateway.
                result = createGSOService(service);
                break;
            case SDNO:
                // for sdno, nsdId is templateId
                String nsdId = templateId;
                result = createNonGSOService(EnumServiceType.SDNO, nsdId, Constant.SDNO_URL_CREATE,
                        Constant.SDNO_URL_INSTANTIATE, Constant.SDNO_URL_QUERYJOB, service);
                break;
            case NFVO:
                // for nfvo, nsdId is the id of the template(it is not the templateId,a template
                // contains id and templateId)
                String nfvNsdId =
                        (String)templateDetail.getTemplateDetail().get(FieldConstant.CatalogTemplate.FIELD_ID);
                result = createNonGSOService(EnumServiceType.NFVO, nfvNsdId, Constant.NFVO_URL_CREATE,
                        Constant.NFVO_URL_INSTANTIATE, Constant.NFVO_URL_QUERYJOB, service);
                break;
            default:
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "query template information failed");

        }
        return result;
    }

    /**
     * Create GSO Service
     * <br>
     * 
     * @param reqBody request body
     * @return operation id for GSO
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private OperationResult createGSOService(Map<String, Object> reqBody) {
        // for GSO， the request body is same as servicegateway.
        OperationResult result = new OperationResult();
        try {

            String body = JsonUtil.marshal(reqBody);
            LOGGER.info("create gso service ,req:" + body);
            RestfulResponse restfulRsp = HttpUtil.post(Constant.GSO_URL_CREATE, body);
            CommonUtil.logTheResponseData("create gso service", restfulRsp);
            // Record the result of registration
            if(HttpCode.isSucess(restfulRsp.getStatus())) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                Map<String, String> respServiceInfo =
                        (Map<String, String>)rspBody.get(FieldConstant.Create.FIELD_RESPONSE_SERVICE);
                String serviceId = respServiceInfo.get(FieldConstant.Create.FIELD_RESPONSE_SERVICEID);
                String operationId = respServiceInfo.get(FieldConstant.Create.FIELD_RESPONSE_OPERATIONID);
                result.setServiceId(serviceId);
                result.setOperationId(operationId);
                String uri = String.format(Constant.GSO_URL_QUERY_OPRATION, serviceId, operationId);
                // use progress pool to create a new thread,for query the progress.
                ProgressPool.getInstance().dealCreateProgress(EnumServiceType.GSO, operationId, uri);
            } else {
                ExceptionArgs args = new ExceptionArgs();
                args.setDescArgs(new String[] {"Fail to create service:" + restfulRsp.getResponseContent()});
                throw new ApplicationException(restfulRsp.getStatus(), args);
            }
        } catch(ServiceException e) {
            LOGGER.error("service gateway create restful call result:", e);
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
        return result;
    }

    /**
     * <br>
     * create sdno service
     * 
     * @param reqBody the request body for create service
     * @return operation result for create sdno/nfvo service
     * @since GSO 0.5
     */
    @SuppressWarnings({"unchecked"})
    private OperationResult createNonGSOService(EnumServiceType serviceType, String nsdId, String createUri,
            String instantUri, String queryJobUri, Map<String, Object> reqBody) {

        OperationResult result = new OperationResult();
        // create service params
        Map<String, Object> paramsForCreate = new HashMap<String, Object>();
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_NSDID, nsdId);
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_NSNAME, reqBody.get(FieldConstant.Create.FIELD_NAME));
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_DESCRIPTION,
                reqBody.get(FieldConstant.Create.FIELD_DESCRIPTION));
        // get the parameters
        Map<String, Object> parameters = (Map<String, Object>)reqBody.get(FieldConstant.Create.FIELD_PARAMETERS);
        // check domain, only gso service support multi domain.
        String domain = (String)parameters.get(FieldConstant.Create.PARAM_FIELD_NAME_DOMAIN);
        if(!StringUtils.isEmpty(domain)) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "The Domain of the service should be null");
        }
        // deal with parameters
        Object nsParams = parameters.get(FieldConstant.Create.PARAM_FIELD_NAME_NSPARAM);

        // sent Create Msg
        try {
            String body = JsonUtil.marshal(paramsForCreate);
            LOGGER.info("create ns service:" + body);
            RestfulResponse restfulRsp = HttpUtil.post(createUri, body);
            CommonUtil.logTheResponseData("create ns service", restfulRsp);
            if(HttpCode.isSucess(restfulRsp.getStatus())) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                String nsInstanceId = (String)rspBody.get(FieldConstant.NSCreate.FIELD_RESPONSE_NSINSTANCEID);

                // instantiate Service
                Map<String, Object> paramsForInstantiate = new HashMap<String, Object>();
                paramsForInstantiate.put(FieldConstant.NSInstantiate.FIELD_NSINSTANCEID, nsInstanceId);
                paramsForInstantiate.put(FieldConstant.NSInstantiate.FIELD_PARAMS, nsParams);
                // sent instantiate msg
                String instantiateMsg = JsonUtil.marshal(paramsForCreate);
                LOGGER.info("instantiate ns service, req:" + instantiateMsg);
                String instantUrl = String.format(instantUri, nsInstanceId);
                RestfulResponse instantiateRsp = HttpUtil.post(instantUrl, instantiateMsg);
                CommonUtil.logTheResponseData("instantiate ns service", instantiateRsp);
                if(HttpCode.isSucess(instantiateRsp.getStatus())) {
                    Map<String, Object> instantRspBody =
                            JsonUtil.unMarshal(instantiateRsp.getResponseContent(), Map.class);
                    result.setServiceId(nsInstanceId);
                    String jobId = (String)instantRspBody.get(FieldConstant.NSInstantiate.FIELD_RESPONSE_JOBID);
                    result.setOperationId(jobId);
                    String uri = String.format(queryJobUri, jobId);
                    // use progressPool to create a thread to query the progress
                    ProgressPool.getInstance().dealCreateProgress(serviceType, jobId, uri);
                    return result;
                } else {
                    ExceptionArgs args = new ExceptionArgs();
                    args.setDescArgs(new String[] {"Fail to instantiate service:" + restfulRsp.getResponseContent()});
                    throw new ApplicationException(instantiateRsp.getStatus(), args);
                }

            } else {
                ExceptionArgs args = new ExceptionArgs();
                args.setDescArgs(new String[] {"Fail to create service:" + restfulRsp.getResponseContent()});
                throw new ApplicationException(restfulRsp.getStatus(), args);
            }
        } catch(ServiceException e) {
            LOGGER.error("service gateway create restful call result:", e);
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
    }

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @since GSO 0.5
     */
    @Override
    public String deleteService(String serviceId, HttpServletRequest httpRequest) {

        // get the serviceType
        EnumServiceType serviceType = CommonUtil.getServiceTypeByServiceId(serviceId);
        String operationId = "";
        switch(serviceType) {
            case GSO:
                // for GSO， the request body is same as servicegateway.
                operationId = deleteGSOService(serviceId);
                break;
            case SDNO:
                String deleteUri = String.format(Constant.SDNO_URL_DELETE, serviceId);
                String terminateUri = String.format(Constant.SDNO_URL_TERMINATE, serviceId);
                operationId = deleteNonGSOService(serviceType, serviceId, deleteUri, terminateUri,
                        Constant.SDNO_URL_QUERYJOB);
                break;
            case NFVO:
                String nfvDeleteUri = String.format(Constant.NFVO_URL_DELETE, serviceId);
                String nfvTerminateUri = String.format(Constant.NFVO_URL_TERMINATE, serviceId);
                operationId = deleteNonGSOService(serviceType, serviceId, nfvDeleteUri, nfvTerminateUri,
                        Constant.NFVO_URL_QUERYJOB);
                break;
            default:
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                        "query service data from inventory failed");
        }
        return operationId;
    }

    /**
     * delete gso service
     * <br>
     * 
     * @param serviceId the service id
     * @return the operationId for delete GSO service.
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private String deleteGSOService(String serviceId) {
        String operationId = "";
        try {
            LOGGER.info("delete gso service:" + serviceId);
            String url = String.format(Constant.GSO_URL_DELETE, serviceId);
            RestfulResponse restfulRsp = HttpUtil.delete(url);
            CommonUtil.logTheResponseData("delete gso service", restfulRsp);
            if(HttpCode.isSucess(restfulRsp.getStatus())) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                operationId = (String)rspBody.get(FieldConstant.Delete.FIELD_RESPONSE_OPERATIONID);
                String operationUri = String.format(Constant.GSO_URL_QUERY_OPRATION, serviceId, operationId);
                // use progress pool to start a new thread to query the progress
                ProgressPool.getInstance().dealDeleteProgress(EnumServiceType.GSO, operationId, "", operationUri);
            } else {
                LOGGER.info("delete gso service failed.");
                throw new ApplicationException(restfulRsp.getStatus(), "delete gso service failed.");
            }
        } catch(ServiceException e) {
            LOGGER.info("delete gso service failed.", e);
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
        return operationId;
    }

    /**
     * delete sdno/nfvo service
     * <br>
     * 
     * @param serviceId the service id
     * @return the operation id for delete sdno/nfvo service
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    private String deleteNonGSOService(EnumServiceType serviceType, final String serviceId, final String deleteUri,
            final String terminateUri, final String queryJobUri) {
        try {
            Map<String, String> reqBody = new HashMap<String, String>();
            reqBody.put(FieldConstant.NSTerminate.FIELD_NSINSTANCEID, serviceId);
            reqBody.put(FieldConstant.NSTerminate.FIELD_TERMINATIONTYPE, "graceful");
            reqBody.put(FieldConstant.NSTerminate.FIELD_TIMEOUT, "60");
            String body = JsonUtil.marshal(reqBody);
            LOGGER.info("delete ns service req:" + body);
            RestfulResponse restfulRsp = HttpUtil.post(terminateUri, body);
            CommonUtil.logTheResponseData("delete ns service", restfulRsp);
            if(HttpCode.isSucess(restfulRsp.getStatus())) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                String jobId = (String)rspBody.get(Constant.JOB_ID);
                // start query progress and delete the service after terminate
                String uri = String.format(queryJobUri, jobId);
                ProgressPool.getInstance().dealDeleteProgress(serviceType, jobId, deleteUri, uri);
                return jobId;
            } else {
                LOGGER.info("delete ns service failed.");
                throw new ApplicationException(restfulRsp.getStatus(), "delete ns service failed.");
            }
        } catch(ServiceException e) {
            LOGGER.info("delete ns service failed.", e);
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
    }

    /**
     * <br>
     * query the operation progress model
     * 
     * @param serviceId the service id
     * @param operationId the operation id
     * @return the operation model
     * @since GSO 0.5
     */
    @Override
    public OperationModel getOperation(String serviceId, String operationId, HttpServletRequest httpRequest) {
        OperationModel operation = ProgressPool.getInstance().getOperation(operationId);
        if(null == operation) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "the operation does not exist");
        }
        return operation;
    }

    /**
     * <br>
     * generate the parameter model for the gui
     * 
     * @param templateId the template id
     * @param servletReq the http req
     * @return the parameter model
     * @since GSO 0.5
     */
    @Override
    public CreateParameterRspModel generateCreateParameters(String templateId, HttpServletRequest servletReq) {

        CreateParameterRspModel rspModel = new CreateParameterRspModel();
        rspModel.setTemplateId(templateId);

        // query template info
        ServiceTemplateModel template = CommonUtil.getServiceTemplateByTemplateId(templateId);
        if(EnumServiceType.UNKNOWN == template.getTemplateType()) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "failed to query the template");
        }
        // generate params
        switch(template.getTemplateType()) {
            case GSO: 
                // gso support multi domain
                CreateParameterModel gsoParam = generateGSOTemplateParameters(template, true);
                rspModel.setParameters(gsoParam);
                break;
            case SDNO: 
                // for sdno , only parameters defined in the template needed.
                CreateParameterModel sdnoParam = generateTemplateParameters(template);
                rspModel.setParameters(sdnoParam);
                break;
            case NFVO: 
                // for nfvo, parameters defined in the template, location param, sdn controller
                // param need.
                CreateParameterModel nfvoParam = generateNFVOTemplateParameters(template);
                rspModel.setParameters(nfvoParam);
                break;
            default: {
                break;
            }
        }
        return rspModel;
    }

    /**
     * generate the parameter model for gso template
     * <br>
     * 
     * @param template the template model
     * @return the parameter model
     * @since GSO 0.5
     */
    private CreateParameterModel generateGSOTemplateParameters(ServiceTemplateModel template, boolean needHostParam) {
        // generate gso's own inputs
        CreateParameterModel param = generateTemplateParameters(template);

        // generate gso's subobjects.
        List<CreateParameterModel> segmentParams = new ArrayList<CreateParameterModel>();
        String templateId = (String)template.getTemplateDetail().get(FieldConstant.CatalogTemplate.FIELD_TEMPLATEID);
        List<SegmentTemplateModel> segments = CommonUtil.getSegmentTemplatesByGSOTemplateId(templateId);
        if(null == segments || segments.isEmpty()) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    "fail to query the segments of the gso template");
        }
        // loop segments
        for(SegmentTemplateModel segment : segments) {
            // some segment is not a subobject, ignore it
            if(null == segment.getTemplateModel()
                    || EnumServiceType.UNKNOWN == segment.getTemplateModel().getTemplateType()) {
                continue;
            }
            // generate the parameters for the subobject
            CreateParameterModel segmentParam = null;
            switch(segment.getTemplateModel().getTemplateType()) {
                case GSO:
                    // only the top gso's subobject can select host,so the second param is false
                    segmentParam = generateGSOTemplateParameters(segment.getTemplateModel(), false);
                    break;
                case SDNO:
                    // for sdno, only the parameters defined in the template needed.
                    segmentParam = generateTemplateParameters(segment.getTemplateModel());
                    break;
                case NFVO:
                    // for nfvo, params defined in the template,location ,sdncontroller needed.
                    segmentParam = generateNFVOTemplateParameters(segment.getTemplateModel());
                    break;
                default:
                    break;
            }
            if(null != segmentParam) {
                // the gso support multidomain
                if(needHostParam) {
                    segmentParam.setDomainHost(CommonUtil.generateDomainsInfo());
                }
                // for segment ,nodeTemplateName and nodeType needed
                segmentParam.setNodeTemplateName(segment.getNodeTemplateName());
                segmentParam.setNodeType(segment.getNodeType());
                // add the segment to the list
                segmentParams.add(segmentParam);
            }
        }
        param.setSegments(segmentParams);
        return param;
    }

    /**
     * generate nfvo parameters.
     * nfvo need vims and sdncontrollers
     * <br>
     * 
     * @param template the template model
     * @return the parameter model for nfvo
     * @since GSO 0.5
     */
    private CreateParameterModel generateNFVOTemplateParameters(ServiceTemplateModel template) {
        CreateParameterModel param = generateTemplateParameters(template);
        // for nfvo, location and sdncontroller needed.
        // query vims
        Map<String, String> vims = CommonUtil.queryVimInfo();
        if(vims.isEmpty()) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "failed to query vims");
        }
        ParameterDefineModel locationModel = CommonUtil.generateLocationParam(vims);
        param.getAdditionalParamForNs().add(locationModel);
        // query sdncontrollers
        Map<String, String> sdnControllers = CommonUtil.querySDNControllerInfo();
        if(sdnControllers.isEmpty()) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "failed to query sdn controllers");
        }
        ParameterDefineModel sdnControllerModel = CommonUtil.generateSDNControllerParam(sdnControllers);
        param.getAdditionalParamForNs().add(sdnControllerModel);
        return param;
    }

    /**
     * generate template input parameters
     * <br>
     * 
     * @param template the template model
     * @return the parameter model contains inputs of the template
     * @since GSO 0.5
     */
    private CreateParameterModel generateTemplateParameters(ServiceTemplateModel template) {
        CreateParameterModel parameters = new CreateParameterModel();
        List<ParameterDefineModel> inputs =
                ResponseUtils.getDataModelFromRsp(JsonUtil.marshal(template.getTemplateDetail()),
                        FieldConstant.CatalogTemplate.FIELD_INPUTS, ParameterDefineModel.class);
        parameters.setAdditionalParamForNs(inputs);
        String nodeType = CommonUtil.getTemplateNodeType(template);
        parameters.setNodeType(nodeType);
        return parameters;
    }

    /**
     * query the services
     * <br>
     * 
     * @param servletReq http request
     * @return the json arrray of services
     * @since GSO Mercury Release
     */
    @Override
    public List<ServiceModel> getServices(HttpServletRequest servletReq) {
        List<ServiceModel> serviceArray = CommonUtil.getServicesFromInventory();
        return serviceArray;
    }

    /**
     * <br>
     * 
     * @param serviceId the service id
     * @param servletReq the http request
     * @return
     * @since GSO Mercury Release
     */
    @Override
    public ServiceModel getService(String serviceId, HttpServletRequest servletReq) {
        ServiceModel model = CommonUtil.getServiceFromInventory(serviceId);
        if(null == model) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "query service from inventory failed");
        }
        return model;
    }

    /**
     * get the domains
     * <br>
     * 
     * @param servletReq the http request
     * @return
     * @since GSO Mercury Release
     */
    @Override
    public List<DomainModel> getDomains(HttpServletRequest servletReq) {
        List<DomainModel> domains = CommonUtil.getDomains();
        return domains;
    }
}
