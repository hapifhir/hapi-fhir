/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Utility class for calculating hashes of SearchParam entity fields.
 */
public class SearchParamHash {

	/**
	 * Don't change this without careful consideration. You will break existing hashes!
	 */
	private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128(0);

	/**
	 * Don't make this public 'cause nobody better be able to modify it!
	 */
	private static final byte[] DELIMITER_BYTES = "|".getBytes(Charsets.UTF_8);

	private SearchParamHash() {}

	/**
	 * Applies a fast and consistent hashing algorithm to a set of strings
	 */
	public static long hashSearchParam(
			@Nonnull PartitionSettings thePartitionSettings,
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nonnull String... theValues) {
		return doHashSearchParam(thePartitionSettings, theRequestPartitionId, theValues);
	}

	/**
	 * Applies a fast and consistent hashing algorithm to a set of strings
	 */
	public static long hashSearchParam(@Nonnull String... theValues) {
		return doHashSearchParam(null, null, theValues);
	}

	private static long doHashSearchParam(
			@Nullable PartitionSettings thePartitionSettings,
			@Nullable RequestPartitionId theRequestPartitionId,
			@Nonnull String[] theValues) {
		Hasher hasher = HASH_FUNCTION.newHasher();

		if (thePartitionSettings != null
				&& theRequestPartitionId != null
				&& thePartitionSettings.isPartitioningEnabled()
				&& thePartitionSettings.isIncludePartitionInSearchHashes()) {
			if (theRequestPartitionId.getPartitionIds().size() > 1) {
				throw new InternalErrorException(Msg.code(1527)
						+ "Can not search multiple partitions when partitions are included in search hashes");
			}
			Integer partitionId = theRequestPartitionId.getFirstPartitionIdOrNull();
			if (partitionId != null) {
				hasher.putInt(partitionId);
			}
		}

		for (String next : theValues) {
			if (next == null) {
				hasher.putByte((byte) 0);
			} else {
				next = UrlUtil.escapeUrlParam(next);
				byte[] bytes = next.getBytes(Charsets.UTF_8);
				hasher.putBytes(bytes);
			}
			hasher.putBytes(DELIMITER_BYTES);
		}

		HashCode hashCode = hasher.hash();
		long retVal = hashCode.asLong();
		return retVal;
	}
}
