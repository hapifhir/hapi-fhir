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
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Covers the parts of {@link HttpTestRequest} that depend on a {@link FhirContext} or on
 * FHIR-specific headers. Behaviour that is purely about issuing a request — verbs, headers,
 * bodies, status and response parsing — lives in {@link FhirHttpTransportContractTest}, which
 * exercises it against every transport rather than just one.
 */
// Created by claude-sonnet-5
class HttpTestRequestTest {

	private static final FhirContext ourFhirContext = mock(FhirContext.class);

	@RegisterExtension
	private static final HttpServletExtension ourServer = new HttpServletExtension().withServlet(new EchoServlet());

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
		assertThatThrownBy(() -> HttpTestRequest.to(ourServer.getHttpClient(), ourServer.getBaseUrl() + "/Patient")
			.post(new Patient()))
			.isInstanceOf(NullPointerException.class)
			.hasMessageContaining("FhirContext");
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

	private HttpTestRequest request(String thePath) {
		return HttpTestRequest.to(ourServer.getHttpClient(), ourFhirContext, ourServer.getBaseUrl() + thePath);
	}

	@Test
	void post_contentTypeAlreadyHasCharset_isNotAppendedTwice() {
		HttpTestResponse response =
				HttpTestRequest.to(ourServer.getHttpClient(), ourServer.getBaseUrl() + "/foo")
						.post("hello", "text/plain; charset=ISO-8859-1");

		assertThat(response.getBody()).contains("rawContentType=text/plain; charset=ISO-8859-1");
	}

	@Test
	void post_contentTypeHasNoCharset_utf8IsAppended() {
		HttpTestResponse response =
				HttpTestRequest.to(ourServer.getHttpClient(), ourServer.getBaseUrl() + "/foo")
						.post("hello", "text/plain");

		assertThat(response.getBody()).contains("rawContentType=text/plain; charset=UTF-8");
	}

	private static class EchoServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
			theResponse.setStatus(200);

			String requestBody = IOUtils.toString(theRequest.getInputStream(), StandardCharsets.UTF_8);
			theResponse.setContentType("text/plain");
			theResponse
				.getWriter()
				.write("method=" + theRequest.getMethod() + "\ncontentType="
					+ stripCharset(theRequest.getContentType()) + "\nrawContentType="
					+ theRequest.getContentType() + "\nprefer="
					+ theRequest.getHeader(Constants.HEADER_PREFER) + "\nbody=" + requestBody);
		}

		private String stripCharset(String theContentType) {
			return theContentType == null ? null : theContentType.replaceAll(";.*", "").trim();
		}
	}
}
