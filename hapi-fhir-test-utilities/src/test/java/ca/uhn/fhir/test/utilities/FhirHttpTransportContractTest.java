package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that every {@link IHttpTestTransport} implementation is observably interchangeable. The
 * whole point of the transport SPI is that {@link HttpTestRequest} behaves identically no matter
 * which HTTP client library is underneath, so each case runs against all transports rather than
 * testing any one of them in isolation.
 * <p>
 * A new transport should be added to {@link #transport(String)} and to each {@code @ValueSource},
 * and nothing else.
 * </p>
 */
// Created by claude-sonnet-5
class FhirHttpTransportContractTest {

	private static final byte[] PNG_MAGIC = new byte[] {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};

	private static final String APACHE_4 = "ApacheHttp4";
	private static final String APACHE_5 = "ApacheHttp5";

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	/**
	 * {@code createDefault()}'s pool trusts a pooled connection is still alive without checking,
	 * which races against the embedded server closing it — HttpClient5 tests reused a dead
	 * connection often enough to see intermittent {@code NoHttpResponseException} here. Validating
	 * on every reuse costs a non-blocking poll per request and closes that race.
	 */
	private static final CloseableHttpClient ourHttp5Client = HttpClients.custom()
			.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
					.setValidateAfterInactivity(TimeValue.ZERO_MILLISECONDS)
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

		assertThat(response.getBodyBytes()).containsExactly(PNG_MAGIC);
	}

	@ParameterizedTest
	@ValueSource(strings = {APACHE_4, APACHE_5})
	void contentType_responseHasCharsetParameter_stripsIt(String theTransport) {
		HttpTestResponse response = request(theTransport, "/foo").get();

		assertThat(response.getHeader("Content-Type")).startsWith("text/plain");
		assertThat(response.contentType()).isEqualTo("text/plain");
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
	void followRedirects_serverRedirects_followsToTheDestination(String theTransport) {
		HttpTestResponse response =
				request(theTransport, "/foo?redirect=true").followRedirects(true).get();

		assertThat(response.getStatusCode()).isEqualTo(200);
		assertThat(response.getBody()).contains("method=GET");
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

	private static class EchoServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			if (theRequest.getParameter("redirect") != null) {
				theResponse.setStatus(302);
				theResponse.addHeader("Location", theRequest.getRequestURL().toString());
				return;
			}

			String statusParameter = theRequest.getParameter("status");
			int status = statusParameter != null ? Integer.parseInt(statusParameter) : 200;
			theResponse.setStatus(status);
			theResponse.addHeader("X-Echo-Header", "echo-value");
			if (status == 204) {
				return;
			}

			if (theRequest.getParameter("binary") != null) {
				theResponse.setContentType("image/png");
				theResponse.getOutputStream().write(PNG_MAGIC);
				return;
			}

			String requestBody = IOUtils.toString(theRequest.getInputStream(), StandardCharsets.UTF_8);
			theResponse.setContentType("text/plain");
			theResponse
					.getWriter()
					.write("method=" + theRequest.getMethod() + "\nauthorization="
							+ theRequest.getHeader(Constants.HEADER_AUTHORIZATION) + "\ncontentType="
							+ stripCharset(theRequest.getContentType()) + "\ncustom="
							+ theRequest.getHeader("X-Custom") + "\nbody=" + requestBody);
		}

		private String stripCharset(String theContentType) {
			return theContentType == null ? null : theContentType.replaceAll(";.*", "").trim();
		}
	}
}
