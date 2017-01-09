/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.model.servicemo;

/**
 * service segment operation<br>
 * <p>
 * </p>
 * @version     GSO 0.5  2017/1/9
 */
public class ServiceSegmentOperation {
    
    /**
     * id of the service segment
     */
    private String serviceSegmentId;
    
    /**
     * type of the service segment
     */
    private String serviceSegmentType;
    
    /**
     * uuid of the job
     */
    private String jobId;
    
    /**
     * staus of the segment
     */
    private String status;
    
    /**
     * process of the segment
     */
    private int process;
    
    /**
     * error code of the operation
     */
    private int errorCode;
    
    /**
     * status description 
     */
    private String statusDescription;
    
    /**
     * @return Returns the serviceSegmentId.
     */
    public String getServiceSegmentId() {
        return serviceSegmentId;
    }
    
    /**
     * @param serviceSegmentId The serviceSegmentId to set.
     */
    public void setServiceSegmentId(String serviceSegmentId) {
        this.serviceSegmentId = serviceSegmentId;
    }
    
    /**
     * @return Returns the serviceSegmentType.
     */
    public String getServiceSegmentType() {
        return serviceSegmentType;
    }
    
    /**
     * @param serviceSegmentType The serviceSegmentType to set.
     */
    public void setServiceSegmentType(String serviceSegmentType) {
        this.serviceSegmentType = serviceSegmentType;
    }
    
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
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return Returns the process.
     */
    public int getProcess() {
        return process;
    }
    
    /**
     * @param process The process to set.
     */
    public void setProcess(int process) {
        this.process = process;
    }
    
    /**
     * @return Returns the errorCode.
     */
    public int getErrorCode() {
        return errorCode;
    }
    
    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * @return Returns the statusDescription.
     */
    public String getStatusDescription() {
        return statusDescription;
    }
    
    /**
     * @param statusDescription The statusDescription to set.
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

}
