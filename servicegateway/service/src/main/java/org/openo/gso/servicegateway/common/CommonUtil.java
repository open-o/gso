/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;
import org.openo.gso.servicegateway.constant.Constant;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.openo.gso.servicegateway.model.EnumServiceType;
import org.openo.gso.servicegateway.model.ParameterDefineModel;
import org.openo.gso.servicegateway.model.SegmentTemplateModel;
import org.openo.gso.servicegateway.model.ServiceTemplateModel;
import org.openo.gso.servicegateway.service.impl.ServiceGatewayImpl;
import org.openo.gso.servicegateway.util.http.HttpUtil;
import org.openo.gso.servicegateway.util.json.JsonUtil;
import org.openo.gso.servicegateway.util.register.RegisterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.11
 */
public class CommonUtil {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGatewayImpl.class);

    /**
     * <br>
     * get the template model by template id
     * @param templateId the template id
     * @return the Type of the Template
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static ServiceTemplateModel getServiceTemplateByTemplateId(String templateId) {

        ServiceTemplateModel templateModel = new ServiceTemplateModel();
        try {
            LOGGER.info("query template detail for templat id:" + templateId);
            String url = String.format(Constant.CATALOG_TEMPLATE_URL, templateId);
            RestfulResponse resp = HttpUtil.get(url, new HashMap<String, String>());
            logTheResponseData("query template detail", resp);
            if(HttpCode.isSucess(resp.getStatus())) {

                Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
                templateModel.setTemplateDetail(rspBody);
                String csarId = (String)rspBody.get(FieldConstant.CatalogTemplate.FIELD_CSARID);
                EnumServiceType type = getServiceTypeByCsarId(csarId);
                templateModel.setTemplateType(type);
                return templateModel;
            }
        } catch(ServiceException e) {
            LOGGER.info("query the template by template Id failed, template Id:" + templateId, e);
        }
        LOGGER.info("query the template by template Id failed, template Id:" + templateId);
        return templateModel;
    }

    /**
     * get service type by csar id
     * <br>
     * 
     * @param csarId the csar id 
     * @return the service type of the csar
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static EnumServiceType getServiceTypeByCsarId(String csarId) {
        EnumServiceType type = EnumServiceType.UNKNOWN;
        RestfulResponse csarResp;
        try {
            LOGGER.info("query the csar, csar id:" + csarId);
            String url = String.format(Constant.CATALOG_CSAR_URL, csarId);
            csarResp = HttpUtil.get(url, new HashMap<String, String>());
            logTheResponseData("query the casr  by csar Id", csarResp);
            if(HttpCode.isSucess(csarResp.getStatus())) {
                Map<String, Object> csarBody = JsonUtil.unMarshal(csarResp.getResponseContent(), Map.class);
                String csarType = (String)csarBody.get(Constant.CATALOG_CSAR_PARAM_TYPE);
                if(Constant.CATALOG_CSAR_TYPE_GSAR.equals(csarType)) {
                    type = EnumServiceType.GSO;
                } else if(Constant.CATALOG_CSAR_TYPE_NFAR.equals(csarType)
                        || Constant.CATALOG_CSAR_TYPE_NSAR.equals(csarType)) {
                    type = EnumServiceType.NFVO;
                } else if(Constant.CATALOG_CSAR_TYPE_SSAR.equals(csarType)) {
                    type = EnumServiceType.SDNO;
                } else {
                    type = EnumServiceType.UNKNOWN;
                }
            }
        } catch(ServiceException e) {
            LOGGER.info("query csar info exception", e);
        }
        return type;
    }

    /**
     * query service type by service Id
     * <br>
     * 
     * @param serviceId the service id
     * @return the service type
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static EnumServiceType getServiceTypeByServiceId(String serviceId) {
        try {
            LOGGER.info("query data from inventory, service id:" + serviceId);
            String url = String.format(Constant.INVENTORY_URL_QUERYSERVICE, serviceId);
            RestfulResponse resp = HttpUtil.get(url, new HashMap<String, String>());
            logTheResponseData("query data from inventory", resp);
            if(HttpCode.isSucess(resp.getStatus())) {
                Map<String, Object> rspBody =
                        (Map<String, Object>)JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
                String serviceType = (String)rspBody.get("serviceType");
                if("GSO".equals(serviceType)) {
                    return EnumServiceType.GSO;
                } else if("SDNO".equals(serviceType)) {
                    return EnumServiceType.SDNO;
                } else if("NFVO".equals(serviceType)) {
                    return EnumServiceType.NFVO;
                } else {
                    return EnumServiceType.UNKNOWN;
                }
            }
        } catch(ServiceException e) {
            LOGGER.info("query the service info by service Id failed, service Id:" + serviceId, e);
        }
        return EnumServiceType.UNKNOWN;
    }

    /**
     * query vim info
     * <br>
     * 
     * @return the vim map, key is id ,value is nmae 
     * @since GSO 0.5
     */
    public static Map<String, String> queryVimInfo() {
        Map<String, String> vims = new HashMap<String, String>();
        try {
            LOGGER.info("query vims from extsys start");
            RestfulResponse resp = HttpUtil.get(Constant.EXTSYS_URL_QUERYVIMS, new HashMap<String, String>());
            logTheResponseData("query vims from extsys", resp);
            if(HttpCode.isSucess(resp.getStatus())) {

                JSONArray array = JSONArray.fromObject(resp.getResponseContent());
                for(int i = 0, size = array.size(); i < size; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    vims.put((String)obj.get(FieldConstant.Vim.FIELD_VIMID),
                            (String)obj.get(FieldConstant.Vim.FIELD_NAME));
                }
                return vims;
            }
        } catch(ServiceException e) {

            LOGGER.info("query the vims failed", e);
        }
        LOGGER.info("query the vims failed");
        return vims;
    }

    /**
     * query sdn controller info
     * <br>
     * 
     * @return the sdn controller map, key is id, value is name
     * @since GSO 0.5
     */
    public static Map<String, String> querySDNControllerInfo() {
        Map<String, String> sdncontrollers = new HashMap<String, String>();
        try {
            LOGGER.info("query sdn controllers from extsys start");
            RestfulResponse resp = HttpUtil.get(Constant.EXTSYS_URL_QUERYSDNCONTROLLERS, new HashMap<String, String>());
            logTheResponseData("query sdn controllers from extsys", resp);
            if(HttpCode.isSucess(resp.getStatus())) {

                JSONArray array = JSONArray.fromObject(resp.getResponseContent());
                for(int i = 0, size = array.size(); i < size; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    sdncontrollers.put((String)obj.get(FieldConstant.SDNController.FIELD_SDNCONTROLLERID),
                            (String)obj.get(FieldConstant.SDNController.FIELD_NAME));
                }
                return sdncontrollers;
            }
        } catch(ServiceException e) {

            LOGGER.info("query the vims failed", e);
        }
        LOGGER.info("query the vims failed");
        return sdncontrollers;
    }

    /**
     * generate location param
     * <br>
     * 
     * @param vims the map of the vims, key is id, value is name
     * @return the location parameter define model
     * @since GSO 0.5
     */
    public static ParameterDefineModel generateLocationParam(Map<String, String> vims) {
        ParameterDefineModel location = new ParameterDefineModel();
        location.setName("location");
        location.setDescription("location for the service");
        location.setType("enum");
        location.setRequired("true");
        location.setRange(vims);
        return location;
    }

    /**
     * generate sdnController param
     * <br>
     * 
     * @param sdncontrollers the map of the sdncontroller ,key is id, value is name
     * @return the sdncontroller param define model
     * @since GSO 0.5
     */
    public static ParameterDefineModel generateSDNControllerParam(Map<String, String> sdnControllers) {
        ParameterDefineModel sdnController = new ParameterDefineModel();
        sdnController.setName("sdncontroller");
        sdnController.setDescription("sdn controller for the service");
        sdnController.setType("enum");
        sdnController.setRequired("true");
        sdnController.setRange(sdnControllers);
        return sdnController;
    }

    /**
     * <br>
     * generate domains param
     * 
     * @return the domain parameter define model
     * @since GSO 0.5
     */
    public static ParameterDefineModel generateDomainsInfo() {
        // get the jsonString form the service file
        String root = SystemEnvVariablesFactory.getInstance().getAppRoot();
        String serviceFilePath = root + File.separator + Constant.FILE_PATH_DOMAINSINFO;
        String jsonInfo = RegisterUtil.readFile(serviceFilePath);
        if("".equals(jsonInfo))
        {
            return null;
        }
        JSONArray array = JSONArray.fromObject(jsonInfo);
        Map<String, String> domainsInfo = new HashMap<String, String>();
        for(int i = 0, size = array.size(); i < size; i++) {
            JSONObject obj = array.getJSONObject(i);
            domainsInfo.put((String)obj.get("name"), (String)obj.get("host"));
        }
        // if only default "localhost" contained. no need to select domain.
        if(domainsInfo.size() == 1) {
            return null;
        }
        ParameterDefineModel domainsParam = new ParameterDefineModel();
        domainsParam.setName("domainHost");
        domainsParam.setDescription("the domain of the service");
        domainsParam.setType("enum");
        domainsParam.setRequired("true");
        domainsParam.setRange(domainsInfo);
        return domainsParam;
    }

    /**
     * query the sub templates by gso's template id
     * <br>
     * 
     * @param templateId the template id
     * @return key is nodeTemplateName of the subTemplate, value is the template
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static List<SegmentTemplateModel> getSegmentTemplatesByGSOTemplateId(String templateId) {
        List<SegmentTemplateModel> segments = new ArrayList<SegmentTemplateModel>();
        try {
            // query nodetypes by template id
            String queryNodeTypeUrl = String.format(Constant.CATALOG_NODETYPE_URL, templateId);
            LOGGER.info("query nodetypes for template id:" + templateId);
            RestfulResponse resp = HttpUtil.get(queryNodeTypeUrl, new HashMap<String, String>());
            logTheResponseData("query nodetypes from catalog", resp);
            if(HttpCode.isSucess(resp.getStatus())) {
                StringBuffer queryNodeTemplateUri = new StringBuffer();
                JSONArray array = JSONArray.fromObject(resp.getResponseContent());
                for(int i = 0, size = array.size(); i < size; i++) {
                    JSONObject object = array.getJSONObject(i);
                    // change the segments to model list
                    SegmentTemplateModel segmentTemplate = new SegmentTemplateModel();
                    segmentTemplate.setNodeTemplateName((String)object.get(FieldConstant.NodeTemplates.FIELD_NAME));
                    String nodeType = (String)object.get(FieldConstant.NodeTemplates.FIELD_TYPE);
                    segmentTemplate.setNodeType(nodeType);
                    segments.add(segmentTemplate);
                    queryNodeTemplateUri.append(nodeType);
                    if(i != size - 1) {
                        queryNodeTemplateUri.append(",");
                    }
                }
                // query nodeTempaltes by node types
                String queryNodeTemplatesUrl =
                        String.format(Constant.CATALOG_TEMPLATE_BYNOTEYPE_URL, queryNodeTemplateUri);
                LOGGER.info("query node templates for node types:" + queryNodeTemplateUri);
                RestfulResponse nodeTemplatesResp = HttpUtil.get(queryNodeTemplatesUrl, new HashMap<String, String>());
                logTheResponseData("query node templates from catalog", nodeTemplatesResp);
                if(HttpCode.isSucess(nodeTemplatesResp.getStatus())) {
                    JSONArray arrayTempate = JSONArray.fromObject(nodeTemplatesResp.getResponseContent());
                    for(int i = 0, size = arrayTempate.size(); i < size; i++) {
                        String object = arrayTempate.getString(i);
                        Map<String, Object> rspBody = JsonUtil.unMarshal(object, Map.class);
                        // here parse the template to model
                        ServiceTemplateModel templateModel = new ServiceTemplateModel();
                        templateModel.setTemplateDetail(rspBody);
                        String csarId = (String)rspBody.get(FieldConstant.CatalogTemplate.FIELD_CSARID);
                        templateModel.setTemplateType(getServiceTypeByCsarId(csarId));
                        // fill the template model to segment list
                        SegmentTemplateModel segmentTemplate =
                                getSegmentTemplateModel(segments, getTemplateNodeType(templateModel));
                        segmentTemplate.setTemplateModel(templateModel);
                    }
                }
            }
        } catch(ServiceException e) {

            LOGGER.info("query the segment templates by template id failed, template Id:" + templateId, e);
        }
        return segments;
    }

    /**
     * get the segment template model from the list by node type
     * <br>
     * 
     * @param segmentTemplates the list of the segment templates
     * @param nodeType the node type
     * @return the segment template
     * @since GSO 0.5
     */
    private static SegmentTemplateModel getSegmentTemplateModel(List<SegmentTemplateModel> segmentTemplates,
            String nodeType) {
        for(SegmentTemplateModel template : segmentTemplates) {
            if(nodeType.equals(template.getNodeType())) {
                return template;
            }
        }
        return null;
    }

    /**
     * get template node type
     * <br>
     * 
     * @param template the template model
     * @return the template node type
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    public static String getTemplateNodeType(ServiceTemplateModel template) {
        Map<String, Object> subStitutionInfo =
                (Map<String, Object>)template.getTemplateDetail().get(FieldConstant.CatalogTemplate.FIELD_SUBSTITUTION);
        String nodeType = "";
        if(null != subStitutionInfo) {
            nodeType = (String)subStitutionInfo.get(FieldConstant.CatalogTemplate.FIELD_SUBSTITUTION_NODETYPE);

        }
        return nodeType;
    }

    /**
     * print the rsponse
     * <br>
     * 
     * @param oparation the operation description
     * @param resp the response model 
     * @since GSO 0.5
     */
    public static void logTheResponseData(String oparation, RestfulResponse resp) {
        LOGGER.info(oparation + " status:" + resp.getStatus());
        LOGGER.info(oparation + " content:" + resp.getResponseContent());
    }
}