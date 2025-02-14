package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesR4Test extends BaseResourceProviderR4Test {
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSettings.setDefaultTransactionEntriesForWrite(new JpaStorageSettings().getDefaultTransactionEntriesForWrite());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		// keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references
		myFhirContext.getParserOptions()
			.setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myTestHelper.beforeEach();
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences(boolean isAsync) {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(myClient, isAsync);

		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		if (isAsync) {
			assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_ACCEPTED);

			Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());

			JobInstance jobInstance = awaitJobCompletion(task);

			patchResultBundle = myTestHelper.validateCompletedTask(jobInstance, task.getIdElement());
		} else {
			patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();
		}

		// validate
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle,
			ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES, List.of(
				"Observation", "Encounter", "CarePlan"));

		// Check that the linked resources were updated

		myTestHelper.assertAllReferencesUpdated();
		myTestHelper.assertReplaceReferencesProvenance();
	}

	private JobInstance awaitJobCompletion(Task task) {
		String jobId = myTestHelper.getJobIdFromTask(task);
		return myBatch2JobHelper.awaitJobCompletion(jobId);
	}

	@Test
	void testReplaceReferencesSmallResourceLimitSync() {
		assertThatThrownBy(() -> myTestHelper.callReplaceReferencesWithResourceLimit(myClient, false, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessage("HTTP 412 Precondition Failed: HAPI-2597: Number of resources with references to " + myTestHelper.getSourcePatientId() + " exceeds the resource-limit 5. Submit the request asynchronsly by adding the HTTP Header 'Prefer: respond-async'.");
	}

	@Test
	void testReplaceReferencesSmallTransactionEntriesSize() {
		myStorageSettings.setDefaultTransactionEntriesForWrite(5);

		// exec
		Parameters outParams = myTestHelper.callReplaceReferencesWithResourceLimit(myClient, true, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);

		assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_ACCEPTED);

		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart());
		ourLog.info("Got task {}", task.getId());

		awaitJobCompletion(task);

		Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
		ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

		assertThat(taskWithOutput.getOutput()).as("task " + task.getId() + " has size " + EXPECTED_SMALL_BATCHES).hasSize(EXPECTED_SMALL_BATCHES);
		List<Resource> containedResources = taskWithOutput.getContained();

		assertThat(containedResources)
			.hasSize(EXPECTED_SMALL_BATCHES)
			.element(0)
			.isInstanceOf(Bundle.class);

		// for each batch, there should be a corresponding patch result bundle in the task as a contained resource.
		// The loop below validates the patch result bundles, and counts total number of bundle entries seen
		// to ensure that all patch results are accounted for.
		int totalPatchResultEntriesSeen = 0;
		for (int i = 0; i < EXPECTED_SMALL_BATCHES; i++) {

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutput().get(i);

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
			assertEquals("Bundle", taskType.getCode());

			Bundle containedBundle = (Bundle) containedResources.get(i);

			Reference outputRef = (Reference) taskOutput.getValue();
			patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));

			int numberOfEntriesInCurrentBundle = patchResultBundle.getEntry().size();
			// validate
			totalPatchResultEntriesSeen += numberOfEntriesInCurrentBundle;
			assertThat(numberOfEntriesInCurrentBundle).isBetween(1, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);
			ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, numberOfEntriesInCurrentBundle, List.of(
				"Observation",
				"Encounter", "CarePlan"));
		}

		assertThat(totalPatchResultEntriesSeen).isEqualTo(TOTAL_EXPECTED_PATCHES);

		// Check that the linked resources were updated

		myTestHelper.assertAllReferencesUpdated();
		myTestHelper.assertReplaceReferencesProvenance();
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testReplaceReferences_MissingSourceId_ThrowsInvalidRequestException(String theSourceId) {
		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myTestHelper.callReplaceReferencesWithResourceLimit(myClient, theSourceId, "target-id", false, null);
		});
		assertThat(exception.getMessage()).contains("HAPI-2583: Parameter 'source-reference-id' is blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testReplaceReferences_MissingTargetId_ThrowsInvalidRequestException(String theTargetId) {
		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myTestHelper.callReplaceReferencesWithResourceLimit(myClient, "source-id", theTargetId, false, null);
		});
		assertThat(exception.getMessage()).contains("HAPI-2584: Parameter 'target-reference-id' is blank");
	}


	@Test
	void testReplaceReferences_WhenReplacingAHighCardinalityReferenceElement_OnlyReplacesMatchingReferences() {
		//This test uses an Observation resource with multiple Practitioner references in the 'performer' element.
		// Create Practitioners
		IIdType practitionerId1 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualifiedVersionless();
		IIdType practitionerId2 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualifiedVersionless();
		IIdType practitionerId3 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualifiedVersionless();

		// Create observation with references in the performer field
		IIdType observationId = createObservationWithPerformers(practitionerId1, practitionerId2).toUnqualifiedVersionless();

		// Call $replace-references operation to replace practitionerId1 with practitionerId3
		Parameters outParams = myTestHelper.callReplaceReferencesWithResourceLimit(myClient,
			practitionerId1.toString(),
			practitionerId3.toString(),
			false,
			null);

		// Assert operation outcome
		Bundle patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();

		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle,
			1, List.of(
				"Observation"));

		// Fetch and validate updated observation
		Observation updatedObservation = myClient
			.read()
			.resource(Observation.class)
			.withId(observationId)
			.execute();

		// Extract the performer references from the updated Observation
		List<String> actualPerformerIds = updatedObservation.getPerformer().stream()
			.map(ref -> ref.getReferenceElement().toString())
			.toList();

		// Assert that the performer references match the expected values
		assertThat(actualPerformerIds).containsExactly(practitionerId3.toString(), practitionerId2.toString());
	}

	@Test
	void testReplaceReferences_ShouldNotReplaceVersionedReferences() {
		// this configuration makes preserve versioned references in the Provenance.target
		// so that we can test that the versioned reference was not replaced
		// but keep a copy of the original configuration to restore it after the test
		Set<String> originalNotStrippedPaths =
			myFhirContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths();
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");
		try {

			IIdType practitionerId1 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualified();
			IIdType practitionerId2 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualified();

			Provenance provenance = new Provenance();
			provenance.addTarget(new Reference(practitionerId1));
			IIdType provenanceId = myClient.create().resource(provenance).execute().getId();
			// Call $replace-references operation to replace practitionerId1 with practitionerId3
			myTestHelper.callReplaceReferencesWithResourceLimit(myClient,
				practitionerId1.toVersionless().toString(),
				practitionerId2.toVersionless().toString(),
				false,
				null);

			// Fetch and validate the provenance
			Provenance provenanceAfterOperation = myClient
				.read()
				.resource(Provenance.class)
				.withId(provenanceId.toUnqualifiedVersionless())
				.execute();

			// Extract the target references from the Provenance
			List<String> actualTargetIds = provenanceAfterOperation.getTarget().stream()
				.map(ref -> ref.getReferenceElement().toString())
				.toList();

			// Assert that the versioned reference in the Provenance  was not replaced
			assertThat(actualTargetIds).containsExactly(practitionerId1.toString());

		} finally {
			myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(originalNotStrippedPaths);
		}
	}

	private IIdType createObservationWithPerformers(IIdType... performerIds) {
		// Create a new Observation resource
		Observation observation = new Observation();

		// Add references to performers
		for (IIdType performerId : performerIds) {
			observation.addPerformer(new Reference(performerId.toUnqualifiedVersionless()));
		}

		// Store the observation resource via the FHIR client
		return myClient.create().resource(observation).execute().getId();

	}




	@Override
	protected boolean verboseClientLogging() {
		return true;
	}
}
