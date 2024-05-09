package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultitenancyR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultitenancyR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private static String ourLastTenantId;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.JSON)
		 .withServer(s -> s.setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy()))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
		ourLastTenantId = null;
	}

	@Test
	public void testUrlBaseStrategy() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/TENANT2/Patient?identifier=foo%7Cbar");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);
			assertEquals("TENANT2", ourLastTenantId);
			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			Bundle resp = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

			assertEquals(ourServer.getBaseUrl() + "/TENANT2/Patient?identifier=foo%7Cbar", resp.getLink("self").getUrl());
			assertEquals(ourServer.getBaseUrl() + "/TENANT2/Patient/0", resp.getEntry().get(0).getFullUrl());
			assertEquals(ourServer.getBaseUrl() + "/TENANT2/Patient/0", resp.getEntry().get(0).getResource().getId());
			assertThat(resp.getLink("next").getUrl()).startsWith(ourServer.getBaseUrl() + "/TENANT2?_getpages=");

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		// GET the root
		httpGet = new HttpGet(ourServer.getBaseUrl() + "/");
		status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent).contains("\"diagnostics\":\"" + Msg.code(307) + "This is the base URL of a multitenant FHIR server. Unable to handle this request, as it does not contain a tenant ID.\"");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	private static class DummyPatientResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			 RequestDetails theRequestDetails,
			 @RequiredParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
			ourLastTenantId = theRequestDetails.getTenantId();
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.setActive(true);
				patient.getIdElement().setValue("Patient/" + i);
				retVal.add(patient);
			}
			return retVal;
		}

	}

}
