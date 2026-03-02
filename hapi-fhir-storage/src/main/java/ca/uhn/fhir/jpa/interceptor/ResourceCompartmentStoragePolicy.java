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
import ca.uhn.fhir.util.UrlUtil;

import java.util.Map;

/**
 * Value type for {@link PatientIdPartitionInterceptor#setResourceTypePolicies(Map)}
 *
 * @see PatientIdPartitionInterceptor#setResourceTypePolicies(Map)
 */
public class ResourceCompartmentStoragePolicy {

	private static final String NON_UNIQUE_COMPARTMENT_IN_DEFAULT_NAME = "NON_UNIQUE_COMPARTMENT_IN_DEFAULT";
	private static final String ALWAYS_USE_DEFAULT_PARTITION_NAME = "ALWAYS_USE_DEFAULT_PARTITION";
	private static final String MANDATORY_SINGLE_COMPARTMENT_NAME = "MANDATORY_SINGLE_COMPARTMENT";
	private static final String OPTIONAL_SINGLE_COMPARTMENT_NAME = "OPTIONAL_SINGLE_COMPARTMENT";

	/**
	 * Any resource being created of the given type that is not in a single compartment
	 * (either because it has zero compartments, or because it has multiple compartments) will be placed
	 * in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static final ResourceCompartmentStoragePolicy NON_UNIQUE_COMPARTMENT_IN_DEFAULT =
			new ResourceCompartmentStoragePolicy(NON_UNIQUE_COMPARTMENT_IN_DEFAULT_NAME, true, null);

	/**
	 * The given resource type should always be placed in the
	 * {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static final ResourceCompartmentStoragePolicy ALWAYS_USE_DEFAULT_PARTITION =
			new ResourceCompartmentStoragePolicy(ALWAYS_USE_DEFAULT_PARTITION_NAME, false, null);

	/**
	 * The given resource type must contain exactly one reference to a Patient placing it in a single
	 * patient compartment. If the resource is found to belong to multiple patient compartments or no Patient
	 * compartment, an error will be raised and the resource will not be stored.
	 */
	public static final ResourceCompartmentStoragePolicy MANDATORY_SINGLE_COMPARTMENT =
			new ResourceCompartmentStoragePolicy(MANDATORY_SINGLE_COMPARTMENT_NAME, true, null);

	/**
	 * The given resource type may contain zero or one references to a Patient placing it in a single
	 * patient compartment or no patient compartment. If the resource is found to belong to no patient compartment,
	 * it will be stored in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 * If the resource is found to belong to multiple patient compartments, an error will be
	 * raised and the resource will not be stored.
	 */
	public static final ResourceCompartmentStoragePolicy OPTIONAL_SINGLE_COMPARTMENT =
			new ResourceCompartmentStoragePolicy(OPTIONAL_SINGLE_COMPARTMENT_NAME, true, null);

	private static final String ALWAYS_USE_PARTITION_ID_PREFIX = "ALWAYS_USE_PARTITION_ID/";
	private final String myName;
	private final Integer myAlwaysUsePartitionId;
	private final boolean myOnlyAppliesToCompartmentResourceTypes;

	private ResourceCompartmentStoragePolicy(
			String theName, boolean theOnlyAppliesToCompartmentResourceTypes, Integer theAlwaysUsePartitionId) {
		myName = theName;
		myOnlyAppliesToCompartmentResourceTypes = theOnlyAppliesToCompartmentResourceTypes;
		myAlwaysUsePartitionId = theAlwaysUsePartitionId;
	}

	/**
	 * Is this a policy that can only apply to resources in the patient compartment?
	 */
	public boolean isOnlyAppliesToCompartmentResourceTypes() {
		return myOnlyAppliesToCompartmentResourceTypes;
	}

	public Integer getAlwaysUsePartitionId() {
		return myAlwaysUsePartitionId;
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
				ALWAYS_USE_PARTITION_ID_PREFIX + theAlwaysUsePartitionId, false, theAlwaysUsePartitionId);
	}

	public static ResourceCompartmentStoragePolicy parse(String theName) {
		return switch (theName) {
				// FIXME: add methods and make the constants be default access?
			case ALWAYS_USE_DEFAULT_PARTITION_NAME -> ALWAYS_USE_DEFAULT_PARTITION;
			case MANDATORY_SINGLE_COMPARTMENT_NAME -> MANDATORY_SINGLE_COMPARTMENT;
			case OPTIONAL_SINGLE_COMPARTMENT_NAME -> OPTIONAL_SINGLE_COMPARTMENT;
			case NON_UNIQUE_COMPARTMENT_IN_DEFAULT_NAME -> NON_UNIQUE_COMPARTMENT_IN_DEFAULT;
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
