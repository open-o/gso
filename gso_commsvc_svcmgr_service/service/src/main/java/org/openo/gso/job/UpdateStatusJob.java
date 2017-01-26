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

package org.openo.gso.job;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.collections.map.HashedMap;
import org.openo.gso.constant.CommonConstant;
import org.openo.gso.constant.Constant;
import org.openo.gso.dao.inf.IInventoryDao;
import org.openo.gso.dao.inf.IServiceModelDao;
import org.openo.gso.dao.inf.IServiceOperDao;
import org.openo.gso.dao.inf.IServiceSegmentDao;
import org.openo.gso.model.servicemo.InvServiceModel;
import org.openo.gso.model.servicemo.ServiceModel;
import org.openo.gso.model.servicemo.ServiceOperation;
import org.openo.gso.model.servicemo.ServiceSegmentOperation;
import org.openo.gso.util.convertor.DataConverter;
import org.openo.gso.util.service.SpringContextUtil;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Update status task.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/15
 */
public class UpdateStatusJob extends TimerTask {

    /**
     * Log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStatusJob.class);

    /**
     * Service model dao.
     */
    private IServiceModelDao svcModelDao = (IServiceModelDao)SpringContextUtil.getBeanById("serviceModelDao");

    /**
     * Service segment dao.
     */
    private IServiceSegmentDao segmentsDao = (IServiceSegmentDao)SpringContextUtil.getBeanById("serviceSegmentDao");

    /**
     * Service operation dao.
     */
    private IServiceOperDao serviceOperDao = (IServiceOperDao)SpringContextUtil.getBeanById("serviceOperDao");

    /**
     * Inventory Service dao.
     */
    private IInventoryDao invDao = (IInventoryDao)SpringContextUtil.getBeanById("inventoryDao");

    /**
     * Operation of service without segment not update for 5m
     */
    private static final long SERVICE_WITHOUT_SEG_NOT_UPDATE = 5 * 60 * 1000L;

    /**
     * Operation of service with segment not update for 2h.
     */
    private static final long SERVICE_WITH_SEG_NOT_UPDATE = 2 * 60 * 60 * 1000L;

    /**
     * The action to be performed by this timer task.<br/>
     * 
     * @since GSO 0.5
     */
    @Override
    public void run() {
        try {
            ValidateUtil.assertObjectNotNull(svcModelDao);
            ValidateUtil.assertObjectNotNull(segmentsDao);
            ValidateUtil.assertObjectNotNull(serviceOperDao);
            ValidateUtil.assertObjectNotNull(invDao);

            // Query service instances by service instance ID, which need to be updated status.
            List<ServiceModel> svcModels = svcModelDao.queryServiceByStatus(CommonConstant.Status.PROCESSING);
            if(CollectionUtils.isEmpty(svcModels)) {
                LOGGER.info("There is no service instance which need to update status.");
                return;
            }

            List<String> svcIds = getSvcIds(svcModels);

            // Query operation details of service segments by service instance ID.
            List<ServiceSegmentOperation> segmentOpers = segmentsDao.querySegmentOperByIds(svcIds);
            if(CollectionUtils.isEmpty(segmentOpers)) {
                // Update service status when there is no segments operations
                // At the same time service operation not update for 300s.
                dealSvcWithoutSegOpers(svcIds, svcModels);
                return;
            }

            // Update service instances status and processing by service segments
            updateData(svcModels, segmentOpers);

        } catch(Exception exception) {
            LOGGER.error("Fail to update status. {}", exception);
        }
    }

    /**
     * Get service instances id.<br/>
     * 
     * @param svcModels service instances
     * @return service instances id
     * @since GSO 0.5
     */
    private List<String> getSvcIds(List<ServiceModel> svcModels) {
        List<String> svcIds = new LinkedList<>();
        for(ServiceModel model : svcModels) {
            svcIds.add(model.getServiceId());
        }
        return svcIds;
    }

    /**
     * Get unnormal service instance which status is not updated for 5m.<br/>
     * 
     * @param svcOpers service operation
     * @param time that service operations not update
     * @return unnormal service instance operation
     * @since GSO 0.5
     */
    private List<ServiceOperation> getUnNormalService(List<ServiceOperation> svcOpers, long time) {
        List<ServiceOperation> unnormal = new LinkedList<>();
        for(ServiceOperation oper : svcOpers) {
            if((oper.getFinishedAt() - oper.getOperateAt()) > time) {
                unnormal.add(oper);
            }
        }

        return unnormal;
    }

    /**
     * Deal service instances which have no segment operations.<br/>
     * 
     * @param svcIds service instance ID.
     * @param svcModels service instances.
     * @since GSO 0.5
     */
    private void dealSvcWithoutSegOpers(List<String> svcIds, List<ServiceModel> svcModels) {
        List<String> unnormalSvcIds = new LinkedList<>();
        // Query unnormal service operation
        List<ServiceOperation> unnormalOper =
                getUnNormalService(serviceOperDao.queryOperByIds(svcIds), SERVICE_WITHOUT_SEG_NOT_UPDATE);
        for(ServiceOperation operation : unnormalOper) {
            operation.setFinishedAt(System.currentTimeMillis());
            operation.setResult(CommonConstant.Status.ERROR);
            operation.setReason(
                    "The progress of operation is still " + operation.getProgress() + "%, so end operation.");
            operation.setProgress(Integer.valueOf(CommonConstant.Progress.ONE_HUNDRED));
            unnormalSvcIds.add(operation.getServiceId());
        }
        // Update service instance operation
        serviceOperDao.batchUpdate(unnormalOper);

        // Assemble service instances which need to update
        List<InvServiceModel> invServices = new LinkedList<>();
        List<ServiceModel> unnormalServices = new LinkedList<>();
        for(ServiceModel service : svcModels) {
            if(unnormalSvcIds.contains(service.getServiceId())) {
                service.setStatus(CommonConstant.Status.ERROR);
                unnormalServices.add(service);
                invServices.add(DataConverter.convertToInvData(service));
            }
        }

        // Update inventory service status
        invDao.batchUpdate(invServices);
        // Update service instance status
        svcModelDao.batchUpdate(unnormalServices);
    }

    /**
     * Update service and operation data.<br/>
     * 
     * @param services service instances
     * @param segmentOpers service operations
     * @since GSO 0.5
     */
    private void updateData(List<ServiceModel> services, List<ServiceSegmentOperation> segmentOpers) {
        String serviceId;
        Map<String, List<ServiceSegmentOperation>> svcSegMaybeNormal = new HashedMap();
        Map<String, ServiceSegmentOperation> svcSegError = new HashedMap();

        // 1. Sort segments by status
        for(ServiceSegmentOperation segOper : segmentOpers) {
            serviceId = segOper.getServiceId();
            // Look for error segments.
            if(svcSegError.containsKey(serviceId)) {
                continue;
            }
            if(segOper.getStatus().equals(CommonConstant.Status.ERROR)) {
                svcSegError.put(serviceId, segOper);
                continue;
            }

            // Storage maybe normal service segments
            // Maybe there are segments which progress not update for a long time
            if(svcSegMaybeNormal.containsKey(serviceId)) {
                svcSegMaybeNormal.get(serviceId).add(segOper);
            } else {
                List<ServiceSegmentOperation> segOpersLst = new LinkedList<>();
                segOpersLst.add(segOper);
                svcSegMaybeNormal.put(serviceId, segOpersLst);
            }
        }

        // 2. Sort service instance.
        // Service instance without service segments
        List<ServiceModel> svcWithoutSegs = new LinkedList<>();
        // Service instance with error service segment
        List<ServiceModel> svcWithErrorSegs = new LinkedList<>();
        // service instance which status maybe is normal
        List<ServiceModel> svcMaybeNormal = new LinkedList<>();
        for(ServiceModel model : services) {
            serviceId = model.getServiceId();
            if(svcSegError.containsKey(serviceId)) {
                model.setStatus(CommonConstant.Status.ERROR);
                svcWithErrorSegs.add(model);
            } else if(svcSegMaybeNormal.containsKey(serviceId)) {
                svcMaybeNormal.add(model);
            } else {
                svcWithoutSegs.add(model);
            }
        }

        // 3. Update service without segment operations
        dealSvcWithoutSegOpers(getSvcIds(svcWithoutSegs), svcWithErrorSegs);

        // 4. Update service status because there is a segment which status is error
        dealSvcWithErrorSeg(svcWithErrorSegs, svcSegError);

        // 5. Calculate service progress by the progress of service segments.
        calcServiceProgess(svcMaybeNormal, svcSegMaybeNormal);
    }

    /**
     * Update service status, which segment status is error.<br/>
     * 
     * @param svcWithErrorSegs service instances
     * @param svcSegError service segments
     * @since GSO 0.5
     */
    private void dealSvcWithErrorSeg(List<ServiceModel> svcWithErrorSegs,
            Map<String, ServiceSegmentOperation> svcSegError) {
        if(CollectionUtils.isEmpty(svcWithErrorSegs)) {
            LOGGER.info("There is no service instance to update.");
            return;
        }

        // 1. Update service operations
        List<ServiceOperation> svcOpers = serviceOperDao.queryOperByIds(getSvcIds(svcWithErrorSegs));
        for(ServiceOperation operation : svcOpers) {
            ServiceSegmentOperation segOper = svcSegError.get(operation.getServiceId());
            if(null == segOper) {
                continue;
            }

            operation.setResult(CommonConstant.Status.ERROR);
            operation.setFinishedAt(System.currentTimeMillis());
            operation.setProgress(Integer.valueOf(CommonConstant.Progress.ONE_HUNDRED));
            // need to confirm if it is reason item.
            operation.setReason(segOper.getStatusDescription());
        }
        serviceOperDao.batchUpdate(svcOpers);

        // 2. Update inventory service instances
        List<InvServiceModel> invServices = new LinkedList<>();
        for(ServiceModel model : svcWithErrorSegs) {
            invServices.add(DataConverter.convertToInvData(model));
        }
        invDao.batchUpdate(invServices);

        // 3. Update service instances
        svcModelDao.batchUpdate(svcWithErrorSegs);
    }

    /**
     * Calculate service operation progress and status.<br/>
     * 
     * @param services service instances
     * @param svcSegmentOpers service segment operations
     * @since GSO 0.5
     */
    private void calcServiceProgess(List<ServiceModel> services,
            Map<String, List<ServiceSegmentOperation>> svcSegmentOpers) {
        if(CollectionUtils.isEmpty(services)) {
            LOGGER.info("There is no service instance which need to calculate progress.");
            return;
        }

        // 1. Get service operations
        List<ServiceOperation> svcOperations = serviceOperDao.queryOperByIds(getSvcIds(services));
        Map<String, ServiceOperation> svcOperMap = new HashedMap();
        for(ServiceOperation oper : svcOperations) {
            svcOperMap.put(oper.getServiceId(), oper);
        }

        List<String> delServiceIds = new LinkedList<>();

        // 2. Calculate progress and status
        int progress;
        ServiceOperation serviceOper;
        List<ServiceSegmentOperation> segOperLst;
        List<InvServiceModel> invServices = new LinkedList<>();
        List<ServiceModel> updateSvc = new LinkedList<>();
        List<ServiceOperation> updateSvcOper = new LinkedList<>();
        for(ServiceModel service : services) {
            segOperLst = svcSegmentOpers.get(service.getServiceId());
            serviceOper = svcOperMap.get(service.getServiceId());
            // all service segments finish operation.
            if(isSvcFinished(segOperLst, service.getSegmentNumber())) {

                if(serviceOper.getOperation().equals(Constant.OPERATION_DELETE)) {
                    delServiceIds.add(service.getServiceId());
                } else {
                    service.setStatus(CommonConstant.Status.FINISHED);
                    updateSvc.add(service);
                    invServices.add(DataConverter.convertToInvData(service));
                }

                serviceOper.setProgress(Integer.valueOf(CommonConstant.Progress.ONE_HUNDRED));
                serviceOper.setFinishedAt(System.currentTimeMillis());
                updateSvcOper.add(serviceOper);
                continue;
            }

            // calculate progress
            progress = calculateProgerss(segOperLst, service.getSegmentNumber());
            if(serviceOper.getProgress() == progress) {
                // Not update for a long time, think as operation fail.
                if((serviceOper.getFinishedAt() - serviceOper.getOperateAt()) > SERVICE_WITH_SEG_NOT_UPDATE) {
                    service.setStatus(CommonConstant.Status.ERROR);
                    updateSvc.add(service);
                    invServices.add(DataConverter.convertToInvData(service));

                    serviceOper.setProgress(Integer.valueOf(CommonConstant.Progress.ONE_HUNDRED));
                    serviceOper.setReason("Not update status for 2 hours.");
                    serviceOper.setFinishedAt(System.currentTimeMillis());
                    updateSvcOper.add(serviceOper);
                }
            } else {
                // Update progress according to real progress
                serviceOper.setProgress(progress);
                serviceOper.setFinishedAt(System.currentTimeMillis());
                updateSvcOper.add(serviceOper);
            }
        }

        // 3. Update service operations
        if(CollectionUtils.isEmpty(updateSvcOper)) {
            serviceOperDao.batchUpdate(updateSvcOper);
        }

        // 4. Update service status
        if(CollectionUtils.isEmpty(updateSvc)) {
            invDao.batchUpdate(invServices);
            svcModelDao.batchUpdate(updateSvc);
        }

        // 5. Delete service instances which are being deleted
        if(CollectionUtils.isEmpty(delServiceIds)) {
            svcModelDao.batchDelete(delServiceIds);
            invDao.batchDelete(delServiceIds);
        }
    }

    /**
     * Judge if service operation is finished.<br/>
     * 
     * @param segOperLst service segment operations
     * @param allSegNum the number of all service segments
     * @return boolean if the status of all service segments are finished, return true
     * @since GSO 0.5
     */
    private boolean isSvcFinished(List<ServiceSegmentOperation> segOperLst, int allSegNum) {
        if(segOperLst.size() < allSegNum) {
            return false;
        }

        for(ServiceSegmentOperation oper : segOperLst) {
            if(!oper.getStatus().equals(CommonConstant.Status.FINISHED)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculate progress.<br/>
     * 
     * @param segOperLst service segment operations
     * @param allSegNum the number of all service segments
     * @return progress
     * @since GSO 0.5
     */
    private int calculateProgerss(List<ServiceSegmentOperation> segOperLst, int allSegNum) {
        int progress = 0;
        for(ServiceSegmentOperation oper : segOperLst) {
            progress += oper.getProgress();
        }
        return progress / allSegNum;
    }
}
