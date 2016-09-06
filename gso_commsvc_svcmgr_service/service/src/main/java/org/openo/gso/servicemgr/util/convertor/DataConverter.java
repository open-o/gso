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

package org.openo.gso.servicemgr.util.convertor;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.servicemgr.constant.Constant;
import org.openo.gso.servicemgr.model.catalogmo.OperationModel;
import org.openo.gso.servicemgr.model.servicemo.ServiceModel;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;
import org.openo.gso.servicemgr.util.json.JsonUtil;
import org.openo.gso.servicemgr.util.uuid.UuidUtils;

/**
 * Data converter.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public class DataConverter {

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
    public static ServiceModel convertServiceModel(Map<String, Object> data) {
        String uuid = UuidUtils.createUuid();
        ServiceModel model = new ServiceModel();

        // Service basic data
        model.setServiceId(uuid);
        model.setName((String)data.get(Constant.SERVICE_NAME));
        model.setDescription((String)data.get(Constant.SERVICE_DESCRIPTION));

        // Service template data
        ServicePackageMapping packageMapping = new ServicePackageMapping();
        packageMapping.setServiceId(uuid);
        packageMapping.setTemplateId((String)data.get(Constant.SERVICE_TEMPLATE_ID));
        packageMapping.setTemplateName((String)data.get(Constant.SERVICE_NAME));
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
     * Construct body used to interacting with wso.<br/>
     * 
     * @param operation operation object
     * @param parameter which in body
     * @return request body
     * @throws ServiceException
     * @since GSO 0.5
     */
    public static Map<String, Object> constructWsoBody(OperationModel operation, Object parameter)
            throws ServiceException {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(Constant.WSO_PROCESSID, operation.getProcessId());
        if(null != parameter) {
            body.put(Constant.WSO_PARAMS, JsonUtil.marshal(parameter));
        }

        return body;
    }
}