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
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2017.1.11
 */
public class FieldConstant {

    /**
     * Constructor<br>
     * <p>
     * </p>
     * 
     * @since GSO Mercury Release
     */
    private FieldConstant() {

    }

    public static class ServiceType {

        public static final String GSO = "GSO";

        public static final String NFVO = "NFVO";

        public static final String SDNO = "SDNO";

        private ServiceType() {

        }
    }

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

        public static final String FIELD_SERVICE = "service";

        public static final String FIELD_NAME = "name";

        public static final String FIELD_DESCRIPTION = "description";

        public static final String FIELD_SERVICEDEFID = "serviceDefId";

        public static final String FIELD_TEMPLATEID = "templateId";

        public static final String FIELD_TEMPLATENAME = "templateName";

        public static final String FIELD_PARAMETERS = "parameters";

        public static final String PARAM_FIELD_NAME_DOMAIN = "domainHost";

        public static final String PARAM_FIELD_NAME_NODETEMPLATENAME = "nodeTemplateName";

        public static final String PARAM_FIELD_NAME_NODETYPE = "nodeType";

        public static final String PARAM_FIELD_NAME_NSPARAMETERS = "nsParameters";

        public static final String PARAM_FIELD_NAME_ADDITIONALPARAMFORNS = "additionalParamForNs";

        public static final String FIELD_RESPONSE_SERVICE = "service";

        public static final String FIELD_RESPONSE_SERVICEID = "serviceId";

        public static final String FIELD_RESPONSE_OPERATIONID = "operationId";

        private Create() {

        }

    }

    public static class Delete {

        public static final String FIELD_RESPONSE_OPERATIONID = "operationId";

        private Delete() {

        }
    }

    public static class NSCreate {

        public static final String FIELD_NSDID = "nsdId";

        public static final String FIELD_NSNAME = "nsName";

        public static final String FIELD_DESCRIPTION = "description";

        public static final String FIELD_RESPONSE_NSINSTANCEID = "nsInstanceId";

        private NSCreate() {

        }
    }

    public static class NSInstantiate {

        public static final String FIELD_NSINSTANCEID = "nsInstanceId";

        public static final String FIELD_PARAMS = "additionalParamForNs";

        public static final String FIELD_RESPONSE_JOBID = "jobId";

        private NSInstantiate() {

        }
    }

    public static class NSTerminate {

        public static final String FIELD_NSINSTANCEID = "nsInstanceId";

        public static final String FIELD_TERMINATIONTYPE = "terminationType";
        
        public static final String FIELD_TERMINATIONTYPE_DEFAULT = "graceful";

        public static final String FIELD_TIMEOUT = "gracefulTerminationTimeout";
        
        public static final String FIELD_TIMEOUT_DEFAULT = "60";

        public static final String FIELD_RESPONSE_JOBID = "jobId";
                
        private NSTerminate() {

        }
    }
    
    public static class NSScale {

        public static final String FIELD_RESPONSE_JOBID = "jobId";
                
        private NSScale() {

        }
    }

    public static class QueryJob {

        public static final String FIELD_JOBID = "jobId";

        public static final String FIELD_RESPONSEDESCRIPTOR = "responseDescriptor";

        public static final String FIELD_PROGRESS = "progress";

        public static final String FIELD_STATUS = "status";

        public static final String FIELD_STATUSDESCRIPTION = "statusDescription";

        public static final String FIELD_ERRORCODE = "errorCode";

        public static final String STATUS_PROCESSING = "processing";

        public static final String STATUS_FINISHED = "finished";

        public static final String STATUS_ERROR = "error";

        private QueryJob() {

        }
    }

    public static class QueryOperation {

        public static final String FIELD_OPERATION = "operation";

        public static final String FIELD_OPERATIONID = "operationId";

        public static final String FIELD_USERID = "userId";

        public static final String FIELD_OPERATIONCONTENT = "operationContent";

        public static final String FIELD_RESULT = "result";

        public static final String FIELD_REASON = "reason";

        public static final String FIELD_PROGRESS = "progress";

        public static final String RESULT_PROCESSING = "processing";

        public static final String RESULT_FINISHED = "finished";

        public static final String RESULT_ERROR = "error";

        private QueryOperation() {

        }

    }

    public static class CatalogTemplate {

        public static final String FIELD_TEMPLATEID = "serviceTemplateId";

        public static final String FIELD_ID = "id";

        public static final String FIELD_TEMPLATENAME = "templateName";

        public static final String FIELD_CSARID = "csarId";

        public static final String FIELD_INPUTS = "inputs";

        public static final String FIELD_SUBSTITUTION = "substitution";

        public static final String FIELD_SUBSTITUTION_NODETYPE = "nodeType";

        private CatalogTemplate() {

        }
    }

    public static class Vim {

        public static final String FIELD_VIMID = "vimId";

        public static final String FIELD_NAME = "name";

        private Vim() {

        }
    }

    public static class SDNController {

        public static final String FIELD_SDNCONTROLLERID = "sdnControllerId";

        public static final String FIELD_NAME = "name";

        private SDNController() {

        }
    }

    public static class NodeTemplates {

        public static final String FIELD_ID = "id";

        public static final String FIELD_NAME = "name";

        public static final String FIELD_TYPE = "type";

        private NodeTemplates() {

        }
    }

    public static class InventoryService {

        public static final String FIELD_SERVICEID = "serviceId";

        public static final String FIELD_SERVICENAME = "serviceName";

        public static final String FIELD_DESCRIPTION = "description";

        public static final String FIELD_CREATETIME = "createTime";

        public static final String FIELD_CREATOR = "creator";

        public static final String FIELD_SERVICETYPE = "serviceType";

        public static final String FIELD_TEMPLATENAME = "templateName";

        public static final String FIELD_INPUTPARAMETERS = "inputParameters";

        private InventoryService() {

        }
    }

    public static class Domain {

        public static final String FIELD_NAME = "name";

        public static final String FIELD_HOST = "host";

        private Domain() {

        }
    }

    public static class VnfNodeTemplate {

        public static final String FIELD_NAME = "name";

        public static final String FIELD_PROPERTIES = "properties";

        public static final String FIELD_PROPERTIES_ID = "id";

        private VnfNodeTemplate() {

        }
    }
}
