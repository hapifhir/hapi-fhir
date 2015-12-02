package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.util.PortUtil;

public class ElementsParamTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static Set<String> ourLastElements;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ElementsParamTest.class);
	private static int ourPort;
	private static Server ourServer;

	@Before
	public void before() {
		ourLastElements = null;
	}

	@Test
	public void testReadSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_elements=name,maritalStatus");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, (containsString("<Patien")));
		assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
		assertThat(responseContent, (containsString("family")));
		assertThat(responseContent, (containsString("maritalStatus")));
		assertThat(ourLastElements, containsInAnyOrder("name", "maritalStatus"));
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_elements=name");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, (containsString("<Patien")));
		assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
		assertThat(responseContent, (containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertThat(ourLastElements, containsInAnyOrder("name"));
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_elements=name,maritalStatus");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient"));
		assertThat(responseContent, not(containsString("THE DIV")));
		assertThat(responseContent, containsString("family"));
		assertThat(responseContent, containsString("maritalStatus"));
		assertThat(ourLastElements, containsInAnyOrder("name", "maritalStatus"));
	}


	@Test
	public void testSearchSummaryText() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_elements=text");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, (containsString("entry")));
		assertThat(responseContent, (containsString("THE DIV")));
		assertThat(responseContent, not(containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertThat(ourLastElements, containsInAnyOrder("text"));
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId, @Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDiv("<div>THE DIV</div>");
			patient.addName().addFamily("FAMILY");
			patient.setMaritalStatus(MaritalStatusCodesEnum.D);
			return patient;
		}

		@Search()
		public Patient search(@Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDiv("<div>THE DIV</div>");
			patient.addName().addFamily("FAMILY");
			patient.setMaritalStatus(MaritalStatusCodesEnum.D);
			return patient;
		}

	}

}
