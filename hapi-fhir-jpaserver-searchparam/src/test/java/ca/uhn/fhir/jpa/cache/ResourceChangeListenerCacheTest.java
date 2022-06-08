package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.cache.config.RegisteredResourceListenerFactoryConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RegisteredResourceListenerFactoryConfig.class)
class ResourceChangeListenerCacheTest {
	private static final String TEST_RESOURCE_NAME = "Foo";
	private static final long TEST_REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;
	private static final IResourceChangeListener ourListener = mock(IResourceChangeListener.class);
	private static final SearchParameterMap ourMap = SearchParameterMap.newSynchronous();
	private static final Patient ourPatient = new Patient();

	@Autowired
	private ResourceChangeListenerCacheFactory myResourceChangeListenerCacheFactory;

	@MockBean
	ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	@Test
	public void doNotRefreshIfNotMatches() {
		ResourceChangeListenerCache cache = myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(TEST_RESOURCE_NAME, ourMap, mock(IResourceChangeListener.class), TEST_REFRESH_INTERVAL);
		cache.forceRefresh();
		assertNotEquals(Instant.MIN, cache.getNextRefreshTimeForUnitTest());

		// Don't reset timer if it doesn't match any searchparams
		mockInMemorySupported(cache, InMemoryMatchResult.fromBoolean(false));
		cache.requestRefreshIfWatching(ourPatient);
		assertNotEquals(Instant.MIN, cache.getNextRefreshTimeForUnitTest());

		// Reset timer if it does match searchparams
		mockInMemorySupported(cache, InMemoryMatchResult.successfulMatch());
		cache.requestRefreshIfWatching(ourPatient);
		assertEquals(Instant.MIN, cache.getNextRefreshTimeForUnitTest());
	}

	private void mockInMemorySupported(ResourceChangeListenerCache thecache, InMemoryMatchResult theTheInMemoryMatchResult) {
		when(mySearchParamMatcher.match(thecache.getSearchParameterMap(), ourPatient)).thenReturn(theTheInMemoryMatchResult);
	}

	@Test
	public void testSchedule() {
		ResourceChangeListenerCache cache = myResourceChangeListenerCacheFactory.newResourceChangeListenerCache(TEST_RESOURCE_NAME, ourMap, ourListener, TEST_REFRESH_INTERVAL);
		ResourceChangeListenerCache.setNowForUnitTests("08:00:00");
		cache.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, times(1)).refreshCacheAndNotifyListener(any());

		reset(myResourceChangeListenerCacheRefresher);
		ResourceChangeListenerCache.setNowForUnitTests("08:00:01");
		cache.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheAndNotifyListener(any());

		reset(myResourceChangeListenerCacheRefresher);
		ResourceChangeListenerCache.setNowForUnitTests("08:59:59");
		cache.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheAndNotifyListener(any());


		reset(myResourceChangeListenerCacheRefresher);
		ResourceChangeListenerCache.setNowForUnitTests("09:00:00");
		cache.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheAndNotifyListener(any());

		reset(myResourceChangeListenerCacheRefresher);
		// Now that we passed TEST_REFRESH_INTERVAL, the cache should refresh
		ResourceChangeListenerCache.setNowForUnitTests("09:00:01");
		cache.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, times(1)).refreshCacheAndNotifyListener(any());
	}


}
