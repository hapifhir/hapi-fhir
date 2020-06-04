package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EmpiProviderMergePersonsR4Test extends BaseProviderR4Test {

	private Person myFromPerson;
	private StringType myFromPersonId;
	private Person myToPerson;
	private StringType myToPersonId;

	@Before
	public void before() {
		super.before();
		super.loadEmpiSearchParameters();

		myFromPerson = createPerson();
		myFromPersonId = new StringType(myFromPerson.getIdElement().getValue());
		myToPerson = createPerson();
		myToPersonId = new StringType(myToPerson.getIdElement().getValue());
	}

	@Test
	public void testMatch() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Bundle result = myEmpiProviderR4.match(newJane);
		assertEquals(1, result.getEntry().size());
		assertEquals(createdJane.getId(), result.getEntryFirstRep().getResource().getId());
	}

	@Test
	public void testMerge() {
		Person mergedPerson = myEmpiProviderR4.mergePersons(myFromPersonId, myToPersonId, myRequestDetails);
		assertEquals(myToPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(2, getAllPersons().size());
		assertEquals(1, getAllActivePersons().size());
	}

	@Test
	public void testUnmanagedMerge() {
		StringType fromPersonId = new StringType(createUnmanagedPerson().getIdElement().getValue());
		StringType toPersonId = new StringType(createUnmanagedPerson().getIdElement().getValue());
		try {
			myEmpiProviderR4.mergePersons(fromPersonId, toPersonId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Only EMPI managed resources can be merged.  Empi managed resource have the HAPI-EMPI tag.", e.getMessage());
		}
	}

	@Test
	public void testMergePatients() {
		try {
			StringType patientId = new StringType(createPatient().getIdElement().getValue());
			StringType otherPatientId = new StringType(createPatient().getIdElement().getValue());
			myEmpiProviderR4.mergePersons(patientId, otherPatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}

	}

	@Test
	public void testNullParams() {
		try {
			myEmpiProviderR4.mergePersons(null, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(null, myToPersonId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myFromPersonId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("toPersonId cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBadParams() {
		try {
			myEmpiProviderR4.mergePersons(new StringType("Patient/1"), new StringType("Patient/2"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myFromPersonId, new StringType("Patient/2"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("toPersonId must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/1"), new StringType("Person/1"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId must be different from toPersonId", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/abc"), myToPersonId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myFromPersonId, new StringType("Person/abc"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
	}
}
