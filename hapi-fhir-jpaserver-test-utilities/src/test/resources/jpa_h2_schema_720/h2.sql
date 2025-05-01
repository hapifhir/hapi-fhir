
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

    create sequence SEQ_HISTORYTAG_ID start with 1 increment by 50;

    create sequence SEQ_IDXCMBTOKNU_ID start with 1 increment by 50;

    create sequence SEQ_IDXCMPSTRUNIQ_ID start with 1 increment by 50;

    create sequence SEQ_NPM_PACK start with 1 increment by 50;

    create sequence SEQ_NPM_PACKVER start with 1 increment by 50;

    create sequence SEQ_NPM_PACKVERRES start with 1 increment by 50;

    create sequence SEQ_RES_REINDEX_JOB start with 1 increment by 50;

    create sequence SEQ_RESLINK_ID start with 1 increment by 50;

    create sequence SEQ_RESOURCE_HISTORY_ID start with 1 increment by 50;

    create sequence SEQ_RESOURCE_ID start with 1 increment by 50;

    create sequence SEQ_RESPARMPRESENT_ID start with 1 increment by 50;

    create sequence SEQ_RESTAG_ID start with 1 increment by 50;

    create sequence SEQ_SEARCH start with 1 increment by 50;

    create sequence SEQ_SEARCH_INC start with 1 increment by 50;

    create sequence SEQ_SEARCH_RES start with 1 increment by 50;

    create sequence SEQ_SPIDX_COORDS start with 1 increment by 50;

    create sequence SEQ_SPIDX_DATE start with 1 increment by 50;

    create sequence SEQ_SPIDX_NUMBER start with 1 increment by 50;

    create sequence SEQ_SPIDX_QUANTITY start with 1 increment by 50;

    create sequence SEQ_SPIDX_QUANTITY_NRML start with 1 increment by 50;

    create sequence SEQ_SPIDX_STRING start with 1 increment by 50;

    create sequence SEQ_SPIDX_TOKEN start with 1 increment by 50;

    create sequence SEQ_SPIDX_URI start with 1 increment by 50;

    create sequence SEQ_SUBSCRIPTION_ID start with 1 increment by 50;

    create sequence SEQ_TAGDEF_ID start with 1 increment by 50;

    create sequence SEQ_VALUESET_C_DSGNTN_PID start with 1 increment by 50;

    create sequence SEQ_VALUESET_CONCEPT_PID start with 1 increment by 50;

    create sequence SEQ_VALUESET_PID start with 1 increment by 50;

    create table BT2_JOB_INSTANCE (
        ID varchar(100) not null,
        JOB_CANCELLED boolean not null,
        CMB_RECS_PROCESSED integer,
        CMB_RECS_PER_SEC float(53),
        CREATE_TIME timestamp(6) not null,
        CUR_GATED_STEP_ID varchar(100),
        DEFINITION_ID varchar(100) not null,
        DEFINITION_VER integer not null,
        END_TIME timestamp(6),
        ERROR_COUNT integer,
        ERROR_MSG varchar(500),
        EST_REMAINING varchar(100),
        FAST_TRACKING boolean,
        PARAMS_JSON varchar(2000),
        PARAMS_JSON_LOB clob,
        PARAMS_JSON_VC clob,
        PROGRESS_PCT float(53),
        REPORT clob,
        REPORT_VC clob,
        START_TIME timestamp(6),
        STAT varchar(20) not null,
        TOT_ELAPSED_MILLIS integer,
        CLIENT_ID varchar(200),
        USER_NAME varchar(200),
        UPDATE_TIME timestamp(6),
        WARNING_MSG varchar(4000),
        WORK_CHUNKS_PURGED boolean not null,
        primary key (ID)
    );

    create table BT2_WORK_CHUNK (
        ID varchar(100) not null,
        CREATE_TIME timestamp(6) not null,
        END_TIME timestamp(6),
        ERROR_COUNT integer not null,
        ERROR_MSG varchar(500),
        INSTANCE_ID varchar(100) not null,
        DEFINITION_ID varchar(100) not null,
        DEFINITION_VER integer not null,
        RECORDS_PROCESSED integer,
        SEQ integer not null,
        CHUNK_DATA clob,
        CHUNK_DATA_VC clob,
        START_TIME timestamp(6),
        STAT varchar(20) not null,
        TGT_STEP_ID varchar(100) not null,
        UPDATE_TIME timestamp(6),
        WARNING_MSG varchar(4000),
        primary key (ID)
    );

    create table HFJ_BINARY_STORAGE (
        CONTENT_ID varchar(200) not null,
        BLOB_DATA blob,
        CONTENT_TYPE varchar(100) not null,
        CONTENT_HASH varchar(128),
        PUBLISHED_DATE timestamp(6) not null,
        RESOURCE_ID varchar(100) not null,
        CONTENT_SIZE bigint,
        STORAGE_CONTENT_BIN blob,
        primary key (CONTENT_ID)
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
        OPTLOCK integer not null,
        JOB_PID bigint not null,
        primary key (PID)
    );

    create table HFJ_BLK_EXPORT_JOB (
        PID bigint not null,
        CREATED_TIME timestamp(6) not null,
        EXP_TIME timestamp(6),
        JOB_ID varchar(36) not null,
        REQUEST varchar(1024) not null,
        EXP_SINCE timestamp(6),
        JOB_STATUS varchar(10) not null,
        STATUS_MESSAGE varchar(500),
        STATUS_TIME timestamp(6) not null,
        OPTLOCK integer not null,
        primary key (PID),
        constraint IDX_BLKEX_JOB_ID unique (JOB_ID)
    );

    create table HFJ_BLK_IMPORT_JOB (
        PID bigint not null,
        BATCH_SIZE integer not null,
        FILE_COUNT integer not null,
        JOB_DESC varchar(500),
        JOB_ID varchar(36) not null,
        ROW_PROCESSING_MODE varchar(20) not null,
        JOB_STATUS varchar(10) not null,
        STATUS_MESSAGE varchar(500),
        STATUS_TIME timestamp(6) not null,
        OPTLOCK integer not null,
        primary key (PID),
        constraint IDX_BLKIM_JOB_ID unique (JOB_ID)
    );

    create table HFJ_BLK_IMPORT_JOBFILE (
        PID bigint not null,
        JOB_CONTENTS blob,
        JOB_CONTENTS_VC clob,
        FILE_DESCRIPTION varchar(500),
        FILE_SEQ integer not null,
        TENANT_NAME varchar(200),
        JOB_PID bigint not null,
        primary key (PID)
    );

    create table HFJ_FORCED_ID (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        FORCED_ID varchar(100) not null,
        RESOURCE_PID bigint not null,
        RESOURCE_TYPE varchar(100) default '',
        primary key (PID)
    );

    create table HFJ_HISTORY_TAG (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        TAG_ID bigint,
        RES_VER_PID bigint not null,
        RES_ID bigint not null,
        RES_TYPE varchar(40) not null,
        primary key (PID),
        constraint IDX_RESHISTTAG_TAGID unique (RES_VER_PID, TAG_ID)
    );

    create table HFJ_IDX_CMB_TOK_NU (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        HASH_COMPLETE bigint not null,
        IDX_STRING varchar(500) not null,
        RES_ID bigint,
        primary key (PID)
    );

    create table HFJ_IDX_CMP_STRING_UNIQ (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        IDX_STRING varchar(500) not null,
        RES_ID bigint,
        primary key (PID),
        constraint IDX_IDXCMPSTRUNIQ_STRING unique (IDX_STRING)
    );

    create table HFJ_PARTITION (
        PART_ID integer not null,
        PART_DESC varchar(200),
        PART_NAME varchar(200) not null,
        primary key (PART_ID),
        constraint IDX_PART_NAME unique (PART_NAME)
    );

    create table HFJ_RES_LINK (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SRC_PATH varchar(500) not null,
        SRC_RESOURCE_ID bigint not null,
        SOURCE_RESOURCE_TYPE varchar(40) not null,
        TARGET_RESOURCE_ID bigint,
        TARGET_RESOURCE_TYPE varchar(40) not null,
        TARGET_RESOURCE_URL varchar(200),
        TARGET_RESOURCE_VERSION bigint,
        SP_UPDATED timestamp(6),
        primary key (PID)
    );

    create table HFJ_RES_PARAM_PRESENT (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        HASH_PRESENCE bigint,
        SP_PRESENT boolean not null,
        RES_ID bigint not null,
        primary key (PID)
    );

    create table HFJ_RES_REINDEX_JOB (
        PID bigint not null,
        JOB_DELETED boolean not null,
        REINDEX_COUNT integer,
        RES_TYPE varchar(100),
        SUSPENDED_UNTIL timestamp(6),
        UPDATE_THRESHOLD_HIGH timestamp(6) not null,
        UPDATE_THRESHOLD_LOW timestamp(6),
        primary key (PID)
    );

    create table HFJ_RES_SEARCH_URL (
        RES_SEARCH_URL varchar(768) not null,
        CREATED_TIME timestamp(6) not null,
        RES_ID bigint not null,
        primary key (RES_SEARCH_URL)
    );

    create table HFJ_RES_TAG (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        TAG_ID bigint,
        RES_ID bigint,
        RES_TYPE varchar(40) not null,
        primary key (PID),
        constraint IDX_RESTAG_TAGID unique (RES_ID, TAG_ID)
    );

    create table HFJ_RES_VER (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        RES_DELETED_AT timestamp(6),
        RES_VERSION varchar(7),
        HAS_TAGS boolean not null,
        RES_PUBLISHED timestamp(6) not null,
        RES_UPDATED timestamp(6) not null,
        RES_ENCODING varchar(5) not null,
        REQUEST_ID varchar(16),
        RES_TEXT blob,
        RES_ID bigint not null,
        RES_TEXT_VC clob,
        RES_TYPE varchar(40) not null,
        RES_VER bigint not null,
        SOURCE_URI varchar(100),
        primary key (PID),
        constraint IDX_RESVER_ID_VER unique (RES_ID, RES_VER)
    );

    create table HFJ_RES_VER_PROV (
        RES_VER_PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        REQUEST_ID varchar(16),
        SOURCE_URI varchar(100),
        RES_PID bigint not null,
        primary key (RES_VER_PID)
    );

    create table HFJ_RESOURCE (
        RES_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        RES_DELETED_AT timestamp(6),
        RES_VERSION varchar(7),
        HAS_TAGS boolean not null,
        RES_PUBLISHED timestamp(6) not null,
        RES_UPDATED timestamp(6) not null,
        FHIR_ID varchar(64),
        SP_HAS_LINKS boolean,
        HASH_SHA256 varchar(64),
        SP_INDEX_STATUS bigint,
        RES_LANGUAGE varchar(20),
        SP_CMPSTR_UNIQ_PRESENT boolean,
        SP_CMPTOKS_PRESENT boolean,
        SP_COORDS_PRESENT boolean,
        SP_DATE_PRESENT boolean,
        SP_NUMBER_PRESENT boolean,
        SP_QUANTITY_NRML_PRESENT boolean,
        SP_QUANTITY_PRESENT boolean,
        SP_STRING_PRESENT boolean,
        SP_TOKEN_PRESENT boolean,
        SP_URI_PRESENT boolean,
        RES_TYPE varchar(40) not null,
        SEARCH_URL_PRESENT boolean,
        RES_VER bigint,
        primary key (RES_ID),
        constraint IDX_RES_TYPE_FHIR_ID unique (RES_TYPE, FHIR_ID)
    );

    create table HFJ_RESOURCE_MODIFIED (
        RES_ID varchar(256) not null,
        RES_VER varchar(8) not null,
        CREATED_TIME timestamp(6) not null,
        RESOURCE_TYPE varchar(40) not null,
        SUMMARY_MESSAGE varchar(4000) not null,
        primary key (RES_ID, RES_VER)
    );

    create table HFJ_REVINFO (
        REV bigint not null,
        REVTSTMP timestamp(6),
        primary key (REV)
    );

    create table HFJ_SEARCH (
        PID bigint not null,
        CREATED timestamp(6) not null,
        SEARCH_DELETED boolean,
        EXPIRY_OR_NULL timestamp(6),
        FAILURE_CODE integer,
        FAILURE_MESSAGE varchar(500),
        LAST_UPDATED_HIGH timestamp(6),
        LAST_UPDATED_LOW timestamp(6),
        NUM_BLOCKED integer,
        NUM_FOUND integer not null,
        PREFERRED_PAGE_SIZE integer,
        RESOURCE_ID bigint,
        RESOURCE_TYPE varchar(200),
        SEARCH_PARAM_MAP blob,
        SEARCH_PARAM_MAP_BIN blob,
        SEARCH_QUERY_STRING clob,
        SEARCH_QUERY_STRING_HASH integer,
        SEARCH_QUERY_STRING_VC clob,
        SEARCH_TYPE integer not null,
        SEARCH_STATUS varchar(10) not null,
        TOTAL_COUNT integer,
        SEARCH_UUID varchar(48) not null,
        OPTLOCK_VERSION integer,
        primary key (PID),
        constraint IDX_SEARCH_UUID unique (SEARCH_UUID)
    );

    create table HFJ_SEARCH_INCLUDE (
        PID bigint not null,
        SEARCH_INCLUDE varchar(200) not null,
        INC_RECURSE boolean not null,
        REVINCLUDE boolean not null,
        SEARCH_PID bigint not null,
        primary key (PID)
    );

    create table HFJ_SEARCH_RESULT (
        PID bigint not null,
        SEARCH_ORDER integer not null,
        RESOURCE_PID bigint not null,
        SEARCH_PID bigint not null,
        primary key (PID),
        constraint IDX_SEARCHRES_ORDER unique (SEARCH_PID, SEARCH_ORDER)
    );

    create table HFJ_SPIDX_COORDS (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        SP_LATITUDE float(53),
        SP_LONGITUDE float(53),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_DATE (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        SP_VALUE_HIGH timestamp(6),
        SP_VALUE_HIGH_DATE_ORDINAL integer,
        SP_VALUE_LOW timestamp(6),
        SP_VALUE_LOW_DATE_ORDINAL integer,
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_NUMBER (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        SP_VALUE decimal(19,2),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_QUANTITY (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        HASH_IDENTITY_AND_UNITS bigint,
        HASH_IDENTITY_SYS_UNITS bigint,
        SP_SYSTEM varchar(200),
        SP_UNITS varchar(200),
        SP_VALUE float(53),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_QUANTITY_NRML (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        HASH_IDENTITY_AND_UNITS bigint,
        HASH_IDENTITY_SYS_UNITS bigint,
        SP_SYSTEM varchar(200),
        SP_UNITS varchar(200),
        SP_VALUE float(53),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_STRING (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_EXACT bigint,
        HASH_IDENTITY bigint,
        HASH_NORM_PREFIX bigint,
        SP_VALUE_EXACT varchar(200),
        SP_VALUE_NORMALIZED varchar(200),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_TOKEN (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        HASH_SYS bigint,
        HASH_SYS_AND_VALUE bigint,
        HASH_VALUE bigint,
        SP_SYSTEM varchar(200),
        SP_VALUE varchar(200),
        primary key (SP_ID)
    );

    create table HFJ_SPIDX_URI (
        SP_ID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        SP_MISSING boolean not null,
        SP_NAME varchar(100) not null,
        RES_ID bigint not null,
        RES_TYPE varchar(100) not null,
        SP_UPDATED timestamp(6),
        HASH_IDENTITY bigint,
        HASH_URI bigint,
        SP_URI varchar(500),
        primary key (SP_ID),
        constraint IDX_SP_URI_HASH_URI_V2 unique (HASH_URI, RES_ID, PARTITION_ID),
        constraint IDX_SP_URI_HASH_IDENTITY_V2 unique (HASH_IDENTITY, SP_URI, RES_ID, PARTITION_ID)
    );

    create table HFJ_SUBSCRIPTION_STATS (
        PID bigint not null,
        CREATED_TIME timestamp(6) not null,
        RES_ID bigint,
        primary key (PID),
        constraint IDX_SUBSC_RESID unique (RES_ID)
    );

    create table HFJ_TAG_DEF (
        TAG_ID bigint not null,
        TAG_CODE varchar(200),
        TAG_DISPLAY varchar(200),
        TAG_SYSTEM varchar(200),
        TAG_TYPE integer not null,
        TAG_USER_SELECTED boolean,
        TAG_VERSION varchar(30),
        primary key (TAG_ID)
    );

    create table MPI_LINK (
        PID bigint not null,
        PARTITION_DATE date,
        PARTITION_ID integer,
        CREATED timestamp(6) not null,
        EID_MATCH boolean,
        GOLDEN_RESOURCE_PID bigint not null,
        NEW_PERSON boolean,
        LINK_SOURCE integer not null,
        MATCH_RESULT integer not null,
        TARGET_TYPE varchar(40),
        PERSON_PID bigint not null,
        RULE_COUNT bigint,
        SCORE float(53),
        TARGET_PID bigint not null,
        UPDATED timestamp(6) not null,
        VECTOR bigint,
        VERSION varchar(16) not null,
        primary key (PID),
        constraint IDX_EMPI_PERSON_TGT unique (PERSON_PID, TARGET_PID)
    );

    create table MPI_LINK_AUD (
        PID bigint not null,
        REV bigint not null,
        REVTYPE tinyint,
        PARTITION_DATE date,
        PARTITION_ID integer,
        CREATED timestamp(6),
        EID_MATCH boolean,
        GOLDEN_RESOURCE_PID bigint,
        NEW_PERSON boolean,
        LINK_SOURCE integer,
        MATCH_RESULT integer,
        TARGET_TYPE varchar(40),
        PERSON_PID bigint,
        RULE_COUNT bigint,
        SCORE float(53),
        TARGET_PID bigint,
        UPDATED timestamp(6),
        VECTOR bigint,
        VERSION varchar(16),
        primary key (REV, PID)
    );

    create table NPM_PACKAGE (
        PID bigint not null,
        CUR_VERSION_ID varchar(200),
        PACKAGE_DESC varchar(200),
        PACKAGE_ID varchar(200) not null,
        UPDATED_TIME timestamp(6) not null,
        primary key (PID),
        constraint IDX_PACK_ID unique (PACKAGE_ID)
    );

    create table NPM_PACKAGE_VER (
        PID bigint not null,
        CURRENT_VERSION boolean not null,
        PKG_DESC varchar(200),
        DESC_UPPER varchar(200),
        FHIR_VERSION varchar(10) not null,
        FHIR_VERSION_ID varchar(20) not null,
        PACKAGE_ID varchar(200) not null,
        PACKAGE_SIZE_BYTES bigint not null,
        SAVED_TIME timestamp(6) not null,
        UPDATED_TIME timestamp(6) not null,
        VERSION_ID varchar(200) not null,
        PACKAGE_PID bigint not null,
        BINARY_RES_ID bigint not null,
        primary key (PID),
        constraint IDX_PACKVER unique (PACKAGE_ID, VERSION_ID)
    );

    create table NPM_PACKAGE_VER_RES (
        PID bigint not null,
        CANONICAL_URL varchar(200),
        CANONICAL_VERSION varchar(200),
        FILE_DIR varchar(200),
        FHIR_VERSION varchar(10) not null,
        FHIR_VERSION_ID varchar(20) not null,
        FILE_NAME varchar(200),
        RES_SIZE_BYTES bigint not null,
        RES_TYPE varchar(40) not null,
        UPDATED_TIME timestamp(6) not null,
        PACKVER_PID bigint not null,
        BINARY_RES_ID bigint not null,
        primary key (PID)
    );

    create table TRM_CODESYSTEM (
        PID bigint not null,
        CODE_SYSTEM_URI varchar(200) not null,
        CURRENT_VERSION_PID bigint,
        CS_NAME varchar(200),
        RES_ID bigint,
        primary key (PID),
        constraint IDX_CS_CODESYSTEM unique (CODE_SYSTEM_URI)
    );

    create table TRM_CODESYSTEM_VER (
        PID bigint not null,
        CS_DISPLAY varchar(200),
        CODESYSTEM_PID bigint,
        CS_VERSION_ID varchar(200),
        RES_ID bigint not null,
        primary key (PID),
        constraint IDX_CODESYSTEM_AND_VER unique (CODESYSTEM_PID, CS_VERSION_ID)
    );

    create table TRM_CONCEPT (
        PID bigint not null,
        CODEVAL varchar(500) not null,
        CODESYSTEM_PID bigint,
        DISPLAY varchar(400),
        INDEX_STATUS bigint,
        PARENT_PIDS clob,
        PARENT_PIDS_VC clob,
        CODE_SEQUENCE integer,
        CONCEPT_UPDATED timestamp(6),
        primary key (PID),
        constraint IDX_CONCEPT_CS_CODE unique (CODESYSTEM_PID, CODEVAL)
    );

    create table TRM_CONCEPT_DESIG (
        PID bigint not null,
        LANG varchar(500),
        USE_CODE varchar(500),
        USE_DISPLAY varchar(500),
        USE_SYSTEM varchar(500),
        VAL varchar(2000) not null,
        CS_VER_PID bigint,
        CONCEPT_PID bigint,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP (
        PID bigint not null,
        RES_ID bigint,
        SOURCE_URL varchar(200),
        TARGET_URL varchar(200),
        URL varchar(200) not null,
        VER varchar(200),
        primary key (PID),
        constraint IDX_CONCEPT_MAP_URL unique (URL, VER)
    );

    create table TRM_CONCEPT_MAP_GROUP (
        PID bigint not null,
        CONCEPT_MAP_URL varchar(200),
        SOURCE_URL varchar(200) not null,
        SOURCE_VS varchar(200),
        SOURCE_VERSION varchar(200),
        TARGET_URL varchar(200) not null,
        TARGET_VS varchar(200),
        TARGET_VERSION varchar(200),
        CONCEPT_MAP_PID bigint not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP_GRP_ELEMENT (
        PID bigint not null,
        SOURCE_CODE varchar(500) not null,
        CONCEPT_MAP_URL varchar(200),
        SOURCE_DISPLAY varchar(500),
        SYSTEM_URL varchar(200),
        SYSTEM_VERSION varchar(200),
        VALUESET_URL varchar(200),
        CONCEPT_MAP_GROUP_PID bigint not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_MAP_GRP_ELM_TGT (
        PID bigint not null,
        TARGET_CODE varchar(500),
        CONCEPT_MAP_URL varchar(200),
        TARGET_DISPLAY varchar(500),
        TARGET_EQUIVALENCE varchar(50),
        SYSTEM_URL varchar(200),
        SYSTEM_VERSION varchar(200),
        VALUESET_URL varchar(200),
        CONCEPT_MAP_GRP_ELM_PID bigint not null,
        primary key (PID)
    );

    create table TRM_CONCEPT_PC_LINK (
        PID bigint not null,
        CHILD_PID bigint,
        CODESYSTEM_PID bigint not null,
        PARENT_PID bigint,
        REL_TYPE integer,
        primary key (PID)
    );

    create table TRM_CONCEPT_PROPERTY (
        PID bigint not null,
        PROP_CODESYSTEM varchar(500),
        PROP_DISPLAY varchar(500),
        PROP_KEY varchar(500) not null,
        PROP_TYPE integer not null,
        PROP_VAL varchar(500),
        PROP_VAL_BIN blob,
        PROP_VAL_LOB blob,
        CS_VER_PID bigint,
        CONCEPT_PID bigint,
        primary key (PID)
    );

    create table TRM_VALUESET (
        PID bigint not null,
        EXPANSION_STATUS varchar(50) not null,
        EXPANDED_AT timestamp(6),
        VSNAME varchar(200),
        RES_ID bigint,
        TOTAL_CONCEPT_DESIGNATIONS bigint default 0 not null,
        TOTAL_CONCEPTS bigint default 0 not null,
        URL varchar(200) not null,
        VER varchar(200),
        primary key (PID),
        constraint IDX_VALUESET_URL unique (URL, VER)
    );

    create table TRM_VALUESET_C_DESIGNATION (
        PID bigint not null,
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
        CODEVAL varchar(500) not null,
        DISPLAY varchar(400),
        INDEX_STATUS bigint,
        VALUESET_ORDER integer not null,
        SOURCE_DIRECT_PARENT_PIDS clob,
        SOURCE_DIRECT_PARENT_PIDS_VC clob,
        SOURCE_PID bigint,
        SYSTEM_URL varchar(200) not null,
        SYSTEM_VER varchar(200),
        VALUESET_PID bigint not null,
        primary key (PID),
        constraint IDX_VS_CONCEPT_CSCD unique (VALUESET_PID, SYSTEM_URL, CODEVAL),
        constraint IDX_VS_CONCEPT_ORDER unique (VALUESET_PID, VALUESET_ORDER)
    );

    create index IDX_BT2JI_CT 
       on BT2_JOB_INSTANCE (CREATE_TIME);

    create index IDX_BT2WC_II_SEQ 
       on BT2_WORK_CHUNK (INSTANCE_ID, SEQ);

    create index IDX_BLKEX_EXPTIME 
       on HFJ_BLK_EXPORT_JOB (EXP_TIME);

    create index IDX_BLKIM_JOBFILE_JOBID 
       on HFJ_BLK_IMPORT_JOBFILE (JOB_PID);

    create index IDX_RESHISTTAG_RESID 
       on HFJ_HISTORY_TAG (RES_ID);

    create index IDX_IDXCMBTOKNU_STR 
       on HFJ_IDX_CMB_TOK_NU (IDX_STRING);

    create index IDX_IDXCMBTOKNU_RES 
       on HFJ_IDX_CMB_TOK_NU (RES_ID);

    create index IDX_IDXCMPSTRUNIQ_RESOURCE 
       on HFJ_IDX_CMP_STRING_UNIQ (RES_ID);

    create index IDX_RL_SRC 
       on HFJ_RES_LINK (SRC_RESOURCE_ID);

    create index IDX_RL_TGT_v2 
       on HFJ_RES_LINK (TARGET_RESOURCE_ID, SRC_PATH, SRC_RESOURCE_ID, TARGET_RESOURCE_TYPE, PARTITION_ID);

    create index IDX_RESPARMPRESENT_RESID 
       on HFJ_RES_PARAM_PRESENT (RES_ID);

    create index IDX_RESPARMPRESENT_HASHPRES 
       on HFJ_RES_PARAM_PRESENT (HASH_PRESENCE);

    create index IDX_RESSEARCHURL_RES 
       on HFJ_RES_SEARCH_URL (RES_ID);

    create index IDX_RESSEARCHURL_TIME 
       on HFJ_RES_SEARCH_URL (CREATED_TIME);

    create index IDX_RES_TAG_RES_TAG 
       on HFJ_RES_TAG (RES_ID, TAG_ID, PARTITION_ID);

    create index IDX_RES_TAG_TAG_RES 
       on HFJ_RES_TAG (TAG_ID, RES_ID, PARTITION_ID);

    create index IDX_RESVER_TYPE_DATE 
       on HFJ_RES_VER (RES_TYPE, RES_UPDATED);

    create index IDX_RESVER_ID_DATE 
       on HFJ_RES_VER (RES_ID, RES_UPDATED);

    create index IDX_RESVER_DATE 
       on HFJ_RES_VER (RES_UPDATED);

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

    create index IDX_SEARCH_RESTYPE_HASHS 
       on HFJ_SEARCH (RESOURCE_TYPE, SEARCH_QUERY_STRING_HASH, CREATED);

    create index IDX_SEARCH_CREATED 
       on HFJ_SEARCH (CREATED);

    create index FK_SEARCHINC_SEARCH 
       on HFJ_SEARCH_INCLUDE (SEARCH_PID);

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

    create index IDX_SP_URI_COORDS 
       on HFJ_SPIDX_URI (RES_ID);

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

    create index FK_NPM_PKV_PKG 
       on NPM_PACKAGE_VER (PACKAGE_PID);

    create index FK_NPM_PKV_RESID 
       on NPM_PACKAGE_VER (BINARY_RES_ID);

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

    create index FK_CODESYSVER_RES_ID 
       on TRM_CODESYSTEM_VER (RES_ID);

    create index FK_CODESYSVER_CS_ID 
       on TRM_CODESYSTEM_VER (CODESYSTEM_PID);

    create index IDX_CONCEPT_INDEXSTATUS 
       on TRM_CONCEPT (INDEX_STATUS);

    create index IDX_CONCEPT_UPDATED 
       on TRM_CONCEPT (CONCEPT_UPDATED);

    create index FK_CONCEPTDESIG_CONCEPT 
       on TRM_CONCEPT_DESIG (CONCEPT_PID);

    create index FK_CONCEPTDESIG_CSV 
       on TRM_CONCEPT_DESIG (CS_VER_PID);

    create index FK_TRMCONCEPTMAP_RES 
       on TRM_CONCEPT_MAP (RES_ID);

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

    create index FK_TRM_VALUESET_CONCEPT_PID 
       on TRM_VALUESET_C_DESIGNATION (VALUESET_CONCEPT_PID);

    create index FK_TRM_VSCD_VS_PID 
       on TRM_VALUESET_C_DESIGNATION (VALUESET_PID);

    alter table if exists BT2_WORK_CHUNK 
       add constraint FK_BT2WC_INSTANCE 
       foreign key (INSTANCE_ID) 
       references BT2_JOB_INSTANCE;

    alter table if exists HFJ_BLK_EXPORT_COLFILE 
       add constraint FK_BLKEXCOLFILE_COLLECT 
       foreign key (COLLECTION_PID) 
       references HFJ_BLK_EXPORT_COLLECTION;

    alter table if exists HFJ_BLK_EXPORT_COLLECTION 
       add constraint FK_BLKEXCOL_JOB 
       foreign key (JOB_PID) 
       references HFJ_BLK_EXPORT_JOB;

    alter table if exists HFJ_BLK_IMPORT_JOBFILE 
       add constraint FK_BLKIMJOBFILE_JOB 
       foreign key (JOB_PID) 
       references HFJ_BLK_IMPORT_JOB;

    alter table if exists HFJ_HISTORY_TAG 
       add constraint FKtderym7awj6q8iq5c51xv4ndw 
       foreign key (TAG_ID) 
       references HFJ_TAG_DEF;

    alter table if exists HFJ_HISTORY_TAG 
       add constraint FK_HISTORYTAG_HISTORY 
       foreign key (RES_VER_PID) 
       references HFJ_RES_VER;

    alter table if exists HFJ_IDX_CMB_TOK_NU 
       add constraint FK_IDXCMBTOKNU_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_IDX_CMP_STRING_UNIQ 
       add constraint FK_IDXCMPSTRUNIQ_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_LINK 
       add constraint FK_RESLINK_SOURCE 
       foreign key (SRC_RESOURCE_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_LINK 
       add constraint FK_RESLINK_TARGET 
       foreign key (TARGET_RESOURCE_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_PARAM_PRESENT 
       add constraint FK_RESPARMPRES_RESID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_TAG 
       add constraint FKbfcjbaftmiwr3rxkwsy23vneo 
       foreign key (TAG_ID) 
       references HFJ_TAG_DEF;

    alter table if exists HFJ_RES_TAG 
       add constraint FK_RESTAG_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_VER 
       add constraint FK_RESOURCE_HISTORY_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_VER_PROV 
       add constraint FK_RESVERPROV_RES_PID 
       foreign key (RES_PID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_RES_VER_PROV 
       add constraint FK_RESVERPROV_RESVER_PID 
       foreign key (RES_VER_PID) 
       references HFJ_RES_VER;

    alter table if exists HFJ_SEARCH_INCLUDE 
       add constraint FK_SEARCHINC_SEARCH 
       foreign key (SEARCH_PID) 
       references HFJ_SEARCH;

    alter table if exists HFJ_SPIDX_COORDS 
       add constraint FKC97MPK37OKWU8QVTCEG2NH9VN 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_DATE 
       add constraint FK_SP_DATE_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_NUMBER 
       add constraint FK_SP_NUMBER_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_QUANTITY 
       add constraint FK_SP_QUANTITY_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_QUANTITY_NRML 
       add constraint FK_SP_QUANTITYNM_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_STRING 
       add constraint FK_SPIDXSTR_RESOURCE 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_TOKEN 
       add constraint FK_SP_TOKEN_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SPIDX_URI 
       add constraint FKGXSREUTYMMFJUWDSWV3Y887DO 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists HFJ_SUBSCRIPTION_STATS 
       add constraint FK_SUBSC_RESOURCE_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists MPI_LINK 
       add constraint FK_EMPI_LINK_GOLDEN_RESOURCE 
       foreign key (GOLDEN_RESOURCE_PID) 
       references HFJ_RESOURCE;

    alter table if exists MPI_LINK 
       add constraint FK_EMPI_LINK_PERSON 
       foreign key (PERSON_PID) 
       references HFJ_RESOURCE;

    alter table if exists MPI_LINK 
       add constraint FK_EMPI_LINK_TARGET 
       foreign key (TARGET_PID) 
       references HFJ_RESOURCE;

    alter table if exists MPI_LINK_AUD 
       add constraint FKaow7nxncloec419ars0fpp58m 
       foreign key (REV) 
       references HFJ_REVINFO;

    alter table if exists NPM_PACKAGE_VER 
       add constraint FK_NPM_PKV_PKG 
       foreign key (PACKAGE_PID) 
       references NPM_PACKAGE;

    alter table if exists NPM_PACKAGE_VER 
       add constraint FK_NPM_PKV_RESID 
       foreign key (BINARY_RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists NPM_PACKAGE_VER_RES 
       add constraint FK_NPM_PACKVERRES_PACKVER 
       foreign key (PACKVER_PID) 
       references NPM_PACKAGE_VER;

    alter table if exists NPM_PACKAGE_VER_RES 
       add constraint FK_NPM_PKVR_RESID 
       foreign key (BINARY_RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists TRM_CODESYSTEM 
       add constraint FK_TRMCODESYSTEM_CURVER 
       foreign key (CURRENT_VERSION_PID) 
       references TRM_CODESYSTEM_VER;

    alter table if exists TRM_CODESYSTEM 
       add constraint FK_TRMCODESYSTEM_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists TRM_CODESYSTEM_VER 
       add constraint FK_CODESYSVER_CS_ID 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM;

    alter table if exists TRM_CODESYSTEM_VER 
       add constraint FK_CODESYSVER_RES_ID 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists TRM_CONCEPT 
       add constraint FK_CONCEPT_PID_CS_PID 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM_VER;

    alter table if exists TRM_CONCEPT_DESIG 
       add constraint FK_CONCEPTDESIG_CSV 
       foreign key (CS_VER_PID) 
       references TRM_CODESYSTEM_VER;

    alter table if exists TRM_CONCEPT_DESIG 
       add constraint FK_CONCEPTDESIG_CONCEPT 
       foreign key (CONCEPT_PID) 
       references TRM_CONCEPT;

    alter table if exists TRM_CONCEPT_MAP 
       add constraint FK_TRMCONCEPTMAP_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists TRM_CONCEPT_MAP_GROUP 
       add constraint FK_TCMGROUP_CONCEPTMAP 
       foreign key (CONCEPT_MAP_PID) 
       references TRM_CONCEPT_MAP;

    alter table if exists TRM_CONCEPT_MAP_GRP_ELEMENT 
       add constraint FK_TCMGELEMENT_GROUP 
       foreign key (CONCEPT_MAP_GROUP_PID) 
       references TRM_CONCEPT_MAP_GROUP;

    alter table if exists TRM_CONCEPT_MAP_GRP_ELM_TGT 
       add constraint FK_TCMGETARGET_ELEMENT 
       foreign key (CONCEPT_MAP_GRP_ELM_PID) 
       references TRM_CONCEPT_MAP_GRP_ELEMENT;

    alter table if exists TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_CHILD 
       foreign key (CHILD_PID) 
       references TRM_CONCEPT;

    alter table if exists TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_CS 
       foreign key (CODESYSTEM_PID) 
       references TRM_CODESYSTEM_VER;

    alter table if exists TRM_CONCEPT_PC_LINK 
       add constraint FK_TERM_CONCEPTPC_PARENT 
       foreign key (PARENT_PID) 
       references TRM_CONCEPT;

    alter table if exists TRM_CONCEPT_PROPERTY 
       add constraint FK_CONCEPTPROP_CSV 
       foreign key (CS_VER_PID) 
       references TRM_CODESYSTEM_VER;

    alter table if exists TRM_CONCEPT_PROPERTY 
       add constraint FK_CONCEPTPROP_CONCEPT 
       foreign key (CONCEPT_PID) 
       references TRM_CONCEPT;

    alter table if exists TRM_VALUESET 
       add constraint FK_TRMVALUESET_RES 
       foreign key (RES_ID) 
       references HFJ_RESOURCE;

    alter table if exists TRM_VALUESET_C_DESIGNATION 
       add constraint FK_TRM_VALUESET_CONCEPT_PID 
       foreign key (VALUESET_CONCEPT_PID) 
       references TRM_VALUESET_CONCEPT;

    alter table if exists TRM_VALUESET_C_DESIGNATION 
       add constraint FK_TRM_VSCD_VS_PID 
       foreign key (VALUESET_PID) 
       references TRM_VALUESET;

    alter table if exists TRM_VALUESET_CONCEPT 
       add constraint FK_TRM_VALUESET_PID 
       foreign key (VALUESET_PID) 
       references TRM_VALUESET;
