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

import org.apache.commons.lang3.StringUtils;
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
     * invoke the restclient and return the response
     * 
     * @param url service path
     * @param methodType http method
     * @param restfulParametes restful parameters(raw data and param map)
     * @param options ip and port
     * @return restful response
     * @since GSO 0.5
     */
    public static RestfulResponse getRemoteResponse(String url, String methodType, RestfulParametes restfulParametes,
            RestfulOptions options) {
        RestfulResponse rsp = null;
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        try {
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
        if(rsp != null) {
            LOGGER.info("responseContent is {}", rsp.getResponseContent());
        } else {
            LOGGER.error("Invalid restful response");
        }
        return rsp;
    }

    /**
     * set RestfulOptions<br>
     * 
     * @param domainHost domainHost field
     * @return RestfulOptions Object
     * @since GSO 0.5
     */
    public static RestfulOptions setRestfulOptions(String domainHost) {
        if(StringUtils.isEmpty(domainHost) || CommonConstant.LOCAL_HOST.equals(domainHost)) {
            LOGGER.info("domainHost is {}", domainHost);
            return null;
        }
        LOGGER.info("domainHost is {}", domainHost);
        String ip = domainHost.substring(0, domainHost.indexOf(":"));
        String port = domainHost.substring(domainHost.indexOf(":") + 1);
        RestfulOptions options = new RestfulOptions();
        options.setHost(ip);
        options.setPort(Integer.valueOf(port));
        return options;
    }

    /**
     * set RestfulParameters<br>
     * 
     * @param rawData request body
     * @param queryParam parameters for query
     * @return RestfulParameters Object
     * @since GSO 0.5
     */
    public static RestfulParametes setRestfulParameters(String rawData, Map<String, String> queryParam) {
        RestfulParametes restfulParametes = new RestfulParametes();
        Map<String, String> headerMap = new HashMap<>(3);
        headerMap.put(CommonConstant.HttpContext.CONTENT_TYPE, CommonConstant.HttpContext.MEDIA_TYPE_JSON);
        restfulParametes.setHeaderMap(headerMap);

        if(null != rawData) {
            restfulParametes.setRawData(rawData);
            LOGGER.info("raw data is {}", rawData);
        }

        if(null != queryParam) {
            for(Map.Entry<String, String> curEntity : queryParam.entrySet()) {
                LOGGER.info("query condition: {} is {}", curEntity.getKey(), curEntity.getValue());
            }
            restfulParametes.setParamMap(queryParam);
        }
        return restfulParametes;
    }

}
