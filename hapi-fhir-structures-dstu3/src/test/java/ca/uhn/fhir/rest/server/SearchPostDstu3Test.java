package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchPostDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchPostDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;
	private static StringAndListParam ourLastName;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastSortSpec = null;
		ourLastName = null;
	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParams() throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?_format=application/xml");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);
			assertEquals(null, ourLastSortSpec);
			assertEquals(1, ourLastName.getValuesAsQueryTokens().size());
			assertEquals(1, ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().size());
			assertEquals("Smith", ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

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
				@Sort SortSpec theSortSpec,
				@OptionalParam(name=Patient.SP_NAME) StringAndListParam theName
				) {
			ourLastMethod = "search";
			ourLastSortSpec = theSortSpec;
			ourLastName = theName;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().addFamily("FAMILY")).setId("foo"));
			return retVal;
		}
		//@formatter:on

	}

}
