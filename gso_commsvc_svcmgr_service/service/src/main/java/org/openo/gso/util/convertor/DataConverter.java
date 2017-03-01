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

package org.openo.gso.util.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.commsvc.common.util.JsonUtil;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.Constant;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.servicemo.InvServiceModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.model.servicemo.ServiceParameter;
import org.openo.gso.model.servicemo.ServiceSegmentModel;
import org.openo.gso.util.uuid.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import net.sf.json.JSONObject;

/**
 * Data converter.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public class DataConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataConverter.class);

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private DataConverter() {

    }

    /**
     * Convert service model.<br/>
     * 
     * @param data object that is converted.
     * @return service model
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static ServiceModel convertServiceModel(Map<String, Object> data) {
        String uuid = UuidUtils.createUuid();
        ServiceModel model = new ServiceModel();

        // Service basic data
        model.setServiceId(uuid);
        model.setName((String)data.get(Constant.SERVICE_NAME));
        model.setDescription((String)data.get(Constant.SERVICE_DESCRIPTION));
        model.setActiveStatus(Constant.DEFAULT_STRING);
        model.setStatus(CommonConstant.Status.PROCESSING);
        model.setCreator(Constant.DEFAULT_STRING);
        model.setSegmentNumber(0);
        Object param = data.get(Constant.SERVICE_PARAMETERS);
        if(param instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>)param;
            Object segments = paramMap.get(Constant.SERVICE_SEGMENTS);
            if(segments instanceof List) {
                model.setSegmentNumber(((List<?>)segments).size());
            }
        }

        // Service template data
        ServicePackageMapping packageMapping = new ServicePackageMapping();
        packageMapping.setServiceId(uuid);
        packageMapping.setTemplateId((String)data.get(Constant.SERVICE_TEMPLATE_ID));
        packageMapping.setTemplateName((String)data.get(Constant.SERVICE_TEMPLATE_NAME));
        packageMapping.setServiceDefId((String)data.get(Constant.SERVICE_DEF_ID));
        model.setServicePackage(packageMapping);

        return model;
    }

    /**
     * Fill in data which is not seen in gui.<br/>
     * 
     * @param model service instance data
     * @since GSO 0.5
     */
    public static void addDynamicData(ServiceModel model) {

        model.setCreateAt(System.currentTimeMillis());
    }

    /**
     * Construct body used to interacting with workflow.<br/>
     * 
     * @param operation operation object
     * @param parameter which in body
     * @return request body
     * @throws ApplicationException
     * @since GSO 0.5
     */
    public static Map<String, Object> constructWorkflowBody(OperationModel operation, Object parameter) {
        Map<String, Object> body = new HashMap<>();
        body.put(Constant.WSO_PROCESSID, operation.getProcessId());
        if(null != parameter) {
            Map<String, Object> planInput = new HashMap<>();
            Map<String, String> inputParam = new HashMap<>();
            inputParam.put("inputParam", JsonUtil.marshal(parameter));
            planInput.put("planInput", inputParam);
            body.put(Constant.WSO_PARAMS, planInput);
        }
        LOGGER.warn(JsonUtil.marshal(body));

        return body;
    }

    /**
     * Assemble all of service instances response result.<br/>
     * 
     * @param services service instance
     * @return response result.
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static Object getAllSvcIntancesResult(List<ServiceModel> services) {
        List<Object> resultLst = new ArrayList<>();
        if(null != services) {
            Map<String, Object> properties;
            for(ServiceModel model : services) {
                properties = JsonUtil.unMarshal(JsonUtil.marshal(model), Map.class);
                if(null != model.getServicePackage()) {
                    properties.put(Constant.SERVICE_DEF_ID, model.getServicePackage().getServiceDefId());
                    properties.put(Constant.SERVICE_TEMPLATE_ID, model.getServicePackage().getTemplateId());
                }
                resultLst.add(properties);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Constant.SERVICES_INDENTIRY, resultLst);

        return resultMap;
    }

    /**
     * Assemble service instance response result<br/>
     * 
     * @param service service instance
     * @return response result
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static Object getSvcInstanceResult(ServiceModel service) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> properties = null;
        if(null != service) {

            // Deal service properties
            properties = JsonUtil.unMarshal(JsonUtil.marshal(service), Map.class);
            if(null != service.getServicePackage()) {
                properties.put(Constant.SERVICE_DEF_ID, service.getServicePackage().getServiceDefId());
                properties.put(Constant.SERVICE_TEMPLATE_ID, service.getServicePackage().getTemplateId());
            }

            // Deal service parameters
            properties.put(Constant.SERVICE_PARAMETERS, service.getParameter());
        }
        resultMap.put(Constant.SERVICE_INDENTIFY, properties);
        LOGGER.info("getSvcInstanceResult: {}", JsonUtil.marshal(resultMap));
        return resultMap;
    }

    /**
     * Assemble workflow parameters.<br/>
     * 
     * @param params service instance parameter
     * @param model service instance model
     * @return workflow parameters
     * @since GSO 0.5
     */
    public static Object getWorkFlowParams(Map<String, Object> params, ServiceModel model) {
        List<Object> workflowParam = new ArrayList<>();
        if(null != params) {
            Object segments = params.get(Constant.SERVICE_SEGMENTS);
            if(segments instanceof List) {
                workflowParam = constructWorkflowParam((List)segments, model);
            }
        }

        return (new HashMap<>()).put(Constant.SERVICE_SEGMENTS, workflowParam);
    }

    /**
     * Construct workflow parameter<br/>
     * 
     * @param segmentLst service segments
     * @param model service model
     * @return workflow parameters
     * @since GSO 0.5
     */
    private static List<Object> constructWorkflowParam(List<Object> segmentLst, ServiceModel model) {
        List<Object> workflowParam = new ArrayList<>();
        for(Object segment : segmentLst) {
            JSONObject obj = JSONObject.fromObject(segment);
            if(null == obj) {
                continue;
            }

            // add fixed parameters
            obj.put(Constant.SERVICE_ID, model.getServiceId());
            obj.put(Constant.SERVICE_SEGMENT_NAME_DIRVER,
                    model.getName() + "." + obj.getString(Constant.NODE_TEMPLATE_NAME));
            obj.put(Constant.SERVICE_SEGMENT_DES_DIRVER, model.getDescription());
            workflowParam.add(JSONObject.toBean(obj));
        }

        return workflowParam;
    }

    /**
     * Convert gso data to inventory data.<br/>
     * 
     * @param service gso instance
     * @return inventory instance
     * @since GSO 0.5
     */
    public static InvServiceModel convertToInvData(ServiceModel service) {
        InvServiceModel invService = new InvServiceModel();
        invService.setServiceId(service.getServiceId());
        invService.setName(service.getName());
        invService.setServiceType(CommonConstant.SegmentType.GSO);
        invService.setDescription(service.getDescription());
        invService.setActiveStatus(service.getActiveStatus());
        invService.setStatus(service.getStatus());
        invService.setCreator(service.getCreator());
        invService.setCreateAt(service.getCreateAt());

        return invService;
    }

    /**
     * Convert segments response data.<br/>
     * 
     * @param segments service segments
     * @param serviceId service instance ID
     * @return response object
     * @since GSO 0.5
     */
    public static Map<String, Object> getSegments(List<ServiceSegmentModel> segments, String serviceId) {

        // service instance ID
        Map<String, Object> rspBody = new HashMap<>();
        rspBody.put(Constant.SERVICE_ID, serviceId);

        // segments list
        List<Object> segmentLst = new ArrayList<>();
        if(!CollectionUtils.isEmpty(segments)) {
            Map<String, String> segParamRsp;
            for(ServiceSegmentModel segModel : segments) {
                segParamRsp = JsonUtil.unMarshal(JsonUtil.marshal(segModel), Map.class);
                segParamRsp.remove(Constant.SERVICE_ID);
                segParamRsp.remove(Constant.SEGMENT_PROPERTY_NAME);
                segParamRsp.remove(Constant.SEGMENT_PROPERTY_NODENAME);
                segParamRsp.remove(Constant.SEGMENT_PROPERTY_NODETYPE);
                segParamRsp.remove(Constant.SEGMENT_PROPERTY_TEMPLATEID);
                segmentLst.add(segParamRsp);
            }
            rspBody.put(Constant.SERVICE_SEGMENTS, segmentLst);
        }

        // response body
        Map<String, Object> segMap = new HashMap<>();
        segMap.put(Constant.SERVICE_INDENTIFY, rspBody);
        LOGGER.info("getSegments: {}", JsonUtil.marshal(segMap));
        return segMap;
    }

    /**
     * Get parameter from service model for DB.<br/>
     * 
     * @param serviceId service instance ID
     * @param parameter service parameter with map structure
     * @return service parameter object
     * @since GSO 0.5
     */
    public static ServiceParameter convertDBParam(String serviceId, Object parameter) {
        ServiceParameter param = new ServiceParameter();
        param.setServiceId(serviceId);
        param.setParamName(Constant.SERVICE_PARAMETERS);
        param.setParamValue(JsonUtil.marshal(parameter));
        return param;
    }
}
