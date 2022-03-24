package ca.uhn.fhir.rest.server;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApacheProxyAddressStrategyTest {

	@Test
	public void testWithoutForwarded() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://localhost/imagingstudy/fhir", serverBase);
	}

	@Test
	public void testWithForwardedHostWithoutForwardedProtoHttps() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();
		request.addHeader("X-Forwarded-Host", "my.example.host");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host/imagingstudy/fhir", serverBase);
	}

	@Test
	public void testWithForwardedHostWithoutForwardedProtoHttp() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				false);
		MockHttpServletRequest request = prepareRequest();
		request.addHeader("X-Forwarded-Host", "my.example.host");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("http://my.example.host/imagingstudy/fhir", serverBase);
	}

	@Test
	public void testWithForwarded() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();
		request.addHeader("X-Forwarded-Host", "my.example.host");
		request.addHeader("X-Forwarded-Proto", "https");
		request.addHeader("X-Forwarded-Prefix", "server-prefix/fhir");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host/server-prefix/fhir", serverBase);
	}

	@Test
	public void testWithForwardedWithHostPrefixWithSlash() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();
		request.addHeader("host", "localhost");

		request.addHeader("X-Forwarded-Host", "my.example.host");
		request.addHeader("X-Forwarded-Proto", "https");
		request.addHeader("X-Forwarded-Prefix", "/server-prefix/fhir");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host/server-prefix/fhir", serverBase);
	}

	@Test
	public void testWithForwardedWithoutPrefix() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();

		request.addHeader("X-Forwarded-Host", "my.example.host");
		request.addHeader("X-Forwarded-Proto", "https");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host/imagingstudy/fhir", serverBase);
	}

	@Test
	public void testWithForwardedHostAndPort() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();

		request.addHeader("X-Forwarded-Host", "my.example.host");
		request.addHeader("X-Forwarded-Port", "345");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host:345/imagingstudy/fhir",
				serverBase);
	}
	
	@Test
	public void testWithForwardedHostAndUnsetPort() {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();

		request.addHeader("X-Forwarded-Host", "my.example.host");
		request.addHeader("X-Forwarded-Port", "-1");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("https://my.example.host/imagingstudy/fhir",
				serverBase);
	}
	
	@Test
	public void testWithRfc7239 () {
		ApacheProxyAddressStrategy addressStrategy = new ApacheProxyAddressStrategy(
				true);
		MockHttpServletRequest request = prepareRequest();

		request.addHeader("Forwarded", "Forwarded: for=192.0.2.43,"
				+ " for=198.51.100.17;by=203.0.113.60;proto=http;host=example.com");
		String serverBase = addressStrategy.determineServerBase(null, request);
		assertEquals("http://example.com/imagingstudy/fhir",
				serverBase);
	}

	private MockHttpServletRequest prepareRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("POST");
		request.setScheme("https");
		request.setServerPort(443);
		request.setServletPath("/fhir");
		request.setServerName("localhost");
		request.setRequestURI("/imagingstudy/fhir/imagingstudy?_format=json");
		request.setContextPath("/imagingstudy");
		return request;
	}
}
