package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchCountParamDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCountParamDstu2Test.class);
	private static String ourLastMethod;
	private static Integer ourLastParam;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastParam = null;
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_count=2");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);
			assertEquals(Integer.valueOf(2), ourLastParam);
			
			assertThat(responseContent, stringContainsInOrder(
				"<link>", 
				"<relation value=\"self\"/>", 
				"<url value=\"" + ourServer.getBaseUrl() + "/Patient?_count=2\"/>",
				"</link>", 
				"<link>",
				"<relation value=\"next\"/>", 
				"<url value=\"" + ourServer.getBaseUrl() + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>",
				"</link>"));

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
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
			assertEquals(null, ourLastParam);
			
			//@formatter:off
			assertThat(responseContent, stringContainsInOrder(
				"<link>", 
				"<relation value=\"self\"/>", 
				"<url value=\"" + ourServer.getBaseUrl() + "/Patient?_count=2&amp;_query=searchWithNoCountParam\"/>",
				"</link>", 
				"<link>",
				"<relation value=\"next\"/>", 
				"<url value=\"" + ourServer.getBaseUrl() + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>",
				"</link>"));
			//@formatter:on
			
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

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
				@Count() Integer theParam
				) {
			ourLastMethod = "search";
			ourLastParam = theParam;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanNameDt().addFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search(queryName="searchWithNoCountParam")
		public List searchWithNoCountParam() {
			ourLastMethod = "searchWithNoCountParam";
			ourLastParam = null;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanNameDt().addFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

	}

}
