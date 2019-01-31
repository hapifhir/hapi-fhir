package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
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
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ElementsParamR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ElementsParamR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static Set<String> ourLastElements;
	private static int ourPort;
	private static Server ourServer;
	private static Procedure ourNextProcedure;

	@Before
	public void before() {
		ourLastElements = null;
		ourNextProcedure = null;
	}

	@Test
	public void testReadSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_elements=name,maritalStatus");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_FHIR_XML_NEW + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
			assertThat(responseContent, not(containsString("<Bundle")));
			assertThat(responseContent, (containsString("<Patient")));
			assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
			assertThat(responseContent, (containsString("family")));
			assertThat(responseContent, (containsString("maritalStatus")));
			assertThat(ourLastElements, containsInAnyOrder("meta", "name", "maritalStatus"));
		}
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_elements=name");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(Constants.CT_FHIR_XML_NEW + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
			assertThat(responseContent, not(containsString("<Bundle")));
			assertThat(responseContent, (containsString("<Patient")));
			assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
			assertThat(responseContent, (containsString("family")));
			assertThat(responseContent, not(containsString("maritalStatus")));
			assertThat(ourLastElements, containsInAnyOrder("meta", "name"));
		}
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_elements=name,maritalStatus");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("<Patient"));
			assertThat(responseContent, not(containsString("THE DIV")));
			assertThat(responseContent, containsString("family"));
			assertThat(responseContent, containsString("maritalStatus"));
			assertThat(ourLastElements, containsInAnyOrder("meta", "name", "maritalStatus"));
		}
	}

	@Test
	public void testSearchSummaryText() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_elements=text");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseContent, (containsString("<total value=\"1\"/>")));
			assertThat(responseContent, (containsString("entry")));
			assertThat(responseContent, (containsString("THE DIV")));
			assertThat(responseContent, not(containsString("family")));
			assertThat(responseContent, not(containsString("maritalStatus")));
			assertThat(ourLastElements, containsInAnyOrder("meta", "text"));
		}
	}

	@Test
	public void testMultiResourceElementsFilter() throws IOException {
		{
			ourNextProcedure = new Procedure();
			ourNextProcedure.setId("Procedure/PROC");
			ourNextProcedure.addReasonCode().addCoding().setCode("REASON_CODE");
			ourNextProcedure.addUsedCode().addCoding().setCode("USED_CODE");

			DiagnosticReport dr = new DiagnosticReport();
			dr.setId("DiagnosticReport/DRA");
			ourNextProcedure.addReport().setResource(dr);

			Observation obs = new Observation();
			obs.setId("Observation/OBSA");
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.setSubject(new Reference("Patient/123"));
			obs.getCode().addCoding().setSystem("http://loinc.org").setCode("1234-5");
			obs.setValue(new StringType("STRING VALUE"));
			dr.addResult().setResource(obs);
		}

		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Procedure?_include=*&_pretty=true&_elements=reasonCode,status",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(0, dr.getMeta().getTag().size());

				Observation obs = (Observation ) bundle.getEntry().get(2).getResource();
				assertEquals(0, obs.getMeta().getTag().size());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals("1234-5", obs.getCode().getCoding().get(0).getCode());
			});

	}

	private void verifyXmlAndJson(String theUri, Consumer<Bundle> theVerifier) throws IOException {
		EncodingEnum theEncoding = EncodingEnum.XML;
		HttpGet httpGet = new HttpGet(theUri + "&_format=" + theEncoding.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			Bundle response = theEncoding.newParser(ourCtx).parseResource(Bundle.class, responseContent);
			theVerifier.accept(response);
		}
		theEncoding = EncodingEnum.JSON;
		httpGet = new HttpGet(theUri + "&_format=" + theEncoding.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			Bundle response = theEncoding.newParser(ourCtx).parseResource(Bundle.class, responseContent);
			theVerifier.accept(response);
		}
	}

	public static class DummyProcedureResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Procedure.class;
		}

		@Search
		public Procedure search(@IncludeParam(allow = {"*"}) Collection<Include> theIncludes) {
			return ourNextProcedure;
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId, @Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().getDiv().setValueAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

		@Search()
		public Patient search(@Elements Set<String> theElements) {
			ourLastElements = theElements;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().getDiv().setValueAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.registerProvider(new DummyPatientResourceProvider());
		servlet.registerProvider(new DummyProcedureResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
