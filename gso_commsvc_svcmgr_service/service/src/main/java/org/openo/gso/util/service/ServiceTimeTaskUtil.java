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

package org.openo.gso.util.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openo.gso.job.DeleteOperationJob;
import org.openo.gso.job.UpdateStatusJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceTimeTaskUtil {

    /**
     * Log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTimeTaskUtil.class);

    /**
     * Executor service.
     */
    private static final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(2);

    /**
     * Delay 30s to do next update operation after finishing current operation.
     */
    private static final long UPDATE_OPERATION_DELAY_TIME = 30;

    /**
     * Delay 30s to do next delete operation after finishing current operation.
     */
    private static final long DELETE_OPERATION_DELAY_TIME = 30;

    /**
     * Delay 7 * 24 * 3600 to delete history operations.
     */
    private static final long DELETE_HISTORY_OPERATION_DELAY = 7 * 24 * 3600L;

    /**
     * Startup task.<br/>
     * 
     * @since GSO 0.5
     */
    public static void startUpTask() {

        // Time task to update status
        // Delay 30 seconds to trigger updating task
        // After one operation is finished, delay 30s to do next operation.
        schedule.scheduleWithFixedDelay(new UpdateStatusJob(), UPDATE_OPERATION_DELAY_TIME, UPDATE_OPERATION_DELAY_TIME,
                TimeUnit.SECONDS);

        // Time task to delete history operations.
        // Delay 30 seconds to trigger delete task, period is 7*24*3600
        schedule.scheduleAtFixedRate(new DeleteOperationJob(), DELETE_OPERATION_DELAY_TIME,
                DELETE_HISTORY_OPERATION_DELAY, TimeUnit.SECONDS);
    }

    /**
     * End task.<br/>
     * 
     * @since GSO 0.5
     */
    public static void endTask() {
        schedule.shutdown();
    }
}
