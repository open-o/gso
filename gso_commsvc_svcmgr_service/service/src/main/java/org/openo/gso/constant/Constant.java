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

package org.openo.gso.constant;

/**
 * Constant definition.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version GSO 0.5 2016/8/22
 */
public class Constant {

    /**
     * Service instance name.
     */
    public static final String SERVICE_NAME = "name";

    /**
     * Service instance description.
     */
    public static final String SERVICE_DESCRIPTION = "description";

    /**
     * Service instance ID.
     */
    public static final String SERVICE_INSTANCE_ID = "serviceId";

    /**
     * Service package ID.
     */
    public static final String SERVICE_DEF_ID = "serviceDefId";

    /**
     * Service template ID.
     */
    public static final String SERVICE_TEMPLATE_ID = "templateId";

    /**
     * Service template name.
     */
    public static final String SERVICE_TEMPLATE_NAME = "templateName";

    /**
     * service parameters
     */
    public static final String SERVICE_PARAMETERS = "parameters";

    /**
     * Create workflow name.
     */
    public static final String WORK_FLOW_PLAN_CREATE = "create";

    /**
     * Delete workflow name.
     */
    public static final String WORK_FLOW_PLAN_DELETE = "delete";

    /**
     * filed in workflow body.
     */
    public static final String WSO_PROCESSID = "processId";

    /**
     * filed in workflow body.
     */
    public static final String WSO_PARAMS = "params";

    /**
     * Response result;
     */
    public static final String RESPONSE_RESULT = "result";

    /**
     * Response status.
     */
    public static final String RESPONSE_STATUS = "status";

    /**
     * Response status description.
     */
    public static final String RESPONSE_STATUS_DESCRIPTION = "statusDescription";

    /**
     * Error code of response.
     */
    public static final String RESPONSE_ERRORCODE = "errorCode";

    /**
     * Operations success.
     */
    public static final String RESPONSE_STATUS_SUCCESS = "success";

    /**
     * Operation failed.
     */
    public static final String RESPONSE_STATUS_FAIL = "fail";

    /**
     * Identify of csar.
     */
    public static final String CSAR_ID = "csarId";

    /**
     * service identify.
     */
    public static final String SERVICE_INDENTIFY = "service";

    /**
     * Predefine parameter for service design.
     */
    public static final String SERVICE_ID = "serviceId";

    /**
     * Node sequence.
     */
    public static final String NODE_SEQUENCE = "sequence";

    /**
     * Sequence property.
     */
    public static final String SEQUENCE_PROPERTY = "nssequence";

    /**
     * Service segment instance id.
     */
    public static final String SERVICE_SEGMENT_INSTANCE_ID = "instanceId";

    /**
     * Default value.
     */
    public static final String DEFAULT_STRING = "--";

    /**
     * Service operation ID.
     */
    public static final String SERVICE_OPERATION_ID = "operationId";

    /**
     * Create operation
     */
    public static final String OPERATION_CREATE = "create";

    /**
     * Delete operation
     */
    public static final String OPERATION_DELETE = "delete";

    /**
     * Service segments.
     */
    public static final String SERVICE_SEGMENTS = "segments";

    /**
     * Services identify
     */
    public static final String SERVICES_INDENTIRY = "services";

    /**
     * Service instance property "segmentNumber"
     */
    public static final String MODEL_COLUMN_SEGMENT_NUMBER = "segmentNumber";

    /**
     * Node name identify.
     */
    public static final String NODE_TEMPLATE_NAME = "nodeTemplateName";

    /**
     * Service segment ID.
     */
    public static final String SERVICE_SEGMENT_ID_DIRVER = "subServiceId";

    /**
     * Service segment name.
     */
    public static final String SERVICE_SEGMENT_NAME_DIRVER = "subServiceName";

    /**
     * Service segment description.
     */
    public static final String SERVICE_SEGMENT_DES_DIRVER = "subServiceDesc";

    /**
     * Service segment domain host
     */
    public static final String SERVICE_SEGMENT_DOMAINHOST = "domainHost";

    /**
     * Service package identify
     */
    public static final String SERVICE_PACKAGE_IDENTIFY = "servicePackage";

    /**
     * Operation identify.
     */
    public static final String OPERATION_IDENTIFY = "operation";

    /**
     * Service segment name
     */
    public static final String SEGMENT_PROPERTY_NAME = "serviceSegmentName";

    /**
     * Node name which is defined in template
     */
    public static final String SEGMENT_PROPERTY_NODENAME = "nodeTemplateName";

    /**
     * Template ID
     */
    public static final String SEGMENT_PROPERTY_TEMPLATEID = "templateId";

    /**
     * Node type
     */
    public static final String SEGMENT_PROPERTY_NODETYPE = "nodeType";

    /**
     * message field of response content
     */
    public static final String RESPONSE_CONTENT_MESSAGE = "message";

    /**
     * Workflow response status
     */
    public static final String WORKFLOW_RESPONSE_ZERO = "0";

    /**
     * Operate service operation record DAO
     */
    public static final String BEAID_SERVICE_OPER_DAO = "serviceOperDao";

    /**
     * Operate service segment DAO
     */
    public static final String BEAID_SERVICE_SEG_DAO = "serviceSegmentDao";

    /**
     * Operae service instance DAO
     */
    public static final String BEAID_SERVICE_MODEL_DAO = "serviceModelDao";

    /**
     * Operate inventory data DAO
     */
    public static final String BEAID_INV_DAO = "inventoryDao";

    /**
     * Constructor<br/>
     * <p>
     * </p>
     * 
     * @since GSO 0.5
     */
    private Constant() {

    }
}
