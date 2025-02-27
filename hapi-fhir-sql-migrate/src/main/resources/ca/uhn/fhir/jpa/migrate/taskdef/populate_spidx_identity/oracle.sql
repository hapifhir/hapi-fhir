INSERT INTO HFJ_SPIDX_IDENTITY (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
SELECT seq_spidx_identity.NEXTVAL AS sp_identity_id,
       src.hash_identity,
       src.res_type,
       src.sp_name
FROM (
    WITH cte_sp_id AS (
        SELECT hash_identity,
               MIN(sp_id) AS min_sp_id
        FROM {0} -- replace with hfj_spidx_* table
        GROUP BY hash_identity
        )
    SELECT src.hash_identity,
           src.res_type,
           src.sp_name
    FROM {0} src -- replace with hfj_spidx_* table
    JOIN cte_sp_id ON
        src.hash_identity = cte_sp_id.hash_identity AND
        src.sp_id = cte_sp_id.min_sp_id
    WHERE NOT EXISTS (
        SELECT 1 FROM hfj_spidx_identity sp_id WHERE sp_id.hash_identity = cte_sp_id.hash_identity
    )
    ORDER BY src.res_type, src.sp_name
) src
