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

import java.util.HashMap;
import java.util.Map;

/**
 * <br>
 * <p>
 * </p>
 * constant
 * @version     GSO 0.5  2016/9/3
 */
public class CommonConstant {

    
    
    public static final String CATALOGUE_QUERY_SVC_TMPL_NODETYPE_URL = "/openoapi/catalog/v1/servicetemplates/nesting";

    public static final String SDNO_CREATE_URL = "/openoapi/sdnonslcm/v1/ns";
    
    public static final String SDNO_INSTANTIATE_URL = "/openoapi/sdnonslcm/v1/ns/%s/instantiate";
    
    public static final String SDNO_TERMINATE_URL = "/openoapi/sdnonslcm/v1/sss/%s/terminate";
    
    public static final String SDNO_DELETE_URL = "/openoapi/sdnonslcm/v1/sss/%s";

    public static final String SDNO_QUERY_URL = "/openoapi/sdnonslcm/v1/jobs/%s";

    public static final String NFVO_CREATE_URL = "/openoapi/nslcm/v1/ns";

    public static final String NFVO_INSTANTIATE_URL = "/openoapi/nslcm/v1/ns/%s/instantiate";

    public static final String NFVO_TERMINATE_URL = "/openoapi/nslcm/v1/ns/%s/terminate";
    
    public static final String NFVO_DELETE_URL = "/openoapi/nslcm/v1/ns/%s";
    
    public static final String NFVO_QUERY_URL = "/openoapi/nslcm/v1/jobs/%s";
    
    public final static Map nfvoUrlMap = new HashMap<String, String>();
    
    static {
        nfvoUrlMap.put(Step.CREATE, NFVO_CREATE_URL);
        nfvoUrlMap.put(Step.INSTANTIATE, NFVO_INSTANTIATE_URL);
        nfvoUrlMap.put(Step.TERMINATE, NFVO_TERMINATE_URL);
        nfvoUrlMap.put(Step.DELETE, NFVO_DELETE_URL);
        nfvoUrlMap.put(Step.QUERY, NFVO_QUERY_URL);
    }
    
    public final static Map sdnoUrlMap = new HashMap<String, String>();

    static {
        sdnoUrlMap.put(Step.CREATE, SDNO_CREATE_URL);
        sdnoUrlMap.put(Step.INSTANTIATE, SDNO_INSTANTIATE_URL);
        sdnoUrlMap.put(Step.TERMINATE, SDNO_TERMINATE_URL);
        sdnoUrlMap.put(Step.DELETE, SDNO_DELETE_URL);
        sdnoUrlMap.put(Step.QUERY, SDNO_QUERY_URL);
    }
    
    /**
     * <br>
     * <p>
     * </p>
     * HttpContext constant
     * @version     GSO 0.5  2016/9/3
     */
    public static class HttpContext {

        public static final String CONTENT_TYPE = "Content-Type";

        public static final String MEDIA_TYPE_JSON = "application/json;charset=UTF-8";

        public static final String URL = "url";

        public static final String METHOD_TYPE = "methodType";

        public static final String IP = "ip";
        
        public static final String PORT = "port";
        
        private HttpContext(){
            
        }
    }

    /**
     * <br>
     * <p>
     * </p>
     * nodeType constant
     * @version     GSO 0.5  2016/9/3
     */
    public static class NodeType {

        //dc
        public static final String NFV_DC_TYPE = "tosca.nodes.nfv.NS.DC_NS";

        //pop
        public static final String NFV_POP_TYPE = "tosca.nodes.nfv.NS.POP_NS";
        
        //tic core
        public static final String NFV_VBRAS_TYPE = "tosca.nodes.nfv.NS.VBRAS_NS";

        //overlay vpn
        public static final String SDN_OVERLAYVPN_TYPE = "tosca.nodes.sdn.ext.NS.enterprise2DC";
        
        //underlay vpn
        public static final String SDN_UNDERLAYVPN_TYPE = "tosca.nodes.sdn.ext.NS.ns_underlayervpn";
        
        private NodeType(){
            
        }

    }

    /**
     * <br>
     * <p>
     * </p>
     * method type constant
     * @version     GSO 0.5  2016/9/3
     */
    public static class MethodType {

        public static final String POST = "post";

        public static final String DELETE = "delete";

        public static final String PUT = "put";

        public static final String GET = "get";
        
        private MethodType(){
            
        }
    }

    /**
     * <br>
     * <p>
     * </p>
     * step constant
     * @version     GSO 0.5  2016/9/3
     */
    public static class Step {

        public static final String CREATE = "create";

        public static final String INSTANTIATE = "instantiate";

        public static final String STATUS = "status";

        public static final String TERMINATE = "terminate";

        public static final String QUERY = "query";

        public static final String DELETE = "delete";
        
        private Step(){
            
        }

    }

    /**
     * <br>
     * <p>
     * </p>
     * domain constant
     * @version     GSO 0.5  2017/1/6
     */
    public static class Domain {
        
        public static final String SDNO = "sdno";
        
        public static final String NFVO = "nfvo";
        
        private Domain(){
            
        }
    }
    
    public static final String NSD_ID = "nsdId";
    
    public static final String NS_NAME= "nsName";
    
    public static final String DESC = "description";

    public static final String NS_INSTANCE_ID = "nsInstanceId";

    public static final String JOB_ID = "jobId";

    public static final String ADDITIONAL_PARAM_FOR_NS = "additionalParamForNs";

    public static class Status {
        
        public static final String FINISHED = "finished";
        
        public static final String PROCESSING = "processing";
        
        public static final String ERROR = "error";
        
        private Status(){
            
        }
    }
    

    
    private CommonConstant(){
        
    }
}
