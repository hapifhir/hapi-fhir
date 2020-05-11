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

	private Person myDeletePerson;
	private StringType myDeletePersonId;
	private Person myKeepPerson;
	private StringType myKeepPersonId;

	@Before
	public void before() {
		super.before();

		myDeletePerson = createPerson();
		myDeletePersonId = new StringType(myDeletePerson.getIdElement().toUnqualifiedVersionless().getValue());
		myKeepPerson = createPerson();
		myKeepPersonId = new StringType(myKeepPerson.getIdElement().toUnqualifiedVersionless().getValue());
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
		Person mergedPerson = myEmpiProviderR4.mergePersons(myDeletePersonId, myKeepPersonId, myRequestDetails);
		assertEquals(myKeepPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(1, getAllPersons().size());
	}

	@Test
	public void testNullParams() {
		try {
			myEmpiProviderR4.mergePersons(null, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToDelete cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(null, myKeepPersonId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToDelete cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToKeep cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBadParams() {
		try {
		myEmpiProviderR4.mergePersons(new StringType("Patient/1"), new StringType("Patient/2"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToDelete must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, new StringType("Patient/2"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToKeep must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/1"), new StringType("Person/1"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToDelete must be different from personToKeep", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/abc"), myKeepPersonId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, new StringType("Person/abc"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
	}
}
