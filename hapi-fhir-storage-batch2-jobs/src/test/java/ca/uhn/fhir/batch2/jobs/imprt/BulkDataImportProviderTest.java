package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hamcrest.CoreMatchers;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UrlType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(value=MethodOrderer.MethodName.class)
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

	@BeforeEach
	public void beforeEach() {
		myProvider.setFhirContext(myCtx);
		myProvider.setJobCoordinator(myJobCoordinator);
	}

	@Test
	public void testStart_Success() throws IOException {
		// Setup

		Parameters input = createRequest();
		ourLog.info("Input: {}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		String jobId = UUID.randomUUID().toString();
		when(myJobCoordinator.startInstance(any())).thenReturn(jobId);

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

			assertEquals(202, response.getStatusLine().getStatusCode());

			OperationOutcome oo = myCtx.newJsonParser().parseResource(OperationOutcome.class, resp);
			assertEquals("Bulk import job has been submitted with ID: " + jobId, oo.getIssue().get(0).getDiagnostics());
			assertEquals("Use the following URL to poll for job status: http://localhost:" + myRestfulServerExtension.getPort() + "/$import-poll-status?_jobId=" + jobId, oo.getIssue().get(1).getDiagnostics());
		}

		verify(myJobCoordinator, times(1)).startInstance(myStartRequestCaptor.capture());

		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		ourLog.info("Parameters: {}", startRequest.getParameters());
		assertEquals("{\"ndJsonUrls\":[\"http://example.com/Patient\",\"http://example.com/Observation\"],\"maxBatchResourceCount\":500}", startRequest.getParameters());
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
			assertThat(resp, containsString("\"resourceType\": \"OperationOutcome\""));
			assertThat(resp, containsString("HAPI-0513: Must request async processing for $import"));
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
			assertThat(resp, containsString("HAPI-1769: No URLs specified"));
		}

	}

	@Nonnull
	private Parameters createRequest() {
		Parameters input = new Parameters();
		input.addParameter(BulkDataImportProvider.PARAM_INPUT_FORMAT, new CodeType(Constants.CT_FHIR_NDJSON));
		input.addParameter(BulkDataImportProvider.PARAM_INPUT_SOURCE, new UrlType("http://foo"));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE).setValue(new CodeType(BulkDataImportProvider.PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS)))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC).setValue(new StringType("admin:password")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT).setValue(new StringType("500")));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_INPUT)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_TYPE).setValue(new CodeType("Observation")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_URL).setValue(new UrlType("http://example.com/Observation")));
		input.addParameter()
			.setName(BulkDataImportProvider.PARAM_INPUT)
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_TYPE).setValue(new CodeType("Patient")))
			.addPart(new Parameters.ParametersParameterComponent().setName(BulkDataImportProvider.PARAM_INPUT_URL).setValue(new UrlType("http://example.com/Patient")));
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
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Job was created at "));
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
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Job was created at 2022-01-01"));
		}
	}

	@Test
	public void testPollForStatus_COMPLETE() throws IOException {
		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.COMPLETED)
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

			assertEquals(200, response.getStatusLine().getStatusCode());
			assertEquals("OK", response.getStatusLine().getReasonPhrase());
			assertThat(response.getEntity().getContentType().getValue(), containsString(Constants.CT_FHIR_JSON));
		}
	}

	@Test
	public void testPollForStatus_ERROR() throws IOException {
		JobInstance jobInfo = new JobInstance()
			.setStatus(StatusEnum.ERRORED)
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
			assertThat(responseContent, containsString("\"diagnostics\": \"Job is in ERRORED state with 123 error count. Last error: It failed.\""));
		}
	}

	private Date parseDate(String theString) {
		return new InstantType(theString).getValue();
	}

}
