package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RuntimeSearchParamTest {

	@Test
	public void getPathMatchesResourceType() {
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "Patient.identifier"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "Resource.identifier"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "DomainResource.identifier"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(Patient.identifier)"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(Resource.identifier)"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(DomainResource.identifier)"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "((Patient.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(( Patient.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "( (  Patient.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "((Resource.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(( Resource.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "((  Resource.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "((DomainResource.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(( DomainResource.identifier))"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "((  DomainResource.identifier))"));

		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "identifier"));
		assertTrue(RuntimeSearchParam.pathMatchesResourceType("Patient", "(identifier)"));

		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "Observation.identifier"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "PatientFoo.identifier"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "Patient"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "PatientFoo"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "((Observation.identifier)"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "((Observation.identifier))"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "(( Observation.identifier))"));
		assertFalse(RuntimeSearchParam.pathMatchesResourceType("Patient", "((  Observation.identifier))"));
	}

}
