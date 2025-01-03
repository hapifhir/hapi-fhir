package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
	}

	// TODO ED we should add some tests for the invalid request error cases (and assert 4xx status code)

	@Override
	protected boolean verboseClientLogging() {
		return true;
	}
}
