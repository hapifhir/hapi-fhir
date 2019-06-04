package ca.uhn.fhir.rest.client;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.Mockito;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;

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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
