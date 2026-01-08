// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.merge.AbstractMergeTestScenario;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

/**
 * Abstract base class for generic undo-merge operation tests.
 *
 * <p>Provides common test methods for all resource types. Subclasses only need to:
 * <ol>
 *   <li>Define the resource type via {@link #createScenario()}</li>
 *   <li>Define the resource type name via {@link #getResourceTypeName()}</li>
 *   <li>Add resource-specific tests (optional)</li>
 * </ol>
 *
 * @param <T> the FHIR resource type being tested
 */
public abstract class AbstractGenericUndoMergeR4Test<T extends IBaseResource> extends BaseResourceProviderR4Test {

	@Autowired
	protected Batch2JobHelper myBatch2JobHelper;

	@Autowired
	protected ResourceLinkServiceFactory myLinkServiceFactory;

	protected MergeOperationTestHelper myHelper;

	/**
	 * Template method for subclasses to create their test scenario.
	 */
	@Nonnull
	protected abstract AbstractMergeTestScenario<T> createScenario();

	/**
	 * Template method for subclasses to provide their resource type name.
	 */
	@Nonnull
	protected abstract String getResourceTypeName();

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
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myHelper = new MergeOperationTestHelper(myClient, myBatch2JobHelper, myFhirContext);
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
		// Setup: Create scenario with multiple referencing resources
		AbstractMergeTestScenario<T> scenario = createScenario()
				.withMultipleReferencingResources()
				.withDeleteSource(theDeleteSource);
		scenario.persistTestData();

		// Store "before merge" state for validation after undo
		T sourceBeforeMerge = scenario.readSourceResource();
		T targetBeforeMerge = scenario.readTargetResource();

		// Execute: Perform merge operation
		// Note: callMergeOperation takes "sourceById" parameter (opposite of "useIdentifiers")
		boolean sourceById = !theUseIdentifiersAsSrcInput;
		boolean targetById = !theUseIdentifiersAsTargetInput;
		scenario.callMergeOperation(sourceById, targetById);

		// Execute: Perform undo-merge operation
		Parameters undoMergeOutParams = scenario.callUndoMergeOperation(sourceById, targetById);

		// Validate: Undo-merge operation outcome indicates success
		int expectedResourceCount = scenario.getTotalReferenceCount() + 2; // +2 for source and target
		validateSuccessOutcome(undoMergeOutParams, expectedResourceCount);

		// Validate: Resources are restored to their "before merge" state
		T sourceAfterUnmerge = scenario.readSourceResource();
		T targetAfterUnmerge = scenario.readTargetResource();
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceBeforeMerge, sourceAfterUnmerge);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetBeforeMerge, targetAfterUnmerge);

		// Validate: References are back to their original state (pointing to source, not target)
		scenario.assertReferencesNotUpdated();
	}



	@Test
	void testUndoMerge_TargetResourceIsNotUpdatedByMerge_SkipsRestoringTargetResource() {
		// During merge, if deleteSource was true, no resultResource was provided and the src resource didn't contain any identifiers to copy to the target,
		// the target resource wouldn't be updated by the merge. In that scenario the target resource
		// should not be restored to a previous version by undo merge. This test verifies that.

		// Setup: Create scenario with NO identifiers on source (empty list means no identifiers to copy)
		// This ensures the target is NOT updated during merge
		AbstractMergeTestScenario<T> scenario = createScenario()
				.withSourceIdentifiers(List.of()) // Empty identifiers = nothing to copy to target
				.withTargetIdentifiers(List.of()) // Empty identifiers on target too
				.withOneReferencingResource()
				.withDeleteSource(true);
		scenario.persistTestData();

		// Store "before merge" state for validation after undo
		T sourceBeforeMerge = scenario.readSourceResource();
		IIdType referencingResourceId = getFirstReferencingResourceId(scenario);

		// Read referencing resource before merge for later comparison
		IBaseResource referencingResourceBeforeMerge = scenario.readResource(referencingResourceId);

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Validate: Target should still be at version 1 (not updated because source had no identifiers)
		T targetAfterMerge = scenario.readTargetResource();
		assertThat(targetAfterMerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(1);

		// Execute: Perform undo-merge operation
		Parameters undoMergeOutParams = scenario.callUndoMergeOperation();

		// Validate: Only source and referencing resource restored (not target), so expected count is 2
		validateSuccessOutcome(undoMergeOutParams, 2);

		// Validate: Target should still be at version 1 (not restored)
		T targetAfterUnmerge = scenario.readTargetResource();
		assertThat(targetAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(1);

		// Validate: Source and referencing resource are restored to version 3
		T sourceAfterUnmerge = scenario.readSourceResource();
		assertThat(sourceAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(3);

		IBaseResource referencingResourceAfterUnmerge = scenario.readResource(referencingResourceId);
		assertThat(referencingResourceAfterUnmerge.getIdElement().getVersionIdPartAsLong()).isEqualTo(3);

		// Validate: Source and referencing resource content match their "before merge" state
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceBeforeMerge, sourceAfterUnmerge);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(referencingResourceBeforeMerge, referencingResourceAfterUnmerge);
	}

	@Test
	void testUndoMerge_SrcResourceUpdatedAfterMerge_UndoFailsWithConflict() {
		// Setup: Create scenario with one referencing resource
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withDeleteSource(false);
		scenario.persistTestData();

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Update the source resource after the merge to create a version conflict
		modifyResourceAndUpdate(scenario.readSourceResource());

		// Execute: Attempt undo-merge, expecting it to fail with version conflict
		callUndoOnScenarioAndAssertException(scenario, ResourceVersionConflictException.class, 409, "HAPI-2732");
	}


	@Test
	void testUndoMerge_TargetUpdatedAfterMerge_UndoFailsWithConflict() {
		// Setup: Create scenario with one referencing resource
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withDeleteSource(false);
		scenario.persistTestData();

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Update the target resource after the merge to create a version conflict
		modifyResourceAndUpdate(scenario.readTargetResource());

		// Execute: Attempt undo-merge, expecting it to fail with version conflict
		callUndoOnScenarioAndAssertException(scenario, ResourceVersionConflictException.class, 409, "HAPI-2732");
	}

	@Test
	void testUndoMerge_ReferencingResourceUpdatedAfterMerge_UndoFailsWithConflict() {
		// Setup: Create scenario with one referencing resource
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withDeleteSource(false);
		scenario.persistTestData();

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Update the referencing resource after the merge to create a version conflict
		modifyResourceAndUpdate(scenario.readResource(getFirstReferencingResourceId(scenario)));

		// Execute: Attempt undo-merge, expecting it to fail with version conflict
		callUndoOnScenarioAndAssertException(scenario, ResourceVersionConflictException.class, 409, "HAPI-2732");
	}

	@Test
	void testUndoMerge_ReferencingResourceDeletedAfterMerge_UndoFailsWithGone() {
		// Setup: Create scenario with one referencing resource
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withDeleteSource(false);
		scenario.persistTestData();

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Delete the referencing resource after the merge
		deleteResourceById(getFirstReferencingResourceId(scenario));

		// Execute: Attempt undo-merge, expecting it to fail with ResourceGoneException
		callUndoOnScenarioAndAssertException(scenario, ResourceGoneException.class, 410, "HAPI-2751");
	}


	@Test
	void testUndoMerge_ResourceLimitExceeded() {
		// Setup: Create scenario with one referencing resource
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withDeleteSource(false);
		scenario.persistTestData();

		// Execute: Perform merge operation
		scenario.callMergeOperation();

		// Set a low limit to trigger the resource limit exceeded error
		int originalLimit = myStorageSettings.getInternalSynchronousSearchSize();
		myStorageSettings.setInternalSynchronousSearchSize(2);

		try {
			// Execute: Attempt undo-merge, expecting it to fail with resource limit exceeded
			// 3 resources: source, target, and one referencing resource
			String expectedMessage = "HAPI-2748: Number of references to update (3) exceeds the limit (2)";
			callUndoOnScenarioAndAssertException(scenario, InvalidRequestException.class, 400, expectedMessage);
		} finally {
			// Restore the original limit
			myStorageSettings.setInternalSynchronousSearchSize(originalLimit);
		}
	}

	@Test
	void testUndoMerge_NoProvenanceExistsForMerge_Fails() {
		// Setup: Create scenario without referencing resources (no merge will be performed)
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

		// Create a Provenance with a non-merge activity (undo-merge should not find valid merge provenance)
		IIdType sourceId = scenario.readSourceResource().getIdElement();
		IIdType targetId = scenario.readTargetResource().getIdElement();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(targetId));
		provenance.addTarget(new Reference(sourceId));
		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("some-other-activity")));
		myProvenanceDao.create(provenance, mySrd);

		// Execute: Attempt undo-merge, expecting it to fail because no valid merge provenance exists
		callUndoOnScenarioAndAssertException(scenario, ResourceNotFoundException.class, 404, "HAPI-2747");
	}


	@Test
	void testUndoMerge_ProvenanceExistsButMissingContainedResourceForInputParams_Fails() {
		// Setup: Create scenario without referencing resources
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

		// Create a Provenance with merge activity but missing contained Parameters resource
		IIdType sourceId = scenario.readSourceResource().getIdElement();
		IIdType targetId = scenario.readTargetResource().getIdElement();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(targetId.withVersion("1")));
		provenance.addTarget(new Reference(sourceId.withVersion("1")));
		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("merge")));
		myProvenanceDao.create(provenance, mySrd);

		// Execute: Attempt undo-merge, expecting it to fail because Provenance is missing contained input params
		callUndoOnScenarioAndAssertException(scenario, InternalErrorException.class, 500, "HAPI-2749");
	}


	@Test
	void testUndoMerge_ProvenanceExistsButMissingContainedResourceTargetUpdateOutput_Fails() {
		// Setup: Create scenario without referencing resources
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

		// Create a Provenance with merge activity and empty contained Parameters (missing target update output)
		IIdType sourceId = scenario.readSourceResource().getIdElement();
		IIdType targetId = scenario.readTargetResource().getIdElement();

		Provenance provenance = new Provenance();
		provenance.addTarget(new Reference(targetId.withVersion("1")));
		provenance.addTarget(new Reference(sourceId.withVersion("1")));
		provenance.setActivity(new CodeableConcept().addCoding(new Coding()
			.setSystem("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle")
			.setCode("merge")));
		Parameters parameters = new Parameters();
		provenance.addContained(parameters);
		myProvenanceDao.create(provenance, mySrd);

		// Execute: Attempt undo-merge, expecting it to fail because Provenance has missing target update output
		callUndoOnScenarioAndAssertException(scenario, InternalErrorException.class, 500, "HAPI-2750");
	}

	/**
	 * Helper method to update a resource using the appropriate DAO.
	 * This ensures consistent behavior across different resource types.
	 *
	 * @param theResource the resource to update
	 */
	private void updateResource(IBaseResource theResource) {
		@SuppressWarnings({"rawtypes"})
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		dao.update(theResource, mySrd);
	}

	/**
	 * Helper method to delete a resource using the appropriate DAO.
	 *
	 * @param theResourceId the ID of the resource to delete
	 */
	private void deleteResourceById(IIdType theResourceId) {
		@SuppressWarnings({"rawtypes"})
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		dao.delete(theResourceId, mySrd);
	}

	/**
	 * Helper method to modify a resource by adding a narrative and then update it.
	 * Uses text.div which all DomainResource subclasses support, making it work generically.
	 *
	 * @param theResource the resource to modify and update
	 */
	private void modifyResourceAndUpdate(IBaseResource theResource) {
		Narrative narrative = new Narrative();
		narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
		narrative.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">Updated after merge</div>");
		((DomainResource) theResource).setText(narrative);
		updateResource(theResource);
	}

	/**
	 * Helper method to get the first referencing resource ID from a scenario.
	 *
	 * @param theScenario the scenario to get the referencing resource from
	 * @return the ID of the first referencing resource
	 */
	private IIdType getFirstReferencingResourceId(AbstractMergeTestScenario<T> theScenario) {
		return theScenario.getAllReferencingResources().values().stream()
			.flatMap(List::stream)
			.findFirst()
			.orElseThrow();
	}

	protected void assertResourcesAreEqualIgnoringVersionAndLastUpdated(IBaseResource theBefore, IBaseResource theAfter) {
		// the resources should have the same versionless id
		assertThat(theBefore.getIdElement().toVersionless()).isEqualTo(theAfter.getIdElement().toVersionless());

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

	@Test
	void testUndoMerge_MissingRequiredParameters_400BadRequest() {
		Parameters params = new Parameters();
		callUndoWithParamsAndAssertException(params,
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
			HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + getResourceTypeName() + "/$hapi.fhir.undo-merge");
			post.addHeader("Content-Type", "application/fhir+json");
			// Send OperationOutcome (not Parameters) to trigger validation error
			post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(new OperationOutcome()), StandardCharsets.UTF_8));
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
		// Build parameters with both source-resource AND source-resource-identifier (conflicting)
		Parameters params = new Parameters();
		params.addParameter().setName("source-resource").setValue(new Reference(getResourceTypeName() + "/123"));
		params.addParameter().setName("source-resource-identifier").setValue(new Identifier().setSystem("sys").setValue("val"));
		params.addParameter().setName("target-resource").setValue(new Reference(getResourceTypeName() + "/456"));

		callUndoWithParamsAndAssertException(params,
			InvalidRequestException.class,
			400,
			"Source resource must be provided either by 'source-resource' or by 'source-resource-identifier'"
		);
	}

	@Test
	void testUndoMerge_BothTargetParametersProvided_400BadRequest() {
		// Build parameters with both target-resource AND target-resource-identifier (conflicting)
		Parameters params = new Parameters();
		params.addParameter().setName("source-resource").setValue(new Reference(getResourceTypeName() + "/123"));
		params.addParameter().setName("target-resource").setValue(new Reference(getResourceTypeName() + "/456"));
		params.addParameter().setName("target-resource-identifier").setValue(new Identifier().setSystem("sys").setValue("val"));

		callUndoWithParamsAndAssertException(params,
			InvalidRequestException.class,
			400,
			"Target resource must be provided either by 'target-resource' or by 'target-resource-identifier'"
		);
	}

	/**
	 * Calls undo-merge with custom parameters and asserts that it throws the expected exception.
	 * Uses the resource type from {@link #getResourceTypeName()} to call the correct endpoint.
	 */
	private void callUndoWithParamsAndAssertException(
			Parameters theParams,
			Class<? extends BaseServerResponseException> theExceptionClass,
			int theExpectedStatusCode,
			String theExpectedMessage) {
		callUndoWithParamsAndAssertException(theParams, theExceptionClass, theExpectedStatusCode, List.of(theExpectedMessage));
	}

	/**
	 * Calls undo-merge with custom parameters and asserts that it throws the expected exception.
	 * Uses the resource type from {@link #getResourceTypeName()} to call the correct endpoint.
	 */
	private void callUndoWithParamsAndAssertException(
			Parameters theParams,
			Class<? extends BaseServerResponseException> theExceptionClass,
			int theExpectedStatusCode,
			List<String> theExpectedMessages) {
		Exception ex = catchException(() -> myHelper.callUndoMergeOperation(getResourceTypeName(), theParams));
		assertExceptionMatchesExpected(ex, theExceptionClass, theExpectedStatusCode, theExpectedMessages);
	}

	/**
	 * Calls undo-merge using the scenario's parameters and asserts that it throws the expected exception.
	 */
	private void callUndoOnScenarioAndAssertException(
			AbstractMergeTestScenario<T> theScenario,
			Class<? extends BaseServerResponseException> theExceptionClass,
			int theExpectedStatusCode,
			String theExpectedMessage) {
		callUndoOnScenarioAndAssertException(theScenario, theExceptionClass, theExpectedStatusCode, List.of(theExpectedMessage));
	}

	/**
	 * Calls undo-merge using the scenario's parameters and asserts that it throws the expected exception.
	 */
	private void callUndoOnScenarioAndAssertException(
			AbstractMergeTestScenario<T> theScenario,
			Class<? extends BaseServerResponseException> theExceptionClass,
			int theExpectedStatusCode,
			List<String> theExpectedMessages) {
		Exception ex = catchException(theScenario::callUndoMergeOperation);
		assertExceptionMatchesExpected(ex, theExceptionClass, theExpectedStatusCode, theExpectedMessages);
	}

	/**
	 * Common helper to verify exception type, status code, and messages.
	 */
	private void assertExceptionMatchesExpected(
			Exception theException,
			Class<? extends BaseServerResponseException> theExceptionClass,
			int theExpectedStatusCode,
			List<String> theExpectedMessages) {
		assertThat(theException).isInstanceOf(theExceptionClass);
		BaseServerResponseException baseEx = (BaseServerResponseException) theException;
		assertThat(baseEx.getStatusCode()).isEqualTo(theExpectedStatusCode);
		String messages = ReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(myFhirContext, baseEx);
		theExpectedMessages.forEach(m -> assertThat(messages).contains(m));
	}

	protected void validateSuccessOutcome(Parameters theOutParams, int theExpectedResourceCount) {
		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) theOutParams.getParameter("outcome").getResource();
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
				String detailsTxt = issue.getDetails().getText();
				assertThat(detailsTxt).matches(format("Successfully restored %d resources to their previous versions based on the Provenance resource: Provenance/[0-9]+/_history/1", theExpectedResourceCount));
			});
	}

}
