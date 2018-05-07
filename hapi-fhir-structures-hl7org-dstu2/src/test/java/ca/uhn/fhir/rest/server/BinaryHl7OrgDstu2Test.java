package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.Binary;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class BinaryHl7OrgDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static Binary ourLast;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BinaryHl7OrgDstu2Test.class);

	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLast = null;
	}

	@Test
	public void testReadWithExplicitTypeXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), "UTF-8");
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("content-type").getValue(), startsWith(Constants.CT_FHIR_XML + ";"));
		
		Binary bin = ourCtx.newXmlParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());
	}

	@Test
	public void testReadWithExplicitTypeJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), "UTF-8");
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("content-type").getValue(), startsWith(Constants.CT_FHIR_JSON + ";"));
		
		Binary bin = ourCtx.newJsonParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());
	}

	
	@Test
	public void testCreate() throws Exception {
		HttpPost http = new HttpPost("http://localhost:" + ourPort + "/Binary");
		http.setEntity(new ByteArrayEntity(new byte[] { 1, 2, 3, 4 }, ContentType.create("foo/bar", "UTF-8")));

		HttpResponse status = ourClient.execute(http);
		assertEquals(201, status.getStatusLine().getStatusCode());

		assertEquals("foo/bar; charset=UTF-8", ourLast.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, ourLast.getContent());

	}

	public void testCreateWrongType() throws Exception {
		Binary res = new Binary();
		res.setContent(new byte[] { 1, 2, 3, 4 });
		res.setContentType("text/plain");
		String stringContent = ourCtx.newJsonParser().encodeResourceToString(res);

		HttpPost http = new HttpPost("http://localhost:" + ourPort + "/Binary");
		http.setEntity(new StringEntity(stringContent, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(http);
		assertEquals(201, status.getStatusLine().getStatusCode());

		assertEquals("text/plain", ourLast.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, ourLast.getContent());

	}

	@Test
	public void testRead() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");
		HttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, responseContent);

	}

	@Test
	public void testSearchJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary?_pretty=true&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").replace("UTF", "utf"));

		ourLog.info(responseContent);

		org.hl7.fhir.instance.model.Bundle bundle = ourCtx.newJsonParser().parseResource(org.hl7.fhir.instance.model.Bundle.class, responseContent);
		Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());
	}

	@Test
	public void testSearchXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").replace("UTF", "utf"));

		ourLog.info(responseContent);

		org.hl7.fhir.instance.model.Bundle bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.instance.model.Bundle.class, responseContent);
		Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ResourceProvider patientProvider = new ResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
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
	public static class ResourceProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Binary theBinary) {
			ourLast = theBinary;
			return new MethodOutcome(new IdType("1"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Binary.class;
		}

		@Read
		public Binary read(@IdParam IdType theId) {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType(theId.getIdPart());
			return retVal;
		}

		@Search
		public List<Binary> search() {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType("text/plain");
			return Collections.singletonList(retVal);
		}

	}

}
