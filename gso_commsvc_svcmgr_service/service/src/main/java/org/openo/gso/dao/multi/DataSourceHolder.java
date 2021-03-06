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

package org.openo.gso.dao.multi;

/**
 * Holder of data source.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/20
 */
public class DataSourceHolder {

    /**
     * Data sources.
     */
    private static final ThreadLocal<String> dataSources = new ThreadLocal<>();

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private DataSourceHolder() {
    }

    /**
     * Set data source.<br/>
     * 
     * @param dataBaseName DB name.
     * @since GSO 0.5
     */
    public static synchronized void setDataSource(String dataBaseName) {
        dataSources.set(dataBaseName);
    }

    /**
     * Get data source.<br/>
     * 
     * @return database name
     * @since GSO 0.5
     */
    public static synchronized String getDataSource() {
        return dataSources.get();
    }

    /**
     * Delete data source<br/>
     * 
     * @since GSO 0.5
     */
    public static synchronized void clearDataSource() {
        dataSources.remove();
    }
}
