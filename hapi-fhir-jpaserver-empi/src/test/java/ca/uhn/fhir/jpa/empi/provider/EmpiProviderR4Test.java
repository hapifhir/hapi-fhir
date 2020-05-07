package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.provider.EmpiProviderR4;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EmpiProviderR4Test extends BaseEmpiR4Test {
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private IEmpiPersonMergerSvc myPersonMergerSvc;
	@Autowired
	private IResourceLoader myResourceLoader;

	EmpiProviderR4 myEmpiProviderR4;
	private Person myDeletePerson;
	private StringType myDeletePersonId;
	private Person myKeepPerson;
	private StringType myKeepPersonId;

	@Before
	public void before() {
		myEmpiProviderR4 = new EmpiProviderR4(myEmpiMatchFinderSvc, myPersonMergerSvc, myResourceLoader);

		myDeletePerson = createPerson();
		myDeletePersonId = new StringType(myDeletePerson.getIdElement().toUnqualifiedVersionless().getValue());
		myKeepPerson = createPerson();
		myKeepPersonId = new StringType(myKeepPerson.getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testMerge() {
		Person mergedPerson = myEmpiProviderR4.mergePersons(myDeletePersonId, myKeepPersonId);
		assertEquals(myKeepPerson.getIdElement(), mergedPerson.getIdElement());
		assertThat(mergedPerson, is(samePersonAs(mergedPerson)));
		assertEquals(1, getAllPersons().size());
	}

	@Test
	public void testNullParams() {
		try {
			myEmpiProviderR4.mergePersons(null, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToDelete cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(null, myKeepPersonId);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToDelete cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personToKeep cannot be null", e.getMessage());
		}
	}

	@Test
	public void testBadParams() {
		try {
		myEmpiProviderR4.mergePersons(new StringType("Patient/1"), new StringType("Patient/2"));
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToDelete must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, new StringType("Patient/2"));
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToKeep must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/1"), new StringType("Person/1"));
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToDelete must be different from personToKeep", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/abc"), myKeepPersonId);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myDeletePersonId, new StringType("Person/abc"));
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
	}
}
