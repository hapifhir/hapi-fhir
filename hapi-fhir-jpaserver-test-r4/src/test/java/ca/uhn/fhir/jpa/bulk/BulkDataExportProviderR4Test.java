package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.MockInvoker;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkDataExportProviderR4Test {

	private static final String A_JOB_ID = "0000000-AAAAAA";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportProviderR4Test.class);
	private static final String GROUP_ID = "Group/G2401";
	private static final String G_JOB_ID = "0000000-GGGGGG";
	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private final RequestPartitionId myRequestPartitionId = RequestPartitionId.fromPartitionIdAndName(123, "Partition-A");
	private final String myPartitionName = "Partition-A";
	private final String myFixedBaseUrl = "http:/myfixedbaseurl.com";
	@Mock
	IFhirResourceDao myFhirResourceDao;
	@Mock
	IJobCoordinator myJobCoordinator;

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@InjectMocks
	private BulkDataExportProvider myProvider;
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(myCtx)
		.withServer(s -> s.registerProvider(myProvider));
	@Spy
	private RequestPartitionHelperSvc myRequestPartitionHelperSvc = new MyRequestPartitionHelperSvc();
	private JpaStorageSettings myStorageSettings;
	@Mock
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	public void injectStorageSettings() {
		myStorageSettings = new JpaStorageSettings();
		myProvider.setStorageSettings(myStorageSettings);
		lenient().when(myDaoRegistry.getRegisteredDaoTypes()).thenReturn(Set.of("Patient", "Observation", "Encounter", "Group", "Device", "DiagnosticReport"));

		lenient().when(myDaoRegistry.getResourceDao(anyString())).thenReturn(myFhirResourceDao);
		myProvider.setDaoRegistry(myDaoRegistry);
	}

	public void startWithFixedBaseUrl() {
		HardcodedServerAddressStrategy hardcodedServerAddressStrategy = new HardcodedServerAddressStrategy(myFixedBaseUrl);
		myServer.withServer(s -> s.setServerAddressStrategy(hardcodedServerAddressStrategy));
	}

	public void enablePartitioning() {
		myServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
	}

	private JobInstanceStartRequest verifyJobStart() {
		ArgumentCaptor<JobInstanceStartRequest> startJobCaptor = ArgumentCaptor.forClass(JobInstanceStartRequest.class);
		verify(myJobCoordinator).startInstance(isNotNull(), startJobCaptor.capture());
		return startJobCaptor.getValue();
	}

	private BulkExportJobParameters verifyJobStartAndReturnParameters() {
		return verifyJobStart().getParameters(BulkExportJobParameters.class);
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

		String partitionPath;
		if (partitioningEnabled) {
			enablePartitioning();
			partitionPath = "/" + myPartitionName;
		} else {
			partitionPath = "";
		}

		String patientResource = "Patient";
		String practitionerResource = "Practitioner";
		String filter = "Patient?identifier=foo";
		String postFetchFilter = "Patient?_tag=foo";
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType(patientResource + ", " + practitionerResource));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_UNTIL, later);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType(filter));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL, new StringType(postFetchFilter));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// test
		HttpTestResponse response = myServer.fhirRequest(partitionPath + "/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input);

		String baseUrl;
		if (baseUrlFixed) {
			// If a fixed Base URL is assigned, then the URLs in the poll response should similarly start with the fixed base URL.
			baseUrl = myFixedBaseUrl;
		} else {
			// Otherwise the URLs in the poll response should start with the default server URL.
			baseUrl = myServer.getBaseUrl();
		}

		if (partitioningEnabled) {
			baseUrl = baseUrl + "/" + myPartitionName;
		}

		response.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(baseUrl + "/$export-poll-status?_jobId=" + A_JOB_ID);

		BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertThat(params.getResourceTypes()).hasSize(2);
		assertThat(params.getResourceTypes()).contains(patientResource);
		assertThat(params.getResourceTypes()).contains(practitionerResource);
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertNotNull(params.getSince());
		assertNotNull(params.getUntil());
		assertThat(params.getFilters()).contains(filter);
		assertThat(params.getPostFetchFilterUrls()).containsExactly("Patient?_tag=foo");
	}

	@Test
	public void testOmittingOutputFormatDefaultsToNdjson() throws IOException {
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);

		BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());


	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSuccessfulInitiateBulkRequest_GetWithPartitioning(boolean partitioningEnabled) throws IOException {
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		String partitionPath;
		if (partitioningEnabled) {
			enablePartitioning();
			partitionPath = "/" + myPartitionName;
		} else {
			partitionPath = "";
		}
		String path = partitionPath + "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_UNTIL + "=" + UrlUtil.escapeUrlParam(later.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?identifier=foo");

		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + partitionPath + "/$export-poll-status?_jobId=" + A_JOB_ID);

		BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertThat(params.getResourceTypes()).containsExactlyInAnyOrder("Patient", "Practitioner");
		assertNotNull(params.getSince());
		assertNotNull(params.getUntil());
		assertThat(params.getFilters()).containsExactlyInAnyOrder("Patient?identifier=foo");
	}

	@Test
	public void testSuccessfulInitiateBulkRequest_Get_MultipleTypeFilters() throws IOException {
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		String path = "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient,EpisodeOfCare")
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?_id=P999999990")
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("EpisodeOfCare?patient=P999999990");

		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
		assertThat(params.getResourceTypes()).containsExactlyInAnyOrder("Patient", "EpisodeOfCare");
		assertNull(params.getSince());
		assertNull(params.getUntil());
		assertThat(params.getFilters()).containsExactlyInAnyOrder("Patient?_id=P999999990", "EpisodeOfCare?patient=P999999990");
	}

	@Test
	public void testPollForStatus_QUEUED() throws IOException {
		// setup
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.QUEUED);
		info.setEndTime(new Date());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_RETRY_AFTER)).isEqualTo("120");
		assertThat(response.getHeader(Constants.HEADER_X_PROGRESS))
			.contains("Build in progress - Status set to " + info.getStatus() + " at 20");
	}

	@Test
	public void testPollForStatus_Failed() throws IOException {
		// setup
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.FAILED);
		info.setStartTime(new Date());
		info.setErrorMessage("Some Error Message");

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(500);
		assertThat(response.getReasonPhrase()).isEqualTo("Server Error");

		String responseContent = response.getBody();
		assertThat(responseContent).contains("\"diagnostics\": \"Some Error Message\"");
		assertThat(responseContent).contains(OperationOutcome.IssueType.PROCESSING.toCode());
	}

	@ParameterizedTest
	@CsvSource({"false, false", "false, true", "true, true", "true, false"})
	public void testPollForStatus_COMPLETED_WithFixedBaseURLAndPartitioning(boolean baseUrlFixed, boolean partitioningEnabled) throws IOException {

		// setup
		if (baseUrlFixed) {
			startWithFixedBaseUrl();
		}

		String partitionPath;
		if (partitioningEnabled) {
			enablePartitioning();
			partitionPath = "/" + myPartitionName;
		} else {
			partitionPath = "";
		}

		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.COMPLETED);
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

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String path = partitionPath + "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get();

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
			myBaseUriForPoll = myBaseUriForPoll + "/" + myPartitionName;
		}

		response.assertStatus(200);
		assertThat(response.getReasonPhrase()).isEqualTo("OK");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).isEqualTo(Constants.CT_JSON);

		BulkExportResponseJson responseJson = JsonUtil.deserialize(response.getBody(), BulkExportResponseJson.class);
		assertThat(responseJson.getOutput()).hasSize(3);
		assertThat(responseJson.getOutput().get(0).getType()).isEqualTo("Patient");
		assertThat(responseJson.getOutput().get(0).getUrl()).isEqualTo(myBaseUriForPoll + "/Binary/111");
		assertThat(responseJson.getOutput().get(1).getType()).isEqualTo("Patient");
		assertThat(responseJson.getOutput().get(1).getUrl()).isEqualTo(myBaseUriForPoll + "/Binary/222");
		assertThat(responseJson.getOutput().get(2).getType()).isEqualTo("Patient");
		assertThat(responseJson.getOutput().get(2).getUrl()).isEqualTo(myBaseUriForPoll + "/Binary/333");
	}

	@Test
	public void testPollForStatus_WithInvalidPartition() throws IOException {

		// setup
		enablePartitioning();

		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.COMPLETED);
		info.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setPartitionIdForSecurity(myRequestPartitionId);
		info.setParameters(parameters);

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
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String path = "/Partition-B/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(403);
		assertThat(response.getReasonPhrase()).isEqualTo("Forbidden");
	}

	@Test
	public void testExportWhenNoResourcesReturned() throws IOException {
		// setup
		String msg = "Some msg";
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.COMPLETED);
		info.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		ArrayList<String> ids = new ArrayList<>();
		BulkExportJobResults results = new BulkExportJobResults();
		HashMap<String, List<String>> map = new HashMap<>();
		map.put("Patient", ids);
		results.setResourceTypeToBinaryIds(map);
		results.setReportMsg(msg);
		info.setReport(JsonUtil.serialize(results));

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(200);
		assertThat(response.getReasonPhrase()).isEqualTo("OK");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).isEqualTo(Constants.CT_JSON);

		BulkExportResponseJson responseJson = JsonUtil.deserialize(response.getBody(), BulkExportResponseJson.class);
		assertThat(responseJson.getMsg()).isEqualTo(msg);
	}

	@Test
	public void testPollForStatus_Gone() throws IOException {
		// setup

		// when
		when(myJobCoordinator.getInstance(anyString()))
			.thenThrow(new ResourceNotFoundException("Unknown job: AAA"));

		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(404);
		assertThat(response.getContentType()).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		assertThat(response.getBody()).contains("\"diagnostics\": \"Unknown job: AAA\"");
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
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse(G_JOB_ID));

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		Parameters input = new Parameters();
		StringType obsTypeFilter = new StringType("Observation?code=OBSCODE,DiagnosticReport?code=DRCODE");
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Observation, DiagnosticReport"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_UNTIL, later);
		input.addParameter(JpaConstants.PARAM_EXPORT_MDM, true);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, obsTypeFilter);

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpTestResponse response = myServer.fhirRequest("/" + GROUP_ID + "/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID);

		// verify
		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();

		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).containsExactlyInAnyOrder("Observation", "DiagnosticReport");
		assertNotNull(bp.getSince());
		assertNotNull(bp.getUntil());
		assertNotNull(bp.getFilters());
		assertEquals(GROUP_ID, bp.getGroupId());
		assertEquals(true, bp.isExpandMdm());
	}

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Get() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse(G_JOB_ID));

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		String path = "/" + GROUP_ID + "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_UNTIL + "=" + UrlUtil.escapeUrlParam(later.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?identifier=foo|bar")
			+ "&" + JpaConstants.PARAM_EXPORT_MDM + "=true";

		// call
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID);

		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).containsExactlyInAnyOrder("Patient", "Practitioner");
		assertNotNull(bp.getSince());
		assertNotNull(bp.getUntil());
		assertNotNull(bp.getFilters());
		assertEquals(GROUP_ID, bp.getGroupId());
		assertTrue(bp.isExpandMdm());
	}

	static List<String> getOmittedResourceTypes() {
		return SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT
			.keySet().stream().toList();
	}

	@ParameterizedTest
	@MethodSource("getOmittedResourceTypes")
	public void patientExport_withVerbotenResourceTypes_fails(String theResourceType) throws IOException {
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType(theResourceType));

		// call
		String responseStr = myServer.fhirRequest("/Patient/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(400)
			.getBody();
		assertThat(responseStr).contains("are invalid for this type of export");
	}

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Get_SomeTypesDisabled() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse(G_JOB_ID));

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		String path = "/" + GROUP_ID + "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_UNTIL + "=" + UrlUtil.escapeUrlParam(later.getValueAsString());

		// call
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID);

		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).as(bp.getResourceTypes().toString()).containsExactlyInAnyOrder("DiagnosticReport", "Group", "Observation", "Device", "Patient", "Encounter");
		assertNotNull(bp.getSince());
		assertNotNull(bp.getUntil());
		assertNotNull(bp.getFilters());
		assertEquals(GROUP_ID, bp.getGroupId());
		assertEquals(false, bp.isExpandMdm());
	}

	@Test
	public void testInitiateWithGetAndMultipleTypeFilters() throws IOException {
		// setup
		InstantType now = InstantType.now();

		// manual construct
		String path = "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Immunization, Observation")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_UNTIL + "=" + UrlUtil.escapeUrlParam(now.getValueAsString());

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

		path += multiValuedTypeFilterBuilder;

		// call
		myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get();

		// verify
		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertThat(bp.getFilters()).containsExactlyInAnyOrder(immunizationTypeFilter1, immunizationTypeFilter2, observationFilter1);
	}

	@Test
	public void testInitiateGroupExportWithInvalidResourceTypesFails() throws IOException {
		// when

		String path = "/" + "Group/123/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("StructureDefinition,Observation");

		String responseBody = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(400)
			.getBody();

		// verify
		assertThat(responseBody).contains("Resource types [StructureDefinition] are invalid for this type of export, as they do not contain search parameters that refer to patients.");
	}

	@Test
	public void testInitiateGroupExportWithNoResourceTypes() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse());

		// test
		String path = "/" + "Group/123/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON);

		myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);

		// verify
		final BulkExportJobParameters BulkExportJobParameters = verifyJobStartAndReturnParameters();
		assertThat(BulkExportJobParameters.getResourceTypes()).contains("Patient", "Group", "Device");
	}

	@Test
	public void testInitiateWithPostAndMultipleTypeFilters() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient"));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Patient?gender=male,Patient?gender=female"));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpTestResponse response = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		// verify
		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).containsExactlyInAnyOrder("Patient");
		assertThat(bp.getFilters()).containsExactlyInAnyOrder("Patient?gender=male", "Patient?gender=female");
	}

	@ParameterizedTest
	@ValueSource(strings = {"/Patient/" + ProviderConstants.OPERATION_EXPORT, "/Patient/p1/" + ProviderConstants.OPERATION_EXPORT})
	public void testInitiateBulkExportOnPatient_noTypeParam_addsTypeBeforeBulkExport(String mode) throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));

		// call
		HttpTestResponse response = myServer.fhirRequest(mode)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		// verify
		Set<String> expectedResourceTypes = SearchParameterUtil.getAllResourceTypesThatAreInPatientCompartment(myCtx)
			.stream().filter(r -> !SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT.containsKey(r)).collect(Collectors.toSet());
		expectedResourceTypes.add("Device");
		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).hasSameElementsAs(expectedResourceTypes);
	}

	@Test
	public void testInitiatePatientExportRequest() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		InstantType now = InstantType.now();
		InstantType later = InstantType.now();
		later.add(Calendar.DATE,1);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Immunization, Observation"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_UNTIL, later);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Immunization?vaccine-code=foo"));
		input.addParameter(JpaConstants.PARAM_EXPORT_PATIENT, new Reference("Patient/123"));
		input.addParameter(JpaConstants.PARAM_EXPORT_PATIENT, new StringType("Patient/456"));
		input.addParameter(JpaConstants.PARAM_EXPORT_MDM, true);

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpTestResponse response = myServer.fhirRequest("/Patient/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, bp.getOutputFormat());
		assertThat(bp.getResourceTypes()).containsExactlyInAnyOrder("Immunization", "Observation");
		assertNotNull(bp.getSince());
		assertNotNull(bp.getUntil());
		assertThat(bp.getFilters()).containsExactlyInAnyOrder("Immunization?vaccine-code=foo");
		assertThat(bp.getPatientIds()).containsExactlyInAnyOrder("Patient/123", "Patient/456");
		assertThat(bp.isExpandMdm()).isTrue();
	}

	@Test
	public void testPatientLevelWithMdmExpandNoPatientReference() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_MDM, true);

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// call
		HttpTestResponse response = myServer.fhirRequest("/Patient/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		BulkExportJobParameters bp = verifyJobStartAndReturnParameters();
		assertThat(bp.isExpandMdm()).isFalse();
	}

	@Test
	public void testProviderProcessesNoCacheHeader() throws IOException {
		// setup
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);

		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(startResponse);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		// call
		HttpTestResponse response = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.withHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		// verify
		JobInstanceStartRequest parameters = verifyJobStart();
		assertFalse(parameters.isUseCache());
	}

	@Test
	public void testProvider_whenEnableBatchJobReuseIsFalse_startsNewJob() throws IOException {
		// setup
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);

		myStorageSettings.setEnableBulkExportJobReuse(false);

		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(startResponse);

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		// call
		HttpTestResponse response = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);

		// verify
		JobInstanceStartRequest parameters = verifyJobStart();
		assertFalse(parameters.isUseCache());
	}

	@Test
	public void testProviderReturnsSameIdForSameJob() throws IOException {
		// given
		Batch2JobStartResponse startResponse = createJobStartResponse();
		startResponse.setUsesCachedResult(true);
		startResponse.setInstanceId(A_JOB_ID);
		when(myJobCoordinator.startInstance(isNotNull(), any()))
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

		BulkExportJobParameters parameters = new BulkExportJobParameters();

		JobInstance info = new JobInstance();
		info.setParameters(parameters);
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.QUEUED);
		info.setEndTime(InstantType.now().getValue());
		JobOperationResultJson result = new JobOperationResultJson();
		result.setOperation("Cancel job instance " + A_JOB_ID);
		result.setMessage("Job instance <" + A_JOB_ID + "> successfully cancelled.");
		result.setSuccess(true);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);
		when(myJobCoordinator.cancelInstance(eq(A_JOB_ID)))
			.thenReturn(result);

		// call
		String partitionPath;
		if (partitioningEnabled) {
			enablePartitioning();
			partitionPath = "/" + myPartitionName;
		} else {
			partitionPath = "";
		}

		String path = partitionPath + "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path).delete().assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");

		verify(myJobCoordinator, times(1)).cancelInstance(A_JOB_ID);
		assertThat(response.getBody()).contains("successfully cancelled.");
	}

	@Test
	public void testDeleteForOperationPollStatus_COMPLETE_ShouldReturnError() throws IOException {
		// setup
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.COMPLETED);
		info.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path).delete().assertStatus(404);
		assertThat(response.getReasonPhrase()).isEqualTo("Not Found");

		verify(myJobCoordinator, times(1)).cancelInstance(A_JOB_ID);
		String responseContent = response.getBody();
		// content would be blank, since the job is cancelled, so no
		assertThat(responseContent).contains("was already cancelled or has completed.");
		assertThat(responseContent).contains(OperationOutcome.IssueType.NOTFOUND.toCode());
	}

	@Test
	public void testGetForOperationPollStatus_CANCELLED_ShouldReturnError() throws IOException {
		// setup
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.CANCELLED);
		info.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// call
		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path).delete().assertStatus(404);
		assertThat(response.getReasonPhrase()).isEqualTo("Not Found");

		String responseContent = response.getBody();
		assertThat(responseContent).contains("was cancelled.  No status to report.");
		assertThat(responseContent).contains(OperationOutcome.IssueType.NOTFOUND.toCode());
	}

	@Test
	public void testGetExportPollStatus_QueuedJobBeingCancelled_return404() throws IOException {
		// Setup
		JobInstance instance = new JobInstance();
		instance.setInstanceId(A_JOB_ID);
		instance.setCancelled(true);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		instance.setParameters(parameters);

		when(myJobCoordinator.getInstance(eq(A_JOB_ID))).thenReturn(instance);

		String path = "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;

		// Execute
		HttpTestResponse response = myServer.fhirRequest(path).get();

		// Verify
		response.assertStatus(404);
		assertThat(response.getReasonPhrase()).isEqualTo("Not Found");
		String responseContent = response.getBody();
		assertThat(responseContent).contains(OperationOutcome.IssueType.NOTFOUND.toCode());
		assertThat(responseContent).contains("was cancelled.  No status to report.");
	}

	@Test
	public void testGetBulkExportByGroupId_urlParamNotEncoded_resultShouldBeFilteredByCriteria() throws IOException {
		// Setup
		when(myJobCoordinator.startInstance(isNotNull(), any())).thenReturn(createJobStartResponse(G_JOB_ID));

		String path = "/" + GROUP_ID + "/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + Constants.CT_FHIR_NDJSON
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=Patient,Observation"
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=Patient?gender=male"
			+ "&" + JpaConstants.PARAM_EXPORT_MDM + "=true";

		// Execute
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get();

		// Verify
		response.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + G_JOB_ID);

		BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertThat(params.getGroupId()).isEqualTo(GROUP_ID);
		assertThat(params.getOutputFormat()).isEqualTo(Constants.CT_FHIR_NDJSON);
		assertThat(params.getResourceTypes()).containsExactlyInAnyOrder("Patient", "Observation");
		assertThat(params.getFilters()).contains("Patient?gender=male");
		assertThat(params.isExpandMdm()).isTrue();
	}


	@ParameterizedTest
	@ValueSource(strings = {
		"$export",
		"Patient/$export",
		"Patient/<id>/$export",
		"Group/<id>/$export"
	})
	public void testBulkDataExport_hookOrder_isMaintained(String theUrl) throws IOException {
		// setup
		String path = "/" + theUrl.replaceAll("<id>", "1");
		AtomicBoolean preInitiateCalled = new AtomicBoolean(false);
		AtomicBoolean initiateCalled = new AtomicBoolean(false);

		// when
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.STORAGE_PRE_INITIATE_BULK_EXPORT))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.STORAGE_INITIATE_BULK_EXPORT))).thenReturn(true);
		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.STORAGE_PRE_INITIATE_BULK_EXPORT))).thenReturn(MockInvoker.list(params->{
				assertFalse(initiateCalled.get());
				assertFalse(preInitiateCalled.getAndSet(true));
				return true;
			}));
		when(myInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.STORAGE_INITIATE_BULK_EXPORT))).thenReturn(MockInvoker.list(params->{
				assertTrue(preInitiateCalled.get());
				assertFalse(initiateCalled.getAndSet(true));
				return true;
			}));
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		// test
		myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);

		// verify
		assertTrue(preInitiateCalled.get());
		assertTrue(initiateCalled.get());
	}

	@Test
	public void testGetBulkExport_outputFormat_FhirNdJson_inHeader() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		// call
		final HttpTestResponse response = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader("_outputFormat", Constants.CT_FHIR_NDJSON)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);
		assertThat(response.getBody()).isEmpty();

		final BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
	}

	@Test
	public void testGetBulkExport_outputFormat_FhirNdJson_inUrl() throws IOException {
		// when
		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		// call
		String path = "/" + ProviderConstants.OPERATION_EXPORT + "?_outputFormat=" + Constants.CT_FHIR_NDJSON;
		final HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + A_JOB_ID);
		assertThat(response.getBody()).isEmpty();

		final BulkExportJobParameters params = verifyJobStartAndReturnParameters();
		assertEquals(Constants.CT_FHIR_NDJSON, params.getOutputFormat());
	}

	@Test
	public void testOperationExportPollStatus_POST_NonExistingId_NotFound() throws IOException {
		String jobId = "NonExisting-JobId";

		when(myJobCoordinator.getInstance(any())).thenThrow(new ResourceNotFoundException("Unknown"));

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, new StringType(jobId));

		// Initiate Export Poll Status
		myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(404);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testOperationExportPollStatus_POST_ExistingId_Accepted(boolean partititioningEnabled) throws IOException {
		// setup
		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.QUEUED);
		info.setEndTime(InstantType.now().getValue());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, new StringType(A_JOB_ID));

		String partitionPath;
		if (partititioningEnabled) {
			enablePartitioning();
			partitionPath = "/" + myPartitionName;
		} else {
			partitionPath = "";
		}

		// Initiate Export Poll Status
		myServer.fhirRequest(partitionPath + "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(202);
	}

	@Test
	public void testOperationExportPollStatus_POST_MissingInputParameterJobId_BadRequest() throws IOException {

		// Create the initial launch Parameters containing the request
		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON));

		// Initiate Export Poll Status
		myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.post(input)
			.assertStatus(400);
	}

	private void callExportAndAssertJobId(Parameters input, String theExpectedJobId) throws IOException {
		HttpTestResponse response = myServer.fhirRequest("/" + ProviderConstants.OPERATION_EXPORT)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.withHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE)
			.post(input)
			.assertStatus(202);
		assertThat(response.getReasonPhrase()).isEqualTo("Accepted");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_LOCATION))
			.isEqualTo(myServer.getBaseUrl() + "/$export-poll-status?_jobId=" + theExpectedJobId);
	}

	@Test
	public void testFailBulkExportRequest_PartitionedWithoutPermissions() throws IOException {

		// setup
		enablePartitioning();

		// test
		String path = "/Partition-B/" + ProviderConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner");

		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(403);
		assertThat(response.getReasonPhrase()).isEqualTo("Forbidden");

	}

	@Test
	public void testFailPollRequest_PartitionedWithoutPermissions() throws IOException {
		// setup
		enablePartitioning();

		JobInstance info = new JobInstance();
		info.setInstanceId(A_JOB_ID);
		info.setStatus(StatusEnum.IN_PROGRESS);
		info.setEndTime(new Date());

		BulkExportJobParameters parameters = new BulkExportJobParameters();
		parameters.setPartitionIdForSecurity(myRequestPartitionId);
		info.setParameters(parameters);

		// when
		when(myJobCoordinator.getInstance(eq(A_JOB_ID)))
			.thenReturn(info);

		// test
		String path = "/Partition-B/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpTestResponse response = myServer.fhirRequest(path)
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC)
			.get()
			.assertStatus(403);
		assertThat(response.getReasonPhrase()).isEqualTo("Forbidden");
	}

	static Stream<Arguments> paramsProvider() {
		return Stream.of(
			Arguments.arguments(true),
			Arguments.arguments(false)
		);
	}

	private class MyRequestPartitionHelperSvc extends RequestPartitionHelperSvc {
		@Override
		public @Nonnull RequestPartitionId determineReadPartitionForRequest(@Nonnull RequestDetails theRequest, @Nonnull ReadPartitionIdRequestDetails theDetails) {
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

	}
}
