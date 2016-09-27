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

package org.openo.gso.service.inf;

import java.util.Map;

import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.model.drivermo.NsProgressStatus;

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
     * interface of deleting the sub-service
     * 
     * @param nodeType type of the node instance
     * @param instanceId id of the node instance
     * @return result of the delete operation
     * @throws ApplicationException when fail to delete the sub-service
     * @since GSO 0.5
     */
    String delete(String nodeType, String instanceId) throws ApplicationException;

    /**
     * <br>
     * create the network service
     * 
     * @param templateId serviceTemplate id
     * @param paramMap map of the input parameters
     * @return result of the creating
     * @throws ApplicationException when fail to creat the network service
     * @since GSO 0.5
     */
    String createNS(String templateId, Map<String, String> paramMap) throws ApplicationException;

    /**
     * <br>
     * instantiate the network service
     * 
     * @param instanceId id of the node instance
     * @param mapParams input parameters
     * @return jobId of the instantiate operation
     * @throws ApplicationException when fail to instantiate
     * @since GSO 0.5
     */
    String instantiateNS(String instanceId, Map<String, String> mapParams) throws ApplicationException;

    /**
     * <br>
     * query the progress of the instantiate operation
     * 
     * @param jobId job id
     * @return progress detail
     * @throws ApplicationException when fail to query the progress
     * @since GSO 0.5
     */
    NsProgressStatus getNsProgress(String jobId) throws ApplicationException;

    /**
     * <br>
     * set the node type
     * 
     * @param jobId type of the node
     * @since GSO 0.5
     */
    void setNodeType(String jobId);
}
