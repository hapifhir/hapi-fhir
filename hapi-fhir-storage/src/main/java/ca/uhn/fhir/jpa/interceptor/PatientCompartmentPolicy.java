package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.util.UrlUtil;

import java.util.Map;

/**
 * Value type for {@link PatientIdPartitionInterceptor#setResourceTypePolicies(Map)}
 *
 * @see PatientIdPartitionInterceptor#setResourceTypePolicies(Map)
 */
public class PatientCompartmentPolicy {

	private static final String ALWAYS_USE_DEFAULT_PARTITION_NAME = "ALWAYS_USE_DEFAULT_PARTITION";
	private static final String MANDATORY_SINGLE_COMPARTMENT_NAME = "MANDATORY_SINGLE_COMPARTMENT";
	private static final String OPTIONAL_SINGLE_COMPARTMENT_NAME = "OPTIONAL_SINGLE_COMPARTMENT";
	private static final String ALWAYS_USE_PARTITION_ID_PREFIX = "ALWAYS_USE_PARTITION_ID/";

	/**
	 * The given resource type should always be placed in the
	 * {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static final PatientCompartmentPolicy ALWAYS_USE_DEFAULT_PARTITION = new PatientCompartmentPolicy(ALWAYS_USE_DEFAULT_PARTITION_NAME, null);

	/**
	 * The given resource type must contain exactly one reference to a Patient placing it in a single
	 * patient compartment. If the resource is found to belong to multiple patient compartments or no Patient
	 * compartment, an error will be raised and the resource will not be stored.
	 */
	public static final PatientCompartmentPolicy MANDATORY_SINGLE_COMPARTMENT = new PatientCompartmentPolicy(MANDATORY_SINGLE_COMPARTMENT_NAME, null);

	/**
	 * The given resource type may contain zero or one references to a Patient placing it in a single
	 * patient compartment or no patient compartment. If the resource is found to belong to no patient compartment,
	 * it will be stored in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 * If the resource is found to belong to multiple patient compartments, an error will be
	 * raised and the resource will not be stored.
	 */
	public static final PatientCompartmentPolicy OPTIONAL_SINGLE_COMPARTMENT = new PatientCompartmentPolicy(OPTIONAL_SINGLE_COMPARTMENT_NAME, null);

	public static final String NON_UNIQUE_COMPARTMENT_IN_DEFAULT_NAME = "NON_UNIQUE_COMPARTMENT_IN_DEFAULT";
	/**
	 * Any resource being created of the given type that is not in a single compartment
	 * (either because it has zero compartments, or because it has multiple compartments) will be placed
	 * in the {@link ca.uhn.fhir.jpa.model.config.PartitionSettings#setDefaultPartitionId(Integer) default partition}.
	 */
	public static final PatientCompartmentPolicy NON_UNIQUE_COMPARTMENT_IN_DEFAULT = new PatientCompartmentPolicy(NON_UNIQUE_COMPARTMENT_IN_DEFAULT_NAME, null);

	private final String myName;
	private final Integer myAlwaysUsePartitionId;

	private PatientCompartmentPolicy(String theName, Integer theAlwaysUsePartitionId) {
		myName = theName;
		myAlwaysUsePartitionId = theAlwaysUsePartitionId;
	}

	public Integer getAlwaysUsePartitionId() {
		return myAlwaysUsePartitionId;
	}

	@Override
	public String toString() {
		return myName;
	}

	/**
	 * All resources of the given type will be stored in the partition with the given ID.
	 */
	public static PatientCompartmentPolicy alwaysUsePartitionId(int theAlwaysUsePartitionId) {
		return new PatientCompartmentPolicy(ALWAYS_USE_PARTITION_ID_PREFIX + theAlwaysUsePartitionId, theAlwaysUsePartitionId);
	}

	public static PatientCompartmentPolicy parse(String theName) {
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
						throw new IllegalArgumentException("Invalid partition ID string: " + ALWAYS_USE_PARTITION_ID_PREFIX + UrlUtil.sanitizeUrlPart(partitionIdStr));
					}
				}
				throw new IllegalArgumentException("Unknown policy name: " + theName);
			}
		};
	}

}
