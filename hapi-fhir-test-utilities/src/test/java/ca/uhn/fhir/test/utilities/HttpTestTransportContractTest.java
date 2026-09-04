package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Verifies that every {@link IHttpTestTransport} implementation is observably interchangeable. The
 * whole point of the transport SPI is that {@link HttpTestRequest} behaves identically no matter
 * which HTTP client library is underneath, so each case runs against all transports rather than
 * testing any one of them in isolation.
 * <p>
 * A new transport should be added to {@link #transport(String)} and to each {@code @ValueSource}.
 * The only cases outside that pattern are the ones needing a specifically configured client, which
 * is inherently client-library-specific; those are named per transport instead.
 * </p>
 */
// Created by claude-sonnet-5
class HttpTestTransportContractTest {

	private static final String APACHE_4 = "ApacheHttp4";
	private static final String APACHE_5 = "ApacheHttp5";

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	/**
	 * The Apache HttpClient 5 client every {@link #APACHE_5} case runs on.
	 * <p>
	 * By default, the pool only re-checks a connection once it has been idle for two seconds, so a
	 * connection the embedded server has already closed can still be leased out, failing the
	 * request with {@code NoHttpResponseException}. These tests reuse connections well inside that
	 * window, so the pool is told to re-check on every lease instead.
	 * </p>
	 * <p>
	 * Zero is the value that re-checks every time: it sets the required idle time to nothing, and
	 * only a negative value turns the check off. HttpClient 4.x, which the {@link #APACHE_4} cases
	 * use, is the reverse — there zero disabled validation and only a positive value enabled it.
	 * </p>
	 */
	private static final CloseableHttpClient ourHttp5Client = HttpClients.custom()
			.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
					.setDefaultConnectionConfig(ConnectionConfig.custom()
							.setValidateAfterInactivity(TimeValue.ZERO_MILLISECONDS)
							.build())
					.build())
			.build();

	@AfterAll
	static void closeHttp5Client() throws IOException {
		ourHttp5Client.close();
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void get_sendsGetMethod(String theTransport) {
		String body = request(theTransport, "/foo").get().getBody();

		assertThat(body).contains("method=GET");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withBasicAuth_sendsBase64EncodedAuthorizationHeader(String theTransport) {
		String body = request(theTransport, "/foo").withBasicAuth("myuser", "mypass").get().getBody();

		// "myuser:mypass" base64-encoded
		assertThat(body).contains("authorization=Basic bXl1c2VyOm15cGFzcw==");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withHeader_sendsGivenHeader(String theTransport) {
		String body = request(theTransport, "/foo").withHeader("X-Custom", "custom-value").get().getBody();

		assertThat(body).contains("custom=custom-value");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void post_withStringBody_sendsBodyAndContentType(String theTransport) {
		String body = request(theTransport, "/foo").post("hello", "text/plain").getBody();

		assertThat(body).contains("method=POST").contains("body=hello").contains("contentType=text/plain");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void post_withByteArrayBody_sendsBytesUnaltered(String theTransport) {
		byte[] bytes = "bytes-payload".getBytes(StandardCharsets.UTF_8);

		String body = request(theTransport, "/foo").post(bytes, "application/octet-stream").getBody();

		assertThat(body)
				.contains("method=POST")
				.contains("body=bytes-payload")
				.contains("contentType=application/octet-stream");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void postForm_withFormParams_sendsThemFormEncoded(String theTransport) {
		String body = request(theTransport, "/foo")
				.withFormParam("grant_type", "authorization_code")
				.withFormParam("client_id", "my-client")
				.postForm()
				.getBody();

		// The echo renders parameters sorted by name, so client_id precedes grant_type.
		assertThat(body)
				.contains("method=POST")
				.contains("contentType=" + Constants.CT_X_FORM_URLENCODED)
				.contains("params=client_id=my-client&grant_type=authorization_code");
	}

	/**
	 * Asserts on what the server decoded rather than on the bytes sent, because the escaping is
	 * only correct if it round-trips — a test pinning the percent-encoded form would pass just as
	 * happily on an encoding no server agrees with.
	 */
	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void postForm_valueNeedsEscaping_serverReadsBackTheOriginalValue(String theTransport) {
		String redirectUri = "https://client.example.org/cb?a=b&c=d e";

		String body = request(theTransport, "/foo")
				.withFormParam("redirect_uri", redirectUri)
				.postForm()
				.getBody();

		assertThat(body).contains("params=redirect_uri=" + redirectUri);
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withFormParam_sameNameTwice_sendsBothValues(String theTransport) {
		String body = request(theTransport, "/foo")
				.withFormParam("scope", "openid")
				.withFormParam("scope", "patient/*.read")
				.postForm()
				.getBody();

		// The echo joins a multi-valued parameter with a comma.
		assertThat(body).contains("params=scope=openid,patient/*.read");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withFormParam_nullValue_sendsTheNameWithNoValue(String theTransport) {
		String body =
				request(theTransport, "/foo").withFormParam("client_id", null).postForm().getBody();

		assertThat(body).contains("params=client_id=");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void patch_sendsPatchMethodAndJsonPatchContentType(String theTransport) {
		String body = request(theTransport, "/foo").patch("[]").getBody();

		assertThat(body).contains("method=PATCH").contains("contentType=" + Constants.CT_JSON_PATCH);
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void delete_sendsDeleteMethod(String theTransport) {
		String body = request(theTransport, "/foo").delete().getBody();

		assertThat(body).contains("method=DELETE");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void options_sendsOptionsMethod(String theTransport) {
		String body = request(theTransport, "/foo").options().getBody();

		assertThat(body).contains("method=OPTIONS");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void method_arbitraryVerb_sendsThatVerb(String theTransport) {
		String body = request(theTransport, "/foo").method("TRACE").getBody();

		assertThat(body).contains("method=TRACE");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void execute_errorStatus_capturesStatusCodeAndReasonPhrase(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo?status=403").get();

		assertThat(response.getStatusCode()).isEqualTo(403);
		assertThat(response.getReasonPhrase()).isEqualTo("Forbidden");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void execute_responseHasHeaders_capturesThemOnResponse(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").get();

		assertThat(response.getHeader("X-Echo-Header")).isEqualTo("echo-value");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void execute_responseHasNoEntity_bodyIsEmptyString(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo?status=204").get();

		assertThat(response.getStatusCode()).isEqualTo(204);
		assertThat(response.getBody()).isEmpty();
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void getBodyBytes_binaryResponse_returnsBytesUnaltered(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo?binary=true").get();

		assertThat(response.getBodyBytes()).containsExactly(EchoServlet.PNG_MAGIC);
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void getContentType_responseHasCharsetParameter_stripsIt(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").get();

		assertThat(response.getContentType()).startsWith("text/plain");
		assertThat(response.getContentType()).isEqualTo("text/plain");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withoutRedirects_serverRedirects_returnsTheRedirectItself(String theTransport) {
		HttpTestResponse response =
				request(theTransport, "/foo?redirect=true").withoutRedirects().get();

		assertThat(response.getStatusCode()).isEqualTo(302);
		assertThat(response.getHeader("Location")).isNotNull();
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void withoutRedirects_notCalled_clientDefaultApplies(String theTransport) {
		// Both transports here run on clients that follow redirects, so saying nothing follows.
		HttpTestResponse response = request(theTransport, "/foo?redirect=true").get();

		assertThat(response.getStatusCode()).isEqualTo(200);
		assertThat(response.getBody()).contains("method=GET");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void head_sendsHeadMethod(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").head();

		assertThat(response.getStatusCode()).isEqualTo(200);
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void put_withStringBody_sendsBodyAndContentType(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").put("hello", "text/plain");

		assertThat(response.getBody()).contains("method=PUT").contains("body=hello");
		assertThat(response.getBody()).contains("contentType=text/plain");
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void put_withByteArrayBody_sendsBytesUnaltered(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").put(EchoServlet.PNG_MAGIC, "image/png");

		assertThat(response.getBody()).contains("method=PUT").contains("contentType=image/png");
	}

	/**
	 * The two guards below need no transport — they fail before anything is sent — so they are
	 * plain cases rather than running once per transport.
	 */
	@Test
	void postForm_noFormParams_failsRatherThanSendingAnEmptyBody() {
		HttpTestRequest request = HttpTestRequest.to(mock(IHttpTestTransport.class), ourServer.getBaseUrl() + "/foo");

		assertThatThrownBy(request::postForm)
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("withFormParam");
	}

	@Test
	void get_afterWithFormParam_failsRatherThanDroppingTheParams() {
		HttpTestRequest request = HttpTestRequest.to(mock(IHttpTestTransport.class), ourServer.getBaseUrl() + "/foo")
				.withFormParam("client_id", "my-client");

		assertThatThrownBy(request::get)
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("postForm");
	}

	@Test
	void to_apacheHttp5ClientOverload_resolvesTheHttp5Transport() {
		HttpTestResponse response =
				HttpTestRequest.to(ourHttp5Client, ourServer.getBaseUrl() + "/foo").get();

		assertThat(response.getStatusCode()).isEqualTo(200);
		assertThat(response.getBody()).contains("method=GET");
	}

	@Test
	void to_apacheHttp5ClientWithFhirContextOverload_resolvesTheHttp5Transport() {
		// A mock context suffices — this module has no FHIR structures JAR, and the request sends
		// no resource body, so the context is only carried through.
		HttpTestResponse response = HttpTestRequest.to(
						ourHttp5Client, mock(FhirContext.class), ourServer.getBaseUrl() + "/foo")
				.get();

		assertThat(response.getStatusCode()).isEqualTo(200);
	}

	/**
	 * Apache does not merge a request-level {@code RequestConfig} with the client's default — it
	 * replaces it — so setting one in order to suppress redirects must copy the client's default
	 * first, or every other setting on it is silently dropped.
	 * <p>
	 * Content compression is the lever because it is observable on the wire: with it disabled the
	 * client sends no {@literal Accept-Encoding} at all. A timeout would need a slow server to
	 * detect, and a cookie policy would need a cookie.
	 * </p>
	 */
	@Test
	void withoutRedirects_apacheHttp4ClientWithNonDefaultRequestConfig_preservesThatConfig() throws IOException {
		try (org.apache.http.impl.client.CloseableHttpClient client = http4ClientWithoutCompression()) {
			assertThat(HttpTestRequest.to(client, ourServer.getBaseUrl() + "/foo").get().getBody())
					.as("premise: this client sends no Accept-Encoding when no request config is set")
					.contains("acceptEncoding=null");

			assertThat(HttpTestRequest.to(client, ourServer.getBaseUrl() + "/foo")
							.withoutRedirects()
							.get()
							.getBody())
					.contains("acceptEncoding=null");
		}
	}

	/**
	 * @see #withoutRedirects_apacheHttp4ClientWithNonDefaultRequestConfig_preservesThatConfig()
	 */
	@Test
	void withoutRedirects_apacheHttp5ClientWithNonDefaultRequestConfig_preservesThatConfig() throws IOException {
		try (CloseableHttpClient client = http5ClientWithoutCompression()) {
			assertThat(HttpTestRequest.to(client, ourServer.getBaseUrl() + "/foo").get().getBody())
					.as("premise: this client sends no Accept-Encoding when no request config is set")
					.contains("acceptEncoding=null");

			assertThat(HttpTestRequest.to(client, ourServer.getBaseUrl() + "/foo")
							.withoutRedirects()
							.get()
							.getBody())
					.contains("acceptEncoding=null");
		}
	}

	/**
	 * Fully qualified because this class already imports the 5.x names of both types.
	 */
	private static org.apache.http.impl.client.CloseableHttpClient http4ClientWithoutCompression() {
		return org.apache.http.impl.client.HttpClients.custom()
				.setDefaultRequestConfig(org.apache.http.client.config.RequestConfig.custom()
						.setContentCompressionEnabled(false)
						.build())
				.build();
	}

	private static CloseableHttpClient http5ClientWithoutCompression() {
		return HttpClients.custom()
				.setDefaultRequestConfig(
						RequestConfig.custom().setContentCompressionEnabled(false).build())
				.build();
	}

	/**
	 * Resolved per-test rather than up front, so that the 4.x client — which the server extension
	 * only creates once it has started — is read after {@code beforeAll} has run.
	 */
	private IHttpTestTransport transport(String theTransport) {
		return switch (theTransport) {
			case APACHE_4 -> new ApacheHttp4TestTransport(ourServer.getHttpClient());
			case APACHE_5 -> new ApacheHttp5TestTransport(ourHttp5Client);
			default -> throw new IllegalArgumentException("Unknown transport: " + theTransport);
		};
	}

	private HttpTestRequest request(String theTransport, String thePath) {
		return HttpTestRequest.to(transport(theTransport), ourServer.getBaseUrl() + thePath);
	}
}
