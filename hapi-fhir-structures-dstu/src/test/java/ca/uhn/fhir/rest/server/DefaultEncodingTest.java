package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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

import com.google.common.net.UrlEscapers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DefaultEncodingTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private static RestfulServer ourRestfulServer;

	@Test
	public void testReadWithDefaultJsonPretty() throws Exception {
		ourRestfulServer.setDefaultPrettyPrint(true);
		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.JSON);
		
		HttpGet httpGet;
		HttpResponse status;
		String responseContent;
		
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, containsString("  \"identifier\":"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=false");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, not(containsString("  \"identifier\":")));
		assertThat(responseContent, containsString("\"identifier\":"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, containsString("  <identifier"));
	
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml&_pretty=false");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, not(containsString("  <identifier")));
		assertThat(responseContent, containsString("<identifier"));

	}


	@Test
	public void testReadWithDefaultXmlUgly() throws Exception {
		ourRestfulServer.setDefaultPrettyPrint(false);
		ourRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		
		HttpGet httpGet;
		HttpResponse status;
		String responseContent;
		
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, not(containsString("  <identifier")));
		assertThat(responseContent, containsString("<identifier"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, containsString("<identifier"));
		assertThat(responseContent, not(containsString("  <identifier")));
	
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, containsString("\"identifier\":"));
		assertThat(responseContent, not(containsString("  \"identifier\":")));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json&_pretty=true");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertThat(responseContent, containsString("  \"identifier\":"));

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		ourRestfulServer = new RestfulServer(ourCtx);
		ourRestfulServer.setResourceProviders(new PatientProvider());
		ServletHolder servletHolder = new ServletHolder(ourRestfulServer);
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
	public static class PatientProvider implements IResourceProvider {

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
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
