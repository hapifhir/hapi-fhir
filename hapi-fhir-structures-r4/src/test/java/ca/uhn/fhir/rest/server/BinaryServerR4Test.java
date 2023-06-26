package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BinaryServerR4Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static Binary ourLastBinary;
	private static byte[] ourLastBinaryBytes;
	private static String ourLastBinaryString;
	private static int ourPort;
	private static Server ourServer;
	private static IdType ourLastId;
	private static Binary ourNextBinary;

	@BeforeEach
	public void before() {
		ourLastBinary = null;
		ourLastBinaryBytes = null;
		ourLastBinaryString = null;
		ourLastId = null;
		ourNextBinary = null;
	}

	@Test
	public void testGetWithNoAccept() throws Exception {

		ourNextBinary = new Binary();
		ourNextBinary.setId("Binary/A/_history/222");
		ourNextBinary.setContent(new byte[]{0, 1, 2, 3, 4});
		ourNextBinary.setSecurityContext(new Reference("Patient/1"));
		ourNextBinary.setContentType("application/foo");

		HttpGet get = new HttpGet("http://localhost:" + ourPort + "/Binary/A");
		get.addHeader("Content-Type", "application/foo");
		CloseableHttpResponse status = ourClient.execute(get);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("application/foo", status.getEntity().getContentType().getValue());
			assertEquals("Patient/1", status.getFirstHeader(Constants.HEADER_X_SECURITY_CONTEXT).getValue());
			assertEquals("W/\"222\"", status.getFirstHeader(Constants.HEADER_ETAG).getValue());
			assertEquals("http://localhost:" + ourPort + "/Binary/A/_history/222", status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
			assertEquals(null, status.getFirstHeader(Constants.HEADER_LOCATION));

			byte[] content = IOUtils.toByteArray(status.getEntity().getContent());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, content);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}


	@Test
	public void testGetWithAccept() throws Exception {

		ourNextBinary = new Binary();
		ourNextBinary.setId("Binary/A/_history/222");
		ourNextBinary.setContent(new byte[]{0, 1, 2, 3, 4});
		ourNextBinary.setSecurityContext(new Reference("Patient/1"));
		ourNextBinary.setContentType("application/foo");

		HttpGet get = new HttpGet("http://localhost:" + ourPort + "/Binary/A");
		get.addHeader("Content-Type", "application/foo");
		get.addHeader("Accept", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(get);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("application/json+fhir;charset=utf-8", status.getEntity().getContentType().getValue());
			assertEquals("Patient/1", status.getFirstHeader(Constants.HEADER_X_SECURITY_CONTEXT).getValue());
			assertEquals("W/\"222\"", status.getFirstHeader(Constants.HEADER_ETAG).getValue());
			assertEquals("http://localhost:" + ourPort + "/Binary/A/_history/222", status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());
			assertEquals(null, status.getFirstHeader(Constants.HEADER_LOCATION));

			String content = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			assertEquals("{\"resourceType\":\"Binary\",\"id\":\"A\",\"meta\":{\"versionId\":\"222\"},\"contentType\":\"application/foo\",\"securityContext\":{\"reference\":\"Patient/1\"},\"data\":\"AAECAwQ=\"}", content);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testPostBinaryWithSecurityContext() throws Exception {
		HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Binary");
		post.setEntity(new ByteArrayEntity(new byte[]{0, 1, 2, 3, 4}));
		post.addHeader("Content-Type", "application/foo");
		post.addHeader(Constants.HEADER_X_SECURITY_CONTEXT, "Encounter/2");
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertNull(ourLastId);
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertEquals("Encounter/2", ourLastBinary.getSecurityContext().getReference());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinary.getContent());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinaryBytes);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testPostRawBytesBinaryContentType() throws Exception {
		HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Binary");
		post.setEntity(new ByteArrayEntity(new byte[]{0, 1, 2, 3, 4}));
		post.addHeader("Content-Type", "application/foo");
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertNull(ourLastId);
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinary.getContent());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinaryBytes);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * Technically the client shouldn't be doing it this way, but we'll be accepting
	 */
	@Test
	public void testPostRawBytesFhirContentType() throws Exception {

		Binary b = new Binary();
		b.setContentType("application/foo");
		b.setContent(new byte[]{0, 1, 2, 3, 4});
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Binary");
		post.setEntity(new StringEntity(encoded));
		post.addHeader("Content-Type", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinary.getContent());
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testPostRawBytesFhirContentTypeContainingFhir() throws Exception {

		Patient p = new Patient();
		p.getText().setDivAsString("A PATIENT");

		Binary b = new Binary();
		b.setContentType("application/xml+fhir");
		b.setContent(ourCtx.newXmlParser().encodeResourceToString(p).getBytes("UTF-8"));
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Binary");
		post.setEntity(new StringEntity(encoded));
		post.addHeader("Content-Type", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("application/xml+fhir", ourLastBinary.getContentType());
			assertArrayEquals(b.getContent(), ourLastBinary.getContent());
			assertEquals(encoded, ourLastBinaryString);
			assertArrayEquals(encoded.getBytes("UTF-8"), ourLastBinaryBytes);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testPostRawBytesNoContentType() throws Exception {
		HttpPost post = new HttpPost("http://localhost:" + ourPort + "/Binary");
		post.setEntity(new ByteArrayEntity(new byte[]{0, 1, 2, 3, 4}));
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertNull(ourLastBinary.getContentType());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinary.getContent());
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testPutBinaryWithSecurityContext() throws Exception {
		HttpPut post = new HttpPut("http://localhost:" + ourPort + "/Binary/A");
		post.setEntity(new ByteArrayEntity(new byte[]{0, 1, 2, 3, 4}));
		post.addHeader("Content-Type", "application/foo");
		post.addHeader(Constants.HEADER_X_SECURITY_CONTEXT, "Encounter/2");
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("Binary/A", ourLastId.getValue());
			assertEquals("Binary/A", ourLastBinary.getId());
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertEquals("Encounter/2", ourLastBinary.getSecurityContext().getReference());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinary.getContent());
			assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourLastBinaryBytes);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		BinaryProvider binaryProvider = new BinaryProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(binaryProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	public static class BinaryProvider implements IResourceProvider {
		@Create()
		public MethodOutcome createBinary(@ResourceParam Binary theBinary, @ResourceParam String theBinaryString, @ResourceParam byte[] theBinaryBytes) {
			ourLastBinary = theBinary;
			ourLastBinaryString = theBinaryString;
			ourLastBinaryBytes = theBinaryBytes;
			return new MethodOutcome(new IdType("Binary/001/_history/002"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Binary.class;
		}

		@Read
		public Binary read(@IdParam IdType theId) {
			return ourNextBinary;
		}

		@Update()
		public MethodOutcome updateBinary(@IdParam IdType theId, @ResourceParam Binary theBinary, @ResourceParam String theBinaryString, @ResourceParam byte[] theBinaryBytes) {
			ourLastId = theId;
			ourLastBinary = theBinary;
			ourLastBinaryString = theBinaryString;
			ourLastBinaryBytes = theBinaryBytes;
			return new MethodOutcome(new IdType("Binary/001/_history/002"));
		}

	}

}
