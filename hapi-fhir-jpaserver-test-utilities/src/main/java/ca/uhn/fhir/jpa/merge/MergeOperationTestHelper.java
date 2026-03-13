// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.merge.IResourceLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test helper for invoking generic merge operations and validating merge results.
 * <p>
 * This helper provides methods for:
 * - Invoking merge operations (sync and async)
 * - Tracking async job completion
 * - Validating source/target resource state after merge
 * - Validating reference updates
 * - Validating provenance creation
 */
public class MergeOperationTestHelper {

	private static final Logger ourLog = LoggerFactory.getLogger(MergeOperationTestHelper.class);

	private final IGenericClient myClient;
	private final Batch2JobHelper myBatch2JobHelper;
	private final FhirContext myFhirContext;
	private final ResourceLinkServiceFactory myLinkServiceFactory;
	private final DaoRegistry myDaoRegistry;

	public MergeOperationTestHelper(
			@Nonnull IGenericClient theClient,
			@Nonnull Batch2JobHelper theBatch2JobHelper,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull DaoRegistry theDaoRegistry) {

		myClient = theClient;
		myBatch2JobHelper = theBatch2JobHelper;
		myFhirContext = theFhirContext;
		myLinkServiceFactory = theLinkServiceFactory;
		myDaoRegistry = theDaoRegistry;
	}

	// ================================================
	// MERGE OPERATION INVOCATION
	// ================================================

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

		ourLog.info("Calling $hapi.fhir.merge on {} with async={}", theResourceType, theAsync);

		var operation = myClient.operation()
				.onType(theResourceType)
				.named("$hapi.fhir.merge")
				.withParameters(inputParams);

		if (theAsync) {
			operation.withAdditionalHeader("Prefer", "respond-async");
		}

		return operation.execute();
	}

	/**
	 * Invoke the generic undo-merge operation.
	 *
	 * @param theResourceType The resource type (e.g., "Practitioner", "Observation")
	 * @param theParams       The undo-merge parameters
	 * @return The operation output parameters
	 */
	@Nonnull
	public Parameters callUndoMergeOperation(@Nonnull String theResourceType, @Nonnull Parameters theParams) {
		ourLog.info("Calling $hapi.fhir.undo-merge on {}", theResourceType);

		return myClient.operation()
				.onType(theResourceType)
				.named("$hapi.fhir.undo-merge")
				.withParameters(theParams)
				.returnResourceType(Parameters.class)
				.execute();
	}

	// ================================================
	// ASYNC JOB TRACKING
	// ================================================

	/**
	 * Extract the batch job ID from a Task resource.
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
	 * Waits for async task completion after merge operation.
	 */
	public void waitForAsyncTaskCompletion(@Nonnull Parameters theOutParams) {
		Task task = (Task)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart()); // No version on initial task

		ourLog.info("Got task {}", task.getId());

		String jobId = getJobIdFromTask(task);
		myBatch2JobHelper.awaitJobCompletion(jobId);
	}

	// ================================================
	// OUTCOME VALIDATION
	// ================================================

	/**
	 * Validates the OperationOutcome from a synchronous merge operation contains
	 * the expected success message.
	 */
	public void validateSyncSuccessMessage(@Nonnull Parameters theOutParams) {
		OperationOutcome outcome = (OperationOutcome)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
		});
	}

	// ================================================
	// ERROR VALIDATION
	// ================================================

	/**
	 * Call merge operation expecting exception, validate type, and return diagnostic message.
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

	// ================================================
	// MERGE RESOURCE STATE VALIDATION
	// ================================================

	/**
	 * Validates the source resource state after a merge operation.
	 * <p>
	 * If deleteSource is true, verifies the source returns 410 Gone.
	 * Otherwise, verifies the source has a replaced-by link to the target
	 * and that the active field (if present) is set to false.
	 */
	public void assertSourceResourceState(
			@Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, boolean theDeleteSource) {

		IIdType versionlessSourceId = theSourceId.toUnqualifiedVersionless();
		IIdType versionlessTargetId = theTargetId.toUnqualifiedVersionless();
		String resourceType = versionlessSourceId.getResourceType();

		if (theDeleteSource) {
			assertThatThrownBy(() -> readResource(versionlessSourceId))
					.as("Source resource should be deleted")
					.isInstanceOf(ResourceGoneException.class);
		} else {
			IBaseResource source = readResource(versionlessSourceId);

			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(resourceType);
			List<IBaseReference> replacedByLinks = linkService.getReplacedByLinks(source);

			assertThat(replacedByLinks)
					.as("Source should have replaced-by link")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement()).isEqualTo(versionlessTargetId));

			assertActiveFieldIfSupported(source, false);
		}
	}

	/**
	 * Validates the target resource state after a merge operation.
	 * <p>
	 * Checks that the target has a replaces link to the source (when source not deleted),
	 * and that identifiers match expectations.
	 */
	public void assertTargetResourceState(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			boolean theDeleteSource,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		IIdType versionlessSourceId = theSourceId.toUnqualifiedVersionless();
		IIdType versionlessTargetId = theTargetId.toUnqualifiedVersionless();
		String resourceType = versionlessTargetId.getResourceType();
		IBaseResource target = readResource(versionlessTargetId);

		if (!theDeleteSource) {
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(resourceType);
			List<IBaseReference> replacesLinksRefs = linkService.getReplacesLinks(target);

			assertThat(replacesLinksRefs)
					.as("Target should have replaces link when source not deleted")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement()).isEqualTo(versionlessSourceId));
		}

		assertIdentifiers(target, theExpectedIdentifiers);
	}

	/**
	 * Validates that all referencing resources have their references updated from source to target.
	 */
	public void assertReferencesUpdated(
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId) {

		String versionlessSourceId = theSourceId.toUnqualifiedVersionless().getValue();
		String versionlessTargetId = theTargetId.toUnqualifiedVersionless().getValue();

		for (IIdType refId : theReferencingResourceIds) {
			List<String> refStrings = readResourceAndExtractReferences(refId);

			assertThat(refStrings)
					.as("Resource %s should contain reference to target %s", refId, versionlessTargetId)
					.contains(versionlessTargetId);

			assertThat(refStrings)
					.as("Resource %s should not contain reference to source %s", refId, versionlessSourceId)
					.doesNotContain(versionlessSourceId);
		}
	}

	/**
	 * Validates that referencing resources still have their original references (not updated).
	 * Used for preview mode validation.
	 */
	public void assertReferencesNotUpdated(
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId) {

		String versionlessSourceId = theSourceId.toUnqualifiedVersionless().getValue();
		String versionlessTargetId = theTargetId.toUnqualifiedVersionless().getValue();

		for (IIdType refId : theReferencingResourceIds) {
			List<String> refStrings = readResourceAndExtractReferences(refId);

			assertThat(refStrings)
					.as("Resource %s should still reference source %s", refId, versionlessSourceId)
					.contains(versionlessSourceId);

			assertThat(refStrings)
					.as("Resource %s should not reference target %s", refId, versionlessTargetId)
					.doesNotContain(versionlessTargetId);
		}
	}

	/**
	 * Validates merge provenance record.
	 * Delegates to {@link ReplaceReferencesTestHelper#assertMergeProvenance}.
	 */
	public void assertMergeProvenance(
			@Nonnull Parameters theInputParams,
			@Nonnull IIdType theExpectedSourceId,
			@Nonnull IIdType theExpectedTargetId,
			int theExpectedReferenceCount,
			@Nonnull Set<String> theExpectedReferencingResourceIds,
			@Nullable List<IProvenanceAgent> theExpectedAgents) {

		ReplaceReferencesTestHelper helper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		helper.assertMergeProvenance(
				theInputParams,
				theExpectedSourceId,
				theExpectedTargetId,
				theExpectedReferenceCount,
				theExpectedReferencingResourceIds,
				theExpectedAgents);
	}

	/**
	 * Validates that two resources are equal ignoring version, lastUpdated, and meta.source.
	 * Used for undo-merge validation to verify resources are restored to their pre-merge state.
	 */
	public void assertResourcesAreEqualIgnoringVersionAndLastUpdated(
			@Nonnull IBaseResource theBefore, @Nonnull IBaseResource theAfter) {

		assertThat(theBefore.getIdElement().toVersionless())
				.isEqualTo(theAfter.getIdElement().toVersionless());

		FhirTerser terser = myFhirContext.newTerser();
		// Create a copy of the before resource since we will modify some of its meta data to match the after resource
		IBaseResource copyOfTheBefore = terser.clone(theBefore);

		copyOfTheBefore.getMeta().setLastUpdated(theAfter.getMeta().getLastUpdated());
		copyOfTheBefore.getMeta().setVersionId(theAfter.getMeta().getVersionId());
		copyOfTheBefore.setId(theAfter.getIdElement());

		// Copy meta.source from after to before using terser
		List<IBase> sourceValues = terser.getValues(theAfter, "meta.source");
		if (!sourceValues.isEmpty()) {
			String sourceValue = terser.getSinglePrimitiveValueOrNull(theAfter, "meta.source");
			if (sourceValue != null) {
				terser.addElement(copyOfTheBefore, "meta.source", sourceValue);
			}
		}

		String before = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(copyOfTheBefore);
		String after = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theAfter);
		assertThat(after).isEqualTo(before);
	}

	// ================================================
	// PRIVATE HELPERS
	// ================================================

	/**
	 * If the resource has an 'active' field, asserts it has the expected value.
	 * Uses FhirTerser to generically check any resource type.
	 */
	@SuppressWarnings("unchecked")
	private void assertActiveFieldIfSupported(@Nonnull IBaseResource theResource, boolean theExpectedValue) {
		BaseRuntimeChildDefinition activeChild =
				myFhirContext.getResourceDefinition(theResource).getChildByName("active");
		if (activeChild != null) {
			List<IBase> values = activeChild.getAccessor().getValues(theResource);
			assertThat(values)
					.as("Resource with 'active' field should have it set after merge")
					.isNotEmpty();
			IPrimitiveType<Boolean> activePrimitive = (IPrimitiveType<Boolean>) values.get(0);
			assertThat(activePrimitive.getValue())
					.as("Resource active field should be %s after merge", theExpectedValue)
					.isEqualTo(theExpectedValue);
		}
	}

	private IBaseResource readResource(@Nonnull IIdType theId) {
		return myClient.read().resource(theId.getResourceType()).withId(theId).execute();
	}

	private List<String> readResourceAndExtractReferences(@Nonnull IIdType theRefId) {
		IBaseResource resource = readResource(theRefId);

		FhirTerser terser = myFhirContext.newTerser();
		List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

		List<String> refStrings = allRefs.stream()
				.map(ref -> ref.getReferenceElement().getValue())
				.filter(Objects::nonNull)
				.toList();

		ourLog.info("Resource {} contains references: {}", theRefId, refStrings);

		return refStrings;
	}

	private void assertIdentifiers(
			@Nonnull IBaseResource theResource, @Nonnull List<Identifier> theExpectedIdentifiers) {
		FhirTerser terser = myFhirContext.newTerser();
		List<IBase> actualIdentifiers = terser.getValues(theResource, "identifier");

		assertThat(actualIdentifiers).hasSize(theExpectedIdentifiers.size());

		for (int i = 0; i < theExpectedIdentifiers.size(); i++) {
			Identifier expected = theExpectedIdentifiers.get(i);
			Identifier actual = (Identifier) actualIdentifiers.get(i);
			assertThat(actual.equalsDeep(expected)).isTrue();
		}
	}
}
