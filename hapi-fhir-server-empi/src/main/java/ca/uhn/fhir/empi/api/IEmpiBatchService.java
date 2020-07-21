package ca.uhn.fhir.empi.api;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IEmpiBatchService {

	/**
	 * Submit all eligible resources for EMPI processing.
	 *  @param theCriteria The FHIR search critieria for filtering the resources to be submitted for EMPI. NOTE:
	 *                    When using this function, the criteria supplied must be valid for all EMPI types. e.g. , if you
	 *                    run this with the criteria birthDate=1990-06-28, it will fail, as Practitioner's do not have a birthday.
	 *                    Use with caution.
	 *
	 */
	int runEmpiOnAllTargetTypes(String theCriteria);

	/**
	 * Given a type and a search criteria, submit all found resources for EMPI processing.
	 *
	 * @param theTargetType the resource type that you wish to execute a search over for submission to EMPI.
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for EMPI.
	 * @return the number of resources submitted for EMPI processing.
	 */
	int runEmpiOnTargetType(String theTargetType, String theCriteria);

	/**
	 * Convenience method which calls {@link #runEmpiOnTargetType(String, String)} with the type pre-populated.
	 *
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for EMPI.
	 * @return the number of resources submitted for EMPI processing.
	 */
	int runEmpiOnPractitionerType(String theCriteria);

	/**
	 * Convenience method which calls {@link #runEmpiOnTargetType(String, String)} with the type pre-populated.
	 *
	 * @param theCriteria The FHIR search critieria for filtering the resources to be submitted for EMPI.
	 * @return the number of resources submitted for EMPI processing.
	 */
	int runEmpiOnPatientType(String theCriteria);

	/**
	 * Given an ID and a target type valid for EMPI, manually submit the given ID for EMPI processing.
	 * @param theId the ID of the resource to process for EMPI.
	 * @param theTargetType The type of this resource.
	 * @return the constant `1`, as if this function returns successfully, it will have processed one resource for EMPI.
	 */
	int runEmpiOnTarget(IIdType theId, String theTargetType);

	/**
	 * Convenience method which calls {@link #runEmpiOnTarget(IIdType, String)} with the type pre-populated
	 *
	 * @param theId the ID of the resource to run EMPI on.
	 *
	 * @return the constant 1, as this will perform one EMPI submission.
	 */
	int runEmpiOnTargetPractitioner(IIdType theId);

	/**
	 * Convenience method which calls {@link #runEmpiOnTarget(IIdType, String)} with the type pre-populated
	 *
	 * @param theId the ID of the resource to run EMPI on.
	 *
	 * @return the constant 1, as this will perform one EMPI submission.
	 */
	int runEmpiOnTargetPatient(IIdType theId);


}
