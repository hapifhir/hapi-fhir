package ca.uhn.fhir.rest.server.interceptor;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.cors.CorsConfiguration;

import com.phloc.commons.collections.iterate.ArrayEnumeration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome.Issue;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;

public class ResponseHighlightingInterceptorTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlightingInterceptorTest.class);
	private static int ourPort;

	private static Server ourServer;
	private static RestfulServer ourServlet;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrue() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader("Accept", "text/html");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (stringContainsInOrder("<body>", "<pre>", "\n", "</pre>")));
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitTrue() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=true");
		httpGet.addHeader("Accept", "text/html");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (stringContainsInOrder("<body>", "<pre>", "\n", "</pre>")));
	}

	/**
	 * See #464
	 */
	@Test
	public void testPrettyPrintDefaultsToTrueWithExplicitFalse() throws Exception {
		ourServlet.setDefaultPrettyPrint(false);
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_pretty=false");
		httpGet.addHeader("Accept", "text/html");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, not(stringContainsInOrder("<body>", "<pre>", "\n", "</pre>")));
	}

	@Test
	public void testForceResponseTime() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
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
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle"));

	}

	@Test
	public void testGetInvalidResourceNoAcceptHeader() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Foobar/123");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());

		assertThat(responseContent, not(stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "Unknown resource type 'Foobar' - Server knows how to handle")));
		assertThat(responseContent, (stringContainsInOrder("Unknown resource type 'Foobar'")));
		assertThat(status.getFirstHeader("Content-Type").getValue(), containsString("application/xml+fhir"));

	}

	@Test
	public void testGetRoot() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/");
		httpGet.addHeader("Accept", "text/html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, stringContainsInOrder("<span class='hlTagName'>OperationOutcome</span>", "This is the base URL of FHIR server. Unable to handle this request, as it does not contain a resource type or operation name."));

	}

	@Test
	public void testHighlightException() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// This can be null depending on the exception type
		// reqDetails.setParameters(null);
		
		ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
		exception.setOperationOutcome(new OperationOutcome().addIssue(new Issue().setDiagnostics("Hello")));

		assertFalse(ic.handleException(reqDetails, exception, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>OperationOutcome</span>"));
	}


	@Test
	public void testHighlightNormalResponseForcePrettyPrint() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		params.put(Constants.PARAM_PRETTY, new String[] { Constants.PARAM_PRETTY_VALUE_TRUE });
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
		assertThat(output, stringContainsInOrder("<body>", "<pre>", "\n", "</pre>"));
	}

	@Test
	public void testHighlightForceRaw() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		params.put(Constants.PARAM_PRETTY, new String[] { Constants.PARAM_PRETTY_VALUE_TRUE });
		params.put(Constants.PARAM_FORMAT, new String[] { Constants.CT_XML });
		params.put(ResponseHighlighterInterceptor.PARAM_RAW, new String[] { ResponseHighlighterInterceptor.PARAM_RAW_TRUE });
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// true means it decided to not handle the request..
		assertTrue(ic.outgoingResponse(reqDetails, resource, req, resp));

	}
	
	@Test
	public void testDontHighlightWhenOriginHeaderPresent() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});
		when(req.getHeader(Constants.HEADER_ORIGIN)).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock theInvocation) throws Throwable {
				return "http://example.com";
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// true means it decided to not handle the request..
		assertTrue(ic.outgoingResponse(reqDetails, resource, req, resp));

	}

	/**
	 * See #346
	 */
	@Test
	public void testHighlightForceHtmlCt() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("application/xml+fhir");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		params.put(Constants.PARAM_FORMAT, new String[] { Constants.FORMAT_HTML });
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// false means it decided to handle the request..
		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));
	}

	/**
	 * See #346
	 */
	@Test
	public void testHighlightForceHtmlFormat() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("application/xml+fhir");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		HashMap<String, String[]> params = new HashMap<String, String[]>();
		params.put(Constants.PARAM_FORMAT, new String[] { Constants.CT_HTML });
		reqDetails.setParameters(params);
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		// false means it decided to handle the request..
		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));
	}

	@Test
	public void testHighlightNormalResponse() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		reqDetails.setServer(new RestfulServer(ourCtx));
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("<span class='hlTagName'>Patient</span>"));
		assertThat(output, stringContainsInOrder("<body>", "<pre>", "\n", "</pre>"));
		assertThat(output, containsString("<a href=\"?_format=json\">"));
	}

	/**
	 * Browsers declare XML but not JSON in their accept header, we should still respond using JSON if that's the default
	 */
	@Test
	public void testHighlightProducesDefaultJsonWithBrowserRequest() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);

		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.JSON);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		assertFalse(ic.outgoingResponse(reqDetails, resource, req, resp));

		String output = sw.getBuffer().toString();
		ourLog.info(output);
		assertThat(output, containsString("resourceType"));
	}

	
	
	
	@Test
	public void testHighlightProducesDefaultJsonWithBrowserRequest2() throws Exception {
		ResponseHighlighterInterceptor ic = new ResponseHighlighterInterceptor();

		HttpServletRequest req = mock(HttpServletRequest.class);

		when(req.getHeaders(Constants.HEADER_ACCEPT)).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock theInvocation) throws Throwable {
				return new ArrayEnumeration<String>("text/html;q=0.8,application/xhtml+xml,application/xml;q=0.9");
			}
		});

		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter sw = new StringWriter();
		when(resp.getWriter()).thenReturn(new PrintWriter(sw));

		Patient resource = new Patient();
		resource.addName().addFamily("FAMILY");

		ServletRequestDetails reqDetails = new ServletRequestDetails();
		reqDetails.setRequestType(RequestTypeEnum.GET);
		reqDetails.setParameters(new HashMap<String, String[]>());
		RestfulServer server = new RestfulServer(ourCtx);
		server.setDefaultResponseEncoding(EncodingEnum.JSON);
		reqDetails.setServer(server);
		reqDetails.setServletRequest(req);

		// True here means the interceptor didn't handle the request, because HTML wasn't the top ranked accept header
		assertTrue(ic.outgoingResponse(reqDetails, resource, req, resp));
	}

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithWildcardRetVal&_summary=count");
		httpGet.addHeader("Accept", "html");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Resp: {}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, not(containsString("entry")));
	}

	@Test
	public void testBinaryReadAcceptMissing() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");

		HttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, responseContent);

	}

	@Test
	public void testBinaryReadAcceptBrowser() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		
		HttpResponse status = ourClient.execute(httpGet);
		byte[] responseContent = IOUtils.toByteArray(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("foo", status.getFirstHeader("content-type").getValue());
		assertEquals("Attachment;", status.getFirstHeader("Content-Disposition").getValue());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, responseContent);
	}
	
	@Test
	public void testBinaryReadAcceptFhirJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Binary/foo");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		httpGet.addHeader("Accept", Constants.CT_FHIR_JSON);
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertNull(status.getFirstHeader("Content-Disposition"));
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"1\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", responseContent);

	}
	@Test
	public void testForceApplicationJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}
	@Test
	public void testForceApplicationJsonFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/json+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}
	@Test
	public void testForceApplicationJsonPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=" + UrlUtil.escape("application/json+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@Test
	public void testForceJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}


	
	@Test
	public void testForceHtmlJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=html/json");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
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
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("text/html;charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, containsString("html"));
		assertThat(responseContent, not(containsString(">{<")));
		assertThat(responseContent, containsString("&lt;"));
	}
	
	@Test
	public void testForceApplicationXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/xml");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}
	@Test
	public void testForceApplicationXmlFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=application/xml+fhir");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}
	@Test
	public void testForceApplicationXmlPlusFhir() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_format=" + UrlUtil.escape("application/xml+fhir"));
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
		
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", status.getFirstHeader("content-type").getValue().replace(" ", "").toLowerCase());
		assertThat(responseContent, not(containsString("html")));
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

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
		config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		ourServlet.registerInterceptor(corsInterceptor);
		
		ourServlet.registerInterceptor(new ResponseHighlighterInterceptor());
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
	
	public static class DummyBinaryResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

		@Read
		public Binary read(@IdParam IdDt theId) {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType(theId.getIdPart());
			return retVal;
		}

		@Search
		public List<Binary> search() {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType("text/plain");
			return Collections.singletonList(retVal);
		}

	}


	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.getId().setValue("1");
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

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new IdentifierDt());
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanNameDt());
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getId().setValue("2");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdDt theId) {
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
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
		public List<? extends IResource> searchWithWildcardRetVal() {
			Patient p = new Patient();
			p.setId("1234");
			p.addName().addFamily("searchWithWildcardRetVal");
			return Collections.singletonList(p);
		}

	}

}
