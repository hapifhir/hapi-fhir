INSERT INTO HFJ_SPIDX_IDENTITY (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
WITH cte_sp_id AS (
    SELECT
        hash_identity,
        MIN(sp_id) AS min_sp_id
    FROM {0} -- replace with hfj_spidx_* table
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
FROM {0} AS src -- replace with hfj_spidx_* table
JOIN cte_sp_id
   ON src.hash_identity = cte_sp_id.hash_identity
  AND src.sp_id         = cte_sp_id.min_sp_id
ORDER BY src.res_type, src.sp_name;
