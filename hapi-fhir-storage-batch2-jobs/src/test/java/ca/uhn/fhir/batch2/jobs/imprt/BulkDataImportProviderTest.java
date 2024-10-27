package ca.uhn.fhir.batch2.jobs.imprt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UrlType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(value = MethodOrderer.MethodName.class)
public class BulkDataImportProviderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportProviderTest.class);
	private static final String A_JOB_ID = "0000000-A1A1A1A1";
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private final BulkDataImportProvider myProvider = new BulkDataImportProvider();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx, myProvider);
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	@Mock
	private IJobCoordinator myJobCoordinator;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;
	@Spy
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc = new MyRequestPartitionHelperSvc();
	private final RequestPartitionId myRequestPartitionId = RequestPartitionId.fromPartitionIdAndName(123, "Partition-A");
	private final String myPartitionName = "Partition-A";

	@BeforeEach
	public void beforeEach() {
		myProvider.setFhirContext(myCtx);
		myProvider.setJobCoordinator(myJobCoordinator);
		myProvider.setRequestPartitionHelperService(myRequestPartitionHelperSvc);
	}

	public void enablePartitioning() {
		myRestfulServerExtension.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
	}

	private static Stream<Arguments> provideParameters() {
		return Stream.of(
			Arguments.of(UrlType.class, false),
			Arguments.of(UriType.class, false),
			Arguments.of(UrlType.class, true),
			Arguments.of(UriType.class, true)
		);
	}

	@ParameterizedTest
	@MethodSource("provideParameters")
	public void testStartWithPartitioning_Success(Class<?> type, boolean partitionEnabled) throws IOException {
		// Setup
		Parameters input = createRequest(type);
		ourLog.debug("Input: {}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		String jobId = UUID.randomUUID().toString();
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(jobId);
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(startResponse);

		String requestUrl;
		if (partitionEnabled) {
			enablePartitioning();
			requestUrl = myRestfulServerExtension.getBaseUrl() + "/" + myPartitionName + "/";
		} else {
			requestUrl = myRestfulServerExtension.getBaseUrl() + "/";
		}
		String url = requestUrl + JpaConstants.OPERATION_IMPORT;
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		// Execute

		try (CloseableHttpResponse response = myClient.getClient().execute(post)) {
			ourLog.info("Response: {}", response);
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);

			// Verify

			assertEquals(202, response.getStatusLine().getStatusCode());

			OperationOutcome oo = myCtx.newJsonParser().parseResource(OperationOutcome.class, resp);
			assertEquals("Bulk import job has been submitted with ID: " + jobId, oo.getIssue().get(0).getDiagnostics());
			assertEquals("Use the following URL to poll for job status: " + requestUrl + "$import-poll-status?_jobId=" + jobId, oo.getIssue().get(1).getDiagnostics());
		}

		verify(myJobCoordinator, times(1)).startInstance(isNotNull(), myStartRequestCaptor.capture());

		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		ourLog.info("Parameters: {}", startRequest.getParameters());
		assertTrue(startRequest.getParameters().startsWith("{\"ndJsonUrls\":[\"http://example.com/Patient\",\"http://example.com/Observation\"],\"httpBasicCredentials\":\"admin:password\",\"maxBatchResourceCount\":500,\"partitionId\":{\"allPartitions\":false"));
	}

	@Test
	public void testStart_NoAsyncHeader() throws IOException {
		// Setup

		Parameters input = createRequest();

		String url = myRestfulServerExtension.getBaseUrl() + "/" + JpaConstants.OPERATION_IMPORT;
		HttpPost post = new HttpPost(url);
		post.setEntity(new ResourceEntity(myCtx, input));

		// Execute

		try (CloseableHttpResponse response = myClient.getClient().execute(post)) {
			ourLog.info("Response: {}", response);
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);

			// Verify

			assertEquals(400, response.getStatusLine().getStatusCode());
			assertEquals("application/fhir+json;charset=utf-8", response.getEntity().getContentType().getValue());
			assertThat(resp).contains("\"resourceType\": \"OperationOutcome\"");
			assertThat(resp).contains("HAPI-0513: Must request async processing for $import");
		}

	}

	@Test
	public void testStart_NoUrls() throws IOException {
		// Setup

		Parameters input = createRequest();
		input
			.getParameter()
			.removeIf(t -> t.getName().equals(BulkDataImportProvider.PARAM_INPUT));
		String url = myRestfulServerExtension.getBaseUrl() + "/" + JpaConstants.OPERATION_IMPORT;
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		// Execute

		try (CloseableHttpResponse response = myClient.getClient().execute(post)) {
			ourLog.info("Response: {}", response);
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);

			// Verify

			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(resp).contains("HAPI-1769: No URLs specified");
		}

	}

	@Nonnull
	Parameters createRequest() {
		return createRequest(UriType.class);
	}

	@Nonnull
	private Parameters createRequest(Class<?> type) {
		Parameters input = new Parameters();
		input.addParameter(BulkDataImportProvider.PARAM_INPUT_FORMAT, new CodeType(Constants.CT_FHIR_NDJSON));
		input.addParameter(BulkDataImportProvider.PARAM_INPUT_SOURCE, type == UrlType.class ? new UrlType("http://foo") : new UriType("http://foo"));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE).setValue(new CodeType(BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS)))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC).setValue(new StringType("admin:password")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT).setValue(new StringType("500")));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_INPUT)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_TYPE).setValue(new CodeType("Observation")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_URL).setValue(type == UrlType.class ? new UrlType("http://example.com/Observation") : new UriType("http://example.com/Observation")));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_INPUT)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_TYPE).setValue(new CodeType("Patient")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_URL).setValue(type == UrlType.class ? new UrlType("http://example.com/Patient") : new UriType("http://example.com/Patient")));
		return input;
	}

	@Test
	public void testPollForStatus_QUEUED() throws IOException {

		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.QUEUED)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"));
		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myRestfulServerExtension.getPort() + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue()).contains("Job was created at ");
		}
	}

	@Test
	public void testPollForStatus_IN_PROGRESS() throws IOException {

		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.IN_PROGRESS)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"));
		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myRestfulServerExtension.getPort() + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue()).contains("Job was created at 2022-01");
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testPollForStatus_COMPLETE(boolean partitionEnabled) throws IOException {
		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.COMPLETED)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"))
			.setEndTime(parseDate("2022-01-01T12:10:00-04:00"));
		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(jobInfo);

		String requestUrl;
		if (partitionEnabled) {
			enablePartitioning();
			requestUrl = myRestfulServerExtension.getBaseUrl() + "/" + myPartitionName + "/";
			BulkImportJobParameters jobParameters = new BulkImportJobParameters().setPartitionId(myRequestPartitionId);
			jobInfo.setParameters(jobParameters);
		} else {
			requestUrl = myRestfulServerExtension.getBaseUrl() + "/";
		}
		String url = requestUrl + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(200, response.getStatusLine().getStatusCode());
			assertEquals("OK", response.getStatusLine().getReasonPhrase());
			assertThat(response.getEntity().getContentType().getValue()).contains(Constants.CT_FHIR_JSON);
		}
	}

	@Test
	public void testPollForStatus_FAILED() throws IOException {
		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.FAILED)
			.setErrorMessage("It failed.")
			.setErrorCount(123)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"))
			.setEndTime(parseDate("2022-01-01T12:10:00-04:00"));
		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myRestfulServerExtension.getPort() + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(500, response.getStatusLine().getStatusCode());
			assertEquals("Server Error", response.getStatusLine().getReasonPhrase());
			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);
			assertThat(responseContent).contains("\"diagnostics\": \"Job is in FAILED state with 123 error count. Last error: It failed.\"");
		}
	}

	@Test
	public void testFailBulkImportRequest_PartitionedWithoutPermissions() throws IOException {
		// setup
		enablePartitioning();
		Parameters input = createRequest();

		// test
		String url = myRestfulServerExtension.getBaseUrl() + "/Partition-B/" + JpaConstants.OPERATION_IMPORT;

		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.getClient().execute(post)) {
			ourLog.info("Response: {}", response);
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);

			// Verify
			assertEquals(403, response.getStatusLine().getStatusCode());
			assertEquals("Forbidden", response.getStatusLine().getReasonPhrase());
		}

	}

	@Test
	public void testFailBulkImportPollStatus_PartitionedWithoutPermissions() throws IOException {
		// setup
		enablePartitioning();
		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.COMPLETED)
			.setCreateTime(parseDate("2022-01-01T12:00:00-04:00"))
			.setStartTime(parseDate("2022-01-01T12:10:00-04:00"))
			.setEndTime(parseDate("2022-01-01T12:10:00-04:00"));
		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(jobInfo);
		BulkImportJobParameters jobParameters = new BulkImportJobParameters().setPartitionId(myRequestPartitionId);
		jobInfo.setParameters(jobParameters);

		// test
		String url = myRestfulServerExtension.getBaseUrl() + "/Partition-B/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			// Verify
			assertEquals(403, response.getStatusLine().getStatusCode());
			assertEquals("Forbidden", response.getStatusLine().getReasonPhrase());
		}

	}

	private class MyRequestPartitionHelperSvc implements IRequestPartitionHelperSvc {
		@Nonnull
		@Override
		public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull ReadPartitionIdRequestDetails theDetails) {
			assert theRequest != null;
			if (myPartitionName.equals(theRequest.getTenantId())) {
				return myRequestPartitionId;
			} else {
				return RequestPartitionId.fromPartitionName(theRequest.getTenantId());
			}
		}

		@Override
		public void validateHasPartitionPermissions(@Nonnull RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {
			if (!myPartitionName.equals(theRequest.getTenantId()) && theRequest.getTenantId() != null) {
				throw new ForbiddenOperationException("User does not have access to resources on the requested partition");
			}
		}

		@Override
		public RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails) {
			return null;
		}

		@Nonnull
		@Override
		public RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
			return null;
		}

		@Nonnull
		@Override
		public Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId) {
			return null;
		}

		@Override
		public boolean isResourcePartitionable(String theResourceType) {
			return false;
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId) {
			return null;
		}

		@Override
		public RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId) {
			return null;
		}
	}

	private Date parseDate(String theString) {
		return new InstantType(theString).getValue();
	}

}
