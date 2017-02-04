/*
 * Copyright (c) 2016-2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.util.json;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.openo.gso.commsvc.common.exception.ApplicationException;
import org.openo.gso.servicegateway.exception.HttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSON;

/**
 * Interface for json analyzing.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/1
 */
public class JsonUtil {

    /**
     * Log service
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * Mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig()
                .without(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        MAPPER.setSerializationInclusion(Inclusion.NON_NULL);
    }

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private JsonUtil() {

    }

    /**
     * Parse the string in form of json.<br/>
     * 
     * @param jsonstr json string.
     * @param type that convert json string to
     * @return
     * @since GSO 0.5
     */
    public static <T> T unMarshal(String jsonstr, Class<T> type) {
        try {
            return MAPPER.readValue(jsonstr, type);
        } catch(IOException e) {
            LOGGER.error("jsonstr unMarshal failed!", e);
            throw new ApplicationException(HttpCode.BAD_REQUEST, "jsonstr unMarshal failed!");
        }
    }

    /**
     * Parse the string in form of json.<br/>
     * 
     * @param jsonstr json string.
     * @param type that convert json string to
     * @return
     * @since GSO 0.5
     */
    public static <T> T unMarshal(String jsonstr, TypeReference<T> type) {
        try {
            return MAPPER.readValue(jsonstr, type);
        } catch(IOException e) {
            LOGGER.error("jsonstr unMarshal failed!", e);
            throw new ApplicationException(HttpCode.BAD_REQUEST, "jsonstr unMarshal failed!");
        }
    }

    /**
     * Convert object to json string.<br/>
     * 
     * @param srcObj data object
     * @return json string
     * @since GSO 0.5
     */
    public static String marshal(Object srcObj) {
        if(srcObj instanceof JSON) {
            return srcObj.toString();
        }
        try {
            return MAPPER.writeValueAsString(srcObj);
        } catch(IOException e) {
            LOGGER.error("srcObj marshal failed!", e);
            throw new ApplicationException(HttpCode.BAD_REQUEST, "srcObj marshal failed!");
        }
    }

    /**
     * Get mapper.<br/>
     * 
     * @return mapper
     * @since GSO 0.5
     */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
