create sequence SEQ_BLKEXCOL_PID start with 1 increment by 50;
create sequence SEQ_BLKEXCOLFILE_PID start with 1 increment by 50;
create sequence SEQ_BLKEXJOB_PID start with 1 increment by 50;
create sequence SEQ_BLKIMJOB_PID start with 1 increment by 50;
create sequence SEQ_BLKIMJOBFILE_PID start with 1 increment by 50;
create sequence SEQ_CNCPT_MAP_GRP_ELM_TGT_PID start with 1 increment by 50;
create sequence SEQ_CODESYSTEM_PID start with 1 increment by 50;
create sequence SEQ_CODESYSTEMVER_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_DESIG_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_MAP_GROUP_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_MAP_GRP_ELM_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_MAP_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_PC_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_PID start with 1 increment by 50;
create sequence SEQ_CONCEPT_PROP_PID start with 1 increment by 50;
create sequence SEQ_EMPI_LINK_ID start with 1 increment by 50;
create sequence SEQ_FORCEDID_ID start with 1 increment by 50;
create sequence SEQ_HFJ_REVINFO start with 1 increment by 50;
create sequence SEQ_HISTORYTAG_ID start with 1000 increment by 50;
create sequence SEQ_IDXCMBTOKNU_ID start with 1000 increment by 50;
create sequence SEQ_IDXCMPSTRUNIQ_ID start with 1000 increment by 50;
create sequence SEQ_NPM_PACK start with 1 increment by 50;
create sequence SEQ_NPM_PACKVER start with 1 increment by 50;
create sequence SEQ_NPM_PACKVERRES start with 1 increment by 50;
create sequence SEQ_RES_REINDEX_JOB start with 1 increment by 50;
create sequence SEQ_RESLINK_ID start with 1000 increment by 50;
create sequence SEQ_RESOURCE_HISTORY_ID start with 1000 increment by 50;
create sequence SEQ_RESOURCE_ID start with 1000 increment by 50;
create sequence SEQ_RESOURCE_TYPE start with 1 increment by 1;
create sequence SEQ_RESPARMPRESENT_ID start with 1000 increment by 50;
create sequence SEQ_RESTAG_ID start with 1000 increment by 50;
create sequence SEQ_SEARCH start with 1 increment by 50;
create sequence SEQ_SEARCH_INC start with 1 increment by 50;
create sequence SEQ_SEARCH_RES start with 1 increment by 50;
create sequence SEQ_SPIDX_COORDS start with 1000 increment by 50;
create sequence SEQ_SPIDX_DATE start with 1000 increment by 50;
create sequence SEQ_SPIDX_IDENTITY start with 1 increment by 1;
create sequence SEQ_SPIDX_NUMBER start with 1000 increment by 50;
create sequence SEQ_SPIDX_QUANTITY start with 1000 increment by 50;
create sequence SEQ_SPIDX_QUANTITY_NRML start with 1000 increment by 50;
create sequence SEQ_SPIDX_STRING start with 1000 increment by 50;
create sequence SEQ_SPIDX_TOKEN start with 1000 increment by 50;
create sequence SEQ_SPIDX_URI start with 1000 increment by 50;
create sequence SEQ_SUBSCRIPTION_ID start with 1 increment by 50;
create sequence SEQ_TAGDEF_ID start with 1 increment by 50;
create sequence SEQ_VALUESET_C_DSGNTN_PID start with 1 increment by 50;
create sequence SEQ_VALUESET_CONCEPT_PID start with 1 increment by 50;
create sequence SEQ_VALUESET_PID start with 1 increment by 50;

CREATE TABLE "FLY_HFJ_MIGRATION" (
        "installed_rank" INTEGER NOT NULL,
        "version" VARCHAR(50),
        "description" VARCHAR(200) NOT NULL,
        "type" VARCHAR(20) NOT NULL,
        "script" VARCHAR(1000) NOT NULL,
        "checksum" INTEGER,
        "installed_by" VARCHAR(100) NOT NULL,
        "installed_on" DATE NOT NULL,
        "execution_time" INTEGER NOT NULL,
        "success" bit NOT NULL,
        "result" VARCHAR(100));

create table BT2_JOB_INSTANCE (
        ID varchar(100) not null,
        JOB_CANCELLED bit not null,
        CMB_RECS_PROCESSED int,
        CMB_RECS_PER_SEC float(53),
        CREATE_TIME datetime2(6) not null,
        CUR_GATED_STEP_ID varchar(100),
        DEFINITION_ID varchar(100) not null,
        DEFINITION_VER int not null,
        END_TIME datetime2(6),
        ERROR_COUNT int not null,
        ERROR_MSG varchar(500),
        EST_REMAINING varchar(100),
        FAST_TRACKING bit,
        PARAMS_JSON varchar(2000),
        PARAMS_JSON_LOB varchar(max),
        PARAMS_JSON_VC varchar(max),
        PROGRESS_PCT float(53) not null,
        REPORT varchar(max),
        REPORT_VC varchar(max),
        START_TIME datetime2(6),
        STAT varchar(20) not null,
        TOT_ELAPSED_MILLIS int,
        CLIENT_ID varchar(200),
        USER_NAME varchar(200),
        UPDATE_TIME datetime2(6),
        USER_DATA_JSON varchar(max),
        WARNING_MSG varchar(4000),
        WORK_CHUNKS_PURGED bit not null,
        primary key (ID)
    );

create table BT2_WORK_CHUNK (
        ID varchar(100) not null,
        CREATE_TIME datetime2(6) not null,
        END_TIME datetime2(6),
        ERROR_COUNT int not null,
        ERROR_MSG varchar(500),
        INSTANCE_ID varchar(100) not null,
        DEFINITION_ID varchar(100) not null,
        DEFINITION_VER int not null,
        NEXT_POLL_TIME datetime2(6),
        POLL_ATTEMPTS int,
        RECORDS_PROCESSED int,
        SEQ int not null,
        CHUNK_DATA varchar(max),
        CHUNK_DATA_VC varchar(max),
        START_TIME datetime2(6),
        STAT varchar(20) not null,
        TGT_STEP_ID varchar(100) not null,
        UPDATE_TIME datetime2(6),
        WARNING_MSG varchar(4000),
        primary key (ID)
    );

create table HFJ_BINARY_STORAGE_BLOB (
        BLOB_ID varchar(200) not null,
        BLOB_DATA varbinary(max),
        CONTENT_TYPE varchar(100) not null,
        BLOB_HASH varchar(128),
        PUBLISHED_DATE datetime2(6) not null,
        RESOURCE_ID varchar(100) not null,
        BLOB_SIZE bigint not null,
        STORAGE_CONTENT_BIN varbinary(max),
        primary key (BLOB_ID)
    );

create table HFJ_BLK_EXPORT_COLFILE (
        PID bigint not null,
        RES_ID varchar(100) not null,
        COLLECTION_PID bigint not null,
        primary key (PID)
    );

create table HFJ_BLK_EXPORT_COLLECTION (
        PID bigint not null,
        TYPE_FILTER varchar(1000),
        RES_TYPE varchar(40) not null,
        OPTLOCK int not null,
        JOB_PID bigint not null,
        primary key (PID)
    );

create table HFJ_BLK_EXPORT_JOB (
        PID bigint not null,
        CREATED_TIME datetime2(6) not null,
        EXP_TIME datetime2(6),
        JOB_ID varchar(36) not null,
        REQUEST varchar(1024) not null,
        EXP_SINCE datetime2(6),
        JOB_STATUS varchar(10) not null,
        STATUS_MESSAGE varchar(500),
        STATUS_TIME datetime2(6) not null,
        OPTLOCK int not null,
        primary key (PID)
    );

create table HFJ_BLK_IMPORT_JOB (
        PID bigint not null,
        BATCH_SIZE int not null,
        FILE_COUNT int not null,
        JOB_DESC varchar(500),
        JOB_ID varchar(36) not null,
        ROW_PROCESSING_MODE varchar(20) not null,
        JOB_STATUS varchar(10) not null,
        STATUS_MESSAGE varchar(500),
        STATUS_TIME datetime2(6) not null,
        OPTLOCK int not null,
        primary key (PID)
    );

create table HFJ_BLK_IMPORT_JOBFILE (
        PID bigint not null,
        JOB_CONTENTS varbinary(max),
        JOB_CONTENTS_VC varchar(max),
        FILE_DESCRIPTION varchar(500),
        FILE_SEQ int not null,
        TENANT_NAME varchar(200),
        JOB_PID bigint not null,
        primary key (PID)
    );

create table HFJ_FORCED_ID (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        FORCED_ID varchar(100) not null,
        RESOURCE_PID bigint not null,
        RESOURCE_TYPE varchar(100) default '',
        primary key (PID)
    );

create table HFJ_HISTORY_TAG (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        TAG_ID bigint,
        RES_VER_PID bigint not null,
        RES_ID bigint not null,
        RES_TYPE varchar(40) not null,
        RES_TYPE_ID smallint,
        primary key (PID)
    );

create table HFJ_IDX_CMB_TOK_NU (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_COMPLETE bigint not null,
        IDX_STRING varchar(500),
        RES_ID bigint,
        primary key (PID)
    );

create table HFJ_IDX_CMP_STRING_UNIQ (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_COMPLETE bigint,
        HASH_COMPLETE_2 bigint,
        IDX_STRING varchar(500) not null,
        RES_ID bigint,
        primary key (PID)
    );

create table HFJ_PARTITION (
        PART_ID int not null,
        PART_DESC varchar(200),
        PART_NAME varchar(200) not null,
        primary key (PART_ID)
    );

create table HFJ_RES_IDENTIFIER_PT_UNIQ (
        IDENT_SYSTEM_PID bigint not null,
        IDENT_VALUE varchar(500) not null,
        FHIR_ID varchar(64) not null,
        primary key (IDENT_SYSTEM_PID, IDENT_VALUE)
    );

create table HFJ_RES_LINK (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        SRC_PATH varchar(500) not null,
        SRC_RESOURCE_ID bigint not null,
        SOURCE_RESOURCE_TYPE varchar(40) not null,
        SRC_RES_TYPE_ID smallint,
        TARGET_RES_PARTITION_DATE date,
        TARGET_RES_PARTITION_ID int,
        TARGET_RESOURCE_ID bigint,
        TARGET_RESOURCE_TYPE varchar(40) not null,
        TARGET_RES_TYPE_ID smallint,
        TARGET_RESOURCE_URL varchar(200),
        TARGET_RESOURCE_VERSION bigint,
        SP_UPDATED datetime2(6),
        primary key (PID)
    );

create table HFJ_RES_PARAM_PRESENT (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_PRESENCE bigint,
        SP_PRESENT bit not null,
        RES_ID bigint not null,
        primary key (PID)
    );

create table HFJ_RES_REINDEX_JOB (
        PID bigint not null,
        JOB_DELETED bit not null,
        REINDEX_COUNT int,
        RES_TYPE varchar(100),
        SUSPENDED_UNTIL datetime2(6),
        UPDATE_THRESHOLD_HIGH datetime2(6) not null,
        UPDATE_THRESHOLD_LOW datetime2(6),
        primary key (PID)
    );

create table HFJ_RES_SEARCH_URL (
        RES_SEARCH_URL varchar(768) not null,
        PARTITION_ID int not null,
        CREATED_TIME datetime2(6) not null,
        PARTITION_DATE date,
        RES_ID bigint not null,
        primary key (RES_SEARCH_URL, PARTITION_ID)
    );

create table HFJ_RES_SYSTEM (
        PID bigint not null,
        SYSTEM_URL varchar(500) not null,
        primary key (PID)
    );

create table HFJ_RES_TAG (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        TAG_ID bigint,
        RES_ID bigint,
        RES_TYPE varchar(40) not null,
        RES_TYPE_ID smallint,
        primary key (PID)
    );

create table HFJ_RES_VER (
        PARTITION_ID int,
        PID bigint not null,
        RES_DELETED_AT datetime2(6),
        RES_VERSION varchar(7),
        HAS_TAGS bit not null,
        RES_PUBLISHED datetime2(6) not null,
        RES_UPDATED datetime2(6) not null,
        RES_ENCODING varchar(5) not null,
        PARTITION_DATE date,
        REQUEST_ID varchar(16),
        RES_TEXT varbinary(max),
        RES_ID bigint not null,
        RES_TEXT_VC varchar(max),
        RES_TYPE varchar(40) not null,
        RES_TYPE_ID smallint,
        RES_VER bigint not null,
        SOURCE_URI varchar(768),
        primary key (PID)
    );

create table HFJ_RES_VER_PROV (
        RES_VER_PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        REQUEST_ID varchar(16),
        RES_PID bigint not null,
        SOURCE_URI varchar(768),
        primary key (RES_VER_PID)
    );

create table HFJ_RESOURCE (
        RES_ID bigint not null,
        PARTITION_ID int,
        RES_DELETED_AT datetime2(6),
        RES_VERSION varchar(7),
        HAS_TAGS bit not null,
        RES_PUBLISHED datetime2(6) not null,
        RES_UPDATED datetime2(6) not null,
        FHIR_ID varchar(64),
        SP_HAS_LINKS bit not null,
        HASH_SHA256 varchar(64),
        SP_INDEX_STATUS smallint,
        RES_LANGUAGE varchar(20),
        SP_CMPSTR_UNIQ_PRESENT bit,
        SP_CMPTOKS_PRESENT bit,
        SP_COORDS_PRESENT bit not null,
        SP_DATE_PRESENT bit not null,
        SP_NUMBER_PRESENT bit not null,
        SP_QUANTITY_NRML_PRESENT bit not null,
        SP_QUANTITY_PRESENT bit not null,
        SP_STRING_PRESENT bit not null,
        SP_TOKEN_PRESENT bit not null,
        SP_URI_PRESENT bit not null,
        PARTITION_DATE date,
        RES_TYPE varchar(40) not null,
        RES_TYPE_ID smallint,
        SEARCH_URL_PRESENT bit,
        RES_VER bigint not null,
        primary key (RES_ID)
    );

create table HFJ_RESOURCE_MODIFIED (
        RES_ID varchar(256) not null,
        RESOURCE_TYPE varchar(40) not null,
        RES_VER varchar(8) not null,
        CREATED_TIME datetime2(6) not null,
        SUMMARY_MESSAGE varchar(4000) not null,
        primary key (RES_ID, RESOURCE_TYPE, RES_VER)
    );

create table HFJ_RESOURCE_TYPE (
        RES_TYPE_ID smallint not null,
        RES_TYPE varchar(100) not null,
        primary key (RES_TYPE_ID)
    );

create table HFJ_REVINFO (
        REV bigint not null,
        REVTSTMP datetime2(6),
        primary key (REV)
    );

create table HFJ_SEARCH (
        PID bigint not null,
        CREATED datetime2(6) not null,
        SEARCH_DELETED bit,
        EXPIRY_OR_NULL datetime2(6),
        FAILURE_CODE int,
        FAILURE_MESSAGE varchar(500),
        LAST_UPDATED_HIGH datetime2(6),
        LAST_UPDATED_LOW datetime2(6),
        NUM_BLOCKED int,
        NUM_FOUND int not null,
        PARTITION_ID int,
        PREFERRED_PAGE_SIZE int,
        RESOURCE_ID bigint,
        RESOURCE_TYPE varchar(200),
        SEARCH_PARAM_MAP varbinary(max),
        SEARCH_PARAM_MAP_BIN varbinary(max),
        SEARCH_QUERY_STRING varchar(max),
        SEARCH_QUERY_STRING_HASH int,
        SEARCH_QUERY_STRING_VC varchar(max),
        SEARCH_TYPE int not null,
        SEARCH_STATUS varchar(10) not null,
        TOTAL_COUNT int,
        SEARCH_UUID varchar(48) not null,
        OPTLOCK_VERSION int,
        primary key (PID)
    );

create table HFJ_SEARCH_INCLUDE (
        PID bigint not null,
        SEARCH_INCLUDE varchar(200) not null,
        INC_RECURSE bit not null,
        REVINCLUDE bit not null,
        SEARCH_PID bigint not null,
        primary key (PID)
    );

create table HFJ_SEARCH_RESULT (
        PID bigint not null,
        SEARCH_ORDER int not null,
        RESOURCE_PARTITION_ID int,
        RESOURCE_PID bigint not null,
        SEARCH_PID bigint not null,
        primary key (PID)
    );

create table HFJ_SPIDX_COORDS (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        SP_LATITUDE float(53),
        SP_LONGITUDE float(53),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_DATE (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        SP_VALUE_HIGH datetime2(6),
        SP_VALUE_HIGH_DATE_ORDINAL int,
        SP_VALUE_LOW datetime2(6),
        SP_VALUE_LOW_DATE_ORDINAL int,
        primary key (SP_ID)
    );

create table HFJ_SPIDX_IDENTITY (
        SP_IDENTITY_ID int not null,
        HASH_IDENTITY bigint not null,
        SP_NAME varchar(256) not null,
        RES_TYPE varchar(100) not null,
        primary key (SP_IDENTITY_ID)
    );

create table HFJ_SPIDX_NUMBER (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        SP_VALUE decimal(19,2),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_QUANTITY (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        HASH_IDENTITY_AND_UNITS bigint,
        HASH_IDENTITY_SYS_UNITS bigint,
        SP_SYSTEM varchar(200),
        SP_UNITS varchar(200),
        SP_VALUE float(53),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_QUANTITY_NRML (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        HASH_IDENTITY_AND_UNITS bigint,
        HASH_IDENTITY_SYS_UNITS bigint,
        SP_SYSTEM varchar(200),
        SP_UNITS varchar(200),
        SP_VALUE float(53),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_STRING (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        HASH_EXACT bigint,
        HASH_NORM_PREFIX bigint,
        SP_VALUE_EXACT varchar(768),
        SP_VALUE_NORMALIZED varchar(768),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_TOKEN (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        HASH_SYS bigint,
        HASH_SYS_AND_VALUE bigint,
        HASH_VALUE bigint,
        SP_SYSTEM varchar(200),
        SP_VALUE varchar(200),
        primary key (SP_ID)
    );

create table HFJ_SPIDX_URI (
        SP_ID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        HASH_IDENTITY bigint,
        SP_MISSING bit not null,
        SP_NAME varchar(100),
        RES_ID bigint not null,
        RES_TYPE varchar(100),
        SP_UPDATED datetime2(6),
        HASH_URI bigint,
        SP_URI varchar(500),
        primary key (SP_ID)
    );

create table HFJ_SUBSCRIPTION_STATS (
        PID bigint not null,
        CREATED_TIME datetime2(6) not null,
        RES_ID bigint,
        primary key (PID)
    );

create table HFJ_TAG_DEF (
        TAG_ID bigint not null,
        TAG_CODE varchar(200),
        TAG_DISPLAY varchar(200),
        TAG_SYSTEM varchar(200),
        TAG_TYPE int not null,
        TAG_USER_SELECTED bit,
        TAG_VERSION varchar(30),
        primary key (TAG_ID)
    );

create table MPI_LINK (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID int,
        CREATED datetime2(6) not null,
        EID_MATCH bit,
        GOLDEN_RESOURCE_PARTITION_ID int,
        GOLDEN_RESOURCE_PID bigint not null,
        NEW_PERSON bit,
        LINK_SOURCE int not null,
        MATCH_RESULT int not null,
        TARGET_TYPE varchar(40),
        PERSON_PARTITION_ID int,
        PERSON_PID bigint not null,
        RULE_COUNT bigint,
        SCORE float(53),
        TARGET_PARTITION_ID int,
        TARGET_PID bigint not null,
        UPDATED datetime2(6) not null,
        VECTOR bigint,
        VERSION varchar(16) not null,
        primary key (PID)
    );

create table MPI_LINK_AUD (
        PID bigint not null,
        REV bigint not null,
        REVTYPE smallint,
        PARTITION_DATE date,
        PARTITION_ID int,
        CREATED datetime2(6),
        EID_MATCH bit,
        GOLDEN_RESOURCE_PARTITION_ID int,
        GOLDEN_RESOURCE_PID bigint,
        NEW_PERSON bit,
        LINK_SOURCE int,
        MATCH_RESULT int,
        TARGET_TYPE varchar(40),
        PERSON_PARTITION_ID int,
        PERSON_PID bigint,
        RULE_COUNT bigint,
        SCORE float(53),
        TARGET_PARTITION_ID int,
        TARGET_PID bigint,
        UPDATED datetime2(6),
        VECTOR bigint,
        VERSION varchar(16),
        primary key (REV, PID)
    );

create table NPM_PACKAGE (
        PID bigint not null,
        CUR_VERSION_ID varchar(200),
        PACKAGE_DESC varchar(512),
        PACKAGE_ID varchar(200) not null,
        UPDATED_TIME datetime2(6) not null,
        primary key (PID)
    );

create table NPM_PACKAGE_VER (
        PID bigint not null,
        PKG_AUTHOR varchar(512),
        AUTHOR_UPPER varchar(512),
        CURRENT_VERSION bit not null,
        PKG_DESC varchar(512),
        DESC_UPPER varchar(512),
        FHIR_VERSION varchar(10) not null,
        FHIR_VERSION_ID varchar(20) not null,
        PARTITION_ID int,
        BINARY_RES_ID bigint not null,
        PACKAGE_ID varchar(200) not null,
        PACKAGE_SIZE_BYTES bigint not null,
        SAVED_TIME datetime2(6) not null,
        UPDATED_TIME datetime2(6) not null,
        VERSION_ID varchar(200) not null,
        PACKAGE_PID bigint not null,
        primary key (PID)
    );

create table NPM_PACKAGE_VER_RES (
        PID bigint not null,
        CANONICAL_URL varchar(200),
        CANONICAL_VERSION varchar(200),
        FILE_DIR varchar(200),
        FHIR_VERSION varchar(10) not null,
        FHIR_VERSION_ID varchar(20) not null,
        FILE_NAME varchar(200),
        PARTITION_ID int,
        RES_SIZE_BYTES bigint not null,
        BINARY_RES_ID bigint not null,
        RES_TYPE varchar(40) not null,
        UPDATED_TIME datetime2(6) not null,
        PACKVER_PID bigint not null,
        primary key (PID)
    );

create table TRM_CODESYSTEM (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        CODE_SYSTEM_URI varchar(200) not null,
        CURRENT_VERSION_PARTITION_ID int,
        CURRENT_VERSION_PID bigint,
        CS_NAME varchar(200),
        RES_ID bigint not null,
        primary key (PID)
    );

create table TRM_CODESYSTEM_VER (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        CS_DISPLAY varchar(200),
        CODESYSTEM_PID bigint,
        CS_VERSION_ID varchar(200),
        RES_ID bigint not null,
        primary key (PID)
    );

create table TRM_CONCEPT (
        PID bigint not null,
        PARTITION_ID int,
        CODEVAL varchar(500) not null,
        CODESYSTEM_PID bigint not null,
        DISPLAY varchar(400),
        INDEX_STATUS smallint,
        PARENT_PIDS varchar(max),
        PARENT_PIDS_VC varchar(max),
        CODE_SEQUENCE int,
        CONCEPT_UPDATED datetime2(6),
        primary key (PID)
    );

create table TRM_CONCEPT_DESIG (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        CONCEPT_PID bigint not null,
        LANG varchar(500),
        USE_CODE varchar(500),
        USE_DISPLAY varchar(500),
        USE_SYSTEM varchar(500),
        VAL varchar(2000),
        VAL_VC varchar(max),
        CS_VER_PID bigint not null,
        primary key (PID)
    );

create table TRM_CONCEPT_MAP (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        RES_ID bigint not null,
        SOURCE_URL varchar(200),
        TARGET_URL varchar(200),
        URL varchar(200) not null,
        VER varchar(200),
        primary key (PID)
    );

create table TRM_CONCEPT_MAP_GROUP (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        CONCEPT_MAP_PID bigint not null,
        CONCEPT_MAP_URL varchar(200),
        SOURCE_URL varchar(200) not null,
        SOURCE_VS varchar(200),
        SOURCE_VERSION varchar(200),
        TARGET_URL varchar(200) not null,
        TARGET_VS varchar(200),
        TARGET_VERSION varchar(200),
        primary key (PID)
    );

create table TRM_CONCEPT_MAP_GRP_ELEMENT (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        SOURCE_CODE varchar(500) not null,
        CONCEPT_MAP_GROUP_PID bigint not null,
        CONCEPT_MAP_URL varchar(200),
        SOURCE_DISPLAY varchar(500),
        SYSTEM_URL varchar(200),
        SYSTEM_VERSION varchar(200),
        VALUESET_URL varchar(200),
        primary key (PID)
    );

create table TRM_CONCEPT_MAP_GRP_ELM_TGT (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        TARGET_CODE varchar(500),
        CONCEPT_MAP_GRP_ELM_PID bigint not null,
        CONCEPT_MAP_URL varchar(200),
        TARGET_DISPLAY varchar(500),
        TARGET_EQUIVALENCE varchar(50),
        SYSTEM_URL varchar(200),
        SYSTEM_VERSION varchar(200),
        VALUESET_URL varchar(200),
        primary key (PID)
    );

create table TRM_CONCEPT_PC_LINK (
        PID bigint not null,
        PARTITION_ID int,
        CHILD_PID bigint not null,
        CODESYSTEM_PID bigint not null,
        PARENT_PID bigint not null,
        REL_TYPE int,
        primary key (PID)
    );

create table TRM_CONCEPT_PROPERTY (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        PROP_CODESYSTEM varchar(500),
        CS_VER_PID bigint,
        CONCEPT_PID bigint not null,
        PROP_DISPLAY varchar(500),
        PROP_KEY varchar(500) not null,
        PROP_TYPE int not null,
        PROP_VAL varchar(500),
        PROP_VAL_BIN varbinary(max),
        PROP_VAL_LOB varbinary(max),
        primary key (PID)
    );

create table TRM_VALUESET (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        EXPANSION_STATUS varchar(50) not null,
        EXPANDED_AT datetime2(6),
        VSNAME varchar(200),
        RES_ID bigint not null,
        TOTAL_CONCEPT_DESIGNATIONS bigint default 0 not null,
        TOTAL_CONCEPTS bigint default 0 not null,
        URL varchar(200) not null,
        VER varchar(200),
        primary key (PID)
    );

create table TRM_VALUESET_C_DESIGNATION (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        VALUESET_CONCEPT_PID bigint not null,
        LANG varchar(500),
        USE_CODE varchar(500),
        USE_DISPLAY varchar(500),
        USE_SYSTEM varchar(500),
        VAL varchar(2000) not null,
        VALUESET_PID bigint not null,
        primary key (PID)
    );

create table TRM_VALUESET_CONCEPT (
        PID bigint not null,
        PARTITION_ID int,
        PARTITION_DATE date,
        CODEVAL varchar(500) not null,
        DISPLAY varchar(400),
        INDEX_STATUS bigint,
        VALUESET_ORDER int not null,
        SOURCE_DIRECT_PARENT_PIDS varchar(max),
        SOURCE_DIRECT_PARENT_PIDS_VC varchar(max),
        SOURCE_PID bigint,
        SYSTEM_URL varchar(200) not null,
        SYSTEM_VER varchar(200),
        VALUESET_PID bigint not null,
        primary key (PID)
    );

create index IDX_BT2JI_CT 
       on BT2_JOB_INSTANCE (CREATE_TIME);

create index IDX_BT2WC_II_SEQ 
       on BT2_WORK_CHUNK (INSTANCE_ID, SEQ);

create index IDX_BT2WC_II_SI_S_SEQ_ID 
       on BT2_WORK_CHUNK (INSTANCE_ID, TGT_STEP_ID, STAT, SEQ, ID);

create index IDX_BLKEX_EXPTIME 
       on HFJ_BLK_EXPORT_JOB (EXP_TIME);

alter table HFJ_BLK_EXPORT_JOB 
       add constraint IDX_BLKEX_JOB_ID unique (JOB_ID);

alter table HFJ_BLK_IMPORT_JOB 
       add constraint IDX_BLKIM_JOB_ID unique (JOB_ID);

create index IDX_BLKIM_JOBFILE_JOBID 
       on HFJ_BLK_IMPORT_JOBFILE (JOB_PID);

create index IDX_RESHISTTAG_RESID 
       on HFJ_HISTORY_TAG (RES_ID);

create unique nonclustered index IDX_RESHISTTAG_TAGID 
       on HFJ_HISTORY_TAG (RES_VER_PID, TAG_ID) where RES_VER_PID is not null and TAG_ID is not null;

create index IDX_IDXCMBTOKNU_STR 
       on HFJ_IDX_CMB_TOK_NU (IDX_STRING);

create index IDX_IDXCMBTOKNU_HASHC 
       on HFJ_IDX_CMB_TOK_NU (HASH_COMPLETE, RES_ID, PARTITION_ID);

create index IDX_IDXCMBTOKNU_RES 
       on HFJ_IDX_CMB_TOK_NU (RES_ID);

create index IDX_IDXCMPSTRUNIQ_RESOURCE 
       on HFJ_IDX_CMP_STRING_UNIQ (RES_ID);

alter table HFJ_IDX_CMP_STRING_UNIQ 
       add constraint IDX_IDXCMPSTRUNIQ_STRING unique (IDX_STRING);

alter table HFJ_PARTITION 
       add constraint IDX_PART_NAME unique (PART_NAME);

create index IDX_RL_SRC 
       on HFJ_RES_LINK (SRC_RESOURCE_ID);

create index IDX_RL_TGT_v2 
       on HFJ_RES_LINK (TARGET_RESOURCE_ID, SRC_PATH, SRC_RESOURCE_ID, TARGET_RESOURCE_TYPE, PARTITION_ID);

create index IDX_RL_SRCPATH_TGTURL 
       on HFJ_RES_LINK (SRC_PATH, TARGET_RESOURCE_URL, PARTITION_ID, SRC_RESOURCE_ID);

create index IDX_RESPARMPRESENT_RESID 
       on HFJ_RES_PARAM_PRESENT (RES_ID);

create index IDX_RESPARMPRESENT_HASHPRES 
       on HFJ_RES_PARAM_PRESENT (HASH_PRESENCE);

create index IDX_RESSEARCHURL_RES 
       on HFJ_RES_SEARCH_URL (RES_ID);

create index IDX_RESSEARCHURL_TIME 
       on HFJ_RES_SEARCH_URL (CREATED_TIME);

alter table HFJ_RES_SYSTEM 
       add constraint IDX_RESIDENT_SYS unique (SYSTEM_URL);

create index IDX_RES_TAG_RES_TAG 
       on HFJ_RES_TAG (RES_ID, TAG_ID, PARTITION_ID);

create index IDX_RES_TAG_TAG_RES 
       on HFJ_RES_TAG (TAG_ID, RES_ID, PARTITION_ID);

create unique nonclustered index IDX_RESTAG_TAGID 
       on HFJ_RES_TAG (RES_ID, TAG_ID) where RES_ID is not null and TAG_ID is not null;

create index IDX_RESVER_TYPE_DATE 
       on HFJ_RES_VER (RES_TYPE, RES_UPDATED, RES_ID);

create index IDX_RESVER_ID_DATE 
       on HFJ_RES_VER (RES_ID, RES_UPDATED);

create index IDX_RESVER_DATE 
       on HFJ_RES_VER (RES_UPDATED, RES_ID);

create index IDX_RESVER_ID_SRC_URI 
       on HFJ_RES_VER (SOURCE_URI, RES_ID, PARTITION_ID);

alter table HFJ_RES_VER 
       add constraint IDX_RESVER_ID_VER unique (RES_ID, RES_VER);

create index IDX_RESVERPROV_SOURCEURI 
       on HFJ_RES_VER_PROV (SOURCE_URI);

create index IDX_RESVERPROV_REQUESTID 
       on HFJ_RES_VER_PROV (REQUEST_ID);

create index IDX_RESVERPROV_RES_PID 
       on HFJ_RES_VER_PROV (RES_PID);

create index IDX_RES_DATE 
       on HFJ_RESOURCE (RES_UPDATED);

create index IDX_RES_FHIR_ID 
       on HFJ_RESOURCE (FHIR_ID);

create index IDX_RES_TYPE_DEL_UPDATED 
       on HFJ_RESOURCE (RES_TYPE, RES_DELETED_AT, RES_UPDATED, PARTITION_ID, RES_ID);

create index IDX_RES_RESID_UPDATED 
       on HFJ_RESOURCE (RES_ID, RES_UPDATED, PARTITION_ID);

create unique nonclustered index IDX_RES_TYPE_FHIR_ID 
       on HFJ_RESOURCE (RES_TYPE, FHIR_ID) where RES_TYPE is not null and FHIR_ID is not null;

alter table HFJ_RESOURCE_TYPE 
       add constraint IDX_RES_TYPE_NAME unique (RES_TYPE);

create index IDX_SEARCH_RESTYPE_HASHS 
       on HFJ_SEARCH (RESOURCE_TYPE, SEARCH_QUERY_STRING_HASH, CREATED);

create index IDX_SEARCH_CREATED 
       on HFJ_SEARCH (CREATED);

alter table HFJ_SEARCH 
       add constraint IDX_SEARCH_UUID unique (SEARCH_UUID);

create index FK_SEARCHINC_SEARCH 
       on HFJ_SEARCH_INCLUDE (SEARCH_PID);

alter table HFJ_SEARCH_RESULT 
       add constraint IDX_SEARCHRES_ORDER unique (SEARCH_PID, SEARCH_ORDER);

create index IDX_SP_COORDS_HASH_V2 
       on HFJ_SPIDX_COORDS (HASH_IDENTITY, SP_LATITUDE, SP_LONGITUDE, RES_ID, PARTITION_ID);

create index IDX_SP_COORDS_UPDATED 
       on HFJ_SPIDX_COORDS (SP_UPDATED);

create index IDX_SP_COORDS_RESID 
       on HFJ_SPIDX_COORDS (RES_ID);

create index IDX_SP_DATE_HASH_V2 
       on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW, SP_VALUE_HIGH, RES_ID, PARTITION_ID);

create index IDX_SP_DATE_HASH_HIGH_V2 
       on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_HIGH, RES_ID, PARTITION_ID);

create index IDX_SP_DATE_ORD_HASH_V2 
       on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW_DATE_ORDINAL, SP_VALUE_HIGH_DATE_ORDINAL, RES_ID, PARTITION_ID);

create index IDX_SP_DATE_ORD_HASH_HIGH_V2 
       on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_HIGH_DATE_ORDINAL, RES_ID, PARTITION_ID);

create index IDX_SP_DATE_RESID_V2 
       on HFJ_SPIDX_DATE (RES_ID, HASH_IDENTITY, SP_VALUE_LOW, SP_VALUE_HIGH, SP_VALUE_LOW_DATE_ORDINAL, SP_VALUE_HIGH_DATE_ORDINAL, PARTITION_ID);

alter table HFJ_SPIDX_IDENTITY 
       add constraint IDX_HASH_IDENTITY unique (HASH_IDENTITY);

create index IDX_SP_NUMBER_HASH_VAL_V2 
       on HFJ_SPIDX_NUMBER (HASH_IDENTITY, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_NUMBER_RESID_V2 
       on HFJ_SPIDX_NUMBER (RES_ID, HASH_IDENTITY, SP_VALUE, PARTITION_ID);

create index IDX_SP_QUANTITY_HASH_V2 
       on HFJ_SPIDX_QUANTITY (HASH_IDENTITY, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QUANTITY_HASH_UN_V2 
       on HFJ_SPIDX_QUANTITY (HASH_IDENTITY_AND_UNITS, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QUANTITY_HASH_SYSUN_V2 
       on HFJ_SPIDX_QUANTITY (HASH_IDENTITY_SYS_UNITS, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QUANTITY_RESID_V2 
       on HFJ_SPIDX_QUANTITY (RES_ID, HASH_IDENTITY, HASH_IDENTITY_SYS_UNITS, HASH_IDENTITY_AND_UNITS, SP_VALUE, PARTITION_ID);

create index IDX_SP_QNTY_NRML_HASH_V2 
       on HFJ_SPIDX_QUANTITY_NRML (HASH_IDENTITY, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QNTY_NRML_HASH_UN_V2 
       on HFJ_SPIDX_QUANTITY_NRML (HASH_IDENTITY_AND_UNITS, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QNTY_NRML_HASH_SYSUN_V2 
       on HFJ_SPIDX_QUANTITY_NRML (HASH_IDENTITY_SYS_UNITS, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_QNTY_NRML_RESID_V2 
       on HFJ_SPIDX_QUANTITY_NRML (RES_ID, HASH_IDENTITY, HASH_IDENTITY_SYS_UNITS, HASH_IDENTITY_AND_UNITS, SP_VALUE, PARTITION_ID);

create index IDX_SP_STRING_HASH_IDENT_V2 
       on HFJ_SPIDX_STRING (HASH_IDENTITY, RES_ID, PARTITION_ID);

create index IDX_SP_STRING_HASH_NRM_V2 
       on HFJ_SPIDX_STRING (HASH_NORM_PREFIX, SP_VALUE_NORMALIZED, RES_ID, PARTITION_ID);

create index IDX_SP_STRING_HASH_EXCT_V2 
       on HFJ_SPIDX_STRING (HASH_EXACT, RES_ID, PARTITION_ID);

create index IDX_SP_STRING_RESID_V2 
       on HFJ_SPIDX_STRING (RES_ID, HASH_NORM_PREFIX, PARTITION_ID);

create index IDX_SP_TOKEN_HASH_V2 
       on HFJ_SPIDX_TOKEN (HASH_IDENTITY, SP_SYSTEM, SP_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_TOKEN_HASH_S_V2 
       on HFJ_SPIDX_TOKEN (HASH_SYS, RES_ID, PARTITION_ID);

create index IDX_SP_TOKEN_HASH_SV_V2 
       on HFJ_SPIDX_TOKEN (HASH_SYS_AND_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_TOKEN_HASH_V_V2 
       on HFJ_SPIDX_TOKEN (HASH_VALUE, RES_ID, PARTITION_ID);

create index IDX_SP_TOKEN_RESID_V2 
       on HFJ_SPIDX_TOKEN (RES_ID, HASH_SYS_AND_VALUE, HASH_VALUE, HASH_SYS, HASH_IDENTITY, PARTITION_ID);

create index IDX_SP_URI_HASH_URI_V2 
       on HFJ_SPIDX_URI (HASH_URI, RES_ID, PARTITION_ID);

create index IDX_SP_URI_HASH_IDENTITY_V2 
       on HFJ_SPIDX_URI (HASH_IDENTITY, SP_URI, RES_ID, PARTITION_ID);

create index IDX_SP_URI_COORDS 
       on HFJ_SPIDX_URI (RES_ID);

create unique nonclustered index IDX_SUBSC_RESID 
       on HFJ_SUBSCRIPTION_STATS (RES_ID) where RES_ID is not null;

create index IDX_TAG_DEF_TP_CD_SYS 
       on HFJ_TAG_DEF (TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_ID, TAG_VERSION, TAG_USER_SELECTED);

create index IDX_EMPI_MATCH_TGT_VER 
       on MPI_LINK (MATCH_RESULT, TARGET_PID, VERSION);

create index IDX_EMPI_GR_TGT 
       on MPI_LINK (GOLDEN_RESOURCE_PID, TARGET_PID);

create index FK_EMPI_LINK_TARGET 
       on MPI_LINK (TARGET_PID);

create index IDX_EMPI_TGT_MR_LS 
       on MPI_LINK (TARGET_TYPE, MATCH_RESULT, LINK_SOURCE);

create index IDX_EMPI_TGT_MR_SCORE 
       on MPI_LINK (TARGET_TYPE, MATCH_RESULT, SCORE);

alter table MPI_LINK 
       add constraint IDX_EMPI_PERSON_TGT unique (PERSON_PID, TARGET_PID);

alter table NPM_PACKAGE 
       add constraint IDX_PACK_ID unique (PACKAGE_ID);

create index FK_NPM_PKV_PKG 
       on NPM_PACKAGE_VER (PACKAGE_PID);

create index FK_NPM_PKV_RESID 
       on NPM_PACKAGE_VER (BINARY_RES_ID);

alter table NPM_PACKAGE_VER 
       add constraint IDX_PACKVER unique (PACKAGE_ID, VERSION_ID);

create index IDX_PACKVERRES_URL 
       on NPM_PACKAGE_VER_RES (CANONICAL_URL);

create index FK_NPM_PACKVERRES_PACKVER 
       on NPM_PACKAGE_VER_RES (PACKVER_PID);

create index FK_NPM_PKVR_RESID 
       on NPM_PACKAGE_VER_RES (BINARY_RES_ID);

create index FK_TRMCODESYSTEM_RES 
       on TRM_CODESYSTEM (RES_ID);

create index FK_TRMCODESYSTEM_CURVER 
       on TRM_CODESYSTEM (CURRENT_VERSION_PID);

alter table TRM_CODESYSTEM 
       add constraint IDX_CS_CODESYSTEM unique (CODE_SYSTEM_URI);

create index FK_CODESYSVER_RES_ID 
       on TRM_CODESYSTEM_VER (RES_ID);

create index FK_CODESYSVER_CS_ID 
       on TRM_CODESYSTEM_VER (CODESYSTEM_PID);

create unique nonclustered index IDX_CODESYSTEM_AND_VER 
       on TRM_CODESYSTEM_VER (CODESYSTEM_PID, CS_VERSION_ID) where CODESYSTEM_PID is not null and CS_VERSION_ID is not null;

create index IDX_CONCEPT_INDEXSTATUS 
       on TRM_CONCEPT (INDEX_STATUS);

create index IDX_CONCEPT_UPDATED 
       on TRM_CONCEPT (CONCEPT_UPDATED);

alter table TRM_CONCEPT 
       add constraint IDX_CONCEPT_CS_CODE unique (CODESYSTEM_PID, CODEVAL);

create index FK_CONCEPTDESIG_CONCEPT 
       on TRM_CONCEPT_DESIG (CONCEPT_PID);

create index FK_CONCEPTDESIG_CSV 
       on TRM_CONCEPT_DESIG (CS_VER_PID);

create index FK_TRMCONCEPTMAP_RES 
       on TRM_CONCEPT_MAP (RES_ID);

create unique nonclustered index IDX_CONCEPT_MAP_URL 
       on TRM_CONCEPT_MAP (URL, VER) where URL is not null and VER is not null;

create index FK_TCMGROUP_CONCEPTMAP 
       on TRM_CONCEPT_MAP_GROUP (CONCEPT_MAP_PID);

create index IDX_CNCPT_MAP_GRP_CD 
       on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE);

create index FK_TCMGELEMENT_GROUP 
       on TRM_CONCEPT_MAP_GRP_ELEMENT (CONCEPT_MAP_GROUP_PID);

create index IDX_CNCPT_MP_GRP_ELM_TGT_CD 
       on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE);

create index FK_TCMGETARGET_ELEMENT 
       on TRM_CONCEPT_MAP_GRP_ELM_TGT (CONCEPT_MAP_GRP_ELM_PID);

create index FK_TERM_CONCEPTPC_CHILD 
       on TRM_CONCEPT_PC_LINK (CHILD_PID);

create index FK_TERM_CONCEPTPC_PARENT 
       on TRM_CONCEPT_PC_LINK (PARENT_PID);

create index FK_TERM_CONCEPTPC_CS 
       on TRM_CONCEPT_PC_LINK (CODESYSTEM_PID);

create index FK_CONCEPTPROP_CONCEPT 
       on TRM_CONCEPT_PROPERTY (CONCEPT_PID);

create index FK_CONCEPTPROP_CSV 
       on TRM_CONCEPT_PROPERTY (CS_VER_PID);

create index FK_TRMVALUESET_RES 
       on TRM_VALUESET (RES_ID);

create unique nonclustered index IDX_VALUESET_URL 
       on TRM_VALUESET (URL, VER) where URL is not null and VER is not null;

create index FK_TRM_VALUESET_CONCEPT_PID 
       on TRM_VALUESET_C_DESIGNATION (VALUESET_CONCEPT_PID);

create index FK_TRM_VSCD_VS_PID 
       on TRM_VALUESET_C_DESIGNATION (VALUESET_PID);

alter table TRM_VALUESET_CONCEPT 
       add constraint IDX_VS_CONCEPT_CSCD unique (VALUESET_PID, SYSTEM_URL, CODEVAL);

alter table TRM_VALUESET_CONCEPT 
       add constraint IDX_VS_CONCEPT_ORDER unique (VALUESET_PID, VALUESET_ORDER);

alter table BT2_WORK_CHUNK 
       add constraint FK_BT2WC_INSTANCE 
       foreign key (INSTANCE_ID) 
       references BT2_JOB_INSTANCE;

alter table HFJ_BLK_EXPORT_COLFILE 
       add constraint FK_BLKEXCOLFILE_COLLECT 
       foreign key (COLLECTION_PID) 
       references HFJ_BLK_EXPORT_COLLECTION;

alter table HFJ_BLK_EXPORT_COLLECTION 
       add constraint FK_BLKEXCOL_JOB 
       foreign key (JOB_PID) 
       references HFJ_BLK_EXPORT_JOB;

alter table HFJ_BLK_IMPORT_JOBFILE 
       add constraint FK_BLKIMJOBFILE_JOB 
       foreign key (JOB_PID) 
       references HFJ_BLK_IMPORT_JOB;

alter table HFJ_HISTORY_TAG 
       add constraint FK_HISTORYTAG_HISTORY 
       foreign key (RES_VER_PID) 
       references HFJ_RES_VER;

alter table HFJ_IDX_CMB_TOK_NU 
       add constraint FK_IDXCMBTOKNU_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_IDX_CMP_STRING_UNIQ 
       add constraint FK_IDXCMPSTRUNIQ_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_RES_LINK 
       add constraint FK_RESLINK_SOURCE 
       foreign key (SRC_RESOURCE_ID) 
       references HFJ_RESOURCE;

alter table HFJ_RES_PARAM_PRESENT 
       add constraint FK_RESPARMPRES_RESID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_RES_TAG 
       add constraint FK_RESTAG_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_RES_VER 
       add constraint FK_RESOURCE_HISTORY_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_RES_VER_PROV 
       add constraint FK_RESVERPROV_RES_PID 
       foreign key (RES_PID) 
       references HFJ_RESOURCE;

alter table HFJ_SEARCH_INCLUDE 
       add constraint FK_SEARCHINC_SEARCH 
       foreign key (SEARCH_PID) 
       references HFJ_SEARCH;

alter table HFJ_SPIDX_COORDS 
       add constraint FKC97MPK37OKWU8QVTCEG2NH9VN 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_DATE 
       add constraint FK_SP_DATE_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_NUMBER 
       add constraint FK_SP_NUMBER_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_QUANTITY 
       add constraint FK_SP_QUANTITY_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_QUANTITY_NRML 
       add constraint FK_SP_QUANTITYNM_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_STRING 
       add constraint FK_SPIDXSTR_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_TOKEN 
       add constraint FK_SP_TOKEN_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table HFJ_SPIDX_URI 
       add constraint FKGXSREUTYMMFJUWDSWV3Y887DO 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table MPI_LINK 
       add constraint FK_EMPI_LINK_GOLDEN_RESOURCE 
       foreign key (GOLDEN_RESOURCE_PID) 
       references HFJ_RESOURCE;

alter table MPI_LINK 
       add constraint FK_EMPI_LINK_PERSON 
       foreign key (PERSON_PID) 
       references HFJ_RESOURCE;

alter table MPI_LINK 
       add constraint FK_EMPI_LINK_TARGET 
       foreign key (TARGET_PID) 
       references HFJ_RESOURCE;

alter table MPI_LINK_AUD 
       add constraint FKaow7nxncloec419ars0fpp58m 
       foreign key (REV) 
       references HFJ_REVINFO;

alter table NPM_PACKAGE_VER 
       add constraint FK_NPM_PKV_PKG 
       foreign key (PACKAGE_PID) 
       references NPM_PACKAGE;

alter table NPM_PACKAGE_VER 
       add constraint FK_NPM_PKV_RESID 
       foreign key (BINARY_RES_ID) 
       references HFJ_RESOURCE;

alter table NPM_PACKAGE_VER_RES 
       add constraint FK_NPM_PACKVERRES_PACKVER 
       foreign key (PACKVER_PID) 
       references NPM_PACKAGE_VER;

alter table NPM_PACKAGE_VER_RES 
       add constraint FK_NPM_PKVR_RESID 
       foreign key (BINARY_RES_ID) 
       references HFJ_RESOURCE;

alter table TRM_CODESYSTEM 
       add constraint FK_TRMCODESYSTEM_CURVER 
       foreign key (CURRENT_VERSION_PID) 
       references TRM_CODESYSTEM_VER;

alter table TRM_CODESYSTEM 
       add constraint FK_TRMCODESYSTEM_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table TRM_CODESYSTEM_VER 
       add constraint FK_CODESYSVER_CS_ID 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM;

alter table TRM_CODESYSTEM_VER 
       add constraint FK_CODESYSVER_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table TRM_CONCEPT 
       add constraint FK_CONCEPT_PID_CS_PID 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM_VER;

alter table TRM_CONCEPT_DESIG 
       add constraint FK_CONCEPTDESIG_CSV 
       foreign key (CS_VER_PID) 
       references TRM_CODESYSTEM_VER;

alter table TRM_CONCEPT_DESIG 
       add constraint FK_CONCEPTDESIG_CONCEPT 
       foreign key (CONCEPT_PID) 
       references TRM_CONCEPT;

alter table TRM_CONCEPT_MAP 
       add constraint FK_TRMCONCEPTMAP_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table TRM_CONCEPT_MAP_GROUP 
       add constraint FK_TCMGROUP_CONCEPTMAP 
       foreign key (CONCEPT_MAP_PID) 
       references TRM_CONCEPT_MAP;

alter table TRM_CONCEPT_MAP_GRP_ELEMENT 
       add constraint FK_TCMGELEMENT_GROUP 
       foreign key (CONCEPT_MAP_GROUP_PID) 
       references TRM_CONCEPT_MAP_GROUP;

alter table TRM_CONCEPT_MAP_GRP_ELM_TGT 
       add constraint FK_TCMGETARGET_ELEMENT 
       foreign key (CONCEPT_MAP_GRP_ELM_PID) 
       references TRM_CONCEPT_MAP_GRP_ELEMENT;

alter table TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_CHILD 
       foreign key (CHILD_PID) 
       references TRM_CONCEPT;

alter table TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_CS 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM_VER;

alter table TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_PARENT 
       foreign key (PARENT_PID) 
       references TRM_CONCEPT;

alter table TRM_CONCEPT_PROPERTY 
       add constraint FK_CONCEPTPROP_CSV 
       foreign key (CS_VER_PID) 
       references TRM_CODESYSTEM_VER;

alter table TRM_CONCEPT_PROPERTY 
       add constraint FK_CONCEPTPROP_CONCEPT 
       foreign key (CONCEPT_PID) 
       references TRM_CONCEPT;

alter table TRM_VALUESET 
       add constraint FK_TRMVALUESET_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

alter table TRM_VALUESET_C_DESIGNATION 
       add constraint FK_TRM_VALUESET_CONCEPT_PID 
       foreign key (VALUESET_CONCEPT_PID) 
       references TRM_VALUESET_CONCEPT;

alter table TRM_VALUESET_C_DESIGNATION 
       add constraint FK_TRM_VSCD_VS_PID 
       foreign key (VALUESET_PID) 
       references TRM_VALUESET;

alter table TRM_VALUESET_CONCEPT 
       add constraint FK_TRM_VALUESET_PID 
       foreign key (VALUESET_PID) 
       references TRM_VALUESET;

