package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class RestfulClientFactoryDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	/**
	 * See #518
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	@Test
	public void testMissingCapabilityStatementDstu2() throws Exception {
		FhirContext ctx = spy(ourCtx);
		IRestfulClientFactory restfulClientFactory = mock(IRestfulClientFactory.class);
		IHttpClient httpClient = mock(IHttpClient.class);
		IHttpRequest httpRequest = mock(IHttpRequest.class);
		IHttpResponse httpResponse = mock(IHttpResponse.class);

		when(ctx.getRestfulClientFactory()).thenReturn(restfulClientFactory);
		when(restfulClientFactory.getHttpClient(nullable(StringBuilder.class), (Map<String, List<String>>)nullable(Map.class), nullable(String.class), nullable(RequestTypeEnum.class), nullable(List.class)))
			.thenReturn(httpClient);
		when(httpClient.createGetRequest(any(FhirContext.class), nullable(EncodingEnum.class))).thenReturn(httpRequest);
		when(httpRequest.execute()).thenReturn(httpResponse);
		when(httpResponse.getStatus()).thenReturn(404);

		ApacheRestfulClientFactory cf = new ApacheRestfulClientFactory(ctx);
		IHttpClient client = mock(IHttpClient.class);
		BaseClient baseClient = mock(BaseClient.class);
		when(baseClient.getInterceptorService()).thenReturn(mock(InterceptorService.class));
		
		try {
			cf.validateServerBase("http://localhost:9999", client, baseClient);
			fail();		} catch (ResourceNotFoundException e) {
			// ok
		}
		
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
