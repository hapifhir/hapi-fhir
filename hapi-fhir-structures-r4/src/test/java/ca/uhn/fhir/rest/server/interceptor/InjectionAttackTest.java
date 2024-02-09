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
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InjectionAttackTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InjectionAttackTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .registerInterceptor(new ResponseHighlighterInterceptor())
		 .setDefaultResponseEncoding(EncodingEnum.JSON);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testPreventHtmlInjectionViaInvalidContentType() throws Exception {
		String requestUrl = ourServer.getBaseUrl() + "/Patient/123";

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "application/<script>");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(responseContent).doesNotContain("<script>");
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidParameterName() throws Exception {
		String requestUrl = ourServer.getBaseUrl() +
			"/Patient?a" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo("text/html");
		}

		// JSON HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo("text/html");
		}

		// XML HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo(Constants.CT_FHIR_XML_NEW);
		}

		// JSON Plain
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidResourceType() throws Exception {
		String requestUrl = ourServer.getBaseUrl() +
			"/AA" +
			UrlUtil.escapeUrlParam("<script>");

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(404);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo("text/html");
		}

		// JSON HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(404);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo("text/html");
		}

		// XML HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(404);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo(Constants.CT_FHIR_XML_NEW);
		}

		// JSON Plain
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(404);
			assertThat(responseContent).doesNotContain("<script>");
			assertThat(status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim()).isEqualTo(Constants.CT_FHIR_JSON_NEW);
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidTokenParamModifier() throws Exception {
		String requestUrl = ourServer.getBaseUrl() +
			"/Patient?identifier:" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "application/<script>");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(responseContent).doesNotContain("<script>");
		}

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
