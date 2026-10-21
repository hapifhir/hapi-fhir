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

public class InjectionAttackTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .registerInterceptor(new ResponseHighlighterInterceptor())
		 .setDefaultResponseEncoding(EncodingEnum.JSON);

	@Test
	public void testPreventHtmlInjectionViaInvalidContentType() {
		String requestPath = "/Patient/123";

		// XML HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, "application/<script>")
				.get()
				.assertStatus(200)
				.assertBodyDoesNotContain("<script>");
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidParameterName() {
		String requestPath =
			"/Patient?a" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";

		// XML HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW)
				.get()
				.assertStatus(400)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals("text/html");

		// JSON HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW)
				.get()
				.assertStatus(400)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals("text/html");

		// XML HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW)
				.get()
				.assertStatus(400)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW);

		// JSON Plain
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
				.get()
				.assertStatus(400)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidResourceType() {
		String requestPath =
			"/AA" +
			UrlUtil.escapeUrlParam("<script>");

		// XML HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW)
				.get()
				.assertStatus(404)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals("text/html");

		// JSON HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW)
				.get()
				.assertStatus(404)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals("text/html");

		// XML HTML
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW)
				.get()
				.assertStatus(404)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals(Constants.CT_FHIR_XML_NEW);

		// JSON Plain
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW)
				.get()
				.assertStatus(404)
				.assertBodyDoesNotContain("<script>")
				.assertContentTypeEquals(Constants.CT_FHIR_JSON_NEW);
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidTokenParamModifier() {
		String requestPath =
			"/Patient?identifier:" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";
		ourServer.fhirRequest(requestPath)
				.withHeader(Constants.HEADER_ACCEPT, "application/<script>")
				.get()
				.assertStatus(200)
				.assertBodyDoesNotContain("<script>");

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
