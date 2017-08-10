package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class GraphQLR4RawTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GraphQLR4RawTest.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;
	private static String ourNextRetVal;
	private static IdType ourLastId;
	private static String ourLastQuery;
	private static int ourMethodCount;

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		MyProvider provider = new MyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	@Before
	public void before() {
		ourNextRetVal = null;
		ourLastId = null;
		ourLastQuery = null;
		ourMethodCount = 0;
	}

	@Test
	public void testGraphInstance() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$graphql?query=" + UrlUtil.escape("{name{family,given}}"));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("{\"foo\"}", responseContent);
			assertEquals("application/json", status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
			assertEquals("Patient/123", ourLastId.getValue());
			assertEquals("{name{family,given}}", ourLastQuery);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	public static class MyProvider {


		@GraphQL
		public String process(@IdParam IdType theId, @GraphQLQuery String theQuery) {
			ourMethodCount++;
			ourLastId = theId;
			ourLastQuery = theQuery;
			return ourNextRetVal;
		}

	}

}
