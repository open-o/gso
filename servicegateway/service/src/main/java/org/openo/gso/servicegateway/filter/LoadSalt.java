/*
 * Copyright 2017 Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.filter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.openo.gso.servicegateway.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;


/**
 * filter to create salt for request<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/2/27
 */
public class LoadSalt implements Filter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadSalt.class);

    /**
     * <br>
     * 
     * @since   GSO 0.5
     */
    @Override
    public void destroy() {
        // do nothing
    }

    /**
     * <br>
     * 
     * @param requset servlet request
     * @param response servlet response
     * @param chain filter chain
     * @throws IOException when fail to filter
     * @throws ServletException when fail to filter
     * @since   GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //httpRequest
        HttpServletRequest httpServletReq = (HttpServletRequest) request;
        
        //get cache from session with attribute name "csrfPreventionSaltCache"
        Cache<String, Boolean> saltCache =
                (Cache<String, Boolean>)httpServletReq.getSession().getAttribute(Constant.SALT_CACHE_NAME);
        if(saltCache == null) {
            //deal with no cache in session condition 
            LOGGER.info("load salt -> there is no cache field in session");
            saltCache = CacheBuilder.newBuilder().maximumSize(5000).expireAfterWrite(20, TimeUnit.MINUTES).build();
            httpServletReq.getSession().setAttribute(Constant.SALT_CACHE_NAME, saltCache);
        }
        
        // generate salt and store it in cache
        String salt = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
        LOGGER.info("load salt -> salt value is : {}", salt);
        saltCache.put(salt, Boolean.TRUE);
        
        // add the salt to the current request as it can be used by the page
        httpServletReq.setAttribute(Constant.SALT_PARAMETER_NAME, salt);
        LOGGER.info("load salt -> succeed to set salt parameter in request");
        chain.doFilter(request, response);
    }

    /**
     * <br>
     * 
     * @param arg0
     * @throws ServletException
     * @since   GSO 0.5
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // do nothing

    }

}
