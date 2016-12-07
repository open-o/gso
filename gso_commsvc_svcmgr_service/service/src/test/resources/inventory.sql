-- Copyright 2016 Huawei Technologies Co., Ltd.
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
drop table t_lcm_servicebaseinfo if exists;
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
drop table t_lcm_defPackage_mapping if exists;
create table t_lcm_defPackage_mapping
(
    serviceId     varchar(255)  not null,
	serviceDefId  varchar(255)  not null,
	templateId    varchar(255)   not null,
	templateName  varchar(255)  not null,
	constraint gso_p_defPackage_mapping primary key(serviceId),
	constraint gso_f_defPackage_mapping foreign key(serviceId) references t_lcm_servicebaseinfo (serviceId)
);
drop table t_lcm_inputParam_mapping if exists;
create table t_lcm_inputParam_mapping
(
    serviceId   varchar(255)  not null,
	inputKey    varchar(255)  not null,
	inputValue  varchar(255)   not null,	
	constraint gso_p_inputParam_mapping primary key(serviceId,inputKey),
	constraint gso_f_inputParam_mapping foreign key(serviceId) references t_lcm_servicebaseinfo (serviceId)
);