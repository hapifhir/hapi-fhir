package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ServerSecurityTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerSecurityTest.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourCtx = new FhirContext(Patient.class);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	@Test
	public void testContextWithSpace() throws Exception {
		if (true) return;
		
		int port = RandomServerPortProvider.findFreePort();
		Server server = new Server(port);

		RestfulServer restServer = new RestfulServer();
		restServer.setFhirContext(ourCtx);
		restServer.setResourceProviders(new DummyPatientResourceProvider());

		// ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(restServer);

		WebAppContext wac = new WebAppContext();
		wac.setWar("src/test/resources/securitytest_war");
		
//		wac.addServlet(servletHolder, "/fhir/*");
		
		
		ServletContextHandler ch = new ServletContextHandler();
		ch.setContextPath("/");
		ch.addServlet(servletHolder, "/fhir/*");
//		ch.add
//		ch.addFilter(org.springframework.web.filter.DelegatingFilterProxy.class, "/*", null);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		server.setHandler(wac);

//		server.setHandler(ch);
		server.start();
		try {

			String baseUri = "http://localhost:" + port + "/fhir";
			String uri = baseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
			HttpGet httpGet = new HttpGet(uri);
			httpGet.addHeader("Authorization", "Basic eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0MDY4MzU3MjMsImF1ZCI6WyJjbGllbnQiXSwiaXNzIjoiaHR0cDpcL1wvdWhudmVzYjAxZC51aG4ub24uY2E6MjUxODBcL3Vobi1vcGVuaWQtY29ubmVjdC1zZXJ2ZXJcLyIsImp0aSI6IjEwZTYwYWY1LTEyZmUtNDFhYy05MWQyLTliNWY0NzBiNGM5OSIsImlhdCI6MTQwNjgzMjEyM30.LaCAOmoM0ikkaalKBfccU_YE8NBXvT5M7L9ITfR86vEp5-3_kVeSrYHI4CjVUSDafyVwQF4x5eJVSZdtQxtEk9F1L6be_JQI84crqff53EKA10z7m9Lkc5jJFs6sd9cnlayhlBgxL7BNdSf0Bjs7aS6YMEcn9IY3RNSeQj7kjhs");
			HttpResponse status = ourClient.execute(httpGet);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

			assertEquals(1, bundle.getEntries().size());

		} finally {
			server.stop();
		}

	}

	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = new Patient();
				patient.setId("1");
				patient.addIdentifier();
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00001");
				patient.addName();
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientOne");
				patient.getGender().setText("M");
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.setId("2");
				patient.getIdentifier().add(new IdentifierDt());
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanNameDt());
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getGender().setText("F");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		@Search()
		public Patient getPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (IdentifierDt nextId : next.getIdentifier()) {
					if (nextId.matchesSystemAndValue(theIdentifier)) {
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
		public Patient getResourceById(@IdParam IdDt theId) {
			return getIdToPatient().get(theId.getValue());
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
