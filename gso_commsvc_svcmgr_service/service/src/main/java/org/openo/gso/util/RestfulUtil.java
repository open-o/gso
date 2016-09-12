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

package org.openo.gso.util;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
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
 * @author
 * @version     GSO 0.5  2016/9/3
 */
public class RestfulUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulUtil.class);

    private RestfulUtil(){
        
    }
    
    /**
     * <br>
     * invoke the restclient and return the response
     * @param paramsMap input header map
     * @param params input body (raw data)
     * @param queryParam query condition
     * @return restful response
     * @since  GSO 0.5
     */
    public static RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
            Map<String, String> queryParam) {
        if(null == paramsMap) {
            return null;
        }
        String url = paramsMap.get(CommonConstant.HttpContext.URL);
        String methodType = paramsMap.get(CommonConstant.HttpContext.METHOD_TYPE);

        RestfulResponse rsp = null;
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        try {

            RestfulParametes restfulParametes = new RestfulParametes();
            Map<String, String> headerMap = new HashMap<String, String>(3);
            headerMap.put(CommonConstant.HttpContext.CONTENT_TYPE, CommonConstant.HttpContext.MEDIA_TYPE_JSON);
            restfulParametes.setHeaderMap(headerMap);

            if(null != params) {
                restfulParametes.setRawData(params);
            }

            if(null != queryParam) {
                for(Map.Entry<String, String> curEntity : queryParam.entrySet()) {
                    restfulParametes.putHttpContextHeader(curEntity.getKey(), curEntity.getValue());
                }
            }

            if(rest != null) {
                if(CommonConstant.MethodType.GET.equalsIgnoreCase(methodType)) {
                    rsp = rest.get(url, restfulParametes);
                } else if(CommonConstant.MethodType.POST.equalsIgnoreCase(methodType)) {
                    rsp = rest.post(url, restfulParametes);
                } else if(CommonConstant.MethodType.PUT.equalsIgnoreCase(methodType)) {
                    rsp = rest.put(url, restfulParametes);
                } else if(CommonConstant.MethodType.DELETE.equalsIgnoreCase(methodType)) {
                    rsp = rest.delete(url, restfulParametes);
                }
            }
        } catch(ServiceException e) {
            LOGGER.error("function=getRemoteResponse, get restful response catch exception {}", e);
        }
        return rsp;
    }
    


}
