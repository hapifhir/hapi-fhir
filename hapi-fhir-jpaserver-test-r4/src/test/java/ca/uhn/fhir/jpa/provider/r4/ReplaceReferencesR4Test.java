package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.r4.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.r4.replacereferences.ReplaceReferencesTestHelper.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesR4Test extends BaseResourceProviderR4Test {
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myClient, myDaoRegistry);
		myTestHelper.beforeEach();
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(isAsync);

		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		if (isAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());
			await().until(() -> myTestHelper.taskCompleted(task.getIdElement()));

			Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
			ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutputFirstRep();

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
			assertEquals("Bundle", taskType.getCode());

			List<Resource> containedResources = taskWithOutput.getContained();
			assertThat(containedResources)
				.hasSize(1)
				.element(0)
				.isInstanceOf(Bundle.class);

			Bundle containedBundle = (Bundle) containedResources.get(0);

			Reference outputRef = (Reference) taskOutput.getValue();
			patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));
		} else {
			patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();
		}

		// validate
		myTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES);

		// Check that the linked resources were updated

		validateLinksUsingEverything();
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferencesSmallBatchSize(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferencesWithBatchSize(isAsync, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);


		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart());
		ourLog.info("Got task {}", task.getId());
		await().until(() -> myTestHelper.taskCompleted(task.getIdElement()));

		Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
		ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

		assertThat(taskWithOutput.getOutput()).hasSize(EXPECTED_SMALL_BATCHES);
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
			myTestHelper.validatePatchResultBundle(patchResultBundle, expectedNumberOfEntries);
		}

		// Check that the linked resources were updated

		validateLinksUsingEverything();
	}


	private void validateLinksUsingEverything() {
		Bundle everythingBundle = myTestHelper.getTargetEverythingBundle();

		assertNull(everythingBundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (Bundle.BundleEntryComponent nextEntry : everythingBundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		myTestHelper.assertContainsAllResources(actual, false);
	}
}
