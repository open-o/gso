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

package org.openo.gso.model.drivermo;

import java.util.List;

/**
 * <br>
 * <p>
 * </p>
 * response model of query operation status
 * @author
 * @version     GSO 0.5  2016/9/3
 */
public class NsProgressStatus {

    String jobId;

    ResponseDescriptor responseDescriptor;

    List<ResponseDescriptor> responseHistoryList;

    
    /**
     * @return Returns the jobId.
     */
    public String getJobId() {
        return jobId;
    }

    
    /**
     * @param jobId The jobId to set.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    
    /**
     * @return Returns the responseDescriptor.
     */
    public ResponseDescriptor getResponseDescriptor() {
        return responseDescriptor;
    }


    
    /**
     * @param responseDescriptor The responseDescriptor to set.
     */
    public void setResponseDescriptor(ResponseDescriptor responseDescriptor) {
        this.responseDescriptor = responseDescriptor;
    }


    
    /**
     * @return Returns the responseHistoryList.
     */
    public List<ResponseDescriptor> getResponseHistoryList() {
        return responseHistoryList;
    }


    
    /**
     * @param responseHistoryList The responseHistoryList to set.
     */
    public void setResponseHistoryList(List<ResponseDescriptor> responseHistoryList) {
        this.responseHistoryList = responseHistoryList;
    }

    
}
