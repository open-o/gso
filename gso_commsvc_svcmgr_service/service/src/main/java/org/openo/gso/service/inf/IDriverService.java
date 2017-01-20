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

import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.drivermo.ServiceTemplate;

/**
 * <br>
 * <p>
 * </p>
 * interface of service action
 * 
 * @version GSO 0.5 2016/9/3
 */
public interface IDriverService {

    /**
     * <br>
     * terminate the network service
     * 
     * @param paramMap parameters map
     * @return job id of the terminate operation
     * @throws ApplicationException when fail to delete the sub-service
     * @since GSO 0.5
     */
    RestfulResponse terminateNs(Map<String, Object> paramMap) throws ApplicationException;

    /**
     * <br>
     * delete the network service
     * @param paramMap map of the input parameters
     * @return response
     * @throws ApplicationException when fail to delete the network service
     * @since  GSO 0.5
     */
    RestfulResponse deleteNs(Map<String, Object> paramMap) throws ApplicationException;
    
    /**
     * <br>
     * create the network service
     * 
     * @param paramMap map of the input parameters
     * @return nsInstanceId
     * @throws ApplicationException when fail to creat the network service
     * @since GSO 0.5
     */
    RestfulResponse createNs(Map<String, Object> paramMap) throws ApplicationException;

    /**
     * <br>
     * instantiate the network service
     * 
     * @param mapParams input parameters
     * @return jobId of the instantiate operation
     * @throws ApplicationException when fail to instantiate
     * @since GSO 0.5
     */
    RestfulResponse instantiateNs(Map<String, Object> mapParams) throws ApplicationException;

    /**
     * <br>
     * query the progress of the instantiate operation
     * 
     * @param jobId job id
     * @param map paramters map
     * @return progress detail
     * @throws ApplicationException when fail to query the progress
     * @since GSO 0.5
     */
    RestfulResponse getNsProgress(String jobId, Map<String, Object> map) throws ApplicationException;

    /**
     * <br>
     * set the segment type
     * 
     * @param segmentType type of the node NFVO or SDNO
     * @since GSO 0.5
     */
    void setSegmentType(String segmentType);

    /**
     * get service template by node type<br>
     * 
     * @param nodeType node type
     * @return service template
     * @throws ApplicationException when fail to get service template
     * @since  GSO 0.5
     */
    ServiceTemplate getSvcTmplByNodeType(String nodeType, String domainHost) throws ApplicationException;

    /**
     * create gso service<br>
     * 
     * @param inputMap parameters map
     * @since  GSO 0.5
     */
    RestfulResponse createGsoNs(Map<String, Object> inputMap);
}
