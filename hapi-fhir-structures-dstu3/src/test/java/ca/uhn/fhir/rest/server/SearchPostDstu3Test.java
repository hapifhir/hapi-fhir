package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.server.SearchPostDstu3Test.ParamLoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchPostDstu3Test {

	public class ParamLoggingInterceptor extends InterceptorAdapter {

		@Override
		public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
			ourLog.info("Params: {}", theRequest.getParameterMap());
			return true;
		}


	}

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchPostDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;
	private static StringAndListParam ourLastName;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastSortSpec = null;
		ourLastName = null;
		
		for (IServerInterceptor next : new ArrayList<IServerInterceptor>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsNoInterceptorsYesParams() throws Exception {
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?_format=application/fhir+json");
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
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
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
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
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
		
		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?_format=application/fhir+json");
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
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
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
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
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
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);
		ourServlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
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
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("foo"));
			return retVal;
		}
		//@formatter:on

	}

}
