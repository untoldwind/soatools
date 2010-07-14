
use esbdata;

--
-- Table structure for table `MESSAGE`
--

DROP TABLE IF EXISTS `MESSAGE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MESSAGE` (
  `uuid` varchar(128) collate utf8_bin NOT NULL,
  `message` longtext collate utf8_bin NOT NULL,
  `type` varchar(128) collate utf8_bin NOT NULL,
  `delivered` varchar(10) collate utf8_bin NOT NULL,
  `classification` varchar(10) collate utf8_bin default NULL,
  PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MLOG_JMS_DLQ`
--

DROP TABLE IF EXISTS `MLOG_JMS_DLQ`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MLOG_JMS_DLQ` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CONTENT` longtext collate utf8_bin,
  `LOG_TIMESTAMP` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MLOG_MESSAGE`
--

DROP TABLE IF EXISTS `MLOG_MESSAGE`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MLOG_MESSAGE` (
  `ID` bigint(20) NOT NULL auto_increment,
  `STATE` varchar(255) collate utf8_bin default NULL,
  `CONTENT` longtext collate utf8_bin,
  `SERVICE_NAME` varchar(255) collate utf8_bin default NULL,
  `MESSAGE_ID` varchar(255) collate utf8_bin default NULL,
  `LOG_ENTER` datetime default NULL,
  `LOG_LEAVE` datetime default NULL,
  `SERVICE_CATEGORY` varchar(255) collate utf8_bin default NULL,
  `CORRELATION_ID` varchar(255) collate utf8_bin default NULL,
  `MESSAGE_TO` varchar(1000) collate utf8_bin,
  `MESSAGE_FROM` varchar(1000) collate utf8_bin,
  `MESSAGE_REPLYTO` varchar(1000) collate utf8_bin,
  `MESSAGE_FAULTTO` varchar(1000) collate utf8_bin,
  `MESSAGE_TYPE` varchar(255) collate utf8_bin default NULL,
  `JBPM_PROCESSINSTANCE_ID` bigint(20) default NULL,
  `JBPM_TOKEN_ID` bigint(20) default NULL,
  `JBPM_NODE_ID` bigint(20) default NULL,
  `FAULT_REASON` varchar(1000) collate utf8_bin,
  `FAULT_CAUSE` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MLOG_TAG`
--

DROP TABLE IF EXISTS `MLOG_TAG`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MLOG_TAG` (
  `ID` bigint(20) NOT NULL auto_increment,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `TAG_VALUE` varchar(1000) collate utf8_bin default NULL,
  `MESSAGE_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK7DF7E6F2D7CA2EFF` (`MESSAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MUTIL_SPLIT`
--

DROP TABLE IF EXISTS `MUTIL_SPLIT`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MUTIL_SPLIT` (
  `ID` bigint(20) NOT NULL auto_increment,
  `SERVICE_NAME` varchar(255) collate utf8_bin default NULL,
  `SERVICE_CATEGORY` varchar(255) collate utf8_bin default NULL,
  `CREATED_AT` datetime default NULL,
  `PART_COUNT` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `MUTIL_SPLIT_PART`
--

DROP TABLE IF EXISTS `MUTIL_SPLIT_PART`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `MUTIL_SPLIT_PART` (
  `ID` bigint(20) NOT NULL auto_increment,
  `CONTENT` longblob,
  `FAULT_CODE` varchar(255) collate utf8_bin default NULL,
  `FAULT_REASON` varchar(1000) collate utf8_bin default NULL,
  `PART_INDEX` int(11) default NULL,
  `RECEIVED_AT` datetime default NULL,
  `SPLIT_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK62E23288100473A4` (`SPLIT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

create index MLOG_TAG_NAMEVALUE_IDX on MLOG_TAG(NAME, TAG_VALUE, MESSAGE_ID);

create index MLOG_MESSAGE_SERVICE_IDX on MLOG_MESSAGE (SERVICE_CATEGORY, SERVICE_NAME, ID);

create index MLOG_MESSAGE_ENTERLEAVE_IDX on MLOG_MESSAGE (LOG_ENTER, LOG_LEAVE, ID);