package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.helger.collection.iterate.ArrayEnumeration;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseHighlightingInterceptorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlightingInterceptorTest.class);
	private static ResponseHighlighterInterceptor ourInterceptor = new ResponseHighlighterInterceptor();
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourInterceptor.setShowRequestHeaders(new ResponseHighlighterInterceptor().isShowRequestHeaders());
		ourInterceptor.setShowResponseHeaders(new ResponseHighlighterInterceptor().isShowResponseHeaders());
	}

	/**
	 * Return a Binary response type - Client accepts text/html but is not a browser
	 */
	@Test
	public void testBinaryOperationHtmlResponseFromProvider() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/html/$binaryOp");
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
	public void testBinaryReadAcceptBrowser() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertArrayEquals(new byte[]{1, 2, 3, 4}, responseContent);
	}

	/**
	 * Return a Binary response type - Client accepts text/html but is not a browser
	 */
	@Test
	public void testBinaryReadHtmlResponseFromProvider() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/html");
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
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");
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
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertArrayEquals(new byte[]{1, 2, 3, 4}, responseContent);

	}

	@Test
	public void testDontHighlightWhenOriginHeaderPresent() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		assertTrue(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

	}

	@Test
	public void testForceApplicationJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceApplicationJsonFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/json+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceApplicationJsonPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/json+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceApplicationXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/xml");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceApplicationXmlFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/xml+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceApplicationXmlPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("application/xml+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceHtmlJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, containsString("html"));
		assertThat(responseContent, containsString(">{<"));
		assertThat(responseContent, not(containsString("&lt;")));

		ourLog.info(responseContent);
	}

	@Test
	public void testForceHtmlJsonWithAdditionalParts() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=" + UrlUtil.escapeUrlParam("html/json; fhirVersion=1.0"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, containsString("html"));
		assertThat(responseContent, containsString(">{<"));
		assertThat(responseContent, not(containsString("&lt;")));

		ourLog.info(responseContent);
	}

	@Test
	public void testForceHtmlXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/xml");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, containsString("html"));
		assertThat(responseContent, not(containsString(">{<")));
		assertThat(responseContent, containsString("&lt;"));
	}

	@Test
	public void testForceJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceResponseTime() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent.replace('\n', ' ').replace('\r', ' '), matchesPattern(".*Response generated in [0-9]+ms.*"));

	}

	@Test
	public void testGetInvalidResource() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Foobar/123");
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle"));

	}

	@Test
	public void testGetInvalidResourceNoAcceptHeader() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Foobar/123");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent, not(stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle")));
		assertThat(responseContent, (stringContainsInOrder("Unknown resource type 'Foobar'")));
		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());

	}

	@Test
	public void testGetRoot() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/");
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "This is the base URL of FHIR server. Unable to handle this request, as it does not contain a resource type or operation name."));

	}

	@Test
	public void testHighlightException() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(theInvocation -> new ArrayEnumeration<>("text/html,application/xhtml+xml,application/xml;q=0.9"));

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().setFamily("FAMILY");

		ServletRequestDetails reqDetails = new TestServletRequestDetails(mock(IInterceptorBroadcaster.class));
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// This can be null depending on the exception type
		// reqDetails.setParameters(null);

		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new OperationOutcome.OperationOutcomeIssueComponent().setDiagnostics("Hello")));

		assertFalse(ic.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>OperationOutcome</span>"));
	}

	/**
	 * See #346
	 */
	@Test
	public void testHighlightForceHtmlCt() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		assertFalse(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	/**
	 * See #346
	 */
	@Test
	public void testHighlightForceHtmlFormat() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		assertFalse(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	@Test
	public void testHighlightForceRaw() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		assertTrue(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

	}

	@Test
	public void testHighlightNormalResponse() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
		assertThat(output, stringContainsInOrder("<body>", "<pre>", "<div", "</pre>"));
		assertThat(output, containsString("<a href=\"?_format=json\">"));
	}

	@Test
	public void testHighlightNormalResponseForcePrettyPrint() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
		assertThat(output, stringContainsInOrder("<body>", "<pre>", "<div", "</pre>"));
	}

	/**
	 * Browsers declare XML but not JSON in their accept header, we should still respond using JSON if that's the default
	 */
	@Test
	public void testHighlightProducesDefaultJsonWithBrowserRequest() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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

		assertFalse(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("resourceType"));
	}

	@Test
	public void testHighlightProducesDefaultJsonWithBrowserRequest2() throws Exception {
		ResponseHighlighterInterceptor ic = ourInterceptor;

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
		assertTrue(ic.outgoingResponse(reqDetails, new ResponseDetails(resource), req, resp));
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrue() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (stringContainsInOrder("<body>", "<pre>", "<div", "</pre>")));
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitFalse() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=false");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, not(stringContainsInOrder("<body>", "<pre>", "\n", "</pre>")));
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitTrue() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=true");
		httpGet.addHeader("Accept", "text/html");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (stringContainsInOrder("<body>", "<pre>", "<div", "</pre>")));
	}

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithWildcardRetVal&_summary=count");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, not(containsString("entry")));
	}

	@Test
	public void testShowNeither() throws Exception {
		ourInterceptor.setShowRequestHeaders(false);
		ourInterceptor.setShowResponseHeaders(false);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsStringIgnoringCase("Accept")));
		assertThat(responseContent, not(containsStringIgnoringCase("Content-Type")));
	}

	@Test
	public void testShowRequest() throws Exception {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(false);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, (containsStringIgnoringCase("Accept")));
		assertThat(responseContent, not(containsStringIgnoringCase("Content-Type")));
	}

	@Test
	public void testShowRequestAndResponse() throws Exception {
		ourInterceptor.setShowRequestHeaders(true);
		ourInterceptor.setShowResponseHeaders(true);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, (containsStringIgnoringCase("Accept")));
		assertThat(responseContent, (containsStringIgnoringCase("Content-Type")));
	}

	@Test
	public void testShowResponse() throws Exception {
		ourInterceptor.setShowResponseHeaders(true);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		status.close();
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsStringIgnoringCase("Accept")));
		assertThat(responseContent, (containsStringIgnoringCase("Content-Type")));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourLog.info("Using port: {}", ourPort);
		Server ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);

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
		ourServlet.registerInterceptor(corsInterceptor);

		ourServlet.registerInterceptor(ourInterceptor);
		ourServlet.setResourceProviders(patientProvider, new DummyBinaryResourceProvider());
		ourServlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");

		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

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
			}else {
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

		@Operation(name="binaryOp", idempotent = true)
		public Binary binaryOp(@IdParam IdType theId) {
			Binary retVal = new Binary();
			retVal.setId(theId);
			if (theId.getIdPart().equals("html")) {
				retVal.setContent("<html>DATA</html>".getBytes(Charsets.UTF_8));
				retVal.setContentType("text/html");
			}else {
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

	}

}
