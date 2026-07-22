/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.merge;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.UUID;

// Created by Claude Fable 5
/**
 * Builds and parses the values of the {@link HapiExtensions#EXT_PROVENANCE_GROUP} extension used to group the
 * Provenance resources a single cross-partition $merge creates (one main Provenance plus one sub-Provenance per
 * involved partition).
 * <p>
 * The value format is:
 * <pre>merge-&lt;ResourceType&gt;-&lt;sourceIdPart&gt;-&lt;targetIdPart&gt;-&lt;uuid&gt;;partition=&lt;partition&gt;</pre>
 * The part before {@code ;partition=} is the <b>group id prefix</b>, unique per merge invocation (the UUID makes it
 * unique even for repeated merges of the same resources). Provenances belong to the same group when their extension
 * values share the same prefix. The {@code ;partition=} suffix names the partition whose changes the Provenance
 * records — the numeric partition id, or {@code default} for the default partition. It deliberately uses a
 * {@code ;partition=} delimiter rather than another dash, because source/target id parts (client-assigned ids,
 * UUIDs) can themselves contain dashes; and it carries the numeric partition id rather than the partition name,
 * because partition names in Patient ID partitioning modes are derived from patient ids and could contain
 * arbitrary characters.
 */
public final class MergeProvenanceGroupIdUtil {

	private static final String GROUP_ID_PREFIX_START = "merge-";
	private static final String PARTITION_DELIMITER = ";partition=";
	private static final String DEFAULT_PARTITION_VALUE = "default";

	private MergeProvenanceGroupIdUtil() {}

	/**
	 * Generates a new group id prefix for one $merge invocation, unique thanks to the random UUID.
	 */
	public static String generateGroupIdPrefix(IIdType theSourceId, IIdType theTargetId) {
		return GROUP_ID_PREFIX_START
				+ theSourceId.getResourceType()
				+ "-" + theSourceId.getIdPart()
				+ "-" + theTargetId.getIdPart()
				+ "-" + UUID.randomUUID();
	}

	/**
	 * Builds the full extension value for the Provenance recording {@code thePartition}'s changes.
	 *
	 * @param theGroupIdPrefix a prefix from {@link #generateGroupIdPrefix(IIdType, IIdType)}
	 * @param thePartition     the partition whose changes the Provenance records
	 */
	public static String buildGroupId(String theGroupIdPrefix, RequestPartitionId thePartition) {
		Integer partitionId = thePartition.getFirstPartitionIdOrNull();
		String partitionValue = partitionId != null ? partitionId.toString() : DEFAULT_PARTITION_VALUE;
		return theGroupIdPrefix + PARTITION_DELIMITER + partitionValue;
	}

	/**
	 * Returns whether the given extension value belongs to the group identified by {@code theGroupIdPrefix},
	 * i.e. whether its prefix (the part before {@code ;partition=}) equals it.
	 */
	public static boolean isInGroup(@Nullable String theGroupId, String theGroupIdPrefix) {
		if (theGroupId == null) {
			return false;
		}
		return theGroupId.equals(theGroupIdPrefix) || theGroupId.startsWith(theGroupIdPrefix + PARTITION_DELIMITER);
	}

	/**
	 * Extracts the group id prefix (the part before {@code ;partition=}) from a full extension value.
	 * Returns the whole value when it has no partition suffix.
	 */
	public static String extractGroupIdPrefix(String theGroupId) {
		int delimiterIndex = theGroupId.indexOf(PARTITION_DELIMITER);
		return delimiterIndex >= 0 ? theGroupId.substring(0, delimiterIndex) : theGroupId;
	}

	/**
	 * Extracts the partition the Provenance records changes for, from a full extension value.
	 *
	 * @return the partition, or {@code null} when the value has no {@code ;partition=} suffix
	 * @throws IllegalArgumentException if the suffix is present but not a valid partition id
	 */
	@Nullable
	public static RequestPartitionId extractPartition(String theGroupId) {
		int delimiterIndex = theGroupId.indexOf(PARTITION_DELIMITER);
		if (delimiterIndex < 0) {
			return null;
		}
		String partitionValue = theGroupId.substring(delimiterIndex + PARTITION_DELIMITER.length());
		if (DEFAULT_PARTITION_VALUE.equals(partitionValue)) {
			// the default partition with a null id; the transaction service normalizes it to the configured
			// default partition id when executing
			return RequestPartitionId.fromPartitionId((Integer) null);
		}
		try {
			return RequestPartitionId.fromPartitionId(Integer.parseInt(partitionValue));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					Msg.code(2995) + "Invalid partition id '" + partitionValue + "' in provenance group id: "
							+ theGroupId,
					e);
		}
	}
}
