package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BulkExportProviderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportProviderTest.class);
	public static final String A_JOB_ID = "0000000-AAAAAA";
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

		BulkExportProvider provider = new BulkExportProvider();
		provider.setBulkDataExportSvcForUnitTests(myBulkDataExportSvc);

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
	public void testSuccessfulInitiateBulkRequest() throws IOException {

		IBulkDataExportSvc.JobInfo jobInfo = new IBulkDataExportSvc.JobInfo()
			.setJobId(A_JOB_ID);
		when(myBulkDataExportSvc.submitJob(any(), any(), any(), any())).thenReturn(jobInfo);

		InstantType now = InstantType.now();

		Parameters input = new Parameters();
		input.addParameter(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, new StringType(Constants.CT_FHIR_NDJSON));
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE, new StringType("Patient, Practitioner"));
		input.addParameter(JpaConstants.PARAM_EXPORT_SINCE, now);
		input.addParameter(JpaConstants.PARAM_EXPORT_TYPE_FILTER, new StringType("Patient?identifier=foo"));

		HttpPost post = new HttpPost("http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.setEntity(new ResourceEntity(myCtx, input));
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
	public void testPollForStatus() throws IOException {

		String url = "http://localhost:" + myPort + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" +
			JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + A_JOB_ID;
		HttpGet get = new HttpGet(url);
		get.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = myClient.execute(get)) {
			ourLog.info("Response: {}", response.toString());

			assertEquals(202, response.getStatusLine().getStatusCode());
			assertEquals("Accepted", response.getStatusLine().getReasonPhrase());
			assertEquals("http://localhost:" + myPort + "/$export-poll-status?_jobId=0000000-AAAAAA", response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
		}

	}

}
