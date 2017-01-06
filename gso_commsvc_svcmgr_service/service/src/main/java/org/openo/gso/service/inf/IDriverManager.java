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

package org.openo.gso.service.inf;

import javax.servlet.http.HttpServletRequest;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;

/**
 * Interface to operate service.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/4
 */

public interface IDriverManager {

    /**
     * instantiate service <br/>
     * 
     * @param nsInstanceId uuid of service instance
     * @param httpRequest - http request
     * @param domain SDNO or NFVO
     * @return restful response
     * @throws ApplicationException when fail to instantiate network service
     * @since GSO 0.5
     */
    RestfulResponse instantiateNs(String nsInstanceId, HttpServletRequest httpRequest, String domain) throws ApplicationException;
    
    /**
     * terminate service instance.<br/>
     * 
     * @param nsInstanceId uuid of service instance
     * @param httpRequest http request
     * @param domain SDNO or NFVO
     * @return restful response
     * @throws ApplicationException when fail to terminate network service
     * @since GSO 0.5
     */
    RestfulResponse terminateNs(String nsInstanceId, HttpServletRequest httpRequest, String domain) throws ApplicationException;

    /**
     * create service<br>
     * 
     * @param servletReq
     * @param domain SDNO or NFVO
     * @return response
     * @throws ApplicationException when fail to create network service
     * @since  GSO 0.5
     */
    RestfulResponse createNs(HttpServletRequest servletReq, String domain) throws ApplicationException;

    /**
     * delete service<br>
     * 
     * @param nsInstanceId uuid of service instance
     * @param domain SDNO or NFVO
     * @return response
     * @throws ApplicationException when fail to delete network service
     * @since  GSO 0.5
     */
    RestfulResponse deleteNs(String nsInstanceId, String domain) throws ApplicationException;

    /**
     * query service job status<br>
     * 
     * @param jobId uuid for service job
     * @param domain SDNO or NFVO
     * @return response
     * @throws ApplicationException when fail to query service job status
     * @since  GSO 0.5
     */
    RestfulResponse getNsProgress(String jobId, String domain) throws ApplicationException;

}
