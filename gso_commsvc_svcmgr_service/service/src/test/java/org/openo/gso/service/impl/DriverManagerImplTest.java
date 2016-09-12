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
package org.openo.gso.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.baseservice.util.RestUtils;
import org.openo.gso.model.drivermo.TerminateParams;
import org.openo.gso.service.inf.IDriverService;
import org.openo.gso.util.RestfulUtil;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;


public class DriverManagerImplTest {
    @Mocked
    IDriverService serviceInf;

    @Test
    public void testTerminateService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        HttpServletRequest httpRequest = new HttpServletRequest() {
            
            @Override
            public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setAttribute(String arg0, Object arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void removeAttribute(String arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isSecure() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public int getServerPort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getServerName() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getScheme() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RequestDispatcher getRequestDispatcher(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getRemotePort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getRemoteHost() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRemoteAddr() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRealPath(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public BufferedReader getReader() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getProtocol() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String[] getParameterValues(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getParameterNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Map getParameterMap() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getParameter(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getLocales() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getLocalPort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getLocalName() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getLocalAddr() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ServletInputStream getInputStream() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getContentType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getContentLength() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getCharacterEncoding() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getAttributeNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Object getAttribute(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public boolean isUserInRole(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdValid() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromUrl() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromURL() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromCookie() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public Principal getUserPrincipal() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpSession getSession(boolean arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpSession getSession() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getServletPath() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRequestedSessionId() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public StringBuffer getRequestURL() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRequestURI() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRemoteUser() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getQueryString() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPathTranslated() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPathInfo() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getMethod() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getIntHeader(String arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Enumeration getHeaders(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getHeaderNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getHeader(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public long getDateHeader(String arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Cookie[] getCookies() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getContextPath() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getAuthType() {
                // TODO Auto-generated method stub
                return null;
            }
        };

        TerminateParams params = new TerminateParams();
        params.setNodeType("tosca.nodes.nfv.dc");
        Map<String, String> inputParameters = new HashMap<String, String>();
        inputParameters.put("serviceId", "1");
        inputParameters.put("tosca.nodes.nfv.dc.instanceId", "sub1");
        params.setInputParameters(inputParameters);
        final String str = "{\"nodeType\":\"tosca.nodes.nfv.dc\",\"inputParameters\":{\"tosca.nodes.nfv.dc.instanceId\":\"sub1\",\"serviceId\":\"1\"}}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(200);
        try {
            new MockUp<RestUtils>(){
              @Mock
              public String getRequestBody(HttpServletRequest request){
                  return str;
              }
            };
            new MockUp<RestfulUtil>()
            {
                @Mock
                public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                Map<String, String> queryParam)
                {
                    return rsp;
                }
            };
            impl.terminateService(httpRequest);
        } catch(ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testInstantiateService() {
        DriverManagerImpl impl = new DriverManagerImpl();
        HttpServletRequest httpRequest = new HttpServletRequest() {
            
            @Override
            public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setAttribute(String arg0, Object arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void removeAttribute(String arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isSecure() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public int getServerPort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getServerName() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getScheme() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RequestDispatcher getRequestDispatcher(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getRemotePort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getRemoteHost() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRemoteAddr() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRealPath(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public BufferedReader getReader() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getProtocol() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String[] getParameterValues(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getParameterNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Map getParameterMap() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getParameter(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getLocales() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getLocalPort() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getLocalName() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getLocalAddr() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ServletInputStream getInputStream() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getContentType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getContentLength() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getCharacterEncoding() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getAttributeNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Object getAttribute(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public boolean isUserInRole(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdValid() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromUrl() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromURL() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public boolean isRequestedSessionIdFromCookie() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public Principal getUserPrincipal() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpSession getSession(boolean arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpSession getSession() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getServletPath() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRequestedSessionId() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public StringBuffer getRequestURL() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRequestURI() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getRemoteUser() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getQueryString() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPathTranslated() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPathInfo() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getMethod() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getIntHeader(String arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Enumeration getHeaders(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Enumeration getHeaderNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getHeader(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public long getDateHeader(String arg0) {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Cookie[] getCookies() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getContextPath() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getAuthType() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        final String str = "{\"nodeType\":\"tosca.nodes.nfv.pop\",\"stNodeParam\":[{\"tosca.nodes.nfv.pop.param1\":\"value1\"}]}";
        final RestfulResponse rsp = new RestfulResponse();
        rsp.setStatus(202);
        String tempStr = "{\"jobId\":\"1\",\"responseDescriptor\":{\"status\":\"error\",\"progress\":\"100\"}}";
        rsp.setResponseJson(tempStr);
        try {
            new MockUp<RestUtils>(){
                @Mock
                public String getRequestBody(HttpServletRequest request){
                    return str;
                }
              };
              new MockUp<RestfulUtil>()
              {
                  @Mock
                  public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                  Map<String, String> queryParam)
                  {
                      return rsp;
                  }
              };
            impl.instantiateService(httpRequest);
        } catch(ServiceException e) {

        }
    }
    

}
