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
drop table t_lcm_service if exists;

create table t_lcm_service
(
    serviceId    varchar(255) not null,
	name         varchar(255) not null,
	description  varchar(255) null,
	activeStatus varchar(255) not null,
	status       varchar(255) not null,
	creator      varchar(255) not null,
	createAt     bigint(13)   not null,
	result       varchar(255) not null,
	primary key(serviceId)
);

insert into t_lcm_service (serviceId,name,description,activeStatus,status,creator,createAt,result) 
VALUES('1', 'siteToDC', 'site to DC', 'active', 'createSucceed', 'admin', 1234567890123,'creating');

insert into t_lcm_service (serviceId,name,description,activeStatus,status,creator,createAt,result) 
VALUES('3', 'siteToDC3', 'site to DC', 'active', 'createSucceed', 'admin', 1234567890123,'creating');