drop table t_lcm_subService if exists;

create table t_lcm_subService
(
    serviceId    varchar(255) not null,
	subServiceId varchar(255) not null,
	subServiceName  varchar(255) ,
	templateId varchar(255) not null,
	nodeType varchar(255) not null,
	owner varchar(255) not null,
	topoSeqNumber int not null,
	constraint pk_t_subservice primary key(serviceId, subServiceId)
);

insert into t_lcm_subService (serviceId,subServiceId,subServiceName,templateId,nodeType,owner,topoSeqNumber) 
VALUES('1', '12345', 'pop service', '12345', 'tosaca.nfv.node.POP', 'nfv', 1);