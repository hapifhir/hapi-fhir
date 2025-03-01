package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class DeleteConflictServiceR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictServiceR4Test.class);

	private final DeleteConflictInterceptor myDeleteInterceptor = new DeleteConflictInterceptor();
	private int myInterceptorDeleteCount;

	@BeforeEach
	public void beforeRegisterInterceptor() {
		myInterceptorRegistry.registerInterceptor(myDeleteInterceptor);
		myInterceptorDeleteCount = 0;
		myDeleteInterceptor.clear();
	}

	@AfterEach
	public void afterUnregisterInterceptor() {
		myInterceptorRegistry.unregisterInterceptor(myDeleteInterceptor);
	}

	@Test
	public void testDeleteFailCallsHook() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = t -> new DeleteConflictOutcome().setShouldRetryCount(0);
		try {
			myOrganizationDao.delete(organizationId);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(515) + "Unable to delete Organization/" + organizationId.getIdPart() + " because at least one resource has a reference to this resource. First reference found was resource Patient/" + patientId.getIdPart() + " in path Patient.managingOrganization", e.getMessage());
		}
		assertEquals(1, myDeleteInterceptor.myDeleteConflictList.size());
		assertEquals(1, myDeleteInterceptor.myCallCount);
		assertEquals(0, myInterceptorDeleteCount);
		myPatientDao.delete(patientId);
		myOrganizationDao.delete(organizationId);
	}

	@Test
	public void testDeleteHookDeletesConflict() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(1, myDeleteInterceptor.myCallCount);
		assertEquals(1, myInterceptorDeleteCount);
	}

	@Test
	public void testDeleteHookDeletesTwoConflicts() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(2, myDeleteInterceptor.myCallCount);
		assertEquals(2, myInterceptorDeleteCount);
	}

	@Test
	public void testDeleteHookDeletesThreeConflicts() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		myDeleteInterceptor.deleteConflictFunction = this::deleteConflicts;
		myOrganizationDao.delete(organizationId);

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(2, myDeleteInterceptor.myCallCount);
		assertEquals(3, myInterceptorDeleteCount);
	}

	@Test
	public void testDeleteHookDeletesLargeNumberOfConflicts() {

		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		// Create 12 conflicts.
		for (int j=0; j < 12 ; j++) {
			Patient patient = new Patient();
			patient.setManagingOrganization(new Reference(organizationId));
			myPatientDao.create(patient).getId().toUnqualifiedVersionless();
		}

		DeleteConflictService.setMaxRetryAttempts(3);
		myStorageSettings.setMaximumDeleteConflictQueryCount(5);
		myDeleteInterceptor.deleteConflictFunction = this::deleteConflictsFixedRetryCount;
		try {
			myOrganizationDao.delete(organizationId);
			// Needs a fourth and final pass to ensure that all conflicts are now gone.
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(821) + DeleteConflictService.MAX_RETRY_ATTEMPTS_EXCEEDED_MSG, e.getMessage());
		}

		// Try again with Maximum conflict count set to 6.
		myDeleteInterceptor.myCallCount=0;
		myInterceptorDeleteCount = 0;
		myStorageSettings.setMaximumDeleteConflictQueryCount(6);

		try {
			myOrganizationDao.delete(organizationId);
		} catch (ResourceVersionConflictException e) {
			fail();
		}

		assertNotNull(myDeleteInterceptor.myDeleteConflictList);
		assertEquals(3, myDeleteInterceptor.myCallCount);
		assertEquals(12, myInterceptorDeleteCount);

	}

	@Test
	public void testBadInterceptorNoInfiniteLoop() {
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Always returning true is bad behaviour.  Our infinite loop checker should halt it
		myDeleteInterceptor.deleteConflictFunction = t -> new DeleteConflictOutcome().setShouldRetryCount(Integer.MAX_VALUE);

		try {
			myOrganizationDao.delete(organizationId);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(821) + DeleteConflictService.MAX_RETRY_ATTEMPTS_EXCEEDED_MSG, e.getMessage());
		}
		assertEquals(1 + DeleteConflictService.MAX_RETRY_ATTEMPTS, myDeleteInterceptor.myCallCount);
	}

	private void setupResourceReferenceTests() {
		// we don't need this
		myInterceptorRegistry.unregisterInterceptor(myDeleteInterceptor);

		// we have to allow versioned references; by default we do not
		myFhirContext.getParserOptions()
			.setStripVersionsFromReferences(false);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void delete_resourceReferencedByVersionedReferenceOnly_succeeds(boolean theUpdateObs) {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		DaoMethodOutcome outcome;

		setupResourceReferenceTests();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		outcome = myObservationDao.create(observation, requestDetails);
		IIdType obsId = outcome.getId();

		// create encounter that references Observation by versioned reference only
		Encounter encounter = new Encounter();
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);
		Coding coding = new Coding();
		coding.setSystem("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode");
		coding.setCode("AMB");
		encounter.setClass_(coding);
		encounter.addReasonReference()
			.setReference(obsId.getValue()); // versioned reference
		outcome = myEncounterDao.create(encounter, requestDetails);
		assertTrue(outcome.getCreated());

		if (theUpdateObs) {
			observation.setId(obsId.toUnqualifiedVersionless());
			observation.setIssued(new Date());
			myObservationDao.update(observation, requestDetails);
		}

		// test
		outcome = myObservationDao.delete(obsId.toUnqualifiedVersionless(), requestDetails);

		// validate
		assertTrue(outcome.getOperationOutcome() instanceof OperationOutcome);
		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertFalse(oo.getIssue().isEmpty());
		assertTrue(oo.getIssue().stream().anyMatch(i -> i.getDiagnostics().contains("Successfully deleted 1 resource(s)")));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void delete_resourceReferencedByNonVersionReferenceOnly_fails(boolean theUpdateObservation) {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		DaoMethodOutcome outcome;

		setupResourceReferenceTests();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		outcome = myObservationDao.create(observation, requestDetails);
		IIdType obsId = outcome.getId();

		// create encounter that references Observation by versioned reference only
		Encounter encounter = new Encounter();
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);
		Coding coding = new Coding();
		coding.setSystem("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode");
		coding.setCode("AMB");
		encounter.setClass_(coding);
		encounter.addReasonReference()
			.setReference(obsId.toUnqualifiedVersionless().getValue()); // versionless reference
		outcome = myEncounterDao.create(encounter, requestDetails);
		assertTrue(outcome.getCreated());

		if (theUpdateObservation) {
			observation.setId(obsId.toUnqualifiedVersionless());
			observation.setIssued(new Date());
			myObservationDao.update(observation, requestDetails);
		}

		// test
		try {
			myObservationDao.delete(obsId.toUnqualifiedVersionless(), requestDetails);
			fail("Deletion of resource referenced by versionless id should fail.");
		} catch (ResourceVersionConflictException ex) {
			assertTrue(ex.getLocalizedMessage().contains("Unable to delete")
					&& ex.getLocalizedMessage().contains("at least one resource has a reference to this resource"),
				ex.getLocalizedMessage());
		}
	}

	@Test
	public void delete_resourceReferencedByNonVersionedAndVersionedReferences_fails() {
		// setup
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		DaoMethodOutcome outcome;

		setupResourceReferenceTests();

		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		outcome = myObservationDao.create(observation, requestDetails);
		IIdType obsId = outcome.getId();

		// create 2 encounters; one referenced with a versionless id, one referenced with a versioned id
		for (String id : new String[] { obsId.getValue(), obsId.toUnqualifiedVersionless().getValue() }) {
			Encounter encounter = new Encounter();
			encounter.setStatus(Encounter.EncounterStatus.FINISHED);
			Coding coding = new Coding();
			coding.setSystem("http://terminology.hl7.org/ValueSet/v3-ActEncounterCode");
			coding.setCode("AMB");
			encounter.setClass_(coding);
			encounter.addReasonReference()
				.setReference(id);
			outcome = myEncounterDao.create(encounter, requestDetails);
			assertTrue(outcome.getCreated());
		}

		// test
		try {
			// versionless delete
			myObservationDao.delete(obsId.toUnqualifiedVersionless(), requestDetails);
			fail("Should not be able to delete observations referenced by versionless id because it is referenced by version AND by versionless.");
		} catch (ResourceVersionConflictException ex) {
			assertTrue(ex.getLocalizedMessage().contains("Unable to delete")
				&& ex.getLocalizedMessage().contains("at least one resource has a reference to this resource"),
				ex.getLocalizedMessage());
		}
	}

	@Test
	public void testNoDuplicateConstraintReferences() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Condition condition = new Condition();
		condition.setSubject(new Reference(patientId));
		condition.setAsserter(new Reference(patientId));
		myConditionDao.create(condition);

		List<DeleteConflict> conflicts = new ArrayList<>();
		myDeleteInterceptor.deleteConflictFunction = t -> {
			for (DeleteConflict next : t) {
				conflicts.add(next);
			}
			return new DeleteConflictOutcome().setShouldRetryCount(0);
		};

		try {
			myPatientDao.delete(patientId);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		assertThat(conflicts).hasSize(1);
	}

	private DeleteConflictOutcome deleteConflicts(DeleteConflictList theList) {
		for (DeleteConflict next : theList) {
			IdDt source = next.getSourceId();
			if ("Patient".equals(source.getResourceType())) {
				ourLog.info("Deleting {}", source);
				myPatientDao.delete(source);
				++myInterceptorDeleteCount;
			}
		}
		return new DeleteConflictOutcome().setShouldRetryCount(myInterceptorDeleteCount);
	}

	private DeleteConflictOutcome deleteConflictsFixedRetryCount(DeleteConflictList theList) {
		TransactionDetails transactionDetails = new TransactionDetails();
		for (DeleteConflict next : theList) {
			IdDt source = next.getSourceId();
			if ("Patient".equals(source.getResourceType())) {
				ourLog.info("Deleting {}", source);
				myPatientDao.delete(source, theList, null, transactionDetails);
				++myInterceptorDeleteCount;
			}
		}
		return new DeleteConflictOutcome().setShouldRetryCount(DeleteConflictService.MAX_RETRY_ATTEMPTS);
	}

	private static class DeleteConflictInterceptor {
		int myCallCount;
		DeleteConflictList myDeleteConflictList;
		Function<DeleteConflictList, DeleteConflictOutcome> deleteConflictFunction;

		@Hook(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS)
		public DeleteConflictOutcome deleteConflicts(DeleteConflictList theDeleteConflictList) {
			++myCallCount;
			myDeleteConflictList = theDeleteConflictList;
			return deleteConflictFunction.apply(theDeleteConflictList);
		}

		public void clear() {
			myDeleteConflictList = null;
			myCallCount = 0;
		}
	}

}
