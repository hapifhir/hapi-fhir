package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4VersionedReferenceTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4VersionedReferenceTest.class);

	@AfterEach
	public void afterEach() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myFhirContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths().clear();
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
		myModelConfig.setRespectVersionsForSearchIncludes(new ModelConfig().isRespectVersionsForSearchIncludes());
		myModelConfig.setAutoVersionReferenceAtPaths(new ModelConfig().getAutoVersionReferenceAtPaths());
	}

	@Test
	public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToUpsertWithNop() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("ExplanationOfBenefit.patient");

		// We'll submit the same bundle twice. It has an UPSERT (with no changes
		// the second time) on a Patient, and a CREATE on an ExplanationOfBenefit
		// referencing that Patient.
		Supplier<Bundle> supplier = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient patient = new Patient();
			patient.setId("Patient/A");
			patient.setActive(true);
			bb.addTransactionUpdateEntry(patient);

			ExplanationOfBenefit eob = new ExplanationOfBenefit();
			eob.setId(IdType.newRandomUuid());
			eob.setPatient(new Reference("Patient/A"));
			bb.addTransactionCreateEntry(eob);

			return (Bundle) bb.getBundle();
		};

		// Send it the first time
		Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Patient/A/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
		String eobId1 = outcome1.getEntry().get(1).getResponse().getLocation();
		assertThat(eobId1, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
		assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

		// Send it again
		Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Patient/A/_history/1", outcome2.getEntry().get(0).getResponse().getLocation());
		String eobId2 = outcome2.getEntry().get(1).getResponse().getLocation();
		assertThat(eobId2, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
		assertEquals("Patient/A/_history/1", eob2.getPatient().getReference());
	}

	@Test
	public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToVersionedReferenceToUpsertWithNop() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths(
			"Patient.managingOrganization",
			"ExplanationOfBenefit.patient"
		);

		// We'll submit the same bundle twice. It has an UPSERT (with no changes
		// the second time) on a Patient, and a CREATE on an ExplanationOfBenefit
		// referencing that Patient.
		Supplier<Bundle> supplier = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Organization organization = new Organization();
			organization.setId("Organization/O");
			organization.setActive(true);
			bb.addTransactionUpdateEntry(organization);

			Patient patient = new Patient();
			patient.setId("Patient/A");
			patient.setManagingOrganization(new Reference("Organization/O"));
			patient.setActive(true);
			bb.addTransactionUpdateEntry(patient);

			ExplanationOfBenefit eob = new ExplanationOfBenefit();
			eob.setId(IdType.newRandomUuid());
			eob.setPatient(new Reference("Patient/A"));
			bb.addTransactionCreateEntry(eob);

			return (Bundle) bb.getBundle();
		};

		// Send it the first time
		Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Organization/O/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/A/_history/1", outcome1.getEntry().get(1).getResponse().getLocation());
		String eobId1 = outcome1.getEntry().get(2).getResponse().getLocation();
		assertThat(eobId1, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
		assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

		// Send it again
		Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Organization/O/_history/1", outcome2.getEntry().get(0).getResponse().getLocation());
		// Technically the patient did not change - If this ever got optimized so that the version here
		// was 1 that would be even better
		String patientId = outcome2.getEntry().get(1).getResponse().getLocation();
		assertEquals("Patient/A/_history/2", patientId);
		String eobId2 = outcome2.getEntry().get(2).getResponse().getLocation();
		assertThat(eobId2, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		Patient patient = myPatientDao.read(new IdType("Patient/A"), new SystemRequestDetails());
		assertEquals(patientId, patient.getId());

		ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
		assertEquals(patientId, eob2.getPatient().getReference());
	}

	@Test
	public void testCreateAndUpdateVersionedReferencesInTransaction_VersionedReferenceToVersionedReferenceToUpsertWithChange() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths(
			"Patient.managingOrganization",
			"ExplanationOfBenefit.patient"
		);

		AtomicInteger counter = new AtomicInteger();
		Supplier<Bundle> supplier = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Organization organization = new Organization();
			organization.setId("Organization/O");
			organization.setName("Org " + counter.incrementAndGet()); // change each time
			organization.setActive(true);
			bb.addTransactionUpdateEntry(organization);

			Patient patient = new Patient();
			patient.setId("Patient/A");
			patient.setManagingOrganization(new Reference("Organization/O"));
			patient.setActive(true);
			bb.addTransactionUpdateEntry(patient);

			ExplanationOfBenefit eob = new ExplanationOfBenefit();
			eob.setId(IdType.newRandomUuid());
			eob.setPatient(new Reference("Patient/A"));
			bb.addTransactionCreateEntry(eob);

			return (Bundle) bb.getBundle();
		};

		// Send it the first time
		Bundle outcome1 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Organization/O/_history/1", outcome1.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/A/_history/1", outcome1.getEntry().get(1).getResponse().getLocation());
		String eobId1 = outcome1.getEntry().get(2).getResponse().getLocation();
		assertThat(eobId1, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		ExplanationOfBenefit eob1 = myExplanationOfBenefitDao.read(new IdType(eobId1), new SystemRequestDetails());
		assertEquals("Patient/A/_history/1", eob1.getPatient().getReference());

		// Send it again
		Bundle outcome2 = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals("Organization/O/_history/2", outcome2.getEntry().get(0).getResponse().getLocation());
		String patientId = outcome2.getEntry().get(1).getResponse().getLocation();
		assertEquals("Patient/A/_history/2", patientId);
		String eobId2 = outcome2.getEntry().get(2).getResponse().getLocation();
		assertThat(eobId2, matchesPattern("ExplanationOfBenefit/[0-9]+/_history/1"));

		Patient patient = myPatientDao.read(new IdType("Patient/A"), new SystemRequestDetails());
		assertEquals(patientId, patient.getId());

		ExplanationOfBenefit eob2 = myExplanationOfBenefitDao.read(new IdType(eobId2), new SystemRequestDetails());
		assertEquals(patientId, eob2.getPatient().getReference());
	}

	@Test
	public void testStoreAndRetrieveVersionedReference() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		Patient p = new Patient();
		p.setActive(true);
		IIdType patientId = myPatientDao.create(p).getId().toUnqualified();
		assertEquals("1", patientId.getVersionIdPart());
		assertEquals(null, patientId.getBaseUrl());
		String patientIdString = patientId.getValue();

		Observation observation = new Observation();
		observation.getSubject().setReference(patientIdString);
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Read back
		observation = myObservationDao.read(observationId);
		assertEquals(patientIdString, observation.getSubject().getReference());
	}

	@Test
	public void testDontOverwriteExistingVersion() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		Patient p = new Patient();
		p.setActive(true);
		myPatientDao.create(p);

		// Update the patient
		p.setActive(false);
		IIdType patientId = myPatientDao.update(p).getId().toUnqualified();

		assertEquals("2", patientId.getVersionIdPart());
		assertEquals(null, patientId.getBaseUrl());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Read back
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.withVersion("1").getValue(), observation.getSubject().getReference());
	}

	@Test
	public void testInsertVersionedReferenceAtPath() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		Patient p = new Patient();
		p.setActive(true);
		IIdType patientId = myPatientDao.create(p).getId().toUnqualified();
		assertEquals("1", patientId.getVersionIdPart());
		assertEquals(null, patientId.getBaseUrl());
		String patientIdString = patientId.getValue();

		// Create - put an unversioned reference in the subject
		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.toVersionless().getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientIdString, observation.getSubject().getReference());

		myCaptureQueriesListener.clear();

		// Update - put an unversioned reference in the subject
		observation = new Observation();
		observation.setId(observationId);
		observation.addIdentifier().setSystem("http://foo").setValue("bar");
		observation.getSubject().setReference(patientId.toVersionless().getValue());
		myObservationDao.update(observation);

		// Make sure we're not introducing any extra DB operations
		assertEquals(6, myCaptureQueriesListener.logSelectQueries().size());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientIdString, observation.getSubject().getReference());
	}

	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_SourceAndTargetBothCreated() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient);

		Encounter encounter = new Encounter();
		encounter.setId(IdType.newRandomUuid());
		encounter.addIdentifier().setSystem("http://baz").setValue("baz");
		builder.addTransactionCreateEntry(encounter);

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		observation.getEncounter().setReference(encounter.getId()); // not versioned
		builder.addTransactionCreateEntry(observation);

		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType encounterId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		IdType observationId = new IdType(outcome.getEntry().get(2).getResponse().getLocation());
		assertTrue(patientId.hasVersionIdPart());
		assertTrue(encounterId.hasVersionIdPart());
		assertTrue(observationId.hasVersionIdPart());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.getValue(), observation.getSubject().getReference());
		assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());
	}

	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_TargetConditionalCreatedNop() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		{
			// Create patient
			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.setActive(true);
			myPatientDao.create(patient).getId();

			// Update patient to make a second version
			patient.setActive(false);
			myPatientDao.update(patient);

			// Create encounter
			Encounter encounter = new Encounter();
			encounter.setId(IdType.newRandomUuid());
			encounter.addIdentifier().setSystem("http://baz").setValue("baz");
			myEncounterDao.create(encounter);
		}

		// Verify Patient Version
		assertEquals("2", myPatientDao.search(SearchParameterMap.newSynchronous("active", new TokenParam("false"))).getResources(0, 1).get(0).getIdElement().getVersionIdPart());

		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient).conditional("Patient?active=false");

		Encounter encounter = new Encounter();
		encounter.setId(IdType.newRandomUuid());
		encounter.addIdentifier().setSystem("http://baz").setValue("baz");
		builder.addTransactionCreateEntry(encounter).conditional("Encounter?identifier=http://baz|baz");

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		observation.getEncounter().setReference(encounter.getId()); // not versioned
		builder.addTransactionCreateEntry(observation);

		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("200 OK", outcome.getEntry().get(0).getResponse().getStatus());
		assertEquals("200 OK", outcome.getEntry().get(1).getResponse().getStatus());
		assertEquals("201 Created", outcome.getEntry().get(2).getResponse().getStatus());
		IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType encounterId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		IdType observationId = new IdType(outcome.getEntry().get(2).getResponse().getLocation());
		assertEquals("2", patientId.getVersionIdPart());
		assertEquals("1", encounterId.getVersionIdPart());
		assertEquals("1", observationId.getVersionIdPart());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.getValue(), observation.getSubject().getReference());
		assertEquals("2", observation.getSubject().getReferenceElement().getVersionIdPart());
		assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());
	}


	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_TargetUpdate() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myDaoConfig.setDeleteEnabled(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		{
			// Create patient
			Patient patient = new Patient();
			patient.setId("PATIENT");
			patient.setActive(true);
			myPatientDao.update(patient).getId();

			// Update patient to make a second version
			patient.setActive(false);
			myPatientDao.update(patient);
		}

		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("Patient/PATIENT");
		patient.setActive(true);
		builder.addTransactionUpdateEntry(patient);

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		builder.addTransactionCreateEntry(observation);

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("200 OK", outcome.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", outcome.getEntry().get(1).getResponse().getStatus());
		IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType observationId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		assertEquals("3", patientId.getVersionIdPart());
		assertEquals("1", observationId.getVersionIdPart());

		// Make sure we're not introducing any extra DB operations
		assertEquals(3, myCaptureQueriesListener.logSelectQueries().size());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.getValue(), observation.getSubject().getReference());

	}


	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_TargetUpdateConditional() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		{
			// Create patient
			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.setActive(true);
			myPatientDao.create(patient).getId();

			// Update patient to make a second version
			patient.setActive(false);
			myPatientDao.update(patient);
		}

		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setDeceased(new BooleanType(true));
		patient.setActive(false);
		builder
			.addTransactionUpdateEntry(patient)
			.conditional("Patient?active=false");

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		builder.addTransactionCreateEntry(observation);

		myCaptureQueriesListener.clear();

		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		assertEquals("200 OK", outcome.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", outcome.getEntry().get(1).getResponse().getStatus());
		IdType patientId = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType observationId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		assertEquals("3", patientId.getVersionIdPart());
		assertEquals("1", observationId.getVersionIdPart());

		// Make sure we're not introducing any extra DB operations
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.getValue(), observation.getSubject().getReference());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_Asynchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(IBaseResource.INCLUDE_ALL));
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(Observation.INCLUDE_PATIENT));
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

	}

	@Test
	public void testSearchAndIncludeVersionedReference_Synchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(IBaseResource.INCLUDE_ALL));
			assertEquals(2, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(Observation.INCLUDE_PATIENT));
			assertEquals(2, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
		}

	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenOnlyOneVersionExists() {
		HashSet<String> refPaths = new HashSet<String>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myModelConfig.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition).getId().toUnqualified();

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(Arrays.asList(new Reference(conditionId)));
		IIdType taskId = myTaskDao.create(task).getId().toUnqualified();

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON));
		assertEquals(2, outcome.size());
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertEquals(2, resources.size(), resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", ")));
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());

		// Now, update the Condition to generate another version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		String conditionIdString = myConditionDao.update(condition).getId().getValue();

		// Search for the Task again and make sure that we get the original version of the Condition resource in the Response
		outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON));
		assertEquals(2, outcome.size());
		resources = outcome.getResources(0, 2);
		assertEquals(2, resources.size());
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("1").getValue(), resources.get(1).getIdElement().getValue());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenMultipleVersionsExist() {
		HashSet<String> refPaths = new HashSet<String>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myModelConfig.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition).getId().toUnqualified();

		// Now, update the Condition 3 times to generate a 4th version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		conditionId = myConditionDao.update(condition).getId();
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 1000000));
		conditionId = myConditionDao.update(condition).getId();
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 2000000));
		conditionId = myConditionDao.update(condition).getId();

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(Arrays.asList(new Reference(conditionId)));
		IIdType taskId = myTaskDao.create(task).getId().toUnqualified();

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON));
		assertEquals(2, outcome.size());
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertEquals(2, resources.size(), resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", ")));
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("4").getValue(), resources.get(1).getIdElement().getValue());
	}

	@Test
	public void testSearchAndIncludeVersionedReference_WhenPreviouslyReferencedVersionOne() {
		HashSet<String> refPaths = new HashSet<String>();
		refPaths.add("Task.basedOn");
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(refPaths);
		myModelConfig.setRespectVersionsForSearchIncludes(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);

		// Create a Condition
		Condition condition = new Condition();
		IIdType conditionId = myConditionDao.create(condition).getId().toUnqualified();
		ourLog.info("conditionId: \n{}", conditionId);

		// Create a Task which is basedOn that Condition
		Task task = new Task();
		task.setBasedOn(Arrays.asList(new Reference(conditionId)));
		IIdType taskId = myTaskDao.create(task).getId().toUnqualified();

		// Now, update the Condition 3 times to generate a 4th version of it
		condition.setRecordedDate(new Date(System.currentTimeMillis()));
		conditionId = myConditionDao.update(condition).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 1000000));
		conditionId = myConditionDao.update(condition).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);
		condition.setRecordedDate(new Date(System.currentTimeMillis() + 2000000));
		conditionId = myConditionDao.update(condition).getId();
		ourLog.info("UPDATED conditionId: \n{}", conditionId);

		// Now, update the Task to refer to the latest version 4 of the Condition
		task.setBasedOn(Arrays.asList(new Reference(conditionId)));
		taskId = myTaskDao.update(task).getId();
		ourLog.info("UPDATED taskId: \n{}", taskId);

		// Search for the Task using an _include=Task.basedOn and make sure we get the Condition resource in the Response
		IBundleProvider outcome = myTaskDao.search(SearchParameterMap.newSynchronous().addInclude(Task.INCLUDE_BASED_ON));
		assertEquals(2, outcome.size());
		List<IBaseResource> resources = outcome.getResources(0, 2);
		assertEquals(2, resources.size(), resources.stream().map(t -> t.getIdElement().toUnqualified().getValue()).collect(Collectors.joining(", ")));
		assertEquals(taskId.getValue(), resources.get(0).getIdElement().getValue());
		assertEquals(conditionId.getValue(), ((Task) resources.get(0)).getBasedOn().get(0).getReference());
		assertEquals(conditionId.withVersion("4").getValue(), resources.get(1).getIdElement().getValue());
	}

	@Test
	public void testSearchAndIncludeUnersionedReference_Asynchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myModelConfig.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(IBaseResource.INCLUDE_ALL));
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(new SearchParameterMap().addInclude(Observation.INCLUDE_PATIENT));
			assertEquals(1, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 1);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

	}

	@Test
	public void testSearchAndIncludeUnversionedReference_Synchronous() {
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myModelConfig.setRespectVersionsForSearchIncludes(true);

		// Create the patient
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		myPatientDao.create(p);

		// Update the patient
		p.getIdentifier().get(0).setValue("2");
		IIdType patientId = myPatientDao.update(p).getId().toUnqualified();
		assertEquals("2", patientId.getVersionIdPart());

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId.withVersion("1").getValue());
		IIdType observationId = myObservationDao.create(observation).getId().toUnqualified();

		// Read the observation back
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.toVersionless().getValue(), observation.getSubject().getReference());

		// Search - Non Synchronous for *
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(IBaseResource.INCLUDE_ALL));
			assertEquals(2, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}

		// Search - Non Synchronous for named include
		{
			IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous().addInclude(Observation.INCLUDE_PATIENT));
			assertEquals(2, outcome.sizeOrThrowNpe());
			List<IBaseResource> resources = outcome.getResources(0, 2);
			assertEquals(2, resources.size());
			assertEquals(observationId.getValue(), resources.get(0).getIdElement().getValue());
			assertEquals(patientId.withVersion("2").getValue(), resources.get(1).getIdElement().getValue());
		}
	}

	@Test
	public void testNoNpeOnEoBBundle() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		List<String> strings = Arrays.asList(
			"ExplanationOfBenefit.patient",
			"ExplanationOfBenefit.insurer",
			"ExplanationOfBenefit.provider",
			"ExplanationOfBenefit.careTeam.provider",
			"ExplanationOfBenefit.insurance.coverage",
			"ExplanationOfBenefit.payee.party"
		);
		myModelConfig.setAutoVersionReferenceAtPaths(new HashSet<>(strings));

		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class,
			new InputStreamReader(
				FhirResourceDaoR4VersionedReferenceTest.class.getResourceAsStream("/npe-causing-bundle.json")));

		Bundle transaction = mySystemDao.transaction(new SystemRequestDetails(), bundle);

		assertNotNull(transaction);
	}

	@Test
	public void testAutoVersionPathsWithAutoCreatePlaceholders() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation obs = new Observation();
		obs.setId("Observation/CDE");
		obs.setSubject(new Reference("Patient/ABC"));
		DaoMethodOutcome update = myObservationDao.create(obs);
		Observation resource = (Observation)update.getResource();
		String versionedPatientReference = resource.getSubject().getReference();
		assertThat(versionedPatientReference, is(equalTo("Patient/ABC")));

		Patient p = myPatientDao.read(new IdDt("Patient/ABC"));
		Assertions.assertNotNull(p);

		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		obs = new Observation();
		obs.setId("Observation/DEF");
		obs.setSubject(new Reference("Patient/RED"));
		update = myObservationDao.create(obs);
		resource = (Observation)update.getResource();
		versionedPatientReference = resource.getSubject().getReference();

		assertThat(versionedPatientReference, is(equalTo("Patient/RED/_history/1")));
	}

	@Test
	public void bundleTransaction_withRequestUrlNotRelativePath_doesNotProcess() throws IOException {
		Bundle bundle = loadResourceFromClasspath(Bundle.class, "/transaction-bundles/transaction-with-full-request-url.json");

		try {
			// test
			mySystemDao.transaction(new ServletRequestDetails(),
				bundle);
			fail("We expect invalid full urls to fail");
		} catch (InvalidRequestException ex) {
			Assertions.assertTrue(ex.getMessage().contains("Unable to perform POST, URL provided is invalid:"));
		}
	}

	@Test
	public void bundleTransaction_withRequestURLWithPrecedingSlash_processesAsExpected() throws IOException {
		Bundle bundle = loadResourceFromClasspath(Bundle.class, "/transaction-bundles/transaction-with-preceding-slash-request-url.json");

		// test
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(),
			bundle);

		// verify it was created
		Assertions.assertEquals(1, outcome.getEntry().size());
		IdType idType = new IdType(bundle.getEntry().get(0)
				.getResource().getId());
		// the bundle above contains an observation, so we'll verify it was created here
		Observation obs = myObservationDao.read(idType);
		Assertions.assertNotNull(obs);
	}

	@Test
	@DisplayName("Bundle transaction with AutoVersionReferenceAtPath on and with existing Patient resource should create")
	public void bundleTransaction_autoreferenceAtPathWithPreexistingPatientReference_shouldCreate() {
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		String patientId = "Patient/RED";
		IIdType idType = new IdDt(patientId);

		// create patient ahead of time
		Patient patient = new Patient();
		patient.setId(patientId);
		DaoMethodOutcome outcome = myPatientDao.update(patient);
		assertThat(outcome.getResource().getIdElement().getValue(), is(equalTo(patientId + "/_history/1")));

		Patient returned = myPatientDao.read(idType);
		Assertions.assertNotNull(returned);
		assertThat(returned.getId(), is(equalTo(patientId + "/_history/1")));

		// update to change version
		patient.setActive(true);
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		Reference patientRef = new Reference(patientId);
		obs.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(obs);

		Bundle submitted = (Bundle)builder.getBundle();

		Bundle returnedTr = mySystemDao.transaction(new SystemRequestDetails(), submitted);

		Assertions.assertNotNull(returnedTr);

		// some verification
		Observation obRet = myObservationDao.read(obs.getIdElement());
		Assertions.assertNotNull(obRet);
	}

	@Test
	@DisplayName("GH-2901 Test no NPE is thrown on autoversioned references")
	public void testNoNpeMinimal() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		Reference patientRef = new Reference("Patient/RED");
		obs.setSubject(patientRef);
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionUpdateEntry(obs);

		Bundle submitted = (Bundle)builder.getBundle();

		Bundle returnedTr = mySystemDao.transaction(new SystemRequestDetails(), submitted);

		Assertions.assertNotNull(returnedTr);

		// some verification
		Observation obRet = myObservationDao.read(obs.getIdElement());
		Assertions.assertNotNull(obRet);
		Patient returned = myPatientDao.read(patientRef.getReferenceElement());
		Assertions.assertNotNull(returned);
	}
}
