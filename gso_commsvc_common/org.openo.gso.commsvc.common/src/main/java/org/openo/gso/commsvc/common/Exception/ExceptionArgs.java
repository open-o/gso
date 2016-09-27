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

package org.openo.gso.commsvc.common.Exception;

/**
 * Exception description class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/26/
 */
public class ExceptionArgs {

    /**
     * Exception description.
     */
    private String description;

    /**
     * Exception reason.
     */
    private Object reason;

    public ExceptionArgs() {
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the reason.
     */
    public Object getReason() {
        return reason;
    }

    /**
     * @param reason The reason to set.
     */
    public void setReason(Object reason) {
        this.reason = reason;
    }
}
