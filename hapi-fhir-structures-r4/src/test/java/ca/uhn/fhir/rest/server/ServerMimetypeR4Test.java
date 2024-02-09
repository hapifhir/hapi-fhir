package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerMimetypeR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerMimetypeR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	private String readAndReturnContentType(String theAccept) throws IOException {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		if (theAccept != null) {
			httpGet.addHeader(Constants.HEADER_ACCEPT, theAccept);
		}
		HttpResponse status = ourClient.execute(httpGet);
		String contentType = status.getEntity().getContentType().getValue();
		IOUtils.closeQuietly(status.getEntity().getContent());
		contentType = contentType.replaceAll(";.*", "");
		return contentType;
	}

	@Test
	public void testConformanceMetadataUsesNewMimetypes() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String content = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			CapabilityStatement conf = ourCtx.newXmlParser().parseResource(CapabilityStatement.class, content);
			List<String> strings = toStrings(conf.getFormat());
			assertThat(strings).contains(Constants.CT_FHIR_XML_NEW, Constants.CT_FHIR_JSON_NEW, Constants.FORMAT_XML, Constants.FORMAT_JSON);
		} finally {
			status.close();
		}
	}


	@Test
	public void testCreateWithJsonLegacyNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testCreateWithJsonNewNoAcceptHeaderReturnsOperationOutcome() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON_NEW + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testCreateWithJsonNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_JSON + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testCreateWithXmlLegacyNoAcceptHeaderReturnsOperationOutcome() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testCreateWithXmlNewNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML_NEW + "; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML_NEW);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testCreateWithXmlNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(enc, ContentType.parse(Constants.CT_FHIR_XML + "; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(201);
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML_NEW);
		assertThat(responseContent).isEqualTo(expectedResponseContent);
	}

	@Test
	public void testHttpTraceNotEnabled() throws Exception {
		HttpTrace req = new HttpTrace(ourServer.getBaseUrl() + "/Patient");
		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
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
			}
		};
		req.setURI(new URI(ourServer.getBaseUrl() + "/Patient"));

		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	/**
	 * See #837
	 */
	@Test
	public void testResponseContentTypesJson() throws IOException {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		// None given
		assertThat(readAndReturnContentType(null)).isEqualTo("application/fhir+xml");

		// Legacy given
		assertThat(readAndReturnContentType("application/json+fhir")).isEqualTo("application/json+fhir");

		// Everything else JSON
		assertThat(readAndReturnContentType("application/fhir+json")).isEqualTo("application/fhir+json");
		assertThat(readAndReturnContentType("application/json")).isEqualTo("application/fhir+json");
		assertThat(readAndReturnContentType("application/fhir+json,application/json;q=0.9")).isEqualTo("application/fhir+json");
		assertThat(readAndReturnContentType("json")).isEqualTo("application/fhir+json");

		// Invalid
		assertThat(readAndReturnContentType("text/plain")).isEqualTo("application/fhir+xml");
	}

	/**
	 * See #837
	 */
	@Test
	public void testResponseContentTypesXml() throws IOException {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		// None given
		assertThat(readAndReturnContentType(null)).isEqualTo("application/fhir+json");

		// Legacy given
		assertThat(readAndReturnContentType("application/xml+fhir")).isEqualTo("application/xml+fhir");

		// Everything else JSON
		assertThat(readAndReturnContentType("application/fhir+xml")).isEqualTo("application/fhir+xml");
		assertThat(readAndReturnContentType("application/xml")).isEqualTo("application/fhir+xml");
		assertThat(readAndReturnContentType("application/fhir+xml,application/xml;q=0.9")).isEqualTo("application/fhir+xml");
		assertThat(readAndReturnContentType("xml")).isEqualTo("application/fhir+xml");

		// Invalid
		assertThat(readAndReturnContentType("text/plain")).isEqualTo("application/fhir+json");
	}

	@Test
	public void testSearchWithFormatJsonLegacy() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=" + Constants.CT_FHIR_JSON);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseContent).contains("\"resourceType\"");
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON);
	}

	@Test
	public void testSearchWithFormatJsonNew() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=" + Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);

			ourLog.info("Response was:\n{}", responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(responseContent).contains("\"resourceType\"");
			assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		}
	}

	@Test
	public void testSearchWithFormatJsonSimple() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=json");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseContent).contains("\"resourceType\"");
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	public void testSearchWithFormatXmlLegacy() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=" + Constants.CT_FHIR_XML);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML);
	}

	@Test
	public void testSearchWithFormatXmlNew() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=" + Constants.CT_FHIR_XML_NEW);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML_NEW);
	}

	@Test
	public void testSearchWithFormatXmlSimple() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertThat(status.getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_XML_NEW);
	}

	private List<String> toStrings(List<CodeType> theFormat) {
		ArrayList<String> retVal = new ArrayList<>();
		for (CodeType next : theFormat) {
			retVal.add(next.asStringValue());
		}
		return retVal;
	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theIdParam) {
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics(theIdParam.getNameFirstRep().getFamily());

			theIdParam.setId("1");
			theIdParam.getMeta().setVersionId("1");

			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(oo).setResource(theIdParam);
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
			ArrayList<IBaseResource> retVal = new ArrayList<>();

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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
