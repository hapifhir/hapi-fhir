package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkDataExportProviderTest {

	private static final String A_JOB_ID = "0000000-AAAAAA";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportProviderTest.class);
	private static final String GROUP_ID = "Group/G2401";
	private static final String G_JOB_ID = "0000000-GGGGGG";
	private Server myServer;
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private int myPort;
	@Mock
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	private CloseableHttpClient myClient;
	@Captor
	private ArgumentCaptor<BulkDataExportOptions> myBulkDataExportOptionsCaptor;
	@Captor
	private ArgumentCaptor<Boolean> myBooleanArgumentCaptor;

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		myClient.close();
	}

	@BeforeEach
	public void start() throws Exception {
		myServer = new Server(0);

		BulkDataExportProvider provider = new BulkDataExportProvider();
		provider.setBulkDataExportSvcForUnitTests(myBulkDataExportSvc);
		provider.setFhirContextForUnitTest(myCtx);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		myClient = builder.build();

	}

	@Test
	public void testSuccessfulInitiateBulkRequest_Post() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), nullable(RequestDetails.class))).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Patient?identifier=foo"));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), any(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(options.getSince(), notNullValue());
		assertThat(options.getFilters(), containsInAnyOrder("Patient?identifier=foo"));
	}


	@Test
	public void testSuccessfulInitiateBulkRequest_Get() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(),any(), nullable(RequestDetails.class))).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT
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
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), any(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(options.getSince(), notNullValue());
		assertThat(options.getFilters(), containsInAnyOrder("Patient?identifier=foo"));
	}


	@Test
	public void testSuccessfulInitiateBulkRequest_Get_MultipleTypeFilters() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(),any(), nullable(RequestDetails.class))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT
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
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), any(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Patient", "EpisodeOfCare"));
		assertThat(options.getSince(), nullValue());
		assertThat(options.getFilters(), containsInAnyOrder("Patient?_id=P999999990", "EpisodeOfCare?patient=P999999990"));
	}

	@Test
	public void testPollForStatus_BUILDING() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID)
			.setStatus(BulkExportJobStatusEnum.BUILDING)
			.setStatusTime(InstantType.now().getValue());
		when(myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Build in progress - Status set to BUILDING at 20"));
		}

	}

	@Test
	public void testPollForStatus_ERROR() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID)
			.setStatus(BulkExportJobStatusEnum.ERROR)
			.setStatusTime(InstantType.now().getValue())
			.setStatusMessage("Some Error Message");
		when(myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
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

	@Test
	public void testPollForStatus_COMPLETED() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID)
			.setStatus(BulkExportJobStatusEnum.COMPLETE)
			.setStatusTime(InstantType.now().getValue());
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/111"));
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/222"));
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/333"));
		when(myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
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
			assertEquals(3, responseJson.getOutput().size());
			assertEquals("Patient", responseJson.getOutput().get(0).getType());
			assertEquals("http://localhost:" + myPort + "/Binary/111", responseJson.getOutput().get(0).getUrl());
			assertEquals("Patient", responseJson.getOutput().get(1).getType());
			assertEquals("http://localhost:" + myPort + "/Binary/222", responseJson.getOutput().get(1).getUrl());
			assertEquals("Patient", responseJson.getOutput().get(2).getType());
			assertEquals("http://localhost:" + myPort + "/Binary/333", responseJson.getOutput().get(2).getUrl());
		}
	}

	@Test
	public void testPollForStatus_Gone() throws IOException {

		when(myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(eq(A_JOB_ID))).thenThrow(new ResourceNotFoundException("Unknown job: AAA"));

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());
			String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response content: {}", responseContent);

			assertEquals(404, response.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_FHIR_JSON_NEW, response.getEntity().getContentType().getValue().replaceAll(";.*", "").trim());
			assertThat(responseContent, containsString("\"diagnostics\":\"Unknown job: AAA\""));
		}

	}

	/**
	 * Group export tests
	 * See https://build.fhir.org/ig/HL7/us-bulk-data/
	 * <p>
	 * GET [fhir base]/Group/[id]/$export
	 * <p>
	 * FHIR Operation to obtain data on all patients listed in a single FHIR Group Resource.
	 */

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Post() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo().setJobId(G_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(),any(), nullable(RequestDetails.class))).thenReturn(jobInfo);
		when(myBulkDataExportSvc.getPatientCompartmentResources()).thenReturn(Sets.newHashSet("Observation", "DiagnosticReport"));

		InstantType now = InstantType.now();


		Parameters input = new Parameters();
		StringType obsTypeFilter = new StringType("Observation?code=OBSCODE,DiagnosticReport?code=DRCODE");
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Observation, DiagnosticReport"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_MDM, true);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, obsTypeFilter);

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + GROUP_ID + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + G_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), any(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Observation", "DiagnosticReport"));
		assertThat(options.getSince(), notNullValue());
		assertThat(options.getFilters(), notNullValue());
		assertEquals(GROUP_ID, options.getGroupId().getValue());
		assertThat(options.isExpandMdm(), is(equalTo(true)));
	}

	@Test
	public void testSuccessfulInitiateGroupBulkRequest_Get() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo().setJobId(G_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), nullable(RequestDetails.class))).thenReturn(jobInfo);
		when(myBulkDataExportSvc.getPatientCompartmentResources()).thenReturn(Sets.newHashSet("Patient", "Practitioner"));

		InstantType now = InstantType.now();

		String url = "http://localhost:" + myPort + "/" + GROUP_ID + "/"  + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString())
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE_FILTER + "=" + UrlUtil.escapeUrlParam("Patient?identifier=foo|bar")
			+ "&" + JpaConstants.PARAM_EXPORT_MDM+ "=true";

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		ourLog.info("Request: {}", url);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + G_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), any(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(options.getSince(), notNullValue());
		assertThat(options.getFilters(), notNullValue());
		assertEquals(GROUP_ID, options.getGroupId().getValue());
		assertThat(options.isExpandMdm(), is(equalTo(true)));
	}

	@Test
	public void testInitiateWithGetAndMultipleTypeFilters() throws IOException {
		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any())).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Immunization, Observation")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE + "=" + UrlUtil.escapeUrlParam(now.getValueAsString());

		String immunizationTypeFilter1 = "Immunization?patient.identifier=SC378274-MRN|009999997,SC378274-MRN|009999998,SC378274-MRN|009999999&date=2020-01-02";
		String immunizationTypeFilter2 = "Immunization?patient=Patient/123";
		String observationFilter1 = "Observation?subject=Patient/123&created=ge2020-01-01";
		StringBuilder multiValuedTypeFilterBuilder = new StringBuilder()
			.append("&")
			.append(JpaConstants.PARAM_EXPORT_TYPE_FILTER)
			.append("=")
			.append(UrlUtil.escapeUrlParam(immunizationTypeFilter1))
			.append(",")
			.append(UrlUtil.escapeUrlParam(immunizationTypeFilter2))
			.append(",")
			.append(UrlUtil.escapeUrlParam(observationFilter1));

		url += multiValuedTypeFilterBuilder.toString();

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		myClient.execute(get);

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), anyBoolean(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();

		assertThat(options.getFilters(), containsInAnyOrder(immunizationTypeFilter1, immunizationTypeFilter2, observationFilter1));
	}


	@Test
	public void testInitiateGroupExportWithInvalidResourceTypesFails() throws IOException {
		when (myBulkDataExportSvc.getPatientCompartmentResources()).thenReturn(Sets.newHashSet("Observation"));
		String url = "http://localhost:" + myPort + "/" + "Group/123/" +JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("StructureDefinition,Observation");

		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		CloseableHttpResponse execute = myClient.execute(get);
		String responseBody = IOUtils.toString(execute.getEntity().getContent());

		assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(400)));
		assertThat(responseBody, is(containsString("Resource types [StructureDefinition] are invalid for this type of export, as they do not contain search parameters that refer to patients.")));
	}

	@Test
	public void testInitiateGroupExportWithNoResourceTypes() throws IOException {
		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), nullable(RequestDetails.class))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + "Group/123/" +JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON);

        HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		CloseableHttpResponse execute = myClient.execute(get);

		assertThat(execute.getStatusLine().getStatusCode(), is(equalTo(202)));
	}

	@Test
	public void testInitiateWithPostAndMultipleTypeFilters() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), nullable(RequestDetails.class))).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient"));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Patient?gender=male,Patient?gender=female"));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), myBooleanArgumentCaptor.capture(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Patient"));
		assertThat(options.getFilters(), containsInAnyOrder("Patient?gender=male", "Patient?gender=female"));
	}

	@Test
	public void testInitiatePatientExportRequest() throws IOException {
		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), nullable(RequestDetails.class))).thenReturn(jobInfo);
		when(myBulkDataExportSvc.getPatientCompartmentResources()).thenReturn(Sets.newHashSet("Immunization", "Observation"));

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Immunization, Observation"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Immunization?vaccine-code=foo"));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/Patient/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

		verify(myBulkDataExportSvc, times(1)).submitJob(myBulkDataExportOptionsCaptor.capture(), myBooleanArgumentCaptor.capture(), nullable(RequestDetails.class));
		BulkDataExportOptions options = myBulkDataExportOptionsCaptor.getValue();
		assertEquals(Constants.CT_FHIR_NDJSON, options.getOutputFormat());
		assertThat(options.getResourceTypes(), containsInAnyOrder("Immunization", "Observation"));
		assertThat(options.getSince(), notNullValue());
		assertThat(options.getFilters(), containsInAnyOrder("Immunization?vaccine-code=foo"));
	}

	@Test
	public void testProviderProcessesNoCacheHeader() throws IOException {
		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), anyBoolean(), nullable(RequestDetails.class))).thenReturn(jobInfo);


		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE);
		post.setEntity(new ResourceEntity(myCtx, input));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = myClient.execute(post)) {
			ourLog.info("Response: {}", response.toString());
			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}


		verify(myBulkDataExportSvc).submitJob(myBulkDataExportOptionsCaptor.capture(), myBooleanArgumentCaptor.capture(), nullable(RequestDetails.class));
		Boolean usedCache = myBooleanArgumentCaptor.getValue();
		assertThat(usedCache, is(equalTo(false)));
	}

}
