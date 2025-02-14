INSERT INTO HFJ_SPIDX_IDENTITY (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
WITH cte_min_id AS (
    SELECT
        hash_identity,
        MIN(sp_id) AS min_sp_id
    FROM {0} -- replace {0} with hfj_spidx_* table
    WHERE hash_identity NOT IN (
        SELECT hash_identity
        FROM hfj_spidx_identity
    )
    GROUP BY hash_identity
)
SELECT
    nextval(''SEQ_SPIDX_IDENTITY'') AS sp_identity_id,
    src.hash_identity,
    src.res_type,
    src.sp_name
FROM {0} AS src -- replace {0} with hfj_spidx_* table
JOIN cte_min_id AS cm
   ON src.hash_identity = cm.hash_identity
  AND src.sp_id         = cm.min_sp_id
ORDER BY src.res_type, src.sp_name;

SELECT DISTINCT on (hash_identity)
    hash_identity
FROM hfj_spidx_token
