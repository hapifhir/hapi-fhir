package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchCountParamDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCountParamDstu3Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static String ourLastMethod;
	private static Integer ourLastParam;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastParam = null;
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_count=2");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);
			assertEquals(Integer.valueOf(2), ourLastParam);

			assertThat(responseContent).contains(
				 "<link>",
				 "<relation value=\"self\"/>",
				 "<url value=\"" + ourServer.getBaseUrl() + "/Patient?_count=2\"/>",
				 "</link>",
				 "<link>",
				 "<relation value=\"next\"/>",
				 "<url value=\"" + ourServer.getBaseUrl() + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>",
				 "</link>");

		}

	}


	@Test
	public void testSearchCount0() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_count=0&_pretty=true");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);
			assertEquals(Integer.valueOf(0), ourLastParam);

			assertThat(responseContent).contains(
				 "<Bundle",
				 "<total value=\"99\"/>",
				 "</Bundle>");
			assertThat(responseContent).doesNotContain("entry");

		}

	}

	/**
	 * See #372
	 */
	@Test
	public void testSearchWithNoCountParam() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithNoCountParam&_count=2");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchWithNoCountParam", ourLastMethod);
			assertNull(ourLastParam);

			assertThat(responseContent).contains(
				 "<link>",
				 "<relation value=\"self\"/>",
				 "<url value=\"" + ourServer.getBaseUrl() + "/Patient?_count=2&amp;_query=searchWithNoCountParam\"/>",
				 "</link>",
				 "<link>",
				 "<relation value=\"next\"/>",
				 "<url value=\"" + ourServer.getBaseUrl() + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>",
				 "</link>");

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

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
			@OptionalParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier,
			@Count() Integer theParam
		) {
			ourLastMethod = "search";
			ourLastParam = theParam;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search(queryName = "searchWithNoCountParam")
		public List searchWithNoCountParam() {
			ourLastMethod = "searchWithNoCountParam";
			ourLastParam = null;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
