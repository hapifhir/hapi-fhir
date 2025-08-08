INSERT INTO HFJ_RESOURCE_TYPE (
   RES_TYPE_ID,
   RES_TYPE
) VALUES (
   130,
   'Observation'
);

INSERT INTO HFJ_RESOURCE_TYPE (
    RES_TYPE_ID,
    RES_TYPE
) VALUES (
   140,
   'Patient'
);

INSERT INTO HFJ_RESOURCE (
   RES_ID,
   PARTITION_ID,
   PARTITION_DATE,
   RES_DELETED_AT,
   RES_VERSION,
   HAS_TAGS,
   RES_PUBLISHED,
   RES_UPDATED,
   FHIR_ID,
   SP_HAS_LINKS,
   HASH_SHA256,
   SP_INDEX_STATUS,
   RES_LANGUAGE,
   SP_CMPSTR_UNIQ_PRESENT,
   SP_CMPTOKS_PRESENT,
   SP_COORDS_PRESENT,
   SP_DATE_PRESENT,
   SP_NUMBER_PRESENT,
   SP_QUANTITY_NRML_PRESENT,
   SP_QUANTITY_PRESENT,
   SP_STRING_PRESENT,
   SP_TOKEN_PRESENT,
   SP_URI_PRESENT,
   SEARCH_URL_PRESENT,
   RES_TYPE,
   RES_VER,
   RES_TYPE_ID
) VALUES (
   1688,
   1,
   '2025-06-15',
   '2025-07-15 09:58:42.92',
   'R4',
   0,
   '2025-06-15 09:58:42.92',
   '2025-06-15 09:58:42.92',
   '1688',
   0,
   '06055bd5bb5cdfcaae9fb93b522ddbb2432e263ec93038ac10d1019a1d7f2ec5',
   1,
   'EN',
   0,
   0,
   0,
   0,
   0,
   1,
   0,
   0,
   1,
   1,
   0,
   'Observation',
   1,
   130
);

INSERT INTO HFJ_RES_VER (
   PID,
   PARTITION_DATE,
   PARTITION_ID,
   RES_DELETED_AT,
   RES_VERSION,
   HAS_TAGS,
   RES_PUBLISHED,
   RES_UPDATED,
   RES_ENCODING,
   RES_TEXT,
   RES_ID,
   RES_TYPE,
   RES_VER,
   RES_TEXT_VC,
   REQUEST_ID,
   SOURCE_URI,
   RES_TYPE_ID
) VALUES (
   1688,
   '2025-04-05',
   1,
   '2025-04-05 15:54:05.296',
   'R4',
   0,
   '2025-05-01 15:54:05.296',
   '2025-05-01 15:54:05.296',
   'JSON',
   1413285,
   1688,
   'Observation',
   1,
   '{"resourceType":"Observation","id":"1688","meta":{"versionId":"1","lastUpdated":"2024-07-09T00:23:39.518-06:00","source":"#d0gIuBBd3WiJQSP6"},"code":{"coding":[{"system":"http://loinc.org","code":"8302-2","display":"Body Height"}]},"subject":{"reference":"Patient/374903"},"effectiveDateTime":"2022-02-09T00:00:00Z","valueQuantity":{"value":0.0000012,"unit":"cm","system":"http://unitsofmeasure.org","code":"cm"}}',
   'd0gIuBBd3WiJQSP6',
   'urn:source:1',
   130
);

INSERT INTO HFJ_RES_TAG (
   PID,
   PARTITION_DATE,
   PARTITION_ID,
   TAG_ID,
   RES_ID,
   RES_TYPE,
   RES_TYPE_ID
) VALUES (
   2,
   '2025-04-05',
   1,
   3,
   1688,
   'Observation',
   130
);

INSERT INTO HFJ_HISTORY_TAG (
   PID,
   PARTITION_DATE,
   PARTITION_ID,
   TAG_ID,
   RES_VER_PID,
   RES_ID,
   RES_TYPE,
   RES_TYPE_ID
) VALUES (
   3,
   '2025-04-05',
   1,
   1,
   1688,
   1688,
   'Observation',
   130
);

INSERT INTO HFJ_RES_LINK (
   PID,
   PARTITION_DATE,
   PARTITION_ID,
   SRC_PATH,
   SRC_RESOURCE_ID,
   SOURCE_RESOURCE_TYPE,
   SRC_RES_TYPE_ID,
   TARGET_RESOURCE_ID,
   TARGET_RESOURCE_TYPE,
   TARGET_RES_TYPE_ID,
   TARGET_RESOURCE_URL,
   TARGET_RESOURCE_VERSION,
   SP_UPDATED,
   TARGET_RES_PARTITION_ID,
   TARGET_RES_PARTITION_DATE
) VALUES (
   703,
   '2025-04-05',
   1,
   'Observation.subject.where(resolve() is Patient)',
   1688,
   'Observation',
   130,
   1906,
   'Patient',
   140,
   'http://localhost:8000/Patient/1906',
   1,
   '2025-04-01 18:01:12.921',
   1,
   '2025-04-05'
);
