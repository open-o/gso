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

package org.openo.gso.model.drivermo;

import java.util.List;

/**
 * <br>
 * <p>
 * </p>
 * model service node
 * 
 * @author
 * @version GSO 0.5 2016/9/3
 */
public class ServiceNode {

    /**
     * name of node template
     */
    private String nodeTemplateName;

    /**
     * input parameters
     */
    private List<SegmentInputParameter> segments;

    /**
     * @return Returns the nodeTemplateName.
     */
    public String getNodeTemplateName() {
        return nodeTemplateName;
    }

    /**
     * @param nodeTemplateName The nodeTemplateName to set.
     */
    public void setNodeTemplateName(String nodeTemplateName) {
        this.nodeTemplateName = nodeTemplateName;
    }

    /**
     * @return Returns the segments.
     */
    public List<SegmentInputParameter> getSegments() {
        return segments;
    }

    /**
     * @param segments The segments to set.
     */
    public void setSegments(List<SegmentInputParameter> segments) {
        this.segments = segments;
    }

}
