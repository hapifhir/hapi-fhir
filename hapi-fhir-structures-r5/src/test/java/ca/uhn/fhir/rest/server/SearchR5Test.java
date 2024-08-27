package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchR5Test.class);
	private static final FhirContext ourCtx = FhirContext.forR5Cached();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;

	@RegisterExtension
	public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}


	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);

			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		}

	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			@RequiredParam(name = "identifier") TokenAndListParam theIdentifiers) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.getIdElement().setValue("Patient/" + i + "/_history/222");
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE);
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.setActive(true);
				retVal.add(patient);
			}
			return retVal;
		}

	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
