package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
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

public class SearchSortR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchSortR4Test.class);
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.JSON)
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();
	
	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastSortSpec = null;
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_sort=param1,-param2,param3,-param4");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);

			assertEquals("param1", ourLastSortSpec.getParamName());
			assertEquals(SortOrderEnum.ASC, ourLastSortSpec.getOrder());

			assertEquals("param2", ourLastSortSpec.getChain().getParamName());
			assertEquals(SortOrderEnum.DESC, ourLastSortSpec.getChain().getOrder());

			assertEquals("param3", ourLastSortSpec.getChain().getChain().getParamName());
			assertEquals(SortOrderEnum.ASC, ourLastSortSpec.getChain().getChain().getOrder());

			assertEquals("param4", ourLastSortSpec.getChain().getChain().getChain().getParamName());
			assertEquals(SortOrderEnum.DESC, ourLastSortSpec.getChain().getChain().getChain().getOrder());

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

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@Sort SortSpec theSortSpec
				) {
			ourLastMethod = "search";
			ourLastSortSpec = theSortSpec;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

	}

}
