package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.FhirHttpResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.helger.collection.iterate.ArrayEnumeration;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.cors.CorsConfiguration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseHighlighterInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlighterInterceptorTest.class);
	private static final ResponseHighlighterInterceptor ourInterceptor = new ResponseHighlighterInterceptor();
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private DummyPatientResourceProvider ourPatientProvider = new DummyPatientResourceProvider();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(ourPatientProvider)
		 .registerProvider(new DummyBinaryResourceProvider())
		 .registerProvider(new GraphQLProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false)
		 .withServer(s->s.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE));

	@BeforeEach
	void before() {
		/*
		 * Enable CORS
		 */
		CorsConfiguration config = new CorsConfiguration();
		CorsInterceptor corsInterceptor = new CorsInterceptor(config);
		config.addAllowedHeader(Constants.HEADER_CORS_ORIGIN);
		config.addAllowedHeader(Constants.HEADER_ACCEPT);
		config.addAllowedHeader(Constants.HEADER_X_REQUESTED_WITH);
		config.addAllowedHeader(Constants.HEADER_CONTENT_TYPE);
		config.addAllowedHeader(Constants.HEADER_CORS_REQUEST_METHOD);
		config.addAllowedHeader(Constants.HEADER_CORS_REQUEST_HEADERS);
		config.addAllowedOrigin("*");
		config.addExposedHeader(Constants.HEADER_LOCATION);
		config.addExposedHeader(Constants.HEADER_CONTENT_LOCATION);
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		ourServer.registerInterceptor(corsInterceptor);

		ourServer.registerInterceptor(ourInterceptor);

		ResponseHighlighterInterceptor defaults = new ResponseHighlighterInterceptor();
		ourInterceptor.setShowRequestHeaders(defaults.isShowRequestHeaders());
		ourInterceptor.setShowResponseHeaders(defaults.isShowResponseHeaders());
		ourInterceptor.setShowNarrative(defaults.isShowNarrative());
		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
	}

	/**
	 * Return a Binary response type - Client accepts text/html but is not a browser
	 */
	@Test
	void testBinaryOperationHtmlResponseFromProvider() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/html/$binaryOp")
			.withHeader("Accept", "text/html")
			.get();

		status.assertStatus(200);
		assertEquals("text/html", status.getHeader("content-type"));
		assertEquals("<html>DATA</html>", status.getBody());
		assertEquals("Attachment;", status.getHeader("Content-Disposition"));
	}

	@Test
	void testInvalidRequest() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/html?_elements=Patient:foo")
			.withHeader("Accept", "text/html")
			.get();

		status.assertStatus(400);
		assertThat(status.getHeader("content-type")).contains("text/html");
		assertThat(status.getBody()).contains("Invalid _elements value");
	}

	@Test
	void testBinaryReadAcceptBrowser() {
		FhirHttpResponse status = ourServer.fhirRequest("/Binary/foo")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.withHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.get();

		status.assertStatus(200);
		assertEquals("foo", status.getHeader("content-type"));
		assertEquals("Attachment;", status.getHeader("Content-Disposition"));
		assertThat(status.getBody().getBytes(StandardCharsets.UTF_8)).containsExactly(new byte[]{1, 2, 3, 4});
	}

	/**
	 * Return a Binary response type - Client accepts text/html but is not a browser
	 */
	@Test
	void testBinaryReadHtmlResponseFromProvider() {
		FhirHttpResponse status = ourServer.fhirRequest("/Binary/html")
			.withHeader("Accept", "text/html")
			.get();

		status.assertStatus(200);
		assertEquals("text/html", status.getHeader("content-type"));
		assertEquals("<html>DATA</html>", status.getBody());
		assertEquals("Attachment;", status.getHeader("Content-Disposition"));
	}

	@Test
	void testBinaryReadAcceptFhirJson() {
		FhirHttpResponse status = ourServer.fhirRequest("/Binary/foo")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.withHeader("Accept", Constants.CT_FHIR_JSON)
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertNull(status.getHeader("Content-Disposition"));
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"foo\",\"contentType\":\"foo\",\"data\":\"AQIDBA==\"}", status.getBody());

	}

	@Test
	void testBinaryReadAcceptMissing() {
		FhirHttpResponse status = ourServer.fhirRequest("/Binary/foo").get();

		status.assertStatus(200);
		assertEquals("foo", status.getHeader("content-type"));
		assertEquals("Attachment;", status.getHeader("Content-Disposition"));
		assertThat(status.getBody().getBytes(StandardCharsets.UTF_8)).containsExactly(new byte[]{1, 2, 3, 4});

	}

	@Test
	void testDontHighlightWhenOriginHeaderPresent() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));
		when(req.getHeader(Constants.HEADER_CORS_ORIGIN)).thenAnswer(theInvocation -> "http://example.com");

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<>();
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// true means it decided to not handle the request..
		assertTrue(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

	}

	@Test
	void testExtractNarrativeHtml_DomainResource() {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div>HELLO</div>");

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), patient);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO</div>", outcome);
	}

	@Test
	void testExtractNarrativeHtml_NonDomainResource() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), bundle);
		assertNull(outcome);
	}

	@Test
	void testExtractNarrativeHtml_DocumentWithCompositionNarrative() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		Composition composition = new Composition();
		composition.getText().setDivAsString("<div>HELLO</div>");

		// Add sections with title and narrative (should be includee)
		composition.addSection().setTitle("Section 1").getText().setDivAsString("<div>HELLO 2</div>");
		composition.addSection().setTitle("Section 2").getText().setDivAsString("<div>HELLO 3</div>");

		// Add sections with no title or no narrative (should not be included)
		composition.addSection().setTitle("Section 3").getText().setDivAsString("");
		composition.addSection().setTitle("").getText().setDivAsString("<div>HELLO 5</div>");

		bundle.addEntry().setResource(composition);

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), bundle);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><div>HELLO</div><h1>Section 1</h1><div>HELLO 2</div><h1>Section 2</h1><div>HELLO 3</div></div>", outcome);
	}

	@Test
	void testExtractNarrativeHtml_ParametersWithNarrativeAsFirstParameter() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new StringType("<div>HELLO</div>"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO</div>", outcome);
	}

	@Test
	void testExtractNarrativeHtml_Parameters() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Foo", new StringType("<div>HELLO</div>"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_1() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new Quantity(123L));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_2() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", (Type)null);

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_3() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new StringType("hello"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	void testForceApplicationJson() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=application/json")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceApplicationJsonFhir() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=application/json+fhir")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceApplicationJsonPlusFhir() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/json+fhir"))
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceApplicationXml() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=application/xml")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceApplicationXmlFhir() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=application/xml+fhir")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceApplicationXmlPlusFhir() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/xml+fhir"))
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceHtmlJson() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();
		ourLog.info(status.getBody());

		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).contains("<html");
		assertThat(status.getBody()).contains(">{<");
		assertThat(status.getBody()).contains(Constants.HEADER_REQUEST_ID);

	}

	@Test
	void testForceHtmlTurtle() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/turtle")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();
		ourLog.info(status.getBody());

		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).contains("<html");
		assertThat(status.getBody()).contains("<span class='hlQuot'>&quot;urn:hapitest:mrns&quot;</span>");
		assertThat(status.getBody()).contains(Constants.HEADER_REQUEST_ID);

	}

	@Test
	void testForceHtmlJsonWithAdditionalParts() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=" + UrlUtil.escapeUrlParam("html/json; fhirVersion=1.0"))
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).contains("<html");
		assertThat(status.getBody()).contains(">{<");

		ourLog.info(status.getBody());
	}

	@Test
	void testForceHtmlXml() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/xml")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).contains("<html");
		assertThat(status.getBody()).doesNotContain(">{<");
		assertThat(status.getBody()).contains("&lt;");
	}

	@Test
	void testForceJson() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=json")
			.withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.get();

		status.assertStatus(200);
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContain("<html");
	}

	@Test
	void testForceResponseTime() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json").get();
		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody().replace('\n', ' ').replace('\r', ' ')).matches(".*Response generated in [0-9]+ms.*");

	}

	@Test
	void testGetInvalidResource() {
		FhirHttpResponse status = ourServer.fhirRequest("/Foobar/123")
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(404);

		assertThat(status.getBody()).containsSubsequence("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle");

	}

	@Test
	void testGetInvalidResourceNoAcceptHeader() {
		FhirHttpResponse status = ourServer.fhirRequest("/Foobar/123").get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(404);

		assertThat(status.getBody()).doesNotContainPattern("(?s)<span class='hlTagName'>OperationOutcome</span>.*Unknown resource type 'Foobar' - Server knows how to handle");
		assertThat(status.getBody()).contains("Unknown resource type 'Foobar'");
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());

	}

	@Test
	void testGetRoot() {
		FhirHttpResponse status = ourServer.fhirRequest("/")
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(400);

		assertThat(status.getBody()).containsSubsequence("<span class='hlTagName'>OperationOutcome</span>", "This is the base URL of FHIR server. Unable to handle this request, as it does not contain a resource type or operation name.");

	}

	@Test
	void testHighlightGraphQLResponse() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"))
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(200);

		assertThat(status.getBody()).containsSubsequence("&quot;foo&quot;");

	}

	@Test
	void testHighlightGraphQLResponseNonHighlighted() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"))
			.withHeader("Accept", "application/jon")
			.get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(200);

		assertThat(status.getBody()).containsSubsequence("{\"foo\":\"bar\"}");

	}

	@Test
	void testHighlightException() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.XML);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new OperationOutcome.OperationOutcomeIssueComponent().setDiagnostics("Hello")));

		assertFalse(ourInterceptor.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output).contains("<span class='hlTagName'>OperationOutcome</span>");
	}

	@Test
	void testHighlightExceptionInvokesOutgoingFailureOperationOutcome() {
		IAnonymousInterceptor outgoingResponseInterceptor = (thePointcut, theArgs) -> {
			OperationOutcome oo = (OperationOutcome) theArgs.get(IBaseOperationOutcome.class);
			oo.addIssue().setDiagnostics("HELP IM A BUG");
		};
		ourServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME, outgoingResponseInterceptor);
		try {

			FhirHttpResponse status = ourServer.fhirRequest("/Foobar/123")
				.withHeader("Accept", "text/html")
				.get();

			ourLog.info("Resp: {}", status.getBody());
			status.assertStatus(404);
			assertThat(status.getBody()).containsSubsequence("HELP IM A BUG");

		} finally {

			ourServer.getInterceptorService().unregisterInterceptor(outgoingResponseInterceptor);

		}
	}


	/**
	 * See #346
	 */
	@Test
	void testHighlightForceHtmlCt() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("application/xml+fhir"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<>();
		params.put(Constants.PARAM_FORMAT, new String[]{Constants.FORMAT_HTML});
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// false means it decided to handle the request..
		assertFalse(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	/**
	 * See #346
	 */
	@Test
	void testHighlightForceHtmlFormat() throws Exception {

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("application/xml+fhir"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<>();
		params.put(Constants.PARAM_FORMAT, new String[]{Constants.CT_HTML});
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// false means it decided to handle the request..
		assertFalse(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	@Test
	void testHighlightForceRaw() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<>();
		params.put(Constants.PARAM_PRETTY, new String[]{Constants.PARAM_PRETTY_VALUE_TRUE});
		params.put(Constants.PARAM_FORMAT, new String[]{Constants.CT_XML});
		params.put(ResponseHighlighterInterceptor.PARAM_RAW, new String[]{ResponseHighlighterInterceptor.PARAM_RAW_TRUE});
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// true means it decided to not handle the request..
		assertTrue(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

	}

	@Test
	void testHighlightNormalResponse() throws Exception {

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<>());
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.XML);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		assertFalse(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output).contains("<span class='hlTagName'>Patient</span>")
			.containsSubsequence("<body>", "<pre>", "<div", "</pre>")
			.contains("<a href=\"?_format=json\">");
	}

	@Test
	void testHighlightNormalResponseForcePrettyPrint() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<>();
		params.put(Constants.PARAM_PRETTY, new String[]{Constants.PARAM_PRETTY_VALUE_TRUE});
		reqDetails.setParameters(params);
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.XML);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		assertFalse(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output).contains("<span class='hlTagName'>Patient</span>")
			 .containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	/**
	 * Browsers declare XML but not JSON in their accept header, we should still respond using JSON if that's the default
	 */
	@Test
	void testHighlightProducesDefaultJsonWithBrowserRequest() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);

		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<>());
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.JSON);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		assertFalse(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output).contains("resourceType");
	}

	@Test
	void testHighlightProducesDefaultJsonWithBrowserRequest2() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);

		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html;q=0.8,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<>());
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.JSON);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		// True here means the interceptor didn't handle the request, because HTML wasn't the top ranked accept header
		assertTrue(ourInterceptor.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	/**
	 * See #464
	 */
	@Test
	void testPrettyPrintDefaultsToTrue() {
		ourServer.setDefaultPrettyPrint(false);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1")
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertThat(status.getBody()).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	/**
	 * See #464
	 */
	@Test
	void testPrettyPrintDefaultsToTrueWithExplicitFalse() {
		ourServer.setDefaultPrettyPrint(false);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_pretty=false")
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertThat(status.getBody()).doesNotContainPattern("(?s)<body>.*<pre>.*\n.*</pre>");
	}

	/**
	 * See #464
	 */
	@Test
	void testPrettyPrintDefaultsToTrueWithExplicitTrue() {
		ourServer.setDefaultPrettyPrint(false);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_pretty=true")
			.withHeader("Accept", "text/html")
			.get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertThat(status.getBody()).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	@Test
	void testSearchWithSummaryParam() {
		FhirHttpResponse status = ourServer.fhirRequest("/Patient?_query=searchWithWildcardRetVal&_summary=count")
			.withHeader("Accept", "html")
			.get();

		ourLog.info("Resp: {}", status.getBody());
		status.assertStatus(200);
		assertThat(status.getBody()).doesNotContain("entry");
	}

	@Test
	void testShowNeither() {
		ourInterceptor.setShowRequestHeaders(false);
		ourInterceptor.setShowResponseHeaders(false);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json").get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContainIgnoringCase("Accept");
		assertThat(status.getBody()).doesNotContainIgnoringCase("Content-Type");
	}

	@Test
	void testShowRequest() {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(false);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json").get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).containsIgnoringCase("Accept");
		assertThat(status.getBody()).doesNotContainIgnoringCase("Content-Type");
	}

	@Test
	void testShowRequestAndResponse() {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(true);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json").get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).containsIgnoringCase("Accept");
		assertThat(status.getBody()).containsIgnoringCase("Content-Type");
	}

	@Test
	void testShowResponse() {
		ourInterceptor.setShowResponseHeaders(true);

		FhirHttpResponse status = ourServer.fhirRequest("/Patient/1?_format=html/json").get();

		ourLog.info(status.getBody());
		status.assertStatus(200);
		assertEquals("text/html;charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertThat(status.getBody()).doesNotContainIgnoringCase("Accept");
		assertThat(status.getBody()).containsIgnoringCase("Content-Type");
	}

	@Test
	void testNarrative() {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		FhirHttpResponse response = ourServer.fhirRequest("/Patient/1/$patientOp?_format=html/json").get();
		assertThat(response.getBody()).contains("<h1>Narrative</h1>");
		assertThat(response.getBody()).contains("<thead><tr><th>Header1</th><th>Header2</th></tr></thead>");

	}


	@Test
	void testNarrative_Disabled() {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		ourInterceptor.setShowNarrative(false);

		FhirHttpResponse response = ourServer.fhirRequest("/Patient/1/$patientOp?_format=html/json").get();
		assertThat(response.getBody()).doesNotContain("<h1>Narrative</h1>");
		assertThat(response.getBody()).doesNotContain("<thead><tr><th>Header1</th><th>Header2</th></tr></thead>");

	}

	@Test
	void testNarrative_SketchyTagBlocked() {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table onclick=\"foo();\"><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		FhirHttpResponse response = ourServer.fhirRequest("/Patient/1/$patientOp?_format=html/json").get();
		assertThat(response.getBody()).contains("<table><thead><tr><th>Header1</th>");

	}

	@Test
	void testNullResponseResource() {
		ourInterceptor.setShowResponseHeaders(true);

		final RequestDetails requestDetails = mock(RequestDetails.class);
		when(requestDetails.getRequestType()).thenReturn(RequestTypeEnum.GET);
		final IRestfulServerDefaults server = mock(IRestfulServerDefaults.class);
		when(server.getDefaultResponseEncoding()).thenReturn(EncodingEnum.JSON);
		when(server.getFhirContext()).thenReturn(ourCtx);
		when(requestDetails.getServer()).thenReturn(server);

		final ResponseDetails responseObject = mock(ResponseDetails.class);

		final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		final Enumeration<String> headers = mock(Enumeration.class);
		when(headers.hasMoreElements()).thenReturn(true).thenReturn(false);
		when(headers.nextElement()).thenReturn("text/html");
		when(servletRequest.getHeaders(Constants.HEADER_ACCEPT)).thenReturn(headers);

		final HttpServletResponse servletResponse = mock(HttpServletResponse.class);

		assertTrue(ourInterceptor.outgoingResponse(requestDetails, responseObject, servletRequest, servletResponse));
	}

	class TestServletRequestDetails extends ServletRequestDetails {
		TestServletRequestDetails(IInterceptorBroadcaster theInterceptorBroadcaster) {
			super(theInterceptorBroadcaster);
		}

		@Override
		public String getServerBaseForRequest() {
			return "/baseDstu3";
		}
	}

	static class GraphQLProvider {
		@GraphQL
		public String processGraphQlRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQuery) {
			return "{\"foo\":\"bar\"}";
		}
	}

	static class DummyBinaryResourceProvider implements IResourceProvider {

		@Override
		public Class<Binary> getResourceType() {
			return Binary.class;
		}

		@Read
		public Binary read(@IdParam IdType theId) {
			Binary retVal = new Binary();
			retVal.setId(theId);
			if (theId.getIdPart().equals("html")) {
				retVal.setContent("<html>DATA</html>".getBytes(Charsets.UTF_8));
				retVal.setContentType("text/html");
			} else {
				retVal.setContent(new byte[]{1, 2, 3, 4});
				retVal.setContentType(theId.getIdPart());
			}
			return retVal;
		}

		@Search
		public List<Binary> search() {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[]{1, 2, 3, 4});
			retVal.setContentType("text/plain");
			return Collections.singletonList(retVal);
		}

	}

	static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient myNextPatientOpResponse;

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
			patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).setFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.setId("1");
			return patient;
		}

		@Search(queryName = "findPatientsWithAbsoluteIdSpecified")
		public List<Patient> findPatientsWithAbsoluteIdSpecified() {
			Patient p = new Patient();
			p.addIdentifier().setSystem("foo");
			p.setId("http://absolute.com/Patient/123/_history/22");

			Organization o = new Organization();
			o.setId("http://foo.com/Organization/222/_history/333");
			p.getManagingOrganization().setResource(o);

			return Collections.singletonList(p);
		}

		@Search(queryName = "findPatientsWithNoIdSpecified")
		public List<Patient> findPatientsWithNoIdSpecified() {
			Patient p = new Patient();
			p.addIdentifier().setSystem("foo");
			return Collections.singletonList(p);
		}

		@Operation(name = "binaryOp", idempotent = true)
		public Binary binaryOp(@IdParam IdType theId) {
			Binary retVal = new Binary();
			retVal.setId(theId);
			if (theId.getIdPart().equals("html")) {
				retVal.setContent("<html>DATA</html>".getBytes(Charsets.UTF_8));
				retVal.setContentType("text/html");
			} else {
				retVal.setContent(new byte[]{1, 2, 3, 4});
				retVal.setContentType(theId.getIdPart());
			}
			return retVal;
		}


		Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new Identifier());
				patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
				patient.getIdentifier().get(0).setSystem("urn:hapitest:mrns");
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanName());
				patient.getName().get(0).setFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.setId("2");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 *
		 * @param theId The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdType theId) {
			String key = theId.getIdPart();
			return getIdToPatient().get(key);
		}

		/**
		 * Retrieve the resource by its identifier
		 *
		 * @param theId The resource identity
		 * @return The resource
		 */
		@Search()
		public List<Patient> getResourceById(@RequiredParam(name = "_id") String theId) {
			Patient patient = getIdToPatient().get(theId);
			if (patient != null) {
				return Collections.singletonList(patient);
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search(queryName = "searchWithWildcardRetVal")
		public List<IBaseResource> searchWithWildcardRetVal() {
			Patient p = new Patient();
			p.setId("1234");
			p.addName().setFamily("searchWithWildcardRetVal");
			return Collections.singletonList(p);
		}

		@Operation(name = "patientOp", idempotent = true)
		public Patient patientOp(@IdParam IIdType theId) {
			return myNextPatientOpResponse;
		}

	}

	@AfterAll
	static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Nonnull
	private static SystemRequestDetails newRequest() {
		SystemRequestDetails retVal = new SystemRequestDetails();
		retVal.setFhirContext(ourCtx);
		return retVal;
	}

}
