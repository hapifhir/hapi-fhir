package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.ResfulServerMethodTest.DummyDiagnosticReportResourceProvider;
import ca.uhn.fhir.rest.server.ResfulServerMethodTest.DummyPatientResourceProvider;
import ca.uhn.fhir.rest.server.ResfulServerMethodTest.DummyRestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

public class ClientIntegrationTest {

	private int myPort;
	private Server myServer;
	private FhirContext myCtx;
	private MyPatientResourceProvider myPatientProvider;

	@Before
	public void before() {
		myPort = RandomServerPortProvider.findFreePort();
		myServer = new Server(myPort);
		myCtx = new FhirContext(Patient.class);

		myPatientProvider = new MyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.setResourceProviders(myPatientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);

	}

	@Test
	public void testClientSecurity() throws Exception {
//		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("foobar", "boobear");
//		AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
//		credentialsProvider.setCredentials(scope, credentials);
//		builder.setDefaultCredentialsProvider(credentialsProvider);
//		

		myServer.start();

		FhirContext ctx = new FhirContext();

		HttpClientBuilder builder = HttpClientBuilder.create();
//		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
//		builder.setConnectionManager(connectionManager);
		builder.addInterceptorFirst(new HttpBasicAuthInterceptor("foobar", "boobear"));
		
		CloseableHttpClient httpClient = builder.build();
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		
		PatientClient client = ctx.newRestfulClient(PatientClient.class, "http://localhost:" + myPort + "/");
		
		List<Patient> actualPatients = client.searchForPatients(new StringDt("AAAABBBB"));
		assertEquals(1, actualPatients.size());
		assertEquals("AAAABBBB", actualPatients.get(0).getNameFirstRep().getFamilyAsSingleString());
		
		assertEquals("Basic Zm9vYmFyOmJvb2JlYXI=", myPatientProvider.getAuthorizationHeader());
	}
	
	
	@After
	public void after() throws Exception {
		myServer.stop();
	}
	
	
	public static class MyPatientResourceProvider implements IResourceProvider {
		private String myAuthorizationHeader;
		
		public String getAuthorizationHeader() {
			return myAuthorizationHeader;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@Search
		public List<Patient> searchForPatients(@RequiredParam(name="fooParam") StringDt theFooParam, HttpServletRequest theRequest) {
			
			myAuthorizationHeader = theRequest.getHeader("authorization");
			
			Patient retVal = new Patient();
			retVal.addName().addFamily(theFooParam.getValue());
			return Collections.singletonList(retVal);
		}
		
		
	}

	
	private static interface PatientClient extends IBasicClient {
		
		@Search
		public List<Patient> searchForPatients(@RequiredParam(name="fooParam") StringDt theFooParam);

	}
	
}
