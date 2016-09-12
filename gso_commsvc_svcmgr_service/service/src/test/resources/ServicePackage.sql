drop table t_lcm_defPackage_mapping if exists;

create table t_lcm_defPackage_mapping
(
    serviceId    varchar(255) not null,
	serviceDefId varchar(255) not null,
	templateId  varchar(255)  not null,
	templateName varchar(255) not null,
	primary key(serviceId)
);

insert into t_lcm_defPackage_mapping (serviceId,serviceDefId,templateId,templateName) 
VALUES('1', '12345', '123456', 'gso');