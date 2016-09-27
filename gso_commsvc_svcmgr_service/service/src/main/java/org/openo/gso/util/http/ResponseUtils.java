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

package org.openo.gso.util.http;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.gso.commsvc.common.Exception.ApplicationException;
import org.openo.gso.commsvc.common.Exception.ExceptionArgs;
import org.openo.gso.exception.HttpCode;
import org.openo.gso.util.json.JsonUtil;
import org.openo.gso.util.validate.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to deal response result.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/9/1
 */
public class ResponseUtils {

    /**
     * Log server
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtils.class);

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private ResponseUtils() {
    }

    /**
     * Check whether there is exception.<br/>
     * <p>
     * If fail, throw the exception.
     * </p>
     * 
     * @param response rest response
     * @param function function name
     * @throws ApplicationException when the result of rest request is failure.
     * @since GSO 0.5
     */
    public static void checkResonseAndThrowException(RestfulResponse response, String function)
            throws ApplicationException {
        if(!HttpCode.isSucess(response.getStatus())) {
            ApplicationException appException = null;
            try {
                appException = JsonUtil.unMarshal(response.getResponseContent(), ApplicationException.class);
            } catch(ApplicationException exception) {
                LOGGER.error("transfer the response json string has some error: {}", exception);

                ExceptionArgs args = new ExceptionArgs();
                args.setDescription("Fail to " + function);
                args.setReason(exception.getResponse().getEntity());

                throw new ApplicationException(exception.getResponse().getStatus(), args);
            }

            throw appException;
        }
    }

    /**
     * Parse data to assigned type model.<br/>
     * 
     * @param request restful request
     * @param key key
     * @param type type
     * @return model data
     * @throws ApplicationException when transfer failed
     * @since SDNO 0.5
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getDataModelFromRsp(String request, String key, Class<T> type)
            throws ApplicationException {
        ValidateUtil.assertStringNotNull(request);
        Map<String, Object> requestMap = JsonUtil.unMarshal(request, Map.class);
        Object data = requestMap.get(key);
        List<T> dataModelList = new LinkedList<T>();
        if(data instanceof List) {
            for(Object model : (List<T>)data) {
                if(!(model instanceof Map)) {
                    LOGGER.error("The format of response content is wrong! Not Map.");
                    throw new ApplicationException(HttpCode.BAD_REQUEST, "The format of response content is wrong.");
                }

                dataModelList.add(JsonUtil.unMarshal(JsonUtil.marshal(model), type));
            }
        }

        return dataModelList;
    }

    /**
     * Parse data to assigned type model.<br/>
     * 
     * @param request restful request
     * @param type type
     * @return model data
     * @throws ApplicationException when transfer failed
     * @since SDNO 0.5
     */
    public static <T> T getDataModelFromRspList(String request, TypeReference<T> type) throws ApplicationException {
        ValidateUtil.assertStringNotNull(request);
        return JsonUtil.unMarshal(request, type);
    }

    /**
     * Get exception information.<br/>
     * 
     * @param exception operation exception
     * @param description
     * @return exception object
     * @since GSO 0.5
     */
    public static ApplicationException getException(ApplicationException exception, String description) {
        ExceptionArgs args = null;
        Object exceptionObject = exception.getResponse().getEntity();
        if(exceptionObject instanceof ExceptionArgs) {
            args = (ExceptionArgs)exceptionObject;
        } else {
            args = new ExceptionArgs();
            args.setDescription(description);
            args.setReason(exception.getResponse().getEntity());
        }
        ApplicationException appException = new ApplicationException(exception.getResponse().getStatus(), args);

        return appException;
    }
}
