package ca.uhn.fhir.rest.client.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdditionalHeadersInterceptorTest {

	private FhirContext ctx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	// atom-document-large.xml

	@BeforeEach
	public void before() {
		ctx = FhirContext.forR4();

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}


	@Test
	public void testNoHeaders() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), StandardCharsets.UTF_8));

		IGenericClient client = ctx.newRestfulGenericClient("http://foo");

		client.registerInterceptor(new AdditionalRequestHeadersInterceptor());

		client.search().forResource("Patient").returnBundle(Bundle.class).execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());
	}

	@Test
	public void testManyHeaders() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), StandardCharsets.UTF_8));

		IGenericClient client = ctx.newRestfulGenericClient("http://foo");

		Map<String, List<String>> map = new HashMap<>();
		map.put("X-0", Lists.newArrayList("X-0-VAL"));

		AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor(map);
		client.registerInterceptor(interceptor);

		interceptor.addHeaderValue("X-1", "X-1-VAL");
		interceptor.addAllHeaderValues("X-2", Lists.newArrayList("X-2-VAL1", "X-2-VAL2"));

		client.search().forResource("Patient").returnBundle(Bundle.class).execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());

		assertThat(Arrays.stream(get.getHeaders("X-0")).map(Object::toString).collect(Collectors.toList()), Matchers.contains("X-0: X-0-VAL"));
		assertThat(Arrays.stream(get.getHeaders("X-1")).map(Object::toString).collect(Collectors.toList()), Matchers.contains("X-1: X-1-VAL"));
		assertThat(Arrays.stream(get.getHeaders("X-2")).map(Object::toString).collect(Collectors.toList()), Matchers.contains("X-2: X-2-VAL1", "X-2: X-2-VAL2"));
	}

	private String createBundle() {
		return ctx.newXmlParser().encodeResourceToString(new Bundle().setTotal(0));
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
