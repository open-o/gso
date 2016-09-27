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
drop table t_lcm_service_segment if exists;

create table t_lcm_service_segment
(
    serviceId    varchar(255) not null,
	serviceSegmentId varchar(255) not null,
	serviceSegmentName  varchar(255) ,
	templateId varchar(255) not null,
	nodeType varchar(255) not null,	
	topoSeqNumber int not null,
	status varchar(20) not null,
	constraint pk_t_segments primary key(serviceId, serviceSegmentId)
);

insert into t_lcm_service_segment (serviceId,serviceSegmentId,serviceSegmentName,templateId,nodeType,topoSeqNumber,status) 
VALUES('1', '12345_1', 'pop service', '12345', 'tosaca.nfv.node.POP', 1, 'createSucceed');