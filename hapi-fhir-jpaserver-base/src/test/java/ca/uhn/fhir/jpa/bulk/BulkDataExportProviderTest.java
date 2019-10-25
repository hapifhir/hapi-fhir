package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.JsonUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequestInterceptor;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BulkDataExportProviderTest {

	private static final String A_JOB_ID = "0000000-AAAAAA";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportProviderTest.class);
	private Server myServer;
	private FhirContext myCtx = FhirContext.forR4();
	private int myPort;
	@Mock
	private IBulkDataExportSvc myBulkDataExportSvc;
	private CloseableHttpClient myClient;
	@Captor
	private ArgumentCaptor<String> myOutputFormatCaptor;
	@Captor
	private ArgumentCaptor<Set<String>> myResourceTypesCaptor;
	@Captor
	private ArgumentCaptor<Date> mySinceCaptor;
	@Captor
	private ArgumentCaptor<Set<String>> myFiltersCaptor;

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		myClient.close();
	}

	@Before
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
		when(myBulkDataExportSvc.submitJob(any(), any(), any(), any())).thenReturn(jobInfo);

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

		verify(myBulkDataExportSvc, times(1)).submitJob(myOutputFormatCaptor.capture(), myResourceTypesCaptor.capture(), mySinceCaptor.capture(), myFiltersCaptor.capture());
		assertEquals(Constants.CT_FHIR_NDJSON, myOutputFormatCaptor.getValue());
		assertThat(myResourceTypesCaptor.getValue(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(mySinceCaptor.getValue(), notNullValue());
		assertThat(myFiltersCaptor.getValue(), containsInAnyOrder("Patient?identifier=foo"));

	}

	@Test
	public void testSuccessfulInitiateBulkRequest_Get() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), any(), any())).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT
			+ "?" + JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT + "=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_NDJSON)
			+ "&" + JpaConstants.PARAM_EXPORT_TYPE + "=" + UrlUtil.escapeUrlParam("Patient, Practitioner")
			+ "&" + JpaConstants.PARAM_EXPORT_SINCE+ "="+  UrlUtil.escapeUrlParam(now.getValueAsString())
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

		verify(myBulkDataExportSvc, times(1)).submitJob(myOutputFormatCaptor.capture(), myResourceTypesCaptor.capture(), mySinceCaptor.capture(), myFiltersCaptor.capture());
		assertEquals(Constants.CT_FHIR_NDJSON, myOutputFormatCaptor.getValue());
		assertThat(myResourceTypesCaptor.getValue(), containsInAnyOrder("Patient", "Practitioner"));
		assertThat(mySinceCaptor.getValue(), notNullValue());
		assertThat(myFiltersCaptor.getValue(), containsInAnyOrder("Patient?identifier=foo"));

	}

	@Test
	public void testPollForStatus_BUILDING() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID)
			.setStatus(BulkJobStatusEnum.BUILDING)
			.setStatusTime(InstantType.now().getValue());
		when(myBulkDataExportSvc.getJobStatusOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

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
			.setStatus(BulkJobStatusEnum.ERROR)
			.setStatusTime(InstantType.now().getValue())
			.setStatusMessage("Some Error Message");
		when(myBulkDataExportSvc.getJobStatusOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

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
			.setStatus(BulkJobStatusEnum.COMPLETE)
			.setStatusTime(InstantType.now().getValue());
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/111"));
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/222"));
		jobInfo.addFile().setResourceType("Patient").setResourceId(new IdType("Binary/333"));
		when(myBulkDataExportSvc.getJobStatusOrThrowResourceNotFound(eq(A_JOB_ID))).thenReturn(jobInfo);

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

		when(myBulkDataExportSvc.getJobStatusOrThrowResourceNotFound(eq(A_JOB_ID))).thenThrow(new ResourceNotFoundException("Unknown job: AAA"));

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


}
