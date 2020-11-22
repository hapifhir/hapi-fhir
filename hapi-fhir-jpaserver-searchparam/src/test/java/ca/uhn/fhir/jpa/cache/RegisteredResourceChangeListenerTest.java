package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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
class RegisteredResourceChangeListenerTest {
	private static final String TEST_RESOURCE_NAME = "Foo";
	private static final long TEST_REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;
	private static final IResourceChangeListener ourListener = mock(IResourceChangeListener.class);
	private static final SearchParameterMap ourMap = SearchParameterMap.newSynchronous();
	private static final Patient ourPatient = new Patient();

	@Autowired
	private RegisteredResourceListenerFactory myRegisteredResourceListenerFactory;

	@MockBean
	ResourceChangeListenerCacheRefresher myResourceChangeListenerCacheRefresher;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	@Configuration
	static class SpringContext {
		@Bean
		RegisteredResourceListenerFactory registeredResourceListenerFactory() {
			return new RegisteredResourceListenerFactory();
		}
		@Bean
		@Scope("prototype")
		RegisteredResourceChangeListener registeredResourceChangeListener(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
			return new RegisteredResourceChangeListener(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
		}
	}

	@Test
	public void doNotRefreshIfNotMatches() {
		RegisteredResourceChangeListener entry = myRegisteredResourceListenerFactory.create(TEST_RESOURCE_NAME, ourMap, mock(IResourceChangeListener.class), TEST_REFRESH_INTERVAL);
		entry.forceRefresh();
		assertNotEquals(Instant.MIN, entry.getNextRefreshTimeForUnitTest());

		// Don't reset timer if it doesn't match any searchparams
		mockInMemorySupported(InMemoryMatchResult.fromBoolean(false));
		entry.requestRefreshIfWatching(ourPatient);
		assertNotEquals(Instant.MIN, entry.getNextRefreshTimeForUnitTest());

		// Reset timer if it does match searchparams
		mockInMemorySupported(InMemoryMatchResult.successfulMatch());
		entry.requestRefreshIfWatching(ourPatient);
		assertEquals(Instant.MIN, entry.getNextRefreshTimeForUnitTest());
	}

	private void mockInMemorySupported(InMemoryMatchResult theTheInMemoryMatchResult) {
		when(mySearchParamMatcher.match(ourMap, ourPatient)).thenReturn(theTheInMemoryMatchResult);
	}

	@Test
	public void testSchedule() {
		RegisteredResourceChangeListener entry = myRegisteredResourceListenerFactory.create(TEST_RESOURCE_NAME, ourMap, ourListener, TEST_REFRESH_INTERVAL);
		RegisteredResourceChangeListener.setNowForUnitTests("08:00:00");
		entry.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, times(1)).refreshCacheWithRetry(any());

		reset(myResourceChangeListenerCacheRefresher);
		RegisteredResourceChangeListener.setNowForUnitTests("08:00:01");
		entry.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheWithRetry(any());

		reset(myResourceChangeListenerCacheRefresher);
		RegisteredResourceChangeListener.setNowForUnitTests("08:59:59");
		entry.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheWithRetry(any());


		reset(myResourceChangeListenerCacheRefresher);
		RegisteredResourceChangeListener.setNowForUnitTests("09:00:00");
		entry.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, never()).refreshCacheWithRetry(any());

		reset(myResourceChangeListenerCacheRefresher);
		// Now that we passed TEST_REFRESH_INTERVAL, the cache should refresh
		RegisteredResourceChangeListener.setNowForUnitTests("09:00:01");
		entry.refreshCacheIfNecessary();
		verify(myResourceChangeListenerCacheRefresher, times(1)).refreshCacheWithRetry(any());
	}


}
