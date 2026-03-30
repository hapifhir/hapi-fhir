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
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.GenericMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.IResourceLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.MetaUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

/**
 * Test helper for invoking merge operations and validating merge results.
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
	private final String myOperationName;
	private final AbstractMergeOperationInputParameterNames myParameterNames;

	/**
	 * Creates a helper configured for the generic $hapi.fhir.merge endpoint
	 * with generic parameter names (source-resource, target-resource, etc.).
	 */
	public MergeOperationTestHelper(
			@Nonnull IGenericClient theClient,
			@Nonnull Batch2JobHelper theBatch2JobHelper,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull DaoRegistry theDaoRegistry) {
		this(
				theClient,
				theBatch2JobHelper,
				theFhirContext,
				theLinkServiceFactory,
				theDaoRegistry,
				"$hapi.fhir.merge",
				new GenericMergeOperationInputParameterNames());
	}

	/**
	 * Creates a helper configured for a specific merge endpoint and parameter names.
	 * Use this for testing the FHIR-standard Patient/$merge endpoint with patient-specific
	 * parameter names (source-patient, target-patient, etc.).
	 */
	public MergeOperationTestHelper(
			@Nonnull IGenericClient theClient,
			@Nonnull Batch2JobHelper theBatch2JobHelper,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull String theOperationName,
			@Nonnull AbstractMergeOperationInputParameterNames theParameterNames) {

		myClient = theClient;
		myBatch2JobHelper = theBatch2JobHelper;
		myFhirContext = theFhirContext;
		myLinkServiceFactory = theLinkServiceFactory;
		myDaoRegistry = theDaoRegistry;
		myOperationName = theOperationName;
		myParameterNames = theParameterNames;
	}

	@Nonnull
	public AbstractMergeOperationInputParameterNames getParameterNames() {
		return myParameterNames;
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

		Parameters inputParams = theParams.asParametersResource(myParameterNames);

		ourLog.info("Calling {} on {} with async={}", myOperationName, theResourceType, theAsync);

		var operation = myClient.operation()
				.onType(theResourceType)
				.named(myOperationName)
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
		assertThat(task.getIdElement().getVersionIdPart()).isNull(); // No version on initial task

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

	/**
	 * Validates a synchronous merge operation outcome: input parameters returned correctly,
	 * success message present, and result resource in output matches the target in the database.
	 *
	 * @param theOutParams             the output parameters from the merge operation
	 * @param theOriginalInputParams   the original input parameters sent to the operation
	 * @param theTargetId              the target resource ID
	 */
	public void validateSyncMergeOutcome(
			@Nonnull Parameters theOutParams,
			@Nonnull Parameters theOriginalInputParams,
			@Nonnull IIdType theTargetId) {

		// Validate input parameters returned
		Parameters returnedInput = (Parameters)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();
		assertThat(returnedInput.equalsDeep(theOriginalInputParams))
				.as("Returned input parameters should match original input parameters")
				.isTrue();

		// Validate success message
		validateSyncSuccessMessage(theOutParams);

		// Validate result resource matches target in database
		IBaseResource targetResourceInOutput =
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
		IBaseResource targetResourceReadFromDB = readResource(theTargetId.toUnqualifiedVersionless());
		String outputJson = myFhirContext.newJsonParser().encodeResourceToString(targetResourceInOutput);
		String dbJson = myFhirContext.newJsonParser().encodeResourceToString(targetResourceReadFromDB);
		assertThat(outputJson).isEqualTo(dbJson);
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
	 * Otherwise, reads the source by its versionless ID, verifies its version matches
	 * the expected versioned ID, that it has a replaced-by link to the target,
	 * and that the active field (if present) is set to false.
	 *
	 * @param theExpectedVersionedSourceId the expected versioned source ID (e.g., Patient/A/_history/2)
	 * @param theTargetId                  the target resource ID
	 * @param theDeleteSource              whether deleteSource was set in the merge
	 */
	public void assertSourceResourceState(
			@Nonnull IIdType theExpectedVersionedSourceId, @Nonnull IIdType theTargetId, boolean theDeleteSource) {

		IIdType versionlessSourceId = theExpectedVersionedSourceId.toUnqualifiedVersionless();
		IIdType versionlessTargetId = theTargetId.toUnqualifiedVersionless();
		String resourceType = versionlessSourceId.getResourceType();

		if (theDeleteSource) {
			assertThatThrownBy(() -> readResource(versionlessSourceId))
					.as("Source resource should be deleted")
					.isInstanceOf(ResourceGoneException.class);
		} else {
			IBaseResource source = readResource(versionlessSourceId);

			assertThat(source.getIdElement().getVersionIdPart())
					.isEqualTo(theExpectedVersionedSourceId.getVersionIdPart());

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
	 * Reads the target by its versionless ID, verifies its version matches the expected
	 * versioned ID, checks that the target has a replaces link to the source (when source
	 * not deleted), and that identifiers match expectations.
	 *
	 * @param theSourceId                  the source resource ID
	 * @param theExpectedVersionedTargetId the expected versioned target ID (e.g., Patient/B/_history/2)
	 * @param theDeleteSource              whether deleteSource was set in the merge
	 * @param theExpectedIdentifiers       expected identifiers on the target after merge
	 */
	public void assertTargetResourceState(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theExpectedVersionedTargetId,
			boolean theDeleteSource,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		IIdType versionlessSourceId = theSourceId.toUnqualifiedVersionless();
		IIdType versionlessTargetId = theExpectedVersionedTargetId.toUnqualifiedVersionless();
		String resourceType = versionlessTargetId.getResourceType();
		IBaseResource target = readResource(versionlessTargetId);

		assertThat(target.getIdElement().getVersionIdPart()).isEqualTo(theExpectedVersionedTargetId.getVersionIdPart());

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
	 * Used for preview mode validation and undo-merge tests.
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
			@Nonnull Set<String> theExpectedProvenanceTargets,
			@Nullable List<IProvenanceAgent> theExpectedAgents) {

		ReplaceReferencesTestHelper helper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		helper.assertMergeProvenance(
				theInputParams,
				theExpectedSourceId,
				theExpectedTargetId,
				theExpectedProvenanceTargets,
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

		// Copy meta.source from after to before
		MetaUtil.setSource(
				myFhirContext, copyOfTheBefore, terser.getSinglePrimitiveValueOrNull(theAfter, "meta.source"));

		String before = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(copyOfTheBefore);
		String after = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theAfter);
		assertThat(after).isEqualTo(before);
	}

	/**
	 * Validates all standard post-merge state: source resource, target resource,
	 * references updated, and merge provenance.
	 *
	 * @param theMergeParams                the merge parameters used to invoke the operation
	 * @param theExpectedVersionedSourceId  the expected versioned source ID after merge
	 * @param theExpectedVersionedTargetId  the expected versioned target ID after merge
	 * @param theReferencingResourceIds     IDs of resources that should now reference the target
	 *                                      instead of source
	 * @param theExpectedProvenanceTargets  versioned ID strings for all expected provenance targets
	 *                                      including source and target patients
	 * @param theExpectedTargetIdentifiers  expected identifiers on the target resource after merge
	 * @param theExpectedAgents             expected provenance agents, or null if not applicable
	 */
	public void validateResourcesAfterMerge(
			@Nonnull MergeTestParameters theMergeParams,
			@Nonnull IIdType theExpectedVersionedSourceId,
			@Nonnull IIdType theExpectedVersionedTargetId,
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull Set<String> theExpectedProvenanceTargets,
			@Nonnull List<Identifier> theExpectedTargetIdentifiers,
			@Nullable List<IProvenanceAgent> theExpectedAgents) {

		IIdType sourceId = theExpectedVersionedSourceId.toUnqualifiedVersionless();
		IIdType targetId = theExpectedVersionedTargetId.toUnqualifiedVersionless();
		boolean deleteSource = Boolean.TRUE.equals(theMergeParams.getDeleteSource());

		assertSourceResourceState(theExpectedVersionedSourceId, targetId, deleteSource);
		assertTargetResourceState(sourceId, theExpectedVersionedTargetId, deleteSource, theExpectedTargetIdentifiers);

		if (!theReferencingResourceIds.isEmpty()) {
			assertReferencesUpdated(theReferencingResourceIds, sourceId, targetId);
		}

		assertMergeProvenance(
				theMergeParams.asParametersResource(myParameterNames),
				theExpectedVersionedSourceId,
				theExpectedVersionedTargetId,
				theExpectedProvenanceTargets,
				theExpectedAgents);
	}

	/**
	 * Computes the expected identifiers on the target resource after a merge.
	 * <p>
	 * If result resource identifiers are provided (non-null), they are used directly
	 * (the result resource replaces the target's identifiers entirely).
	 * Otherwise, the target keeps its own identifiers, and source identifiers not already
	 * present on the target are appended with {@link Identifier.IdentifierUse#OLD}.
	 *
	 * @param theTargetIdentifiers         identifiers on the target before merge
	 * @param theSourceIdentifiers         identifiers on the source before merge
	 * @param theResultResourceIdentifiers identifiers from the result resource, or null if no result resource
	 * @return the expected identifier list on the target after merge
	 */
	@Nonnull
	public List<Identifier> computeIdentifiersExpectedOnTargetAfterMerge(
			@Nonnull List<Identifier> theTargetIdentifiers,
			@Nonnull List<Identifier> theSourceIdentifiers,
			@Nullable List<Identifier> theResultResourceIdentifiers) {

		if (theResultResourceIdentifiers != null) {
			return new ArrayList<>(theResultResourceIdentifiers);
		}

		List<Identifier> expected = new ArrayList<>(theTargetIdentifiers);

		for (Identifier sourceId : theSourceIdentifiers) {
			boolean isCommonIdentifier = theTargetIdentifiers.stream()
					.anyMatch(targetId -> sourceId.getSystem().equals(targetId.getSystem())
							&& sourceId.getValue().equals(targetId.getValue()));

			if (!isCommonIdentifier) {
				Identifier copy = sourceId.copy();
				copy.setUse(Identifier.IdentifierUse.OLD);
				expected.add(copy);
			}
		}

		return expected;
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
		IIdType versionlessId = theId.toUnqualifiedVersionless();
		return myClient.read()
				.resource(versionlessId.getResourceType())
				.withId(versionlessId)
				.execute();
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
