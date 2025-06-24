package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UndoReplaceReferencesR4Test extends BaseResourceProviderR4Test {
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		// we need to keep the version on Provenance.target fields to
		// verify that Provenance resources were saved with versioned target references
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
	}



	@Test
	public void testUndoReplaceReferences_OneReferencingResource() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();

		Encounter encounter = new Encounter();
		encounter.getSubject().setReference(sourcePatientId.getValue());
		IIdType encounterId = myEncounterDao.create(encounter, srd).getId().toUnqualifiedVersionless();

		myTestHelper.callReplaceReferences(myClient,
			sourcePatientId.toString(),
			targetPatientId.toString(),
			false);

		callUndoReplaceReferences(myClient,
			sourcePatientId.toString(),
			targetPatientId.toString());

		// --- Validation: Read Encounter and check subject and version ---
		Encounter updatedEncounter = myEncounterDao.read(encounterId, srd);
		// the subject reference should be reverted to the source patient
		assertThat(updatedEncounter.getSubject().getReference()).isEqualTo(sourcePatientId.getValue());
		assertThat(updatedEncounter.getIdElement().getVersionIdPart()).isEqualTo("3");
	}



	@Test
	public void testUndoReplaceReferences() {
		myTestHelper.beforeEach();

		IIdType sourcePatientId = myTestHelper.getSourcePatientId();
		IIdType targetPatientId = myTestHelper.getTargetPatientId();

		myTestHelper.callReplaceReferences(myClient,
			sourcePatientId.toString(),
			targetPatientId.toString(),
			false);

		callUndoReplaceReferences(myClient,
			sourcePatientId.toString(),
			targetPatientId.toString());

		myTestHelper.assertNothingChanged();


		// Second undo is called a second time it should fail because resources are already reverted
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient,
			sourcePatientId.toString(),
			targetPatientId.toString()))
			.isInstanceOf(ResourceVersionConflictException.class)
			.hasMessageContaining("HAPI-0111")
			.hasMessageContaining("does not match Provenance version");

		myTestHelper.assertNothingChanged();
	}


	public Parameters callUndoReplaceReferences(
		IGenericClient theFhirClient,
		String theSourceId,
		String theTargetId) {
		IOperationUntypedWithInputAndPartialOutput<Parameters> request = theFhirClient
			.operation()
			.onServer()
			.named("$hapi.fhir.undo-replace-references")
			.withParameter(
				Parameters.class,
				"source-reference-id",
				new StringType(theSourceId))
			.andParameter(
				"target-reference-id",
				new StringType(theTargetId));

		return request.returnResourceType(Parameters.class).execute();
	}

	@Test
	public void testUndoReplaceReferences_ResourceLimitExceeded() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();

		// Create multiple Encounter resources referencing the source patient
		int encounterCount = 3;
		for (int i = 0; i < encounterCount; i++) {
			Encounter encounter = new Encounter();
			encounter.getSubject().setReference(sourcePatientId.getValue());
			myEncounterDao.create(encounter, srd);
		}

		// Set the resource limit to a small number to trigger the exception
		JpaStorageSettings storageSettings = myStorageSettings;
		int originalLimit = storageSettings.getInternalSynchronousSearchSize();
		storageSettings.setInternalSynchronousSearchSize(2);

		try {
			myTestHelper.callReplaceReferences(myClient,
				sourcePatientId.toString(),
				targetPatientId.toString(),
				false);

			String sourceIdString = sourcePatientId.toString();
			String targetIdString = targetPatientId.toString();

			assertThatThrownBy(() -> callUndoReplaceReferences(myClient,
					sourceIdString,
					targetIdString))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-0111")
				.hasMessageContaining("Number of references to update (3) exceeds the limit (2)");
		} finally {
			// Restore the original limit
			storageSettings.setInternalSynchronousSearchSize(originalLimit);
		}
	}

	@Test
	public void testUndoReplaceReferences_WithoutReplaceReferencesProvenance_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();


		String versionlessSourceId = sourcePatientId.toVersionless().toString();
		String versionlessTargetId = targetPatientId.toVersionless().toString();
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient,
				versionlessSourceId,
				versionlessTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
	}


	@Test
	public void testUndoReplaceReferences_ProvenanceExistsButActivityIsIncorrect_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(targetPatientId));
		provenance.addTarget(new Reference(sourcePatientId));


		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("some-other-activity")));
		myProvenanceDao.create(provenance, srd);

		String versionlessSourceId = sourcePatientId.toVersionless().toString();
		String versionlessTargetId = targetPatientId.toVersionless().toString();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient,
			versionlessSourceId,
			versionlessTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-0123: Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
	}


	@Test
	public void testUndoReplaceReferences_ProvenanceExistsButSourceAndTargetOrderIsIncorrect_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(sourcePatientId));
		provenance.addTarget(new Reference(targetPatientId));


		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("link")));
		myProvenanceDao.create(provenance, srd);

		String versionlessSourceId = sourcePatientId.toVersionless().toString();
		String versionlessTargetId = targetPatientId.toVersionless().toString();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient,
			versionlessSourceId,
			versionlessTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-0123: Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
	}


}
