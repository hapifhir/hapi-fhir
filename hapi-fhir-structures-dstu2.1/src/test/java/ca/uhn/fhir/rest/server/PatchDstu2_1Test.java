package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

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
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class PatchDstu2_1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatchDstu2_1Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static PatchTypeEnum ourLastPatchType;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastBody = null;
		ourLastId = null;
	}

	@Test
	public void testPatchValidJson() throws Exception {
		String requestContents = "[ { \"op\": \"add\", \"path\": \"/a/b/c\", \"value\": [ \"foo\", \"bar\" ] } ]";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
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
	public void testPatchValidXml() throws Exception {
		String requestContents = "<root/>";
		HttpPatch httpPatch = new HttpPatch("http://localhost:" + ourPort + "/Patient/123");
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
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"Invalid Content-Type for PATCH operation: text/plain; charset=UTF-8\"/></issue></OperationOutcome>", responseContent);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

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

	private static String ourLastBody;
	private static IdType ourLastId;
	public static class DummyPatientResourceProvider implements IResourceProvider {



		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@Patch
		public OperationOutcome patientPatch(@IdParam IdType theId, PatchTypeEnum thePatchType, @ResourceParam String theBody) {
			ourLastMethod = "patientPatch";
			ourLastBody = theBody;
			ourLastId = theId;
			ourLastPatchType = thePatchType;
			OperationOutcome retVal = new OperationOutcome();
			retVal.getText().setDivAsString("<div>OK</div>");
			return retVal;
		}
		//@formatter:on

	}

}
