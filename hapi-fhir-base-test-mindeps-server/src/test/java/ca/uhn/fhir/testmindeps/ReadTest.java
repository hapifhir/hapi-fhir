package ca.uhn.fhir.testmindeps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.PortUtil;

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
			
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals(null, dt.getValue().getValueAsString());
			
			Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
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
			
			Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Binary/1/_history/1", cl.getValue());
			
			Header cd = status.getFirstHeader("content-disposition");
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
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals("2", dt.getValue().getValueAsString());

			Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());
		}
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		ourCtx = servlet.getFhirContext();
		servlet.setResourceProviders(patientProvider, new DummyBinaryProvider());
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
