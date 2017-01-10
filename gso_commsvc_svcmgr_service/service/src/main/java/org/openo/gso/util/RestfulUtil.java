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

package org.openo.gso.util;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>
 * <p>
 * </p>
 * utility to invoke restclient
 * 
 * @author
 * @version GSO 0.5 2016/9/3
 */
public class RestfulUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulUtil.class);

    private RestfulUtil() {

    }

    /**
     * <br>
     * invoke the restclient and return the response
     * 
     * @param paramsMap input header map
     * @param body input body (raw data)
     * @param queryParam query condition
     * @return restful response
     * @since GSO 0.5
     */
    public static RestfulResponse getRemoteResponse(Map<String, Object> paramsMap, String body,
            Map<String, String> queryParam) {
        if(null == paramsMap) {
            return null;
        }
        String url = (String) paramsMap.get(CommonConstant.HttpContext.URL);
        LOGGER.info("url is : {}", url);
        String methodType = (String) paramsMap.get(CommonConstant.HttpContext.METHOD_TYPE);
        LOGGER.info("method type is : {}", methodType);
        String ip = (String) paramsMap.get(CommonConstant.HttpContext.IP);
        LOGGER.info("ip is : {}", ip);
        String port =  (String) paramsMap.get(CommonConstant.HttpContext.PORT);
        LOGGER.info("port is : {}", port);
        RestfulResponse rsp = null;
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        try {

            RestfulParametes restfulParametes = setRestfulParameters(body, queryParam);

            RestfulOptions options = setRestfulOptions(ip, port);
                
            if(rest != null) {
                if(CommonConstant.MethodType.GET.equalsIgnoreCase(methodType)) {
                    rsp = rest.get(url, restfulParametes, options);
                } else if(CommonConstant.MethodType.POST.equalsIgnoreCase(methodType)) {
                    rsp = rest.post(url, restfulParametes, options);
                } else if(CommonConstant.MethodType.PUT.equalsIgnoreCase(methodType)) {
                    rsp = rest.put(url, restfulParametes, options);
                } else if(CommonConstant.MethodType.DELETE.equalsIgnoreCase(methodType)) {
                    rsp = rest.delete(url, restfulParametes, options);
                }
            }
        } catch(ServiceException e) {
            LOGGER.error("function=getRemoteResponse, get restful response catch exception {}", e);
        }
        LOGGER.warn("responseContent is {}", rsp.getResponseContent());
        return rsp;
    }

    /**
     * set RestfulOptions<br>
     * 
     * @param ip ip of domainHost field
     * @param port port of domainHost field
     * @return RestfulOptions Object
     * @since  GSO 0.5
     */
    private static RestfulOptions setRestfulOptions(String ip, String port) {
        //ip & port must exist at the same time
        if((null == ip) || (null == port)){
            return null;
        }
        RestfulOptions options = new RestfulOptions();
        options.setHost(ip);
        options.setPort(Integer.valueOf(port));
        return options;
    }

    /**
     * set RestfulParameters<br>
     * 
     * @param body request body
     * @param queryParam parameters for query
     * @return RestfulParameters Object
     * @since  GSO 0.5
     */
    private static RestfulParametes setRestfulParameters(String body, Map<String, String> queryParam) {
        RestfulParametes restfulParametes = new RestfulParametes();
        Map<String, String> headerMap = new HashMap<String, String>(3);
        headerMap.put(CommonConstant.HttpContext.CONTENT_TYPE, CommonConstant.HttpContext.MEDIA_TYPE_JSON);
        restfulParametes.setHeaderMap(headerMap);

        if(null != body) {
            restfulParametes.setRawData(body);
            LOGGER.warn("raw data is {}", body);
        }

        if(null != queryParam) {
            for(Map.Entry<String, String> curEntity : queryParam.entrySet()) {
                LOGGER.warn("query condition: {} is {}", curEntity.getKey(), curEntity.getValue());
            }
            restfulParametes.setParamMap(queryParam);
        }
        return restfulParametes;
    }

}
