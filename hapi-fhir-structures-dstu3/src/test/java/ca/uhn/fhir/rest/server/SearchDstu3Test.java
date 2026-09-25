package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


public class SearchDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static TokenAndListParam ourIdentifiers;
	private static String ourLastMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}

	@Test
	public void testSearchNormal() throws Exception {
		String responseContent = ourServer.fhirRequest("/Patient?identifier=foo%7Cbar").get().assertStatus(200).getBody();
		ourLog.info(responseContent);

		assertEquals("search", ourLastMethod);

		assertEquals("foo", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertEquals("bar", ourIdentifiers.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

	}

	@Test
	public void testSearchWithInvalidChain() throws Exception {
		String responseContent = ourServer.fhirRequest("/Patient?identifier.chain=foo%7Cbar").get().assertStatus(400).getBody();
		ourLog.info(responseContent);

		OperationOutcome oo = (OperationOutcome) ourCtx.newJsonParser().parseResource(responseContent);
		assertEquals(Msg.code(1935) + "Invalid search parameter \"identifier.chain\". Parameter contains a chain (.chain) and chains are not supported for this parameter (chaining is only allowed on reference parameters)", oo.getIssueFirstRep().getDiagnostics());

	}

	
	@Test
	public void testPagingPreservesEncodingJson() throws Exception {
		String linkNext;
		Bundle bundle;

		// Initial search
		bundle = executeAndReturnLinkNext(ourServer.fhirRequest("/Patient?identifier=foo%7Cbar&_format=json"), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=json");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=json");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=json");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=json");

	}

	@Test
	public void testPagingPreservesEncodingApplicationJsonFhir() throws Exception {
		String linkNext;
		Bundle bundle;

		// Initial search
		bundle = executeAndReturnLinkNext(ourServer.fhirRequest("/Patient?identifier=foo%7Cbar&_format=" + Constants.CT_FHIR_JSON_NEW), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW));

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW));

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW));

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=" + UrlUtil.escapeUrlParam(Constants.CT_FHIR_JSON_NEW));

	}

	@Test
	public void testPagingPreservesEncodingXml() throws Exception {
		String linkNext;
		Bundle bundle;

		// Initial search
		bundle = executeAndReturnLinkNext(ourServer.fhirRequest("/Patient?identifier=foo%7Cbar&_format=xml"), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=xml");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=xml");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=xml");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).contains("_format=xml");

	}

	@Test
	public void testPagingPreservesEncodingNone() throws Exception {
		String linkNext;
		Bundle bundle;

		// Initial search
		bundle = executeAndReturnLinkNext(ourServer.fhirRequest("/Patient?identifier=foo%7Cbar"), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext), EncodingEnum.JSON);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

	}

	@Test
	public void testPagingPreservesEncodingNoneWithBrowserAcceptHeader() throws Exception {
		String linkNext;
		Bundle bundle;

		// Initial search
		bundle = executeAndReturnLinkNext(ourServer.fhirRequest("/Patient?identifier=foo%7Cbar").withHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext).withHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext).withHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

		// Fetch the next page
		bundle = executeAndReturnLinkNext(HttpTestRequest.to(ourServer.getHttpClient(), ourCtx, linkNext).withHeader(Constants.HEADER_ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"), EncodingEnum.XML);
		linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertThat(linkNext).doesNotContain("_format");

	}

	private Bundle executeAndReturnLinkNext(HttpTestRequest theRequest, EncodingEnum theExpectEncoding) {
		HttpTestResponse response = theRequest.get();
		String responseContent = response.assertStatus(200).getBody();
		ourLog.info(responseContent);
		EncodingEnum ct = EncodingEnum.forContentType(response.getContentType());
		assertEquals(theExpectEncoding, ct);
		Bundle bundle = ct.newParser(ourCtx).parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(10);
		String linkNext = bundle.getLink(Constants.LINK_NEXT).getUrl();
		assertNotNull(linkNext);
		return bundle;
	}

	
	@Test
	public void testSearchWithPostAndInvalidParameters() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourServer.getBaseUrl());
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
			fail();		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid request: The FHIR endpoint on this server does not know how to handle POST operation[Patient/_search] with parameters [[_pretty, foo]]");
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			assertEquals(OperationOutcome.IssueType.NOTSUPPORTED, oo.getIssueFirstRep().getCode());
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

		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@RequiredParam(name = Patient.SP_IDENTIFIER) TokenAndListParam theIdentifiers) {
			ourLastMethod = "search";
			ourIdentifiers = theIdentifiers;
			ArrayList<Patient> retVal = new ArrayList<>();
			
			for (int i = 0; i < 200; i++) {
				Patient patient = new Patient();
				patient.addName(new HumanName().setFamily("FAMILY"));
				patient.getIdElement().setValue("Patient/" + i);
				retVal.add(patient);
			}
			return retVal;
		}

	}

}
