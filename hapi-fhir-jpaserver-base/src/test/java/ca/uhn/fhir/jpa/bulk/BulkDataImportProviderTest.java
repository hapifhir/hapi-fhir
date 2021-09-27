package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.provider.BulkDataImportProvider;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.test.utilities.JettyUtil;
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
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
                    .setText("{\"resourceType\":\"Patient\",\"id\":\"P1\"}\n" +
                             "{\"resourceType\":\"Patient\",\"id\":\"P2\"}\n")
                    .build());

                ourLog.info("Request: {}", post);
                try (CloseableHttpResponse response = myClient.execute(post)) {
                        ourLog.info("Response: {}", EntityUtils.toString(response.getEntity()));
                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("http://localhost:" + myPort + "/$import-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
                }

                verify(myBulkDataImportSvc, times(1)).createNewJob(myBulkImportJobJsonCaptor.capture(), any());
                BulkImportJobJson options = myBulkImportJobJsonCaptor.getValue();
                assertEquals(1, options.getFileCount());
                assertEquals(100, options.getBatchSize());
                assertEquals(JobFileRowProcessingModeEnum.FHIR_TRANSACTION, options.getProcessingMode());
                assertEquals("My Import Job", options.getJobDescription());
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
                    .setText("{\"resourceType\":\"Patient\",\"id\":\"P1\"}\n" +
                             "{\"resourceType\":\"Patient\",\"id\":\"P2\"}\n")
                    .build());

                ourLog.info("Request: {}", post);
                try (CloseableHttpResponse response = myClient.execute(post)) {
                        ourLog.info("Response: {}", EntityUtils.toString(response.getEntity()));
                        assertEquals(202, response.getStatusLine().getStatusCode());
                        assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
                        assertEquals("http://localhost:" + myPort + "/$import-poll-status?_jobId=" + A_JOB_ID, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
                }

                verify(myBulkDataImportSvc, times(1)).createNewJob(myBulkImportJobJsonCaptor.capture(), any());
                BulkImportJobJson options = myBulkImportJobJsonCaptor.getValue();
                assertEquals(1, options.getFileCount());
                assertEquals(100, options.getBatchSize());
                assertEquals(JobFileRowProcessingModeEnum.FHIR_TRANSACTION, options.getProcessingMode());
                assertEquals("My Import Job", options.getJobDescription());
        }
}
