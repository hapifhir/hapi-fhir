create sequence SEQ_BLKEXCOL_PID start with 1 increment by  50;
create sequence SEQ_BLKEXCOLFILE_PID start with 1 increment by  50;
create sequence SEQ_BLKEXJOB_PID start with 1 increment by  50;
create sequence SEQ_CNCPT_MAP_GRP_ELM_TGT_PID start with 1 increment by  50;
create sequence SEQ_CODESYSTEM_PID start with 1 increment by  50;
create sequence SEQ_CODESYSTEMVER_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_DESIG_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_MAP_GROUP_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_MAP_GRP_ELM_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_MAP_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_PC_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_PID start with 1 increment by  50;
create sequence SEQ_CONCEPT_PROP_PID start with 1 increment by  50;
create sequence SEQ_EMPI_LINK_ID start with 1 increment by  50;
create sequence SEQ_FORCEDID_ID start with 1 increment by  50;
create sequence SEQ_HISTORYTAG_ID start with 1 increment by  50;
create sequence SEQ_IDXCMPSTRUNIQ_ID start with 1 increment by  50;
create sequence SEQ_NPM_PACK start with 1 increment by  50;
create sequence SEQ_NPM_PACKVER start with 1 increment by  50;
create sequence SEQ_NPM_PACKVERRES start with 1 increment by  50;
create sequence SEQ_RES_REINDEX_JOB start with 1 increment by  50;
create sequence SEQ_RESLINK_ID start with 1 increment by  50;
create sequence SEQ_RESOURCE_HISTORY_ID start with 1 increment by  50;
create sequence SEQ_RESOURCE_ID start with 1 increment by  50;
create sequence SEQ_RESPARMPRESENT_ID start with 1 increment by  50;
create sequence SEQ_RESTAG_ID start with 1 increment by  50;
create sequence SEQ_SEARCH start with 1 increment by  50;
create sequence SEQ_SEARCH_INC start with 1 increment by  50;
create sequence SEQ_SEARCH_RES start with 1 increment by  50;
create sequence SEQ_SPIDX_COORDS start with 1 increment by  50;
create sequence SEQ_SPIDX_DATE start with 1 increment by  50;
create sequence SEQ_SPIDX_NUMBER start with 1 increment by  50;
create sequence SEQ_SPIDX_QUANTITY start with 1 increment by  50;
create sequence SEQ_SPIDX_STRING start with 1 increment by  50;
create sequence SEQ_SPIDX_TOKEN start with 1 increment by  50;
create sequence SEQ_SPIDX_URI start with 1 increment by  50;
create sequence SEQ_SUBSCRIPTION_ID start with 1 increment by  50;
create sequence SEQ_TAGDEF_ID start with 1 increment by  50;
create sequence SEQ_VALUESET_C_DSGNTN_PID start with 1 increment by  50;
create sequence SEQ_VALUESET_CONCEPT_PID start with 1 increment by  50;
create sequence SEQ_VALUESET_PID start with 1 increment by  50;

    CREATE TABLE "FLY_HFJ_MIGRATION" (
        "installed_rank" number(10,0) NOT NULL,
        "version" varchar2(50 char),
        "description" varchar2(200 char) NOT NULL,
        "type" varchar2(20 char) NOT NULL,
        "script" varchar2(1000 char) NOT NULL,
        "checksum" number(10,0),
        "installed_by" varchar2(100 char) NOT NULL,
        "installed_on" timestamp NOT NULL,
        "execution_time" number(10,0) NOT NULL,
        "success" number(1,0) NOT NULL
    );

    create table HFJ_BINARY_STORAGE_BLOB (
       BLOB_ID varchar2(200 char) not null,
        BLOB_DATA blob not null,
        CONTENT_TYPE varchar2(100 char) not null,
        BLOB_HASH varchar2(128 char),
        PUBLISHED_DATE timestamp not null,
        RESOURCE_ID varchar2(100 char) not null,
        BLOB_SIZE number(10,0),
        primary key (BLOB_ID)
    );

    create table HFJ_BLK_EXPORT_COLFILE (
       PID number(19,0) not null,
        RES_ID varchar2(100 char) not null,
        COLLECTION_PID number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_BLK_EXPORT_COLLECTION (
       PID number(19,0) not null,
        TYPE_FILTER varchar2(1000 char),
        RES_TYPE varchar2(40 char) not null,
        OPTLOCK number(10,0) not null,
        JOB_PID number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_BLK_EXPORT_JOB (
       PID number(19,0) not null,
        CREATED_TIME timestamp not null,
        EXP_TIME timestamp not null,
        JOB_ID varchar2(36 char) not null,
        REQUEST varchar2(500 char) not null,
        EXP_SINCE timestamp,
        JOB_STATUS varchar2(10 char) not null,
        STATUS_MESSAGE varchar2(500 char),
        STATUS_TIME timestamp not null,
        OPTLOCK number(10,0) not null,
        primary key (PID)
    );

    create table HFJ_FORCED_ID (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        FORCED_ID varchar2(100 char) not null,
        RESOURCE_PID number(19,0) not null,
        RESOURCE_TYPE varchar2(100 char) default '',
        primary key (PID)
    );

    create table HFJ_HISTORY_TAG (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        TAG_ID number(19,0),
        RES_VER_PID number(19,0) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(40 char) not null,
        primary key (PID)
    );

    create table HFJ_IDX_CMP_STRING_UNIQ (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        IDX_STRING varchar2(200 char) not null,
        RES_ID number(19,0),
        primary key (PID)
    );

    create table HFJ_PARTITION (
       PART_ID number(10,0) not null,
        PART_DESC varchar2(200 char),
        PART_NAME varchar2(200 char) not null,
        primary key (PART_ID)
    );

    create table HFJ_RES_LINK (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SRC_PATH varchar2(200 char) not null,
        SRC_RESOURCE_ID number(19,0) not null,
        SOURCE_RESOURCE_TYPE varchar2(40 char) not null,
        TARGET_RESOURCE_ID number(19,0),
        TARGET_RESOURCE_TYPE varchar2(40 char) not null,
        TARGET_RESOURCE_URL varchar2(200 char),
        SP_UPDATED timestamp,
        primary key (PID)
    );

    create table HFJ_RES_PARAM_PRESENT (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        HASH_PRESENCE number(19,0),
        SP_PRESENT number(1,0) not null,
        RES_ID number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_RES_REINDEX_JOB (
       PID number(19,0) not null,
        JOB_DELETED number(1,0) not null,
        REINDEX_COUNT number(10,0),
        RES_TYPE varchar2(100 char),
        SUSPENDED_UNTIL timestamp,
        UPDATE_THRESHOLD_HIGH timestamp not null,
        UPDATE_THRESHOLD_LOW timestamp,
        primary key (PID)
    );

    create table HFJ_RES_TAG (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        TAG_ID number(19,0),
        RES_ID number(19,0),
        RES_TYPE varchar2(40 char) not null,
        primary key (PID)
    );

    create table HFJ_RES_VER (
       PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        RES_DELETED_AT timestamp,
        RES_VERSION varchar2(7 char),
        HAS_TAGS number(1,0) not null,
        RES_PUBLISHED timestamp not null,
        RES_UPDATED timestamp not null,
        RES_ENCODING varchar2(5 char) not null,
        RES_TEXT blob,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(40 char) not null,
        RES_VER number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_RES_VER_PROV (
       RES_VER_PID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        REQUEST_ID varchar2(16 char),
        SOURCE_URI varchar2(100 char),
        RES_PID number(19,0) not null,
        primary key (RES_VER_PID)
    );

    create table HFJ_RESOURCE (
       RES_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        RES_DELETED_AT timestamp,
        RES_VERSION varchar2(7 char),
        HAS_TAGS number(1,0) not null,
        RES_PUBLISHED timestamp not null,
        RES_UPDATED timestamp not null,
        SP_HAS_LINKS number(1,0),
        HASH_SHA256 varchar2(64 char),
        SP_INDEX_STATUS number(19,0),
        RES_LANGUAGE varchar2(20 char),
        SP_CMPSTR_UNIQ_PRESENT number(1,0),
        SP_COORDS_PRESENT number(1,0),
        SP_DATE_PRESENT number(1,0),
        SP_NUMBER_PRESENT number(1,0),
        SP_QUANTITY_PRESENT number(1,0),
        SP_STRING_PRESENT number(1,0),
        SP_TOKEN_PRESENT number(1,0),
        SP_URI_PRESENT number(1,0),
        RES_TYPE varchar2(40 char) not null,
        RES_VER number(19,0),
        primary key (RES_ID)
    );

    create table HFJ_SEARCH (
       PID number(19,0) not null,
        CREATED timestamp not null,
        SEARCH_DELETED number(1,0),
        EXPIRY_OR_NULL timestamp,
        FAILURE_CODE number(10,0),
        FAILURE_MESSAGE varchar2(500 char),
        LAST_UPDATED_HIGH timestamp,
        LAST_UPDATED_LOW timestamp,
        NUM_BLOCKED number(10,0),
        NUM_FOUND number(10,0) not null,
        PREFERRED_PAGE_SIZE number(10,0),
        RESOURCE_ID number(19,0),
        RESOURCE_TYPE varchar2(200 char),
        SEARCH_PARAM_MAP blob,
        SEARCH_QUERY_STRING clob,
        SEARCH_QUERY_STRING_HASH number(10,0),
        SEARCH_TYPE number(10,0) not null,
        SEARCH_STATUS varchar2(10 char) not null,
        TOTAL_COUNT number(10,0),
        SEARCH_UUID varchar2(36 char) not null,
        OPTLOCK_VERSION number(10,0),
        primary key (PID)
    );

    create table HFJ_SEARCH_INCLUDE (
       PID number(19,0) not null,
        SEARCH_INCLUDE varchar2(200 char) not null,
        INC_RECURSE number(1,0) not null,
        REVINCLUDE number(1,0) not null,
        SEARCH_PID number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_SEARCH_RESULT (
       PID number(19,0) not null,
        SEARCH_ORDER number(10,0) not null,
        RESOURCE_PID number(19,0) not null,
        SEARCH_PID number(19,0) not null,
        primary key (PID)
    );

    create table HFJ_SPIDX_COORDS (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        SP_LATITUDE double precision,
        SP_LONGITUDE double precision,
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_DATE (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        SP_VALUE_HIGH timestamp,
        SP_VALUE_HIGH_DATE_ORDINAL number(10,0),
        SP_VALUE_LOW timestamp,
        SP_VALUE_LOW_DATE_ORDINAL number(10,0),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_NUMBER (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        SP_VALUE number(19,2),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_QUANTITY (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        HASH_IDENTITY_AND_UNITS number(19,0),
        HASH_IDENTITY_SYS_UNITS number(19,0),
        SP_SYSTEM varchar2(200 char),
        SP_UNITS varchar2(200 char),
        SP_VALUE number(19,2),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_STRING (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_EXACT number(19,0),
        HASH_IDENTITY number(19,0),
        HASH_NORM_PREFIX number(19,0),
        SP_VALUE_EXACT varchar2(200 char),
        SP_VALUE_NORMALIZED varchar2(200 char),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_TOKEN (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        HASH_SYS number(19,0),
        HASH_SYS_AND_VALUE number(19,0),
        HASH_VALUE number(19,0),
        SP_SYSTEM varchar2(200 char),
        SP_VALUE varchar2(200 char),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_URI (
       SP_ID number(19,0) not null,
        PARTITION_DATE date,
        PARTITION_ID number(10,0),
        SP_MISSING number(1,0) not null,
        SP_NAME varchar2(100 char) not null,
        RES_ID number(19,0) not null,
        RES_TYPE varchar2(100 char) not null,
        SP_UPDATED timestamp,
        HASH_IDENTITY number(19,0),
        HASH_URI number(19,0),
        SP_URI varchar2(254 char),
        primary key (SP_ID)
    );

    create table HFJ_SUBSCRIPTION_STATS (
       PID number(19,0) not null,
        CREATED_TIME timestamp not null,
        RES_ID number(19,0),
        primary key (PID)
    );

    create table HFJ_TAG_DEF (
       TAG_ID number(19,0) not null,
        TAG_CODE varchar2(200 char),
        TAG_DISPLAY varchar2(200 char),
        TAG_SYSTEM varchar2(200 char),
        TAG_TYPE number(10,0) not null,
        primary key (TAG_ID)
    );

    create table MPI_LINK (
       PID number(19,0) not null,
        CREATED timestamp not null,
        EID_MATCH number(1,0),
        TARGET_TYPE varchar2(40 char),
        LINK_SOURCE number(10,0) not null,
        MATCH_RESULT number(10,0) not null,
        NEW_PERSON number(1,0),
        PERSON_PID number(19,0) not null,
        SCORE double precision,
        TARGET_PID number(19,0) not null,
        UPDATED timestamp not null,
        VECTOR number(19,0),
        VERSION varchar2(16 char) not null,
        primary key (PID)
    );

    create table NPM_PACKAGE (
       PID number(19,0) not null,
        CUR_VERSION_ID varchar2(200 char),
        PACKAGE_DESC varchar2(200 char),
        PACKAGE_ID varchar2(200 char) not null,
        UPDATED_TIME timestamp not null,
        primary key (PID)
    );

    create table NPM_PACKAGE_VER (
       PID number(19,0) not null,
        CURRENT_VERSION number(1,0) not null,
        PKG_DESC varchar2(200 char),
        DESC_UPPER varchar2(200 char),
        FHIR_VERSION varchar2(10 char) not null,
        FHIR_VERSION_ID varchar2(10 char) not null,
        PACKAGE_ID varchar2(200 char) not null,
        PACKAGE_SIZE_BYTES number(19,0) not null,
        SAVED_TIME timestamp not null,
        UPDATED_TIME timestamp not null,
        VERSION_ID varchar2(200 char) not null,
        PACKAGE_PID number(19,0) not null,
        BINARY_RES_ID number(19,0) not null,
        primary key (PID)
    );

    create table NPM_PACKAGE_VER_RES (
       PID number(19,0) not null,
        CANONICAL_URL varchar2(200 char),
        CANONICAL_VERSION varchar2(200 char),
        FILE_DIR varchar2(200 char),
        FHIR_VERSION varchar2(10 char) not null,
        FHIR_VERSION_ID varchar2(10 char) not null,
        FILE_NAME varchar2(200 char),
        RES_SIZE_BYTES number(19,0) not null,
        RES_TYPE varchar2(40 char) not null,
        UPDATED_TIME timestamp not null,
        PACKVER_PID number(19,0) not null,
        BINARY_RES_ID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_CODESYSTEM (
       PID number(19,0) not null,
        CODE_SYSTEM_URI varchar2(200 char) not null,
        CURRENT_VERSION_PID number(19,0),
        CS_NAME varchar2(200 char),
        RES_ID number(19,0),
        primary key (PID)
    );

    create table TRM_CODESYSTEM_VER (
       PID number(19,0) not null,
        CS_DISPLAY varchar2(200 char),
        CODESYSTEM_PID number(19,0),
        CS_VERSION_ID varchar2(200 char),
        RES_ID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_CONCEPT (
       PID number(19,0) not null,
        CODEVAL varchar2(500 char) not null,
        CODESYSTEM_PID number(19,0),
        DISPLAY varchar2(400 char),
        INDEX_STATUS number(19,0),
        PARENT_PIDS clob,
        CODE_SEQUENCE number(10,0),
        CONCEPT_UPDATED timestamp,
        primary key (PID)
    );

    create table TRM_CONCEPT_DESIG (
       PID number(19,0) not null,
        LANG varchar2(500 char),
        USE_CODE varchar2(500 char),
        USE_DISPLAY varchar2(500 char),
        USE_SYSTEM varchar2(500 char),
        VAL varchar2(2000 char) not null,
        CS_VER_PID number(19,0),
        CONCEPT_PID number(19,0),
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP (
       PID number(19,0) not null,
        RES_ID number(19,0),
        SOURCE_URL varchar2(200 char),
        TARGET_URL varchar2(200 char),
        URL varchar2(200 char) not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP_GROUP (
       PID number(19,0) not null,
        CONCEPT_MAP_URL varchar2(200 char),
        SOURCE_URL varchar2(200 char) not null,
        SOURCE_VS varchar2(200 char),
        SOURCE_VERSION varchar2(200 char),
        TARGET_URL varchar2(200 char) not null,
        TARGET_VS varchar2(200 char),
        TARGET_VERSION varchar2(200 char),
        CONCEPT_MAP_PID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP_GRP_ELEMENT (
       PID number(19,0) not null,
        SOURCE_CODE varchar2(500 char) not null,
        CONCEPT_MAP_URL varchar2(200 char),
        SOURCE_DISPLAY varchar2(400 char),
        SYSTEM_URL varchar2(200 char),
        SYSTEM_VERSION varchar2(200 char),
        VALUESET_URL varchar2(200 char),
        CONCEPT_MAP_GROUP_PID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP_GRP_ELM_TGT (
       PID number(19,0) not null,
        TARGET_CODE varchar2(500 char) not null,
        CONCEPT_MAP_URL varchar2(200 char),
        TARGET_DISPLAY varchar2(400 char),
        TARGET_EQUIVALENCE varchar2(50 char),
        SYSTEM_URL varchar2(200 char),
        SYSTEM_VERSION varchar2(200 char),
        VALUESET_URL varchar2(200 char),
        CONCEPT_MAP_GRP_ELM_PID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_PC_LINK (
       PID number(19,0) not null,
        CHILD_PID number(19,0),
        CODESYSTEM_PID number(19,0) not null,
        PARENT_PID number(19,0),
        REL_TYPE number(10,0),
        primary key (PID)
    );

    create table TRM_CONCEPT_PROPERTY (
       PID number(19,0) not null,
        PROP_CODESYSTEM varchar2(500 char),
        PROP_DISPLAY varchar2(500 char),
        PROP_KEY varchar2(500 char) not null,
        PROP_TYPE number(10,0) not null,
        PROP_VAL varchar2(500 char),
        PROP_VAL_LOB blob,
        CS_VER_PID number(19,0),
        CONCEPT_PID number(19,0),
        primary key (PID)
    );

    create table TRM_VALUESET (
       PID number(19,0) not null,
        EXPANSION_STATUS varchar2(50 char) not null,
        VSNAME varchar2(200 char),
        RES_ID number(19,0),
        TOTAL_CONCEPT_DESIGNATIONS number(19,0) default 0 not null,
        TOTAL_CONCEPTS number(19,0) default 0 not null,
        URL varchar2(200 char) not null,
        primary key (PID)
    );

    create table TRM_VALUESET_C_DESIGNATION (
       PID number(19,0) not null,
        VALUESET_CONCEPT_PID number(19,0) not null,
        LANG varchar2(500 char),
        USE_CODE varchar2(500 char),
        USE_DISPLAY varchar2(500 char),
        USE_SYSTEM varchar2(500 char),
        VAL varchar2(2000 char) not null,
        VALUESET_PID number(19,0) not null,
        primary key (PID)
    );

    create table TRM_VALUESET_CONCEPT (
       PID number(19,0) not null,
        CODEVAL varchar2(500 char) not null,
        DISPLAY varchar2(400 char),
        VALUESET_ORDER number(10,0) not null,
        SYSTEM_URL varchar2(200 char) not null,
        VALUESET_PID number(19,0) not null,
        primary key (PID)
    );
create index IDX_BLKEX_EXPTIME on HFJ_BLK_EXPORT_JOB (EXP_TIME);

    alter table HFJ_BLK_EXPORT_JOB 
       add constraint IDX_BLKEX_JOB_ID unique (JOB_ID);

    alter table HFJ_FORCED_ID 
       add constraint IDX_FORCEDID_RESID unique (RESOURCE_PID);

    alter table HFJ_FORCED_ID 
       add constraint IDX_FORCEDID_TYPE_FID unique (RESOURCE_TYPE, FORCED_ID);

    alter table HFJ_HISTORY_TAG 
       add constraint IDX_RESHISTTAG_TAGID unique (RES_VER_PID, TAG_ID);
create index IDX_IDXCMPSTRUNIQ_RESOURCE on HFJ_IDX_CMP_STRING_UNIQ (RES_ID);

    alter table HFJ_IDX_CMP_STRING_UNIQ 
       add constraint IDX_IDXCMPSTRUNIQ_STRING unique (IDX_STRING);

    alter table HFJ_PARTITION 
       add constraint IDX_PART_NAME unique (PART_NAME);
create index IDX_RL_TPATHRES on HFJ_RES_LINK (SRC_PATH, TARGET_RESOURCE_ID);
create index IDX_RL_SRC on HFJ_RES_LINK (SRC_RESOURCE_ID);
create index IDX_RL_DEST on HFJ_RES_LINK (TARGET_RESOURCE_ID);
create index IDX_RESPARMPRESENT_RESID on HFJ_RES_PARAM_PRESENT (RES_ID);
create index IDX_RESPARMPRESENT_HASHPRES on HFJ_RES_PARAM_PRESENT (HASH_PRESENCE);

    alter table HFJ_RES_TAG 
       add constraint IDX_RESTAG_TAGID unique (RES_ID, TAG_ID);
create index IDX_RESVER_TYPE_DATE on HFJ_RES_VER (RES_TYPE, RES_UPDATED);
create index IDX_RESVER_ID_DATE on HFJ_RES_VER (RES_ID, RES_UPDATED);
create index IDX_RESVER_DATE on HFJ_RES_VER (RES_UPDATED);

    alter table HFJ_RES_VER 
       add constraint IDX_RESVER_ID_VER unique (RES_ID, RES_VER);
create index IDX_RESVERPROV_SOURCEURI on HFJ_RES_VER_PROV (SOURCE_URI);
create index IDX_RESVERPROV_REQUESTID on HFJ_RES_VER_PROV (REQUEST_ID);
create index IDX_RES_DATE on HFJ_RESOURCE (RES_UPDATED);
create index IDX_RES_LANG on HFJ_RESOURCE (RES_TYPE, RES_LANGUAGE);
create index IDX_RES_TYPE on HFJ_RESOURCE (RES_TYPE);
create index IDX_INDEXSTATUS on HFJ_RESOURCE (SP_INDEX_STATUS);
create index IDX_SEARCH_RESTYPE_HASHS on HFJ_SEARCH (RESOURCE_TYPE, SEARCH_QUERY_STRING_HASH, CREATED);
create index IDX_SEARCH_CREATED on HFJ_SEARCH (CREATED);

    alter table HFJ_SEARCH 
       add constraint IDX_SEARCH_UUID unique (SEARCH_UUID);

    alter table HFJ_SEARCH_RESULT 
       add constraint IDX_SEARCHRES_ORDER unique (SEARCH_PID, SEARCH_ORDER);
create index IDX_SP_COORDS_HASH on HFJ_SPIDX_COORDS (HASH_IDENTITY, SP_LATITUDE, SP_LONGITUDE);
create index IDX_SP_COORDS_UPDATED on HFJ_SPIDX_COORDS (SP_UPDATED);
create index IDX_SP_COORDS_RESID on HFJ_SPIDX_COORDS (RES_ID);
create index IDX_SP_DATE_HASH on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW, SP_VALUE_HIGH);
create index IDX_SP_DATE_HASH_LOW on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW);
create index IDX_SP_DATE_ORD_HASH on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW_DATE_ORDINAL, SP_VALUE_HIGH_DATE_ORDINAL);
create index IDX_SP_DATE_ORD_HASH_LOW on HFJ_SPIDX_DATE (HASH_IDENTITY, SP_VALUE_LOW_DATE_ORDINAL);
create index IDX_SP_DATE_RESID on HFJ_SPIDX_DATE (RES_ID);
create index IDX_SP_DATE_UPDATED on HFJ_SPIDX_DATE (SP_UPDATED);
create index IDX_SP_NUMBER_HASH_VAL on HFJ_SPIDX_NUMBER (HASH_IDENTITY, SP_VALUE);
create index IDX_SP_NUMBER_UPDATED on HFJ_SPIDX_NUMBER (SP_UPDATED);
create index IDX_SP_NUMBER_RESID on HFJ_SPIDX_NUMBER (RES_ID);
create index IDX_SP_QUANTITY_HASH on HFJ_SPIDX_QUANTITY (HASH_IDENTITY, SP_VALUE);
create index IDX_SP_QUANTITY_HASH_UN on HFJ_SPIDX_QUANTITY (HASH_IDENTITY_AND_UNITS, SP_VALUE);
create index IDX_SP_QUANTITY_HASH_SYSUN on HFJ_SPIDX_QUANTITY (HASH_IDENTITY_SYS_UNITS, SP_VALUE);
create index IDX_SP_QUANTITY_UPDATED on HFJ_SPIDX_QUANTITY (SP_UPDATED);
create index IDX_SP_QUANTITY_RESID on HFJ_SPIDX_QUANTITY (RES_ID);
create index IDX_SP_STRING_HASH_IDENT on HFJ_SPIDX_STRING (HASH_IDENTITY);
create index IDX_SP_STRING_HASH_NRM on HFJ_SPIDX_STRING (HASH_NORM_PREFIX, SP_VALUE_NORMALIZED);
create index IDX_SP_STRING_HASH_EXCT on HFJ_SPIDX_STRING (HASH_EXACT);
create index IDX_SP_STRING_UPDATED on HFJ_SPIDX_STRING (SP_UPDATED);
create index IDX_SP_STRING_RESID on HFJ_SPIDX_STRING (RES_ID);
create index IDX_SP_TOKEN_HASH on HFJ_SPIDX_TOKEN (HASH_IDENTITY);
create index IDX_SP_TOKEN_HASH_S on HFJ_SPIDX_TOKEN (HASH_SYS);
create index IDX_SP_TOKEN_HASH_SV on HFJ_SPIDX_TOKEN (HASH_SYS_AND_VALUE);
create index IDX_SP_TOKEN_HASH_V on HFJ_SPIDX_TOKEN (HASH_VALUE);
create index IDX_SP_TOKEN_UPDATED on HFJ_SPIDX_TOKEN (SP_UPDATED);
create index IDX_SP_TOKEN_RESID on HFJ_SPIDX_TOKEN (RES_ID);
create index IDX_SP_URI on HFJ_SPIDX_URI (RES_TYPE, SP_NAME, SP_URI);
create index IDX_SP_URI_HASH_IDENTITY on HFJ_SPIDX_URI (HASH_IDENTITY, SP_URI);
create index IDX_SP_URI_HASH_URI on HFJ_SPIDX_URI (HASH_URI);
create index IDX_SP_URI_RESTYPE_NAME on HFJ_SPIDX_URI (RES_TYPE, SP_NAME);
create index IDX_SP_URI_UPDATED on HFJ_SPIDX_URI (SP_UPDATED);
create index IDX_SP_URI_COORDS on HFJ_SPIDX_URI (RES_ID);

    alter table HFJ_SUBSCRIPTION_STATS 
       add constraint IDX_SUBSC_RESID unique (RES_ID);

    alter table HFJ_TAG_DEF 
       add constraint IDX_TAGDEF_TYPESYSCODE unique (TAG_TYPE, TAG_SYSTEM, TAG_CODE);

    alter table MPI_LINK 
       add constraint IDX_EMPI_PERSON_TGT unique (PERSON_PID, TARGET_PID);

    alter table NPM_PACKAGE 
       add constraint IDX_PACK_ID unique (PACKAGE_ID);

    alter table NPM_PACKAGE_VER 
       add constraint IDX_PACKVER unique (PACKAGE_ID, VERSION_ID);
create index IDX_PACKVERRES_URL on NPM_PACKAGE_VER_RES (CANONICAL_URL);

    alter table TRM_CODESYSTEM 
       add constraint IDX_CS_CODESYSTEM unique (CODE_SYSTEM_URI);
create index IDX_CONCEPT_INDEXSTATUS on TRM_CONCEPT (INDEX_STATUS);
create index IDX_CONCEPT_UPDATED on TRM_CONCEPT (CONCEPT_UPDATED);

    alter table TRM_CONCEPT 
       add constraint IDX_CONCEPT_CS_CODE unique (CODESYSTEM_PID, CODEVAL);

    alter table TRM_CONCEPT_MAP 
       add constraint IDX_CONCEPT_MAP_URL unique (URL);
create index IDX_CNCPT_MAP_GRP_CD on TRM_CONCEPT_MAP_GRP_ELEMENT (SOURCE_CODE);
create index IDX_CNCPT_MP_GRP_ELM_TGT_CD on TRM_CONCEPT_MAP_GRP_ELM_TGT (TARGET_CODE);

    alter table TRM_VALUESET 
       add constraint IDX_VALUESET_URL unique (URL);

    alter table TRM_VALUESET_CONCEPT 
       add constraint IDX_VS_CONCEPT_CS_CD unique (VALUESET_PID, SYSTEM_URL, CODEVAL);

    alter table TRM_VALUESET_CONCEPT 
       add constraint IDX_VS_CONCEPT_ORDER unique (VALUESET_PID, VALUESET_ORDER);

    alter table HFJ_BLK_EXPORT_COLFILE 
       add constraint FK_BLKEXCOLFILE_COLLECT 
       foreign key (COLLECTION_PID) 
       references HFJ_BLK_EXPORT_COLLECTION;

    alter table HFJ_BLK_EXPORT_COLLECTION 
       add constraint FK_BLKEXCOL_JOB 
       foreign key (JOB_PID) 
       references HFJ_BLK_EXPORT_JOB;

    alter table HFJ_FORCED_ID 
       add constraint FK_FORCEDID_RESOURCE 
       foreign key (RESOURCE_PID) 
       references HFJ_RESOURCE;

    alter table HFJ_HISTORY_TAG 
       add constraint FKtderym7awj6q8iq5c51xv4ndw 
       foreign key (TAG_ID) 
       references HFJ_TAG_DEF;

    alter table HFJ_HISTORY_TAG 
       add constraint FK_HISTORYTAG_HISTORY 
       foreign key (RES_VER_PID) 
       references HFJ_RES_VER;

    alter table HFJ_IDX_CMP_STRING_UNIQ 
       add constraint FK_IDXCMPSTRUNIQ_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_RES_LINK 
       add constraint FK_RESLINK_SOURCE 
       foreign key (SRC_RESOURCE_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_RES_LINK 
       add constraint FK_RESLINK_TARGET 
       foreign key (TARGET_RESOURCE_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_RES_PARAM_PRESENT 
       add constraint FK_RESPARMPRES_RESID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_RES_TAG 
       add constraint FKbfcjbaftmiwr3rxkwsy23vneo 
       foreign key (TAG_ID) 
       references HFJ_TAG_DEF;

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

    alter table HFJ_RES_VER_PROV 
       add constraint FK_RESVERPROV_RESVER_PID 
       foreign key (RES_VER_PID) 
       references HFJ_RES_VER;

    alter table HFJ_SEARCH_INCLUDE 
       add constraint FK_SEARCHINC_SEARCH 
       foreign key (SEARCH_PID) 
       references HFJ_SEARCH;

    alter table HFJ_SPIDX_COORDS 
       add constraint FKc97mpk37okwu8qvtceg2nh9vn 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_DATE 
       add constraint FK17s70oa59rm9n61k9thjqrsqm 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_NUMBER 
       add constraint FKcltihnc5tgprj9bhpt7xi5otb 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_QUANTITY 
       add constraint FKn603wjjoi1a6asewxbbd78bi5 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_STRING 
       add constraint FK_SPIDXSTR_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_TOKEN 
       add constraint FK7ulx3j1gg3v7maqrejgc7ybc4 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SPIDX_URI 
       add constraint FKgxsreutymmfjuwdswv3y887do 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table HFJ_SUBSCRIPTION_STATS 
       add constraint FK_SUBSC_RESOURCE_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table MPI_LINK 
       add constraint FK_EMPI_LINK_PERSON 
       foreign key (PERSON_PID) 
       references HFJ_RESOURCE;

    alter table MPI_LINK 
       add constraint FK_EMPI_LINK_TARGET 
       foreign key (TARGET_PID) 
       references HFJ_RESOURCE;

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
