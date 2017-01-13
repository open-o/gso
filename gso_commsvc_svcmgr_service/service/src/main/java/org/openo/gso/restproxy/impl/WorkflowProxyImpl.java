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

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.restproxy.inf.IWorkflowProxy;
import org.openo.gso.util.http.HttpUtil;
import org.openo.gso.util.http.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for interface of interaction with workflow.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public class WorkflowProxyImpl implements IWorkflowProxy {

    /**
     * Log server.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowProxyImpl.class);

    /**
     * URI of workflow starting bpel.
     */
    private static final String WSO_URI = "/openoapi/wso2bpel/v1/process/instance";

    /**
     * Start bepl workflow.<br/>
     * 
     * @param sendBody content of request
     * @param request http request
     * @return response content
     * @throws ApplicationException when workflow fails to start work flow.
     * @since GSO 0.5
     */
    @Override
    public int startWorkFlow(Object sendBody, HttpServletRequest request) throws ApplicationException {
        LOGGER.info("Notify workflow to startup bpel workflow.");
        RestfulResponse response = HttpUtil.post(WSO_URI, sendBody, request);
        ResponseUtils.checkResonseAndThrowException(response, "start to bpel workflow.");

        return response.getStatus();
    }

}
