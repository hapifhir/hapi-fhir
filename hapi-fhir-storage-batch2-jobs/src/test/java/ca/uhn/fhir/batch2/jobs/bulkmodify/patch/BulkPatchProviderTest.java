package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobAppCtx;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.eclipse.jetty.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkPatchProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private static final BulkPatchProvider ourProvider = new BulkPatchProvider();
	private static final String MY_INSTANCE_ID = "MY-INSTANCE-ID";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkPatchProviderTest.class);
	@RegisterExtension
	static RestfulServerExtension ourFhirServer = new RestfulServerExtension(ourCtx)
		.keepAliveBetweenTests()
		.registerProvider(ourProvider);
	@RegisterExtension
	static HttpClientExtension ourHttpClient = new HttpClientExtension();
	@Mock
	private IJobCoordinator myJobCoordinator;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@BeforeEach
	void beforeEach() {
		ourProvider.setContextForUnitTest(ourCtx);
		ourProvider.setJobCoordinatorForUnitTest(myJobCoordinator);
	}

	@Test
	void testInitiateJob() throws IOException {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		Parameters patch = new Parameters();
		patch.setId("PATCH");

		Parameters request = new Parameters();
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH)
			.setResource(patch);
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Patient?"));
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Location?"));

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + JpaConstants.OPERATION_BULK_PATCH;
		HttpPost post = new HttpPost(url);
		post.setEntity(new ResourceEntity(ourCtx, request));
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {

			// Verify
			String expectedUrl = ourFhirServer.getBaseUrl() + "/$bulk-patch-status?_jobId=MY-INSTANCE-ID";
			assertEquals(HttpStatus.Code.ACCEPTED.getCode(), response.getStatusLine().getStatusCode());
			assertEquals(expectedUrl, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());

		}

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		assertEquals(BulkPatchJobAppCtx.JOB_ID, startRequest.getJobDefinitionId());
		BulkPatchJobParameters jobParameters = startRequest.getParameters(BulkPatchJobParameters.class);
		IBaseResource fhirPatch = jobParameters.getFhirPatch(ourCtx);
		assertNotNull(fhirPatch);
		assertEquals(Parameters.class, fhirPatch.getClass());
		assertEquals("Parameters/PATCH", fhirPatch.getIdElement().getValue());
		assertThat(jobParameters.getUrls()).containsExactly("Patient?", "Location?");
	}

	@ParameterizedTest
	@MethodSource("testPollForStatusParameters")
	void testPollForStatus(PollForStatusTest theParams) throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setStatus(theParams.jobStatus);
		instance.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		instance.setErrorMessage(theParams.errorMessage());
		instance.setReport(theParams.reportMessage());
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID";
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

			if (theParams.expectBundleResponse()) {
				assertEquals(200, response.getStatusLine().getStatusCode());
				Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseString);
				ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
				assertEquals("batch-response", bundle.getType().toCode());
				assertEquals(1, bundle.getEntry().size());

				OperationOutcome oo = (OperationOutcome) bundle.getEntry().get(0).getResponse().getOutcome();
				assertThat(bundle.getEntry().get(0).getResponse().getStatus()).startsWith(theParams.expectedStatusCode() + " ");
				assertEquals(theParams.expectedOoMessage(), oo.getIssueFirstRep().getDiagnostics());

			} else {
				assertEquals(theParams.expectedStatusCode(), response.getStatusLine().getStatusCode());

				OperationOutcome oo = ourCtx.newJsonParser().parseResource(OperationOutcome.class, responseString);
				ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

				assertEquals(theParams.expectedOoMessage(), oo.getIssueFirstRep().getDiagnostics());
			}

			if (theParams.expectedProgressHeaderValue() != null) {
				assertEquals(theParams.expectedProgressHeaderValue(), response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue());
			} else {
				assertNull(response.getFirstHeader(Constants.HEADER_X_PROGRESS));
			}
		}
	}

	@Test
	void testPollForStatus_WrongJobType() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setStatus(StatusEnum.COMPLETED);
		instance.setJobDefinitionId(BulkPatchRewriteJobAppCtx.JOB_ID);
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID";
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(400, response.getStatusLine().getStatusCode());

			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			OperationOutcome oo = ourCtx.newJsonParser().parseResource(OperationOutcome.class, responseString);
			ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

			assertEquals("HAPI-1769: Job ID does not correspond to a $bulk-patch job", oo.getIssueFirstRep().getDiagnostics());
			assertNull(response.getFirstHeader(Constants.HEADER_X_PROGRESS));
		}
	}

	@Test
	void testPollForStatus_UnknownJob() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setStatus(StatusEnum.COMPLETED);
		instance.setJobDefinitionId(BulkPatchRewriteJobAppCtx.JOB_ID);
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenThrow(new ResourceNotFoundException("This is a message"));

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID";
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(Constants.STATUS_HTTP_404_NOT_FOUND, response.getStatusLine().getStatusCode());

			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			OperationOutcome oo = ourCtx.newJsonParser().parseResource(OperationOutcome.class, responseString);
			ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

			assertEquals("HAPI-2787: Invalid/unknown job ID: MY-INSTANCE-ID", oo.getIssueFirstRep().getDiagnostics());
			assertNull(response.getFirstHeader(Constants.HEADER_X_PROGRESS));
		}
	}

	public static Stream<PollForStatusTest> testPollForStatusParameters() {
		BulkModifyResourcesResultsJson successReportJson = new BulkModifyResourcesResultsJson();
		successReportJson.setReport("This is the report\nLine 2 or report");
		String successReport = JsonUtil.serialize(successReportJson);

		return Stream.of(
			new PollForStatusTest(StatusEnum.QUEUED, HttpStatus.Code.ACCEPTED.getCode(), "$bulk-patch job has not yet started"),
			new PollForStatusTest(StatusEnum.IN_PROGRESS, HttpStatus.Code.ACCEPTED.getCode(), "$bulk-patch job has started and is in progress"),
			new PollForStatusTest(StatusEnum.FINALIZE, HttpStatus.Code.ACCEPTED.getCode(), "$bulk-patch job has started and is being finalized"),
			new PollForStatusTest(StatusEnum.CANCELLED, null, null, HttpStatus.Code.OK.getCode(), "$bulk-patch job has been cancelled", "$bulk-patch job has been cancelled", true),
			new PollForStatusTest(StatusEnum.FAILED, "This is an error message", null, HttpStatus.Code.INTERNAL_SERVER_ERROR.getCode(), "$bulk-patch job has failed with error: This is an error message", "$bulk-patch job has failed with error: This is an error message", true),
			new PollForStatusTest(StatusEnum.COMPLETED, null, null, HttpStatus.Code.OK.getCode(), "$bulk-patch job has completed successfully", "$bulk-patch job has completed successfully", true),
			new PollForStatusTest(StatusEnum.COMPLETED, null, successReport, HttpStatus.Code.OK.getCode(), successReportJson.getReport(), "$bulk-patch job has completed successfully", true)
		);
	}

	public record PollForStatusTest(StatusEnum jobStatus, String errorMessage, String reportMessage, int expectedStatusCode, String expectedOoMessage, String expectedProgressHeaderValue, boolean expectBundleResponse) {

		PollForStatusTest(StatusEnum jobStatus, int expectedStatusCode, String expectedMessage) {
			this(jobStatus, null, null, expectedStatusCode, expectedMessage, expectedMessage, false);
		}

	}


}
