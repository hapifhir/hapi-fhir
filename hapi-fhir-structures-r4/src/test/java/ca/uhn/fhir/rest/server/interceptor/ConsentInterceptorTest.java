package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentOutcome;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//import static org.hamcrest.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ConsentInterceptorTest.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static RestfulServer ourServlet;
	private static Server ourServer;
	@Mock
	private IConsentService myConsentSvc;
	private ConsentInterceptor myInterceptor;

	@After
	public void after() {
		ourServlet.unregisterInterceptor(myInterceptor);
	}

	@Before
	public void before() {
		myInterceptor = new ConsentInterceptor(myConsentSvc);
		ourServlet.registerInterceptor(myInterceptor);
	}

	@Test
	public void testException() throws IOException {
		when(myConsentSvc.startOperation(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.canSeeResource(any(), any())).thenReturn(ConsentOutcome.PROCEED);
		when(myConsentSvc.seeResource(any(), any())).thenReturn(ConsentOutcome.PROCEED);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?searchThrowNullPointerException=1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(500, status.getStatusLine().getStatusCode());
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		}

		verify(myConsentSvc, times(0)).completeOperationSuccess(any());
		verify(myConsentSvc, times(1)).completeOperationSuccess(any());

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		/*
		 * searchThrowNullPointerException
		 */
		@Search()
		public List<IBaseResource> searchWithWildcardRetVal(@RequiredParam(name = "searchThrowNullPointerException") StringParam theValue) {
			throw new NullPointerException("A MESSAGE");
		}

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourClient.close();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourLog.info("Using port: {}", ourPort);
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler servletHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ourServlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		servletHandler.addServletWithMapping(servletHolder, "/*");

		ourServer.setHandler(servletHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
