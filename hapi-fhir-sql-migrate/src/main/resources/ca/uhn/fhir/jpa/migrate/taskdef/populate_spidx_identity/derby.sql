INSERT INTO hfj_spidx_identity (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
SELECT
    NEXT VALUE FOR SEQ_SPIDX_IDENTITY AS sp_identity_id,
    src.hash_identity,
    src.res_type,
    src.sp_name
FROM {0} AS src JOIN ( -- replace with hfj_spidx_* table
    SELECT hash_identity, MIN(sp_id) AS min_sp_id
    FROM {0} AS min_hash_id -- replace with hfj_spidx_* table
    WHERE NOT EXISTS (
    SELECT 1 FROM hfj_spidx_identity AS i WHERE i.hash_identity = min_hash_id.hash_identity
    )
    GROUP BY hash_identity
) AS min_spid_per_hash ON
    src.hash_identity = min_spid_per_hash.hash_identity AND
    src.sp_id = min_spid_per_hash.min_sp_id;
