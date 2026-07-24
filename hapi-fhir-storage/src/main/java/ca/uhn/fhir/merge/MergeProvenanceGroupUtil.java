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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Provenance;

import java.util.Optional;
import java.util.UUID;

// Created by Claude Fable 5
public final class MergeProvenanceGroupUtil {

	private static final String GROUP_ID_PREFIX = "merge-";
	private static final char COMPONENT_SEPARATOR = ';';
	private static final String PARTITION_DELIMITER = ";partition=";
	private static final String OPERATION_DELIMITER = ";operation=";
	private static final String DEFAULT_PARTITION_VALUE = "default";

	private MergeProvenanceGroupUtil() {}

	public static Optional<String> getProvenanceGroupValue(Provenance theProvenance) {
		Extension ext = theProvenance.getExtensionByUrl(HapiExtensions.EXT_PROVENANCE_GROUP);
		if (ext != null && ext.hasValue()) {
			return Optional.ofNullable(ext.getValueAsPrimitive().getValueAsString());
		}
		return Optional.empty();
	}

	public static String generateGroupId(IIdType theSourceId, IIdType theTargetId) {
		return GROUP_ID_PREFIX
				+ theSourceId.getResourceType()
				+ "-" + theSourceId.getIdPart()
				+ "-" + theTargetId.getIdPart()
				+ "-" + UUID.randomUUID();
	}

	public static String buildMemberProvenanceGroupValue(
			String theGroupId, RequestPartitionId thePartition, MergeProvenanceOperation theOperation) {
		Integer partitionId = thePartition.getFirstPartitionIdOrNull();
		String partitionValue = partitionId != null ? partitionId.toString() : DEFAULT_PARTITION_VALUE;
		return theGroupId + PARTITION_DELIMITER + partitionValue + OPERATION_DELIMITER + theOperation.getCode();
	}

	public static boolean isInGroup(String theGroupValue, String theGroupId) {
		return theGroupValue.equals(theGroupId) || theGroupValue.startsWith(theGroupId + PARTITION_DELIMITER);
	}

	public static boolean isInGroup(Provenance theProvenance, String theGroupId) {
		return getProvenanceGroupValue(theProvenance)
				.filter(groupValue -> isInGroup(groupValue, theGroupId))
				.isPresent();
	}

	public static String extractGroupId(String theGroupValue) {
		int separatorIndex = theGroupValue.indexOf(COMPONENT_SEPARATOR);
		return separatorIndex >= 0 ? theGroupValue.substring(0, separatorIndex) : theGroupValue;
	}

	public static Optional<RequestPartitionId> extractPartition(String theGroupValue) {
		return extractComponent(theGroupValue, PARTITION_DELIMITER).map(partitionValue -> {
			if (DEFAULT_PARTITION_VALUE.equals(partitionValue)) {
				return RequestPartitionId.fromPartitionId((Integer) null);
			}
			try {
				return RequestPartitionId.fromPartitionId(Integer.parseInt(partitionValue));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						Msg.code(2995) + "Invalid partition id '" + partitionValue + "' in provenance group value: "
								+ theGroupValue,
						e);
			}
		});
	}

	public static Optional<MergeProvenanceOperation> extractOperation(String theGroupValue) {
		return extractComponent(theGroupValue, OPERATION_DELIMITER)
				.map(operationValue -> MergeProvenanceOperation.fromCode(operationValue)
						.orElseThrow(() -> new IllegalArgumentException(Msg.code(2999) + "Invalid operation '"
								+ operationValue + "' in provenance group value: " + theGroupValue)));
	}

	private static Optional<String> extractComponent(String theGroupValue, String theDelimiter) {
		int delimiterIndex = theGroupValue.indexOf(theDelimiter);
		if (delimiterIndex < 0) {
			return Optional.empty();
		}
		int valueStart = delimiterIndex + theDelimiter.length();
		int valueEnd = theGroupValue.indexOf(COMPONENT_SEPARATOR, valueStart);
		return Optional.of(
				valueEnd < 0 ? theGroupValue.substring(valueStart) : theGroupValue.substring(valueStart, valueEnd));
	}
}
