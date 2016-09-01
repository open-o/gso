/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
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
package org.openo.gso.servicemgr.activator;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.util.inf.SystemEnvVariables;
import org.openo.gso.commsvc.common.constant.Constant;
import org.openo.gso.commsvc.common.register.RegisterUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Start <br/>
 * 
 * @author
 * @since GSO 1.0, 2016-8-10
 */
public class Activator implements BundleActivator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Activator.class);

    /**
     * start operation by spring <br/>
     * 
     * @param context
     *            service context
     * @author
     * @since GSO 1.0, 2016-8-10
     */
    public void start(BundleContext context) throws Exception {
        // register restful to M-Bus when starting service
        registerService();
    }

    /**
     * register the service to M-Bus<br/>
     * 
     * @author
     * @since GSO 1.0, 2016-8-10
     */
    private void registerService() {
        // get the jsonString form the service file
        String root = SystemEnvVariables.getinstance().getAppRoot();
        String serviceFilePath = root + File.separator + Constant.FILE_PATH_ETC + File.separator
                + Constant.FILE_PATH_REGISTER + File.separator + Constant.FILE_PATH_JSON;
        String jsonInfo = RegisterUtil.readFile(serviceFilePath);

        // check the jsonInfo
        if (StringUtils.isEmpty(jsonInfo)) {
            LOGGER.error("GSO ReadFile  fail: jsonInfo is null. the serviceFilePath=" + serviceFilePath);
            return;
        }

        // register service by the jsonInfo
        RegisterUtil.registerService(jsonInfo);
    }

    /**
     * stop operation by spring <br/>
     * 
     * @param context
     *            service context
     * @author
     * @since GSO 1.0, 2016-8-10
     */
    public void stop(BundleContext context) throws Exception {
        // no operation
    }
}