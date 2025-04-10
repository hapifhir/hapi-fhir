package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestAuthorizationJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CdsPrefetchFhirClientSvcTest {

	private IGenericClient myMockClient;
	private FhirContext mySpyFhirContext;
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;
	private IInterceptorBroadcaster myMockInterceptorBroadcaster;
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvcWithInterceptorBroadcaster;

	@RegisterExtension
	public LogbackTestExtension myLogBackTestExtension = new LogbackTestExtension(CdsPrefetchFhirClientSvc.class, Level.INFO);

	@BeforeEach
	public void beforeEach() {
		myMockClient = Mockito.mock(IGenericClient.class, Mockito.RETURNS_DEEP_STUBS);
		mySpyFhirContext = Mockito.spy(FhirContext.forR4());
		doReturn(myMockClient).when(mySpyFhirContext).newRestfulGenericClient(any(String.class));
		myCdsPrefetchFhirClientSvc = new CdsPrefetchFhirClientSvc(mySpyFhirContext, null);

		myMockInterceptorBroadcaster = mock(IInterceptorBroadcaster.class);
		myCdsPrefetchFhirClientSvcWithInterceptorBroadcaster = new CdsPrefetchFhirClientSvc(mySpyFhirContext, myMockInterceptorBroadcaster);

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

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234", 0);
		assertNotNull(srq);
		verify(myMockClient.search(), times(1)).byUrl("ServiceRequest?_id=1234");
	}

	@Test
	void testParseResourceWithAdditionalSearchParams() throws IllegalArgumentException {
		setupClientForSearchBundleWithNoNextLink();
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest?_id=1234&_include=ServiceRequest:performer&_include=ServiceRequest:requester", 0);
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

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest/1234", 0);
		assertNotNull(srq);
		verify(cdsServiceRequestAuthorizationJson, times(2)).getAccessToken();
		verify(myMockClient.read().resource("ServiceRequest"), times(1)).withId("1234");
	}

	@Test
	void testParseResourceWithReferenceUrl() {
		setupClientForReadResourceById();
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		IBaseResource srq = myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "ServiceRequest/1234", 0);
		assertNotNull(srq);
		verify(myMockClient.read().resource("ServiceRequest"), times(1)).withId("1234");
	}

	@Test
	void testParseResourceWithNoResourceType() {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		try {
			myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "1234", 0);
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
			myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "/1234", 0);
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
		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name", 0);

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

		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name", 0);

		assertEquals("searchset", BundleUtil.getBundleType(mySpyFhirContext, resultBundle));
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(mySpyFhirContext, resultBundle);
		assertEquals(2, entries.size());
		assertEquals("http://localhost:8000/Patient/1_p1", entries.get(0).getFullUrl());
		assertNull(entries.get(0).getResource());
		assertNull(entries.get(1).getFullUrl());
		assertEquals("Patient/1_p2", entries.get(1).getResource().getIdElement().getValue());
	}

	@ParameterizedTest
	@ValueSource(ints = { -1,0,1,2,3,4 })
	void testResourceFromUrl_EnforcesMaxPages(int theMaxPages) {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		int totalNumberOfPages = 3;
		boolean shouldExpectAllPagesToBeLoaded = theMaxPages <= 0 || theMaxPages > totalNumberOfPages;
		int expectedNumberOfPagesToLoad =  shouldExpectAllPagesToBeLoaded ? totalNumberOfPages : theMaxPages;
		createTestBundlesAndSetupMocksForPagination(totalNumberOfPages, expectedNumberOfPagesToLoad);

		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvc.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name", theMaxPages);

		assertEquals("searchset", BundleUtil.getBundleType(mySpyFhirContext, resultBundle));
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(mySpyFhirContext, resultBundle);
		//since each page contains one entry in the test setup, the number of entries in the result bundle should be equal to the number of expected pages
		assertEquals(expectedNumberOfPagesToLoad, entries.size());


		if (expectedNumberOfPagesToLoad < totalNumberOfPages) {
			String expectedLogMessage = String.format("The limit of %d pages has been reached for retrieving the CDS Hooks prefetch url 'Patient?given=name', will not fetch any more pages for this query.", theMaxPages);
			LogbackTestExtensionAssert.assertThat(myLogBackTestExtension).hasWarnMessage(expectedLogMessage);
		}
	}


	@ParameterizedTest
	@CsvSource({"3,true",
				"4, true",
				"3, false",
				"4, false"})
	void testResourceFromUrl_WhenTheNumberOfEntriesExceedsTheThreshold_CallsThePerfTraceWarningHookIfOneRegistered(int theNumberOfPages, boolean thePerfTraceWarningHookRegistered) {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setFhirServer("http://localhost:8000");

		createTestBundlesAndSetupMocksForPagination(theNumberOfPages, theNumberOfPages);

		ArgumentCaptor<HookParams> hookParamsArgumentCaptor = ArgumentCaptor.forClass(HookParams.class);
		when(myMockInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING)).thenReturn(thePerfTraceWarningHookRegistered);
		if (thePerfTraceWarningHookRegistered) {
			when(myMockInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), hookParamsArgumentCaptor.capture())).thenReturn(false);
		}
		myCdsPrefetchFhirClientSvcWithInterceptorBroadcaster.setNumberOfEntriesThresholdForPerfWarning(2);
		IBaseBundle resultBundle = (IBaseBundle) myCdsPrefetchFhirClientSvcWithInterceptorBroadcaster.resourceFromUrl(cdsServiceRequestJson, "Patient?given=name", 0);

		assertEquals("searchset", BundleUtil.getBundleType(mySpyFhirContext, resultBundle));
		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(mySpyFhirContext, resultBundle);
		//each page contains one entry in the setup
		assertEquals(theNumberOfPages, entries.size());

		String expectedMsg = "CDS Hooks prefetch url 'Patient?given=name' returned 2 entries after 2 pages and there are more pages to fetch. This may be a performance issue. If possible, consider modifying the query to return only the resources needed.";
		if (thePerfTraceWarningHookRegistered) {
			verify(myMockInterceptorBroadcaster, times(1)).callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), any());
			HookParams hookParams = hookParamsArgumentCaptor.getValue();
			String perfWarningMsg = hookParams.get(StorageProcessingMessage.class).getMessage();
			assertEquals(expectedMsg, perfWarningMsg);
		}
		else {
			verify(myMockInterceptorBroadcaster).hasHooks(Pointcut.JPA_PERFTRACE_WARNING);
			verifyNoMoreInteractions(myMockInterceptorBroadcaster);
		}

		LogbackTestExtensionAssert.assertThat(myLogBackTestExtension).hasWarnMessage(expectedMsg);
	}

	private void createTestBundlesAndSetupMocksForPagination(int theNumberOfPages, int theExpectedPagesToLoad) {

		List<Bundle> bundles = new ArrayList<>();
		for (int i = 1; i <= theNumberOfPages; i++) {
			Bundle currentBundle = new Bundle();
			currentBundle.setType(Bundle.BundleType.SEARCHSET);
			currentBundle.addEntry()
				.setFullUrl("http://localhost:8000/Patient/1_p"+i)
				.setResource(new Patient().setId("Patient/1_p"+i));
			//add next link to all pages except the last one
			if (i != theNumberOfPages) {
				currentBundle.addLink().setRelation("next").setUrl("http://localhost:8000/page"+(i+1));
			}
			bundles.add(currentBundle);
		}


		//first bundle is the one returned by the search().byUrl() method
		when(myMockClient.search().byUrl(any(String.class)).execute()).thenReturn(bundles.get(0));
		//the remaining bundles are returned by the loadPage().next() method
		for(int i = 1; i < theExpectedPagesToLoad; i++) {
			when(myMockClient.loadPage().next(bundles.get(i-1)).execute()).thenReturn(bundles.get(i));
		}

	}
}
