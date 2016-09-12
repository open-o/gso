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
package org.openo.gso.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.gso.util.json.JsonUtil;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;


public class JsonUtilTest {

    @Test
    public void testUnMarshal() {
        Person p1 = new Person();
        p1.setName("Tom");
        p1.setAddress("Beijing");
        p1.setAge(10);
        p1.setComment("good boy");
        
        String jsonStr = null;
        Person p2 = null;
        try {
            jsonStr = JsonUtil.marshal(p1);
            p2 = JsonUtil.unMarshal(jsonStr, Person.class);
            assertEquals(p1, p2);
            
        } catch(ServiceException e) {
            
        }
        
        
        String str = null;
        try{
            str = JsonUtil.marshal(p1);
            new MockUp<ObjectMapper>(){
                @Mock
                public <T> T readValue(String content, Class<T> valueType)
                        throws IOException, JsonParseException, JsonMappingException{
                    throw new IOException();
                }
            };
            p2 = JsonUtil.unMarshal(str, Person.class);
        } catch(ServiceException e) {
            
        }
        
    }

    @Test
    public void testMarshal() {
        Person person = new Person();
        person.setName("Tom");
        person.setAddress("Beijing");
        person.setAge(10);
        person.setComment("good boy");
        
        String jsonStr = null;
        try {
            jsonStr = JsonUtil.marshal(person);
            assertNotNull(jsonStr);
        } catch(ServiceException e) {
            
        }
        String result = null;
        JSONObject obj = JSONObject.fromObject(person);
        try {
            result = JsonUtil.marshal(obj);
        } catch(ServiceException e) {

        }
        assertNotNull(result);
        
    }

}
