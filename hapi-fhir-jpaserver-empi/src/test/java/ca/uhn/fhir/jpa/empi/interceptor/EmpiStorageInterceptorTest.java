package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.helper.EmpiHelperConfig;
import ca.uhn.fhir.jpa.empi.helper.EmpiHelperR4;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static ca.uhn.fhir.empi.api.EmpiConstants.CODE_HAPI_EMPI_MANAGED;
import static ca.uhn.fhir.empi.api.EmpiConstants.SYSTEM_EMPI_MANAGED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {EmpiHelperConfig.class})
public class EmpiStorageInterceptorTest extends BaseEmpiR4Test {

	private static final Logger ourLog = getLogger(EmpiStorageInterceptorTest.class);

	@Rule
	@Autowired
	public EmpiHelperR4 myEmpiHelper;
	@Autowired
	private IdHelperService myIdHelperService;

	@Test
	public void testCreatePatient() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Patient());
		assertLinkCount(1);
	}

	@Test
	public void testCreatePractitioner() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Practitioner());
		assertLinkCount(1);
	}


	@Test
	public void testCreatePerson() throws InterruptedException {
		myPersonDao.create(new Person());
		assertLinkCount(0);
	}

	@Test
	public void testDeletePersonDeletesLinks() throws InterruptedException {
		myEmpiHelper.createWithLatch(new Patient());
		assertLinkCount(1);
		Person person = getOnlyPerson();
		myPersonDao.delete(person.getIdElement());
		assertLinkCount(0);
	}

	@Test
	public void testCreatePersonWithEmpiTagForbidden() throws InterruptedException {
		//Creating a person with the EMPI-MANAGED tag should fail
		Person person = new Person();
		person.getMeta().addTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED, "User is managed by EMPI");
		try {
			myEmpiHelper.doCreateResource(person, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot create or modify Persons who are managed by EMPI.", e.getMessage());
		}
	}

	@Test
	public void testPersonRecordsManagedByEmpiAllShareSameTag() throws InterruptedException {
		myEmpiHelper.createWithLatch(buildJanePatient());
		myEmpiHelper.createWithLatch(buildPaulPatient());

		IBundleProvider search = myPersonDao.search(new SearchParameterMap().setLoadSynchronous(true));
		List<IBaseResource> resources = search.getResources(0, search.size());

		for (IBaseResource person: resources) {
			assertThat(person.getMeta().getTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED), is(notNullValue()));
		}
	}

	@Test
	public void testNonEmpiManagedPersonCannotHaveEmpiManagedTagAddedToThem() {
		//Person created manually.
		Person person = new Person();
		DaoMethodOutcome daoMethodOutcome = myEmpiHelper.doCreateResource(person, true);
		assertNotNull(daoMethodOutcome.getId());

		//Updating that person to set them as EMPI managed is not allowed.
		person.getMeta().addTag(SYSTEM_EMPI_MANAGED, CODE_HAPI_EMPI_MANAGED, "User is managed by EMPI");
		try {
			myEmpiHelper.doUpdateResource(person, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("The EMPI status of a Person may not be changed once created.", e.getMessage() );
		}
	}

	@Test
	public void testEmpiManagedPersonCannotBeModifiedByPersonUpdateRequest() throws InterruptedException {
		// When EMPI is enabled, only the EMPI system is allowed to modify Person links of Persons with the EMPI-MANAGED tag.
		Patient patient = new Patient();
		IIdType patientId = myEmpiHelper.createWithLatch(new Patient()).getDaoMethodOutcome().getId().toUnqualifiedVersionless();

		patient.setId(patientId);

		//Updating a Person who was created via EMPI should fail.
		EmpiLink empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(patient)).get();
		Long personPid = empiLink.getPersonPid();
		Person empiPerson= (Person)myPersonDao.readByPid(new ResourcePersistentId(personPid));
		empiPerson.setGender(Enumerations.AdministrativeGender.MALE);
		try {
			myEmpiHelper.doUpdateResource(empiPerson, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertEquals("Cannot create or modify Persons who are managed by EMPI.", e.getMessage());
		}
	}
	
	@Test
	public void testEmpiPointcutReceivesTransactionLogMessages() throws InterruptedException {
		EmpiHelperR4.OutcomeAndLogMessageWrapper wrapper = myEmpiHelper.createWithLatch(buildJanePatient());

		TransactionLogMessages empiTransactionLogMessages = wrapper.getLogMessages();

		//There is no TransactionGuid here as there is no TransactionLog in this context.
		assertThat(empiTransactionLogMessages.getTransactionGuid(), is(nullValue()));

		List<String> messages = empiTransactionLogMessages.getValues();
		assertThat(messages, hasSize(3));
		assertThat(messages.get(0), is(equalToIgnoringCase("There were no matched candidates for EMPI, creating a new Person.")));
		assertThat(messages.get(1), containsString("Creating new link from new Person -> "));
		assertThat(messages.get(1), containsString("with IdentityAssuranceLevel: LEVEL3"));
		assertThat(messages.get(2), containsString("Creating EmpiLink from"));
		assertThat(messages.get(2), containsString("MATCH"));
	}

	@Test
	public void testWhenASingularPatientUpdatesExternalEidThatPersonEidIsUpdated() throws InterruptedException {
		Patient jane = addExternalEID(buildJanePatient(), "some_eid");
		EmpiHelperR4.OutcomeAndLogMessageWrapper latch = myEmpiHelper.createWithLatch(jane);
		jane.setId(latch.getDaoMethodOutcome().getId());
		clearExternalEIDs(jane);
		jane = addExternalEID(jane, "some_new_eid");

		EmpiHelperR4.OutcomeAndLogMessageWrapper outcomeWrapper = myEmpiHelper.updateWithLatch(jane);
		Person person = getPersonFromTarget(jane);
		List<CanonicalEID> externalEids = myEIDHelper.getExternalEid(person);
		assertThat(externalEids, hasSize(1));
		assertThat("some_new_eid", is(equalTo(externalEids.get(0).getValue())));
	}
	@Test
	public void testWhenEidUpdatesAreDisabledForbidsUpdatesToEidsOnTargets() throws InterruptedException {
		setPreventEidUpdates(true);
		Patient jane = addExternalEID(buildJanePatient(), "some_eid");
		EmpiHelperR4.OutcomeAndLogMessageWrapper latch = myEmpiHelper.createWithLatch(jane);
		jane.setId(latch.getDaoMethodOutcome().getId());
		clearExternalEIDs(jane);
		jane = addExternalEID(jane, "some_new_eid");
		try {
			myEmpiHelper.doUpdateResource(jane, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), is(equalTo("While running with EID updates disabled, EIDs may not be updated on Patient/Practitioner resources")));
		}
		setPreventEidUpdates(false);
	}

	@Test
	public void testWhenMultipleEidsAreDisabledThatTheInterceptorRejectsCreatesWithThem() {
		setPreventMultipleEids(true);
		Patient patient = buildJanePatient();
		addExternalEID(patient, "123");
		addExternalEID(patient, "456");
		try {
			myEmpiHelper.doCreateResource(patient, true);
			fail();
		} catch (ForbiddenOperationException e) {
			assertThat(e.getMessage(), is(equalTo("While running with multiple EIDs disabled, Patient/Practitioner resources may have at most one EID.")));
		}

		setPreventMultipleEids(false);

	}

	private void setPreventEidUpdates(boolean thePrevent) {
		((EmpiSettings)myEmpiConfig).setPreventEidUpdates(thePrevent);
	}

	private void setPreventMultipleEids(boolean thePrevent) {
		((EmpiSettings)myEmpiConfig).setPreventMultipleEids(thePrevent);
	}

}
