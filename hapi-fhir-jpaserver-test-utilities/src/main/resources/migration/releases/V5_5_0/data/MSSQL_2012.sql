INSERT INTO HFJ_RESOURCE (
   RES_ID,
   RES_VERSION,
   HAS_TAGS,
   RES_PUBLISHED,
   RES_UPDATED,
   SP_HAS_LINKS,
   HASH_SHA256,
   SP_INDEX_STATUS,
   SP_CMPSTR_UNIQ_PRESENT,
   SP_COORDS_PRESENT,
   SP_DATE_PRESENT,
   SP_NUMBER_PRESENT,
   SP_QUANTITY_NRML_PRESENT,
   SP_QUANTITY_PRESENT,
   SP_STRING_PRESENT,
   SP_TOKEN_PRESENT,
   SP_CMPTOKS_PRESENT,
   SP_URI_PRESENT,
   RES_TYPE,
   RES_VER
)
   VALUES (
   1653,
   'R4',
   'false',
   '2023-06-15 09:58:42.92',
   '2023-06-15 09:58:42.92',
   'false',
   '6beed652b77f6c65d776e57341a0b5b0596ac9cfb0e8345a5a5cfbfaa59e2b62',
   1,
   'false',
   'false',
   'false',
   'false',
   'true',
   'false',
   'false',
   'true',
   'false',
   'true',
   'Observation',
   1
);

INSERT INTO HFJ_RESOURCE (
    RES_ID,
    RES_VERSION,
    HAS_TAGS,
    RES_PUBLISHED,
    RES_UPDATED,
    SP_HAS_LINKS,
    HASH_SHA256,
    SP_INDEX_STATUS,
    SP_CMPSTR_UNIQ_PRESENT,
    SP_COORDS_PRESENT,
    SP_DATE_PRESENT,
    SP_NUMBER_PRESENT,
    SP_QUANTITY_NRML_PRESENT,
    SP_QUANTITY_PRESENT,
    SP_STRING_PRESENT,
    SP_TOKEN_PRESENT,
    SP_CMPTOKS_PRESENT,
    SP_URI_PRESENT,
    RES_TYPE,
    RES_VER
)VALUES (
   1654,
   'R4',
   'false',
   '2023-06-15 09:58:42.95',
   '2023-06-15 09:58:42.95',
   'false',
   '712b39864f0650e6b9ffd7b4ffa7b2de5db4ceecbcbd76b1f1b8e152ed17fcd0',
   1,
   'false',
   'false',
   'false',
   'false',
   'false',
   'false',
   'true',
   'true',
   'false',
   'true',
   'VALUESET',
   1
);


INSERT INTO HFJ_IDX_CMB_TOK_NU (
   PID,
   PARTITION_DATE,
   PARTITION_ID,
   HASH_COMPLETE,
   IDX_STRING,
   RES_ID
) VALUES (
   10,
   '2024-04-05',
   1,
   '5570851350247697202',
   'Patient?birthdate=1974-12-25&family=WINDSOR&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale',
   1653
);

INSERT INTO HFJ_BLK_IMPORT_JOB (
   PID,
   JOB_ID,
   JOB_STATUS,
   STATUS_TIME,
   STATUS_MESSAGE,
   JOB_DESC,
   OPTLOCK,
   FILE_COUNT,
   ROW_PROCESSING_MODE,
   BATCH_SIZE
) VALUES (
   61,
   '0fe3744a-93f3-4bb8-a59e-15643566e513',
   'READY',
   '2024-04-04 16:09:14.488',
   '',
   'ETL Import Job: (unnamed)',
   0,
   1,
   'FHIR_TRANSACTION',
   10
);

INSERT INTO HFJ_BLK_IMPORT_JOBFILE (
   PID,
   JOB_PID,
   JOB_CONTENTS,
   FILE_DESCRIPTION,
   FILE_SEQ,
   TENANT_NAME
) VALUES (
   65,
   61,
   72995,
   'Rows 0 - 10',
   0,
   ''
);

INSERT INTO TRM_VALUESET (
   PID,
   EXPANSION_STATUS,
   EXPANDED_AT,
   VSNAME,
   RES_ID,
   TOTAL_CONCEPT_DESIGNATIONS,
   TOTAL_CONCEPTS,
   URL,
   VER
) VALUES (
   60,
   'EXPANDED',
   '2024-04-04 16:09:14.488',
   'v2.0926',
   1654,
   0,
   3,
   'http://terminology.hl7.org/ValueSet/v2-0926',
   '2.9'
);
