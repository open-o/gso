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
drop table t_lcm_defPackage_mapping if exists;

create table t_lcm_defPackage_mapping
(
    serviceId    varchar(255) not null,
	serviceDefId varchar(255) not null,
	templateId  varchar(255)  not null,	
	primary key(serviceId)
);

insert into t_lcm_defPackage_mapping (serviceId,serviceDefId,templateId) 
VALUES('1', '12345', 'gso');