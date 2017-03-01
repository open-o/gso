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

package org.openo.gso.servicegateway.constant;

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
     * service file Path:etc
     */
    public static final String FILE_PATH_ETC = "etc";

    /**
     * service file Path:register
     */
    public static final String FILE_PATH_REGISTER = "register";

    /**
     * service file Path:file name
     */
    public static final String FILE_PATH_JSON = "service.json";

    /**
     * domains info file
     */
    public static final String FILE_PATH_DOMAINSINFO = "etc/conf/domainsInfo.json";

    /**
     * the head type for Restful
     */
    public static final String HEAD_ERMAP_TYPE = "Content-Type";

    /**
     * the head value for Restful
     */
    public static final String HEAD_ERMAP_VALUE = "application/json;charset=UTF-8";

    /**
     * the URL for Register service to the M-service Bus
     */
    public static final String M_BUS_REGISTER_URL = "/openoapi/microservices/v1/services";

    /**
     * constant:0
     */
    public static final int ZERO = 0;

    /**
     * the URL for template request.
     */
    public static final String CATALOG_TEMPLATE_URL = "/openoapi/catalog/v1/servicetemplates/%s";

    /**
     * the URL for csar request.
     */
    public static final String CATALOG_CSAR_URL = "/openoapi/catalog/v1/csars/%s";

    /**
     * the URL for query node templates by template id
     */
    public static final String CATALOG_NODETYPE_URL = "/openoapi/catalog/v1/servicetemplates/%s/nodetemplates";

    /**
     * the URL for query template by node types
     */
    public static final String CATALOG_TEMPLATE_BYNOTEYPE_URL =
            "/openoapi/catalog/v1/servicetemplates/nesting?nodeTypeIds=%s";

    /**
     * the param key for csar type
     */
    public static final String CATALOG_CSAR_PARAM_TYPE = "type";

    /**
     * GSAR
     */
    public static final String CATALOG_CSAR_TYPE_GSAR = "GSAR";

    /**
     * NSAR
     */
    public static final String CATALOG_CSAR_TYPE_NSAR = "NSAR";

    /**
     * NFAR
     */
    public static final String CATALOG_CSAR_TYPE_NFAR = "NFAR";

    /**
     * SSAR
     */
    public static final String CATALOG_CSAR_TYPE_SSAR = "SSAR";

    /**
     * URL for create gso
     */
    public static final String GSO_URL_CREATE = "/openoapi/gso/v1/services";

    /**
     * URL for delete GSO
     */
    public static final String GSO_URL_DELETE = "/openoapi/gso/v1/services/%s";

    /**
     * URL for query gso operation
     */
    public static final String GSO_URL_QUERY_OPRATION = "/openoapi/gso/v1/services/%s/operations/%s";

    /**
     * URL for create sdno
     */
    public static final String SDNO_URL_CREATE = "/openoapi/sdnonslcm/v1/ns";

    /**
     * URL for delete sdno
     */
    public static final String SDNO_URL_DELETE = "/openoapi/sdnonslcm/v1/ns/%s";

    /**
     * URL for terminate sdno
     */
    public static final String SDNO_URL_TERMINATE = "/openoapi/sdnonslcm/v1/ns/%s/terminate";

    /**
     * URL for instantiate sdno
     */
    public static final String SDNO_URL_INSTANTIATE = "/openoapi/sdnonslcm/v1/ns/%s/instantiate";

    /**
     * URL for query sdno job
     */
    public static final String SDNO_URL_QUERYJOB = "/openoapi/sdnonslcm/v1/jobs/%s";

    /**
     * URL for create nfvo
     */
    public static final String NFVO_URL_CREATE = "/openoapi/nslcm/v1/ns";

    /**
     * URL for delete nfvo
     */
    public static final String NFVO_URL_DELETE = "/openoapi/nslcm/v1/ns/%s";

    /**
     * URL for terminate nfvo
     */
    public static final String NFVO_URL_TERMINATE = "/openoapi/nslcm/v1/ns/%s/terminate";

    /**
     * URL for instantiate nfvo
     */
    public static final String NFVO_URL_INSTANTIATE = "/openoapi/nslcm/v1/ns/%s/instantiate";

    /**
     * URL for scale nfvo
     */
    public static final String NFVO_URL_SCALE = "/openoapi/nslcm/v1/ns/%s/scale";

    /**
     * URL for query nfvo job
     */
    public static final String NFVO_URL_QUERYJOB = "/openoapi/nslcm/v1/jobs/%s";

    /**
     * URL for query services
     */
    public static final String INVENTORY_URL_QUERYSERVICES = "/openoapi/inventory/v1/services";

    /**
     * URL for query vims
     */
    public static final String EXTSYS_URL_QUERYVIMS = "/openoapi/extsys/v1/vims";

    /**
     * URL for query sdncontrollers
     */
    public static final String EXTSYS_URL_QUERYSDNCONTROLLERS = "/openoapi/extsys/v1/sdncontrollers";

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
