package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.List;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesR4Test extends BaseResourceProviderR4Test {
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myTestHelper.beforeEach();
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(myClient, isAsync);

		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		if (isAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());

			awaitJobCompletion(task);

// FIXME KHS verify report

			patchResultBundle = myTestHelper.validateCompletedTask(task.getIdElement());
		} else {
			patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();
		}

		// validate
		myTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES, List.of("Observation", "Encounter", "CarePlan"));

		// Check that the linked resources were updated

		myTestHelper.assertAllReferencesUpdated();
	}

	private void awaitJobCompletion(Task task) {
		assertThat(task.getIdentifier()).hasSize(1)
			.element(0)
			.extracting(Identifier::getSystem)
			.isEqualTo(HAPI_BATCH_JOB_ID_SYSTEM);

		String jobId = task.getIdentifierFirstRep().getValue();
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(jobId);
	}


	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferencesSmallBatchSize(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferencesWithBatchSize(myClient, isAsync, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);


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

		int entriesLeft = ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES;
		for (int i = 1; i < EXPECTED_SMALL_BATCHES; i++) {

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutput().get(i);

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
			assertEquals("Bundle", taskType.getCode());

			Bundle containedBundle = (Bundle) containedResources.get(i);

			Reference outputRef = (Reference) taskOutput.getValue();
			patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));

			// validate
			entriesLeft -= ReplaceReferencesTestHelper.SMALL_BATCH_SIZE;
			int expectedNumberOfEntries = Math.min(entriesLeft, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);
			myTestHelper.validatePatchResultBundle(patchResultBundle, expectedNumberOfEntries, List.of("Observation", "Encounter", "CarePlan"));
		}

		// Check that the linked resources were updated

		myTestHelper.assertAllReferencesUpdated();
	}
}
