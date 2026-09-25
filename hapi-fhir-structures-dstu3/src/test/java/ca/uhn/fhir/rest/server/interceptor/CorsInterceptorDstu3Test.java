package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CorsInterceptorDstu3Test {
	private static String ourBaseUri;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CorsInterceptorDstu3Test.class);
	private static Server ourServer;

	@Test
	public void testContextWithSpace() throws Exception {
		{
			HttpTestResponse status = fhirRequest("/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2")
				.withHeader(Constants.HEADER_CORS_REQUEST_METHOD, "POST")
				.withHeader(Constants.HEADER_CORS_ORIGIN, "http://www.fhir-starter.com")
				.withHeader(Constants.HEADER_CORS_REQUEST_HEADERS, "accept, x-fhir-starter, content-type")
				.options();
			String responseContent = status.getBody();
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("GET,POST,PUT,DELETE,OPTIONS", status.getHeader(Constants.HEADER_CORS_ALLOW_METHODS));
			assertEquals("http://www.fhir-starter.com", status.getHeader(Constants.HEADER_CORS_ALLOW_ORIGIN));
		}
		{
			HttpTestResponse status = fhirRequest("/Patient?identifier=urn:hapitest:mrns%7C00001")
				.withHeader(Constants.HEADER_X_FHIR_STARTER, "urn:fhir.starter")
				.withHeader(Constants.HEADER_CORS_ORIGIN, "http://www.fhir-starter.com")
				.get();

			String origin = status.getHeader(Constants.HEADER_CORS_ALLOW_ORIGIN);
			assertEquals("http://www.fhir-starter.com", origin);

			String responseContent = status.getBody();
			ourLog.info("Response was:\n{}", responseContent);

			status.assertStatus(200);
			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

			assertThat(bundle.getEntry()).hasSize(1);
		}
		{
			HttpTestResponse status = fhirRequest("/Patient")
				.withHeader(Constants.HEADER_CORS_REQUEST_METHOD, "POST")
				.withHeader(Constants.HEADER_CORS_ORIGIN, "http://www.fhir-starter.com")
				.withHeader(Constants.HEADER_CORS_REQUEST_HEADERS, "accept, x-fhir-starter, content-type")
				.post(ourCtx.newXmlParser().encodeResourceToString(new Patient()), "text/plain; charset=ISO-8859-1");
			String responseContent = status.getBody();
			ourLog.info("Response: {}", status);
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("http://www.fhir-starter.com", status.getHeader(Constants.HEADER_CORS_ALLOW_ORIGIN));
		}
	}
	
	@Test
	public void testCorsConfigMethods() {
		CorsInterceptor corsInterceptor = new CorsInterceptor();
		assertNotNull(corsInterceptor.getConfig());
		corsInterceptor.setConfig(new CorsConfiguration());
	}

	@Test
	public void testDefaultConfig() {
		CorsInterceptor def = new CorsInterceptor();
		assertThat(def.getConfig().getAllowedOrigins()).containsExactly("*");
	}

	@Test
	public void testRequestWithInvalidOrigin() throws ClientProtocolException, IOException {
		{
			HttpTestResponse status = fhirRequest("/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2")
				.withHeader(Constants.HEADER_CORS_REQUEST_METHOD, "GET")
				.withHeader(Constants.HEADER_CORS_ORIGIN, "http://yahoo.com")
				.withHeader(Constants.HEADER_CORS_REQUEST_HEADERS, "accept, x-fhir-starter, content-type")
				.options();
			String responseContent = status.getBody();
			ourLog.info("Response was:\n{}", responseContent);
			status.assertStatus(403);
		}
	}

	@Test
	public void testRequestWithNullOrigin() throws ClientProtocolException, IOException {
		{
			HttpTestResponse status = fhirRequest("/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2").withHeader(Constants.HEADER_CORS_REQUEST_METHOD, "GET")
				.withHeader(Constants.HEADER_CORS_ORIGIN, "null").withHeader(Constants.HEADER_CORS_REQUEST_HEADERS, "accept, x-fhir-starter, content-type").options();
			String responseContent = status.getBody();
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("GET,POST,PUT,DELETE,OPTIONS", status.getHeader(Constants.HEADER_CORS_ALLOW_METHODS));
			assertEquals("null", status.getHeader(Constants.HEADER_CORS_ALLOW_ORIGIN));
		}
	}
	
	private HttpTestRequest fhirRequest(String thePath) {
		return HttpTestRequest.to(ourClient, ourCtx, ourBaseUri + thePath);
	}

	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
		ourClient.close();
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
	

	@BeforeAll
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourServer = new Server(0);

		RestfulServer restServer = new RestfulServer(ourCtx);
		restServer.setResourceProviders(new DummyPatientResourceProvider());
		restServer.setDefaultResponseEncoding(EncodingEnum.XML);

		ServletHolder servletHolder = new ServletHolder(restServer);

		CorsConfiguration config = new CorsConfiguration();
		CorsInterceptor interceptor = new CorsInterceptor(config);
		config.addAllowedHeader(Constants.HEADER_X_FHIR_STARTER);
		config.addAllowedHeader(Constants.HEADER_CORS_ORIGIN);
		config.addAllowedHeader(Constants.HEADER_ACCEPT);
		config.addAllowedHeader(Constants.HEADER_X_REQUESTED_WITH);
		config.addAllowedHeader(Constants.HEADER_CONTENT_TYPE);
		config.addAllowedHeader(Constants.HEADER_CORS_REQUEST_METHOD);
		config.addAllowedHeader(Constants.HEADER_CORS_REQUEST_HEADERS);
		config.addAllowedOrigin("http://www.fhir-starter.com");
		config.addAllowedOrigin("null");
		config.addAllowedOrigin("file://");
		config.addExposedHeader(Constants.HEADER_LOCATION);
		config.addExposedHeader(Constants.HEADER_CONTENT_LOCATION);
		config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		restServer.registerInterceptor(interceptor);
		
		ServletContextHandler ch = new ServletContextHandler();
		ch.setContextPath("/rootctx/rcp2");
		ch.addServlet(servletHolder, "/fhirctx/fcp2/*");

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		ourServer.setHandler(contexts);

		ourServer.setHandler(ch);
		JettyUtil.startServer(ourServer);
        int port = JettyUtil.getPortForStartedServer(ourServer);
		ourBaseUri = "http://localhost:" + port + "/rootctx/rcp2/fhirctx/fcp2";

	}
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			return new MethodOutcome(thePatient.getIdElement());
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<>();
			{
				Patient patient = new Patient();
				patient.setId("1");
				patient.addIdentifier();
				patient.getIdentifier().get(0).setUse(IdentifierUse.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00001");
				patient.addName();
				patient.getName().get(0).setFamily("Test");
				patient.getName().get(0).addGiven("PatientOne");
				patient.setGender(AdministrativeGender.MALE);
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.setId("2");
				patient.getIdentifier().add(new Identifier());
				patient.getIdentifier().get(0).setUse(IdentifierUse.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanName());
				patient.getName().get(0).setFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.setGender(AdministrativeGender.FEMALE);
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}
		
		@Search()
		public Patient getPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (Identifier nextId : next.getIdentifier()) {
					if (nextId.getSystem().equals(theIdentifier.getSystem())&& nextId.getValue().equals(theIdentifier.getValue())) {
						return next;
					}
				}
			}
			return null;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdType theId) {
			return getIdToPatient().get(theId.getValue());
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}


}
