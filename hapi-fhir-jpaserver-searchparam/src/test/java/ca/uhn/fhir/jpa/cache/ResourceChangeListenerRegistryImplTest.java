package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerRegistryImplTest {
	public static final String RESOURCE_NAME = "Patient";
	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerCache myResourceChangeListenerCache;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	private IResourceChangeListener myTestListener = mock(IResourceChangeListener.class);
	private SearchParameterMap myMap = SearchParameterMap.newSynchronous();
	private Set<ResourceChangeListenerWithSearchParamMap> myEntries;

	@Configuration
	static class SpringContext {
		@Bean
		public IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
			return new ResourceChangeListenerRegistryImpl();
		}
		@Bean
		public FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@BeforeEach
	public void before() {
		myEntries = new HashSet<>();
		myEntries.add(new ResourceChangeListenerWithSearchParamMap(RESOURCE_NAME, myTestListener, myMap));
		resetMockCache();
	}

	private void resetMockCache() {
		reset(myResourceChangeListenerCache);
		when(myResourceChangeListenerCache.getListenerEntries(RESOURCE_NAME)).thenReturn(myEntries);
		when(myResourceChangeListenerCache.notifyListener(any(), any(), any())).thenReturn(new ResourceChangeResult());
	}

	@AfterEach
	public void after() {
		ResourceChangeListenerRegistryImpl.setNowForUnitTests(null);
	}

	@Test
	public void addingListenerForNonResourceFails() {
		try {
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Foo", myMap, myTestListener);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"Foo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void addingNonInMemorySearchParamFails() {
		try {
			mockInMemorySupported(InMemoryMatchResult.unsupportedFromReason("TEST REASON"));
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, myMap, myTestListener);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("SearchParameterMap SearchParameterMap[] cannot be evaluated in-memory: TEST REASON.  Only search parameter maps that can be evaluated in-memory may be registered.", e.getMessage());
		}
	}

	@Test
	public void addingListenerResetsTimer() {
		mockInMemorySupported(InMemoryMatchResult.successfulMatch());

		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, myMap, myTestListener);
		myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest(RESOURCE_NAME));

		// Add a second listener to reset the timer
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, myMap, mock(IResourceChangeListener.class));
		assertEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest(RESOURCE_NAME));
	}

	@Test
	public void doNotRefreshIfNotMatches() {
		mockInMemorySupported(InMemoryMatchResult.successfulMatch());

		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, myMap, mock(IResourceChangeListener.class));
		myResourceChangeListenerRegistry.forceRefresh(RESOURCE_NAME);
		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest(RESOURCE_NAME));

		Patient patient = new Patient();

		// Don't reset timer if it doesn't match any searchparams
		when(myResourceChangeListenerCache.hasListenerFor(patient)).thenReturn(false);
		myResourceChangeListenerRegistry.requestRefreshIfWatching(patient);

		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest(RESOURCE_NAME));

		// Reset timer if it does match searchparams
		when(myResourceChangeListenerCache.hasListenerFor(patient)).thenReturn(true);
		myResourceChangeListenerRegistry.requestRefreshIfWatching(patient);

		assertEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest(RESOURCE_NAME));
	}

	private void mockInMemorySupported(InMemoryMatchResult theTheInMemoryMatchResult) {
		when(myInMemoryResourceMatcher.checkIfInMemorySupported(myMap, ourFhirContext.getResourceDefinition(RESOURCE_NAME))).thenReturn(theTheInMemoryMatchResult);
	}

	/**
	 * This test assumes that  {@link ResourceChangeListenerRegistryImpl#REMOTE_REFRESH_INTERVAL_MS} is set to one minute.
	 * Adjust the times below if that changes.
 	 */
	@Test
	public void testSchedule() {
		mockInMemorySupported(InMemoryMatchResult.successfulMatch());

		resetMockCache();
		ResourceChangeListenerRegistryImpl.setNowForUnitTests("08:00:00");
		myResourceChangeListenerRegistry.refreshCacheIfNecessary(RESOURCE_NAME);
		verify(myResourceChangeListenerCache, times(1)).notifyListener(any(), any(), any());

		resetMockCache();
		ResourceChangeListenerRegistryImpl.setNowForUnitTests("08:00:01");
		myResourceChangeListenerRegistry.refreshCacheIfNecessary(RESOURCE_NAME);
		verify(myResourceChangeListenerCache, never()).notifyListener(any(), any(), any());

		resetMockCache();
		ResourceChangeListenerRegistryImpl.setNowForUnitTests("08:00:59");
		myResourceChangeListenerRegistry.refreshCacheIfNecessary(RESOURCE_NAME);
		verify(myResourceChangeListenerCache, never()).notifyListener(any(), any(), any());

		resetMockCache();
		ResourceChangeListenerRegistryImpl.setNowForUnitTests("08:01:00");
		myResourceChangeListenerRegistry.refreshCacheIfNecessary(RESOURCE_NAME);
		verify(myResourceChangeListenerCache, never()).notifyListener(any(), any(), any());

		resetMockCache();
		ResourceChangeListenerRegistryImpl.setNowForUnitTests("08:01:01");
		myResourceChangeListenerRegistry.refreshCacheIfNecessary(RESOURCE_NAME);
		verify(myResourceChangeListenerCache, times(1)).notifyListener(any(), any(), any());
	}
}
