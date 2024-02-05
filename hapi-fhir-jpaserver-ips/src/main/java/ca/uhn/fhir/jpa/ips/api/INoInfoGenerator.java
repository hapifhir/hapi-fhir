package ca.uhn.fhir.jpa.ips.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * This interface is invoked when a section has no resources found, and should generate
 * a "stub" resource explaining why. Typically this would be content such as "no information
 * is available for this section", and might indicate for example that the absence of
 * AllergyIntolerance resources only indicates that the allergy status is not known, not that
 * the patient has no allergies.
 */
public interface INoInfoGenerator {

	/**
	 * Generate an appropriate no-info resource. The resource does not need to have an ID populated,
	 * although it can if it is a resource found in the repository.
	 */
	IBaseResource generate(IIdType theSubjectId);
}
