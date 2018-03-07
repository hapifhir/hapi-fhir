package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

public interface IVersionSpecificValidationSupport {

	List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode);

	List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode);

	void storeNewCodeSystemVersion(CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<ValueSet> theValueSets);

}
