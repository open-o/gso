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
package org.openo.gso.commsvc.common.util;

import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.commsvc.common.exception.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ValidateUtil {


    /**
     * Log server.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private ValidateUtil() {

    }

    /**
     * Assert String parameter.<br/>
     * 
     * @param paramValue parameter data
     * @param name of parameter
     * @since GSO 0.5
     */
    public static void assertStringNotNull(String paramValue, String paramName) {
        if(StringUtils.hasLength(paramValue)) {
            return;
        }

        LOGGER.error(paramName + ": Parameter is null or empty.");
        throw new ApplicationException(HttpCode.BAD_REQUEST, paramName + ": Invalid parameter.");
    }

    /**
     * Assert object is null.<br/>
     * 
     * @param object data object
     * @since GSO 0.5
     */
    public static void assertObjectNotNull(Object object) {
        if(null == object) {
            LOGGER.error("Object is null.");
            throw new ApplicationException(HttpCode.BAD_REQUEST, "Object is null.");
        }

    }
}
