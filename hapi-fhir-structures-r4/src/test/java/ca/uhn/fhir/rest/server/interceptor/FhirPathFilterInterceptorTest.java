package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.HttpClientRule;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderRule;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FhirPathFilterInterceptorTest {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPathFilterInterceptorTest.class);
	@ClassRule
	public static HttpClientRule ourClientRule = new HttpClientRule();
	private static FhirContext ourCtx = FhirContext.forR4();
	@ClassRule
	public static RestfulServerRule ourServerRule = new RestfulServerRule(ourCtx);
	@ClassRule
	public static HashMapResourceProviderRule<Patient> ourProviderRule = new HashMapResourceProviderRule<>(ourServerRule, Patient.class);
	private IGenericClient myClient;
	private String myBaseUrl;
	private CloseableHttpClient myHttpClient;
	private IIdType myPatientId;

	@Before
	public void before() {
		ourProviderRule.clear();
		ourServerRule.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		ourServerRule.getRestfulServer().getInterceptorService().registerInterceptor(new FhirPathFilterInterceptor());

		myClient = ourServerRule.getFhirClient();
		myBaseUrl = "http://localhost:" + ourServerRule.getPort();
		myHttpClient = ourClientRule.getClient();
	}

	@Test
	public void testUnfilteredResponse() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myPatientId.getValue());
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString("\"system\": \"http://identifiers/1\""));
			assertThat(responseText, containsString("\"given\": [ \"Homer\", \"Jay\" ]"));
		}
	}


	@Test
	public void testUnfilteredResponse_WithResponseHighlightingInterceptor() throws IOException {
		ourServerRule.getRestfulServer().registerInterceptor(new ResponseHighlighterInterceptor());
		createPatient();

		HttpGet request = new HttpGet(myPatientId.getValue() + "?_format=" + Constants.FORMATS_HTML_JSON);
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString("<span class='hlTagName'>&quot;system&quot;</span>: <span class='hlQuot'>&quot;http://identifiers/1&quot;"));
			assertThat(responseText, containsString("<span class='hlTagName'>&quot;given&quot;</span>: <span class='hlControl'>[</span> <span class='hlTagName'>&quot;Homer&quot;</span><span class='hlControl'>,</span> <span class='hlTagName'>&quot;Jay&quot;</span> ]</div>"));
		}
	}

	@Test
	public void testFilteredResponse() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myPatientId + "?_fhirpath=Patient.identifier&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString("\"system\": \"http://identifiers/1\""));
			assertThat(responseText, not(containsString("\"given\": [ \"Homer\", \"Jay\" ]")));
		}

	}

	@Test
	public void testFilteredResponse_ExpressionReturnsResource() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myPatientId + "?_fhirpath=Patient&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString("\"resource\": {"));
			assertThat(responseText, containsString("\"system\": \"http://identifiers/1\""));
			assertThat(responseText, containsString("\"given\": [ \"Homer\", \"Jay\" ]"));
		}

	}

	@Test
	public void testFilteredResponse_ExpressionIsInvalid() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myPatientId + "?_fhirpath=" + UrlUtil.escapeUrlParam("***"));
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertEquals(400, response.getStatusLine().getStatusCode());
			assertThat(responseText, containsString("Error parsing FHIRPath expression: Error performing *: left operand has more than one value"));
		}

	}

	@Test
	public void testFilteredResponseBundle() throws IOException {
		createPatient();

		HttpGet request = new HttpGet(myBaseUrl + "/Patient?_fhirpath=Bundle.entry.resource.as(Patient).name&_pretty=true");
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString(
				"      \"valueHumanName\": {\n" +
					"        \"family\": \"Simpson\",\n" +
					"        \"given\": [ \"Homer\", \"Jay\" ]\n" +
					"      }"
			));
		}

	}

	@Test
	public void testFilteredResponse_WithResponseHighlightingInterceptor() throws IOException {
		ourServerRule.getRestfulServer().registerInterceptor(new ResponseHighlighterInterceptor());
		createPatient();

		HttpGet request = new HttpGet(myPatientId + "?_fhirpath=Patient.identifier&_format=" + Constants.FORMATS_HTML_JSON);
		try (CloseableHttpResponse response = myHttpClient.execute(request)) {
			String responseText = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response:\n{}", responseText);
			assertThat(responseText, containsString("<span class='hlTagName'>&quot;system&quot;</span>: <span class='hlQuot'>&quot;http://identifiers/1&quot;"));
			assertThat(responseText, not(containsString("<span class='hlTagName'>&quot;given&quot;</span>: <span class='hlControl'>[</span> <span class='hlTagName'>&quot;Homer&quot;</span><span class='hlControl'>,</span> <span class='hlTagName'>&quot;Jay&quot;</span> ]</div>")));
		}

	}

	private void createPatient() {
		Patient p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("http://identifiers/1").setValue("value-1");
		p.addIdentifier().setSystem("http://identifiers/2").setValue("value-2");
		p.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		p.addName().setFamily("Simpson").addGiven("Grandpa");
		myPatientId = myClient.create().resource(p).execute().getId().withServerBase(myBaseUrl, "Patient");
	}

}
