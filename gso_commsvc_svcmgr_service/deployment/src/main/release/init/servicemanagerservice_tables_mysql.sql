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
use gsodb
/
drop table if exists t_lcm_service
/
create table t_lcm_service
(
    serviceId    varchar(255) not null,
	name         varchar(255) not null,
	description  varchar(255) null,
	activeStatus varchar(255) not null,
	status       varchar(255) not null,
	creator      varchar(255) not null,
	createAt     bigint(13)   not null,
	primary key(serviceId)
)
engine=innodb
/
drop table if exists t_lcm_defPackage_mapping
/
create table t_lcm_defPackage_mapping 
create table t_lcm_defPackage_mapping
(
    serviceId    varchar(255) not null,
	serviceDefId varchar(255) not null,
	templateId  varchar(255)  not null,
	templateName varchar(255) not null,
	primary key(serviceId)
)
engine=innodb
/

drop table if exists t_lcm_service_parameter
/
create table t_lcm_service_parameter
(
    serviceId    varchar(255) not null,
	paramName varchar(255) not null,
	paramValue  varchar(255) not null,	
	constraint pk_t_parameters primary key(serviceId, paramName)
)
engine=innodb
/

drop table if exists t_lcm_service_segment
/
create table t_lcm_service_segment
(
    serviceId    varchar(255) not null,
	serviceSegmentId varchar(255) not null,
	serviceSegmentName  varchar(255) ,
	templateId varchar(255) not null,
	nodeType varchar(255) not null,	
	topoSeqNumber int not null,
	status varchar(20) ,
	constraint pk_t_segments primary key(serviceId, serviceSegmentId)
)
engine=innodb
/
commit
/
