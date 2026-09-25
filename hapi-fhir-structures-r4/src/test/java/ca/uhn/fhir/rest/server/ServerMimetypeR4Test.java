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
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerMimetypeR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerMimetypeR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	private String readAndReturnContentType(String theAccept) throws IOException {
		HttpTestRequest request = ourServer.fhirRequest("/Patient");
		if (theAccept != null) {
			request.withHeader(Constants.HEADER_ACCEPT, theAccept);
		}
		String contentType = request.get().getHeader(Constants.HEADER_CONTENT_TYPE);
		contentType = contentType.replaceAll(";.*", "");
		return contentType;
	}

	@Test
	public void testConformanceMetadataUsesNewMimetypes() throws Exception {
		String content = ourServer.fhirRequest("/metadata").get().getBody();
		CapabilityStatement conf = ourCtx.newXmlParser().parseResource(CapabilityStatement.class, content);
		List<String> strings = toStrings(conf.getFormat());
		assertThat(strings).contains(Constants.CT_FHIR_XML_NEW, Constants.CT_FHIR_JSON_NEW, Constants.FORMAT_XML, Constants.FORMAT_JSON);
	}


	@Test
	public void testCreateWithJsonLegacyNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(enc, Constants.CT_FHIR_JSON + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_JSON, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithJsonNewNoAcceptHeaderReturnsOperationOutcome() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"diagnostics\":\"FAMILY\"}]}";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.post(enc, Constants.CT_FHIR_JSON_NEW + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithJsonNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newJsonParser().encodeResourceToString(p);
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"name\":[{\"family\":\"FAMILY\"}]}";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW).post(enc, Constants.CT_FHIR_JSON + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithXmlLegacyNoAcceptHeaderReturnsOperationOutcome() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><diagnostics value=\"FAMILY\"/></issue></OperationOutcome>";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.post(enc, Constants.CT_FHIR_XML + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_XML, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithXmlNewNoAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(enc, Constants.CT_FHIR_XML_NEW + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithXmlNewWithAcceptHeader() throws Exception {
		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		String expectedResponseContent = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/></meta><name><family value=\"FAMILY\"/></name></Patient>";

		HttpTestResponse response = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW).post(enc, Constants.CT_FHIR_XML + "; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testHttpTraceNotEnabled() throws Exception {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACE");
		ourLog.info(status.toString());
		assertEquals(400, status.getStatusCode());
	}

	@Test
	public void testHttpTrackNotEnabled() throws Exception {
		HttpTestResponse status = ourServer.fhirRequest("/Patient").method("TRACK");
		ourLog.info(status.toString());
		assertEquals(400, status.getStatusCode());
	}

	/**
	 * See #837
	 */
	@Test
	public void testResponseContentTypesJson() throws IOException {
		ourServer.setDefaultResponseEncoding(EncodingEnum.XML);

		// None given
		assertEquals("application/fhir+xml", readAndReturnContentType(null));

		// Legacy given
		assertEquals("application/json+fhir", readAndReturnContentType("application/json+fhir"));

		// Everything else JSON
		assertEquals("application/fhir+json", readAndReturnContentType("application/fhir+json"));
		assertEquals("application/fhir+json", readAndReturnContentType("application/json"));
		assertEquals("application/fhir+json", readAndReturnContentType("application/fhir+json,application/json;q=0.9"));
		assertEquals("application/fhir+json", readAndReturnContentType("json"));

		// Invalid
		assertEquals("application/fhir+xml", readAndReturnContentType("text/plain"));
	}

	/**
	 * See #837
	 */
	@Test
	public void testResponseContentTypesXml() throws IOException {
		ourServer.setDefaultResponseEncoding(EncodingEnum.JSON);

		// None given
		assertEquals("application/fhir+json", readAndReturnContentType(null));

		// Legacy given
		assertEquals("application/xml+fhir", readAndReturnContentType("application/xml+fhir"));

		// Everything else JSON
		assertEquals("application/fhir+xml", readAndReturnContentType("application/fhir+xml"));
		assertEquals("application/fhir+xml", readAndReturnContentType("application/xml"));
		assertEquals("application/fhir+xml", readAndReturnContentType("application/fhir+xml,application/xml;q=0.9"));
		assertEquals("application/fhir+xml", readAndReturnContentType("xml"));

		// Invalid
		assertEquals("application/fhir+json", readAndReturnContentType("text/plain"));
	}

	@Test
	public void testSearchWithFormatJsonLegacy() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_JSON).get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("\"resourceType\"");
		assertEquals(Constants.CT_FHIR_JSON, response.getHeader("content-type").replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatJsonNew() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_JSON_NEW).get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("\"resourceType\"");
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatJsonSimple() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=json").get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("\"resourceType\"");
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatXmlLegacy() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_XML).get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertEquals(Constants.CT_FHIR_XML, response.getHeader("content-type").replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatXmlNew() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=" + Constants.CT_FHIR_XML_NEW).get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
	}

	@Test
	public void testSearchWithFormatXmlSimple() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient?_format=xml").get();
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(200);
		assertThat(responseContent).contains("<Patient xmlns=\"http://hl7.org/fhir\">");
		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getHeader("content-type").replaceAll(";.*", ""));
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
