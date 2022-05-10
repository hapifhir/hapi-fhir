package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.cache.config.RegisteredResourceListenerFactoryConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.parser.DataFormatException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerRegistryImplTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final String PATIENT_RESOURCE_NAME = "Patient";
	public static final String OBSERVATION_RESOURCE_NAME = "Observation";
	private static final long TEST_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_HOUR;

	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@Autowired
	ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;

	private final IResourceChangeListener myTestListener = mock(IResourceChangeListener.class);
	private static final SearchParameterMap ourMap = SearchParameterMap.newSynchronous();

	@Configuration
	@Import(RegisteredResourceListenerFactoryConfig.class)
	static class SpringContext {
		@Autowired
		ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;

		@Bean
		public IResourceChangeListenerRegistry resourceChangeListenerRegistry(InMemoryResourceMatcher theInMemoryResourceMatcher) {
			return new ResourceChangeListenerRegistryImpl(ourFhirContext, myResourceChangeListenerCacheFactory, theInMemoryResourceMatcher);
		}

		@Bean
		public FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@BeforeEach
	public void before() {
		Set<IResourceChangeListenerCache> entries = new HashSet<>();
		IResourceChangeListenerCache cache = myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(PATIENT_RESOURCE_NAME, ourMap, myTestListener, TEST_REFRESH_INTERVAL_MS);
		entries.add(cache);
		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());
	}

	@Test
	public void addingListenerForNonResourceFails() {
		try {
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Foo", ourMap, myTestListener, TEST_REFRESH_INTERVAL_MS);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1684) + "Unknown resource name \"Foo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void addingNonInMemorySearchParamFails() {
		try {
			mockInMemorySupported(InMemoryMatchResult.unsupportedFromReason("TEST REASON"));
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener(PATIENT_RESOURCE_NAME, ourMap, myTestListener, TEST_REFRESH_INTERVAL_MS);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(482) + "SearchParameterMap SearchParameterMap[] cannot be evaluated in-memory: TEST REASON.  Only search parameter maps that can be evaluated in-memory may be registered.", e.getMessage());
		}
	}

	private void mockInMemorySupported(InMemoryMatchResult theTheInMemoryMatchResult) {
		when(myInMemoryResourceMatcher.canBeEvaluatedInMemory(ourMap, ourFhirContext.getResourceDefinition(PATIENT_RESOURCE_NAME))).thenReturn(theTheInMemoryMatchResult);
	}

	@AfterEach
	public void after() {
		myResourceChangeListenerRegistry.clearListenersForUnitTest();
			ResourceChangeListenerCache.setNowForUnitTests(null);
	}

	@Test
	public void registerUnregister() {
		IResourceChangeListener listener1 = mock(IResourceChangeListener.class);
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(PATIENT_RESOURCE_NAME, ourMap, listener1, TEST_REFRESH_INTERVAL_MS);
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(OBSERVATION_RESOURCE_NAME, ourMap, listener1, TEST_REFRESH_INTERVAL_MS);

		when(mySearchParamMatcher.match(any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		assertEquals(2, myResourceChangeListenerRegistry.size());

		IResourceChangeListener listener2 = mock(IResourceChangeListener.class);
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener(PATIENT_RESOURCE_NAME, ourMap, listener2, TEST_REFRESH_INTERVAL_MS);
		assertEquals(3, myResourceChangeListenerRegistry.size());

		List<ResourceChangeListenerCache> entries = Lists.newArrayList(myResourceChangeListenerRegistry.iterator());
		assertThat(entries, hasSize(3));

		List<IResourceChangeListener> listeners = entries.stream().map(ResourceChangeListenerCache::getResourceChangeListener).collect(Collectors.toList());
		assertThat(listeners, contains(listener1, listener1, listener2));

		List<String> resourceNames = entries.stream().map(IResourceChangeListenerCache::getResourceName).collect(Collectors.toList());
		assertThat(resourceNames, contains(PATIENT_RESOURCE_NAME, OBSERVATION_RESOURCE_NAME, PATIENT_RESOURCE_NAME));

		IResourceChangeListenerCache firstcache = entries.iterator().next();
		// We made a copy
		assertTrue(ourMap != firstcache.getSearchParameterMap());

		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(listener1);
		assertEquals(1, myResourceChangeListenerRegistry.size());
		ResourceChangeListenerCache cache = myResourceChangeListenerRegistry.iterator().next();
		assertEquals(PATIENT_RESOURCE_NAME, cache.getResourceName());
		assertEquals(listener2, cache.getResourceChangeListener());
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(listener2);
		assertEquals(0, myResourceChangeListenerRegistry.size());
	}
}
