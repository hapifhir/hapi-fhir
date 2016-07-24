package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

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
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CompressionTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompressionTest.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testRead() throws Exception {
		{
			/*
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
			httpGet.addHeader(Constants.HEADER_ACCEPT_ENCODING, "gzip");
			HttpResponse status = ourClient.execute(httpGet);
			Header ce = status.getFirstHeader(Constants.HEADER_CONTENT_ENCODING);
			*/
			
			URLConnection c = new URL("http://localhost:" + ourPort + "/Patient/1").openConnection();
			c.addRequestProperty(Constants.HEADER_ACCEPT_ENCODING, "gzip");
			
			String enc = c.getContentEncoding();
			
			assertEquals("gzip", enc);
			
			byte[] responseContentBytes = IOUtils.toByteArray(c.getInputStream());
			String responseContent = decompress(responseContentBytes);
			
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
			String responseContent = IOUtils.toString(status.getEntity().getContent());		IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals("2", dt.getValue().getValueAsString());
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

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		
//		GzipHandler gh = new GzipHandler();
//		gh.setHandler(proxyHandler);
//		gh.setMimeTypes(Constants.CT_ATOM_XML+','+Constants.CT_FHIR_JSON+','+Constants.CT_FHIR_XML);
		
		ourServer.setHandler(proxyHandler);
				
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static String decompress(byte[] theResource) {
		GZIPInputStream is;
		try {
			is = new GZIPInputStream(new ByteArrayInputStream(theResource));
			return IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new DataFormatException("Failed to decompress contents", e);
		}
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
