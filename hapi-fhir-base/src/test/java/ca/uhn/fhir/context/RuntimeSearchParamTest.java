package ca.uhn.fhir.context;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeSearchParamTest {

	@ParameterizedTest
	@CsvSource({
		"true, Patient, Patient.identifier",
		"true, Patient, Resource.identifier",
		"true, Patient, DomainResource.identifier",
		"true, Patient, (Patient.identifier)",
		"true, Patient, (Patient.identifier )",
		"true, Patient, (Resource.identifier)",
		"true, Patient, (DomainResource.identifier)",
		"true, Patient, (DomainResource.identifier   )",
		"true, Patient, ((Patient.identifier))",
		"true, Patient, ((Patient.identifier ))",
		"true, Patient, ((Patient.identifier )  )",
		"true, Patient, (( Patient.identifier))",
		"true, Patient, ( (  Patient.identifier))",
		"true, Patient, ((Resource.identifier))",
		"true, Patient, (( Resource.identifier))",
		"true, Patient, ((  Resource.identifier))",
		"true, Patient, ((DomainResource.identifier))",
		"true, Patient, (( DomainResource.identifier))",
		"true, Patient, (( DomainResource. identifier))",
		"true, Patient, (( DomainResource . identifier))",
		"true, Patient, ((  DomainResource.identifier))",

		"true, Patient, identifier",
		"true, Patient, (identifier)",

		"false, Patient, Observation.identifier",
		"false, Patient, PatientFoo.identifier",
		"false, Patient, Patient",
		"false, Patient, PatientFoo",
		"false, Patient, ((Observation.identifier)",
		"false, Patient, ((Observation.identifier))",
		"false, Patient, (( Observation.identifier))",
		"false, Patient, ((  Observation.identifier))"
	})
	public void getPathMatchesResourceType(boolean theShouldMatch, String theResourceType, String thePath) {
		assertEquals(theShouldMatch, RuntimeSearchParam.pathMatchesResourceType(theResourceType, thePath));
	}

}
