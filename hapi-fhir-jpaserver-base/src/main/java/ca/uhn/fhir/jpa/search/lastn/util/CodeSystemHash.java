package ca.uhn.fhir.jpa.search.lastn.util;

import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class CodeSystemHash {
    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128(0);
    private static final byte[] DELIMITER_BYTES = "|".getBytes(Charsets.UTF_8);

    static public long hashCodeSystem( String system, String code ) {
        Hasher hasher = HASH_FUNCTION.newHasher();
        addStringToHasher(hasher, system);
        addStringToHasher(hasher, code);

        HashCode hashCode = hasher.hash();
        return hashCode.asLong();
    }

    static private void addStringToHasher(Hasher hasher, String next) {
        if (next == null) {
            hasher.putByte((byte) 0);
        } else {
            next = UrlUtil.escapeUrlParam(next);
            byte[] bytes = next.getBytes(Charsets.UTF_8);
            hasher.putBytes(bytes);
        }
        hasher.putBytes(DELIMITER_BYTES);
    }
}
