package ca.uhn.fhir.testmindeps;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

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

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();

			assertThat(dt.getSystemElement().getValueAsString()).isEqualTo("1");
			assertThat(dt.getValueElement().getValueAsString()).isNull();
			
			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertThat(cl).isNotNull();
			assertThat(cl.getValue()).isEqualTo("http://localhost:" + ourPort + "/Patient/1/_history/1");
			
		}
		
	}
	
	
	@Test
	public void testBinaryRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/1");
			HttpResponse status = ourClient.execute(httpGet);
			byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());


			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(status.getEntity().getContentType().getValue()).isEqualTo("application/x-foo");
			
			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertThat(cl).isNotNull();
			assertThat(cl.getValue()).isEqualTo("http://localhost:" + ourPort + "/Binary/1/_history/1");
			
			org.apache.http.Header cd = status.getFirstHeader("content-disposition");
			assertThat(cd).isNotNull();
			assertThat(cd.getValue()).isEqualTo("Attachment;");

			assertThat(responseContent.length).isEqualTo(4);
			for (int i = 0; i < 4; i++) {
				assertThat(responseContent[i]).isEqualTo(i + 1); // should be 1,2,3,4
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

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class,responseContent).getIdentifierFirstRep();
			assertThat(dt.getSystemElement().getValueAsString()).isEqualTo("1");
			assertThat(dt.getValueElement().getValueAsString()).isEqualTo("2");

			org.apache.http.Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertThat(cl).isNotNull();
			assertThat(cl.getValue()).isEqualTo("http://localhost:" + ourPort + "/Patient/1/_history/1");
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

		RestfulServer servlet = new RestfulServer();
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		ourCtx = servlet.getFhirContext();
		servlet.setResourceProviders(patientProvider, new DummyBinaryProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.addServlet(servletHolder, "/*");

		ourServer.setHandler(contextHandler);
		ourServer.start();
        Connector[] connectors = ourServer.getConnectors();
        assert connectors.length == 1;
        ourPort = ((ServerConnector) (connectors[0])).getLocalPort();

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
