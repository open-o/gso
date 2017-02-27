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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.openo.gso.servicegateway.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;


/**
 * filter to validate salt for the request<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017/2/27
 */
public class ValidateSalt implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateSalt.class);
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
     * @param req servlet request
     * @param rsp servlet response
     * @param chain filter chain
     * @throws IOException when fail to validate
     * @throws ServletException when fail to validate
     * @since   GSO 0.5
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        // get salt parameter from the request
        String salt = httpServletRequest.getParameter(Constant.SALT_PARAMETER_NAME);
        
        LOGGER.info("validate salt -> salt value from request is {}", salt);
        //validate whether the salt exist in the cache
        Cache<String, Boolean> saltCache =
                (Cache<String, Boolean>)httpServletRequest.getSession().getAttribute(Constant.SALT_CACHE_NAME);
        if((salt != null) && (saltCache != null) && (saltCache.getIfPresent(salt) != null)){
            LOGGER.info("validate salt -> validate result : ok");
            chain.doFilter(req, rsp);
        } else {
            LOGGER.error("validate salt -> validate result : failed");
            //if salt not exist in the cache, throw exception of csrf
            throw new ServletException("Potential CSRF detected, Inform a scary sysadmin ASAP");
        }
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
