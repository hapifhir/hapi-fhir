package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectionAttackTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InjectionAttackTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .registerInterceptor(new ResponseHighlighterInterceptor())
		 .setDefaultResponseEncoding(EncodingEnum.JSON);

	@Test
	public void testPreventHtmlInjectionViaInvalidContentType() throws Exception {
		String requestPath = "/Patient/123";

		// XML HTML
		String responseContent = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, "application/<script>").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidParameterName() throws Exception {
		String requestPath =
			"/Patient?a" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";

		// XML HTML
		HttpTestResponse response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW).get().assertStatus(400);
		String responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals("text/html", response.getContentType());

		// JSON HTML
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW).get().assertStatus(400);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals("text/html", response.getContentType());

		// XML HTML
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW).get().assertStatus(400);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getContentType());

		// JSON Plain
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW).get().assertStatus(400);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getContentType());
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidResourceType() throws Exception {
		String requestPath =
			"/AA" +
			UrlUtil.escapeUrlParam("<script>");

		// XML HTML
		HttpTestResponse response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW).get().assertStatus(404);
		String responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals("text/html", response.getContentType());

		// JSON HTML
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW).get().assertStatus(404);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals("text/html", response.getContentType());

		// XML HTML
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW).get().assertStatus(404);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals(Constants.CT_FHIR_XML_NEW, response.getContentType());

		// JSON Plain
		response = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW).get().assertStatus(404);
		responseContent = response.getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");
		assertEquals(Constants.CT_FHIR_JSON_NEW, response.getContentType());
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidTokenParamModifier() throws Exception {
		String requestPath =
			"/Patient?identifier:" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";
		String responseContent = ourServer.fhirRequest(requestPath).withHeader(Constants.HEADER_ACCEPT, "application/<script>").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("<script>");

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient patient = new Patient();
			patient.setId(theId);
			patient.setActive(true);
			return patient;
		}

		@Search
		public List<Patient> search(@OptionalParam(name = "identifier") TokenParam theToken) {
			return new ArrayList<>();
		}


	}

}
