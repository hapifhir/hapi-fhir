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
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
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
			HttpOptions httpOpt = new HttpOptions(ourBaseUri + "/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2");
			httpOpt.addHeader("Access-Control-Request-Method", "POST");
			httpOpt.addHeader("Origin", "http://www.fhir-starter.com");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("GET,POST,PUT,DELETE,OPTIONS", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_METHODS).getValue());
			assertEquals("http://www.fhir-starter.com", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
		}
		{
			String uri = ourBaseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("X-FHIR-Starter", "urn:fhir.starter");
			httpGet.addHeader("Origin", "http://www.fhir-starter.com");
			HttpResponse status = ourClient.execute(httpGet);

			Header origin = status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN);
			assertEquals("http://www.fhir-starter.com", origin.getValue());

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

			assertEquals(1, bundle.getEntry().size());
		}
		{
			HttpPost httpOpt = new HttpPost(ourBaseUri + "/Patient");
			httpOpt.addHeader("Access-Control-Request-Method", "POST");
			httpOpt.addHeader("Origin", "http://www.fhir-starter.com");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			httpOpt.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(new Patient())));
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response: {}", status);
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("http://www.fhir-starter.com", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
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
		assertThat(def.getConfig().getAllowedOrigins(), contains("*"));
	}

	@Test
	public void testRequestWithInvalidOrigin() throws ClientProtocolException, IOException {
		{
			HttpOptions httpOpt = new HttpOptions(ourBaseUri + "/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2");
			httpOpt.addHeader("Access-Control-Request-Method", "GET");
			httpOpt.addHeader("Origin", "http://yahoo.com");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals(403, status.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testRequestWithNullOrigin() throws ClientProtocolException, IOException {
		{
			HttpOptions httpOpt = new HttpOptions(ourBaseUri + "/Organization/b27ed191-f62d-4128-d99d-40b5e84f2bf2");
			httpOpt.addHeader("Access-Control-Request-Method", "GET");
			httpOpt.addHeader("Origin", "null");
			httpOpt.addHeader("Access-Control-Request-Headers", "accept, x-fhir-starter, content-type");
			HttpResponse status = ourClient.execute(httpOpt);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);
			assertEquals("GET,POST,PUT,DELETE,OPTIONS", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_METHODS).getValue());
			assertEquals("null", status.getFirstHeader(Constants.HEADER_CORS_ALLOW_ORIGIN).getValue());
		}
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
		config.addAllowedHeader("x-fhir-starter");
		config.addAllowedHeader("Origin");
		config.addAllowedHeader("Accept");
		config.addAllowedHeader("X-Requested-With");
		config.addAllowedHeader("Content-Type");
		config.addAllowedHeader("Access-Control-Request-Method");
		config.addAllowedHeader("Access-Control-Request-Headers");
		config.addAllowedOrigin("http://www.fhir-starter.com");
		config.addAllowedOrigin("null");
		config.addAllowedOrigin("file://");
		config.addExposedHeader("Location");
		config.addExposedHeader("Content-Location");
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
