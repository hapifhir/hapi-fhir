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
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.helger.collection.iterate.ArrayEnumeration;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Narrative;
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

import java.io.IOException;
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

public class ResponseHighlighterInterceptorTest {

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

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		/*
		 * Enable CORS
		 */
		CorsConfiguration config = new CorsConfiguration();
		CorsInterceptor corsInterceptor = new CorsInterceptor(config);
		config.addAllowedHeader("Origin");
		config.addAllowedHeader("Accept");
		config.addAllowedHeader("X-Requested-With");
		config.addAllowedHeader("Content-Type");
		config.addAllowedHeader("Access-Control-Request-Method");
		config.addAllowedHeader("Access-Control-Request-Headers");
		config.addAllowedOrigin("*");
		config.addExposedHeader("Location");
		config.addExposedHeader("Content-Location");
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
	public void testBinaryOperationHtmlResponseFromProvider() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/html/$binaryOp");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html", status.getFirstHeader("content-type").getValue());
		assertEquals("<html>DATA</html>", responseContent);
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
	}

	@Test
	public void testInvalidRequest() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/html?_elements=Patient:foo");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(400, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("content-type").getValue()).contains("text/html");
		assertThat(responseContent).contains("Invalid _elements value");
	}

	@Test
	public void testBinaryReadAcceptBrowser() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Binary/foo");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertThat(responseContent).containsExactly(new byte[]{1, 2, 3, 4});
	}

	/**
	 * Return a Binary response type - Client accepts text/html but is not a browser
	 */
	@Test
	public void testBinaryReadHtmlResponseFromProvider() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Binary/html");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html", status.getFirstHeader("content-type").getValue());
		assertEquals("<html>DATA</html>", responseContent);
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
	}

	@Test
	public void testBinaryReadAcceptFhirJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Binary/foo");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON);

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertNull(status.getFirstHeader("Content-Disposition"));
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"foo\",\"contentType\":\"foo\",\"data\":\"AQIDBA==\"}", responseContent);

	}

	@Test
	public void testBinaryReadAcceptMissing() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Binary/foo");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertThat(responseContent).containsExactly(new byte[]{1, 2, 3, 4});

	}

	@Test
	public void testDontHighlightWhenOriginHeaderPresent() throws Exception {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));
		when(req.getHeader(Constants.HEADER_ORIGIN)).thenAnswer(theInvocation -> "http://example.com");

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
	public void testExtractNarrativeHtml_DomainResource() {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div>HELLO</div>");

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), patient);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO</div>", outcome);
	}

	@Test
	public void testExtractNarrativeHtml_NonDomainResource() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), bundle);
		assertNull(outcome);
	}

	@Test
	public void testExtractNarrativeHtml_DocumentWithCompositionNarrative() {
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
	public void testExtractNarrativeHtml_ParametersWithNarrativeAsFirstParameter() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new StringType("<div>HELLO</div>"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO</div>", outcome);
	}

	@Test
	public void testExtractNarrativeHtml_Parameters() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Foo", new StringType("<div>HELLO</div>"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	public void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_1() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new Quantity(123L));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	public void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_2() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", (Type)null);

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	public void testExtractNarrativeHtml_ParametersWithNonNarrativeFirstParameter_3() {
		Parameters parameters = new Parameters();
		parameters.addParameter("Narrative", new StringType("hello"));

		String outcome = ourInterceptor.extractNarrativeHtml(newRequest(), parameters);
		assertNull(outcome);
	}

	@Test
	public void testForceApplicationJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=application/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceApplicationJsonFhir() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=application/json+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceApplicationJsonPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/json+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceApplicationXml() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=application/xml");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceApplicationXmlFhir() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=application/xml+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceApplicationXmlPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/xml+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceHtmlJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).contains("<html");
		assertThat(responseContent).contains(">{<");
		assertThat(responseContent).contains(Constants.HEADER_REQUEST_ID);

	}

	@Test
	public void testForceHtmlTurtle() throws Exception {
		String url = ourServer.getBaseUrl() + "/Patient/1?_format=html/turtle";
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).contains("<html");
		assertThat(responseContent).contains("<span class='hlQuot'>&quot;urn:hapitest:mrns&quot;</span>");
		assertThat(responseContent).contains(Constants.HEADER_REQUEST_ID);

	}

	@Test
	public void testForceHtmlJsonWithAdditionalParts() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("html/json; fhirVersion=1.0"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).contains("<html");
		assertThat(responseContent).contains(">{<");

		ourLog.info(responseContent);
	}

	@Test
	public void testForceHtmlXml() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/xml");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).contains("<html");
		assertThat(responseContent).doesNotContain(">{<");
		assertThat(responseContent).contains("&lt;");
	}

	@Test
	public void testForceJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContain("<html");
	}

	@Test
	public void testForceResponseTime() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent.replace('\n', ' ').replace('\r', ' ')).matches(".*Response generated in [0-9]+ms.*");

	}

	@Test
	public void testGetInvalidResource() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Foobar/123");
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent).containsSubsequence("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle");

	}

	@Test
	public void testGetInvalidResourceNoAcceptHeader() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Foobar/123");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent).doesNotContainPattern("(?s)<span class='hlTagName'>OperationOutcome</span>.*Unknown resource type 'Foobar' - Server knows how to handle");
		assertThat(responseContent).contains("Unknown resource type 'Foobar'");
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());

	}

	@Test
	public void testGetRoot() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/");
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent).containsSubsequence("<span class='hlTagName'>OperationOutcome</span>", "This is the base URL of FHIR server. Unable to handle this request, as it does not contain a resource type or operation name.");

	}

	@Test
	public void testHighlightGraphQLResponse() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"));
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(responseContent).containsSubsequence("&quot;foo&quot;");

	}

	@Test
	public void testHighlightGraphQLResponseNonHighlighted() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/A/$graphql?query=" + UrlUtil.escapeUrlParam("{name}"));
		httpGet.addHeader("Accept", "application/jon");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(responseContent).containsSubsequence("{\"foo\":\"bar\"}");

	}

	@Test
	public void testHighlightException() throws Exception {
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

		// This can be null depending on the exception type
		// reqDetails.setParameters(null);

		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new OperationOutcome.OperationOutcomeIssueComponent().setDiagnostics("Hello")));

		assertFalse(ourInterceptor.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output).contains("<span class='hlTagName'>OperationOutcome</span>");
	}

	@Test
	public void testHighlightExceptionInvokesOutgoingFailureOperationOutcome() throws Exception {
		IAnonymousInterceptor outgoingResponseInterceptor = (thePointcut, theArgs) -> {
			OperationOutcome oo = (OperationOutcome) theArgs.get(IBaseOperationOutcome.class);
			oo.addIssue().setDiagnostics("HELP IM A BUG");
		};
		ourServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME, outgoingResponseInterceptor);
		try {

			HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Foobar/123");
			httpGet.addHeader("Accept", "text/html");
			CloseableHttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			status.close();

			ourLog.info("Resp: {}", responseContent);
			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent).containsSubsequence("HELP IM A BUG");

		} finally {

			ourServer.getInterceptorService().unregisterInterceptor(outgoingResponseInterceptor);

		}
	}


	/**
	 * See #346
	 */
	@Test
	public void testHighlightForceHtmlCt() throws Exception {
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
	public void testHighlightForceHtmlFormat() throws Exception {

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
	public void testHighlightForceRaw() throws Exception {
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
	public void testHighlightNormalResponse() throws Exception {

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
		assertThat(output).contains("<span class='hlTagName'>Patient</span>");
		assertThat(output).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
		assertThat(output).contains("<a href=\"?_format=json\">");
	}

	@Test
	public void testHighlightNormalResponseForcePrettyPrint() throws Exception {
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
		assertThat(output).contains("<span class='hlTagName'>Patient</span>");
		assertThat(output).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	/**
	 * Browsers declare XML but not JSON in their accept header, we should still respond using JSON if that's the default
	 */
	@Test
	public void testHighlightProducesDefaultJsonWithBrowserRequest() throws Exception {
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
	public void testHighlightProducesDefaultJsonWithBrowserRequest2() throws Exception {
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
	public void testPrettyPrintDefaultsToTrue() throws Exception {
		ourServer.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitFalse() throws Exception {
		ourServer.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_pretty=false");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).doesNotContainPattern("(?s)<body>.*<pre>.*\n.*</pre>");
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitTrue() throws Exception {
		ourServer.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_pretty=true");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).containsSubsequence("<body>", "<pre>", "<div", "</pre>");
	}

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithWildcardRetVal&_summary=count");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).doesNotContain("entry");
	}

	@Test
	public void testShowNeither() throws Exception {
		ourInterceptor.setShowRequestHeaders(false);
		ourInterceptor.setShowResponseHeaders(false);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContainIgnoringCase("Accept");
		assertThat(responseContent).doesNotContainIgnoringCase("Content-Type");
	}

	@Test
	public void testShowRequest() throws Exception {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(false);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).containsIgnoringCase("Accept");
		assertThat(responseContent).doesNotContainIgnoringCase("Content-Type");
	}

	@Test
	public void testShowRequestAndResponse() throws Exception {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(true);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).containsIgnoringCase("Accept");
		assertThat(responseContent).containsIgnoringCase("Content-Type");
	}

	@Test
	public void testShowResponse() throws Exception {
		ourInterceptor.setShowResponseHeaders(true);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent).doesNotContainIgnoringCase("Accept");
		assertThat(responseContent).containsIgnoringCase("Content-Type");
	}

	@Test
	public void testNarrative() throws IOException {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		String url = ourServer.getBaseUrl() + "/Patient/1/$patientOp?_format=html/json";
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpResponse response = ourClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			assertThat(resp).contains("<h1>Narrative</h1>");
			assertThat(resp).contains("<thead><tr><th>Header1</th><th>Header2</th></tr></thead>");
		}

	}


	@Test
	public void testNarrative_Disabled() throws IOException {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		ourInterceptor.setShowNarrative(false);

		String url = ourServer.getBaseUrl() + "/Patient/1/$patientOp?_format=html/json";
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpResponse response = ourClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			assertThat(resp).doesNotContain("<h1>Narrative</h1>");
			assertThat(resp).doesNotContain("<thead><tr><th>Header1</th><th>Header2</th></tr></thead>");
		}

	}

	@Test
	public void testNarrative_SketchyTagBlocked() throws IOException {
		Patient patient = new Patient();
		patient.addName().setFamily("Simpson");
		patient.getText().setDivAsString("<div><table onclick=\"foo();\"><thead><tr><th>Header1</th><th>Header2</th></tr></thead><tr><td>A cell</td><td>A cell</td></tr><tr><td>A cell 2</td><td>A cell 2</td></tr></table></div>");
		ourPatientProvider.myNextPatientOpResponse = patient;

		String url = ourServer.getBaseUrl() + "/Patient/1/$patientOp?_format=html/json";
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpResponse response = ourClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			assertThat(resp).contains("<table><thead><tr><th>Header1</th>");
		}

	}

	@Test
	public void testNullResponseResource() {
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

	public static class GraphQLProvider {
		@GraphQL
		public String processGraphQlRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQuery) {
			return "{\"foo\":\"bar\"}";
		}
	}

	public static class DummyBinaryResourceProvider implements IResourceProvider {

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

	public static class DummyPatientResourceProvider implements IResourceProvider {

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
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@Nonnull
	private static SystemRequestDetails newRequest() {
		SystemRequestDetails retVal = new SystemRequestDetails();
		retVal.setFhirContext(ourCtx);
		return retVal;
	}

}

