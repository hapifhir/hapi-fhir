package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite.BulkPatchRewriteJobAppCtx;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.test.util.CloseableHttpResponseUtil;
import ca.uhn.test.util.ParsedHttpResponse;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.eclipse.jetty.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
	@Mock
	private IJobPartitionProvider myJobPartitionProvider;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@BeforeEach
	void beforeEach() {
		ourProvider.setContextForUnitTest(ourCtx);
		ourProvider.setJobCoordinatorForUnitTest(myJobCoordinator);
		ourProvider.setPartitionSettingsForUnitTest(new PartitionSettings());
		ourProvider.setJobPartitionProviderForUnitTest(myJobPartitionProvider);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testInitiateJob(boolean theDryRun) throws IOException {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);
		when(myJobPartitionProvider.getPartitionedUrls(any(), any())).thenCallRealMethod();

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
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_BATCH_SIZE)
			.setValue(new IntegerType(54));

		if (theDryRun) {
			request.addParameter()
				.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN)
				.setValue(new BooleanType(true));
			request.addParameter()
				.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE)
				.setValue(new CodeType(JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE_COLLECT_CHANGES));
			request.addParameter()
				.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_LIMIT_RESOURCE_COUNT)
				.setValue(new IntegerType(111));
			request.addParameter()
				.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_LIMIT_RESOURCE_VERSION_COUNT)
				.setValue(new IntegerType(222));
		}

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + JpaConstants.OPERATION_BULK_PATCH;
		HttpPost post = new HttpPost(url);
		post.setEntity(new ResourceEntity(ourCtx, request));
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {

			ourLog.info("Response:\n{}", CloseableHttpResponseUtil.parse(response));

			// Verify
			String expectedUrl = ourFhirServer.getBaseUrl() + "/$hapi.fhir.bulk-patch-status?_jobId=MY-INSTANCE-ID";
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
		assertEquals(54, jobParameters.getBatchSize());
		assertEquals(theDryRun, jobParameters.isDryRun());
		if (theDryRun) {
			assertEquals(BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED, jobParameters.getDryRunMode());
			assertEquals(111, jobParameters.getLimitResourceCount());
			assertEquals(222, jobParameters.getLimitResourceVersionCount());
		}
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
		instance.setParameters(new BulkPatchJobParameters());
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID";
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {

			// Verify
			validateStatusPollResponse(theParams, response);
		}
	}

	@Test
	void testPollForStatusReport() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setParameters(new BulkPatchJobParameters());
		instance.setStatus(StatusEnum.COMPLETED);
		instance.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		BulkModifyResourcesResultsJson report = createDryRunReport();
		instance.setReport(JsonUtil.serialize(report));
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID" + "&" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "=" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT;
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {

			ParsedHttpResponse parsedResponse = CloseableHttpResponseUtil.parse(response);
			ourLog.info("ResponseBundle:\n{}", parsedResponse.body());
			assertEquals(report.getReport(), parsedResponse.body());
			assertEquals("text/plain", parsedResponse.contentType());

		}

	}

	@Test
	void testPollForStatusDryRunChanges() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setParameters(new BulkPatchJobParameters().setDryRun(true).setDryRunMode(BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED));
		instance.setStatus(StatusEnum.COMPLETED);
		instance.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		instance.setReport(JsonUtil.serialize(createDryRunReport()));
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID" + "&" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "=" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES;
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {

			ParsedHttpResponse parsedResponse = CloseableHttpResponseUtil.parse(response);
			ourLog.info("Response body:\n{}", parsedResponse.body());
			assertThat(parsedResponse.body())
				.containsSubsequence(
					"\"method\": \"PUT\"",
					"\"url\": \"Patient/123\"",
					"\"method\": \"PUT\"",
					"\"url\": \"Patient/456\"",
					" \"method\": \"DELETE\"",
					"\"url\": \"Patient/A\"",
					" \"method\": \"DELETE\"",
					"\"url\": \"Patient/B\""
				);
			assertEquals("application/fhir+json", parsedResponse.contentType());

		}

	}

	@Test
	void testPollForStatusDryRunChanges_NonDryRunJobInstance() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setStatus(StatusEnum.COMPLETED);
		instance.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		instance.setParameters(new BulkPatchJobParameters());
		BulkModifyResourcesResultsJson report = createDryRunReport();
		instance.setReport(JsonUtil.serialize(report));
		when(myJobCoordinator.getInstance(eq("MY-INSTANCE-ID"))).thenReturn(instance);

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + OPERATION_BULK_PATCH_STATUS + "?" + OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID + "=MY-INSTANCE-ID" + "&" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "=" + OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES;
		HttpGet get = new HttpGet(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {

			ParsedHttpResponse parsedResponse = CloseableHttpResponseUtil.parse(response);
			ourLog.info("Response body:\n{}", parsedResponse.body());
			assertThat(parsedResponse.body())
				.contains("HAPI-2815: Changes response can only be provided for dryRun jobs with dryRunMode=collectChanges");
			assertEquals("application/fhir+json", parsedResponse.contentType());

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

			assertEquals("HAPI-1769: Job ID does not correspond to a $hapi.fhir.bulk-patch job", oo.getIssueFirstRep().getDiagnostics());
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

	@ParameterizedTest
	@CsvSource(delimiter = '#', textBlock = """
		1|2|3       # {"allPartitions":false,"partitionIds":[1,2,3]}
		1|  2   |3  # {"allPartitions":false,"partitionIds":[1,2,3]}
		1|2|  | |3  # {"allPartitions":false,"partitionIds":[1,2,3]}
		_ALL        # {"allPartitions":true}
		_ALL|1      # {"allPartitions":true}
		2|_ALL|1    # {"allPartitions":true}
		FOO         # EX: HAPI-2820: Invalid partition ID: FOO
		""")
	void testPparsePartitionIdsParameter(String theValues, String theExpected) {
		List<IPrimitiveType<String>> input = Arrays.stream(StringUtils.split(theValues, '|'))
			.map(StringType::new)
			.collect(Collectors.toUnmodifiableList());

		if (theExpected.startsWith("EX: ")) {
			String expectedMessage = theExpected.substring(4);
			assertThatThrownBy(() -> BulkPatchProvider.parsePartitionIdsParameter(input))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(expectedMessage);
			return;
		}

		String actual = BulkPatchProvider.parsePartitionIdsParameter(input).toJson();
		assertEquals(theExpected, actual);
	}


	public static void validateStatusPollResponse(PollForStatusTest theParams, CloseableHttpResponse response) throws IOException {
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

	public static Stream<PollForStatusTest> testPollForStatusParameters() {
		return createTestPollForStatusParameters(JpaConstants.OPERATION_BULK_PATCH);
	}

	@Nonnull
	private static BulkModifyResourcesResultsJson createDryRunReport() {
		String reportText = """
			this is the report
			line 2
			line 3""";

		BulkModifyResourcesResultsJson resultsJson = new BulkModifyResourcesResultsJson();
		resultsJson.setReport(reportText);
		resultsJson.setResourcesChangedBodies(List.of(
			"{\"resourceType\":\"Patient\",\"id\":\"123\"}",
			"{\"resourceType\":\"Patient\",\"id\":\"456\"}"
		));
		resultsJson.setResourcesDeletedIds(List.of(
			"Patient/A",
			"Patient/B"
		));

		return resultsJson;
	}

	@Nonnull
	private static BulkModifyResourcesResultsJson createSuccessReport() {
		BulkModifyResourcesResultsJson successReportJson = new BulkModifyResourcesResultsJson();
		successReportJson.setResourcesFailedCount(1);
		successReportJson.setReport("This is the report\nLine 2 or report");
		return successReportJson;
	}

	public static Stream<PollForStatusTest> createTestPollForStatusParameters(String theOperation) {
		BulkModifyResourcesResultsJson successReportJson = createSuccessReport();
		String successReport = JsonUtil.serialize(successReportJson);

		return Stream.of(
			new PollForStatusTest(StatusEnum.QUEUED, HttpStatus.Code.ACCEPTED.getCode(), theOperation + " job has not yet started"),
			new PollForStatusTest(StatusEnum.IN_PROGRESS, HttpStatus.Code.ACCEPTED.getCode(), theOperation + " job has started and is in progress"),
			new PollForStatusTest(StatusEnum.FINALIZE, HttpStatus.Code.ACCEPTED.getCode(), theOperation + " job has started and is being finalized"),
			new PollForStatusTest(StatusEnum.CANCELLED, null, null, HttpStatus.Code.OK.getCode(), theOperation + " job has been cancelled", theOperation + " job has been cancelled", true),
			new PollForStatusTest(StatusEnum.FAILED, "This is an error message", null, HttpStatus.Code.OK.getCode(), theOperation + " job has failed with error: This is an error message", theOperation + " job has failed with error: This is an error message", true),
			new PollForStatusTest(StatusEnum.COMPLETED, null, null, HttpStatus.Code.OK.getCode(), theOperation + " job has completed successfully", theOperation + " job has completed successfully", true),
			new PollForStatusTest(StatusEnum.COMPLETED, null, successReport, HttpStatus.Code.OK.getCode(), successReportJson.getReport(), theOperation + " job has completed successfully", true)
		);
	}

	public record PollForStatusTest(StatusEnum jobStatus, String errorMessage, String reportMessage,
									int expectedStatusCode, String expectedOoMessage,
									String expectedProgressHeaderValue, boolean expectBundleResponse) {

		PollForStatusTest(StatusEnum jobStatus, int expectedStatusCode, String expectedMessage) {
			this(jobStatus, null, null, expectedStatusCode, expectedMessage, expectedMessage, false);
		}

	}


}
