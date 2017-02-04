/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.restproxy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.util.http.HttpUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * Test CatalogProxyImpl class.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/31
 */
public class CatalogProxyImplTest {

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";

    /**
     * Rest response.
     */
    private RestfulResponse response;

    /**
     * Catalog proxy.
     */
    CatalogProxyImpl catalogProxy;

    /**
     * Http request.
     */
    private HttpServletRequest request;

    /**
     * Build data.<br/>
     * 
     * @since GSO 0.5
     */
    @Before
    public void setUp() {
        response = new RestfulResponse();
        catalogProxy = new CatalogProxyImpl();
    }

    /**
     * Test getParamsByTemplateId method.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test
    public void testGetParamsByTemplateId() throws ApplicationException {
        response.setStatus(HttpCode.RESPOND_OK);
        response.setResponseJson(getJsonString(FILE_PATH + "getTemplateParamters.json"));
        mockGet(response);
        Assert.assertNotNull(catalogProxy.getParamsByTemplateId("123456", request));
    }

    /**
     * Test getOperationsByTemplateId method.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test
    public void testGetOperationsByTemplateId() throws ApplicationException {
        response.setStatus(HttpCode.RESPOND_OK);
        response.setResponseJson(getJsonString(FILE_PATH + "getPlans.json"));
        mockGet(response);
        Assert.assertNotNull(catalogProxy.getOperationsByTemplateId("123456", request));
    }

    /**
     * Test getTemplateByNodeTypeId method.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test
    public void testGetTemplateByNodeTypeId() throws ApplicationException {
        response.setStatus(HttpCode.RESPOND_OK);
        response.setResponseJson(getJsonString(FILE_PATH + "getTemplateByNodeType.json"));
        mockGet(response);
        Assert.assertNotNull(catalogProxy.getTemplateByNodeTypeId("123456", request));
    }

    /**
     * Test getOperationsByTemplateId method.<br/>
     * 
     * @throws ApplicationException
     * @since GSO 0.5
     */
    @Test
    public void testGetNodeTemplate() throws ApplicationException {
        response.setStatus(HttpCode.RESPOND_OK);
        response.setResponseJson(getJsonString(FILE_PATH + "getTemplateNodes.json"));
        mockGet(response);
        Assert.assertNotNull(catalogProxy.getNodeTemplate("123456", request));
    }

    /**
     * Mock rest request for get.<br/>
     * 
     * @param response rest response
     * @since GSO 0.5
     */
    private void mockGet(final RestfulResponse response) {
        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse get(final String url, final Map<String, String> httpHeaders,
                    HttpServletRequest httpRequest) throws ApplicationException {
                return response;
            }
        };
    }

    /**
     * Get json string from file.<br/>
     * 
     * @param file the path of file
     * @return json string
     * @throws IOException when fail to read
     * @since GSO 0.5
     */
    private String getJsonString(final String file) {
        if(StringUtils.isEmpty(file)) {
            return "";
        }

        String json = null;
        try {
            FileInputStream fileStream = new FileInputStream(new File(file));
            json = IOUtils.toString(fileStream);
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }

        return json;
    }
}
