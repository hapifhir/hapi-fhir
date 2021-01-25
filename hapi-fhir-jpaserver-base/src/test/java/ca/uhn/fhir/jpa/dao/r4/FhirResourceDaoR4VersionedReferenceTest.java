package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoR4VersionedReferenceTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4VersionedReferenceTest.class);

	@AfterEach
	public void afterEach() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(true);
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

		// Update - put an unversioned reference in the subject
		observation = new Observation();
		observation.setId(observationId);
		observation.addIdentifier().setSystem("http://foo").setValue("bar");
		observation.getSubject().setReference(patientId.toVersionless().getValue());
		myObservationDao.update(observation);

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
		builder.addCreateEntry(patient);

		Encounter encounter = new Encounter();
		encounter.setId(IdType.newRandomUuid());
		encounter.addIdentifier().setSystem("http://baz");
		builder.addCreateEntry(encounter);

		Observation observation = new Observation();
		observation.getSubject().setReference(patient.getId());
		observation.getEncounter().setReference(encounter.getId());
		builder.addCreateEntry(observation);

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

		// TODO: update observation in another transaction

		// Read back and verify that reference is now versioned
		observation = myObservationDao.read(observationId);
		assertEquals(patientId.getValue(), observation.getSubject().getReference());
		assertEquals(encounterId.toVersionless().getValue(), observation.getEncounter().getReference());
	}

}
