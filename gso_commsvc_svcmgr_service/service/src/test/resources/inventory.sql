-- Copyright 2016-2017 Huawei Technologies Co., Ltd.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
drop table if exists t_lcm_servicebaseinfo;
create table t_lcm_servicebaseinfo
(
    serviceId    varchar(255)  not null,
	serviceName  varchar(255)  not null,
	serviceType  varchar(20)   not null,
	description  varchar(255)  null,
	activeStatus varchar(20)  not null,
	status       varchar(20)  not null,	
	creator      varchar(50)  not null,
	createTime  bigint       not null,
	constraint t_lcm_servicebaseinfo primary key(serviceId)
);
drop table if exists t_lcm_inputParam_mapping;
create table t_lcm_inputParam_mapping
(
    serviceId   varchar(255)  not null,
	inputKey    varchar(255)  not null,
	inputValue  mediumtext   not null,	
	constraint gso_p_inputParam_mapping primary key(serviceId,inputKey),
	constraint gso_f_inputParam_mapping foreign key(serviceId) references t_lcm_servicebaseinfo (serviceId)
);
drop table if exists t_lcm_defPackage_mapping_inv;
create table t_lcm_defPackage_mapping_inv
(
    serviceId    varchar(36) not null,
    serviceDefId varchar(255) not null,
    templateId  varchar(255)  not null,
    primary key(serviceId)
);