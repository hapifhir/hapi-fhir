package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchR4Test.class);
	private static CloseableHttpClient ourClient;
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyMedicationRequestResourceProvider())
		.withServer(t -> t.setDefaultResponseEncoding(EncodingEnum.JSON))
		.withServer(t -> t.setPagingProvider(new FifoMemoryPagingProvider(10)));
	private int myPort;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
		myPort = myRestfulServerExtension.getPort();
	}

	private Bundle executeSearchAndValidateHasLinkNext(HttpGet httpGet, EncodingEnum theExpectEncoding) throws IOException {
		Bundle bundle = executeSearch(httpGet, theExpectEncoding);
		String linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertNotNull(linkNext);

		assertEquals(10, bundle.getEntry().size());
		return bundle;
	}

	private Bundle executeSearch(HttpGet httpGet, EncodingEnum theExpectEncoding) throws IOException {
		Bundle bundle;
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			EncodingEnum ct = EncodingEnum.forContentType(status.getEntity().getContentType().getValue().replaceAll(";.*", "").trim());
			assertEquals(theExpectEncoding, ct);
			bundle = ct.newParser(myCtx).parseResource(Bundle.class, responseContent);
			validate(bundle);
		}
		return bundle;
	}

	/**
	 * A paging request that incorrectly executes at the type level shouldn't be grabbed by the search method binding
	 */
	@Test
	public void testPageRequestCantTriggerSearchAccidentally() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?" + Constants.PARAM_PAGINGACTION + "=12345");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("not know how to handle GET operation[Patient] with parameters [[_getpages]]"));
		}
	}


	/**
	 * See #1763
	 */
	@Test
	public void testSummaryCount() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&" + Constants.PARAM_SUMMARY + "=" + SummaryEnum.COUNT.getCode());
		Bundle bundle = executeSearch(httpGet, EncodingEnum.JSON);
		ourLog.info(toJson(bundle));
		assertEquals(200, bundle.getTotal());
		assertEquals("searchset", bundle.getType().toCode());
		assertEquals(0, bundle.getEntry().size());
	}


	@Test
	public void testPagingPreservesElements() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;
		String linkSelf;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_elements=name&_elements:exclude=birthDate,active");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkSelf = bundle.getLink(Constants.LINK_SELF).getUrl();
		assertThat(linkSelf, containsString("_elements=name"));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));

		ourLog.info(toJson(bundle));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));
		assertThat(linkNext, containsString("_elements:exclude=active,birthDate"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), not(containsString("\"active\"")));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_elements=name"));
		assertThat(linkNext, containsString("_elements:exclude=active,birthDate"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
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
		httpGet = new HttpGet("http://localhost:" + myPort + "/MedicationRequest");
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
		httpGet = new HttpGet("http://localhost:" + myPort + "/MedicationRequest?_include=" + UrlUtil.escapeUrlParam("*"));
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
		httpGet = new HttpGet("http://localhost:" + myPort + "/MedicationRequest?_include=" + UrlUtil.escapeUrlParam(MedicationRequest.INCLUDE_MEDICATION.getValue()));
		bundle = executeAndReturnBundle(httpGet);
		assertEquals(2, bundle.getEntry().size());

	}

	private Bundle executeAndReturnBundle(HttpGet theHttpGet) throws IOException {
		Bundle bundle;
		try (CloseableHttpResponse status = ourClient.execute(theHttpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			bundle = myCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		}
		return bundle;
	}

	@Test
	public void testPagingPreservesEncodingApplicationJsonFhir() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_format=" + Constants.CT_FHIR_JSON_NEW);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW)));

	}

	@Test
	public void testPagingPreservesEncodingJson() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_format=json");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		assertThat(toJson(bundle), containsString("active"));
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=json"));

	}

	@Test
	public void testPagingPreservesEncodingNone() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

	}

	@Test
	public void testPagingPreservesEncodingNoneWithBrowserAcceptHeader() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar");
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, not(containsString("_format")));

	}

	@Test
	public void testPagingPreservesEncodingXml() throws Exception {
		HttpGet httpGet;
		String linkNext;
		Bundle bundle;

		// Initial search
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_format=xml");
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

		// Fetch the next page
		httpGet = new HttpGet(linkNext);
		bundle = executeSearchAndValidateHasLinkNext(httpGet, EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext, containsString("_format=xml"));

	}

	@Test
	public void testSearchNormal() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			validate(myCtx.newJsonParser().parseResource(responseContent));
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);

			assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
			assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		}

	}

	@Test
	public void testRequestIdGeneratedAndReturned() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("[a-zA-Z0-9]{16}"));
		}
	}

	@Test
	public void testRequestIdSuppliedAndReturned() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		httpGet.addHeader(Constants.HEADER_REQUEST_ID, "help im a bug");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("help im a bug"));
		}
	}

	@Test
	public void testRequestIdSuppliedAndReturned_Invalid() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier=foo%7Cbar&_pretty=true");
		httpGet.addHeader(Constants.HEADER_REQUEST_ID, "help i'm a bug");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
			String requestId = status.getFirstHeader(Constants.HEADER_REQUEST_ID).getValue();
			assertThat(requestId, matchesPattern("[a-zA-Z0-9]{16}"));
		}
	}

	@Test
	public void testSearchWithInvalidChain() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?identifier.chain=foo%7Cbar");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(400, status.getStatusLine().getStatusCode());

			OperationOutcome oo = (OperationOutcome) myCtx.newJsonParser().parseResource(responseContent);
			assertEquals(
				Msg.code(1935) + "Invalid search parameter \"identifier.chain\". Parameter contains a chain (.chain) and chains are not supported for this parameter (chaining is only allowed on reference parameters)",
				oo.getIssueFirstRep().getDiagnostics());
		}

	}

	@Test
	public void testSearchWithPostAndInvalidParameters() {
		IGenericClient client = myCtx.newRestfulGenericClient("http://localhost:" + myPort);
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
		return myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theBundle);
	}

	protected void validate(IBaseResource theResource) {
		FhirValidator validatorModule = myCtx.newValidator();
		ValidationResult result = validatorModule.validateWithResult(theResource);
		if (!result.isSuccessful()) {
			fail(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
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
			@OptionalParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		ourClient.close();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

}
