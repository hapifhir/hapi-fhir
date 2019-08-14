package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.IncludeParam;
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
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import ca.uhn.fhir.test.utilities.JettyUtil;

public class SearchR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}

	private Bundle executeAndReturnLinkNext(HttpGet httpGet, EncodingEnum theExpectEncoding) throws IOException, ClientProtocolException {
		Bundle bundle;
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			EncodingEnum ct = EncodingEnum.forContentType(status.getEntity().getContentType().getValue().replaceAll(";.*", "").trim());
			assertEquals(theExpectEncoding, ct);
			bundle = ct.newParser(ourCtx).parseResource(Bundle.class, responseContent);
			validate(bundle);
			assertEquals(10, bundle.getEntry().size());
			String linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
			assertNotNull(linkNext);
		}
		return bundle;
	}

	@Test
	public void testPagingPreservesElements() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;
		String linkSelf;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_elements=name&_elements:exclude=birthDate,active");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkSelf = bundle.getLink(Constants.LINK_SELF).getUrl();
		assertThat(linkSelf, containsString("_elements=name"));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));

		ourLog.info(toJson(bundle));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));
		assertThat(linkNext, containsString("_elements:exclude=active,birthDate"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));
		assertThat(linkNext, containsString("_elements:exclude=active,birthDate"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));
		assertThat(linkNext, containsString("_elements:exclude=active,birthDate"));

	}

	/**
	 * See #836
	 */
	@Test
	public void testIncludeSingleParameter() throws Exception {
		HttpGet httpGet;
		Bundle bundle;

		// No include specified
		httpGet = new HttpGet("http://localhost:" + ourPort + "/MedicationRequest");
		bundle = executeAndReturnBundle(httpGet);
		assertEquals(1, bundle.getEntry().size());
	}

	/**
	 * See #836
	 */
	@Test
	public void testIncludeSingleParameterWithIncludeStar() throws Exception {
		HttpGet httpGet;
		Bundle bundle;

		// * include specified
		httpGet = new HttpGet("http://localhost:" + ourPort + "/MedicationRequest?_include=" + UrlUtil.escapeUrlParam("*"));
		bundle = executeAndReturnBundle(httpGet);
		assertEquals(2, bundle.getEntry().size());
	}

	/**
	 * See #836
	 */
	@Test
	public void testIncludeSingleParameterWithIncludeByName() throws Exception {
		HttpGet httpGet;
		Bundle bundle;

		// MedicationRequest:medication include specified
		httpGet = new HttpGet("http://localhost:" + ourPort + "/MedicationRequest?_include=" + UrlUtil.escapeUrlParam(MedicationRequest.INCLUDE_MEDICATION.getValue()));
		bundle = executeAndReturnBundle(httpGet);
		assertEquals(2, bundle.getEntry().size());

	}

	private Bundle executeAndReturnBundle(HttpGet theHttpGet) throws IOException {
		Bundle bundle;
		try (CloseableHttpResponse status = ourClient.execute(theHttpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		}
		return bundle;
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
	public void testPagingPreservesEncodingJson() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_format=json");
		bundle = executeAndReturnLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), containsString("active"));
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
	public void testSearchNormal() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			validate(ourCtx.newJsonParser().parseResource(responseContent));
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);

			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		}

	}

	@Test
	public void testRequestIdGeneratedAndReturned() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("[a-z0-9]{16}"));
		}
	}

	@Test
	public void testRequestIdSuppliedAndReturned() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		httpGet.addHeader(Constants.HEADER_REQUEST_ID, "help im a bug");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("help im a bug"));
		}
	}

	@Test
	public void testRequestIdSuppliedAndReturned_Invalid() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		httpGet.addHeader(Constants.HEADER_REQUEST_ID, "help i'm a bug");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("[a-z0-9]{16}"));
		}
	}

	@Test
	public void testSearchWithInvalidChain() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier.chain=foo%7Cbar");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());

			OperationOutcome oo = (OperationOutcome) ourCtx.newJsonParser().parseResource(responseContent);
			assertEquals(
				"Invalid search parameter \"identifier.chain\". Parameter contains a chain (.chain) and chains are not supported for this parameter (chaining is only allowed on reference parameters)",
				oo.getIssueFirstRep().getDiagnostics());
		}

	}

	@Test
	public void testSearchWithPostAndInvalidParameters() {
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
				.returnBundle(org.hl7.fhir.r4.model.Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Invalid request: The FHIR endpoint on this server does not know how to handle POST operation[Patient/_search] with parameters [[_pretty, foo]]"));
		}

	}

	private String toJson(Bundle theBundle) {
		return ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theBundle);
	}

	protected void validate(IBaseResource theResource) {
		FhirValidator validatorModule = ourCtx.newValidator();
		ValidationResult result = validatorModule.validateWithResult(theResource);
		if (!result.isSuccessful()) {
			fail(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		}
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
			ArrayList<Patient> retVal = new ArrayList<>();

			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.getIdElement().setValue("Patient/" + i + "/_history/222");
				ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE.getCode());
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.setActive(true);
				retVal.add(patient);
			}
			return retVal;
		}

	}

	public static class DummyMedicationRequestResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return MedicationRequest.class;
		}

		@SuppressWarnings("rawtypes")
		@Search()
		public List<IBaseResource> search(
			@IncludeParam Set<Include> theIncludes
		) {
			MedicationRequest mr = new MedicationRequest();
			mr.setId("1");
			mr.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);

			Medication m = new Medication();
			m.setId("2");
			m.setStatus(Medication.MedicationStatus.ENTEREDINERROR);

			mr.setMedication(new Reference(m));

			ArrayList<IBaseResource> retVal = Lists.newArrayList(mr);

			if (theIncludes.contains(MedicationRequest.INCLUDE_MEDICATION)) {
				retVal.add(m);
			}

			return retVal;
		}

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();
		DummyMedicationRequestResourceProvider medRequestProvider = new DummyMedicationRequestResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.JSON);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.setResourceProviders(patientProvider, medRequestProvider);
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

}
