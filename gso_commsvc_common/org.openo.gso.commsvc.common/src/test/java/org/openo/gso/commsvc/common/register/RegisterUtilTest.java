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
package org.openo.gso.commsvc.common.register;

import java.io.File;

import org.junit.Test;
import org.openo.baseservice.util.inf.SystemEnvVariables;
import org.openo.gso.commsvc.common.constant.Constant;

/**
 * Test Register Tool <br/>
 * 
 * @author
 * @since GSO 0.5, 2016-8-9
 */
public class RegisterUtilTest {
	
    /**
     * Register Service that jsonInfo is null.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testRegisterServiceNull() {
        String jsonInfo = null;
        RegisterUtil.registerService(jsonInfo);
    }

    /**
     * Register Service that jsonInfo have the real IP.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testRegisterService1() {
        String jsonInfo = "'ip' : '137.25.1.1'";
        RegisterUtil.registerService(jsonInfo);
    }

    /**
     * Register Service that jsonInfo have the input Key.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testRegisterService2() {
        String jsonInfo = "'ip' : 'getInputIP'";
        RegisterUtil.registerService(jsonInfo);
    }

    /**
     * Read File that path is null.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testReadFileNull() {
        String path = null;
        RegisterUtil.readFile(path);
    }

    /**
     * Read File with the real path.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testReadFileWithRealPath() {
        String root = "D:\\source\\gso_commsvc_svcmgr_service\\deployment\\src\\main\\release";
        String path = root + File.separator + Constant.FILE_PATH_ETC + File.separator + Constant.FILE_PATH_REGISTER
                + File.separator + Constant.FILE_PATH_JSON;
        RegisterUtil.readFile(path);

    }

    /**
     * Read File with the warn path.<br/>
     * 
     * @since GSO 0.5, 2016-8-10
     */
    @Test
    public void testReadFileWithWarnPath() {
        String root = SystemEnvVariables.getinstance().getAppRoot();
        String path = root + File.separator + Constant.FILE_PATH_ETC + File.separator + Constant.FILE_PATH_REGISTER
                + File.separator + "test.json";
        RegisterUtil.readFile(path);
    }

}
