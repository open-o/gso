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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.util.uuid.UuidUtils;

/**
 * Operation service instance class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/12
 */
public class ServiceOperation {

    /**
     * Service instance ID.
     */
    @JsonIgnore
    private String serviceId;

    /**
     * Operation ID.
     */
    private String operationId;

    /**
     * Operation Type, Create|Delete.
     */
    private String operation;

    /**
     * Operation result. finished|error|processing
     */
    private String result;

    /**
     * Record failure reason of operation.
     */
    private String reason;

    /**
     * Operation user ID.
     */
    private String userId;

    /**
     * The status detail of current operation which is being executing.
     */
    private String operationContent;

    /**
     * Operation progress.
     */
    private int progress;

    /**
     * Time that it starts to execute operation.
     */
    private long operateAt;

    /**
     * Time that it finished executing operation.
     */
    private long finishedAt;

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @param serviceId service instance ID
     * @param operation operation type, create|delete
     * @param userId user ID
     * @since GSO 0.5
     */
    public ServiceOperation(String serviceId, String operation, String userId) {
        this.serviceId = serviceId;
        this.operationId = UuidUtils.createUuid();
        this.operation = operation;
        this.result = CommonConstant.Status.PROCESSING;
        this.userId = userId;
        this.operationContent = "";
        this.progress = 0;
        this.operateAt = System.currentTimeMillis();
        this.finishedAt = System.currentTimeMillis();
    }

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    public ServiceOperation() {
        super();
    }

    /**
     * @return Returns the serviceId.
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId The serviceId to set.
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return Returns the operationId.
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId The operationId to set.
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return Returns the operationType.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation The operation to set.
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return Returns the userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The userId to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the result.
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result The result to set.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return Returns the operationContent.
     */
    public String getOperationContent() {
        return operationContent;
    }

    /**
     * @param operationContent The operationContent to set.
     */
    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    /**
     * @return Returns the progress.
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress The progress to set.
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason The reason to set.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Returns the operateAt.
     */
    public long getOperateAt() {
        return operateAt;
    }

    /**
     * @param operateAt The operateAt to set.
     */
    public void setOperateAt(long operateAt) {
        this.operateAt = operateAt;
    }

    /**
     * @return Returns the finishedAt.
     */
    public long getFinishedAt() {
        return finishedAt;
    }

    /**
     * @param finishedAt The finishedAt to set.
     */
    public void setFinishedAt(long finishedAt) {
        this.finishedAt = finishedAt;
    }

    /**
     * Add for log.<br/>
     * 
     * @return model data
     * @since GSO 0.5
     */
    @Override
    public String toString() {
        return "ServiceOperation [serviceId=" + serviceId + ", operationId=" + operationId + ", operation=" + operation
                + ", result=" + result + ", reason=" + reason + ", userId=" + userId + ", operationContent="
                + operationContent + ", progress=" + progress + ", operateAt=" + operateAt + ", finishedAt="
                + finishedAt + "]";
    }

}
