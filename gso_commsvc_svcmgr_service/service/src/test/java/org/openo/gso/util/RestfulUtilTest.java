
package org.openo.gso.util;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.HttpRest;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;

import mockit.Mock;
import mockit.MockUp;


public class RestfulUtilTest {

    @Test
    public void testGetRemoteResponse() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(CommonConstant.HttpContext.URL, "");
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.POST);
        String params = "";
        final RestfulResponse rsp = new RestfulResponse();
        new MockUp<HttpRest>(){
            @Mock
            public RestfulResponse get(String servicePath, RestfulParametes restParametes) throws ServiceException{
                return rsp;
            }
            
            @Mock
            public RestfulResponse put(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }
            
            @Mock
            public RestfulResponse post(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }
            
            @Mock
            public RestfulResponse delete(String servicePath, RestfulParametes restParametes) throws ServiceException {
                return rsp;
            }
        };
        
        RestfulResponse postRsp = RestfulUtil.getRemoteResponse(paramsMap, params, paramsMap);
        assertNotNull(postRsp);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.GET);
        RestfulResponse getRsp = RestfulUtil.getRemoteResponse(paramsMap, params, paramsMap);
        assertNotNull(getRsp);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.DELETE);
        RestfulResponse deleteRsp = RestfulUtil.getRemoteResponse(paramsMap, params, paramsMap);
        assertNotNull(deleteRsp);
        paramsMap.put(CommonConstant.HttpContext.METHOD_TYPE, CommonConstant.MethodType.PUT);
        RestfulResponse putRsp = RestfulUtil.getRemoteResponse(paramsMap, params, paramsMap);
        assertNotNull(putRsp);
    }

}
