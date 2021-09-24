package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
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
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchPostDstu2_1Test {

	public class ParamLoggingInterceptor extends InterceptorAdapter {

		@Override
		public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
			ourLog.info("Params: {}", theRequest.getParameterMap());
			return true;
		}


	}

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchPostDstu2_1Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;
	private static StringAndListParam ourLastName;
	private static RestfulServer ourServlet;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastSortSpec = null;
		ourLastName = null;

		ourServlet.getInterceptorService().unregisterAllInterceptors();
	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsNoInterceptorsYesParams() throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?_format="+Constants.CT_FHIR_JSON);
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);

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
			assertEquals(Constants.CT_FHIR_JSON, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsNoInterceptorsNoParams() throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);

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
			assertEquals(Constants.CT_FHIR_XML, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsYesInterceptorsYesParams() throws Exception {
		ourServlet.registerInterceptor(new ParamLoggingInterceptor());

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?_format="+Constants.CT_FHIR_JSON);
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);

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
			assertEquals(Constants.CT_FHIR_JSON, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsYesInterceptorsNoParams() throws Exception {
		ourServlet.registerInterceptor(new ParamLoggingInterceptor());

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);

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
			assertEquals(Constants.CT_FHIR_XML, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);
		ourServlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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
			ArrayList<Patient> retVal = new ArrayList<>();
			retVal.add((Patient) new Patient().addName(new HumanName().addFamily("FAMILY")).setId("foo"));
			return retVal;
		}
		//@formatter:on

	}

}
