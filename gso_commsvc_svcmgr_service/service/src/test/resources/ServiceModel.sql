drop table t_lcm_service if exists;

create table t_lcm_service
(
    serviceId    varchar(255) not null,
	name         varchar(255) not null,
	description  varchar(255) ,
	activeStatus varchar(255) not null,
	status       varchar(255) not null,
	creator      varchar(255) not null,
	createAt     bigint(13)   not null,
	primary key(serviceId)
);

insert into t_lcm_service (serviceId,name,description,activeStatus,status,creator,createAt) 
VALUES('1', 'siteToDC', 'site to DC', 'active', 'createSucceed', 'admin', 1234567890123);

insert into t_lcm_service (serviceId,name,description,activeStatus,status,creator,createAt) 
VALUES('3', 'siteToDC3', 'site to DC', 'active', 'createSucceed', 'admin', 1234567890123);