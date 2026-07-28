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
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Provenance;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The value stored in a merge Provenance's group extension, tying a merge operation's Provenances together.
 * Every Provenance of one merge shares the same group id: the main Provenance stores that group id bare, while
 * each member Provenance qualifies it with the partition and change type it records.
 * <p>
 * For example, given the group id {@code merge|Patient|123|456|<uuid>}, the main Provenance stores
 * {@code merge|Patient|123|456|<uuid>}, and a member Provenance recording updates on partition 0 stores
 * {@code merge|Patient|123|456|<uuid>;partition=0;changeType=update}.
 */
// Created by Claude Opus 4.8
public final class MergeProvenanceGroupValue {

	private static final String GROUP_ID_PREFIX = "merge";
	private static final String GROUP_ID_PART_SEPARATOR = "|";
	private static final String COMPONENT_SEPARATOR = ";";
	private static final String KEY_VALUE_SEPARATOR = "=";
	private static final String PARTITION_KEY = "partition";
	private static final String CHANGE_TYPE_KEY = "changeType";
	private static final String NULL_PARTITION_VALUE = "null";

	private final String myGroupId;

	@Nullable
	private final RequestPartitionId myPartition;

	@Nullable
	private final MergeChangeType myChangeType;

	private MergeProvenanceGroupValue(
			String theGroupId, @Nullable RequestPartitionId thePartition, @Nullable MergeChangeType theChangeType) {
		myGroupId = theGroupId;
		myPartition = thePartition;
		myChangeType = theChangeType;
	}

	// ================================================
	// PUBLIC INSTANCE METHODS
	// ================================================

	public String getGroupId() {
		return myGroupId;
	}

	public Optional<RequestPartitionId> getPartition() {
		return Optional.ofNullable(myPartition);
	}

	public Optional<MergeChangeType> getChangeType() {
		return Optional.ofNullable(myChangeType);
	}

	public boolean isMain() {
		return myPartition == null;
	}

	public boolean isSameGroup(String theGroupId) {
		return myGroupId.equals(theGroupId);
	}

	/**
	 * Whether the given Provenance belongs to the same group as this value.
	 */
	public boolean isInSameGroup(Provenance theProvenance) {
		return fromProvenance(theProvenance)
				.map(groupValue -> groupValue.isSameGroup(myGroupId))
				.orElse(false);
	}

	/**
	 * Derives a member value in this group for the given partition and change type.
	 */
	public MergeProvenanceGroupValue member(RequestPartitionId thePartition, MergeChangeType theChangeType) {
		Objects.requireNonNull(thePartition, "A member group value requires a partition");
		Objects.requireNonNull(theChangeType, "A member group value requires a change type");
		validateSinglePartition(thePartition);
		return new MergeProvenanceGroupValue(myGroupId, thePartition, theChangeType);
	}

	/**
	 * Serializes this value to the string stored in a Provenance's group extension.
	 */
	public String encode() {
		if (isMain()) {
			return myGroupId;
		}
		Integer partitionId = myPartition.getPartitionIds().get(0);
		String partitionValue = partitionId != null ? partitionId.toString() : NULL_PARTITION_VALUE;
		return myGroupId
				+ component(PARTITION_KEY, partitionValue)
				+ component(CHANGE_TYPE_KEY, myChangeType.getCode());
	}

	// ================================================
	// PUBLIC STATIC METHODS
	// ================================================

	/**
	 * Creates the main value for a new merge, generating a fresh group id from the source and target ids.
	 */
	public static MergeProvenanceGroupValue newGroup(IIdType theSourceId, IIdType theTargetId) {
		Objects.requireNonNull(theSourceId, "A merge group requires a source id");
		Objects.requireNonNull(theTargetId, "A merge group requires a target id");
		String groupId = String.join(
				GROUP_ID_PART_SEPARATOR,
				GROUP_ID_PREFIX,
				theSourceId.getResourceType(),
				theSourceId.getIdPart(),
				theTargetId.getIdPart(),
				UUID.randomUUID().toString());
		return new MergeProvenanceGroupValue(groupId, null, null);
	}

	/**
	 * Parses an encoded group value back into its components.
	 */
	public static MergeProvenanceGroupValue parse(String theEncoded) {
		String[] components = theEncoded.split(COMPONENT_SEPARATOR);
		String groupId = components[0];
		return new MergeProvenanceGroupValue(
				groupId, extractPartition(components, theEncoded), extractChangeType(components));
	}

	/**
	 * Reads and parses the group value from a Provenance's group extension, or empty if it has none.
	 */
	public static Optional<MergeProvenanceGroupValue> fromProvenance(Provenance theProvenance) {
		return getGroupExtensionValue(theProvenance).map(MergeProvenanceGroupValue::parse);
	}

	/**
	 * A Provenance is the main Provenance of its group when its group value is the bare group id, i.e. it has
	 * no partition or change type qualifier. A Provenance with no group extension at all is also treated as main.
	 */
	public static boolean isMainProvenance(Provenance theProvenance) {
		return fromProvenance(theProvenance)
				.map(MergeProvenanceGroupValue::isMain)
				.orElse(true);
	}

	// ================================================
	// PRIVATE STATIC METHODS
	// ================================================

	private static void validateSinglePartition(RequestPartitionId thePartition) {
		if (thePartition.isAllPartitions()
				|| !thePartition.hasPartitionIds()
				|| thePartition.getPartitionIds().size() != 1) {
			throw new IllegalArgumentException(
					Msg.code(3014) + "A merge Provenance must record exactly one partition, but got: " + thePartition);
		}
	}

	private static String component(String theKey, String theValue) {
		return COMPONENT_SEPARATOR + theKey + KEY_VALUE_SEPARATOR + theValue;
	}

	private static Optional<String> getGroupExtensionValue(Provenance theProvenance) {
		Extension ext = theProvenance.getExtensionByUrl(HapiExtensions.EXT_PROVENANCE_GROUP);
		if (ext != null && ext.hasValue()) {
			return Optional.ofNullable(ext.getValueAsPrimitive().getValueAsString());
		}
		return Optional.empty();
	}

	@Nullable
	private static RequestPartitionId extractPartition(String[] theComponents, String theEncoded) {
		return findComponent(theComponents, PARTITION_KEY)
				.map(partitionValue -> {
					if (NULL_PARTITION_VALUE.equals(partitionValue)) {
						return RequestPartitionId.fromPartitionId((Integer) null);
					}
					try {
						return RequestPartitionId.fromPartitionId(Integer.parseInt(partitionValue));
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException(
								Msg.code(2975) + "Invalid partition id '" + partitionValue
										+ "' in provenance group value: " + theEncoded,
								e);
					}
				})
				.orElse(null);
	}

	@Nullable
	private static MergeChangeType extractChangeType(String[] theComponents) {
		return findComponent(theComponents, CHANGE_TYPE_KEY)
				.map(MergeChangeType::fromCode)
				.orElse(null);
	}

	private static Optional<String> findComponent(String[] theComponents, String theKey) {
		String prefix = theKey + KEY_VALUE_SEPARATOR;
		for (String component : theComponents) {
			if (component.startsWith(prefix)) {
				return Optional.of(component.substring(prefix.length()));
			}
		}
		return Optional.empty();
	}
}
