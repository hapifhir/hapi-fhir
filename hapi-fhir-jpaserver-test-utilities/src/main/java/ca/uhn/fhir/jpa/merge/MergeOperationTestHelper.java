// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Task;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test helper for invoking generic merge operations.
 * <p>
 * This helper provides methods for:
 * - Invoking merge operations (sync and async)
 * - Tracking async job completion
 * - Extracting job IDs from Task resources
 *
 * <p>Validation methods have been moved to {@link MergeTestScenario}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * MergeOperationTestHelper helper = new MergeOperationTestHelper(client, batch2JobHelper);
 *
 * // Execute merge
 * Parameters outParams = helper.callMergeOperation("Practitioner", params, false);
 *
 * // For async operations
 * Task task = (Task) outParams.getParameter("task").getResource();
 * String jobId = helper.getJobIdFromTask(task);
 * helper.awaitJobCompletion(jobId);
 * </pre>
 */
public class MergeOperationTestHelper {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MergeOperationTestHelper.class);

	private final IGenericClient myClient;
	private final Batch2JobHelper myBatch2JobHelper;
	private final FhirContext myFhirContext;

	/**
	 * Create a new merge operation test helper.
	 *
	 * @param theClient          FHIR client for invoking operations
	 * @param theBatch2JobHelper Helper for tracking async jobs
	 * @param theFhirContext     FHIR context for parsing error responses
	 */
	public MergeOperationTestHelper(
			@Nonnull IGenericClient theClient,
			@Nonnull Batch2JobHelper theBatch2JobHelper,
			@Nonnull FhirContext theFhirContext) {

		myClient = theClient;
		myBatch2JobHelper = theBatch2JobHelper;
		myFhirContext = theFhirContext;

		// Register verbose logging interceptor to capture error response bodies
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogRequestSummary(true);
		loggingInterceptor.setLogResponseSummary(true);
		loggingInterceptor.setLogRequestBody(true);
		loggingInterceptor.setLogResponseBody(true); // KEY: This logs error response bodies
		myClient.registerInterceptor(loggingInterceptor);
	}

	/**
	 * Invoke the generic merge operation.
	 *
	 * @param theResourceType The resource type (e.g., "Practitioner", "Observation")
	 * @param theParams       The merge parameters
	 * @param theAsync        Whether to execute asynchronously
	 * @return The operation output parameters
	 */
	@Nonnull
	public Parameters callMergeOperation(
			@Nonnull String theResourceType, @Nonnull MergeTestParameters theParams, boolean theAsync) {

		Parameters inputParams = theParams.asParametersResource();

		ourLog.info("Calling $hapi-fhir-merge on {} with async={}", theResourceType, theAsync);

		var operation = myClient.operation()
				.onType(theResourceType)
				.named("$hapi-fhir-merge")
				.withParameters(inputParams);

		if (theAsync) {
			operation.withAdditionalHeader("Prefer", "respond-async");
		}

		return operation.execute();
	}

	// Async job tracking

	/**
	 * Extract the batch job ID from a Task resource.
	 *
	 * @param theTask The Task resource
	 * @return The batch job ID
	 */
	@Nonnull
	public String getJobIdFromTask(@Nonnull Task theTask) {
		return theTask.getIdentifier().stream()
				.filter(id -> "http://hapifhir.io/batch/jobId".equals(id.getSystem()))
				.map(Identifier::getValue)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No batch2-job-id found in task: " + theTask.getId()));
	}

	/**
	 * Wait for an async job to complete.
	 *
	 * @param theJobId The batch job ID
	 */
	public void awaitJobCompletion(@Nonnull String theJobId) {
		ourLog.debug("Waiting for job completion: {}", theJobId);
		myBatch2JobHelper.awaitJobCompletion(theJobId);
		ourLog.debug("Job completed: {}", theJobId);
	}

	/**
	 * Waits for async task completion after merge operation.
	 * Validates task creation, extracts job ID, and waits for batch job to complete.
	 * Copied from PatientMergeR4Test.waitForAsyncTaskCompletion().
	 *
	 * @param theOutParams the output parameters from merge operation
	 */
	public void waitForAsyncTaskCompletion(@Nonnull Parameters theOutParams) {
		Task task = (Task)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart()); // No version on initial task

		ourLog.info("Got task {}", task.getId());

		// Use existing getJobIdFromTask() method
		String jobId = getJobIdFromTask(task);

		// Use existing awaitJobCompletion() method
		awaitJobCompletion(jobId);
	}

	// Error validation helpers

	/**
	 * Call merge operation expecting exception, validate type, and return diagnostic message.
	 *
	 * @param theResourceType          resource type to merge
	 * @param theParams                merge parameters
	 * @param theExpectedExceptionType expected exception class
	 * @return diagnostic message for custom validation
	 */
	@Nonnull
	public String callMergeAndExtractDiagnosticMessage(
			@Nonnull String theResourceType,
			@Nonnull MergeTestParameters theParams,
			@Nonnull Class<? extends BaseServerResponseException> theExpectedExceptionType) {

		Exception ex = catchException(() -> callMergeOperation(theResourceType, theParams, false));
		assertThat(ex).isInstanceOf(theExpectedExceptionType);

		BaseServerResponseException serverEx = (BaseServerResponseException) ex;
		return ReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(myFhirContext, serverEx);
	}

	/**
	 * Call merge operation and validate that it throws expected exception with expected messages.
	 *
	 * @param theResourceType                resource type to merge
	 * @param theParams                      merge parameters
	 * @param theExpectedExceptionType       expected exception class
	 * @param theExpectedDiagnosticMessageParts diagnostic message parts that should be present
	 */
	public void callMergeAndValidateException(
			@Nonnull String theResourceType,
			@Nonnull MergeTestParameters theParams,
			@Nonnull Class<? extends BaseServerResponseException> theExpectedExceptionType,
			String... theExpectedDiagnosticMessageParts) {

		String diagnosticMessage =
				callMergeAndExtractDiagnosticMessage(theResourceType, theParams, theExpectedExceptionType);

		for (String messagePart : theExpectedDiagnosticMessageParts) {
			assertThat(diagnosticMessage).contains(messagePart);
		}
	}
}
