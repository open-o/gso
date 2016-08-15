DROP TABLE IF EXISTS SERVICEINFO
/
CREATE TABLE SERVICEINFO
(
    SERVICENAME VARCHAR(128) NOT NULL,
    SERVICEVERSION VARCHAR(128),
    SERVICEOWNER VARCHAR(128),	
    PRIMARY KEY (SERVICENAME),
    UNIQUE KEY (SERVICENAME)
)
ENGINE = INNODB
/
drop table if exists SD_ACTIVE_JOBS
/
create table SD_ACTIVE_JOBS 
( 
   ID                   VARCHAR(36) NOT NULL,
   VERSION              INT(11) NULL,
   TYPE                 VARCHAR(36) NULL,
   CONTEXT_TAG          BOOLEAN NULL,
   CREATED_AT           BIGINT(20) NULL,
   LAST_SCHEDULED_AT    BIGINT(20) NULL,
   SCHEDULER_ID         VARCHAR(36) NULL,
   ATTRIBUTE            LONGTEXT NULL,
   PRIMARY KEY (ID)
)
engine=innodb
/

drop table if exists SD_INSTANCES
/
create table SD_INSTANCES
(
   INSTANCE_ID           VARCHAR(36) NOT NULL,
   REFRESHED_AT          BIGINT(20) NULL,
   PRIMARY KEY (INSTANCE_ID)
)
engine=innodb
/

drop table if exists SD_JOB_CONTEXTS
/
create table SD_JOB_CONTEXTS 
( 
   JOB_ID                   VARCHAR(36) NOT NULL,
   CONTEXT_SEGMENT          VARCHAR(1024) NULL,
   POSITION                 INT NULL,
   primary key (JOB_ID, POSITION)
)
engine=innodb
/
commit
/
