package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu21.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu21.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu21.model.IdType;
import org.hl7.fhir.dstu21.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.validation.IValidatorModule;

public class RequestValidatingInterceptorDstu21Test {
	private static CloseableHttpClient ourClient;
	
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorDstu21Test.class);
	private static int ourPort;

	private static Server ourServer;

	private static RestfulServer ourServlet;
	
	
	
	private RequestValidatingInterceptor myInterceptor;

	@Before
	public void before() {
		ourLastRequestWasSearch = false;
		while (ourServlet.getInterceptors().size() > 0) {
			ourServlet.unregisterInterceptor(ourServlet.getInterceptors().get(0));
		}
		
		myInterceptor = new RequestValidatingInterceptor();
//		myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
//		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
//		myInterceptor.setResponseHeaderName("X-RESP");
//		myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);
		
		ourServlet.registerInterceptor(myInterceptor);
	}

	@Test
	public void testCreateJsonInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-HAPI-Request-Validation"));
		assertThat(responseContent, containsString("<severity value=\"error\"/>"));
	}

	@Test
	public void testCreateJsonInvalidNoFailure() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-HAPI-Request-Validation"));
		assertThat(responseContent, not(containsString("<severity value=\"error\"/>")));
	}
	
	@Test
	public void testCreateJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-HAPI-Request-Validation")));
	}

	@Test
	public void testCreateJsonValidNoValidatorsSpecifiedDefaultMessage() throws Exception {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-HAPI-Request-Validation: NO ISSUES")));
	}
	
	@Test
	public void testCreateXmlInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-HAPI-Request-Validation"));
	}

	@Test
	public void testCreateXmlInvalidInstanceValidator() throws Exception {
		IValidatorModule module = new FhirInstanceValidator();
		myInterceptor.addValidatorModule(module);
		
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-HAPI-Request-Validation"));
	}
	
	@Test
	public void testSearch() throws Exception {
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-HAPI-Request-Validation")));
		assertEquals(true, ourLastRequestWasSearch);
	}

	@Test
	public void testCreateXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(encoded, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-HAPI-Request-Validation")));
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}
	public static class PatientProvider implements IResourceProvider {


		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
		
		@Search
		public List<IResource> search(@OptionalParam(name="foo") StringParam theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<IResource>();
		}

	}

}
