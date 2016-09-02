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

package org.openo.gso.servicemgr.restproxy.inf;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.remoteservice.exception.ServiceException;

/**
 * Interface for interaction with wso2.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public interface IWsoProxy {

    /**
     * Start bepl workflow.<br/>
     * 
     * @param sendBody content of request
     * @param request http request
     * @return response content
     * @throws ServiceException when wso2 fails to start work flow.
     * @since GSO 0.5
     */
    String startWorkFlow(Object sendBody, HttpServletRequest request) throws ServiceException;
}
