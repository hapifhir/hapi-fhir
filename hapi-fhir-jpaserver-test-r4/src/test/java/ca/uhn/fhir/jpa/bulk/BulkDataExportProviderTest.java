package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.api.model.Batch2JobOperationResult;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkDataExportProviderTest {

	private static final String A_JOB_ID = "0000000-AAAAAA";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportProviderTest.class);
	private static final String GROUP_ID = "Group/G2401";
	private static final String G_JOB_ID = "0000000-GGGGGG";
	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@RegisterExtension
	private final HttpClientExtension myClient = new HttpClientExtension();
	@Mock
	private IBatch2JobRunner myJobRunner;
	@Mock
	IFhirResourceDao myFhirResourceDao;
	@InjectMocks
	private BulkDataExportProvider myProvider;
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(myCtx)
		.withServer(s -> s.registerProvider(myProvider));
	@Spy
	private RequestPartitionHelperSvc myRequestPartitionHelperSvc = new MyRequestPartitionHelperSvc();

	private JpaStorageSettings myStorageSettings;

	private final RequestPartitionId myRequestPartitionId = RequestPartitionId.fromPartitionIdAndName(123, "Partition-A");

	private final String myPartitionName = "Partition-A";

	private final String myFixedBaseUrl = "http:/myfixedbaseurl.com";

	private class MyRequestPartitionHelperSvc extends RequestPartitionHelperSvc {
		@Override
		public @NotNull RequestPartitionId determineReadPartitionForRequest(RequestDetails theRequest, ReadPartitionIdRequestDetails theDetails) {
			assert theRequest != null;
			if (myPartitionName.equals(theRequest.getTenantId())) {
				return myRequestPartitionId;
			} else {
				return RequestPartitionId.fromPartitionName(theRequest.getTenantId());
			}
		}

		@Override
		public void validateHasPartitionPermissions(RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {
			if (!myPartitionName.equals(theRequest.getTenantId()) && theRequest.getTenantId() != null) {
				throw new ForbiddenOperationException("User does not have access to resources on the requested partition");
			}
		}

	}

	@BeforeEach
	public void injectStorageSettings() {
		myStorageSettings = new JpaStorageSettings();
		myProvider.setStorageSettings(myStorageSettings);
		DaoRegistry daoRegistry = mock(DaoRegistry.class);
		lenient().when(daoRegistry.getRegisteredDaoTypes()).thenReturn(Set.of("Patient", "Observation", "Encounter"));

		lenient().when(daoRegistry.getResourceDao(anyString())).thenReturn(myFhirResourceDao);
		myProvider.setDaoRegistry(daoRegistry);

	}

	public void startWithFixedBaseUrl() {
		HardcodedServerAddressStrategy hardcodedServerAddressStrategy = new HardcodedServerAddressStrategy(myFixedBaseUrl);
		myServer.withServer(s -> s.setServerAddressStrategy(hardcodedServerAddressStrategy));
	}

	public void enablePartitioning() {
		myServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
	}

	private BulkExportParameters verifyJobStart() {
		ArgumentCaptor<Batch2BaseJobParameters> startJobCaptor = ArgumentCaptor.forClass(Batch2BaseJobParameters.class);
		verify(myJobRunner).startNewJob(isNotNull(), startJobCaptor.capture());
		Batch2BaseJobParameters sp = startJobCaptor.getValue();
		assertTrue(sp instanceof BulkExportParameters);
		return (BulkExportParameters) sp;
	}

	private Batch2JobStartResponse createJobStartResponse(String theJobId) {
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(theJobId);

		return response;
	}

	private Batch2JobStartResponse createJobStartResponse() {
		return createJobStartResponse(A_JOB_ID);
	}

	@ParameterizedTest
	@CsvSource({"false, false", "false, true", "true, true", "true, false"})
	public void testSuccessfulInitiateBulkRequest_Post_WithFixedBaseURLAndPartitioning(Boolean baseUrlFixed, Boolean partitioningEnabled) throws IOException {
		// setup
		if (baseUrlFixed) {
			startWithFixedBaseUrl();
		}

		String myBaseUriForExport;
		if (partitioningEnabled) {
			enablePartitioning();
			myBaseUriForExport = myServer.getBaseUrl() + "/" + myPartitionName;
		} else {
			myBaseUriForExport = myServer.getBaseUrl();
		}

		String patientResource = "Patient";
		String practitionerResource = "Practitioner";
		String filter = "Patient?identifier=foo";
		String postFetchFilter = "Patient?_tag=foo";
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType(patientResource + ", " + practitionerResource));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType(filter));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL, new StringType(postFetchFilter));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// test
		HttpPost post = new HttpPost(myBaseUriForExport + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());

			String baseUrl;
			if (baseUrlFixed) {
				// If a fixed Base URL is assigned, then the URLs in the poll response should similarly start with the fixed base URL.
				baseUrl = myFixedBaseUrl;
			} else {
				// Otherwise the URLs in the poll response should start with the default server URL.
				baseUrl = myServer.getBaseUrl();
			}

			if(partitioningEnabled) {
				baseUrl = baseUrl + "/" + myPartitionName;
			}

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(baseUrl + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters params = verifyJobStart();
		assertEquals(2, params.getResourceTypes().size());
		assertTrue(params.getResourceTypes().contains(patientResource));
		assertTrue(params.getResourceTypes().contains(practitionerResource));
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertNotNull(params.getSince());
		assertTrue(params.getFilters().contains(filter));
		assertThat(params.getPostFetchFilterUrls(), contains("Patient?_tag=foo"));
	}

	@Test
	public void testOmittingOutputFormatDefaultsToNdjson() throws IOException {
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		try (CloseableHttpResponse response = myClient.execute(post)) {
			assertEquals(202, response.getStatusLine().getStatusCode());
		}

		BulkExportParameters params = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());


	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSuccessfulInitiateBulkRequest_GetWithPartitioning(boolean partitioningEnabled) throws IOException {
		when(myJobRunner.startNewJob(isNotNull(), any())).thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();

		String myBaseUrl;
		if (partitioningEnabled) {
			enablePartitioning();
			myBaseUrl = myServer.getBaseUrl() + "/" + myPartitionName;
		} else {
			myBaseUrl = myServer.getBaseUrl();
		}
		String url = myBaseUrl + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?identifier=foo");

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myBaseUrl + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters params = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertThat(params.getResourceTypes(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(params.getSince(), notNullValue());
		assertThat(params.getFilters(), containsInAnyOrder("Patient?identifier=foo"));
	}

	@Test
	public void testSuccessfulInitiateBulkRequest_Get_MultipleTypeFilters() throws IOException {
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient,EpisodeOfCare")
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?_id=P999999990")
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("EpisodeOfCare?patient=P999999990");

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters params = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertThat(params.getResourceTypes(), containsInAnyOrder("Patient", "EpisodeOfCare"));
		assertThat(params.getSince(), nullValue());
		assertThat(params.getFilters(), containsInAnyOrder("Patient?_id=P999999990", "EpisodeOfCare?patient=P999999990"));
	}

	@Test
	public void testPollForStatus_QUEUED() throws IOException {
		// setup
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.BUILDING);
		info.setEndTime(new Date());

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(),
				containsString("Build in progress - Status set to " + info.getStatus() + " at 20"));
		}
	}

	@Test
	public void testPollForStatus_ERROR() throws IOException {
		// setup
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.ERROR);
		info.setStartTime(new Date());
		info.setErrorMsg("Some Error Message");

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(500, response.getStatusLine().getStatusCode());
			assertEquals("Server Error", response.getStatusLine().getReasonPhrase());

			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);
			assertThat(responseContent, containsString("\"diagnostics\": \"Some Error Message\""));
		}
	}

	@ParameterizedTest
	@CsvSource({"false, false", "false, true", "true, true", "true, false"})
	public void testPollForStatus_COMPLETED_WithFixedBaseURLAndPartitioning(boolean baseUrlFixed, boolean partitioningEnabled) throws IOException {

		// setup
		if (baseUrlFixed) {
			startWithFixedBaseUrl();
		}

		String myBaseUriForExport;
		if (partitioningEnabled) {
			enablePartitioning();
			myBaseUriForExport = myServer.getBaseUrl() + "/" + myPartitionName;
		} else {
			myBaseUriForExport = myServer.getBaseUrl();
		}

		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.COMPLETE);
		info.setEndTime(InstantType.now().getValue());
		ArrayList<String> ids = new ArrayList<>();
		ids.add(new IdType("Binary/111").getValueAsString());
		ids.add(new IdType("Binary/222").getValueAsString());
		ids.add(new IdType("Binary/333").getValueAsString());
		BulkExportJobResults results = new BulkExportJobResults();

		HashMap<String, List<String>> map = new HashMap<>();
		map.put("Patient", ids);
		results.setResourceTypeToBinaryIds(map);
		info.setReport(JsonUtil.serialize(results));
		if (partitioningEnabled) {
			info.setRequestPartitionId(myRequestPartitionId);
		}

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String url = myBaseUriForExport + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			String myBaseUriForPoll;
			if (baseUrlFixed) {
				// If a fixed Base URL is provided, the URLs in the poll response should similarly start with the fixed Base URL.
				myBaseUriForPoll = myFixedBaseUrl;
			} else {
				// Otherwise the URLs in the poll response should instead with the default server URL.
				myBaseUriForPoll = myServer.getBaseUrl();
			}
			if (partitioningEnabled) {
				// If partitioning is enabled, then the URLs in the poll response should also have the partition name.
				myBaseUriForPoll = myBaseUriForPoll + "/"+ myPartitionName;
			}

			assertEquals(200, response.getStatusLine().getStatusCode());
			assertEquals("OK", response.getStatusLine().getReasonPhrase());
			assertEquals(Constants.CT_JSON, response.getEntity().getContentType().getValue());

			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);
			BulkExportResponseJson responseJson = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
			assertEquals(3, responseJson.getOutput().size());
			assertEquals("Patient", responseJson.getOutput().get(0).getType());
			assertEquals(myBaseUriForPoll + "/Binary/111", responseJson.getOutput().get(0).getUrl());
			assertEquals("Patient", responseJson.getOutput().get(1).getType());
			assertEquals(myBaseUriForPoll + "/Binary/222", responseJson.getOutput().get(1).getUrl());
			assertEquals("Patient", responseJson.getOutput().get(2).getType());
			assertEquals(myBaseUriForPoll + "/Binary/333", responseJson.getOutput().get(2).getUrl());
		}
	}

	@Test
	public void testPollForStatus_WithInvalidPartition() throws IOException {

		// setup
		enablePartitioning();

		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.COMPLETE);
		info.setEndTime(InstantType.now().getValue());
		info.setRequestPartitionId(myRequestPartitionId);
		ArrayList<String> ids = new ArrayList<>();
		ids.add(new IdType("Binary/111").getValueAsString());
		ids.add(new IdType("Binary/222").getValueAsString());
		ids.add(new IdType("Binary/333").getValueAsString());
		BulkExportJobResults results = new BulkExportJobResults();

		HashMap<String, List<String>> map = new HashMap<>();
		map.put("Patient", ids);
		results.setResourceTypeToBinaryIds(map);
		info.setReport(JsonUtil.serialize(results));

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String myBaseUriForExport = myServer.getBaseUrl() + "/Partition-B";
		String url = myBaseUriForExport + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(403, response.getStatusLine().getStatusCode());
			assertEquals("Forbidden", response.getStatusLine().getReasonPhrase());
		}
	}

	@Test
	public void testExportWhenNoResourcesReturned() throws IOException {
		// setup
		String msg = "Some msg";
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.COMPLETE);
		info.setEndTime(InstantType.now().getValue());
		ArrayList<String> ids = new ArrayList<>();
		BulkExportJobResults results = new BulkExportJobResults();
		HashMap<String, List<String>> map = new HashMap<>();
		map.put("Patient", ids);
		results.setResourceTypeToBinaryIds(map);
		results.setReportMsg(msg);
		info.setReport(JsonUtil.serialize(results));

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(200, response.getStatusLine().getStatusCode());
			assertEquals("OK", response.getStatusLine().getReasonPhrase());
			assertEquals(Constants.CT_JSON, response.getEntity().getContentType().getValue());

			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);
			BulkExportResponseJson responseJson = JsonUtil.deserialize(responseContent, BulkExportResponseJson.class);
			assertEquals(msg, responseJson.getMsg());
		}
	}

	@Test
	public void testPollForStatus_Gone() throws IOException {
		// setup

		// when
		when(myJobRunner.getJobInfo(anyString()))
			.thenThrow(new ResourceNotFoundException("Unknown job: AAA"));

		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());
			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);

			assertEquals(404, response.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_FHIR_JSON_NEW, response.getEntity().getContentType().getValue().replaceAll(";.*", "").trim());
			assertThat(responseContent, containsString("\"diagnostics\": \"Unknown job: AAA\""));
		}
	}

	/**
	 * Group export tests
	 * See <a href="https://build.fhir.org/ig/HL7/us-bulk-data/">Bulk Data IG</a>
	 * <p>
	 * GET [fhir base]/Group/[id]/$export
	 * <p>
	 * FHIR Operation to obtain data on all patients listed in a single FHIR Group Resource.
	 */

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Post() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse(G_JOB_ID));

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		StringType obsTypeFilter = new StringType("Observation?code=OBSCODE,DiagnosticReport?code=DRCODE");
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Observation, DiagnosticReport"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_MDM, true);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, obsTypeFilter);

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + GROUP_ID + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		// verify
		BulkExportParameters bp = verifyJobStart();

		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes(), containsInAnyOrder("Observation", "DiagnosticReport"));
		assertThat(bp.getSince(), notNullValue());
		assertThat(bp.getFilters(), notNullValue());
		assertEquals(GROUP_ID, bp.getGroupId());
		assertThat(bp.isExpandMdm(), is(equalTo(true)));
	}

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Get() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any())).thenReturn(createJobStartResponse(G_JOB_ID));

		InstantType now = InstantType.now();

		String url = myServer.getBaseUrl() + "/" + GROUP_ID + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?identifier=foo|bar")
			+ "&" + JpaConstants.PARAM_EXPORT_MDM + "=true";

		// call
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters bp = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(bp.getSince(), notNullValue());
		assertThat(bp.getFilters(), notNullValue());
		assertEquals(GROUP_ID, bp.getGroupId());
		assertThat(bp.isExpandMdm(), is(equalTo(true)));
	}

	@Test
	public void testInitiateWithGetAndMultipleTypeFilters() throws IOException {
		// setup
		InstantType now = InstantType.now();

		// manual construct
		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Immunization, Observation")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString());

		String immunizationTypeFilter1 = "Immunization?patient.identifier=SC378274-MRN|009999997,SC378274-MRN|009999998,SC378274-MRN|009999999&date=2020-01-02";
		String immunizationTypeFilter2 = "Immunization?patient=Patient/123";
		String observationFilter1 = "Observation?subject=Patient/123&created=ge2020-01-01";
		String multiValuedTypeFilterBuilder = "&" +
			JpaConstants.PARAM_EXPORT_TYPE_FILTER +
			"=" +
			UrlUtil.escapeUrlParam(immunizationTypeFilter1) +
			"," +
			UrlUtil.escapeUrlParam(immunizationTypeFilter2) +
			"," +
			UrlUtil.escapeUrlParam(observationFilter1);

		url += multiValuedTypeFilterBuilder;

		// call
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse ignored = myClient.execute(get)) {
			// verify
			BulkExportParameters bp = verifyJobStart();
			assertThat(bp.getFilters(), containsInAnyOrder(immunizationTypeFilter1, immunizationTypeFilter2, observationFilter1));
		}
	}

	@Test
	public void testInitiateGroupExportWithInvalidResourceTypesFails() throws IOException {
		// when

		String url = myServer.getBaseUrl() + "/" + "Group/123/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("StructureDefinition,Observation");

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse execute = myClient.execute(get)) {
			String responseBody = IOUtils.toString(execute.getEntity().getContent(), StandardCharsets.UTF_8);

			// verify
			assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(400)));
			assertThat(responseBody, is(containsString("Resource types [StructureDefinition] are invalid for this type of export, as they do not contain search parameters that refer to patients.")));
		}
	}

	@Test
	public void testInitiateGroupExportWithNoResourceTypes() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(createJobStartResponse());

		// test
		String url = myServer.getBaseUrl() + "/" + "Group/123/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON);

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse execute = myClient.execute(get)) {

			// verify
			assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(202)));
			final BulkExportParameters bulkExportParameters = verifyJobStart();

			assertAll(
				() -> assertTrue(bulkExportParameters.getResourceTypes().contains("Patient")),
				() -> assertTrue(bulkExportParameters.getResourceTypes().contains("Group")),
				() -> assertTrue(bulkExportParameters.getResourceTypes().contains("Device"))
			);
		}
	}

	@Test
	public void testInitiateGroupExportWithTypeAllergyIntolerance() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(createJobStartResponse());

		// http://localhost:8000/Group/1370/$export?_type=AllergyIntolerance
		final String url = String.format("%s/%s/%s/%s?%s=%s",
			myServer.getBaseUrl(),
			ResourceType.Group.name(),
			"123",
			JpaConstants.OPERATION_EXPORT,
			JpaConstants.PARAM_EXPORT_TYPE,
			ResourceType.AllergyIntolerance.name());

		final HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (final CloseableHttpResponse execute = myClient.execute(get)) {
			// verify
			assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(202)));

			final BulkExportParameters bulkExportParameters = verifyJobStart();

			assertEquals(Collections.singletonList(ResourceType.AllergyIntolerance.name()), bulkExportParameters.getResourceTypes());
		}
	}

	@Test
	public void testInitiateGroupExportWithTypeFilterAllergyIntolerance() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(createJobStartResponse());

		// http://localhost:8000/Group/1370/$export?_typeFilter=AllergyIntolerance?category=food
		final String url = String.format("%s/%s/%s/%s?%s=%s?%s=%s",
			myServer.getBaseUrl(),
			ResourceType.Group.name(),
			"123",
			JpaConstants.OPERATION_EXPORT,
			JpaConstants.PARAM_EXPORT_TYPE_FILTER,
			ResourceType.AllergyIntolerance.name(),
			"category",
			"food");

		final HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (final CloseableHttpResponse execute = myClient.execute(get)) {

			// verify
			assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(202)));
			final BulkExportParameters bulkExportParameters = verifyJobStart();

			assertEquals(Collections.singletonList(ResourceType.AllergyIntolerance.name()), bulkExportParameters.getResourceTypes());
		}
	}

	@Test
	public void testInitiateWithPostAndMultipleTypeFilters() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any())).thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient"));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Patient?gender=male,Patient?gender=female"));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		// verify
		BulkExportParameters bp = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes(), containsInAnyOrder("Patient"));
		assertThat(bp.getFilters(), containsInAnyOrder("Patient?gender=male", "Patient?gender=female"));
	}

	@Test
	public void testInitiateBulkExportOnPatient_noTypeParam_addsTypeBeforeBulkExport() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/Patient/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters bp = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes(), containsInAnyOrder("Patient"));
	}

	@Test
	public void testInitiatePatientExportRequest() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Immunization, Observation"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Immunization?vaccine-code=foo"));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/Patient/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		BulkExportParameters bp = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes(), containsInAnyOrder("Immunization", "Observation"));
		assertThat(bp.getSince(), notNullValue());
		assertThat(bp.getFilters(), containsInAnyOrder("Immunization?vaccine-code=foo"));
	}

	@Test
	public void testProviderProcessesNoCacheHeader() throws IOException {
		// setup
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);

		// when
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(startResponse);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		// verify
		BulkExportParameters parameters = verifyJobStart();
		assertThat(parameters.isUseExistingJobsFirst(), is(equalTo(false)));
	}

	@Test
	public void testProvider_whenEnableBatchJobReuseIsFalse_startsNewJob() throws IOException {
		// setup
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);

		myStorageSettings.setEnableBulkExportJobReuse(false);

		// when
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(startResponse);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		// call
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		// verify
		BulkExportParameters parameters = verifyJobStart();
		assertThat(parameters.isUseExistingJobsFirst(), is(equalTo(false)));
	}

	@Test
	public void testProviderReturnsSameIdForSameJob() throws IOException {
		// given
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);
		startResponse.setInstanceId(A_JOB_ID);
		when(myJobRunner.startNewJob(isNotNull(), any(Batch2BaseJobParameters.class)))
			.thenReturn(startResponse);

		// when
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		// then
		callExportAndAssertJobId(input, A_JOB_ID);
		callExportAndAssertJobId(input, A_JOB_ID);

	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeleteForOperationPollStatus_SUBMITTED_ShouldCancelJobSuccessfully(boolean partitioningEnabled) throws IOException {
		// setup
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.SUBMITTED);
		info.setEndTime(InstantType.now().getValue());
		if (partitioningEnabled) {
			info.setRequestPartitionId(myRequestPartitionId);
		}
		Batch2JobOperationResult result = new Batch2JobOperationResult();
		result.setOperation("Cancel job instance " + A_JOB_ID);
		result.setMessage("Job instance <" + A_JOB_ID + "> successfully cancelled.");
		result.setSuccess(true);

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);
		when(myJobRunner.cancelInstance(eq(A_JOB_ID)))
			.thenReturn(result);

		// call
		String baseUrl;
		if (partitioningEnabled) {
			enablePartitioning();
			baseUrl = myServer.getBaseUrl() + "/" + myPartitionName;
		} else {
			baseUrl = myServer.getBaseUrl();
		}

		String url = baseUrl + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpDelete delete = new HttpDelete(url);
		try (CloseableHttpResponse response = myClient.execute(delete)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());

			verify(myJobRunner, times(1)).cancelInstance(A_JOB_ID);
			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);
			assertThat(responseContent, containsString("successfully cancelled."));
		}
	}

	@Test
	public void testDeleteForOperationPollStatus_COMPLETE_ShouldReturnError() throws IOException {
		// setup
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.COMPLETE);
		info.setEndTime(InstantType.now().getValue());

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String url = myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpDelete delete = new HttpDelete(url);
		try (CloseableHttpResponse response = myClient.execute(delete)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(404, response.getStatusLine().getStatusCode());
			assertEquals("Not Found", response.getStatusLine().getReasonPhrase());

			verify(myJobRunner, times(1)).cancelInstance(A_JOB_ID);
			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			// content would be blank, since the job is cancelled, so no
			ourLog.info("Response content: {}", responseContent);
			assertThat(responseContent, containsString("was already cancelled or has completed."));
		}
	}

	@Test
	public void testGetBulkExport_outputFormat_FhirNdJson_inHeader() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		// call
		final HttpGet httpGet = new HttpGet(String.format("http://localhost:%s/%s", myServer.getPort(), JpaConstants.OPERATION_EXPORT));
		httpGet.addHeader("_outputFormat", Constants.CT_FHIR_NDJSON);
		httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);

		try (CloseableHttpResponse response = myClient.execute(httpGet)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(String.format("http://localhost:%s/$export-poll-status?_jobId=%s", myServer.getPort(), A_JOB_ID), response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
			assertTrue(IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8).isEmpty());
		}

		final BulkExportParameters params = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
	}

	@Test
	public void testGetBulkExport_outputFormat_FhirNdJson_inUrl() throws IOException {
		// when
		when(myJobRunner.startNewJob(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		// call
		final HttpGet httpGet = new HttpGet(String.format("http://localhost:%s/%s?_outputFormat=%s", myServer.getPort(), JpaConstants.OPERATION_EXPORT, Constants.CT_FHIR_NDJSON));
		httpGet.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);

		try (CloseableHttpResponse response = myClient.execute(httpGet)) {
			assertAll(
				() -> assertEquals(202, response.getStatusLine().getStatusCode()),
				() -> assertEquals("Accepted", response.getStatusLine().getReasonPhrase()),
				() -> assertEquals(String.format("http://localhost:%s/$export-poll-status?_jobId=%s", myServer.getPort(), A_JOB_ID), response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue()),
				() -> assertTrue(IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8).isEmpty())
			);
		}

		final BulkExportParameters params = verifyJobStart();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
	}

	@Test
	public void testOperationExportPollStatus_POST_NonExistingId_NotFound() throws IOException {
		String jobId = "NonExisting-JobId";

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, new StringType(jobId));

		// Initiate Export Poll Status
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));


		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(Constants.STATUS_HTTP_404_NOT_FOUND, response.getStatusLine().getStatusCode());
		}
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testOperationExportPollStatus_POST_ExistingId_Accepted(boolean partititioningEnabled) throws IOException {
		// setup
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.SUBMITTED);
		info.setEndTime(InstantType.now().getValue());
		if(partititioningEnabled) {
			info.setRequestPartitionId(myRequestPartitionId);
		}

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, new StringType(A_JOB_ID));

		String baseUrl;
		if (partititioningEnabled) {
			enablePartitioning();
			baseUrl = myServer.getBaseUrl() + "/" + myPartitionName;
		} else {
			baseUrl = myServer.getBaseUrl();
		}

		// Initiate Export Poll Status
		HttpPost post = new HttpPost(baseUrl + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(Constants.STATUS_HTTP_202_ACCEPTED, response.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testOperationExportPollStatus_POST_MissingInputParameterJobId_BadRequest() throws IOException {

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));

		// Initiate Export Poll Status
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));

		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, response.getStatusLine().getStatusCode());
		}
	}

	private void callExportAndAssertJobId(Parameters input, String theExpectedJobId) throws IOException {
		HttpPost post;
		post = new HttpPost(myServer.getBaseUrl() + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + theExpectedJobId, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}
	}

	@Test
	public void testFailBulkExportRequest_PartitionedWithoutPermissions() throws IOException {

		// setup
		enablePartitioning();

		// test
		String url = myServer.getBaseUrl() + "/Partition-B/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner");

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(403, response.getStatusLine().getStatusCode());
			assertEquals("Forbidden", response.getStatusLine().getReasonPhrase());
		}

	}

	@Test
	public void testFailPollRequest_PartitionedWithoutPermissions() throws IOException {
		// setup
		enablePartitioning();

		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(A_JOB_ID);
		info.setStatus(BulkExportJobStatusEnum.BUILDING);
		info.setEndTime(new Date());
		info.setRequestPartitionId(myRequestPartitionId);

		// when
		when(myJobRunner.getJobInfo(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String url = myServer.getBaseUrl() + "/Partition-B/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(403, response.getStatusLine().getStatusCode());
			assertEquals("Forbidden", response.getStatusLine().getReasonPhrase());
		}

	}

	static Stream<Arguments> paramsProvider() {
		return Stream.of(
			Arguments.arguments(true),
			Arguments.arguments(false)
		);
	}


}
