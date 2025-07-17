package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

		// exec
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
		// if deleteSource is true, no resultResource is provided and the src resource doesn't contain any identifiers to copy to the target,
		// the target resource won't be updated by the merge operation, in that scenario the target resource
		// should not be restored to a previous version by undo merge
		createInputPatientsAndEncounter();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = myTestHelper.idAsReference(mySourcePatientId);
		inParams.targetPatient = myTestHelper.idAsReference(myTargetPatientId);
		inParams.deleteSource = true;

		Parameters inParametersMerge = inParams.asParametersResource();

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
		callUndoAndAssertExceptionWithMessageInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, "HAPI-2732");
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
		callUndoAndAssertExceptionWithMessageInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, "HAPI-2732");
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
		callUndoAndAssertExceptionWithMessageInTheOutcome(inParametersUndoMerge, ResourceVersionConflictException.class, "HAPI-2732");
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
		//fix msg.code
		callUndoAndAssertExceptionWithMessageInTheOutcome(inParametersUndoMerge, ResourceGoneException.class, "HAPI-1234");
	}


	@Test
	public void testUndoReplaceReferences_ResourceLimitExceeded() {
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
			String expectedMessage = "HAPI-1234: Number of references to update (3) exceeds the limit (2)";
			callUndoAndAssertExceptionWithMessageInTheOutcome(inParametersUndoMerge, InvalidRequestException.class, expectedMessage);
		} finally {
			// Restore the original limit
			storageSettings.setInternalSynchronousSearchSize(originalLimit);
		}
	}


	private void createInputPatientsAndEncounter() {
		DaoMethodOutcome outcome = myPatientDao.create(new Patient(), mySrd);
		mySourcePatientBeforeMerge = (Patient) outcome.getResource();
		mySourcePatientId = outcome.getId().toUnqualifiedVersionless();


		outcome = myPatientDao.create(new Patient(), mySrd);
		myTargetPatientBeforeMerge = (Patient) outcome.getResource();
		myTargetPatientId = outcome.getId().toUnqualifiedVersionless();

		Encounter encounter1 = new Encounter();
		encounter1.setSubject(new Reference(mySourcePatientId));
		outcome =  myEncounterDao.create(encounter1, mySrd);
		myEncounterInitiallyReferencingSrcBeforeMerge = (Encounter) outcome.getResource();
		myEncounterIdInitiallyReferencingSrc = outcome.getId().toUnqualifiedVersionless();
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
	void test_MissingRequiredParameters_Returns400BadRequest() {
		Parameters params = new Parameters();
		assertThatThrownBy(() -> myTestHelper.callUndoMergeOperation(myClient, params))
			.isInstanceOf(InvalidRequestException.class)
			.extracting(InvalidRequestException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(400);
	}

	private void callUndoAndAssertExceptionWithMessageInTheOutcome(Parameters inParameters, Class<? extends  BaseServerResponseException> theExceptionClass, String theExpectedMessage) {
		assertThatThrownBy(() -> myTestHelper.callUndoMergeOperation(myClient, inParameters))
			.isInstanceOf(theExceptionClass)
			.extracting(theExceptionClass::cast)
			.extracting(myTestHelper::extractFailureMessageFromOutcomeParameter)
			.asString()
			.contains(theExpectedMessage);
	}



	@Override
	protected boolean verboseClientLogging() {
		return true;
	}

}
