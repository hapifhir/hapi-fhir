package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
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
import ca.uhn.fhir.util.TestUtil;

public class ReadDstu1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadDstu1Test.class);
	private static int ourPort;
	private static Server ourServer;

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

			assertEquals(4, responseContent.length);
			for (int i = 0; i < 4; i++) {
				assertEquals(i + 1, responseContent[i]); // should be 1,2,3,4
			}

		}

	}

	@Test
	public void testEncodeConvertsReferencesToRelative() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		String ref = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getManagingOrganization().getReference().getValue();
		assertEquals("Organization/555", ref);
	}

	@Test
	public void testReadForProviderWithAbstractReturnType() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Organization/1");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Organization.class, responseContent).getIdentifierFirstRep();

			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals(null, dt.getValue().getValueAsString());

			Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Organization/1/_history/1", cl.getValue());

		}

	}

	@Test
	public void testReadJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IdentifierDt dt = ourCtx.newJsonParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();

		assertEquals("1", dt.getSystem().getValueAsString());
		assertEquals(null, dt.getValue().getValueAsString());

		Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
		assertNotNull(cl);
		assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());

		assertThat(responseContent, stringContainsInOrder("1", "\""));
		assertThat(responseContent, not(stringContainsInOrder("1", "\"", "1")));
	}

	@Test
	public void testReadWithEscapedCharsInId() throws Exception {
		String id = "ABC!@#$--DEF";
		String idEscaped = URLEncoder.encode(id, "UTF-8");

		String vid = "GHI:/:/JKL";
		String vidEscaped = URLEncoder.encode(vid, "UTF-8");

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/" + idEscaped + "/_history/" + vidEscaped);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();
		assertEquals(id, dt.getSystem().getValueAsString());
		assertEquals(vid, dt.getValue().getValueAsString());
	}

	@Test
	public void testReadXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();

		assertEquals("1", dt.getSystem().getValueAsString());
		assertEquals(null, dt.getValue().getValueAsString());

		Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
		assertNotNull(cl);
		assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());

		assertThat(responseContent, stringContainsInOrder("1", "\""));
		assertThat(responseContent, not(stringContainsInOrder("1", "\"", "1")));
	}

	@Test
	public void testVRead() throws Exception {
		{
			HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/2");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			IdentifierDt dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifierFirstRep();
			assertEquals("1", dt.getSystem().getValueAsString());
			assertEquals("2", dt.getValue().getValueAsString());

			Header cl = status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC);
			assertNotNull(cl);
			assertEquals("http://localhost:" + ourPort + "/Patient/1/_history/1", cl.getValue());
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

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(patientProvider, new BinaryProvider(), new OrganizationProviderWithAbstractReturnType());
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
	public static class BinaryProvider implements IResourceProvider {

		@Read(version = true)
		public Binary findPatient(@IdParam IdDt theId) {
			Binary bin = new Binary();
			bin.setContentType("application/x-foo");
			bin.setContent(new byte[] { 1, 2, 3, 4 });
			bin.setId("Binary/1/_history/1");
			return bin;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class OrganizationProviderWithAbstractReturnType implements IResourceProvider {

		@Read(version = true)
		public BaseResource findOrganization(@IdParam IdDt theId) {
			Organization org = new Organization();
			org.addIdentifier(theId.getIdPart(), theId.getVersionIdPart());
			org.setId("Organization/1/_history/1");
			return org;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Organization.class;
		}

	}


	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			Patient patient = new Patient();
			patient.addIdentifier(theId.getIdPart(), theId.getVersionIdPart());
			patient.setId("Patient/1/_history/1");
			patient.getManagingOrganization().setReference("http://localhost:" + ourPort + "/Organization/555/_history/666");
			return patient;
		}

	}

}
