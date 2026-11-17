// Created by Claude Opus 5.5
package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

/**
 * Simulates a server without Apache Jena on the classpath (RDF/Turtle encoding unavailable) by using a
 * {@link FhirContext} whose {@link FhirContext#newRDFParser()} fails the way it does when the Jena classes are
 * missing. Requests for Turtle must then be treated like any other format HAPI can't produce or read.
 */
class ServerRdfFormatUnavailableR4Test {

	private static final String TURTLE_BODY = "@prefix fhir: <http://hl7.org/fhir/> .\n[] a fhir:Patient .";
	private static final FhirContext ourCtx = newContextWithoutRdfSupport();
	private static Binary ourLastBinary;
	private static Patient ourLastPatient;

	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.registerProvider(new BinaryProvider())
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.setDefaultPrettyPrint(false);

	/**
	 * Control: the same providers on a server whose context has RDF support (Jena is on this module's test
	 * classpath), to show that behaviour with Jena present is unchanged.
	 */
	@RegisterExtension
	private final RestfulServerExtension myServerWithRdf = new RestfulServerExtension(FhirContext.forR4Cached())
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	private final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(RestfulServer.class);

	@BeforeEach
	void before() {
		ourLastBinary = null;
		ourLastPatient = null;
	}

	@AfterEach
	void after() {
		myServer.getRestfulServer().setDefaultResponseEncoding(EncodingEnum.XML);
		myServer.unregisterAllInterceptors();
	}

	@Test
	void testContextReportsRdfUnsupported() {
		assertThat(ourCtx.isFormatRdfSupported()).isFalse();
	}

	/**
	 * Each Turtle request is paired with the equivalent request for an unknown format ("foo"), which shows the
	 * behaviour HAPI already has for formats it can't produce: the candidate is ignored and negotiation falls
	 * through to the next acceptable encoding, or the server default (XML here).
	 */
	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
		// _format                 | Accept                                          | expected content type
		"ttl                       |                                                 | application/fhir+xml",
		"text/turtle               |                                                 | application/fhir+xml",
		"application/x-turtle      |                                                 | application/fhir+xml",
		"foo                       |                                                 | application/fhir+xml",
		"ttl                       | application/fhir+json                           | application/fhir+json",
		"foo                       | application/fhir+json                           | application/fhir+json",
		"                          | text/turtle                                     | application/fhir+xml",
		"                          | application/x-turtle                            | application/fhir+xml",
		"                          | application/foo                                 | application/fhir+xml",
		"                          | text/turtle, application/fhir+json;q=0.5        | application/fhir+json",
		"                          | application/foo, application/fhir+json;q=0.5    | application/fhir+json",
	})
	void testReadWithUnavailableFormatFallsBack(String theFormat, String theAccept, String theExpectedContentType) {
		String path = "/Patient/1";
		if (isNotBlank(theFormat)) {
			path += "?_format=" + theFormat;
		}
		HttpTestRequest request = myServer.fhirRequest(path);
		if (isNotBlank(theAccept)) {
			request.withHeader(Constants.HEADER_ACCEPT, theAccept);
		}

		HttpTestResponse response = request.get();

		response.assertStatus(200);
		assertThat(contentType(response)).isEqualTo(theExpectedContentType);
		assertThat(response.getBody()).contains("Patient");
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', value = {
		// _format                 | Accept
		"ttl                       |                                                 ",
		"text/turtle               |                                                 ",
		"ttl                       | application/fhir+json                           ",
		"                          | text/turtle                                     ",
		"                          | text/turtle, application/fhir+json;q=0.5        ",
	})
	void testReadWithTurtleWhenRdfSupportedIsUnchanged(String theFormat, String theAccept) {
		String path = "/Patient/1";
		if (isNotBlank(theFormat)) {
			path += "?_format=" + theFormat;
		}
		HttpTestRequest request = myServerWithRdf.fhirRequest(path);
		if (isNotBlank(theAccept)) {
			request.withHeader(Constants.HEADER_ACCEPT, theAccept);
		}

		HttpTestResponse response = request.get();

		response.assertStatus(200);
		assertThat(EncodingEnum.forContentType(contentType(response))).isEqualTo(EncodingEnum.RDF);
		assertThat(response.getBody()).contains("fhir:Patient");
	}

	@Test
	void testCreateWithTurtleBodyWhenRdfSupportedIsUnchanged() {
		Patient patient = new Patient();
		patient.addName().setFamily("FAMILY");
		String body = myServerWithRdf.getFhirContext().newRDFParser().encodeResourceToString(patient);

		HttpTestResponse response =
			myServerWithRdf.fhirRequest("/Patient").post(body, Constants.CT_RDF_TURTLE_LEGACY + "; charset=utf-8");

		response.assertStatus(201);
		assertThat(ourLastPatient).isNotNull();
		assertThat(ourLastPatient.getNameFirstRep().getFamily()).isEqualTo("FAMILY");
	}

	@Test
	void testReadWithTurtleContentTypeAndNoAcceptFallsBackToDefault() {
		HttpTestResponse response = myServer.fhirRequest("/Patient/1")
			.withHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_RDF_TURTLE_LEGACY)
			.get();

		response.assertStatus(200);
		assertThat(contentType(response)).isEqualTo(Constants.CT_FHIR_XML_NEW);
	}

	@Test
	void testCreateWithUnknownContentType_existingBehaviour() {
		HttpTestResponse response = myServer.fhirRequest("/Patient").post(TURTLE_BODY, "application/foo");

		response.assertStatus(400);
		assertThat(response.getBody()).contains("HAPI-0449");
		assertThat(ourLastPatient).isNull();
	}

	@ParameterizedTest
	@CsvSource({Constants.CT_RDF_TURTLE_LEGACY, Constants.CT_RDF_TURTLE, "application/fhir+turtle"})
	void testCreateWithTurtleBodyIsRejected(String theContentType) {
		HttpTestResponse response =
			myServer.fhirRequest("/Patient").post(TURTLE_BODY, theContentType + "; charset=utf-8");

		response.assertStatus(400);
		assertThat(response.getBody())
			.contains("HAPI-3062")
			.contains("RDF/Turtle encoding is not available on this server");
		assertThat(ourLastPatient).isNull();
	}

	@Test
	void testCreateBinaryWithTurtleContentTypeIsStoredAsRawContent() {
		byte[] body = TURTLE_BODY.getBytes(StandardCharsets.UTF_8);

		HttpTestResponse response = myServer.fhirRequest("/Binary").post(body, Constants.CT_RDF_TURTLE_LEGACY);

		response.assertStatus(201);
		assertThat(ourLastBinary).isNotNull();
		assertThat(ourLastBinary.getContentType()).isEqualTo(Constants.CT_RDF_TURTLE_LEGACY);
		assertThat(ourLastBinary.getContent()).isEqualTo(body);
	}

	@Test
	void testDefaultResponseEncodingRdfFallsBackToJson() {
		myServer.getRestfulServer().setDefaultResponseEncoding(EncodingEnum.RDF);

		HttpTestResponse response = myServer.fhirRequest("/Patient/1").get();

		response.assertStatus(200);
		assertThat(contentType(response)).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		assertThat(response.getBody()).startsWith("{");
	}

	@Test
	void testDefaultResponseEncodingRdfFallsBackToJsonInHighlighter() {
		myServer.getRestfulServer().setDefaultResponseEncoding(EncodingEnum.RDF);
		myServer.registerInterceptor(new ResponseHighlighterInterceptor());

		HttpTestResponse response = myServer.fhirRequest("/Patient/1")
			.withHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9")
			.get();

		response.assertStatus(200);
		assertThat(contentType(response)).isEqualTo(Constants.CT_HTML);
		assertThat(response.getBody()).contains("resourceType");
	}

	@Test
	void testInitWithDefaultResponseEncodingRdfLogsWarning() throws Exception {
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.RDF);

		server.init();

		LogbackTestExtensionAssert.assertThat(myLogbackTestExtension)
			.hasWarnMessage("RDF/Turtle encoding is not available");
	}

	private static String contentType(HttpTestResponse theResponse) {
		return theResponse.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", "");
	}

	private static FhirContext newContextWithoutRdfSupport() {
		FhirContext retVal = spy(FhirContext.forR4());
		doThrow(new NoClassDefFoundError("org/apache/jena/riot/Lang")).when(retVal).newRDFParser();
		return retVal;
	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			retVal.addName().setFamily("FAMILY");
			return retVal;
		}

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			ourLastPatient = thePatient;
			return new MethodOutcome(new IdType("Patient/1/_history/1"));
		}
	}

	public static class BinaryProvider implements IResourceProvider {

		@Override
		public Class<Binary> getResourceType() {
			return Binary.class;
		}

		@Create
		public MethodOutcome create(@ResourceParam Binary theBinary) {
			ourLastBinary = theBinary;
			return new MethodOutcome(new IdType("Binary/1/_history/1"));
		}
	}
}
