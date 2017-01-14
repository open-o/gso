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
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.servicegateway.common.CommonUtil;
import org.openo.gso.servicegateway.constant.Constant;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.openo.gso.servicegateway.model.CreateParameterModel;
import org.openo.gso.servicegateway.model.CreateParameterRspModel;
import org.openo.gso.servicegateway.model.EnumServiceType;
import org.openo.gso.servicegateway.model.OperationModel;
import org.openo.gso.servicegateway.model.OperationResult;
import org.openo.gso.servicegateway.model.ParameterDefineModel;
import org.openo.gso.servicegateway.model.SegmentTemplateModel;
import org.openo.gso.servicegateway.model.ServiceTemplateModel;
import org.openo.gso.servicegateway.service.inf.IServiceGateway;
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
     * @throws ApplicationException when operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public OperationResult createService(String reqContent, HttpServletRequest httpRequest)
            throws ApplicationException {
        // check the value
        if(StringUtils.isEmpty(reqContent)) {
            LOGGER.error("ServiceGatewayImpl createService reqContent is null.");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    "ServiceGatewayImpl createService reqContent is null.");
        }

        // Parse request
        Map<String, Object> requestBody = JsonUtil.unMarshal(reqContent, Map.class);
        Map<String, Object> service = (Map<String, Object>)requestBody.get(Constant.SERVICE_INDENTIFY);
        if(null == service) {
            service = requestBody;
        }
        ValidateUtil.assertObjectNotNull(requestBody);
        String templateId = (String)service.get("templateId");
        ServiceTemplateModel templateDetail = CommonUtil.getServiceTemplateByTemplateId(templateId);
        LOGGER.info("Send the cretation RESTful request to orchestrator.The Body is" + requestBody.toString());
        // call the restful
        OperationResult result = null;
        switch(templateDetail.getTemplateType()) {
            case GSO: {
                // for GSO， the request body is same as servicegateway.
                result = createGSOService(service);
                break;
            }
            case SDNO: {
                String nsdId = (String)service.get(FieldConstant.Create.FIELD_SERVICEDEFID);
                result = createNonGSOService(EnumServiceType.SDNO, nsdId, Constant.SDNO_URL_CREATE,
                        Constant.SDNO_URL_INSTANTIATE, Constant.SDNO_URL_QUERYJOB, service);
                break;
            }
            case NFVO: {
                String nsdId = (String)templateDetail.getTemplateDetail().get(FieldConstant.CatalogTemplate.FIELD_ID);
                result = createNonGSOService(EnumServiceType.NFVO, nsdId, Constant.NFVO_URL_CREATE,
                        Constant.NFVO_URL_INSTANTIATE, Constant.NFVO_URL_QUERYJOB, service);
                break;
            }
            default: {
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "query Template info failed");
            }
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
    private OperationResult createGSOService(Map<String, Object> reqBody) throws ApplicationException {
        // for GSO， the request body is same as servicegateway.
        OperationResult result = new OperationResult();
        try {
            RestfulResponse restfulRsp = RestfulFactory.getRestInstance("http").post(Constant.GSO_URL_CREATE,
                    getRestfulParameters(JsonUtil.marshal(reqBody)));

            LOGGER.info("Receive the cretation RESTful response from orchestrator.The status is:"
                    + restfulRsp.getStatus() + " the content is:" + restfulRsp.getResponseContent());
            if(null != restfulRsp) {
                // Record the result of registration
                // (201:success;415:Invalid Parameter;500:Internal Server Error)
                LOGGER.info("restful call result:" + restfulRsp.getStatus());
                if(restfulRsp.getStatus() == HttpCode.RESPOND_ACCEPTED || restfulRsp.getStatus() == HttpCode.RESPOND_OK
                        || restfulRsp.getStatus() == HttpCode.CREATED_OK) {
                    Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                    Map<String, String> respServiceInfo =
                            (Map<String, String>)rspBody.get(FieldConstant.Create.FIELD_RESPONSE_SERVICE);
                    String serviceId = respServiceInfo.get(FieldConstant.Create.FIELD_RESPONSE_SERVICEID);
                    String operationId = respServiceInfo.get(FieldConstant.Create.FIELD_RESPONSE_OPERATIONID);
                    result.setServiceId(serviceId);
                    result.setOperationId(operationId);
                    String uri = String.format(Constant.GSO_URL_QUERY_OPRATION, serviceId, operationId);
                    ProgressPool.getInstance().dealCreateProgress(EnumServiceType.GSO, operationId, uri);
                } else {
                    ExceptionArgs args = new ExceptionArgs();
                    args.setDescArgs(new String[] {"Fail to create service:" + restfulRsp.getResponseContent()});
                    throw new ApplicationException(restfulRsp.getStatus(), args);
                }
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
    @SuppressWarnings("unchecked")
    private OperationResult createNonGSOService(EnumServiceType serviceType, String nsdId, String createUri,
            String instantUri, String queryJobUri, Map<String, Object> reqBody) throws ApplicationException {

        OperationResult result = new OperationResult();
        // create service
        Map<String, Object> paramsForCreate = new HashMap<String, Object>();
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_NSDID, nsdId);
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_NSNAME, reqBody.get(FieldConstant.Create.FIELD_NAME));
        paramsForCreate.put(FieldConstant.NSCreate.FIELD_DESCRIPTION,
                reqBody.get(FieldConstant.Create.FIELD_DESCRIPTION));
        Map<String, Object> parameters =
                (Map<String, Object>)(((List)reqBody.get(FieldConstant.Create.FIELD_PARAMETERS)).get(0));
        // check domain, only gso service support multi domain.
        String domain = (String)parameters.get(FieldConstant.Create.PARAM_FIELD_NAME_DOMAIN);
        if(!StringUtils.isEmpty(domain)) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "The Domain of the Service should be null");
        }
        // deal with parameters
        Object nsParams = parameters.get(FieldConstant.Create.PARAM_FIELD_NAME_NSPARAM);

        // sent Create Msg
        try {
            RestfulResponse restfulRsp = RestfulFactory.getRestInstance("http").post(createUri,
                    getRestfulParameters(JsonUtil.marshal(paramsForCreate)));

            LOGGER.info("Receive the cretation RESTful response from orchestrator.The status is:"
                    + restfulRsp.getStatus() + " the content is:" + restfulRsp.getResponseContent());
            // Record the result of registration
            // (201:success;415:Invalid Parameter;500:Internal Server Error)
            LOGGER.info("restful call result:" + restfulRsp.getStatus());
            if(restfulRsp.getStatus() == HttpCode.RESPOND_ACCEPTED || restfulRsp.getStatus() == HttpCode.RESPOND_OK
                    || restfulRsp.getStatus() == HttpCode.CREATED_OK) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                String nsInstanceId = (String)rspBody.get(FieldConstant.NSCreate.FIELD_RESPONSE_NSINSTANCEID);

                // instantiate Service
                Map<String, Object> paramsForInstantiate = new HashMap<String, Object>();
                paramsForInstantiate.put(FieldConstant.NSInstantiate.FIELD_NSINSTANCEID, nsInstanceId);
                paramsForInstantiate.put(FieldConstant.NSInstantiate.FIELD_PARAMS, nsParams);
                RestfulResponse instantiateRsp = RestfulFactory.getRestInstance("http").post(instantUri,
                        getRestfulParameters(JsonUtil.marshal(paramsForCreate)));
                if(instantiateRsp.getStatus() == HttpCode.RESPOND_ACCEPTED
                        || instantiateRsp.getStatus() == HttpCode.RESPOND_OK
                        || instantiateRsp.getStatus() == HttpCode.CREATED_OK) {
                    Map<String, Object> instantRspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                    result.setServiceId(nsInstanceId);
                    String jobId = (String)instantRspBody.get(FieldConstant.NSInstantiate.FIELD_RESPONSE_JOBID);
                    result.setOperationId(jobId);
                    String uri = String.format(queryJobUri, jobId);
                    ProgressPool.getInstance().dealCreateProgress(EnumServiceType.GSO, jobId, uri);
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
     * get the parameters for restful<br/>
     * 
     * @author
     * @param bodyData
     *            Json Body
     * @return the RestfulParametes Instance
     * @since GSO 0.5, 2016-8-9
     */
    private static RestfulParametes getRestfulParameters(final String bodyData) {
        RestfulParametes param = new RestfulParametes();
        param.putHttpContextHeader(Constant.HEAD_ERMAP_TYPE, Constant.HEAD_ERMAP_VALUE);
        param.setRawData(bodyData);
        return param;
    }

    /**
     * Delete service instances.<br/>
     * 
     * @param serviceId service instance ID
     * @param httpRequest http request
     * @throws ApplicationException operate DB or parameter is wrong.
     * @since GSO 0.5
     */
    @Override
    public String deleteService(String serviceId, String reqContent, HttpServletRequest httpRequest)
            throws ApplicationException {
        if(httpRequest == null) {
            LOGGER.error("ServiceGatewayImpl.deleteService httpRequest is null");
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR,
                    "ServiceGatewayImpl.deleteService httpRequest is null");
        }
        EnumServiceType serviceType = CommonUtil.getServiceTypeByServiceId(serviceId);
        String operationId = "";
        switch(serviceType) {
            case GSO: {
                // for GSO， the request body is same as servicegateway.
                operationId = deleteGSOService(serviceId);
                break;
            }
            case SDNO: {
                String deleteUri = String.format(Constant.SDNO_URL_DELETE, serviceId);
                String terminateUri = String.format(Constant.SDNO_URL_TERMINATE, serviceId);
                deleteNonGSOService(serviceType, serviceId, deleteUri, terminateUri, Constant.SDNO_URL_QUERYJOB);
                break;
            }
            case NFVO: {
                String deleteUri = String.format(Constant.NFVO_URL_DELETE, serviceId);
                String terminateUri = String.format(Constant.NFVO_URL_TERMINATE, serviceId);
                deleteNonGSOService(serviceType, serviceId, deleteUri, terminateUri, Constant.NFVO_URL_QUERYJOB);
                break;
            }
            default: {
                throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "query Template info failed");
            }
        }
        return operationId;
    }

    /**
     * delete gso service
     * <br>
     * 
     * @param serviceId
     * @return the operationId for delete GSO service.
     * @since GSO 0.5
     */
    private String deleteGSOService(String serviceId) throws ApplicationException {
        String operationId = "";
        try {
            RestfulResponse restfulRsp =
                    RestfulFactory.getRestInstance("http").delete(Constant.GSO_URL_DELETE, getRestfulParameters(""));
            if(restfulRsp.getStatus() == HttpCode.RESPOND_ACCEPTED || restfulRsp.getStatus() == HttpCode.RESPOND_OK
                    || restfulRsp.getStatus() == HttpCode.CREATED_OK) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                operationId = (String)rspBody.get(FieldConstant.Delete.FIELD_RESPONSE_OPERATIONID);
                String operationUri = String.format(Constant.GSO_URL_QUERY_OPRATION, serviceId, operationId);
                ProgressPool.getInstance().dealDeleteProgress(EnumServiceType.GSO, operationId, serviceId, "",
                        operationUri);
            } else {
                LOGGER.info("delete gso service failed.");
                throw new ApplicationException(restfulRsp.getStatus(), "delete gso service failed.");
            }
        } catch(ServiceException e) {
            LOGGER.info("delete gso service failed.");
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
        return operationId;
    }

    /**
     * delete sdno/nfvo service
     * <br>
     * 
     * @param ServiceId
     * @return the operation id for delete sdno/nfvo service
     * @throws ApplicationException
     * @since GSO 0.5
     */
    private String deleteNonGSOService(EnumServiceType serviceType, final String serviceId, final String deleteUri,
            final String terminateUri, final String queryJobUri) throws ApplicationException {
        try {
            Map<String, String> reqBody = new HashMap<String, String>();
            reqBody.put(FieldConstant.NSTerminate.FIELD_NSINSTANCEID, serviceId);
            reqBody.put(FieldConstant.NSTerminate.FIELD_TERMINATIONTYPE, "graceful");
            reqBody.put(FieldConstant.NSTerminate.FIELD_TIMEOUT, "60");
            RestfulResponse restfulRsp = RestfulFactory.getRestInstance("http").post(terminateUri,
                    getRestfulParameters(JsonUtil.marshal(reqBody)));
            if(HttpCode.isSucess(restfulRsp.getStatus())) {
                Map<String, Object> rspBody = JsonUtil.unMarshal(restfulRsp.getResponseContent(), Map.class);
                String jobId = (String)rspBody.get(Constant.JOB_ID);
                // start query progress and delete the service after terminate
                String uri = String.format(queryJobUri, jobId);
                ProgressPool.getInstance().dealDeleteProgress(serviceType, jobId, serviceId, deleteUri, uri);
                return jobId;
            } else {
                LOGGER.info("delete gso service failed.");
                throw new ApplicationException(restfulRsp.getStatus(), "delete gso service failed.");
            }
        } catch(ServiceException e) {
            LOGGER.info("delete gso service failed.");
            throw new ApplicationException(e.getHttpCode(), e.getExceptionArgs());
        }
    }

    /**
     * <br>
     * 
     * @param serviceId
     * @param operationId
     * @return
     * @throws ApplicationException
     * @since GSO 0.5
     */
    public OperationModel getOperation(String serviceId, String operationId, HttpServletRequest httpRequest)
            throws ApplicationException {
        OperationModel operation = ProgressPool.getInstance().getOperation(operationId);
        if(null == operation) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "the operation does not exist");
        }
        return operation;
    }

    /**
     * <br>
     * 
     * @param templateId
     * @param servletReq
     * @return
     * @throws ApplicationException
     * @since GSO 0.5
     */
    public CreateParameterRspModel generateCreateParameters(String templateId, HttpServletRequest servletReq)
            throws ApplicationException {

        CreateParameterRspModel rspModel = new CreateParameterRspModel();
        rspModel.setTemplateId(templateId);

        // query template info
        ServiceTemplateModel template = CommonUtil.getServiceTemplateByTemplateId(templateId);
        if(EnumServiceType.UNKNOWN == template.getTemplateType()) {
            throw new ApplicationException(HttpCode.INTERNAL_SERVER_ERROR, "failed to query the template");
        }

        // generate params
        switch(template.getTemplateType()) {
            case GSO: {
                // gso support multi domain
                CreateParameterModel param = generateGSOTemplateParameters(template, true);
                rspModel.setParameters(param);
                break;
            }
            case SDNO: {
                CreateParameterModel param = generateTemplateParameters(template);
                rspModel.setParameters(param);
                break;
            }
            case NFVO: {
                CreateParameterModel param = generateNFVOTemplateParameters(template);
                rspModel.setParameters(param);
                break;
            }
            default: {
                break;
            }
        }
        return rspModel;
    }

    /**
     * generate the parameter list for gso template
     * <br>
     * 
     * @param template
     * @return
     * @since GSO 0.5
     */
    private CreateParameterModel generateGSOTemplateParameters(ServiceTemplateModel template, boolean needHostParam) {
        // generate gso's inputs
        CreateParameterModel param = generateTemplateParameters(template);

        // generate gso's subobjects.
        List<CreateParameterModel> segmentParams = new ArrayList<CreateParameterModel>();
        String templateId = (String)template.getTemplateDetail().get(FieldConstant.CatalogTemplate.FIELD_TEMPLATEID);
        List<SegmentTemplateModel> segments = CommonUtil.getSegmentTemplatesByGSOTemplateId(templateId);
        for(SegmentTemplateModel segment : segments) {
            if(null == segment.getTemplateModel()
                    || EnumServiceType.UNKNOWN == segment.getTemplateModel().getTemplateType()) {
                continue;
            }
            CreateParameterModel segmentParam = null;
            switch(segment.getTemplateModel().getTemplateType()) {
                case GSO:
                    // only the top gso's subobject can select host.
                    segmentParam = generateGSOTemplateParameters(segment.getTemplateModel(), false);
                    break;
                case SDNO:
                    segmentParam = generateTemplateParameters(segment.getTemplateModel());
                    break;
                case NFVO:
                    segmentParam = generateNFVOTemplateParameters(template);
                    break;
                default:
                    break;
            }
            if(null != segmentParam) {
                // the gso support multidomain
                if(needHostParam) {
                    segmentParam.setDomainHost(CommonUtil.generateDomainsInfo());
                }
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
     * @param template
     * @return
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
     * generate template parameters
     * <br>
     * 
     * @param template
     * @return
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

}
