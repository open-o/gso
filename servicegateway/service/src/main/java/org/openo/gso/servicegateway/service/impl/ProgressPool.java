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

package org.openo.gso.servicegateway.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.servicegateway.constant.Constant;
import org.openo.gso.servicegateway.constant.FieldConstant;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.openo.gso.servicegateway.model.EnumServiceType;
import org.openo.gso.servicegateway.model.OperationModel;
import org.openo.gso.servicegateway.util.http.HttpUtil;
import org.openo.gso.servicegateway.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * progress pool.
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.6
 */
public class ProgressPool {

    /**
     * Log service.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressPool.class);

    /**
     * operation map
     */
    private Map<String, OperationModel> operationMap = new HashMap<String, OperationModel>();

    /**
     * single instance
     */
    private static ProgressPool instance;

    /**
     * time out: 1hour
     */
    private static final int TIME_OUT = 3600000;

    /**
     * time sleep: 10 second
     */
    private static final int TIME_SLEEP = 10000;
    
    /**
     * release data time:1 day
     */
    private static final int TIME_OPERATION_RELEASE = 3600 * 24 * 1000;

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private ProgressPool() {

    }

    /**
     * <br>
     * 
     * @return
     * @since GSO 0.5
     */
    public static synchronized ProgressPool getInstance() {
        if(null == instance) {
            instance = new ProgressPool();
        }
        return instance;
    }

    /**
     * delete service
     * <br>
     * 
     * @param operationId
     * @param serviceId
     * @param deleteUri
     * @param queryJobUri
     * @since GSO 0.5
     */
    public void dealCreateProgress(final EnumServiceType serviceType, final String operationId,
            final String queryJobUri) {

        Thread deleteThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if(serviceType == EnumServiceType.GSO) {
                    dealGSOProgress(operationId, queryJobUri);
                } else {
                    dealNonGSOCreateProgress(operationId, queryJobUri);
                }
            }
        });
        deleteThread.start();
    }

    /**
     * delete gso service in background
     * <br>
     * 
     * @param operationId
     * @param serviceId
     * @param deleteUri
     * @param queryJobUri
     * @since GSO 0.5
     */
    private void dealNonGSOCreateProgress(final String operationId, final String queryJobUri) {
        OperationModel model = addNewOpertaionModel(operationId);
        try {
            int timeOut = 0;
            while(timeOut <= TIME_OUT) {
                RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
                if(resp.getStatus() == HttpCode.RESPOND_ACCEPTED || resp.getStatus() == HttpCode.RESPOND_OK
                        || resp.getStatus() == HttpCode.CREATED_OK) {
                    Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
                    Map<String, Object> responseDescriptor =
                            (Map<String, Object>)rspBody.get(FieldConstant.QueryJob.FIELD_RESPONSEDESCRIPTOR);
                    model.setProgress((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_PROGRESS));
                    // set the status as processing. only after deleted. it could be finished
                    String status = (String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUS);
                    model.setResult(status);
                    model.setOperationContent(
                            (String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
                    model.setReason((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
                    if(FieldConstant.QueryJob.STATUS_FINISHED.equals(status)
                            || FieldConstant.QueryJob.STATUS_ERROR.equals(status)) {
                        break;
                    }
                    try {
                        Thread.sleep(TIME_SLEEP);
                        timeOut = timeOut + TIME_SLEEP;
                    } catch(InterruptedException e) {
                        LOGGER.info("time sleep error");
                    }

                } else {
                    break;
                }
            }

        } catch(ServiceException e) {
            LOGGER.info("query operation failed", e);
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason("query operation inner error");
        }
    }

    /**
     * delete service
     * <br>
     * 
     * @param operationId
     * @param serviceId
     * @param deleteUri
     * @param queryJobUri
     * @since GSO 0.5
     */
    public void dealDeleteProgress(final EnumServiceType serviceType, final String operationId, final String serviceId,
            final String deleteUri, final String queryJobUri) {

        Thread deleteThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if(serviceType == EnumServiceType.GSO) {
                    dealGSOProgress(operationId, queryJobUri);
                } else {
                    dealNonGSODeleteProgress(operationId, serviceId, deleteUri, queryJobUri);
                }
            }
        });
        deleteThread.start();
    }

    /**
     * delete service in background
     * <br>
     * 
     * @param operationId
     * @param serviceId
     * @param deleteUri
     * @param queryJobUri
     * @since GSO 0.5
     */
    private void dealNonGSODeleteProgress(final String operationId, final String serviceId, final String deleteUri,
            final String queryJobUri) {
        OperationModel model = addNewOpertaionModel(operationId);
        try {
            int timeOut = 0;
            while(timeOut <= TIME_OUT) {
                RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
                if(resp.getStatus() == HttpCode.RESPOND_ACCEPTED || resp.getStatus() == HttpCode.RESPOND_OK
                        || resp.getStatus() == HttpCode.CREATED_OK) {

                    Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
                    Map<String, Object> responseDescriptor =
                            (Map<String, Object>)rspBody.get(FieldConstant.QueryJob.FIELD_RESPONSEDESCRIPTOR);
                    model.setProgress((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_PROGRESS));
                    // set the status as processing. only after deleted. it could be finished
                    String status = (String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUS);
                    if(FieldConstant.QueryJob.STATUS_FINISHED.equals(status)) {
                        model.setResult(FieldConstant.QueryJob.STATUS_PROCESSING);
                    } else {
                        model.setResult(status);
                    }
                    model.setOperationContent(
                            (String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
                    model.setReason((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
                    // if finished, send delete msg
                    if(FieldConstant.QueryJob.STATUS_FINISHED.equals(status)) {
                        RestfulResponse restfulRsp =
                                RestfulFactory.getRestInstance("http").delete(deleteUri, getRestfulParameters(""));

                        if(resp.getStatus() == HttpCode.RESPOND_ACCEPTED || resp.getStatus() == HttpCode.RESPOND_OK
                                || resp.getStatus() == HttpCode.CREATED_OK) {
                            model.setResult(FieldConstant.QueryJob.STATUS_FINISHED);
                        } else {
                            model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
                            model.setOperationContent("delete service failed");
                            model.setReason("delete service failed");
                        }
                        break;
                    } else if(FieldConstant.QueryJob.STATUS_ERROR.equals(status)) {
                        break;
                    }
                    try {
                        Thread.sleep(TIME_SLEEP);
                        timeOut = timeOut + TIME_SLEEP;
                    } catch(InterruptedException e) {
                        LOGGER.info("time sleep error");
                    }

                } else {
                    model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
                    model.setOperationContent("query progress failed");
                    model.setReason("query progress failed");
                    break;
                }
            }

        } catch(ServiceException e) {
            model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
            model.setReason("query progress failed");
        }
    }

    /**
     * delete gso service in background
     * <br>
     * 
     * @param operationId
     * @param serviceId
     * @param deleteUri
     * @param queryJobUri
     * @since GSO 0.5
     */
    private void dealGSOProgress(final String operationId, final String queryJobUri) {
        OperationModel model = addNewOpertaionModel(operationId);
        try {
            int timeOut = 0;
            while(timeOut <= TIME_OUT) {
                RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
                if(resp.getStatus() == HttpCode.RESPOND_ACCEPTED || resp.getStatus() == HttpCode.RESPOND_OK
                        || resp.getStatus() == HttpCode.CREATED_OK) {
                    Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
                    Map<String, Object> responseDescriptor =
                            (Map<String, Object>)rspBody.get(FieldConstant.QueryOperation.FIELD_OPERATION);
                    model.setProgress((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_PROGRESS));
                    model.setUserId((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_USERID));
                    String result = (String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_RESULT);
                    model.setResult(result);
                    model.setOperationContent(
                            (String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_OPERATIONCONTENT));
                    model.setReason((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_REASON));
                    if(FieldConstant.QueryOperation.RESULT_FINISHED.equals(result)
                            || FieldConstant.QueryOperation.RESULT_ERROR.equals(result)) {
                        break;
                    }
                    try {
                        Thread.sleep(TIME_SLEEP);
                        timeOut = timeOut + TIME_SLEEP;
                    } catch(InterruptedException e) {
                        LOGGER.info("time sleep error");
                    }

                } else {
                    break;
                }
            }

        } catch(ServiceException e) {
            LOGGER.info("query operation failed", e);
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason("query operation inner error");
        }
    }

    /**
     * 
     * <br>
     * 
     * @param bodyData
     * @return
     * @since  GSO 0.5
     */
    private RestfulParametes getRestfulParameters(final String bodyData) {
        RestfulParametes param = new RestfulParametes();
        param.putHttpContextHeader(Constant.HEAD_ERMAP_TYPE, Constant.HEAD_ERMAP_VALUE);
        param.setRawData(bodyData);
        return param;
    }

    /**
     * add new operation
     * <br>
     * 
     * @param operationId
     * @since GSO 0.5
     */
    private synchronized OperationModel addNewOpertaionModel(String operationId) {
        releaseOldOperations();
        OperationModel model = new OperationModel();
        model.setOperationId(operationId);
        model.setProgress("0");
        model.setResult("processing");
        model.setStartTime(String.valueOf(new Date().getTime()));
        operationMap.put(operationId, model);
        return model;

    }

    /**
     * release old operations
     * <br>
     * 
     * @since GSO 0.5
     */
    private void releaseOldOperations() {
        Long curTime = new Date().getTime();
        Iterator<Map.Entry<String, OperationModel>> it = operationMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, OperationModel> entry = it.next();
            Long finishedTime = Long.valueOf(entry.getValue().getFinishedTime());
            // release the record which is 1 day atfter finished.
            if(curTime - finishedTime >= TIME_OPERATION_RELEASE) {
                it.remove();
            }
        }
    }

    /**
     * <br>
     * get operation model by operation id
     * 
     * @param operationId
     * @return
     * @since GSO 0.5
     */
    public OperationModel getOperation(String operationId) {
        return operationMap.get(operationId);
    }

}
