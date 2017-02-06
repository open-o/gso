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

package org.openo.gso.restproxy.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.catalogmo.ServiceTemplateModel;
import org.openo.gso.model.drivermo.ServiceTemplate;
import org.openo.gso.restproxy.inf.ICatalogProxy;
import org.openo.gso.util.RestfulUtil;
import org.openo.gso.util.http.HttpUtil;
import org.openo.gso.util.http.ResponseUtils;
import org.openo.gso.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

/**
 * Implement class of calling catalog rest interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/12
 */
public class CatalogProxyImpl implements ICatalogProxy {

    /**
     * Log serve.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogProxyImpl.class);

    /**
     * Uri prefix of rest interface.
     */
    private static final String CATALOG_REST_URI_SERVICETEMPALTE = "/openoapi/catalog/v1/servicetemplates/";

    /**
     * Uri prefix of rest interface.
     */
    private static final String CATALOG_REST_URI_CSAR = "/openoapi/catalog/v1/csars/";

    /**
     * Operations field of uri.
     */
    private static final String URI_PATH_OPERATIONS = "operations";

    /**
     * Parameters field of uri.
     */
    private static final String URI_PATH_PARAMETERS = "parameters";

    /**
     * Nesting field of uri.
     */
    private static final String URI_PATH_NESTING = "nesting";

    /**
     * Identify for querying condition by node type.
     */
    private static final String URI_PATH_QUERYING_NODETYPEDS = "nodeTypeIds";

    /**
     * Forward flash in uri.
     */
    private static final String FORWARD_FLASH = "/";

    /**
     * Identify of input parameters.
     */
    private static final String TEMPLATE_INPUTS = "inputs";

    /**
     * Package state.
     */
    private static final String PACKAGE_STATE_ONBOARDED = "onBoarded";

    /**
     * Condition.
     */
    private static final String URI_PATH_QUERY_ONBOARDSTATE = "onBoardState";

    /**
     * Nodetemplates filed of uri.
     */
    private static final String URI_PATH_NODETEMPLATES = "/nodetemplates";

    /**
     * <br/>
     * 
     * @param templateId template UD
     * @param request http request
     * @return template parameters
     * @since GSO 0.5
     */
    @Override
    public List<CatalogParameterModel> getParamsByTemplateId(String templateId, HttpServletRequest request) {
        LOGGER.info("Get input parameters from catalog.");
        String url = new StringBuilder().append(CATALOG_REST_URI_SERVICETEMPALTE).append(templateId)
                .append(FORWARD_FLASH).append(URI_PATH_PARAMETERS).toString();

        Map<String, String> httpHeaders = new HashMap<>();
        RestfulResponse response = HttpUtil.get(url, httpHeaders, request);
        ResponseUtils.checkResonseAndThrowException(response, "query parameters of service template.");

        return ResponseUtils.getDataModelFromRsp(response.getResponseContent(), TEMPLATE_INPUTS,
                CatalogParameterModel.class);
    }

    /**
     * Query operation list of service template.<br/>
     * 
     * @param templateId id of service template.
     * @return operations
     * @since GSO 0.5
     */
    @Override
    public List<OperationModel> getOperationsByTemplateId(String templateId, HttpServletRequest request) {
        LOGGER.info("Get list of operations from catalog.");
        String url = new StringBuilder().append(CATALOG_REST_URI_SERVICETEMPALTE).append(templateId)
                .append(FORWARD_FLASH).append(URI_PATH_OPERATIONS).toString();

        Map<String, String> httpHeaders = new HashMap<>();
        RestfulResponse response = HttpUtil.get(url, httpHeaders, request);
        ResponseUtils.checkResonseAndThrowException(response, "query operation list of service template.");

        return ResponseUtils.getDataModelFromRspList(response.getResponseContent(),
                new TypeReference<List<OperationModel>>() {});
    }

    /**
     * Query nesting service template.<br/>
     * 
     * @param nodeTypeId id of node type
     * @return service template data
     * @since GSO 0.5
     */
    @Override
    public List<ServiceTemplateModel> getTemplateByNodeTypeId(String nodeTypeId, HttpServletRequest request) {
        LOGGER.info("Get basic information of template by node type ID from catalog.");
        String url = new StringBuilder().append(CATALOG_REST_URI_SERVICETEMPALTE).append(URI_PATH_NESTING).toString();
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(URI_PATH_QUERYING_NODETYPEDS, nodeTypeId);
        RestfulResponse response = HttpUtil.get(url, httpHeaders, request);
        ResponseUtils.checkResonseAndThrowException(response, "query service tempalte.");

        return ResponseUtils.getDataModelFromRspList(response.getResponseContent(),
                new TypeReference<List<ServiceTemplateModel>>() {});
    }

    /**
     * Delete GSAR package by ID.<br/>
     * 
     * @param csarId package ID
     * @param request http request
     * @since GSO 0.5
     */
    @Override
    public void deleteGsarPackage(String csarId, HttpServletRequest request) {
        LOGGER.info("Notify catalog to delete GSAR package.");
        String url = new StringBuilder().append(CATALOG_REST_URI_CSAR).append(csarId).toString();
        RestfulResponse response = HttpUtil.delete(url, request);
        ResponseUtils.checkResonseAndThrowException(response, "delete csar pacakge.");
    }

    /**
     * Update GSAR onBoard status.<br/>
     * 
     * @param csarId package ID
     * @param request http request
     * @since GSO 0.5
     */
    @Override
    public void updateGsarStatus(String csarId, HttpServletRequest request) {
        LOGGER.info("Notify catalog to set GSAR status.");
        String url = new StringBuilder().append(CATALOG_REST_URI_CSAR).append(csarId).append("?")
                .append(URI_PATH_QUERY_ONBOARDSTATE).append("=").append(PACKAGE_STATE_ONBOARDED).toString();
        Map<String, String> httpHeaders = new HashMap<>();
        RestfulResponse response = HttpUtil.put(url, httpHeaders, request);
        ResponseUtils.checkResonseAndThrowException(response, "update csar package state.");
    }

    /**
     * Get nodes of template.<br/>
     * 
     * @param templateId template ID
     * @param request http request
     * @return nodes data
     * @since GSO 0.5
     */
    @Override
    public List<NodeTemplateModel> getNodeTemplate(String templateId, HttpServletRequest request) {
        LOGGER.info("Get nodes of template from catalog. Template id is {}", templateId);
        String url = new StringBuilder().append(CATALOG_REST_URI_SERVICETEMPALTE).append(templateId)
                .append(URI_PATH_NODETEMPLATES).toString();
        Map<String, String> httpHeaders = new HashMap<>();
        RestfulResponse response = HttpUtil.get(url, httpHeaders, request);
        ResponseUtils.checkResonseAndThrowException(response, "query tempalte node.");

        return ResponseUtils.getDataModelFromRspList(response.getResponseContent(),
                new TypeReference<List<NodeTemplateModel>>() {});
    }

    /**
     * get service template by node type<br>
     * 
     * @param nodeType node type
     * @return service template
     * @since GSO 0.5
     */
    @Override
    public ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost) {
        // Step 1: Prepare url and method type
        String url = CommonConstant.CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL;
        String methodType = CommonConstant.MethodType.GET;

        // Step 2: Prepare the restful parameters and options
        LOGGER.info("node Type is {}", nodeType);
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("nodeTypeIds", nodeType);

        RestfulParametes restfulParameters = RestfulUtil.setRestfulParameters(null, queryParam);
        RestfulOptions options = RestfulUtil.setRestfulOptions(domainHost);

        // Step 4:Send the request and get response
        RestfulResponse rsp = RestfulUtil.getRemoteResponse(url, methodType, restfulParameters, options);
        LOGGER.info("response status is {}", rsp.getStatus());
        LOGGER.info("response content is {}", rsp.getResponseContent());
        JSONArray array = JSONArray.fromObject(rsp.getResponseContent());
        return JsonUtil.unMarshal(array.getString(0), ServiceTemplate.class);
    }
}
