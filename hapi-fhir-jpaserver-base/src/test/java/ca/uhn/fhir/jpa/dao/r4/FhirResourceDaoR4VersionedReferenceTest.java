package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4VersionedReferenceTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4VersionedReferenceTest.class);

	@AfterEach
	public void afterEach() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(true);
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
		myModelConfig.setRespectVersionsForSearchIncludes(new ModelConfig().isRespectVersionsForSearchIncludes());
		myModelConfig.setAutoVersionReferenceAtPaths(new ModelConfig().getAutoVersionReferenceAtPaths());
	}

	@Test
	public void testStoreAndRetrieveVersionedReference() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);

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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);

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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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
		assertEquals(5, myCaptureQueriesListener.logSelectQueries().size());

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientIdString, observation.getSubject().getReference());
	}

	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_SourceAndTargetBothCreated() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");


		BundleBuilder builder = new BundleBuilder(myFhirCtx);

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
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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

		BundleBuilder builder = new BundleBuilder(myFhirCtx);

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
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());

	}


	@Test
	public void testInsertVersionedReferenceAtPath_InTransaction_TargetUpdate() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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

		BundleBuilder builder = new BundleBuilder(myFhirCtx);

		Patient patient = new Patient();
		patient.setId("Patient/PATIENT");
		patient.setActive(true);
		builder.addTransactionUpdateEntry(patient);

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		builder.addTransactionCreateEntry(observation);

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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

		BundleBuilder builder = new BundleBuilder(myFhirCtx);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		builder
			.addTransactionUpdateEntry(patient)
			.conditional("Patient?active=false");

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId()); // versioned
		builder.addTransactionCreateEntry(observation);

		myCaptureQueriesListener.clear();

		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) builder.getBundle());
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(false);
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
	public void testSearchAndIncludeUnersionedReference_Asynchronous() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(true);
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
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(true);
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

}
