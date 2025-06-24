package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.jena.base.Sys;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.SMALL_BATCH_SIZE;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
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
	ReplaceReferencesLargeTestData myLargeTestData;

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
		// we need to keep the version on Provenance.target fields to
		// verify that Provenance resources were saved with versioned target references
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myLargeTestData = new ReplaceReferencesLargeTestData(myDaoRegistry);
	}



	private void updateSourcePatientToIncrementItsVersion() {
		//update the source resource for it to have a different version than the target resource.
		//The reason is to test that when creating the ReplaceReferencesJobParameters object in async mode
		//we pass the correct versions for target and source.
		Patient srcPatient = myTestHelper.readPatient(myLargeTestData.getSourcePatientId());
		srcPatient.setActive(!srcPatient.getActive());
		String srcResourceVersion = myClient.update().resource(srcPatient).execute().getId().getVersionIdPart();
		assertThat(srcResourceVersion).isEqualTo("2");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences(boolean isAsync) {

		myLargeTestData.createTestResources();
		updateSourcePatientToIncrementItsVersion();

		Bundle patchResultBundle = executeReplaceReferences(myLargeTestData.getSourcePatientId(), myLargeTestData.getTargetPatientId(), isAsync);

		// validate
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES, RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED);
		// Check that the linked resources were updated
		myTestHelper.assertAllReferencesUpdated(myLargeTestData);
		myTestHelper.assertReplaceReferencesProvenance("2", "1", myLargeTestData, null);
	}

	@ParameterizedTest(name = "{index}: isAsync={0}, theAgentInterceptorReturnsMultipleAgents={1}")
	@CsvSource (value = {
		"false, false",
		"false, true",
		"true, false",
		"true, true",
	})
	void testReplaceReferences_WithProvenanceAgentInterceptor_Success(boolean theIsAsync, boolean theAgentInterceptorReturnsMultipleAgents) {
		myLargeTestData.createTestResources();
		List<IProvenanceAgent> agents = new ArrayList<>();
		agents.add(myTestHelper.createTestProvenanceAgent());
		if (theAgentInterceptorReturnsMultipleAgents) {
			agents.add(myTestHelper.createTestProvenanceAgent());
		}
		// this interceptor will be unregistered in @AfterEach of the base class, which unregisters all interceptors
		ReplaceReferencesTestHelper.registerProvenanceAgentInterceptor(myServer.getRestfulServer(), agents);

		Bundle patchResultBundle = executeReplaceReferences(myLargeTestData.getSourcePatientId(), myLargeTestData.getTargetPatientId(), theIsAsync);

		// validate
		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES, RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED);

		// Check that the linked resources were updated
		myTestHelper.assertAllReferencesUpdated(myLargeTestData);
		myTestHelper.assertReplaceReferencesProvenance("1", "1", myLargeTestData, agents);
	}


	@ParameterizedTest(name = "{index}: isAsync={0}")
	@CsvSource (value = {
		"false",
		"true"
	})
	void testReplaceReferences_withProvenanceAgentInterceptor_InterceptorReturnsNoAgent_ReturnsInternalError(boolean theIsAsync) {

		// this interceptor will be unregistered in @AfterEach of the base class, which unregisters all interceptors
		ReplaceReferencesTestHelper.registerProvenanceAgentInterceptor(myServer.getRestfulServer(), Collections.emptyList());
		IIdType srcId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless();
		IIdType targetId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless();
		assertThatThrownBy(() -> executeReplaceReferences(srcId, targetId, theIsAsync)
		).isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("HAPI-2723: No Provenance Agent was provided by any interceptor for Pointcut.PROVENANCE_AGENTS")
			.extracting(InternalErrorException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(500);
	}


	private Bundle executeReplaceReferences(IIdType theSourceId, IIdType theTargetId, boolean isAsync) {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(myClient,
			theSourceId,
			theTargetId,
			isAsync,
			null);

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
		return patchResultBundle;
	}


	private JobInstance awaitJobCompletion(Task task) {
		String jobId = myTestHelper.getJobIdFromTask(task);
		return myBatch2JobHelper.awaitJobCompletion(jobId);
	}

	@Test
	void testReplaceReferencesSmallResourceLimitSync() {
		myLargeTestData.createTestResources();
		assertThatThrownBy(() -> myTestHelper.callReplaceReferences(myClient,
			myLargeTestData.getSourcePatientId().toString(),
			myLargeTestData.getTargetPatientId().toString(),
			false,
			SMALL_BATCH_SIZE))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessage("HTTP 412 Precondition Failed: HAPI-2597: Number of resources with references to " + myLargeTestData.getSourcePatientId() + " exceeds the resource-limit 5. Submit the request asynchronsly by adding the HTTP Header 'Prefer: respond-async'.");
	}

	@Test
	void testReplaceReferencesSmallTransactionEntriesSize() {
		myLargeTestData.createTestResources();
		myStorageSettings.setDefaultTransactionEntriesForWrite(5);

		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(myClient,
			myLargeTestData.getSourcePatientId(),
			myLargeTestData.getTargetPatientId(),
			true,
			SMALL_BATCH_SIZE);

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
			assertThat(numberOfEntriesInCurrentBundle).isBetween(1, SMALL_BATCH_SIZE);
			ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, numberOfEntriesInCurrentBundle, RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED);
		}

		assertThat(totalPatchResultEntriesSeen).isEqualTo(TOTAL_EXPECTED_PATCHES);

		// Check that the linked resources were updated

		myTestHelper.assertAllReferencesUpdated(myLargeTestData);
		myTestHelper.assertReplaceReferencesProvenance("1", "1", myLargeTestData, null);
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testReplaceReferences_MissingSourceId_ThrowsInvalidRequestException(String theSourceId) {
		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myTestHelper.callReplaceReferences(myClient, theSourceId, "target-id", false, null);
		});
		assertThat(exception.getMessage()).contains("HAPI-2583: Parameter 'source-reference-id' is blank");
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void testReplaceReferences_MissingTargetId_ThrowsInvalidRequestException(String theTargetId) {
		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
			myTestHelper.callReplaceReferences(myClient, "source-id", theTargetId, false, null);
		});
		assertThat(exception.getMessage()).contains("HAPI-2584: Parameter 'target-reference-id' is blank");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences_NotExistingSourceId_ThrowsNotFoundException(boolean theIsAsync) {
		String nonExistingSourceId = "Patient/does-not-exist";
		String targetId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless().toString();
		assertThatThrownBy(() -> {
			myTestHelper.callReplaceReferences(myClient, nonExistingSourceId, targetId, theIsAsync, null);
		}).isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2001: Resource Patient/does-not-exist is not known");
	}


	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences_NotExistingTargetId_ThrowsNotFoundException(boolean theIsAsync) {
		String nonExistingTargetId = "Patient/does-not-exist";
		String sourceId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless().toString();
		assertThatThrownBy(() -> {
			myTestHelper.callReplaceReferences(myClient, nonExistingTargetId, sourceId, theIsAsync, null);
		}).isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("HAPI-2001: Resource Patient/does-not-exist is not known");
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
		Parameters outParams = myTestHelper.callReplaceReferences(myClient,
			practitionerId1.toString(),
			practitionerId3.toString(),
			false,
			null);

		// Assert operation outcome
		Bundle patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();

		ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle, 1, List.of( "Observation"));

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
			IIdType practitionerId1 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualified();
			IIdType practitionerId2 = myClient.create().resource(new Practitioner()).execute().getId().toUnqualified();

			Provenance provenance = new Provenance();
			provenance.addTarget(new Reference(practitionerId1));
			IIdType provenanceId = myClient.create().resource(provenance).execute().getId();
			// Call $replace-references operation to replace practitionerId1 with practitionerId2
			myTestHelper.callReplaceReferences(myClient,
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

			// Assert that the versioned reference in the Provenance was not replaced
			assertThat(actualTargetIds).containsExactly(practitionerId1.toString());
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

	@Test
	void testReplaceReferences_ResourceHasMultipleReferencesToTheSameResource() {
		IIdType srcPractitionerId = myClient.create().resource(new Practitioner()).execute().getId().toUnqualifiedVersionless();
		// Create a ServiceRequest where requester and performer refer to the practitioner
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setRequester(new Reference(srcPractitionerId));
		serviceRequest.addPerformer(new Reference(srcPractitionerId));
		IIdType serviceRequestId = myClient.create().resource(serviceRequest).execute().getId().toUnqualified();
		IIdType targetPractitionerId = myClient.create().resource(new Practitioner()).execute().getId().toUnqualified();

		// Call $replace-references operation to replace practitionerId1 with practitionerId3
		 myTestHelper.callReplaceReferences(myClient,
			srcPractitionerId.toVersionless().toString(),
			targetPractitionerId.toVersionless().toString(),
			false,
			null);

		// ensure that both references to the srcPractitionerId were replaced with the targetPractitionerId
		ServiceRequest updatedServiceReq = myClient.read().resource(ServiceRequest.class).withId(serviceRequestId.toVersionless()).execute();
		assertThat(updatedServiceReq.getRequester().getReference()).isEqualTo(targetPractitionerId.toVersionless().toString());
		assertThat(updatedServiceReq.getPerformer().get(0).getReference()).isEqualTo(targetPractitionerId.toVersionless().toString());
		// the ServiceRequest resource should have been updated only once, so it should have version 2
		assertThat(updatedServiceReq.getIdElement().getVersionIdPart()).isEqualTo("2");

		myTestHelper.assertReplaceReferencesProvenance(
			srcPractitionerId.withVersion("1"),
			targetPractitionerId.withVersion("1"),
			1,
			Set.of(serviceRequestId.withVersion("2").toString()),
			null
		);
	}


	@ParameterizedTest
	@CsvSource (value = {
		"false",
		"true"
	})
	void testReplaceReferences_SourceResourceNotReferencedByAnyResource_SucceedAndShouldNotCreateAProvenance(boolean theIsAsync) {

		IIdType sourcePatientId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless();
		IIdType targetPatientId = myPatientDao.create(new Patient(), mySrd).getId().toUnqualifiedVersionless();

		Parameters outParams = myTestHelper.callReplaceReferences(myClient,
			sourcePatientId,
			targetPatientId,
			theIsAsync,
			null);

		if (theIsAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
			awaitJobCompletion(task);
		}

		List<IBaseResource> provenances = myTestHelper.searchProvenance(targetPatientId.toString());
		assertThat(provenances).isEmpty();
	}

	@Override
	protected boolean verboseClientLogging() {
		return true;
	}
}
