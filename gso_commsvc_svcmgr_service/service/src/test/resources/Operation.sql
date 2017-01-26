-- Copyright 2017 Huawei Technologies Co., Ltd.
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
drop table t_lcm_service_operation if exists;
create table t_lcm_service_operation
(
    serviceId varchar(36) not null,
    operationId varchar(36) not null,
    operationType varchar(36)  not null,
    userId  varchar(36) ,
    result varchar(20) not null,
    operationContent varchar(255) ,
    progress int not null,
    reason varchar(255),
    operateAt bigint(13) not null,
    finishedAt bigint(13) not null,
    constraint pk_t_svc_operation primary key(serviceId, operationId)
);

insert into t_lcm_service_operation(serviceId,operationId,operationType,userId,result,operationContent,progress,reason,operateAt,finishedAt)
values ('1','123','create','--','finished','',100,'',1234567890,1234567890);

drop table if exists t_lcm_svcsegment_operation;
create table t_lcm_svcsegment_operation
(
    serviceSegmentId varchar(255) not null,
    serviceSegmentType varchar(20)  not null,
    operationType varchar(20) not null,
    serviceId varchar(36),
    jobId  varchar(36),
    status varchar(20) ,
    progress int,    
    errorCode int,
    statusDescription varchar(255) ,
    constraint pk_t_seg_operation primary key(serviceSegmentId, serviceSegmentType, operationType)
);