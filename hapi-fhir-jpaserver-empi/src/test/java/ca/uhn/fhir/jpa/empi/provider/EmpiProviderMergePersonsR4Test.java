package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiProviderMergePersonsR4Test extends BaseProviderR4Test {

	private Patient myFromSourcePatient;
	private StringType myFromSourcePatientId;
	private Patient myToSourcePatient;
	private StringType myToSourcePatientId;

	@Override
	@BeforeEach
	public void before() {
		super.before();
		super.loadEmpiSearchParameters();

		myFromSourcePatient = createSourceResourcePatient();
		myFromSourcePatientId = new StringType(myFromSourcePatient.getIdElement().getValue());
		myToSourcePatient = createSourceResourcePatient();
		myToSourcePatientId = new StringType(myToSourcePatient.getIdElement().getValue());
	}

	@Test
	public void testMerge() {
		//TODO GGG RP fix
		IBaseResource mergedSourcePatient = myEmpiProviderR4.mergePersons(myFromSourcePatientId, myToSourcePatientId, myRequestDetails);
		assertEquals(myToSourcePatient.getIdElement(), mergedSourcePatient.getIdElement());
//		TODO GGG RP FIX
		//assertThat(mergedSourcePatient, is(sameSourceResourceAs(myToSourcePatient)));
		assertEquals(2, getAllSourcePatients().size());
		assertEquals(1, getAllActiveSourcePatients().size());

		Patient fromSourcePatient = myPatientDao.read(myFromSourcePatient.getIdElement().toUnqualifiedVersionless());
		assertThat(fromSourcePatient.getActive(), is(false));
		//TODO GGG eventually this will need to check a redirect... this is a hack which doesnt work
		Optional<Identifier> redirect = fromSourcePatient.getIdentifier().stream().filter(theIdentifier -> theIdentifier.getSystem().equals("REDIRECT")).findFirst();
		assertThat(redirect.get().getValue(), is(equalTo(myToSourcePatient.getIdElement().toUnqualified().getValue())));
		//List<Person.PersonLinkComponent> links = fromSourcePatient.getLink();
		//assertThat(links, hasSize(1));
      //assertThat(links.get(0).getTarget().getReference(), is (myToSourcePatient.getIdElement().toUnqualifiedVersionless().getValue()));
		//assertThat(links.get(0).getAssurance(), is (AssuranceLevelUtil.getAssuranceLevel(EmpiMatchResultEnum.REDIRECT, EmpiLinkSourceEnum.MANUAL).toR4()));
	}

	@Test
	public void testUnmanagedMerge() {
		StringType fromPersonId = new StringType(createUnmanagedSourceResource().getIdElement().getValue());
		StringType toPersonId = new StringType(createUnmanagedSourceResource().getIdElement().getValue());
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
			assertThat(e.getMessage(), endsWith("must have form Person/<id> where <id> is the id of the person"));
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
			myEmpiProviderR4.mergePersons(null, myToSourcePatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId cannot be null", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myFromSourcePatientId, null, myRequestDetails);
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
			assertThat(e.getMessage(), endsWith(" must have form Person/<id> where <id> is the id of the person"));
		}
		try {
			myEmpiProviderR4.mergePersons(myFromSourcePatientId, new StringType("Patient/2"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form Person/<id> where <id> is the id of the person"));
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/1"), new StringType("Person/1"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("fromPersonId must be different from toPersonId", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(new StringType("Person/abc"), myToSourcePatientId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
		try {
			myEmpiProviderR4.mergePersons(myFromSourcePatientId, new StringType("Person/abc"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/abc is not known", e.getMessage());
		}
	}
}
