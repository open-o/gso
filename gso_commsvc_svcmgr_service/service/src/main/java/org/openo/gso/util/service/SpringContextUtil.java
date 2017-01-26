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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Get bean class by bean ID.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017/1/14
 */
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * Application context object;
     */
    private static ApplicationContext applicationContext;

    /**
     * Set application context object<br/>
     * 
     * @param arg0 context object
     * @throws BeansException when something is wrong
     * @since GSO 0.5
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0) {
        SpringContextUtil.applicationContext = arg0;
    }

    /**
     * Get Bean object by bean ID.<br/>
     * 
     * @param id bean ID
     * @return bean object
     * @since GSO 0.5
     */
    public static Object getBeanById(String id) {
        return applicationContext.getBean(id);
    }
}
