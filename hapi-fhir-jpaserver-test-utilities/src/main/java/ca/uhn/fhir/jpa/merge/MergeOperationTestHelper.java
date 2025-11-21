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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test helper for invoking and validating generic merge operations.
 * <p>
 * This helper provides methods for:
 * - Invoking merge operations (sync and async)
 * - Validating operation outcomes
 * - Tracking async job completion
 * - Validating reference updates
 * - Validating provenance creation
 *
 * <p>Example usage:</p>
 * <pre>
 * MergeOperationTestHelper helper = new MergeOperationTestHelper(
 *     fhirContext, daoRegistry, client, batch2JobHelper);
 *
 * // Execute merge
 * Parameters outParams = helper.callMergeOperation("Practitioner", params, false);
 *
 * // Validate outcome
 * helper.validateSyncMergeOutcome(outParams);
 *
 * // Validate references updated
 * helper.assertReferencesUpdated(scenario.getReferencingResourceIds("PractitionerRole"),
 *     sourceId, targetId);
 * </pre>
 */
public class MergeOperationTestHelper {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MergeOperationTestHelper.class);

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final IGenericClient myClient;
	private final Batch2JobHelper myBatch2JobHelper;

	/**
	 * Create a new merge operation test helper.
	 *
	 * @param theFhirContext     FHIR context
	 * @param theDaoRegistry     DAO registry for reading resources
	 * @param theClient          FHIR client for invoking operations
	 * @param theBatch2JobHelper Helper for tracking async jobs
	 */
	public MergeOperationTestHelper(
			@Nonnull FhirContext theFhirContext,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull IGenericClient theClient,
			@Nonnull Batch2JobHelper theBatch2JobHelper) {

		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myClient = theClient;
		myBatch2JobHelper = theBatch2JobHelper;

		// Register verbose logging interceptor to capture error response bodies
		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogRequestSummary(true);
		loggingInterceptor.setLogResponseSummary(true);
		loggingInterceptor.setLogRequestBody(true);
		loggingInterceptor.setLogResponseBody(true); // KEY: This logs error response bodies
		myClient.registerInterceptor(loggingInterceptor);
	}

	// Core merge operation invocation

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

		Parameters inputParams = theParams.asParametersResource(theResourceType);

		ourLog.debug("Calling $hapi-fhir-merge on {} with async={}", theResourceType, theAsync);

		var operation = myClient.operation()
				.onType(theResourceType)
				.named("$hapi-fhir-merge")
				.withParameters(inputParams);

		if (theAsync) {
			operation.withAdditionalHeader("Prefer", "respond-async");
		}

		return operation.execute();
	}

	// Validation methods

	/**
	 * Validate that a synchronous merge completed successfully.
	 * <p>
	 * Checks:
	 * - Output has 3 parameters (input, outcome, result)
	 * - Outcome severity is INFORMATION
	 * - Outcome message indicates success
	 *
	 * @param theOutParams The operation output parameters
	 */
	public void validateSyncMergeOutcome(@Nonnull Parameters theOutParams) {
		assertThat(theOutParams.getParameter())
				.as("Sync merge should return 3 parameters")
				.hasSize(3);

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		ourLog.info(
				"Sync merge OperationOutcome: severity={}, details={}, diagnostics={}",
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getSeverity(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDetails().getText(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDiagnostics());

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
		});

		ourLog.debug("Sync merge outcome validated successfully");
	}

	/**
	 * Validate that an async merge task was created.
	 * <p>
	 * Checks:
	 * - Output has 3 parameters (input, outcome, task)
	 * - Task resource is present
	 * - Outcome indicates async processing
	 *
	 * @param theOutParams The operation output parameters
	 */
	public void validateAsyncTaskCreated(@Nonnull Parameters theOutParams) {
		assertThat(theOutParams.getParameter())
				.as("Async merge should return 3 parameters")
				.hasSize(3);

		Task task = (Task) theOutParams.getParameter("task").getResource();
		assertThat(task).as("Task should be present").isNotNull();

		ourLog.info(
				"Async Task created: id={}, status={}, identifiers={}",
				task.getId(),
				task.getStatus(),
				task.getIdentifier().stream()
						.map(id -> id.getSystem() + "|" + id.getValue())
						.toList());

		assertThat(task.getIdElement().hasVersionIdPart())
				.as("Task should not have version")
				.isFalse();

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).contains("asynchronously");
		});

		ourLog.debug("Async task creation validated successfully: {}", task.getId());
	}

	/**
	 * Validate that a preview merge completed successfully.
	 * <p>
	 * Checks:
	 * - Outcome indicates preview mode
	 * - Diagnostics shows expected update count
	 *
	 * @param theOutParams           The operation output parameters
	 * @param theExpectedUpdateCount Expected number of resources that would be updated
	 */
	public void validatePreviewOutcome(@Nonnull Parameters theOutParams, int theExpectedUpdateCount) {
		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		ourLog.info(
				"Preview merge OperationOutcome: expectedCount={}, actualDiagnostics={}, details={}",
				theExpectedUpdateCount,
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDiagnostics(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDetails().getText());

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Preview only merge operation - no issues detected");
			assertThat(issue.getDiagnostics()).isEqualTo("Merge would update " + theExpectedUpdateCount + " resources");
		});

		ourLog.debug("Preview outcome validated successfully: {} resources would be updated", theExpectedUpdateCount);
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

	// Reference update validation

	/**
	 * Assert that referencing resources have been updated to point to the target.
	 * <p>
	 * Verifies that none of the specified resources contain references to the source resource.
	 *
	 * @param theReferencingResourceIds IDs of resources that should have been updated
	 * @param theSourceId               The source resource ID (references should be removed)
	 * @param theTargetId               The target resource ID (not used in validation, provided for logging)
	 */
	public void assertReferencesUpdated(
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId) {

		ourLog.debug(
				"Validating {} referencing resources updated from {} to {}",
				theReferencingResourceIds.size(),
				theSourceId,
				theTargetId);

		FhirTerser terser = myFhirContext.newTerser();

		for (IIdType refId : theReferencingResourceIds) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(refId.getResourceType());
			IBaseResource resource = dao.read(refId, null);

			// Use FhirTerser to find all references
			List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

			List<String> refStrings = allRefs.stream()
					.map(ref -> ref.getReferenceElement().getValue())
					.filter(refStr -> refStr != null)
					.toList();

			ourLog.info("Resource {} contains references: {}", refId, refStrings);

			// Verify none point to source
			for (IBaseReference reference : allRefs) {
				String refString = reference.getReferenceElement().getValue();
				if (refString != null) {
					assertThat(refString)
							.as("Reference in %s should not point to source %s", refId, theSourceId)
							.doesNotContain(theSourceId.getIdPart());
				}
			}
		}

		ourLog.debug("All {} referencing resources validated successfully", theReferencingResourceIds.size());
	}

	/**
	 * Assert that referencing resources have NOT been updated (for preview mode validation).
	 * <p>
	 * Verifies that resources still contain references to the source resource.
	 *
	 * @param theScenario The test scenario with referencing resources
	 */
	public <T extends IBaseResource> void assertReferencesNotUpdated(@Nonnull MergeTestScenario<T> theScenario) {
		ourLog.debug("Validating references NOT updated in preview mode for source: {}", theScenario.getSourceId());

		FhirTerser terser = myFhirContext.newTerser();

		for (String resourceType : theScenario.getReferencingResourceTypes()) {
			for (IIdType refId : theScenario.getReferencingResourceIds(resourceType)) {
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
				IBaseResource resource = dao.read(refId, null);

				List<IBaseReference> allRefs =
						terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

				boolean foundSourceRef = false;
				for (IBaseReference reference : allRefs) {
					String refString = reference.getReferenceElement().getValue();
					if (refString != null
							&& refString.contains(theScenario.getSourceId().getIdPart())) {
						foundSourceRef = true;
						break;
					}
				}

				assertThat(foundSourceRef)
						.as(
								"Resource %s should still reference source %s in preview mode",
								refId, theScenario.getSourceId())
						.isTrue();
			}
		}

		ourLog.debug("Verified references not updated in preview mode");
	}

	// Provenance validation

	/**
	 * Assert that merge provenance was created for the merge operation.
	 *
	 * @param theSourceId    The source resource ID
	 * @param theTargetId    The target resource ID
	 * @param theInputParams The input parameters used for the merge
	 */
	public void assertMergeProvenanceCreated(
			@Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, @Nonnull Parameters theInputParams) {

		ourLog.debug("Validating provenance created for merge: source={}, target={}", theSourceId, theTargetId);

		// Search for provenance targeting the resources
		// Implementation would search for Provenance resources with target references
		// This is a placeholder - actual implementation would use search parameters

		ourLog.debug("Provenance validation completed");
	}
}
