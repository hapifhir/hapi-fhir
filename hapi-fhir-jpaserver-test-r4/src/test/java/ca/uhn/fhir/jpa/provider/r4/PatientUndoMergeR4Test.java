package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

/**
 * Tests for the $hapi.fhir.undo-merge operation on Patient resources in R4.
 */
public class PatientUndoMergeR4Test extends BaseResourceProviderR4Test {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientUndoMergeR4Test.class);

	ReplaceReferencesTestHelper myTestHelper;

	ReplaceReferencesLargeTestData myLargeTestData;

	Patient mySourcePatientBeforeMerge;
	Patient myTargetPatientBeforeMerge;
	Encounter myEncounterInitiallyReferencingSrcBeforeMerge;

	IIdType mySourcePatientId;
	IIdType myTargetPatientId;
	IIdType myEncounterIdInitiallyReferencingSrc;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setDefaultTransactionEntriesForWrite(new JpaStorageSettings().getDefaultTransactionEntriesForWrite());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setAllowMultipleDelete(true);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		// we need to keep the version on Provenance.target fields to
		// verify that Provenance resources were saved with versioned target references
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myLargeTestData = new ReplaceReferencesLargeTestData(myDaoRegistry);
	}




	@ParameterizedTest
	@CsvSource(
		value = {
			"true,true,true",
			"false,true,true",
			"true,false,true",
			"false,false,true",
			"true,true,false",
			"false,true,false",
			"true,false,false",
			"false,false,false"
		})
	void testUndoMerge(boolean theDeleteSource, boolean theUseIdentifiersAsSrcInput, boolean theUseIdentifiersAsTargetInput){
		// setup
		myLargeTestData.createTestResources();

		Patient sourceBeforeMerge = myTestHelper.readPatient(myLargeTestData.getSourcePatientId());
		Patient targetBeforeMerge = myTestHelper.readPatient(myLargeTestData.getTargetPatientId());

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		if (theUseIdentifiersAsSrcInput) {
			inParams.sourcePatientIdentifiers = myLargeTestData.getSourcePatientIdentifiers();
		}
		else {
			inParams.sourcePatient = myTestHelper.idAsReference(myLargeTestData.getSourcePatientId());
		}
		if (theUseIdentifiersAsTargetInput) {
			inParams.targetPatientIdentifiers = myLargeTestData.getTargetPatientIdentifiers();
		}
		else {
			inParams.targetPatient = myTestHelper.idAsReference(myLargeTestData.getTargetPatientId());
		}
		if (theDeleteSource) {
			inParams.deleteSource = true;
		}

		Parameters inParametersMerge = inParams.asParametersResource();

		// exec, merge first and then undo it
		 myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		 Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		 Parameters outParams = myTestHelper.callUndoMergeOperation(myClient, inParametersUndoMerge);
		 // + 2 is for source and target resources
		 validateSuccessOutcome(outParams, TOTAL_EXPECTED_PATCHES + 2);


		Patient sourceAfterUnmerge = myTestHelper.readPatient(myLargeTestData.getSourcePatientId());
		Patient targetAfterUnmerge = myTestHelper.readPatient(myLargeTestData.getTargetPatientId());
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceBeforeMerge, sourceAfterUnmerge);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetBeforeMerge, targetAfterUnmerge);

		myTestHelper.assertReferencesHaveNotChanged(myLargeTestData);
	}


	@Test
	void testUndoMerge_TargetResourceIsNotUpdatedByMerge_SkipsRestoringTargetResource() {
		// During merge, if deleteSource was true, no resultResource was provided and the src resource didn't contain any identifiers to copy to the target,
		// the target resource wouldn't be updated by the merge. In that scenario the target resource
		// should not be restored to a previous version by undo merge. This test verifies that.

		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);
		inParams.deleteSource = true;

		Parameters inParametersMerge = inParams.asParametersResource();
		// merge and then undo it
		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		Patient targetAfterMerge = myTestHelper.readPatient(myTargetPatientId);
		assertThat(targetAfterMerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(1);

		Parameters inParametersUndoMerge = inParams.asUndoParametersResource();

		Parameters outParams = myTestHelper.callUndoMergeOperation(myClient, inParametersUndoMerge);
		// in this case, only the source and the encounter should be restored, not the target, so the expected count is 2
		validateSuccessOutcome(outParams,  2);

		Patient sourceAfterUnmerge = myTestHelper.readPatient(mySourcePatientId);
		Patient targetAfterUnmerge = myTestHelper.readPatient(myTargetPatientId);
		Encounter encounterAfterUnmerge = myEncounterDao.read(myEncounterIdInitiallyReferencingSrc, mySrd);

		// target should still be at version 1
		assertThat(targetAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(1);

		// src and the referencing resource should be restored, becoming version 3
		assertThat(sourceAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(3);
		assertThat(encounterAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(3);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(mySourcePatientBeforeMerge, sourceAfterUnmerge);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(myEncounterInitiallyReferencingSrcBeforeMerge, encounterAfterUnmerge);
	}

	@Test
	void testUndoMerge_SrcResourceUpdatedAfterMerge_UndoFailsWithConflict() {
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		Parameters inParametersMerge = inParams.asParametersResource();

		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		// update the source resource after the merge
		Patient updatedSrcPatient = new Patient().setActive(true);
		updatedSrcPatient.setId(mySourcePatientId);
		myPatientDao.update(updatedSrcPatient, mySrd);

		Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, 409, "HAPI-2732");
	}


	@Test
	void testUndoMerge_TargetUpdatedAfterMerge_UndoFailsWithConflict() {
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		Parameters inParametersMerge = inParams.asParametersResource();

		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		Patient updatedTargetPatient = new Patient().setActive(true);
		updatedTargetPatient.setId(myTargetPatientId);
		myPatientDao.update(updatedTargetPatient, mySrd);


		Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, 409,"HAPI-2732");
	}

	@Test
	void testUndoMerge_ReferencingResourceUpdatedAfterMerge_UndoFailsWithConflict() {
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		Parameters inParametersMerge = inParams.asParametersResource();

		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		Encounter updatedEncounter = new Encounter();
		updatedEncounter.setId(myEncounterIdInitiallyReferencingSrc);
		updatedEncounter.addIdentifier().setSystem("sys").setValue("val");
		myEncounterDao.update(updatedEncounter, mySrd);

		Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, 409,"HAPI-2732");
	}

	@Test
	void testUndoMerge_ReferencingResourceDeletedAfterMerge_UndoFailsWithGone() {
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		Parameters inParametersMerge = inParams.asParametersResource();

		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		myEncounterDao.delete(myEncounterIdInitiallyReferencingSrc, mySrd);

		Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParametersUndoMerge, ResourceGoneException.class, 410,"HAPI-2751");
	}


	@Test
	public void testUndoMerge_ResourceLimitExceeded() {
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		Parameters inParametersMerge = inParams.asParametersResource();

		myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		JpaStorageSettings storageSettings = myStorageSettings;
		int originalLimit = storageSettings.getInternalSynchronousSearchSize();
		storageSettings.setInternalSynchronousSearchSize(2);

		try {
			Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
			String expectedMessage = "HAPI-2748: Number of references to update (3) exceeds the limit (2)";
			callUndoAndAssertExceptionWithMessagesInTheOutcome(inParametersUndoMerge, InvalidRequestException.class, 400, expectedMessage);
		} finally {
			// Restore the original limit
			storageSettings.setInternalSynchronousSearchSize(originalLimit);
		}
	}

	@Test
	public void testUndoMerge_NoProvenanceExistsForMerge_Fails() {
		createInputPatients();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(myTargetPatientId));
		provenance.addTarget(new Reference(mySourcePatientId));

		// create provenance but with a different kind of activity, not merge
		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("some-other-activity")));
		myProvenanceDao.create(provenance, mySrd);

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);


		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParams.asUndoParametersResource(),
			ResourceNotFoundException.class,
			404,
			"HAPI-2747");
	}


	@Test
	public void testUndoMerge_ProvenanceExistsButMissingContainedResourceForInputParams_Fails() {
		createInputPatients();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(myTargetPatientId.withVersion("1")));
		provenance.addTarget(new Reference(mySourcePatientId.withVersion("1")));

		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("merge")));
		myProvenanceDao.create(provenance, mySrd);

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParams.asUndoParametersResource(),
			InternalErrorException.class,
			500,
			"HAPI-2749");
	}


	@Test
	public void testUndoMerge_ProvenanceExistsButMissingContainedResourceTargetUpdateOutput_Fails() {
		createInputPatients();


		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(myTargetPatientId.withVersion("1")));
		provenance.addTarget(new Reference(mySourcePatientId.withVersion("1")));

		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("merge")));
		Parameters parameters = new Parameters();
		provenance.addContained(parameters);
		myProvenanceDao.create(provenance, mySrd);

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);

		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParams.asUndoParametersResource(),
			InternalErrorException.class,
			500,
			"HAPI-2750");
	}


	private void createInputPatientsAndEncounter() {
		createInputPatients();

		Encounter encounter1 = new Encounter();
		encounter1.setSubject(new Reference(mySourcePatientId));
		DaoMethodOutcome outcome =  myEncounterDao.create(encounter1, mySrd);
		myEncounterInitiallyReferencingSrcBeforeMerge = (Encounter) outcome.getResource();
		myEncounterIdInitiallyReferencingSrc = outcome.getId().toUnqualifiedVersionless();
	}

	private void createInputPatients() {
		DaoMethodOutcome outcome = myPatientDao.create(new Patient(), mySrd);
		mySourcePatientBeforeMerge = (Patient) outcome.getResource();
		mySourcePatientId = outcome.getId().toUnqualifiedVersionless();

		outcome = myPatientDao.create(new Patient(), mySrd);
		myTargetPatientBeforeMerge = (Patient) outcome.getResource();
		myTargetPatientId = outcome.getId().toUnqualifiedVersionless();
	}

	private void assertResourcesAreEqualIgnoringVersionAndLastUpdated(Resource theBefore, Resource theAfter) {

		// the resources should have the same versionless id
		assertThat(theBefore.getIdElement().toVersionless()).isEqualTo(theAfter.getIdElement().toVersionless());

		//create a copy of the before since we will modify some of its meta data to match the after resource
		Resource copyOfTheBefore = theBefore.copy();

		copyOfTheBefore.getMeta().setLastUpdated(theAfter.getMeta().getLastUpdated());
		copyOfTheBefore.getMeta().setVersionId(theAfter.getMeta().getVersionId());
		copyOfTheBefore.getMeta().setSource(theAfter.getMeta().getSource());
		copyOfTheBefore.setId(theAfter.getIdElement());

		String before = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(copyOfTheBefore);
		String after = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theAfter);
		assertThat(after).isEqualTo(before);
		// compare using the equalsDeep method as well, just to be sure
		assertThat(theAfter.equalsDeep(copyOfTheBefore)).isTrue();
	}

	@Test
	void testUndoMerge_MissingRequiredParameters_400BadRequest() {
		Parameters params = new Parameters();
		callUndoAndAssertExceptionWithMessagesInTheOutcome(params,
			InvalidRequestException.class,
			400,
			List.of("There are no source resource parameters provided",
				"There are no target resource parameters provided")
		);
	}

	@Test
	void testUndoMerge_NonParameterRequestBody_Returns400BadRequest() throws IOException {
		HttpClientExtension clientExtension = new HttpClientExtension();
		clientExtension.initialize();
		try (CloseableHttpClient client = clientExtension.getClient()) {
			HttpPost post = new HttpPost(myServer.getBaseUrl() + "/Patient/$hapi.fhir.undo-merge");
			post.addHeader("Content-Type", "application/fhir+json");
			post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(new Patient()), StandardCharsets.UTF_8));
			try (CloseableHttpResponse response = client.execute(post)) {
				assertThat(response.getStatusLine().getStatusCode()).isEqualTo(400);
				String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				assertThat(responseContent).contains("There are no source resource parameters provided");
				assertThat(responseContent).contains("There are no target resource parameters provided");
			}
		}
	}



	@Test
	void testUndoMerge_BothSrcParametersProvided_400BadRequest() {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = new Reference("Patient/123");
		inParams.sourcePatientIdentifiers = List.of(new Identifier().setSystem("sys").setValue("val"));
		inParams.targetPatient = new Reference("Patient/456");
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParams.asUndoParametersResource(),
			InvalidRequestException.class,
			400,
			"Source resource must be provided either by 'source-patient' or by 'source-patient-identifier'"
		);
	}

	@Test
	void testUndoMerge_BothTargetParametersProvided_400BadRequest() {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = new Reference("Patient/123");
		inParams.targetPatient = new Reference("Patient/456");
		inParams.targetPatientIdentifiers = List.of(new Identifier().setSystem("sys").setValue("val"));
		callUndoAndAssertExceptionWithMessagesInTheOutcome(inParams.asUndoParametersResource(),
			InvalidRequestException.class,
			400,
			"Target resource must be provided either by 'target-patient' or by 'target-patient-identifier'"
		);
	}

	private void callUndoAndAssertExceptionWithMessagesInTheOutcome(Parameters theInParameters, Class<? extends  BaseServerResponseException> theExceptionClass, int theExpectedStatusCode, String theExpectedMessage) {
		callUndoAndAssertExceptionWithMessagesInTheOutcome(theInParameters, theExceptionClass, theExpectedStatusCode, List.of(theExpectedMessage));
	}

	private void callUndoAndAssertExceptionWithMessagesInTheOutcome(Parameters theInParameters, Class<? extends  BaseServerResponseException> theExceptionClass, int theExpectedStatusCode, List<String> theExpectedMessages) {
		 Exception ex  = catchException(() -> myTestHelper.callUndoMergeOperation(myClient, theInParameters));
		 assertThat(ex).isInstanceOf(theExceptionClass);
		 BaseServerResponseException baseEx = (BaseServerResponseException) ex;
		 assertThat(baseEx.getStatusCode()).isEqualTo(theExpectedStatusCode);
		 String messages = myTestHelper.extractFailureMessageFromOutcomeParameter(baseEx);
		 theExpectedMessages.forEach(m -> assertThat(messages).contains(m));
	}

	private void validateSuccessOutcome(Parameters theOutParams, int theExpectedResourceCount) {
		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
				String detailsTxt = issue.getDetails().getText();
				assertThat(detailsTxt).matches(format("Successfully restored %d resources to their previous versions based on the Provenance resource: Provenance/[0-9]+/_history/1", theExpectedResourceCount));
			});
	}

	@Override
	protected boolean verboseClientLogging() {
		return true;
	}

}
