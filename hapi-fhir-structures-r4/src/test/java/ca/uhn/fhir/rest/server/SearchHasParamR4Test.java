package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchHasParamR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchHasParamR4Test.class);
	private static String ourLastMethod;
	private static HasAndListParam ourLastParam;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(10))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastParam = null;
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_has:Encounter:patient:type=SURG");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("search", ourLastMethod);
		
		HasParam param = ourLastParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0);
		assertEquals("Encounter", param.getTargetResourceType());
		assertEquals("patient", param.getReferenceFieldName());
		assertEquals("type", param.getParameterName());
		assertEquals("SURG", param.getParameterValue());
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@OptionalParam(name=Patient.SP_IDENTIFIER) TokenParam theIdentifier,
				@OptionalParam(name="_has") HasAndListParam theParam
				) {
			ourLastMethod = "search";
			ourLastParam = theParam;
			ArrayList<Patient> retVal = new ArrayList<>();
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("1"));
			return retVal;
		}
		//@formatter:on

	}

}
