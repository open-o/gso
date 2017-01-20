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
drop table t_lcm_service_parameter if exists;

create table t_lcm_service_parameter
(
    serviceId    varchar(255) not null,
	paramName varchar(255) not null,
	paramValue  mediumtext not null,	
	constraint pk_t_parameters primary key(serviceId, paramName)
);

insert into t_lcm_service_parameter (serviceId,paramName,paramValue) 
VALUES('1', 'pop.name', 'testPop');

insert into t_lcm_service_parameter (serviceId,paramName,paramValue)
VALUES('3', 'dc.name', 'testDc');