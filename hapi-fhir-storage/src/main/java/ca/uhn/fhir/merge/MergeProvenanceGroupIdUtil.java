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
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.UUID;

// Created by Claude Fable 5
public final class MergeProvenanceGroupIdUtil {

	private static final String GROUP_ID_PREFIX_START = "merge-";
	private static final String PARTITION_DELIMITER = ";partition=";
	private static final String DEFAULT_PARTITION_VALUE = "default";

	private MergeProvenanceGroupIdUtil() {}

	public static String generateGroupIdPrefix(IIdType theSourceId, IIdType theTargetId) {
		return GROUP_ID_PREFIX_START
				+ theSourceId.getResourceType()
				+ "-" + theSourceId.getIdPart()
				+ "-" + theTargetId.getIdPart()
				+ "-" + UUID.randomUUID();
	}

	public static String buildGroupId(String theGroupIdPrefix, RequestPartitionId thePartition) {
		Integer partitionId = thePartition.getFirstPartitionIdOrNull();
		String partitionValue = partitionId != null ? partitionId.toString() : DEFAULT_PARTITION_VALUE;
		return theGroupIdPrefix + PARTITION_DELIMITER + partitionValue;
	}

	public static boolean isInGroup(@Nullable String theGroupId, String theGroupIdPrefix) {
		if (theGroupId == null) {
			return false;
		}
		return theGroupId.equals(theGroupIdPrefix) || theGroupId.startsWith(theGroupIdPrefix + PARTITION_DELIMITER);
	}

	public static String extractGroupIdPrefix(String theGroupId) {
		int delimiterIndex = theGroupId.indexOf(PARTITION_DELIMITER);
		return delimiterIndex >= 0 ? theGroupId.substring(0, delimiterIndex) : theGroupId;
	}

	@Nullable
	public static RequestPartitionId extractPartition(String theGroupId) {
		int delimiterIndex = theGroupId.indexOf(PARTITION_DELIMITER);
		if (delimiterIndex < 0) {
			return null;
		}
		String partitionValue = theGroupId.substring(delimiterIndex + PARTITION_DELIMITER.length());
		if (DEFAULT_PARTITION_VALUE.equals(partitionValue)) {
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
