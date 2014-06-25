package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

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
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ReadTest {

	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx;

	@Test
	public void testRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals(null, dt.getValue().getValueAsString());
		}
		
	}

	@Test
	public void testVRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/2");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals("2", dt.getValue().getValueAsString());
		}
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		ourCtx = servlet.getFhirContext();
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

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyProvider implements IResourceProvider {

		@Read(version = true)
		public Patient findPatient(@IdParam IdDt theId) {
			Patient patient = new Patient();
			patient.addIdentifier(theId.getIdPart(), theId.getVersionIdPart());
			patient.setId("Patient/1/_history/1");
			return patient;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
