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
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.servicegateway.common.CommonUtil;
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
    private Map<String, OperationModel> operationMap = new HashMap<>();

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
    public void dealCommonProgress(final EnumServiceType serviceType, final String operationId,
            final String queryJobUri) {

        Thread createThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if(serviceType == EnumServiceType.GSO) {
                    dealGSOProgress(operationId, queryJobUri);
                } else {
                    dealNonGSOCommonProgress(operationId, queryJobUri);
                }
            }
        });
        createThread.start();
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
    public void dealDeleteProgress(final EnumServiceType serviceType, final String operationId, final String deleteUri,
            final String queryJobUri) {

        Thread deleteThread = new Thread(new Runnable() {

            @Override
            public void run() {
                if(serviceType == EnumServiceType.GSO) {
                    dealGSOProgress(operationId, queryJobUri);
                } else {
                    dealNonGSODeleteProgress(operationId, deleteUri, queryJobUri);
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
    private void dealNonGSOCommonProgress(final String operationId, final String queryJobUri) {
        OperationModel model = addNewOpertaionModel(operationId);
        try {
            int timeOut = 0;
            while(timeOut <= TIME_OUT) {
                boolean needBreak = queryNonGSOCommonProgress(queryJobUri, model);
                if(needBreak) {
                    break;
                }
                sleep(model);
                timeOut = timeOut + TIME_SLEEP;
            }
            if(timeOut > TIME_OUT) {
                // if time out, set the result as failed.
                LOGGER.info("query the progress infomation timeout");
                model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
                model.setReason("query the progress infomation timeout");
            }
        } catch(ServiceException e) {
            LOGGER.info("query operation failed", e);
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason("query operation inner error");
        }
        model.setFinishedTime(String.valueOf(new Date().getTime()));
    }

    /**
     * <br>
     * 
     * @param queryJobUri the uri for query job
     * @param model the operation model
     * @return
     * @throws ServiceException
     * @since GSO Mercury Release
     */
    @SuppressWarnings("unchecked")
    private boolean queryNonGSOCommonProgress(final String queryJobUri, OperationModel model) throws ServiceException {
        boolean needBreak = false;
        LOGGER.info("query NS operation info start");
        RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
        CommonUtil.logTheResponseData("query NS operation info", resp);
        if(HttpCode.isSucess(resp.getStatus())) {
            Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
            // set the progress information to model
            Map<String, Object> responseDescriptor =
                    (Map<String, Object>)rspBody.get(FieldConstant.QueryJob.FIELD_RESPONSEDESCRIPTOR);
            model.setProgress((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_PROGRESS));
            // set the status as processing. only after deleted. it could be finished
            String status = (String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUS);
            model.setResult(status);
            model.setOperationContent((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
            // for nfvo/sdno, set the errorcode as fail reason
            model.setReason((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_ERRORCODE));
            // if finished or error ,stop to query the progress
            if(FieldConstant.QueryJob.STATUS_FINISHED.equals(status)
                    || FieldConstant.QueryJob.STATUS_ERROR.equals(status)) {
                needBreak = true;
            }

        } else {
            // if response error, set the result as failed.
            LOGGER.info("query the progress infomation error");
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason(resp.getResponseContent());
            needBreak = true;
        }
        return needBreak;
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
    private void dealNonGSODeleteProgress(final String operationId, final String deleteUri, final String queryJobUri) {
        OperationModel model = addNewOpertaionModel(operationId);
        try {
            int timeOut = 0;
            while(timeOut <= TIME_OUT) {
                boolean needBreak = queryNonGSODeleteProgress(deleteUri, queryJobUri, model);
                if(needBreak) {
                    // break out the loop
                    break;
                }
                sleep(model);
                timeOut = timeOut + TIME_SLEEP;
            }
            if(timeOut > TIME_OUT) {
                // if time out, set the result as failed.
                LOGGER.info("query the progress infomation timeout");
                model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
                model.setReason("query the progress infomation timeout");
            }
        } catch(ServiceException e) {
            LOGGER.info("query the progress infomation service exception", e);
            model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
            model.setReason("query progress failed");
        }
        model.setFinishedTime(String.valueOf(new Date().getTime()));
    }

    /**
     * <br>
     * 
     * @param deleteUri the delete Uri for delete the service
     * @param queryJobUri the query job uri
     * @param model the operation model
     * @return
     * @throws ServiceException
     * @since GSO Mercury Release
     */
    @SuppressWarnings("unchecked")
    private boolean queryNonGSODeleteProgress(final String deleteUri, final String queryJobUri, OperationModel model)
            throws ServiceException {
        LOGGER.info("query ns service job start.");
        RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
        CommonUtil.logTheResponseData("query ns service job", resp);
        boolean needBreak = false;
        if(HttpCode.isSucess(resp.getStatus())) {
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
            model.setOperationContent((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_STATUSDESCRIPTION));
            // put the error code as reason
            model.setReason((String)responseDescriptor.get(FieldConstant.QueryJob.FIELD_ERRORCODE));
            // if finished, send delete msg
            if(FieldConstant.QueryJob.STATUS_FINISHED.equals(status)) {
                LOGGER.info("start to delete service:" + deleteUri);
                RestfulResponse restfulRsp = HttpUtil.delete(deleteUri);
                CommonUtil.logTheResponseData("delete ns service", restfulRsp);
                if(HttpCode.isSucess(restfulRsp.getStatus())) {
                    model.setResult(FieldConstant.QueryJob.STATUS_FINISHED);
                } else {
                    model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
                    model.setOperationContent("delete service");
                    model.setReason("delete service failed");
                }
                needBreak = true;
            } else if(FieldConstant.QueryJob.STATUS_ERROR.equals(status)) {
                needBreak = true;
            }

        } else {
            model.setResult(FieldConstant.QueryJob.STATUS_ERROR);
            model.setOperationContent("query progress failed");
            model.setReason("query progress failed");
            needBreak = true;
        }
        return needBreak;
    }

    /**
     * sleep
     * <br>
     * 
     * @param model if sleep failed set the fail reason to model
     * @since GSO Mercury Release
     */
    private void sleep(OperationModel model) {
        try {
            Thread.sleep(TIME_SLEEP);

        } catch(InterruptedException e) {
            LOGGER.info("time sleep error");
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason("time sleep error");
            Thread.currentThread().interrupt();
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
                boolean needBreak = queryGSOProgress(queryJobUri, model);
                if(needBreak) {
                    break;
                }
                sleep(model);
                timeOut = timeOut + TIME_SLEEP;
            }
            if(timeOut >= TIME_OUT) {
                // if time out, set the result as failed.
                LOGGER.info("query the progress infomation timeout");
                model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
                model.setReason("query the progress infomation timeout");
            }
        } catch(ServiceException e) {
            // if exception got , set the result as failed.
            LOGGER.info("query operation failed", e);
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason("query operation inner error");
        }
        model.setFinishedTime(String.valueOf(new Date().getTime()));
    }

    /**
     * <br>
     * 
     * @param queryJobUri the uri for query gso progress
     * @param model the operation model
     * @return is break needed
     * @throws ServiceException
     * @since GSO Mercury Release
     */
    @SuppressWarnings("unchecked")
    private boolean queryGSOProgress(final String queryJobUri, OperationModel model) throws ServiceException {
        boolean needBreak = false;
        LOGGER.info("query GSO operation info start");
        RestfulResponse resp = HttpUtil.get(queryJobUri, new HashMap<String, String>());
        CommonUtil.logTheResponseData("query GSO operation info", resp);
        if(HttpCode.isSucess(resp.getStatus())) {
            Map<String, Object> rspBody = JsonUtil.unMarshal(resp.getResponseContent(), Map.class);
            Map<String, Object> responseDescriptor =
                    (Map<String, Object>)rspBody.get(FieldConstant.QueryOperation.FIELD_OPERATION);
            // set the result to model
            model.setProgress((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_PROGRESS));
            model.setUserId((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_USERID));
            String result = (String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_RESULT);
            model.setResult(result);
            model.setOperationContent(
                    (String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_OPERATIONCONTENT));
            model.setReason((String)responseDescriptor.get(FieldConstant.QueryOperation.FIELD_REASON));

            // if failed or error ,stop to query the progress
            if(FieldConstant.QueryOperation.RESULT_FINISHED.equals(result)
                    || FieldConstant.QueryOperation.RESULT_ERROR.equals(result)) {
                needBreak = true;
            }
        } else {
            // if response error, set the result as failed.
            LOGGER.info("query the progress infomation error");
            model.setResult(FieldConstant.QueryOperation.RESULT_ERROR);
            model.setReason(resp.getResponseContent());
            needBreak = true;
        }
        return needBreak;
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
        model.setResult(FieldConstant.QueryOperation.RESULT_PROCESSING);
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
            if("".equals(entry.getValue().getStartTime()) || "".equals(entry.getValue().getFinishedTime())) {
                continue;
            }
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
