/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.gso.commsvc.common.constant;

/**
 * Restful Request Results <br>
 * 
 * @author
 * @since 2016-6-15
 * @version GSO 0.5
 */
public class Constant {

    /**
     * service file Path:etc
     */
    public static final String FILE_PATH_ETC = "etc";

    /**
     * service file Path:register
     */
    public static final String FILE_PATH_REGISTER = "register";

    /**
     * service file Path:file name
     */
    public static final String FILE_PATH_JSON = "service.json";
    
    /**
     * the head type for Restful
     */
    public static final String HEAD_ERMAP_TYPE = "Content-Type";

    /**
     * the head value for Restful
     */
    public static final String HEAD_ERMAP_VALUE = "application/json;charset=UTF-8";
    
    /**
     * the URL for Register service to the M-service Bus
     */
    public static final String M_BUS_REGISTER_URL = "/openoapi/microservices/v1/services";

    /**
     * the IP key for the service file
     */
    public static final String SERVICE_KEY_IP = "getInputIP";

    /**
     * constant:0
     */
    public static final int ZERO = 0;

    private Constant() {
    }
}
