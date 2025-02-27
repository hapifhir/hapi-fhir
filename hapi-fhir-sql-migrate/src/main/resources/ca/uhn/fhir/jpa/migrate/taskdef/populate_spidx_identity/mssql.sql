INSERT INTO HFJ_SPIDX_IDENTITY (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
SELECT NEXT VALUE FOR seq_spidx_identity,
       sub.hash_identity,
       sub.res_type,
       sub.sp_name
FROM (
    SELECT src.hash_identity,
           src.res_type,
           src.sp_name,
           ROW_NUMBER() OVER (
               PARTITION BY src.hash_identity ORDER BY src.sp_id
           ) AS rn
    FROM {0} AS src -- replace with hfj_spidx_* table
    WHERE NOT EXISTS (
        SELECT 1 FROM hfj_spidx_identity i WHERE i.hash_identity = src.hash_identity
    )) AS sub
WHERE sub.rn = 1
