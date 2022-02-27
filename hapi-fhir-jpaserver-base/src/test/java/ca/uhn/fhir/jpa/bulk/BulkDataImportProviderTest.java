package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imprt.provider.BulkDataImportProvider;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BulkDataImportProviderTest {
        private static final String A_JOB_ID = "0000000-AAAAAA";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportProviderTest.class);
	private Server myServer;
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private int myPort;
	@Mock
	private IBulkDataImportSvc myBulkDataImportSvc;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	private CloseableHttpClient myClient;
	@Captor
	private ArgumentCaptor<BulkImportJobJson> myBulkImportJobJsonCaptor;
        @Captor
        private ArgumentCaptor<List<BulkImportJobFileJson>> myBulkImportJobFileJsonCaptor;

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
		myClient.close();
	}

	@BeforeEach
	public void start() throws Exception {
		myServer = new Server(0);

		BulkDataImportProvider provider = new BulkDataImportProvider();
		provider.setBulkDataImportSvcForUnitTests(myBulkDataImportSvc);
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
                when(myBulkDataImportSvc.createNewJob(any(), any())).thenReturn(A_JOB_ID);

                HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT +
                        "?" + JpaConstants.PARAM_IMPORT_JOB_DESCRIPTION + "=" + UrlUtil.escapeUrlParam("My Import Job") +
                        "&" + JpaConstants.PARAM_IMPORT_BATCH_SIZE + "=" + UrlUtil.escapeUrlParam("100"));

                post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
                post.setEntity(
                    EntityBuilder.create()
                    .setContentType(ContentType.create(Constants.CT_FHIR_NDJSON))
                    .setText("{\"resourceType\":\"Patient\",\"id\":\"Pat1\"}\n" +
                             "{\"resourceType\":\"Patient\",\"id\":\"Pat2\"}\n")
                    .build());

                ourLog.info("Request: {}", post);
                try (CloseableHttpResponse response = myClient.execute(post)) {
                        ourLog.info("Response: {}", EntityUtils.toString(response.getEntity()));
                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("http://localhost:" + myPort + "/$import-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
                }

                verify(myBulkDataImportSvc, times(1)).createNewJob(myBulkImportJobJsonCaptor.capture(), myBulkImportJobFileJsonCaptor.capture());
                BulkImportJobJson options = myBulkImportJobJsonCaptor.getValue();
                assertEquals(1, options.getFileCount());
                assertEquals(100, options.getBatchSize());
                assertEquals(JobFileRowProcessingModeEnum.FHIR_TRANSACTION, options.getProcessingMode());
                assertEquals("My Import Job", options.getJobDescription());
                List<BulkImportJobFileJson> jobs = myBulkImportJobFileJsonCaptor.getValue();
                assertEquals(1, jobs.size());
                assertThat(jobs.get(0).getContents(), containsString("Pat1"));
        }

        @Test
        public void testSuccessfulInitiateBulkRequest_Post_AllParameters() throws IOException {
                when(myBulkDataImportSvc.createNewJob(any(), any())).thenReturn(A_JOB_ID);

                HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT +
                        "?" + JpaConstants.PARAM_IMPORT_JOB_DESCRIPTION + "=" + UrlUtil.escapeUrlParam("My Import Job") +
                        "&" + JpaConstants.PARAM_IMPORT_PROCESSING_MODE + "=" + UrlUtil.escapeUrlParam(JobFileRowProcessingModeEnum.FHIR_TRANSACTION.toString()) +
                        "&" + JpaConstants.PARAM_IMPORT_BATCH_SIZE + "=" + UrlUtil.escapeUrlParam("100") +
                        "&" + JpaConstants.PARAM_IMPORT_FILE_COUNT + "=" + UrlUtil.escapeUrlParam("1"));

                post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
                post.setEntity(
                    EntityBuilder.create()
                    .setContentType(ContentType.create(Constants.CT_FHIR_NDJSON))
                    .setText("{\"resourceType\":\"Patient\",\"id\":\"Pat1\"}\n" +
                             "{\"resourceType\":\"Patient\",\"id\":\"Pat2\"}\n")
                    .build());

                ourLog.info("Request: {}", post);
                try (CloseableHttpResponse response = myClient.execute(post)) {
                        ourLog.info("Response: {}", EntityUtils.toString(response.getEntity()));
                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("http://localhost:" + myPort + "/$import-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
                }

                verify(myBulkDataImportSvc, times(1)).createNewJob(myBulkImportJobJsonCaptor.capture(), myBulkImportJobFileJsonCaptor.capture());
                BulkImportJobJson options = myBulkImportJobJsonCaptor.getValue();
                assertEquals(1, options.getFileCount());
                assertEquals(100, options.getBatchSize());
                assertEquals(JobFileRowProcessingModeEnum.FHIR_TRANSACTION, options.getProcessingMode());
                assertEquals("My Import Job", options.getJobDescription());
                List<BulkImportJobFileJson> jobs = myBulkImportJobFileJsonCaptor.getValue();
                assertEquals(1, jobs.size());
                assertThat(jobs.get(0).getContents(), containsString("Pat1"));
        }

	@Test
	public void testPollForStatus_STAGING() throws IOException {

		IBulkDataImportSvc.JobInfo jobInfo = new IBulkDataImportSvc.JobInfo()
			.setStatus(BulkImportJobStatusEnum.STAGING)
			.setStatusTime(InstantType.now().getValue());
		when(myBulkDataImportSvc.getJobStatus(eq(A_JOB_ID))).thenReturn(jobInfo);

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
			assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Status set to STAGING at 20"));
		}
	}

        @Test
        public void testPollForStatus_READY() throws IOException {

                IBulkDataImportSvc.JobInfo jobInfo = new IBulkDataImportSvc.JobInfo()
                        .setStatus(BulkImportJobStatusEnum.READY)
                        .setStatusTime(InstantType.now().getValue());
                when(myBulkDataImportSvc.getJobStatus(eq(A_JOB_ID))).thenReturn(jobInfo);

                String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
                        JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
                HttpGet get = new HttpGet(url);
                get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
                try (CloseableHttpResponse response = myClient.execute(get)) {
                        ourLog.info("Response: {}", response.toString());

                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
                        assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Status set to READY at 20"));
                }
        }

        @Test
        public void testPollForStatus_RUNNING() throws IOException {

                IBulkDataImportSvc.JobInfo jobInfo = new IBulkDataImportSvc.JobInfo()
                        .setStatus(BulkImportJobStatusEnum.RUNNING)
                        .setStatusTime(InstantType.now().getValue());
                when(myBulkDataImportSvc.getJobStatus(eq(A_JOB_ID))).thenReturn(jobInfo);

                String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
                        JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
                HttpGet get = new HttpGet(url);
                get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
                try (CloseableHttpResponse response = myClient.execute(get)) {
                        ourLog.info("Response: {}", response.toString());

                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("120", response.getFirstHeader(Constants.HEADER_RETRY_AFTER).getValue());
                        assertThat(response.getFirstHeader(Constants.HEADER_X_PROGRESS).getValue(), containsString("Status set to RUNNING at 20"));
                }
        }

        @Test
        public void testPollForStatus_COMPLETE() throws IOException {
                IBulkDataImportSvc.JobInfo jobInfo = new IBulkDataImportSvc.JobInfo()
                        .setStatus(BulkImportJobStatusEnum.COMPLETE)
                        .setStatusTime(InstantType.now().getValue());
                when(myBulkDataImportSvc.getJobStatus(eq(A_JOB_ID))).thenReturn(jobInfo);

                String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
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
                IBulkDataImportSvc.JobInfo jobInfo = new IBulkDataImportSvc.JobInfo()
                        .setStatus(BulkImportJobStatusEnum.ERROR)
                        .setStatusMessage("It failed.")
                        .setStatusTime(InstantType.now().getValue());
                when(myBulkDataImportSvc.getJobStatus(eq(A_JOB_ID))).thenReturn(jobInfo);

                String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" +
                        JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
                HttpGet get = new HttpGet(url);
                get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
                try (CloseableHttpResponse response = myClient.execute(get)) {
                        ourLog.info("Response: {}", response.toString());

                        assertEquals(500, response.getStatusLine().getStatusCode());
                        assertEquals("Server Error", response.getStatusLine().getReasonPhrase());
                        String responseContent = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
                        ourLog.info("Response content: {}", responseContent);
                        assertThat(responseContent, containsString("\"diagnostics\": \"It failed.\""));
                }
        }
}
