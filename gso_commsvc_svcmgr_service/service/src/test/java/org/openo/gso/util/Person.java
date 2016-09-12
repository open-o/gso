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

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version     GSO 0.5  2016/9/3
 */
public class Person {
    
    private String name;
    
    private int age;
    
    private String address;
    
    private String comment;

    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @return Returns the age.
     */
    public int getAge() {
        return age;
    }

    
    /**
     * @param age The age to set.
     */
    public void setAge(int age) {
        this.age = age;
    }

    
    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }

    
    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    
    /**
     * @return Returns the comment.
     */
    public String getComment() {
        return comment;
    }

    
    /**
     * @param comment The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * <br>
     * 
     * @return
     * @since   SDNO 0.5
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + age;
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    /**
     * <br>
     * 
     * @param obj
     * @return
     * @since   SDNO 0.5
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Person other = (Person)obj;
        if(address == null) {
            if(other.address != null)
                return false;
        } else if(!address.equals(other.address))
            return false;
        if(age != other.age)
            return false;
        if(comment == null) {
            if(other.comment != null)
                return false;
        } else if(!comment.equals(other.comment))
            return false;
        if(name == null) {
            if(other.name != null)
                return false;
        } else if(!name.equals(other.name))
            return false;
        return true;
    }
    
    

}
