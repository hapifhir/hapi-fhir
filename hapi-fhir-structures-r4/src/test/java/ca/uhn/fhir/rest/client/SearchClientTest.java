package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchClientTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchClientTest.class);

	private FhirContext ourCtx;
	private HttpClient ourHttpClient;
	private HttpResponse ourHttpResponse;

	@BeforeEach
	public void before() {
		ourCtx = FhirContext.forR4();

		ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testPostOnLongParamsList() throws Exception {
		String resp = createBundle();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenAnswer(t->new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Set<Include> includes = new HashSet<Include>();
		includes.add(new Include("one"));
		includes.add(new Include("two"));
		TokenOrListParam params = new TokenOrListParam();
		for (int i = 0; i < 1000; i++) {
			params.add(new TokenParam("system", "value"));
		}

		// With OR list

		List<Encounter> found = client.searchByList(params, includes);

		assertEquals(1, found.size());

		Encounter encounter = found.get(0);
		assertNotNull(encounter.getSubject().getReference());
		HttpUriRequest value = capt.getValue();

		assertTrue(value instanceof HttpPost, "Expected request of type POST on long params list");
		HttpPost post = (HttpPost) value;
		String body = IOUtils.toString(post.getEntity().getContent(), Charsets.UTF_8);
		ourLog.info(body);
		assertThat(body, Matchers.containsString("_include=one"));
		assertThat(body, Matchers.containsString("_include=two"));

		// With AND list

		TokenAndListParam paramsAndList = new TokenAndListParam();
		paramsAndList.addAnd(params);
		found = client.searchByList(paramsAndList, includes);

		assertEquals(1, found.size());

		encounter = found.get(0);
		assertNotNull(encounter.getSubject().getReference());
		value = capt.getAllValues().get(1);

		assertTrue(value instanceof HttpPost, "Expected request of type POST on long params list");
		post = (HttpPost) value;
		body = IOUtils.toString(post.getEntity().getContent(), Charsets.UTF_8);
		ourLog.info(body);
		assertThat(body, Matchers.containsString("_include=one"));
		assertThat(body, Matchers.containsString("_include=two"));
	}

	@Test
	public void testReturnTypedList() throws Exception {

		String resp = createBundle();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		List<Encounter> found = client.search();
		assertEquals(1, found.size());

		Encounter encounter = found.get(0);
		assertNotNull(encounter.getSubject().getReference());
	}

	private String createBundle() {
		Bundle bundle = new Bundle();

		Encounter enc = new Encounter();
		enc.getSubject().setReference("Patient/1");

		bundle.addEntry().setResource(enc);

		String retVal = ourCtx.newXmlParser().encodeResourceToString(bundle);
		return retVal;
	}

	private interface ITestClient extends IBasicClient {

		@Search
		List<Encounter> search();

		@Search
		List<Encounter> searchByList(@RequiredParam(name = Encounter.SP_IDENTIFIER) TokenOrListParam tokenOrListParam, @IncludeParam Set<Include> theIncludes) throws BaseServerResponseException;

		@Search
		List<Encounter> searchByList(@RequiredParam(name = Encounter.SP_IDENTIFIER) TokenAndListParam tokenOrListParam, @IncludeParam Set<Include> theIncludes) throws BaseServerResponseException;

	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
