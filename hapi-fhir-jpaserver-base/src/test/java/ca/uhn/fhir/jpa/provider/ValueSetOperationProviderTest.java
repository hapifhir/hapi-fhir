package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetResultJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider.PARAM_JOB_INSTANCE_ID;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_INVALIDATE_EXPANSION;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_INVALIDATE_EXPANSION_POLL_FOR_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValueSetOperationProviderTest {

	public static final String VS_URL = "http://vs";
	public static final String VS_VERSION = "1.0";
	public static final String INSTANCE_ID = "123";

	@Spy
	private final FhirContext myFhirContext = FhirContext.forR5Cached();

	@Mock
	IJobCoordinator myJobCoordinator;

	@InjectMocks
	ValueSetOperationProvider mySvc;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(myFhirContext)
		.withServer(t->t.registerProvider(mySvc));

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@Test
	void testInvalidateExpansion_ByUrlAndVersion() {

		// Setup

		when(myJobCoordinator.startInstance(any(), any())).thenReturn(new Batch2JobStartResponse().setInstanceId(INSTANCE_ID));

		// Execute

		MethodOutcome outcome = myServer
			.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_INVALIDATE_EXPANSION)
			.withParameter(Parameters.class, ValueSetOperationProvider.PARAM_URL, new UriType(VS_URL))
			.andParameter(ValueSetOperationProvider.PARAM_VERSION, new StringType(VS_VERSION))
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();

		// Verify

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals("$invalidate-expansion job has been accepted. Poll for status at the following URL: http://localhost:" + myServer.getPort() + "/ValueSet/$hapi.fhir.invalidate-expansion.poll-for-status?jobInstanceId=123", oo.getIssueFirstRep().getDiagnostics());
		assertThat(oo.getIssue()).hasSize(1);

		assertEquals("http://localhost:" + myServer.getPort() + "/ValueSet/$hapi.fhir.invalidate-expansion.poll-for-status?jobInstanceId=123", outcome.getFirstResponseHeader(Constants.HEADER_CONTENT_LOCATION).orElseThrow());

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		assertEquals(PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET, myStartRequestCaptor.getValue().getJobDefinitionId());
		assertEquals(VS_URL, myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getUrl());
		assertEquals(VS_VERSION, myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getVersion());
		assertNull(myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getId());
	}

	@Test
	void testInvalidateExpansion_NoPreferHeader() {

		// Setup

		when(myJobCoordinator.startInstance(any(), any())).thenReturn(new Batch2JobStartResponse().setInstanceId(INSTANCE_ID));

		// Execute

		MethodOutcome outcome = myServer
			.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_INVALIDATE_EXPANSION)
			.withParameter(Parameters.class, ValueSetOperationProvider.PARAM_URL, new UriType(VS_URL))
			.andParameter(ValueSetOperationProvider.PARAM_VERSION, new StringType(VS_VERSION))
			.returnMethodOutcome()
			.execute();

		// Verify

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals("This method should be invoked with the Prefer: respond-async header. Proceeding anyway.", oo.getIssue().get(1).getDiagnostics());
		assertThat(oo.getIssue()).hasSize(2);

		assertEquals("http://localhost:" + myServer.getPort() + "/ValueSet/$hapi.fhir.invalidate-expansion.poll-for-status?jobInstanceId=123", outcome.getFirstResponseHeader(Constants.HEADER_CONTENT_LOCATION).orElseThrow());

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		assertEquals(PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET, myStartRequestCaptor.getValue().getJobDefinitionId());
		assertEquals(VS_URL, myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getUrl());
		assertEquals(VS_VERSION, myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getVersion());
		assertNull(myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getId());
	}

	@Test
	void testInvalidateExpansion_ById() {

		// Setup

		when(myJobCoordinator.startInstance(any(), any())).thenReturn(new Batch2JobStartResponse().setInstanceId(INSTANCE_ID));

		// Execute

		MethodOutcome outcome = myServer
			.getFhirClient()
			.operation()
			.onInstance("ValueSet/123")
			.named(OPERATION_INVALIDATE_EXPANSION)
			.withNoParameters(Parameters.class)
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();

		// Verify

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals("$invalidate-expansion job has been accepted. Poll for status at the following URL: http://localhost:" + myServer.getPort() + "/ValueSet/$hapi.fhir.invalidate-expansion.poll-for-status?jobInstanceId=123", oo.getIssueFirstRep().getDiagnostics());
		assertThat(oo.getIssue()).hasSize(1);

		assertEquals("http://localhost:" + myServer.getPort() + "/ValueSet/$hapi.fhir.invalidate-expansion.poll-for-status?jobInstanceId=123", outcome.getFirstResponseHeader(Constants.HEADER_CONTENT_LOCATION).orElseThrow());

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		assertEquals(PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET, myStartRequestCaptor.getValue().getJobDefinitionId());
		assertNull(myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getUrl());
		assertNull(myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getVersion());
		assertEquals("ValueSet/123", myStartRequestCaptor.getValue().getParameters(PreExpandValueSetParameters.class).getId());
	}

	@Test
	void testPollForStatus_InProgress() {

		// Setup

		JobInstance jobInstance = new JobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		jobInstance.setProgress(0.1);
		when(myJobCoordinator.getInstance(eq("123"))).thenReturn(jobInstance);

		// Test

		MethodOutcome outcome = myServer
			.getFhirClient()
			.operation()
			.onType("ValueSet")
			.named(OPERATION_INVALIDATE_EXPANSION_POLL_FOR_STATUS)
			.withParameter(Parameters.class, PARAM_JOB_INSTANCE_ID, new CodeType("123"))
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();

		// Verify

		assertEquals(Constants.STATUS_HTTP_202_ACCEPTED, outcome.getResponseStatusCode());
		assertEquals("$invalidate-expansion job has started and is in progress Overall progress: 10%.", ((OperationOutcome)outcome.getOperationOutcome()).getIssueFirstRep().getDiagnostics());
	}

	@Test
	void testPollForStatus_Complete() {

		// Setup

		PreExpandValueSetResultJson result = new PreExpandValueSetResultJson();
		result.setReport("This is the report text");

		JobInstance jobInstance = new JobInstance();
		jobInstance.setStatus(StatusEnum.COMPLETED);
		jobInstance.setReport(JsonUtil.serialize(result));
		when(myJobCoordinator.getInstance(eq("123"))).thenReturn(jobInstance);

		// Test

		MethodOutcome outcome = myServer
			.getFhirClient()
			.operation()
			.onType("ValueSet")
			.named(OPERATION_INVALIDATE_EXPANSION_POLL_FOR_STATUS)
			.withParameter(Parameters.class, PARAM_JOB_INSTANCE_ID, new CodeType("123"))
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.returnMethodOutcome()
			.execute();

		// Verify

		assertEquals(Constants.STATUS_HTTP_200_OK, outcome.getResponseStatusCode());
		Bundle resultBundle = (Bundle) outcome.getResource();
		String resultBundleString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resultBundle);
		assertThat(resultBundleString).contains("This is the report text");
	}

}
