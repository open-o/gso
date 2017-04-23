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
create database if not exists gsodb default charset utf8 collate utf8_general_ci;
use gsodb;

drop table if exists t_lcm_service;

create table t_lcm_service
(
    serviceId    varchar(36) not null,
    name         varchar(255) not null,
    description  varchar(255) null,
    activeStatus varchar(20) not null,
    status       varchar(20) not null,
    creator      varchar(50) not null,
    createAt     bigint(13)   not null,
    segmentNumber     int default 0,
    primary key(serviceId)
)
engine=innodb;

drop table if exists t_lcm_defPackage_mapping;

 
create table t_lcm_defPackage_mapping
(
    serviceId    varchar(36) not null,
    serviceDefId varchar(255) not null,
    templateId  varchar(255)  not null,
    templateName  varchar(255)  null,
    primary key(serviceId)
)
engine=innodb;


drop table if exists t_lcm_service_parameter;

create table t_lcm_service_parameter
(
    serviceId    varchar(36) not null,
    paramName varchar(36) not null,
    paramValue  mediumtext not null,    
    constraint pk_t_parameters primary key(serviceId, paramName)
)
engine=innodb;


drop table if exists t_lcm_service_segment;

create table t_lcm_service_segment
(
    serviceId    varchar(36) not null,
    serviceSegmentId varchar(255) not null,
    serviceSegmentType varchar(20) not null,
    serviceSegmentName  varchar(255) ,
    templateId varchar(255) not null,
    nodeType varchar(255) not null,    
    topoSeqNumber int not null,
    domainHost varchar(100),
    nodeTemplateName varchar(255) not null,
    constraint pk_t_segments primary key(serviceId, serviceSegmentId, serviceSegmentType)
)
engine=innodb;


drop table if exists t_lcm_svcsegment_operation;

create table t_lcm_svcsegment_operation
(
    serviceSegmentId varchar(255) not null,
    serviceSegmentType varchar(20)  not null,
    operationType varchar(20) not null,
    serviceId varchar(36),
    jobId  varchar(255),
    status varchar(20) ,
    progress int,    
    errorCode int,
    statusDescription varchar(255) ,
    constraint pk_t_seg_operation primary key(serviceSegmentId, serviceSegmentType, operationType)
)
engine=innodb;


drop table if exists t_lcm_service_operation;

create table t_lcm_service_operation
(
    serviceId varchar(36) not null,
    operationId varchar(36) not null,
    operationType varchar(36)  not null,
    userId  varchar(36) ,
    result varchar(20) not null,
    operationContent varchar(255) default '',
    progress int not null,
    reason varchar(255),
    operateAt bigint(13) not null,
    finishedAt bigint(13) not null,
    constraint pk_t_svc_operation primary key(serviceId, operationId)
)
engine=innodb;

commit;

