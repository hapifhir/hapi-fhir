package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.hapi.fhir.cdshooks.api.CdsPrefetchFailureMode;
import ca.uhn.hapi.fhir.cdshooks.api.CdsResolutionStrategyEnum;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CdsPrefetchSvcTest {

	@Mock
	private CdsResolutionStrategySvc myCdsResolutionStrategySvc;
	@Mock
	private CdsPrefetchDaoSvc myCdsPrefetchDaoSvc;
	@Mock
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;
	@Mock
	private ICdsHooksDaoAuthorizationSvc myCdsHooksDaoAuthorizationSvc;
	@Spy
	private FhirContext myFhirContext = FhirContext.forR4();
	@Mock
	private ICdsServiceMethod myServiceMethodMock;

	@InjectMocks
	private CdsPrefetchSvc myCdsPrefetchSvc;

	@Test
	void testFindMissingPrefetch() {
		Set<String> result;
		CdsServiceJson spec = new CdsServiceJson();
		CdsServiceRequestJson input = new CdsServiceRequestJson();

		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).hasSize(0);

		spec.addPrefetch("foo", "fooval");
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("foo");

		input.addPrefetch("foo", new Patient());
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).hasSize(0);

		spec.addPrefetch("bar", "barval");
		spec.addPrefetch("baz", "bazval");
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("bar", "baz");

		/**
		 * From the Spec:
		 *
		 * The CDS Client MUST NOT send any prefetch template key that it chooses not to satisfy. Similarly, if the CDS Client
		 * encounters an error while prefetching any data, the prefetch template key MUST NOT be sent to the CDS Service. If the
		 * CDS Client has no data to populate a template prefetch key, the prefetch template key MUST have a value of null. Note
		 * that the null result is used rather than a bundle with zero entries to account for the possibility that the prefetch
		 * url is a single-resource request.
		 *
		 */

		// Per the spec above, null is not considered missing
		input.addPrefetch("baz", null);
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("bar");
	}


	protected static Stream<Arguments> provideArgumentsForAllPrefetchFailureModes() {
		return Stream.of(
			Arguments.of(CdsPrefetchFailureMode.OPERATION_OUTCOME, CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsPrefetchFailureMode.OMIT, CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsPrefetchFailureMode.FAIL, CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsPrefetchFailureMode.OPERATION_OUTCOME, CdsResolutionStrategyEnum.DAO),
			Arguments.of(CdsPrefetchFailureMode.OMIT, CdsResolutionStrategyEnum.DAO),
			Arguments.of(CdsPrefetchFailureMode.FAIL, CdsResolutionStrategyEnum.DAO)
		);
	}


	protected static Stream<Arguments> provideArgumentsForNonFailingPrefetchFailureModes() {
		return Stream.of(
			Arguments.of(CdsPrefetchFailureMode.OPERATION_OUTCOME, CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsPrefetchFailureMode.OMIT, CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsPrefetchFailureMode.OPERATION_OUTCOME, CdsResolutionStrategyEnum.DAO),
			Arguments.of(CdsPrefetchFailureMode.OMIT, CdsResolutionStrategyEnum.DAO)
			);
	}


	@ParameterizedTest
	@MethodSource("provideArgumentsForNonFailingPrefetchFailureModes")
	void testAugmentRequest_WhenPrefetchFailureModeIsNonFailing_ClientSendsOperationOutcome_Succeeds(CdsPrefetchFailureMode theFailureMode,
																					  CdsResolutionStrategyEnum theResolutionStrategy) {

		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(theFailureMode, theResolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();
		// client sends an OperationOutcome for a prefetch
		OperationOutcome ooInPrefetch = new OperationOutcome();
		request.addPrefetch("patient", ooInPrefetch);

		when(myServiceMethodMock.getCdsServiceJson()).thenReturn(spec);

		myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);

		if (theFailureMode == CdsPrefetchFailureMode.OPERATION_OUTCOME) {
			IBaseResource prefetchedResource = request.getPrefetch("patient");
			assertThat(prefetchedResource).isInstanceOf(IBaseOperationOutcome.class);
			OperationOutcome prefetchedResourceAsOperationOutcome = (OperationOutcome) prefetchedResource;
			assertThat(prefetchedResourceAsOperationOutcome).isEqualTo(ooInPrefetch);
		}
		else if (theFailureMode == CdsPrefetchFailureMode.OMIT) {
			assertThat(request.getPrefetchKeys()).doesNotContain("patient");
		}

		verifyNoInteractions(myCdsPrefetchFhirClientSvc, myCdsPrefetchDaoSvc);
	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForNonFailingPrefetchFailureModes")
	void testAugmentRequest_WhenPrefetchFailureModeIsNonFailing_PrefetchingMissingKeyFails_Succeeds(CdsPrefetchFailureMode theFailureMode,
																					  CdsResolutionStrategyEnum theResolutionStrategy) {

		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(theFailureMode, theResolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();

		RuntimeException ex = new RuntimeException("this is the failure message");

		setupMocksForFailureModeTest(request, spec, theResolutionStrategy, ex);

		myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);

		if (theFailureMode == CdsPrefetchFailureMode.OPERATION_OUTCOME) {
			IBaseResource prefetchedResource = request.getPrefetch("patient");
			assertThat(prefetchedResource).isInstanceOf(IBaseOperationOutcome.class);
			OperationOutcome prefetchedResourceAsOperationOutcome = (OperationOutcome) prefetchedResource;
			assertThat(prefetchedResourceAsOperationOutcome.getIssue()).hasSize(1);
			OperationOutcome.OperationOutcomeIssueComponent ooIssue = prefetchedResourceAsOperationOutcome.getIssueFirstRep();
			assertThat(ooIssue.getDiagnostics()).isEqualTo("this is the failure message");
			assertThat(ooIssue.getCode()).isEqualTo(OperationOutcome.IssueType.EXCEPTION);
			assertThat(ooIssue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
		}
		else if (theFailureMode == CdsPrefetchFailureMode.OMIT) {
			assertThat(request.getPrefetchKeys()).doesNotContain("patient");
		}
	}

	@Test
	void testAugmentRequest_WhenPrefetchFailureModeIsOperationOutcome_And_PrefetchingMissingKeyFailsWithAnExceptionContainingOperationOutcome_Succeeds() {

		CdsResolutionStrategyEnum resolutionStrategy = CdsResolutionStrategyEnum.FHIR_CLIENT;
		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(CdsPrefetchFailureMode.OPERATION_OUTCOME, resolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();


		OperationOutcome operationOutcomeInException = new OperationOutcome();
		BaseServerResponseException ex =  new ResourceNotFoundException(new IdDt(123), operationOutcomeInException);

		setupMocksForFailureModeTest(request, spec, resolutionStrategy, ex);

		myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);

		IBaseResource prefetchedResource = request.getPrefetch("patient");
		assertThat(prefetchedResource).isInstanceOf(IBaseOperationOutcome.class);
		OperationOutcome prefetchedResourceAsOperationOutcome = (OperationOutcome) prefetchedResource;
		assertThat(prefetchedResourceAsOperationOutcome).isEqualTo(operationOutcomeInException);
	}

	protected static Stream<Arguments> provideArgumentsForAutoFetchingResolutionStrategies() {
		return Stream.of(
			Arguments.of(CdsResolutionStrategyEnum.FHIR_CLIENT),
			Arguments.of(CdsResolutionStrategyEnum.DAO)
		);
	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForAutoFetchingResolutionStrategies")
	void testAugmentRequest_WhenPrefetchFailureModeIsFail_ClientSendsOperationOutcome_ThrowsException(CdsResolutionStrategyEnum theResolutionStrategy) {

		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(CdsPrefetchFailureMode.FAIL, theResolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();
		request.addPrefetch("patient", new OperationOutcome());
		when(myServiceMethodMock.getCdsServiceJson()).thenReturn(spec);


		PreconditionFailedException exceptionThrown = assertThrows(PreconditionFailedException.class, () -> myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock));
		assertThat(exceptionThrown.getMessage()).isEqualTo("HAPI-2635: The CDS service 'test_failure_mode_service_id' received an OperationOutcome resource for prefetch key 'patient' but the service isn't configured to handle OperationOutcome");
	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForAllPrefetchFailureModes")
	void testAugmentRequest_WhenPrefetchFailureModeIsFail_ClientSendsPatientResource_Succeeds(CdsPrefetchFailureMode theFailureMode, CdsResolutionStrategyEnum theResolutionStrategy) {

		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(theFailureMode, theResolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();
		Patient p = new Patient();
		request.addPrefetch("patient", p);
		when(myServiceMethodMock.getCdsServiceJson()).thenReturn(spec);


		 myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);
		 assertThat(request.getPrefetch("patient")).isEqualTo(p);
	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForAutoFetchingResolutionStrategies")
	void testAugmentRequest_WhenPrefetchFailureModeIsFail_PrefetchingMissingKeyFails_ThrowsException(CdsResolutionStrategyEnum theResolutionStrategy) {

		CdsServiceJson spec = createServiceDefinitionForFailureModeTest(CdsPrefetchFailureMode.FAIL, theResolutionStrategy);
		CdsServiceRequestJson request = createRequestForFailureModeTest();

		RuntimeException ex = new RuntimeException("this is the failure message");

		setupMocksForFailureModeTest(request, spec, theResolutionStrategy, ex);

		RuntimeException exceptionThrown = assertThrows(RuntimeException.class, () -> myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock));
		assertThat(exceptionThrown).isEqualTo(ex);

	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForAutoFetchingResolutionStrategies")
	void testAugmentRequest_MultiplePrefetchKeysWithDifferentNonFailingModes_PrefetchingMissingKeysFails_Succeeds(CdsResolutionStrategyEnum theResolutionStrategy) {
		CdsServiceJson spec = new CdsServiceJson();
		spec.setId("test_failure_mode_service_id");
		spec.setHook("test_failure_mode_hook");

		spec.addPrefetchFailureMode("patient1", CdsPrefetchFailureMode.OMIT);
		spec.addPrefetch("patient1", "Patient/{{context.patientId1}}");
		spec.addSource("patient1", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient2", CdsPrefetchFailureMode.OPERATION_OUTCOME);
		spec.addPrefetch("patient2", "Patient/{{context.patientId2}}");
		spec.addSource("patient2", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient3", CdsPrefetchFailureMode.OMIT);
		spec.addPrefetch("patient3", "Patient/{{context.patientId3}}");
		spec.addSource("patient3", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient4", CdsPrefetchFailureMode.OPERATION_OUTCOME);
		spec.addPrefetch("patient4", "Patient/{{context.patientId4}}");
		spec.addSource("patient4", theResolutionStrategy);

		CdsServiceRequestContextJson reqContext = new CdsServiceRequestContextJson();
		reqContext.put("patientId1", "p1");
		reqContext.put("patientId2", "p2");
		reqContext.put("patientId3", "p3");
		reqContext.put("patientId4", "p4");

		CdsServiceRequestJson request = createRequestForFailureModeTest(reqContext);
		RuntimeException ex = new RuntimeException("this is the failure message");
		setupMocksForFailureModeTest(request, spec, theResolutionStrategy, ex);

		myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);

		assertThat(request.getPrefetchKeys()).doesNotContain("patient1", "patient3");
		assertThat(request.getPrefetch("patient2")).isInstanceOf(IBaseOperationOutcome.class);
		assertThat(request.getPrefetch("patient4")).isInstanceOf(IBaseOperationOutcome.class);

	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForAutoFetchingResolutionStrategies")
	void testAugmentRequest_MultiplePrefetchKeysWithDifferentNonFailingModes_ClientSendsPrefetchResources_Succeeds(CdsResolutionStrategyEnum theResolutionStrategy) {
		CdsServiceJson spec = new CdsServiceJson();
		spec.setId("test_failure_mode_service_id");
		spec.setHook("test_failure_mode_hook");

		spec.addPrefetchFailureMode("patient1", CdsPrefetchFailureMode.OMIT);
		spec.addPrefetch("patient1", "Patient/{{context.patientId1}}");
		spec.addSource("patient1", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient2", CdsPrefetchFailureMode.OPERATION_OUTCOME);
		spec.addPrefetch("patient2", "Patient/{{context.patientId2}}");
		spec.addSource("patient2", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient3", CdsPrefetchFailureMode.OMIT);
		spec.addPrefetch("patient3", "Patient/{{context.patientId3}}");
		spec.addSource("patient3", theResolutionStrategy);

		spec.addPrefetchFailureMode("patient4", CdsPrefetchFailureMode.OPERATION_OUTCOME);
		spec.addPrefetch("patient4", "Patient/{{context.patientId4}}");
		spec.addSource("patient4", theResolutionStrategy);

		CdsServiceRequestContextJson reqContext = new CdsServiceRequestContextJson();
		reqContext.put("patientId1", "p1");
		reqContext.put("patientId2", "p2");
		reqContext.put("patientId3", "p3");
		reqContext.put("patientId4", "p4");

		CdsServiceRequestJson request = createRequestForFailureModeTest(reqContext);
		request.addPrefetch("patient1", new OperationOutcome());
		request.addPrefetch("patient2", new OperationOutcome());
		request.addPrefetch("patient3", new OperationOutcome());
		request.addPrefetch("patient4", new Patient());

		when(myServiceMethodMock.getCdsServiceJson()).thenReturn(spec);
		myCdsPrefetchSvc.augmentRequest(request, myServiceMethodMock);

		assertThat(request.getPrefetchKeys()).doesNotContain("patient1", "patient3");
		assertThat(request.getPrefetch("patient2")).isInstanceOf(IBaseOperationOutcome.class);
		assertThat(request.getPrefetch("patient4")).isInstanceOf(Patient.class);

	}

	private CdsServiceJson createServiceDefinitionForFailureModeTest(CdsPrefetchFailureMode theFailureMode,
																	 CdsResolutionStrategyEnum theResolutionStrategy) {
		CdsServiceJson spec = new CdsServiceJson();
		spec.setId("test_failure_mode_service_id");
		spec.addPrefetchFailureMode("patient", theFailureMode);
		spec.addPrefetch("patient", "Patient/{{context.patientId}}");
		spec.addSource("patient", theResolutionStrategy);
		spec.setHook("test_failure_mode_hook");
		return spec;
	}

	private CdsServiceRequestJson createRequestForFailureModeTest() {
		CdsServiceRequestContextJson requestContext = new CdsServiceRequestContextJson();
		requestContext.put("patientId", "123");
		return createRequestForFailureModeTest(requestContext);
	}

	private CdsServiceRequestJson createRequestForFailureModeTest(CdsServiceRequestContextJson theRequestContext) {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHook("test_failure_mode_hook");
		request.setFhirServer("http://example-fhir.com/fhir");
		request.setHookInstance("test_failure_mode_hook_instance");
		request.setContext(theRequestContext);
		return request;
	}

	private void setupMocksForFailureModeTest(CdsServiceRequestJson theRequest,
											  CdsServiceJson theServiceDefinition,
											  CdsResolutionStrategyEnum theResolutionStrategy,
											  Exception theException) {
		when(myCdsResolutionStrategySvc.determineResolutionStrategy(myServiceMethodMock, theRequest)).thenReturn(Set.of(theResolutionStrategy));
		when(myServiceMethodMock.getCdsServiceJson()).thenReturn(theServiceDefinition);
		if (theResolutionStrategy == CdsResolutionStrategyEnum.FHIR_CLIENT) {
			when(myCdsPrefetchFhirClientSvc.resourceFromUrl(eq(theRequest), any(String.class))).thenThrow(theException);
		}
		else if (theResolutionStrategy == CdsResolutionStrategyEnum.DAO) {
			when(myCdsPrefetchDaoSvc.resourceFromUrl(any(String.class))).thenThrow(theException);
		}
	}
}
