drop table t_lcm_service_parameter if exists;

create table t_lcm_service_parameter
(
    serviceId    varchar(255) not null,
	paramName varchar(255) not null,
	paramValue  varchar(255) not null,	
	constraint pk_t_parameters primary key(serviceId, paramName)
);

insert into t_lcm_service_parameter (serviceId,paramName,paramValue) 
VALUES('1', 'pop.name', 'testPop');

insert into t_lcm_service_parameter (serviceId,paramName,paramValue)
VALUES('3', 'dc.name', 'testDc');