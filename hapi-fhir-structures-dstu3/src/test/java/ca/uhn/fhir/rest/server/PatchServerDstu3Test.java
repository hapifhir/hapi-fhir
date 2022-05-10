package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatchServerDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatchServerDstu3Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static PatchTypeEnum ourLastPatchType;
	private static String ourLastBody;
	private static IdType ourLastId;
	private static String ourLastConditional;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastBody = null;
		ourLastId = null;
		ourLastConditional = null;
	}

	@Test
	public void testPatchValidJson() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
		httpPatch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		httpPatch.setEntity(new StringEntity(requestContents, ContentType.parse(Constants.CT_JSON_PATCH)));
		CloseableHttpResponse status = ourClient.execute(httpPatch);

		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.JSON_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchUsingConditional() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient?_id=123");
		httpPatch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		httpPatch.setEntity(new StringEntity(requestContents, ContentType.parse(Constants.CT_JSON_PATCH)));
		CloseableHttpResponse status = ourClient.execute(httpPatch);

		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient?_id=123", ourLastConditional);
		assertEquals(null, ourLastId);
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.JSON_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchValidXml() throws Exception {
		String requestContents = "<root/>";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
		httpPatch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		httpPatch.setEntity(new StringEntity(requestContents, ContentType.parse(Constants.CT_XML_PATCH)));
		CloseableHttpResponse status = ourClient.execute(httpPatch);

		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div></text></OperationOutcome>", responseContent);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
		assertEquals(PatchTypeEnum.XML_PATCH, ourLastPatchType);
	}

	@Test
	public void testPatchValidJsonWithCharset() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
		httpPatch.setEntity(new StringEntity(requestContents, ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		CloseableHttpResponse status = ourClient.execute(httpPatch);

		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		assertEquals("patientPatch", ourLastMethod);
		assertEquals("Patient/123", ourLastId.getValue());
		assertEquals(requestContents, ourLastBody);
	}

	@Test
	public void testPatchInvalidMimeType() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
		httpPatch.setEntity(new StringEntity(requestContents, ContentType.parse("text/plain; charset=UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPatch);

		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\""+ Msg.code(1965)+"Invalid Content-Type for PATCH operation: text/plain\"/></issue></OperationOutcome>", responseContent);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Patch
		public OperationOutcome patientPatch(
			@IdParam IdType theId,
			PatchTypeEnum thePatchType,
			@ResourceParam String theBody,
			@ConditionalUrlParam String theConditional
		) {
			ourLastMethod = "patientPatch";
			ourLastBody = theBody;
			ourLastId = theId;
			ourLastPatchType = thePatchType;
			ourLastConditional = theConditional;
			OperationOutcome retVal = new OperationOutcome();
			retVal.getText().setDivAsString("<div>OK</div>");
			return retVal;
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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));
		servlet.setResourceProviders(patientProvider);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

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

}
