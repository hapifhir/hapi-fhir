package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
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
import static org.junit.Assert.*;

public class ElementsParamR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ElementsParamR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static Set<String> ourLastElements;
	private static int ourPort;
	private static Server ourServer;
	private static Procedure ourNextProcedure;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourLastElements = null;
		ourNextProcedure = null;
		ourServlet.setElementsSupport(new RestfulServer().getElementsSupport());
	}

	@Test
	public void testReadSummaryData() throws Exception {
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Patient/1?_elements=name,maritalStatus",
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent, not(containsString("<Bundle")));
				assertThat(responseContent, (containsString("<Patient")));
				assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
				assertThat(responseContent, (containsString("family")));
				assertThat(responseContent, (containsString("maritalStatus")));
				assertThat(ourLastElements, containsInAnyOrder("meta", "name", "maritalStatus"));
			}
		);
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Patient/1?_elements=name",
			Patient.class,
			patient -> {
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(patient);
				assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
				assertThat(responseContent, (containsString("family")));
				assertThat(responseContent, not(containsString("maritalStatus")));
				assertThat(ourLastElements, containsInAnyOrder("meta", "name"));
			}
		);
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Patient?_elements=name,maritalStatus",
			bundle -> {
				assertEquals("1", bundle.getTotalElement().getValueAsString());
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle.getEntry().get(0).getResource());
				assertThat(responseContent, containsString("<Patient"));
				assertThat(responseContent, not(containsString("THE DIV")));
				assertThat(responseContent, containsString("family"));
				assertThat(responseContent, containsString("maritalStatus"));
				assertThat(ourLastElements, containsInAnyOrder("meta", "name", "maritalStatus"));
			}
		);
	}

	@Test
	public void testSearchSummaryText() throws Exception {
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Patient?_elements=text&_pretty=true",
			bundle -> {
				assertEquals("1", bundle.getTotalElement().getValueAsString());
				String responseContent = ourCtx.newXmlParser().encodeResourceToString(bundle.getEntry().get(0).getResource());
				assertThat(responseContent, containsString("THE DIV"));
				assertThat(responseContent, not(containsString("family")));
				assertThat(responseContent, not(containsString("maritalStatus")));
				assertThat(ourLastElements, containsInAnyOrder("meta", "text"));
			}
		);
	}

	/**
	 * By default the elements apply only to the focal resource in a search
	 * and not any included resources
	 */
	@Test
	public void testStandardElementsFilter() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Procedure?_include=*&_elements=reasonCode,status",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(0, dr.getMeta().getTag().size());
				assertEquals("Observation/OBSA", dr.getResult().get(0).getReference());

				Observation obs = (Observation ) bundle.getEntry().get(2).getResource();
				assertEquals(0, obs.getMeta().getTag().size());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals("1234-5", obs.getCode().getCoding().get(0).getCode());
			});
	}

	@Test
	public void testMultiResourceElementsFilter() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Procedure?_include=*&_elements=Procedure.reasonCode,Observation.status,Observation.subject,Observation.value",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(0, dr.getMeta().getTag().size());

				Observation obs = (Observation ) bundle.getEntry().get(2).getResource();
				assertEquals("SUBSETTED", obs.getMeta().getTag().get(0).getCode());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(0, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	@Test
	public void testMultiResourceElementsFilterWithMetadataExcluded() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Procedure?_include=*&_elements=Procedure.reasonCode,Observation.status,Observation.subject,Observation.value&_elements:exclude=*.meta",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals(true, procedure.getMeta().isEmpty());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(0, procedure.getUsedCode().size());

				DiagnosticReport dr = (DiagnosticReport) bundle.getEntry().get(1).getResource();
				assertEquals(true, dr.getMeta().isEmpty());

				Observation obs = (Observation ) bundle.getEntry().get(2).getResource();
				assertEquals(true, obs.getMeta().isEmpty());
				assertEquals(Observation.ObservationStatus.FINAL, obs.getStatus());
				assertEquals(0, obs.getCode().getCoding().size());
				assertEquals("STRING VALUE", obs.getValueStringType().getValue());
			});
	}

	@Test
	public void testElementsFilterWithComplexPath() throws IOException {
		createProcedureWithLongChain();
		verifyXmlAndJson(
			"http://localhost:" + ourPort + "/Procedure?_elements=Procedure.reasonCode.coding.code",
			bundle -> {
				Procedure procedure = (Procedure) bundle.getEntry().get(0).getResource();
				assertEquals("SUBSETTED", procedure.getMeta().getTag().get(0).getCode());
				assertEquals("REASON_CODE", procedure.getReasonCode().get(0).getCoding().get(0).getCode());
				assertEquals(null, procedure.getReasonCode().get(0).getCoding().get(0).getSystem());
				assertEquals(null, procedure.getReasonCode().get(0).getCoding().get(0).getDisplay());
				assertEquals(0, procedure.getUsedCode().size());
			});
	}

	private void createProcedureWithLongChain() {
		ourNextProcedure = new Procedure();
		ourNextProcedure.setId("Procedure/PROC");
		ourNextProcedure.addReasonCode().addCoding().setCode("REASON_CODE").setSystem("REASON_SYSTEM").setDisplay("REASON_DISPLAY");
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

	private void verifyXmlAndJson(String theUri, Consumer<Bundle> theVerifier) throws IOException {
		verifyXmlAndJson(theUri, Bundle.class, theVerifier);
	}

	private <T extends IBaseResource> void verifyXmlAndJson(String theUri, Class<T> theType, Consumer<T> theVerifier) throws IOException {
		EncodingEnum encodingEnum;
		HttpGet httpGet;

		encodingEnum = EncodingEnum.JSON;
		httpGet = new HttpGet(theUri + "&_pretty=true&_format=" + encodingEnum.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			T response = encodingEnum.newParser(ourCtx).parseResource(theType, responseContent);
			theVerifier.accept(response);
		}

		encodingEnum = EncodingEnum.XML;
		httpGet = new HttpGet(theUri + "&_pretty=true&_format=" + encodingEnum.getFormatContentType());
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);
			T response = encodingEnum.newParser(ourCtx).parseResource(theType, responseContent);
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
		ourServlet = new RestfulServer(ourCtx);

		ourServlet.registerProvider(new DummyPatientResourceProvider());
		ourServlet.registerProvider(new DummyProcedureResourceProvider());
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
