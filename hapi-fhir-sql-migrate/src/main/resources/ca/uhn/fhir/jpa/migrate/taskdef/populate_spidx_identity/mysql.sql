-- Step 1: Initialize @row_number with the current next_val from SEQ_SPIDX_IDENTITY
SET @row_number = (SELECT next_val FROM SEQ_SPIDX_IDENTITY);

-- Step 2: Perform the insert
INSERT INTO HFJ_SPIDX_IDENTITY (
    sp_identity_id,
    hash_identity,
    res_type,
    sp_name
)
SELECT
    (@row_number := @row_number + 1) AS sp_identity_id, -- increment row_number for each row
    src.hash_identity,
    src.res_type,
    src.sp_name
FROM {0} AS src -- replace with hfj_spidx_* table
         JOIN (
    SELECT
        min_hash_id.hash_identity,
        MIN(min_hash_id.sp_id) AS min_sp_id
    FROM {0} AS min_hash_id -- replace with hfj_spidx_* table
    WHERE NOT EXISTS (
        SELECT 1 FROM HFJ_SPIDX_IDENTITY i WHERE i.hash_identity = min_hash_id.hash_identity
    )
    GROUP BY min_hash_id.hash_identity
) AS min_spid_per_hash ON
    src.hash_identity = min_spid_per_hash.hash_identity
    AND src.sp_id = min_spid_per_hash.min_sp_id;

-- Step 3: Update the sequence with the number of rows inserted
UPDATE SEQ_SPIDX_IDENTITY
SET next_val = next_val + ROW_COUNT();
