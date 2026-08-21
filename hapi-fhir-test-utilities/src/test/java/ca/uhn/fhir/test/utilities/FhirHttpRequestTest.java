package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Created by claude-opus-5
class FhirHttpRequestTest {

	private static final FhirContext ourFhirContext = mock(FhirContext.class);

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

	@Test
	void withBasicAuth_sendsBase64EncodedAuthorizationHeader() {
		String body = request("/foo").withBasicAuth("myuser", "mypass").get().getBody();

		// "myuser:mypass" base64-encoded
		assertThat(body).contains("method=GET").contains("authorization=Basic bXl1c2VyOm15cGFzcw==");
	}

	@Test
	void withHeader_sendsGivenHeader() {
		String body = request("/foo").withHeader("X-Custom", "custom-value").get().getBody();

		assertThat(body).contains("custom=custom-value");
	}

	@Test
	void withLenient_sendsPreferHandlingLenientHeader() {
		String body = request("/foo").withLenient().get().getBody();

		assertThat(body).contains("prefer=handling=" + PreferHandlingEnum.LENIENT.getHeaderValue());
	}

	@Test
	void withPreferHandling_strict_sendsPreferHandlingStrictHeader() {
		String body = request("/foo")
				.withPreferHandling(PreferHandlingEnum.STRICT)
				.get()
				.getBody();

		assertThat(body).contains("prefer=handling=" + PreferHandlingEnum.STRICT.getHeaderValue());
	}

	@Test
	void post_withResourceBody_sendsFhirJson() {
		Patient patient = new Patient();
		patient.setActive(true);

		String encoded = "{\"resourceType\":\"Patient\"}";
		JsonParser jsonParser = mock(JsonParser.class);
		when(ourFhirContext.newJsonParser()).thenReturn(jsonParser);
		when(jsonParser.encodeResourceToString(patient)).thenReturn(encoded);
		String body = request("/Patient").post(patient).assertStatus(200).getBody();

		assertThat(body).contains("method=POST").contains(encoded)
			.contains("contentType=" + Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	void post_withResourceBodyAndNoFhirContext_throwsWithActionableMessage() {
		assertThatThrownBy(() -> FhirHttpRequest.to(ourServer.getHttpClient(), ourServer.getBaseUrl() + "/Patient")
						.post(new Patient()))
				.isInstanceOf(NullPointerException.class)
				.hasMessageContaining("FhirContext");
	}

	@Test
	void post_withStringBody_sendsGivenContentType() {
		String body = request("/foo").post("hello", "text/plain").getBody();

		assertThat(body).contains("method=POST").contains("body=hello").contains("contentType=text/plain");
	}

	@Test
	void put_withResourceBody_sendsFhirJson() {
		Patient patient = new Patient();

		String encoded = "{\"resourceType\":\"Patient\"}";
		JsonParser jsonParser = mock(JsonParser.class);
		when(ourFhirContext.newJsonParser()).thenReturn(jsonParser);
		when(jsonParser.encodeResourceToString(patient)).thenReturn(encoded);
		String body = request("/Patient/123").put(patient).getBody();

		assertThat(body).contains("method=PUT").contains("contentType=" + Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	void patch_withJsonPatchBody_sendsJsonPatchContentType() {
		String patchBody = "[ { \"op\": \"replace\", \"path\": \"/status\", \"value\": \"amended\" } ]";

		String body = request("/Observation/456").patch(patchBody).getBody();

		assertThat(body).contains("method=PATCH").contains("body=" + patchBody)
			.contains("contentType=" + Constants.CT_JSON_PATCH);
	}

	@Test
	void delete_sendsDeleteMethod() {
		String body = request("/Patient/123").delete().getBody();

		assertThat(body).contains("method=DELETE");
	}

	@Test
	void execute_customRequest_appliesAccumulatedHeaders() {
		String body = request("/foo")
				.withHeader("X-Custom", "custom-value")
				.execute(new HttpGet(ourServer.getBaseUrl() + "/foo"))
				.getBody();

		assertThat(body).contains("method=GET").contains("custom=custom-value");
	}

	@Test
	void execute_errorStatus_capturesStatusCodeAndReasonPhrase() {
		FhirHttpResponse response = request("/foo?status=403").get();

		assertThat(response.getStatusCode()).isEqualTo(403);
		assertThat(response.getReasonPhrase()).isEqualTo("Forbidden");
	}

	@Test
	void execute_responseHasHeaders_capturesThemOnResponse() {
		FhirHttpResponse response = request("/foo").get();

		assertThat(response.getHeader("X-Echo-Header")).isEqualTo("echo-value");
	}

	@Test
	void execute_responseHasNoEntity_bodyIsEmptyString() {
		FhirHttpResponse response = request("/foo?status=204").get();

		assertThat(response.getStatusCode()).isEqualTo(204);
		assertThat(response.getBody()).isEmpty();
	}

	private FhirHttpRequest request(String thePath) {
		return FhirHttpRequest.to(ourServer.getHttpClient(), ourFhirContext, ourServer.getBaseUrl() + thePath);
	}

	private static class EchoServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			String statusParameter = theRequest.getParameter("status");
			int status = statusParameter != null ? Integer.parseInt(statusParameter) : 200;
			theResponse.setStatus(status);
			theResponse.addHeader("X-Echo-Header", "echo-value");
			if (status == 204) {
				return;
			}

			String requestBody = IOUtils.toString(theRequest.getInputStream(), StandardCharsets.UTF_8);
			theResponse.setContentType("text/plain");
			theResponse
					.getWriter()
					.write("method=" + theRequest.getMethod() + "\nauthorization="
							+ theRequest.getHeader(Constants.HEADER_AUTHORIZATION) + "\ncontentType="
							+ stripCharset(theRequest.getContentType()) + "\nprefer="
							+ theRequest.getHeader(Constants.HEADER_PREFER) + "\ncustom="
							+ theRequest.getHeader("X-Custom") + "\nbody=" + requestBody);
		}

		private String stripCharset(String theContentType) {
			return theContentType == null ? null : theContentType.replaceAll(";.*", "").trim();
		}
	}
}
