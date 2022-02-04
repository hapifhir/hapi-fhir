package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu2016may.model.HumanName;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchDstu2_1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDstu2_1Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static TokenAndListParam ourIdentifiers;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}

	@Test
	public void testSearchNormal() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);

			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchWithInvalidChain() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier.chain=foo%7Cbar");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());
			
			OperationOutcome oo = (OperationOutcome) ourCtx.newXmlParser().parseResource(responseContent);
			assertEquals(Msg.code(1935) + "Invalid search parameter \"identifier.chain\". Parameter contains a chain (.chain) and chains are not supported for this parameter (chaining is only allowed on reference parameters)", oo.getIssue().get(0).getDiagnostics());
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
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setResourceProviders(patientProvider);

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

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@OptionalParam(name=Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers
				) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().addFamily("FAMILY")).setId("1"));
			return retVal;
		}
		//@formatter:on

	}

}
