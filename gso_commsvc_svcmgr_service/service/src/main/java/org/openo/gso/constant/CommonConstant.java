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

package org.openo.gso.constant;

/**
 * <br>
 * <p>
 * </p>
 * constant
 * @version     GSO 0.5  2016/9/3
 */
public class CommonConstant {

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
     * @author
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
     * @author
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
     * @author
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
    
    private CommonConstant(){
        
    }
}
