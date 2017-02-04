/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.model;

import java.util.HashMap;
import java.util.Map;

import org.openo.gso.servicegateway.constant.FieldConstant;

/**
 * operation model
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.6
 */
public class OperationModel {

    private String operationId = "";

    private String userId = "";

    private String operationContent = "";

    private String result = "processing";

    private String reason = "";

    private String progress = "0";

    private String startTime = "";

    private String finishedTime = "";

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
     * @return Returns the progress.
     */
    public String getProgress() {
        return progress;
    }

    /**
     * @param progress The progress to set.
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }

    /**
     * @return Returns the startTime.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The startTime to set.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Returns the finishedTime.
     */
    public String getFinishedTime() {
        return finishedTime;
    }

    /**
     * @param finishedTime The finishedTime to set.
     */
    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**
     * convert to result map
     * <br>
     * 
     * @return
     * @since GSO 0.5
     */
    public Map<String, Map<String, String>> toResultMap() {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        Map<String, String> operationRst = new HashMap<>();
        operationRst.put(FieldConstant.QueryOperation.FIELD_OPERATIONID, operationId);
        operationRst.put(FieldConstant.QueryOperation.FIELD_USERID, userId);
        operationRst.put(FieldConstant.QueryOperation.FIELD_OPERATIONCONTENT, operationContent);
        operationRst.put(FieldConstant.QueryOperation.FIELD_RESULT, result);
        operationRst.put(FieldConstant.QueryOperation.FIELD_REASON, reason);
        operationRst.put(FieldConstant.QueryOperation.FIELD_PROGRESS, progress);
        resultMap.put(FieldConstant.QueryOperation.FIELD_OPERATION, operationRst);
        return resultMap;
    }
}
