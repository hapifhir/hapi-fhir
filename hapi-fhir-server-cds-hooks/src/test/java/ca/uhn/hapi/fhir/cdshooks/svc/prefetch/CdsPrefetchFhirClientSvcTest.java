package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestAuthorizationJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CdsPrefetchFhirClientSvcTest {

	private IGenericClient myMockClient;
	private FhirContext myMockFhirContext;
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;

	@BeforeEach
	public void beforeEach() {
		myMockClient = Mockito.mock(IGenericClient.class, Mockito.RETURNS_DEEP_STUBS);
		myMockFhirContext = Mockito.mock(FhirContext.class);
		myCdsPrefetchFhirClientSvc = new CdsPrefetchFhirClientSvc(myMockFhirContext);
		Bundle results = new Bundle();
		Mockito.when(myMockFhirContext.newRestfulGenericClient(any(String.class))).thenReturn(myMockClient);
		Mockito.when(myMockClient.search().byUrl(any(String.class)).execute()).thenReturn(results);
		Mockito.when(
			myMockClient.read()
				.resource(any(String.class))
				.withId(any(String.class))
				.execute())
			.thenReturn(results);
	}

	@Test
	void testParseResourceWithSearchParams() throws IllegalArgumentException {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234");
		assertNotNull(srq);
		verify(myMockClient.search(), times(1)).byUrl("ServiceRequest?_id=1234");
	}

	@Test
	void testParseResourceWithAdditionalSearchParams() throws IllegalArgumentException {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");
		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234&_include=ServiceRequest:performer&_include=ServiceRequest:requester");
		assertNotNull(srq);
		verify(myMockClient.search(), times(1)).byUrl("ServiceRequest?_id=1234&_include=ServiceRequest:performer&_include=ServiceRequest:requester");
	}

	@Test
	void testParseResourceWithReferenceUrlAndAuth() throws Exception {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		CdsServiceRequestAuthorizationJson cdsServiceRequestAuthorizationJson = spy(new CdsServiceRequestAuthorizationJson());
		cdsServiceRequestAuthorizationJson.setAccessToken("test123");
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");
		cdsServiceRequestJson.setServiceRequestAuthorizationJson(cdsServiceRequestAuthorizationJson);

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest/1234");
		assertNotNull(srq);
		verify(cdsServiceRequestAuthorizationJson, times(2)).getAccessToken();
		verify(myMockClient.read().resource("ServiceRequest"), times(1)).withId("1234");
	}

	@Test
	void testParseResourceWithReferenceUrl() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest/1234");
		assertNotNull(srq);
		verify(myMockClient.read().resource("ServiceRequest"), times(1)).withId("1234");
	}

	@Test
	void testParseResourceWithNoResourceType() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		try {
			IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "1234");
			fail("should throw, no resource present");
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2384: Unable to translate url 1234 into a resource or a bundle.", e.getMessage());
		}
	}

	@Test
	void testParseResourceWithNoResourceTypeAndSlash() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		try {
			IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "/1234");
			fail("should throw, no resource present");
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2383: Failed to resolve /1234. Url does not start with a resource type.", e.getMessage());
		}
	}
}
