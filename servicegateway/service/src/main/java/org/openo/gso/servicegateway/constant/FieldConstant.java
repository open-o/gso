/*
 * Copyright (c) 2017, Huawei Technologies Co., Ltd.
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

package org.openo.gso.servicegateway.constant;

/**
 * 
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2017.1.11
 */
public class FieldConstant {

    /**
     * field define for create json
     * <br>
     * <p>
     * </p>
     * 
     * @author
     * @version GSO 0.5 2017.1.4
     */
    public static class Create {

        public static String FIELD_SERVICE = "service";

        public static String FIELD_NAME = "name";

        public static String FIELD_DESCRIPTION = "description";

        public static String FIELD_SERVICEDEFID = "serviceDefId";

        public static String FIELD_TEMPLATEID = "templateId";

        public static String FIELD_TEMPLATENAME = "templateName";

        public static String FIELD_PARAMETERS = "parameters";

        public static String PARAM_FIELD_NAME_DOMAIN = "domainHost";

        public static String PARAM_FIELD_NAME_NODETEMPLATENAME = "nodeTemplateName";

        public static String PARAM_FIELD_NAME_NODETYPE = "nodeType";

        public static String PARAM_FIELD_NAME_NSPARAM = "additionalParamForNS";

        public static String FIELD_RESPONSE_SERVICE = "service";

        public static String FIELD_RESPONSE_SERVICEID = "serviceId";

        public static String FIELD_RESPONSE_OPERATIONID = "operationId";

    }

    public static class Delete {

        public static String FIELD_RESPONSE_OPERATIONID = "operationId";
    }

    public static class NSCreate {

        public static String FIELD_NSDID = "nsdId";

        public static String FIELD_NSNAME = "nsName";

        public static String FIELD_DESCRIPTION = "description";

        public static String FIELD_RESPONSE_NSINSTANCEID = "nsInstanceId";
    }

    public static class NSInstantiate {

        public static String FIELD_NSINSTANCEID = "nsInstanceId";

        public static String FIELD_PARAMS = "additionalParamForNs";

        public static String FIELD_RESPONSE_JOBID = "jobId";
    }

    public static class NSTerminate {

        public static String FIELD_NSINSTANCEID = "nsInstanceId";

        public static String FIELD_TERMINATIONTYPE = "terminationType";

        public static String FIELD_TIMEOUT = "gracefulTerminationTimeout";
    }

    public static class QueryJob {

        public static String FIELD_JOBID = "jobId";

        public static String FIELD_RESPONSEDESCRIPTOR = "responseDescriptor";

        public static String FIELD_PROGRESS = "progress";

        public static String FIELD_STATUS = "status";

        public static String FIELD_STATUSDESCRIPTION = "statusDescription";

        public static String FIELD_ERRORCODE = "errorCode";

        public static String STATUS_PROCESSING = "processing";

        public static String STATUS_FINISHED = "finished";

        public static String STATUS_ERROR = "error";
    }

    public static class QueryOperation {

        public static String FIELD_OPERATION = "operation";

        public static String FIELD_OPERATIONID = "operationId";

        public static String FIELD_USERID = "userId";

        public static String FIELD_OPERATIONCONTENT = "operationContent";
        
        public static String FIELD_RESULT = "result";

        public static String FIELD_REASON = "reason";

        public static String FIELD_PROGRESS = "progress";

        public static String RESULT_PROCESSING = "processing";

        public static String RESULT_FINISHED = "finished";

        public static String RESULT_ERROR = "error";

    }

    public static class CatalogTemplate {

        public static String FIELD_TEMPLATEID = "serviceTemplateId";

        public static String FIELD_ID = "id";

        public static String FIELD_TEMPLATENAME = "templateName";

        public static String FIELD_CSARID = "csarId";

        public static String FIELD_INPUTS = "inputs";

        public static String FIELD_SUBSTITUTION = "substitution";

        public static String FIELD_SUBSTITUTION_NODETYPE = "nodeType";

    }

    public static class Vim {

        public static String FIELD_VIMID = "vimId";

        public static String FIELD_NAME = "name";
    }

    public static class SDNController {

        public static String FIELD_SDNCONTROLLERID = "sdnControllerId";

        public static String FIELD_NAME = "name";
    }

    public static class NodeTemplates {

        public static String FIELD_ID = "id";

        public static String FIELD_NAME = "name";

        public static String FIELD_TYPE = "type";

    }
}
