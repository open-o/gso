/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.constant;

/**
 * <br>
 * <p>
 * </p>
 * identification of adapter exception
 * 
 * @author
 * @version GSO 0.5 2016/9/3
 */
public class DriverExceptionID {

    public static final String INVALID_PARAM = "Invalid parameter";

    public static final String INTERNAL_ERROR = "Internal error";

    public static final String FAILED_TO_SVCTMPL_CATALOGUE = "Failed to get service template from catalogue";

    public static final String INVALID_RESPONSE_FROM_INSTANTIATE_OPERATION = "Invalid response from instantiate operation";

    public static final String INVALID_RESPONSEE_FROM_CREATE_OPERATION = "Invalid response from create operation";

    public static final String FAIL_TO_INSTANTIATE_NS = "Fail to instantiate ns";

    public static final String FAIL_TO_CREATE_NS = "Fail to create ns";

    public static final String FAIL_TO_CREATE_GSO_NS = "Fail to create gso ns";

    private DriverExceptionID() {

    }

}
