package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDstu3Test.class);
	private static int ourPort;

	private static Server ourServer;

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

			OperationOutcome oo = (OperationOutcome) ourCtx.newJsonParser().parseResource(responseContent);
			assertEquals(
					Msg.code(1935) + "Invalid search parameter \"identifier.chain\". Parameter contains a chain (.chain) and chains are not supported for this parameter (chaining is only allowed on reference parameters)",
					oo.getIssueFirstRep().getDiagnostics());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	
	@Test
	public void testPagingPreservesEncodingJson() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_format=json");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

	}

	@Test
	public void testPagingPreservesEncodingApplicationJsonFhir() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_format=" + Constants.CT_FHIR_JSON_NEW);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

	}

	@Test
	public void testPagingPreservesEncodingXml() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_format=xml");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

	}

	@Test
	public void testPagingPreservesEncodingNone() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

	}

	@Test
	public void testPagingPreservesEncodingNoneWithBrowserAcceptHeader() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar");
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

	}

	private Bundle executeAndReturnLinkNext(HttpGet httpGet, EncodingEnum theExpectEncoding) throws IOException, ClientProtocolException {
		CloseableHttpResponse status = ourClient.execute(httpGet);
		Bundle bundle;
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			EncodingEnum ct = EncodingEnum.forContentType(status.getEntity().getContentType().getValue().replaceAll(";.*", "").trim());
			assertEquals(theExpectEncoding, ct);
			bundle = ct.newParser(ourCtx).parseResource(Bundle.class, responseContent);
			assertEquals(10, bundle.getEntry().size());
			String linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
			assertNotNull(linkNext);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
		return bundle;
	}

	
	@Test
	public void testSearchWithPostAndInvalidParameters() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setLogRequestSummary(true);
		interceptor.setLogRequestBody(true);
		interceptor.setLogRequestHeaders(false);
		interceptor.setLogResponseBody(false);
		interceptor.setLogResponseHeaders(false);
		interceptor.setLogResponseSummary(false);
		client.registerInterceptor(interceptor);
		try {
			client
					.search()
					.forResource(Patient.class)
					.where(new StringClientParam("foo").matches().value("bar"))
					.prettyPrint()
					.usingStyle(SearchStyleEnum.POST)
					.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
					.encodedJson()
					.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Invalid request: The FHIR endpoint on this server does not know how to handle POST operation[Patient/_search] with parameters [[_pretty, foo]]"));
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals(OperationOutcome.IssueType.NOTSUPPORTED, oo.getIssueFirstRep().getCode());
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
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

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

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@RequiredParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
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
