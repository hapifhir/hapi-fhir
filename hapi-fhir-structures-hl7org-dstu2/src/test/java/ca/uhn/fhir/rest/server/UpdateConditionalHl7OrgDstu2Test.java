package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.JettyUtil;

public class UpdateConditionalHl7OrgDstu2Test {
	private static CloseableHttpClient ourClient;
	private static String ourLastConditionalUrl;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpdateConditionalHl7OrgDstu2Test.class);
	private static int ourPort;
	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static Server ourServer;
	private static String ourLastId;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasSearch;
	
	
	
	@Before
	public void before() {
		ourLastId = null;
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasSearch = false;
	}

	@Test
	public void testUpdateWithConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(null, status.getFirstHeader("location"));
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());
		
		assertNull(ourLastId);
		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);

	}

	@Test
	public void testUpdateWithoutConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.setId("2");
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(null, status.getFirstHeader("location"));
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());
		
		assertEquals("Patient/2", new IdType(ourLastId).toUnqualified().getValue());
		assertEquals("Patient/2", ourLastIdParam.toUnqualified().getValue());
		assertNull(ourLastConditionalUrl);

	}

	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true");

		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertTrue(ourLastRequestWasSearch);
		assertNull(ourLastId);
		assertNull(ourLastIdParam);
		assertNull(ourLastConditionalUrl);

	}

	
	@AfterClass
	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
	}
		
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name="foo") StringDt theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<IResource>();
		}
		
		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdType theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastId = thePatient.getId();
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

	}

}
