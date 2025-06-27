package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UndoReplaceReferencesR4Test extends BaseResourceProviderR4Test {
	ReplaceReferencesTestHelper myTestHelper;
	ReplaceReferencesLargeTestData myLargeTestData;
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
		myLargeTestData = new ReplaceReferencesLargeTestData(myDaoRegistry);
	}



	@Test
	public void testUndoReplaceReferences_OneReferencingResource_Success() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();

		Encounter encounter = new Encounter();
		encounter.getSubject().setReference(sourcePatientId.getValue());
		IIdType encounterId = myEncounterDao.create(encounter, srd).getId().toUnqualifiedVersionless();

		myTestHelper.callReplaceReferences(myClient, sourcePatientId, targetPatientId, false);

		Parameters outputParams = callUndoReplaceReferences(myClient, sourcePatientId, targetPatientId);

		validateOutputParameters(outputParams, 1);

		// --- Validation: Read Encounter and check subject and version ---
		Encounter updatedEncounter = myEncounterDao.read(encounterId, srd);
		// the subject reference should be reverted to the source patient
		assertThat(updatedEncounter.getSubject().getReference()).isEqualTo(sourcePatientId.getValue());
		assertThat(updatedEncounter.getIdElement().getVersionIdPart()).isEqualTo("3");
	}

	@Test
	public void testUndoReplaceReferences_Success() {
		myLargeTestData.createTestResources();

		IIdType sourcePatientId = myLargeTestData.getSourcePatientId();
		IIdType targetPatientId = myLargeTestData.getTargetPatientId();

		myTestHelper.callReplaceReferences(myClient,
			sourcePatientId,
			targetPatientId,
			false);

		Parameters outputParams = callUndoReplaceReferences(myClient,
			sourcePatientId,
			targetPatientId);

		validateOutputParameters(outputParams, TOTAL_EXPECTED_PATCHES);

		myTestHelper.assertReferencesHaveNotChanged(myLargeTestData);

		// Second undo is called a second time it should fail because resources are already reverted
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, sourcePatientId, targetPatientId))
			.isInstanceOf(ResourceVersionConflictException.class)
			.hasMessageContaining("HAPI-2732")
			.hasMessageContaining("does not match the expected version");

		myTestHelper.assertReferencesHaveNotChanged(myLargeTestData);
	}

	@Test
	public void testUndoReplaceReferences_ReferencingResourceGotDeletedAfterReplaceReferences_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();

		Encounter encounter1 = new Encounter();
		encounter1.getSubject().setReference(sourcePatientId.getValue());
		IIdType encounterId1 = myEncounterDao.create(encounter1, srd).getId().toUnqualifiedVersionless();
		Encounter encounter2 = new Encounter();
		encounter2.getSubject().setReference(sourcePatientId.getValue());
		IIdType encounterId2 = myEncounterDao.create(encounter2, srd).getId().toUnqualifiedVersionless();

		myTestHelper.callReplaceReferences(myClient, sourcePatientId, targetPatientId, false);

		myEncounterDao.delete(encounterId2, srd);

		assertThatThrownBy( () -> callUndoReplaceReferences(myClient, sourcePatientId, targetPatientId))
			.isInstanceOf(ResourceGoneException.class);


		// the existing Encounter should not be changed because of failed undo, because the undo operation is transactional
		Encounter existingEncounter = myEncounterDao.read(encounterId1, srd);
		// the subject reference should be to the target patient
		assertThat(existingEncounter.getSubject().getReference()).isEqualTo(targetPatientId.getValue());
		assertThat(existingEncounter.getIdElement().getVersionIdPart()).isEqualTo("2");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testUndoReplaceReferences_NotExistingSourceId_ThrowsNotFoundException(boolean theIsAsync) {
		String nonExistingSourceId = "Patient/does-not-exist";
		String targetId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless().toString();
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, nonExistingSourceId, targetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2001: Resource Patient/does-not-exist is not known");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testUndoReplaceReferences_NotExistingTargetId_ThrowsNotFoundException(boolean theIsAsync) {
		String nonExistingTargetId = "Patient/does-not-exist";
		String srcId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless().toString();
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, srcId, nonExistingTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2001: Resource Patient/does-not-exist is not known");
	}

	@Test
	public void testUndoReplaceReferences_ResourceLimitExceeded() {
		myLargeTestData.createTestResources();

		// Set the resource limit to a small number to trigger the exception
		JpaStorageSettings storageSettings = myStorageSettings;
		int originalLimit = storageSettings.getInternalSynchronousSearchSize();
		storageSettings.setInternalSynchronousSearchSize(5);

		try {
			IIdType srcId = myLargeTestData.getSourcePatientId();
			IIdType tgtId = myLargeTestData.getTargetPatientId();
			myTestHelper.callReplaceReferences(myClient, srcId, tgtId, false);

			assertThatThrownBy(() -> callUndoReplaceReferences(myClient, srcId, tgtId))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2729")
				.hasMessageContaining("Number of references to update (" + TOTAL_EXPECTED_PATCHES + ") exceeds the limit (5)");
		} finally {
			// Restore the original limit
			storageSettings.setInternalSynchronousSearchSize(originalLimit);
		}
	}

	@Test
	public void testUndoReplaceReferences_WithoutReplaceReferencesProvenance_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualifiedVersionless();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, sourcePatientId, targetPatientId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2728: Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
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

		IIdType versionlessSourceId = sourcePatientId.toVersionless();
		IIdType versionlessTargetId = targetPatientId.toVersionless();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, versionlessSourceId, versionlessTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2728: Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
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

		IIdType versionlessSourceId = sourcePatientId.toVersionless();
		IIdType versionlessTargetId = targetPatientId.toVersionless();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, versionlessSourceId, versionlessTargetId))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2728: Unable to find a Provenance created by a $hapi.fhir.replace-references for the provided source and target IDs");
	}


	@Test
	public void testUndoReplaceReferences_ProvenanceContainingAVersionlessTargetReference_Fails() {
		SystemRequestDetails srd = new SystemRequestDetails();
		IIdType sourcePatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();
		IIdType targetPatientId = myPatientDao.create(new Patient(), srd).getId().toUnqualified();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(targetPatientId));
		provenance.addTarget(new Reference(sourcePatientId));
		IIdType encounterId = myEncounterDao.create(new Encounter(), srd).getId().toUnqualified();

		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("link")));

		// add a target reference with no version
		provenance.addTarget(new Reference(encounterId.toVersionless().toString()));

		myProvenanceDao.create(provenance, srd);

		IIdType versionlessSourceId = sourcePatientId.toVersionless();
		IIdType versionlessTargetId = targetPatientId.toVersionless();

		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, versionlessSourceId, versionlessTargetId))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("HAPI-2730: Reference does not have a version: " + encounterId.toVersionless().toString());
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testUndoReplaceReferences_MissingSourceId_ThrowsInvalidRequestException(String theSourceId) {
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, theSourceId, "target-id"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2583: Parameter 'source-reference-id' is blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testUndoReplaceReferences_MissingTargetId_ThrowsInvalidRequestException(String theTargetId) {
		assertThatThrownBy(() -> callUndoReplaceReferences(myClient, "source-id", theTargetId))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-2584: Parameter 'target-reference-id' is blank");
	}


	private void validateOutputParameters(Parameters theOutputParams, int theNumberOfExpectedUpdates) {
		assertThat(theOutputParams).isNotNull();
		assertThat(theOutputParams.getParameter()).hasSize(1);
		OperationOutcome outcome = (OperationOutcome) theOutputParams.getParameter("outcome").getResource();
		String expectedMsgPattern = String.format("Successfully restored %d resources to their previous versions based on the Provenance resource: Provenance/[0-9]+/_history/1", theNumberOfExpectedUpdates) ;
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
				assertThat(issue.getDiagnostics()).matches(expectedMsgPattern);
			});

	}

	public Parameters callUndoReplaceReferences(
		IGenericClient theFhirClient,
		IIdType theSourceId,
		IIdType theTargetId) {
		return callUndoReplaceReferences(theFhirClient, theSourceId.toString(), theTargetId.toString());
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

}
