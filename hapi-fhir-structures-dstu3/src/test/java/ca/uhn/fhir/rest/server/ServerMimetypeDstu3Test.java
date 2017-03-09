package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class ServerMimetypeDstu3Test {
	private static CloseableHttpClient ourClient;

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerMimetypeDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testConformanceMetadataUsesNewMimetypes() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String content = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			CapabilityStatement conf = ourCtx.newXmlParser().parseResource(CapabilityStatement.class, content);
			List<String> strings = toStrings(conf.getFormat());
			assertThat(strings, contains(Constants.CT_FHIR_XML_NEW, Constants.CT_FHIR_JSON_NEW));
		} finally {
			status.close();
		}
	}
	
	
	
	private List<String> toStrings(List<CodeType> theFormat) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CodeType next : theFormat) {
			retVal.add(next.asStringValue());
		}
		return retVal;
	}



	@Test
	public void testCreateWithXmlLegacyNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>", responseContent);
	}

	@Test
	public void testHttpTraceNotEnabled() throws Exception {
		HttpTrace req = new HttpTrace("http://localhost:" + ourPort + "/Patient");
		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertEquals(400, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testHttpTrackNotEnabled() throws Exception {
		HttpRequestBase req = new HttpRequestBase() {
			@Override
			public String getMethod() {
				return "TRACK";
			}};
		req.setURI(new URI("http://localhost:" + ourPort + "/Patient"));
			
		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertEquals(400, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testCreateWithXmlNewNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML_NEW + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>", responseContent);
	}

	@Test
	public void testCreateWithXmlNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>", responseContent);
	}

	@Test
	public void testCreateWithJsonLegacyNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}", responseContent);
	}

	@Test
	public void testCreateWithJsonNewNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON_NEW + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}", responseContent);
	}

	@Test
	public void testCreateWithJsonNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}", responseContent);
	}
	
	@Test
	public void testSearchWithFormatXmlSimple() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(responseContent, not(containsString("http://hl7.org/fhir/")));
		assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatXmlLegacy() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=" + Constants.CT_FHIR_XML);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(responseContent, not(containsString("http://hl7.org/fhir/")));
		assertEquals(Constants.CT_FHIR_XML, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatXmlNew() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=" + Constants.CT_FHIR_XML_NEW);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(responseContent, not(containsString("http://hl7.org/fhir/")));
		assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
	}


	
	@Test
	public void testSearchWithFormatJsonSimple() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=json");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("\"resourceType\""));
		assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatJsonLegacy() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=" + Constants.CT_FHIR_JSON);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("\"resourceType\""));
		assertEquals(Constants.CT_FHIR_JSON, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatJsonNew() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=" + Constants.CT_FHIR_JSON_NEW);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("\"resourceType\""));
		assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
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

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theIdParam) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics(theIdParam.getNameFirstRep().getFamily());
			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(oo);
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read()
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(theIdParam);
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

		@Search
		public List<IBaseResource> search() {
			ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();

			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(new IdType("Patient/0"));
			p0.setDateExt(new DateType("2011-01-01"));
			retVal.add(p0);

			Patient p1 = new Patient();
			p1.setId(new IdType("Patient/1"));
			p1.addName().setFamily("The Family");
			retVal.add(p1);

			return retVal;
		}

	}

}
