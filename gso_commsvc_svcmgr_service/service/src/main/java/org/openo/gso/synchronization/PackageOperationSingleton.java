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

package org.openo.gso.synchronization;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to control the operation of csar package every time.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/23
 */
public class PackageOperationSingleton {

    /**
     * Csar is being used to create service instance.
     */
    private Set<String> beingUsedCsar = new HashSet<String>();

    /**
     * It is deleting csar package.
     */
    private Set<String> beingDeletedCsar = new HashSet<String>();

    /**
     * Operation instance.
     */
    private static PackageOperationSingleton INSTANCE = new PackageOperationSingleton();

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private PackageOperationSingleton() {

    }

    /**
     * Get instance object.<br/>
     * 
     * @return
     * @since GSO 0.5
     */
    public static PackageOperationSingleton getInstance() {
        return PackageOperationSingleton.INSTANCE;
    }

    /**
     * Cache csar ID which is being used.<br/>
     * 
     * @param csarId template ID
     * @since GSO 0.5
     */
    public synchronized void addBeingUsedCsarIds(String csarId) {
        this.beingUsedCsar.add(csarId);
    }

    /**
     * Delete csar from cache.<br/>
     * 
     * @param csarId template ID
     * @since GSO 0.5
     */
    public synchronized void removeBeingUsedCsarId(String csarId) {
        this.beingUsedCsar.remove(csarId);
    }

    /**
     * Judge whether csar is being used to create service instance.<br/>
     * 
     * @param csarId
     * @return true when csar id exists.
     * @since GSO 0.5
     */
    public synchronized boolean isCsarBeingUsed(String csarId) {
        return this.beingUsedCsar.contains(csarId);
    }

    /**
     * Cache csar ID which is being deleted.<br/>
     * 
     * @param csarId template ID
     * @since GSO 0.5
     */
    public synchronized void addBeingDeletedCsarIds(String csarId) {
        beingDeletedCsar.add(csarId);
    }

    /**
     * Delete csar from cache.<br/>
     * 
     * @param csarId csar ID
     * @since GSO 0.5
     */
    public synchronized void removeDeletedCsarIds(String csarId) {
        this.beingDeletedCsar.remove(csarId);
    }

    /**
     * Judge whether csar is being deleted.<br/>
     * 
     * @param csarId
     * @return true when it is deleting csar package.
     * @since GSO 0.5
     */
    public synchronized boolean isCsarBeingDeleted(String csarId) {
        return this.beingDeletedCsar.contains(csarId);
    }
}
