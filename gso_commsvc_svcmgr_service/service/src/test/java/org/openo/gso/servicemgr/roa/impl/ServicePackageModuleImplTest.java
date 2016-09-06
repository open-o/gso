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

package org.openo.gso.servicemgr.roa.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.servicemgr.constant.Constant;
import org.openo.gso.servicemgr.dao.impl.ServicePackageDaoImpl;
import org.openo.gso.servicemgr.exception.HttpCode;
import org.openo.gso.servicemgr.model.servicemo.ServicePackageMapping;
import org.openo.gso.servicemgr.restproxy.impl.CatalogProxyImpl;
import org.openo.gso.servicemgr.service.impl.PackageManagerImpl;
import org.openo.gso.servicemgr.synchronization.PackageOperationSingleton;
import org.openo.gso.servicemgr.util.http.HttpUtil;
import org.openo.gso.servicemgr.util.json.JsonUtil;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;

/**
 * Test Service Package rest interface.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/30
 */
public class ServicePackageModuleImplTest {

    /**
     * CsarId.
     */
    private static final String CSAR_ID = "1234567890";

    /**
     * File path
     */
    private static final String FILE_PATH = "src/test/resources/json/";

    /**
     * Service Package rest entry.
     */
    private ServicePackageModuleImpl servicePackageImpl;

    /**
     * Package manager.
     */
    private PackageManagerImpl packageMgr;

    /**
     * Catalog Proxy.
     */
    private CatalogProxyImpl catalogProxy;

    /**
     * Package Dao.
     */
    @Mocked
    private ServicePackageDaoImpl servicePackageDao;

    /**
     * Http request.
     */
    private HttpServletRequest request;

    /**
     * Response for success.
     */
    private RestfulResponse responseSuccess;

    /**
     * Response for fail.
     */
    private RestfulResponse responseFail;

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since GSO 0.5
     */
    @Before
    public void setUp() throws Exception {

        // Initiation
        servicePackageImpl = new ServicePackageModuleImpl();
        packageMgr = new PackageManagerImpl();
        catalogProxy = new CatalogProxyImpl();
        servicePackageDao = new ServicePackageDaoImpl();
        responseSuccess = new RestfulResponse();
        responseFail = new RestfulResponse();
        Map<String, String> catalogResult = new HashMap<String, String>();
        catalogResult.put("status", "fail");
        responseFail.setResponseJson(JsonUtil.marshal(catalogResult));

        // Set dependency
        packageMgr.setServicePackageDao(servicePackageDao);
        packageMgr.setCatalogProxy(catalogProxy);
        servicePackageImpl.setPackageMgr(packageMgr);
        responseSuccess.setStatus(HttpCode.RESPOND_OK);
        responseFail.setStatus(HttpCode.BAD_REQUEST);
    }

    /**
     * <br/>
     * 
     * @throws java.lang.Exception
     * @since GSO 0.5
     */
    @After
    public void tearDown() throws Exception {
        PackageOperationSingleton.getInstance().removeBeingUsedCsarId(CSAR_ID);
        PackageOperationSingleton.getInstance().removeDeletedCsarIds(CSAR_ID);
    }

    /**
     * Succeed to update package state.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testOnBoardingPackageSucces() {
        mockMethod((FILE_PATH + "updateState.json"), responseSuccess);
        Response response = servicePackageImpl.onBoardingPackage(request);
        Assert.assertTrue(HttpCode.isSucess(response.getStatus()));
    }

    /**
     * Test that csarId is invalid when updating package state.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnBoardingPackageCsarIdInvalid() {
        mockMethod((FILE_PATH + "csarIdInvalid.json"), responseFail);
        Response response = servicePackageImpl.onBoardingPackage(request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Test that catalog fails to update package state.<br/>
     * 
     * @throws ServiceException
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnBoardingPackageCatalogFail() throws ServiceException {
        mockMethod((FILE_PATH + "updateState.json"), responseFail);
        Response response = servicePackageImpl.onBoardingPackage(request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Update package state when executing to delete package.<br/>
     * 
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnBoardingPackageWhenDeleteCsar() {
        PackageOperationSingleton.getInstance().addBeingDeletedCsarIds(CSAR_ID);
        mockMethod((FILE_PATH + "updateState.json"), responseSuccess);
        Response response = servicePackageImpl.onBoardingPackage(request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Succeed to delete Gsar package.<br/>
     * 
     * @throws ServiceException when fail to execute database.
     * @since GSO 0.5
     */
    @Test
    public void testDeleteGsarPackageSuccess() throws ServiceException {
        mocDao(null);
        mockMethod("", responseSuccess);
        Response response = servicePackageImpl.deleteGsarPackage(CSAR_ID, request);
        Assert.assertTrue(HttpCode.isSucess(response.getStatus()));
    }

    /**
     * Delete csar package when instances exist.<br/>
     * 
     * @throws ServiceException when fail to execute database
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteGsarPackageExistInstance() throws ServiceException {
        List<ServicePackageMapping> serviceMappings = new LinkedList<ServicePackageMapping>();
        ServicePackageMapping mapping = new ServicePackageMapping();
        serviceMappings.add(mapping);

        mocDao(serviceMappings);
        mockMethod("", responseSuccess);
        Response response = servicePackageImpl.deleteGsarPackage(CSAR_ID, request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Delete csar package when using package to create instance.<br/>
     * 
     * @throws ServiceException when fail to execute database
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteGsarPackageUsingCreate() throws ServiceException {
        mocDao(null);
        mockMethod("", responseSuccess);
        PackageOperationSingleton.getInstance().addBeingUsedCsarIds(CSAR_ID);
        Response response = servicePackageImpl.deleteGsarPackage(CSAR_ID, request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Delete csar package when deleting package.<br/>
     * 
     * @throws ServiceException when fail to execute database
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteGsarPackageBeingDeleted() throws ServiceException {
        mocDao(null);
        mockMethod("", responseSuccess);
        PackageOperationSingleton.getInstance().addBeingDeletedCsarIds(CSAR_ID);
        Response response = servicePackageImpl.deleteGsarPackage(CSAR_ID, request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Catalog fail to delete csar package.<br/>
     * 
     * @throws ServiceException when fail to execute database
     * @since GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteGsarPackageCatalogFail() throws ServiceException {
        mocDao(null);
        mockMethod("", responseFail);
        Response response = servicePackageImpl.deleteGsarPackage(CSAR_ID, request);
        assertResultFail((Map<String, Object>)response.getEntity());
    }

    /**
     * Test whether object is null.<br/>
     * 
     * @since GSO 0.5
     */
    @Test
    public void testGetPackageMgr() {
        Assert.assertNotNull(servicePackageImpl.getPackageMgr());
    }

    /**
     * Check result<br/>
     * 
     * @param result
     * @since GSO 0.5
     */
    private void assertResultFail(Map<String, Object> result) {
        Assert.assertEquals(Constant.RESPONSE_STATUS_FAIL, result.get(Constant.RESPONSE_STATUS));
    }

    /**
     * Mock method.<br/>
     * 
     * @param file json file path.
     * @since GSO 0.5
     */
    private void mockMethod(final String file, final RestfulResponse response) {
        new MockUp<RestUtils>() {

            @Mock
            public String getRequestBody(HttpServletRequest request) {
                return getJsonString(file);
            }
        };

        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse delete(final String url, HttpServletRequest httpRequest) {
                return response;
            }
        };

        new MockUp<HttpUtil>() {

            @Mock
            public RestfulResponse put(final String url, final Map<String, String> httpHeaders,
                    HttpServletRequest httpRequest) {
                return response;
            }
        };
    }

    /**
     * Mock database operation.<br/>
     * 
     * @param object operation result
     * @throws ServiceException when fail to operate DB.
     * @since GSO 0.5
     */
    private void mocDao(final Object object) throws ServiceException {
        new NonStrictExpectations() {

            {
                servicePackageDao.queryPackageMappings((String)any);
                returns(object);
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
