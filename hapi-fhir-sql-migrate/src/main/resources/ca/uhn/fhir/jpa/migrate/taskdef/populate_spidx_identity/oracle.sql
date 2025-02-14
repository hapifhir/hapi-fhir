SELECT
    hash_identity,
    MAX(sp_name)   KEEP (DENSE_RANK FIRST ORDER BY sp_id) AS sp_name,
    MAX(res_type)  KEEP (DENSE_RANK FIRST ORDER BY sp_id) AS res_type
FROM hfj_spidx_token
GROUP BY hash_identity;
