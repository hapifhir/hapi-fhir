package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiProviderClearLinkR4Test extends BaseLinkR4Test {
	protected Practitioner myPractitioner;
	protected StringType myPractitionerId;
	protected IAnyResource myPractitionerPerson;
	protected StringType myPractitionerPersonId;

	@BeforeEach
	public void before() {
		super.before();
		myPractitioner = createPractitionerAndUpdateLinks(new Practitioner());
		myPractitionerId = new StringType(myPractitioner.getIdElement().getValue());
		myPractitionerPerson = getSourceResourceFromTargetResource(myPractitioner);
		myPractitionerPersonId = new StringType(myPractitionerPerson.getIdElement().getValue());
	}

	@Test
	public void testClearAllLinks() {
		assertLinkCount(2);
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		assertNoLinksExist();
	}

	private void assertNoLinksExist() {
		assertNoPatientLinksExist();
		assertNoPractitionerLinksExist();
	}

	private void assertNoPatientLinksExist() {
		assertThat(getPatientLinks(), hasSize(0));
	}

	private void assertNoPractitionerLinksExist() {
		assertThat(getPractitionerLinks(), hasSize(0));
	}

	@Test
	public void testClearPatientLinks() {
		assertLinkCount(2);
		Person read = myPersonDao.read(new IdDt(myPersonId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		myEmpiProviderR4.clearEmpiLinks(new StringType("Patient"), myRequestDetails);
		assertNoPatientLinksExist();
		try {
			myPersonDao.read(new IdDt(myPersonId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {}

	}
	@Test
	public void testPersonsWithMultipleHistoricalVersionsCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource person = getSourceResourceFromTargetResource(patientAndUpdateLinks);
		assertThat(person, is(notNullValue()));
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		assertNoPatientLinksExist();
		person = getSourceResourceFromTargetResource(patientAndUpdateLinks);
		assertThat(person, is(nullValue()));
	}

	@Test
	public void testPersonWithLinksToOtherPersonsCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());

		IAnyResource personFromTarget = getSourceResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource personFromTarget2 = getSourceResourceFromTargetResource(patientAndUpdateLinks1);
		linkPersons(personFromTarget, personFromTarget2);

		//SUT
		myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);

		assertNoPatientLinksExist();
		IBundleProvider search = myPersonDao.search(new SearchParameterMap().setLoadSynchronous(true));
		assertThat(search.size(), is(equalTo(0)));
	}

	@Test
	public void testPersonsWithCircularReferenceCanBeCleared() {
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks2 = createPatientAndUpdateLinks(buildFrankPatient());

		IAnyResource personFromTarget = getSourceResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource personFromTarget1 = getSourceResourceFromTargetResource(patientAndUpdateLinks1);
		IAnyResource personFromTarget2 = getSourceResourceFromTargetResource(patientAndUpdateLinks2);

		// A -> B -> C -> A linkages.
		linkPersons(personFromTarget, personFromTarget1);
		linkPersons(personFromTarget1, personFromTarget2);
		linkPersons(personFromTarget2, personFromTarget);

		//SUT
		Parameters parameters = myEmpiProviderR4.clearEmpiLinks(null, myRequestDetails);
		assertNoPatientLinksExist();
		IBundleProvider search = myPersonDao.search(new SearchParameterMap().setLoadSynchronous(true));
		assertThat(search.size(), is(equalTo(0)));

	}

	//TODO GGG
	private void linkPersons(IAnyResource theSourcePerson, IAnyResource theTargetPerson) {
		throw new UnsupportedOperationException("We need to fix this!");
	}

	@Test
	public void testClearPractitionerLinks() {
		assertLinkCount(2);
		Person read = myPersonDao.read(new IdDt(myPractitionerPersonId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		myEmpiProviderR4.clearEmpiLinks(new StringType("Practitioner"), myRequestDetails);
		assertNoPractitionerLinksExist();
		try {
			myPersonDao.read(new IdDt(myPractitionerPersonId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {}
	}

	@Test
	public void testClearInvalidTargetType() {
		try {
			myEmpiProviderR4.clearEmpiLinks(new StringType("Observation"), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), is(equalTo("$empi-clear does not support resource type: Observation")));
		}
	}

	@Nonnull
	protected List<EmpiLink> getPractitionerLinks() {
		return myEmpiLinkDaoSvc.findEmpiLinksByTarget(myPractitioner);
	}
}
