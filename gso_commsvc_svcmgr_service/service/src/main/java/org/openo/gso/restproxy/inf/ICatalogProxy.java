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

package org.openo.gso.restproxy.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.catalogmo.CatalogParameterModel;
import org.openo.gso.model.catalogmo.NodeTemplateModel;
import org.openo.gso.model.catalogmo.OperationModel;
import org.openo.gso.model.catalogmo.ServiceTemplateModel;

/**
 * Interface to contact with catalog rest interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/12
 */
public interface ICatalogProxy {

    /**
     * Query input parameters of service template.<br/>
     * 
     * @param templateId id of service template
     * @param request http request
     * @return input parameters
     * @throws ApplicationException when catalog fails to query data.
     * @since GSO 0.5
     */
    List<CatalogParameterModel> getParamsByTemplateId(String templateId, HttpServletRequest request)
            throws ApplicationException;

    /**
     * Query operation list of service template.<br/>
     * 
     * @param templateId id of service template.
     * @param request http request
     * @return operations
     * @throws ApplicationException when catalog fails to query data.
     * @since GSO 0.5
     */
    List<OperationModel> getOperationsByTemplateId(String templateId, HttpServletRequest request)
            throws ApplicationException;

    /**
     * Query nesting service template.<br/>
     * 
     * @param nodeTypeId id of node type
     * @param request http request
     * @return service template data
     * @throws ApplicationException when catalog fails to query data.
     * @since GSO 0.5
     */
    List<ServiceTemplateModel> getTemplateByNodeTypeId(String nodeTypeId, HttpServletRequest request)
            throws ApplicationException;

    /**
     * Get nodes of template.<br/>
     * 
     * @param templateId template ID
     * @param request http request
     * @return nodes data
     * @throws ApplicationException when catalog fails to query data.
     * @since GSO 0.5
     */
    List<NodeTemplateModel> getNodeTemplate(String templateId, HttpServletRequest request) throws ApplicationException;

    /**
     * Delete GSAR package by ID.<br/>
     * 
     * @param csarId package ID
     * @param request http request
     * @throws ApplicationException when fail to delete
     * @since GSO 0.5
     */
    void deleteGsarPackage(String csarId, HttpServletRequest request) throws ApplicationException;

    /**
     * Update GSAR onBoard status.<br/>
     * 
     * @param csarId package ID
     * @param request http request
     * @throws ApplicationException when fail to update
     * @since GSO 0.5
     */
    void updateGsarStatus(String csarId, HttpServletRequest request) throws ApplicationException;
}
