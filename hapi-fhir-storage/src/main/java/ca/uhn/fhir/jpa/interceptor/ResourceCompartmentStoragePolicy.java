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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.util.UrlUtil;

import java.util.Map;
import java.util.Optional;

/**
 * Value type for {@link PatientIdPartitionInterceptor#setResourceTypePolicies(Map)}.
 * When using {@link PatientIdPartitionInterceptor Patient ID Partition Mode}, this class holds a
 * policy defining which partition to write to or search in when storing or searching for resources
 * that are in the Patient compartment. It can be used to specify that certain resources should
 * be stored in a global partition instead of a patient-specific one.
 * <p>
 * Use the factory methods to create instances of this class:
 * <ul>
 *     <li>{@link #alwaysUseDefaultPartition()} - Always use the default partition for this resource type.</li>
 *     <li>{@link #mandatorySingleCompartment()} - Require that the resource has exactly one compartment.</li>
 *     <li>{@link #optionalSingleCompartment()} - Allow zero or one compartments for this resource type. If the resource is not in any compartment, store it in the default partition.</li>
 *     <li>{@link #nonUniqueCompartmentInDefault()} - Allow the resource to be in multiple compartments or no compartment, store in the default partition if there isn't one unique compartment.</li>
 *     <li>{@link #alwaysUsePartitionId(int)} - Always store the resource in the partition with the given ID.</li>
 * </ul>
 * </p>
 *
 * @see PatientIdPartitionInterceptor#setResourceTypePolicies(Map)
 * @since 8.10.0
 */
public class ResourceCompartmentStoragePolicy {

	private static final String NON_UNIQUE_COMPARTMENT_IN_DEFAULT = "NON_UNIQUE_COMPARTMENT_IN_DEFAULT";
	private static final String ALWAYS_USE_DEFAULT_PARTITION = "ALWAYS_USE_DEFAULT_PARTITION";
	private static final String MANDATORY_SINGLE_COMPARTMENT = "MANDATORY_SINGLE_COMPARTMENT";
	private static final String OPTIONAL_SINGLE_COMPARTMENT = "OPTIONAL_SINGLE_COMPARTMENT";
	private static final String ALWAYS_USE_PARTITION_ID_PREFIX = "ALWAYS_USE_PARTITION_ID/";

	private final String myName;
	private final boolean myOnlyAppliesToCompartmentResourceTypes;
	private final boolean myAlwaysUseDefaultPartition;
	private final RequestPartitionId myAlwaysUsePartition;
	private final boolean myMultipleCompartmentsUseDefaultPartition;
	private final boolean myNoCompartmentsUseDefaultPartition;

	private ResourceCompartmentStoragePolicy(
			String theName,
			boolean theOnlyAppliesToCompartmentResourceTypes,
			boolean theAlwaysUseDefaultPartition,
			Integer theAlwaysUsePartitionId,
			boolean theMultipleCompartmentsUseDefaultPartition,
			boolean theNoCompartmentsUseDefaultPartition) {
		myName = theName;
		myOnlyAppliesToCompartmentResourceTypes = theOnlyAppliesToCompartmentResourceTypes;
		myAlwaysUseDefaultPartition = theAlwaysUseDefaultPartition;
		myAlwaysUsePartition =
				theAlwaysUsePartitionId != null ? RequestPartitionId.fromPartitionId(theAlwaysUsePartitionId) : null;
		myMultipleCompartmentsUseDefaultPartition = theMultipleCompartmentsUseDefaultPartition;
		myNoCompartmentsUseDefaultPartition = theNoCompartmentsUseDefaultPartition;
	}

	/**
	 * Is this a policy that can only apply to resources in the patient compartment?
	 */
	public boolean isOnlyAppliesToCompartmentResourceTypes() {
		return myOnlyAppliesToCompartmentResourceTypes;
	}

	public Optional<RequestPartitionId> getPartitionIdForNonUniqueCompartment(PartitionSettings thePartitionSettings) {
		if (hasPartitionIdForNonUniqueCompartment()) {
			return Optional.of(RequestPartitionId.defaultPartition(thePartitionSettings));
		} else {
			return Optional.empty();
		}
	}

	public boolean hasPartitionIdForNonUniqueCompartment() {
		return myMultipleCompartmentsUseDefaultPartition;
	}

	public Optional<RequestPartitionId> getPartitionIdForNoCompartment(PartitionSettings thePartitionSettings) {
		if (hasPartitionIdForNoCompartment()) {
			return Optional.of(RequestPartitionId.defaultPartition(thePartitionSettings));
		} else {
			return Optional.empty();
		}
	}

	public boolean hasPartitionIdForNoCompartment() {
		return myNoCompartmentsUseDefaultPartition;
	}

	public Optional<RequestPartitionId> getUsePartitionId(PartitionSettings thePartitionSettings) {
		if (myAlwaysUseDefaultPartition) {
			return Optional.of(RequestPartitionId.defaultPartition(thePartitionSettings));
		}
		if (myAlwaysUsePartition != null) {
			return Optional.of(myAlwaysUsePartition);
		}
		return Optional.empty();
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return myName;
	}

	/**
	 * All resources of the given type will be stored in the partition with the given ID.
	 */
	public static ResourceCompartmentStoragePolicy alwaysUsePartitionId(int theAlwaysUsePartitionId) {
		return new ResourceCompartmentStoragePolicy(
				ALWAYS_USE_PARTITION_ID_PREFIX + theAlwaysUsePartitionId,
				false,
				false,
				theAlwaysUsePartitionId,
				false,
				false);
	}

	/**
	 * The given resource type should always be placed in the
	 * {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static ResourceCompartmentStoragePolicy alwaysUseDefaultPartition() {
		return new ResourceCompartmentStoragePolicy(ALWAYS_USE_DEFAULT_PARTITION, false, true, null, false, false);
	}

	/**
	 * The given resource type must contain exactly one reference to a Patient placing it in a single
	 * patient compartment. If the resource is found to belong to multiple patient compartments or no Patient
	 * compartment, an error will be raised and the resource will not be stored.
	 */
	public static ResourceCompartmentStoragePolicy mandatorySingleCompartment() {
		return new ResourceCompartmentStoragePolicy(MANDATORY_SINGLE_COMPARTMENT, true, false, null, false, false);
	}

	/**
	 * The given resource type may contain zero or one references to a Patient placing it in a single
	 * patient compartment or no patient compartment. If the resource is found to belong to no patient compartment,
	 * it will be stored in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 * If the resource is found to belong to multiple patient compartments, an error will be
	 * raised and the resource will not be stored.
	 */
	public static ResourceCompartmentStoragePolicy optionalSingleCompartment() {
		return new ResourceCompartmentStoragePolicy(OPTIONAL_SINGLE_COMPARTMENT, true, false, null, false, true);
	}

	/**
	 * Any resource being created of the given type that is not in a single compartment
	 * (either because it has zero compartments, or because it has multiple compartments) will be placed
	 * in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static ResourceCompartmentStoragePolicy nonUniqueCompartmentInDefault() {
		return new ResourceCompartmentStoragePolicy(NON_UNIQUE_COMPARTMENT_IN_DEFAULT, true, false, null, true, true);
	}

	/**
	 * Parses a string representation of a resource compartment storage policy.
	 * Supported values:
	 * <table>
	 *     <tr><th>String</th><th>Description</th></tr>
	 *     <tr><td>ALWAYS_USE_DEFAULT_PARTITION</td><td>Always use the default partition for this resource type.</td></tr>
	 *     <tr><td>MANDATORY_SINGLE_COMPARTMENT</td><td>Require that the resource has exactly one compartment.</td></tr>
	 *     <tr><td>OPTIONAL_SINGLE_COMPARTMENT</td><td>Allow zero or one compartments for this resource type. If the resource is not in any compartment, store it in the default partition.</td></tr>
	 *     <tr><td>NON_UNIQUE_COMPARTMENT_IN_DEFAULT</td><td>Allow the resource to be in multiple compartments or no compartment, store in the default partition if there isn't one unique compartment.</td></tr>
	 *     <tr><td>ALWAYS_USE_PARTITION_ID/[id]</td><td>Always store the resource in the partition with the given ID.</td></tr>
	 * </table>
	 *
	 * @param theName The string representation of the policy.
	 * @return The parsed ResourceCompartmentStoragePolicy object.
	 */
	public static ResourceCompartmentStoragePolicy parse(String theName) {
		return switch (theName) {
			case ALWAYS_USE_DEFAULT_PARTITION -> alwaysUseDefaultPartition();
			case MANDATORY_SINGLE_COMPARTMENT -> mandatorySingleCompartment();
			case OPTIONAL_SINGLE_COMPARTMENT -> optionalSingleCompartment();
			case NON_UNIQUE_COMPARTMENT_IN_DEFAULT -> nonUniqueCompartmentInDefault();
			default -> {
				if (theName.startsWith(ALWAYS_USE_PARTITION_ID_PREFIX)) {
					String partitionIdStr = theName.substring(ALWAYS_USE_PARTITION_ID_PREFIX.length());
					try {
						int partitionId = Integer.parseInt(partitionIdStr);
						yield alwaysUsePartitionId(partitionId);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException(Msg.code(2865) + "Invalid partition ID string: "
								+ ALWAYS_USE_PARTITION_ID_PREFIX + UrlUtil.sanitizeUrlPart(partitionIdStr));
					}
				}
				throw new ConfigurationException("Unknown policy name: " + theName);
			}
		};
	}
}
