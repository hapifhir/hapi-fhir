package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

import ca.uhn.fhir.test.utilities.JettyUtil;

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
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.registerProviders(Collections.singletonList(new MyGraphQLProvider()));
		servlet.registerProvider(new MyPatientResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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


		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}"));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("{\"foo\"}", responseContent);
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
			assertEquals("Patient/123", ourLastId.getValue());
			assertEquals("{name{family,given}}", ourLastQuery);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testGraphInstanceUnknownType() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Condition/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}"));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("Unknown resource type"));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testGraphSystem() throws Exception {
		ourNextRetVal = "{\"foo\"}";


		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$graphql?query=" + UrlUtil.escapeUrlParam("{name{family,given}}"));
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("{\"foo\"}", responseContent);
			assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), startsWith("application/json"));
			assertEquals(null, ourLastId);
			assertEquals("{name{family,given}}", ourLastQuery);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	public static class MyGraphQLProvider {


		@GraphQL
		public String process(@IdParam IdType theId, @GraphQLQuery String theQuery) {
			ourMethodCount++;
			ourLastId = theId;
			ourLastQuery = theQuery;
			return ourNextRetVal;
		}

	}

	public static class MyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
			@OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.getIdElement().setValue("Patient/" + i);
				retVal.add((Patient) patient);
			}
			return retVal;
		}

	}


}
