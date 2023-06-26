package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
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
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
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
		FhirContext ctx = mock(FhirContext.class);
		IRestfulClientFactory restfulClientFactory = mock(IRestfulClientFactory.class);
		IHttpClient httpClient = mock(IHttpClient.class);
		
		when(ctx.getResourceDefinition(Mockito.eq("CapabilityStatement"))).thenThrow(new DataFormatException());
		when(ctx.getResourceDefinition(Mockito.eq("Conformance"))).thenReturn(ourCtx.getResourceDefinition("Conformance"));
		when(ctx.getResourceDefinition(Conformance.class)).thenReturn(ourCtx.getResourceDefinition("Conformance"));
		when(ctx.getVersion()).thenReturn(FhirVersionEnum.DSTU2.getVersionImplementation());
		when(ctx.getRestfulClientFactory()).thenReturn(restfulClientFactory);

		when(restfulClientFactory.getHttpClient(nullable(StringBuilder.class), (Map<String, List<String>>)nullable(Map.class), nullable(String.class), nullable(RequestTypeEnum.class), nullable(List.class))).thenReturn(httpClient);
		
		IHttpRequest httpRequest = mock(IHttpRequest.class);
		when(httpClient.createGetRequest(any(FhirContext.class), nullable(EncodingEnum.class))).thenReturn(httpRequest);
	
		IHttpResponse httpResponse = mock(IHttpResponse.class);
		when(httpRequest.execute()).thenReturn(httpResponse);
		
		when(httpResponse.getStatus()).thenReturn(404);
		
		ApacheRestfulClientFactory cf = new ApacheRestfulClientFactory(ctx);
		IHttpClient client = mock(IHttpClient.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
		BaseClient baseClient = mock(BaseClient.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
		
		try {
			cf.validateServerBase("http://localhost:9999", client, baseClient);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}
		
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
