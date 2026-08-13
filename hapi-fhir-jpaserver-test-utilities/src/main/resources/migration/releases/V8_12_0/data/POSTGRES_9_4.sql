INSERT INTO BT2_JOB_ATTACHMENT (
   JOB_INSTANCE_ID,
   ATTACHMENT_ID,
   FILENAME,
   CONTENT_TYPE,
   CMP_STATUS,
   ATTACHMENT_DATA,
   ATTACHMENT_LENGTH_CMP,
   ATTACHMENT_LENGTH_UC,
   EXTRA_CHUNK_IDX
) VALUES (
   '79751e50-fbfc-479a-8e14-29bed1973d67',
   '2eb2c28c-fa20-48df-9652-00669dcaea2d',
   'loinc.zip',
   'ZIP',
   'NONE',
   '\x48656c6c6f20776f726c6422',
   22,
   22,
   0
);

INSERT INTO BT2_JOB_ATTACHMENT_CHUNK (
   JOB_INSTANCE_ID,
   ATTACHMENT_ID,
   CHUNK_INDEX,
   ATTACHMENT_DATA
) VALUES (
   '79751e50-fbfc-479a-8e14-29bed1973d67',
   '2eb2c28c-fa20-48df-9652-00669dcaea2d',
   0,
   '\x48656c6c6f20776f726c6423'
);

INSERT INTO TRM_CODESYSTEM_VER (
    PID,
    PARTITION_ID,
    PARTITION_DATE,
    CS_DISPLAY,
    CODESYSTEM_PID,
    CS_VERSION_ID,
    RES_ID,
    CS_INTENDED_VERSION_ID
) VALUES (
    56,
    1,
    '2024-05-01',
    'LOINC',
    2,
    '2.65',
    1780,
    '2.66'
);

INSERT INTO TRM_VALUESET (
    PID,
    PARTITION_ID,
    PARTITION_DATE,
    EXPANSION_STATUS,
    EXPANDED_AT,
    VSNAME,
    RES_ID,
    TOTAL_CONCEPT_DESIGNATIONS,
    TOTAL_CONCEPTS,
    URL,
    VER,
    VS_INTENDED_VERSION_ID,
    EXPANSION_ERROR
) VALUES (
    62,
    1,
    '2025-05-01',
    'FAILED_TO_EXPAND',
    '2025-01-04 16:09:14.488',
    'v2.0127',
    1654,
    0,
    8,
    'http://terminology.hl7.org/ValueSet/v2-0127',
    '2.0.1',
    '2.1.0',
    'Unable to expand ValueSet because CodeSystem could not be found: http://terminology.hl7.org/CodeSystem/v2-0127'
);
