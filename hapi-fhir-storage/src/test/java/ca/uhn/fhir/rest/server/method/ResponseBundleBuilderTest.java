package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.BundleProviderWithNamedPages;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.rest.api.Constants.LINK_NEXT;
import static ca.uhn.fhir.rest.api.Constants.LINK_PREVIOUS;
import static ca.uhn.fhir.rest.api.Constants.LINK_SELF;
import static java.lang.Math.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.r4.model.Bundle.BundleType.SEARCHSET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseBundleBuilderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(ResponseBundleBuilderTest.class);
	public static final String TEST_LINK_SELF = "http://test.link";
	private static final String TEST_SERVER_BASE = "http://test.server/base";
	public static final int RESOURCE_COUNT = 50;
	public static final int LIMIT = 20;
	public static final int DEFAULT_PAGE_SIZE = 15;
	public static final int CURRENT_PAGE_OFFSET = 2;
	private static final int CURRENT_PAGE_SIZE = 8;
	private static final Integer MAX_PAGE_SIZE = 43;
	private static final String SEARCH_ID = "test-search-id";
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static final String TEST_REQUEST_PATH = "test/request/path";
	private static final Integer REQUEST_OFFSET = 3;
	private static final Integer NEAR_END_NO_NEXT_REQUEST_OFFSET = 49;
	@Mock
	IRestfulServer<RequestDetails> myServer;
	@Mock
	IPagingProvider myPagingProvider;
	private Integer myLimit = null;

	@BeforeEach
	public void before() {
		lenient().when(myServer.getFhirContext()).thenReturn(ourFhirContext);
	}

	@AfterEach
	public void after() {
		reset();
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testEmpty(boolean theCanStoreSearchResults) {
		// setup
		setCanStoreSearchResults(theCanStoreSearchResults);
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(new SimpleBundleProvider());
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, 0, 0);
		assertThat(bundle.getLink()).hasSize(1);
		assertSelfLink(bundle);
	}

	@Test
	void testOffsetNoPageSize() {
		// setup
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider();
		bundleProvider.setCurrentPageOffset(CURRENT_PAGE_OFFSET);

		// run
		try {
			buildResponseBundleRequest(bundleProvider);

			// verify
		} catch (NullPointerException e) {
			assertEquals("IBundleProvider returned a non-null offset, but did not return a non-null page size", e.getMessage());
		}
	}

	@Test
	void testNullId() {
		// setup
		setCanStoreSearchResults(true);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(new Patient());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		try {
			svc.buildResponseBundle(responseBundleRequest);

			// verify
		} catch (InternalErrorException e) {
			assertEquals("HAPI-0435: Server method returned resource of type[Patient] with no ID specified (IResource#setId(IdDt) must be called)", e.getMessage());
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testNoLimit(boolean theCanStoreSearchResults) {
		// setup
		setCanStoreSearchResults(theCanStoreSearchResults);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);
		if (!theCanStoreSearchResults) {
			when(myServer.getDefaultPageSize()).thenReturn(DEFAULT_PAGE_SIZE);
		}
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, DEFAULT_PAGE_SIZE);

		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);
		assertNextLink(bundle, DEFAULT_PAGE_SIZE);
	}

	@Test
	public void buildResponseBundle_withIncludeParamAndFewerResultsThanPageSize_doesNotReturnNextLink() {
		// setup
		int includeResources = 4;
		// we want the number of resources returned to be equal to the pagesize
		List<IBaseResource> list = buildXPatientList(DEFAULT_PAGE_SIZE - includeResources);

		ResponseBundleBuilder svc = new ResponseBundleBuilder(false);

		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Nonnull
			@Override
			public List<IBaseResource> getResources(int theFrom, int theTo, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
				List<IBaseResource> retList = new ArrayList<>(list);
				// our fake includes
				for (int i = 0; i < includeResources; i++) {
					retList.add(new Organization().setId("Organization/" + i));
				}
				theResponsePageBuilder.setIncludedResourceCount(includeResources);
				return retList;
			}
		};

		provider.setSize(null);

		// mocking
		when(myServer.canStoreSearchResults()).thenReturn(true);
		when(myServer.getPagingProvider()).thenReturn(myPagingProvider);
		when(myPagingProvider.getDefaultPageSize()).thenReturn(DEFAULT_PAGE_SIZE);

		ResponseBundleRequest req = buildResponseBundleRequest(provider, "search-id");

		// test
		Bundle bundle = (Bundle) svc.buildResponseBundle(req);

		// verify
		// no next link
		assertThat(bundle.getLink()).hasSize(1);
		assertThat(bundle.getEntry()).hasSize(DEFAULT_PAGE_SIZE);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testFilterNulls(boolean theCanStoreSearchResults) {
		// setup
		setCanStoreSearchResults(theCanStoreSearchResults);
		List<IBaseResource> list = buildPatientList();
		list.set(7, null);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(list);
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);
		if (!theCanStoreSearchResults) {
			when(myServer.getDefaultPageSize()).thenReturn(DEFAULT_PAGE_SIZE);
		}
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, DEFAULT_PAGE_SIZE - 1, "A0", "A14");

		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);
		assertNextLink(bundle, DEFAULT_PAGE_SIZE);
	}

	// TODO KHS add test that relies on Constants.PARAM_OFFSET supplied from request details

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testWithLimit(boolean theCanStoreSearchResults) {
		// setup
		myLimit = LIMIT;
		setCanStoreSearchResults(theCanStoreSearchResults);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);

		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, LIMIT);
		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);
		assertNextLink(bundle, LIMIT);
	}

	@Test
	void testNoLimitNoDefaultPageSize() {
		// setup
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);

		when(myServer.getDefaultPageSize()).thenReturn(null);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, RESOURCE_COUNT);
		assertThat(bundle.getLink()).hasSize(1);
		assertSelfLink(bundle);
	}

	@Test
	void testOffset() {
		// setup
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		bundleProvider.setCurrentPageOffset(CURRENT_PAGE_OFFSET);
		bundleProvider.setCurrentPageSize(CURRENT_PAGE_SIZE);

		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);
		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, RESOURCE_COUNT);
		assertThat(bundle.getLink()).hasSize(3);
		assertSelfLink(bundle);
		assertNextLink(bundle, CURRENT_PAGE_SIZE, CURRENT_PAGE_OFFSET + CURRENT_PAGE_SIZE);
		//noinspection ConstantValue
		assertPrevLink(bundle, max(0, CURRENT_PAGE_OFFSET - CURRENT_PAGE_SIZE));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void unknownBundleSize(boolean theCanStoreSearchResults) {
		// setup
		myLimit = LIMIT;
		setCanStoreSearchResults(theCanStoreSearchResults);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		bundleProvider.setSize(null);
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider, SEARCH_ID);

		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, null, LIMIT);
		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);
		assertNextLink(bundle, LIMIT);
	}

	@Test
	void testCustomLinks() {
		// setup
		setCanStoreSearchResults(true);
		String pageId = "testPageId";
		String nextPageId = "testNextPageId";
		String prevPageId = "testPrevPageId";
		BundleProviderWithNamedPages bundleProvider = new BundleProviderWithNamedPages(buildPatientList(), SEARCH_ID, pageId, RESOURCE_COUNT);
		bundleProvider.setNextPageId(nextPageId);
		bundleProvider.setPreviousPageId(prevPageId);
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider, SEARCH_ID);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(false);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, RESOURCE_COUNT);
		assertThat(bundle.getLink()).hasSize(3);
		assertSelfLink(bundle);

		Bundle.BundleLinkComponent nextLink = bundle.getLink().get(1);
		assertEquals(LINK_NEXT, nextLink.getRelation());
		assertEquals(TEST_SERVER_BASE + "?_getpages=" + SEARCH_ID + "&_pageId=" + nextPageId + "&_bundletype=" + SEARCHSET.toCode(), nextLink.getUrl());

		Bundle.BundleLinkComponent prevLink = bundle.getLink().get(2);
		assertEquals(LINK_PREVIOUS, prevLink.getRelation());
		assertEquals(TEST_SERVER_BASE + "?_getpages=" + SEARCH_ID + "&_pageId=" + prevPageId + "&_bundletype=" + SEARCHSET.toCode(), prevLink.getUrl());
	}

	@Test
	void testCustomLinksWithPageOffset() {
		// setup
		String pageId = "testPageId";
		String nextPageId = "testNextPageId";
		String prevPageId = "testPrevPageId";
		BundleProviderWithNamedPages bundleProvider = new BundleProviderWithNamedPages(buildPatientList(), SEARCH_ID, pageId, RESOURCE_COUNT);
		bundleProvider.setNextPageId(nextPageId);
		bundleProvider.setPreviousPageId(prevPageId);
		// Even though next and prev links are provided, a page offset will override them and force page offset mode
		bundleProvider.setCurrentPageOffset(CURRENT_PAGE_OFFSET);
		bundleProvider.setCurrentPageSize(CURRENT_PAGE_SIZE);
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(true);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, RESOURCE_COUNT);
		assertThat(bundle.getLink()).hasSize(3);
		assertSelfLink(bundle);
		assertNextLink(bundle, CURRENT_PAGE_SIZE, CURRENT_PAGE_OFFSET + CURRENT_PAGE_SIZE);
		assertPrevLink(bundle, 0);
	}


	@Test
	void offsetSinceNonNullSearchId() {
		// setup
		myLimit = LIMIT;
		setCanStoreSearchResults(true);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider, SEARCH_ID);

		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(false);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, LIMIT);
		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);

		assertNextLinkOffset(bundle, LIMIT, LIMIT);
	}

	@Test
	void offsetSinceNonNullSearchIdWithRequestOffset() {
		// setup
		setCanStoreSearchResults(true);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider, SEARCH_ID, REQUEST_OFFSET);

		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(false);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, DEFAULT_PAGE_SIZE, "A3", "A17");
		assertThat(bundle.getLink()).hasSize(3);
		assertSelfLink(bundle);

		assertNextLinkOffset(bundle, DEFAULT_PAGE_SIZE + REQUEST_OFFSET, DEFAULT_PAGE_SIZE);
		assertPrevLinkOffset(bundle);
	}

	@Test
	void testHighOffsetWithSearchIdAndInitiallyUnknownBundleSize() {
		// setup
		setCanStoreSearchResults(true);
		SimpleBundleProvider bundleProvider = new TestBundleProvider(buildPatientList());
		ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(bundleProvider, SEARCH_ID, NEAR_END_NO_NEXT_REQUEST_OFFSET);

		responseBundleRequest.requestDetails.setFhirServerBase(TEST_SERVER_BASE);
		ResponseBundleBuilder svc = new ResponseBundleBuilder(false);

		// run
		Bundle bundle = (Bundle) svc.buildResponseBundle(responseBundleRequest);

		// verify
		verifyBundle(bundle, RESOURCE_COUNT, RESOURCE_COUNT - NEAR_END_NO_NEXT_REQUEST_OFFSET, "A49", "A49");
		assertThat(bundle.getLink()).hasSize(2);
		assertSelfLink(bundle);

		Bundle.BundleLinkComponent nextLink = bundle.getLink().get(1);
		assertEquals(LINK_PREVIOUS, nextLink.getRelation());
		int prevOffset = NEAR_END_NO_NEXT_REQUEST_OFFSET - DEFAULT_PAGE_SIZE;
		assertEquals(TEST_SERVER_BASE + "?_getpages=" + SEARCH_ID + "&_getpagesoffset=" + prevOffset + "&_count=" + ResponseBundleBuilderTest.DEFAULT_PAGE_SIZE + "&_bundletype=" + SEARCHSET.toCode(), nextLink.getUrl());
	}


	private static void assertNextLinkOffset(Bundle theBundle, Integer theOffset, Integer theCount) {
		Bundle.BundleLinkComponent nextLink = theBundle.getLink().get(1);
		assertEquals(LINK_NEXT, nextLink.getRelation());
		assertEquals(TEST_SERVER_BASE + "?_getpages=" + SEARCH_ID + "&_getpagesoffset=" + theOffset + "&_count=" + theCount + "&_bundletype=" + SEARCHSET.toCode(), nextLink.getUrl());
	}

	private static void assertPrevLinkOffset(Bundle theBundle) {
		Bundle.BundleLinkComponent nextLink = theBundle.getLink().get(2);
		assertEquals(LINK_PREVIOUS, nextLink.getRelation());
		assertEquals(TEST_SERVER_BASE + "?_getpages=" + SEARCH_ID + "&_getpagesoffset=" + 0 + "&_count=" + ResponseBundleBuilderTest.DEFAULT_PAGE_SIZE + "&_bundletype=" + SEARCHSET.toCode(), nextLink.getUrl());
	}
	private static void assertNextLink(Bundle theBundle, int theCount) {
		assertNextLink(theBundle, theCount, theCount);
	}

	private static void assertNextLink(Bundle theBundle, int theCount, int theOffset) {
		Bundle.BundleLinkComponent link = theBundle.getLink().get(1);
		assertEquals(LINK_NEXT, link.getRelation());
		assertEquals(TEST_SERVER_BASE + "/" + TEST_REQUEST_PATH + "?_count=" + theCount + "&_offset=" + theOffset, link.getUrl());
	}

	private static void assertPrevLink(Bundle theBundle, int theOffset) {
		Bundle.BundleLinkComponent link = theBundle.getLink().get(2);
		assertEquals(LINK_PREVIOUS, link.getRelation());
		assertEquals(TEST_SERVER_BASE + "/" + TEST_REQUEST_PATH + "?_count=" + ResponseBundleBuilderTest.CURRENT_PAGE_SIZE + "&_offset=" + theOffset, link.getUrl());
	}

	private static void assertSelfLink(Bundle bundle) {
		Bundle.BundleLinkComponent link = bundle.getLinkFirstRep();
		assertEquals(LINK_SELF, link.getRelation());
		assertEquals(TEST_LINK_SELF, link.getUrl());
	}

	private List<IBaseResource> buildPatientList() {
		return buildXPatientList(ResponseBundleBuilderTest.RESOURCE_COUNT);
	}

	private List<IBaseResource> buildXPatientList(int theCount) {
		List<IBaseResource> retval = new ArrayList<>();
		for (int i = 0; i < theCount; ++i) {
			Patient p = new Patient();
			p.setId("A" + i);
			p.setActive(true);
			retval.add(p);
		}
		return retval;
	}

	private void setCanStoreSearchResults(boolean theCanStoreSearchResults) {
		when(myServer.canStoreSearchResults()).thenReturn(theCanStoreSearchResults);
		if (theCanStoreSearchResults) {
			when(myServer.getPagingProvider()).thenReturn(myPagingProvider);
			if (myLimit == null) {
				when(myPagingProvider.getDefaultPageSize()).thenReturn(DEFAULT_PAGE_SIZE);
			} else {
				when(myPagingProvider.getMaximumPageSize()).thenReturn(MAX_PAGE_SIZE);
			}
		}
	}

	@Nonnull
	private ResponseBundleRequest buildResponseBundleRequest(IBundleProvider theBundleProvider) {
		return buildResponseBundleRequest(theBundleProvider, null);
	}

	@Nonnull
	private ResponseBundleRequest buildResponseBundleRequest(IBundleProvider theBundleProvider, String theSearchId) {
		return buildResponseBundleRequest(theBundleProvider, theSearchId, 0);
	}

	@Nonnull
	private ResponseBundleRequest buildResponseBundleRequest(IBundleProvider theBundleProvider, String theSearchId, Integer theOffset) {
		Set<Include> includes = Collections.emptySet();
		BundleTypeEnum bundleType = BundleTypeEnum.SEARCHSET;

		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setFhirServerBase(TEST_SERVER_BASE);
		systemRequestDetails.setRequestPath(TEST_REQUEST_PATH);

		return new ResponseBundleRequest(myServer, theBundleProvider, systemRequestDetails, theOffset, myLimit, TEST_LINK_SELF, includes, bundleType, theSearchId);
	}

	private static void verifyBundle(Bundle theBundle, Integer theExpectedTotal, int theExpectedEntryCount) {
		String firstId = null;
		String lastId = null;
		if (theExpectedEntryCount > 0) {
			firstId = "A0";
			lastId = "A" + (theExpectedEntryCount - 1);
		}
		verifyBundle(theBundle, theExpectedTotal, theExpectedEntryCount, firstId, lastId);
	}

	private static void verifyBundle(Bundle theBundle, Integer theExpectedTotal, int theExpectedEntryCount, String theFirstId, String theLastId) {
		ourLog.trace(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theBundle));
		assertFalse(theBundle.isEmpty());
		assertEquals(SEARCHSET, theBundle.getType());
		assertEquals(theExpectedTotal, theBundle.getTotalElement().getValue());
		List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
		assertThat(entries).hasSize(theExpectedEntryCount);
		if (theFirstId != null) {
			assertEquals(theFirstId, entries.get(0).getResource().getId());
		}
		if (theLastId != null) {
			assertEquals(theLastId, entries.get(theExpectedEntryCount - 1).getResource().getId());
		}
	}

	private static class TestBundleProvider extends SimpleBundleProvider {
		boolean getResourcesCalled = false;
		public TestBundleProvider(List<IBaseResource> theResourceList) {
			super(theResourceList);
		}

		@Nonnull
		public List<IBaseResource> getResources(int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponseBundleBuilder) {
			getResourcesCalled = true;
			return super.getResources(theFromIndex, theToIndex, theResponseBundleBuilder);
		}

		// Emulate the behaviour of PersistedJpaBundleProvider where size() is only set after getResources() has been called
		@Override
		public Integer size() {
			if (getResourcesCalled) {
				return super.size();
			}
			return null;
		}
	}
}
