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

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.Constant;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServicePackageMapping;
import org.openo.gso.util.json.JsonUtil;
import org.openo.gso.util.uuid.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static Map<String, Object> constructWorkflowBody(OperationModel operation, Object parameter)
            throws ApplicationException {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(Constant.WSO_PROCESSID, operation.getProcessId());
        if(null != parameter) {
            Map<String, Object> planInput = new HashMap<String, Object>();
            Map<String, String> inputParam = new HashMap<String, String>();
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
            Map<String, Object> properties = null;
            for(ServiceModel model : services) {
                properties = JsonUtil.unMarshal(JsonUtil.marshal(model), Map.class);
                properties.remove(Constant.MODEL_COLUMN_SEGMENT_NUMBER);
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
            properties = JsonUtil.unMarshal(JsonUtil.marshal(service), Map.class);
            properties.remove(Constant.MODEL_COLUMN_SEGMENT_NUMBER);
            resultMap.put(Constant.SERVICE_INDENTIFY, properties);
        }
        resultMap.put(Constant.SERVICE_INDENTIFY, properties);

        return resultMap;
    }
}
