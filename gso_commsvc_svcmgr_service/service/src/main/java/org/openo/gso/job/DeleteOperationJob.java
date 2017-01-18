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

import java.util.TimerTask;

import org.openo.gso.dao.inf.IServiceOperDao;
import org.openo.gso.util.service.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delete history operation task.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/15
 */
public class DeleteOperationJob extends TimerTask {

    /**
     * Log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteOperationJob.class);

    /**
     * The action to be performed by this timer task.<br/>
     * 
     * @since GSO 0.5
     */
    @Override
    public void run() {
        try {

            IServiceOperDao operDao = (IServiceOperDao)SpringContextUtil.getBeanById("serviceOperDao");
            operDao.deleteHistory();
        } catch(Exception exception) {
            LOGGER.error("Delete operation job exception. {}", exception);
        }
    }

}
