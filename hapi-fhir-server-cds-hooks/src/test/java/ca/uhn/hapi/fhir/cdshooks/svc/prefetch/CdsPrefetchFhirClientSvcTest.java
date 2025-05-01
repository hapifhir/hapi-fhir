package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestAuthorizationJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CdsPrefetchFhirClientSvcTest {

	private IGenericClient myMockClient;
	private FhirContext mySpyFhirContext;
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;

	@BeforeEach
	public void beforeEach() {
		myMockClient = Mockito.mock(IGenericClient.class, Mockito.RETURNS_DEEP_STUBS);
		mySpyFhirContext = Mockito.spy(FhirContext.forR4());
		doReturn(myMockClient).when(mySpyFhirContext).newRestfulGenericClient(any(String.class));
		myCdsPrefetchFhirClientSvc = new CdsPrefetchFhirClientSvc(mySpyFhirContext);
	}

	private void setupClientForSearchBundleWithNoNextLink() {
		Bundle results = new Bundle();
		Mockito.when(myMockClient.search().byUrl(any(String.class)).execute()).thenReturn(results);
	}

	private void setupClientForReadResourceById() {
		Bundle results = new Bundle();
		Mockito.when(
				myMockClient.read()
					.resource(any(String.class))
					.withId(any(String.class))
					.execute())
			.thenReturn(results);
	}


	@Test
	void testParseResourceWithSearchParams() throws IllegalArgumentException {
		setupClientForSearchBundleWithNoNextLink();
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234");
		assertNotNull(srq);
		verify(myMockClient.search(), times(1)).byUrl("ServiceRequest?_id=1234");
	}

	@Test
	void testParseResourceWithAdditionalSearchParams() throws IllegalArgumentException {
		setupClientForSearchBundleWithNoNextLink();
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234&_include=ServiceRequest:performer&_include=ServiceRequest:requester");
		assertNotNull(srq);
		verify(myMockClient.search(), times(1)).byUrl("ServiceRequest?_id=1234&_include=ServiceRequest:performer&_include=ServiceRequest:requester");
		verify(myMockClient, never()).loadPage();
	}

	@Test
	void testParseResourceWithReferenceUrlAndAuth() {
		setupClientForReadResourceById();
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
		setupClientForReadResourceById();
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
			myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "1234");
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

	@Test
	void testResourceFromUrl_SearchSetBundleWithPagination() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		// Create the first bundle with a next link
		Bundle firstBundle = new Bundle();
		firstBundle.setType(Bundle.BundleType.SEARCHSET);
		firstBundle.addLink().setRelation("next").setUrl("http://localhost:8000/page2");
		firstBundle.addEntry()
			.setFullUrl("http://localhost:8000/Patient/1_p1")
			.setResource(new Patient().setId("Patient/1_p1"))
			.setSearch( new Bundle.BundleEntrySearchComponent().setMode(Bundle.SearchEntryMode.MATCH).setScore(1.0));
		firstBundle.addEntry()
			.setFullUrl("http://localhost:8000/Patient/2_p1")
			.setResource(new Patient().setId("Patient/2_p1"));

		// Create the second bundle again with a next link
		Bundle secondBundle = new Bundle();
		secondBundle.addLink().setRelation("next").setUrl("http://localhost:8000/page3");
		secondBundle.addEntry()
			.setFullUrl("http://localhost:8000/Patient/1_p2")
			.setResource(new Patient().setId("Patient/1_p2"))
			.setSearch(new Bundle.BundleEntrySearchComponent().setMode(Bundle.SearchEntryMode.INCLUDE));

		Bundle thirdBundle = new Bundle();
		thirdBundle.addEntry()
			.setFullUrl("http://localhost:8000/Patient/1_p3")
			.setResource(new Patient().setId("Patient/1_p3"))
			.setSearch(new Bundle.BundleEntrySearchComponent().setScore(0.5));

		// Mock the client to return the bundles
		when(myMockClient.search().byUrl(any(String.class)).execute()).thenReturn(firstBundle);

		when(myMockClient.loadPage().next(firstBundle).execute()).thenReturn(secondBundle);

		when(myMockClient.loadPage().next(secondBundle).execute()).thenReturn(thirdBundle);
		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name");

		assertEquals("searchset", BundleUtil.getBundleType(mySpyFhirContext, resultBundle));
		// Verify the result bundle contains patients from all pages
		List<SearchBundleEntryParts> entries = BundleUtil.getSearchBundleEntryParts(mySpyFhirContext, resultBundle);
		assertEquals(4, entries.size());

		assertEquals("Patient/1_p1", entries.get(0).getResource().getIdElement().getValue());
		assertEquals("http://localhost:8000/Patient/1_p1", entries.get(0).getFullUrl());
		assertEquals("match", entries.get(0).getSearchMode().getCode());
		assertEquals("1.0", entries.get(0).getSearchScore().toString());
		assertEquals("Patient/2_p1", entries.get(1).getResource().getIdElement().getValue());
		assertEquals("http://localhost:8000/Patient/2_p1", entries.get(1).getFullUrl());
		assertNull(entries.get(1).getSearchMode());
		assertNull(entries.get(1).getSearchScore());
		assertEquals("Patient/1_p2", entries.get(2).getResource().getIdElement().getValue());
		assertEquals("http://localhost:8000/Patient/1_p2", entries.get(2).getFullUrl());
		assertEquals("include", entries.get(2).getSearchMode().getCode());
		assertNull(entries.get(2).getSearchScore());
		assertEquals("Patient/1_p3", entries.get(3).getResource().getIdElement().getValue());
		assertEquals("http://localhost:8000/Patient/1_p3", entries.get(3).getFullUrl());
		assertNull(entries.get(3).getSearchMode());
		assertEquals("0.5", entries.get(3).getSearchScore().toString());
	}

	@Test
	void testResourceFromUrl_PaginatedBundleEntriesWithMissingResourceAndFullUrl_DoesNotThrowException() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		// Create the first bundle with a next link, missing resource
		Bundle firstBundle = new Bundle();
		firstBundle.setType(Bundle.BundleType.SEARCHSET);
		firstBundle.addLink().setRelation("next").setUrl("http://localhost:8000/page2");
		firstBundle.addEntry()
			.setFullUrl("http://localhost:8000/Patient/1_p1");

		//second entry missing FullUrl
		Bundle secondBundle = new Bundle();
		secondBundle.addEntry()
			.setResource(new Patient().setId("Patient/1_p2"));

		when(myMockClient.search().byUrl(any(String.class)).execute()).thenReturn(firstBundle);
		when(myMockClient.loadPage().next(firstBundle).execute()).thenReturn(secondBundle);

		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name");

		assertEquals("searchset", BundleUtil.getBundleType(mySpyFhirContext, resultBundle));
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(mySpyFhirContext, resultBundle);
		assertEquals(2, entries.size());
		assertEquals("http://localhost:8000/Patient/1_p1", entries.get(0).getFullUrl());
		assertNull(entries.get(0).getResource());
		assertNull(entries.get(1).getFullUrl());
		assertEquals("Patient/1_p2", entries.get(1).getResource().getIdElement().getValue());
	}
}
