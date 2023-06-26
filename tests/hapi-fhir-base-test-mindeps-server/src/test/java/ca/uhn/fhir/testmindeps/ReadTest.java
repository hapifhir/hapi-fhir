package ca.uhn.fhir.testmindeps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import ca.uhn.fhir.rest.api.EncodingEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.*; import static org.hamcrest.MatcherAssert.assertThat;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.bio.SocketConnector;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ReadTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx;

	@Test
	public void testRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			
			assertEquals("1", dt.getSystemElement().getValueAsString());
			assertEquals(null, dt.getValueElement().getValueAsString());
			
			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());
			
		}
		
	}
	
	
	@Test
	public void testBinaryRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/1");
			HttpResponse status = ourClient.execute(httpGet);
			byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("application/x-foo", status.getEntity().getContentType().getValue());
			
			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Binary/1/_history/1", cl.getValue());
			
			org.apache.http.Header cd = status.getFirstHeader("content-disposition");
			assertNotNull(cd);
			assertEquals("Attachment;", cd.getValue());
			
			assertEquals(4,responseContent.length);
			for (int i = 0; i < 4; i++) {
				assertEquals(i+1, responseContent[i]); // should be 1,2,3,4
			}
			
		}
		
	}

	@Test
	public void testVRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/2");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			assertEquals("1", dt.getSystemElement().getValueAsString());
			assertEquals("2", dt.getValueElement().getValueAsString());

			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());
		}
	}

	@AfterAll
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		ourCtx = servlet.getFhirContext();
		servlet.setResourceProviders(patientProvider, new DummyBinaryProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();
        Connector[] connectors = ourServer.getConnectors();
        assert connectors.length == 1;
        ourPort = ((SocketConnector) (connectors[0])).getLocalPort();

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
			patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
			patient.setId("Patient/1/_history/1");
			return patient;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyBinaryProvider implements IResourceProvider {

		@Read(version = true)
		public Binary findPatient(@IdParam IdDt theId) {
			Binary bin = new Binary();
			bin.setContentType("application/x-foo");
			bin.setContent(new byte[] {1,2,3,4});
			bin.setId("Binary/1/_history/1");
			return bin;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

	}

	
}
