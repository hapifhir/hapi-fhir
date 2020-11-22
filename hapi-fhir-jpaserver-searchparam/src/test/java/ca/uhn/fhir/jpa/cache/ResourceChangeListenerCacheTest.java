package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.cache.config.RegisteredResourceListenerFactoryConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerCacheTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final String PATIENT_RESOURCE_NAME = "Patient";
	public static final String OBSERVATION_RESOURCE_NAME = "Observation";
	private static final long TEST_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_DAY;

	@Autowired
	ResourceChangeListenerCache myResourceChangeListenerCache;

	@MockBean
	ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	private final SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
	@Import(RegisteredResourceListenerFactoryConfig.class)
	static class SpringContext {
		@Bean
		ResourceChangeListenerCache resourceChangeListenerCache() {
			return new ResourceChangeListenerCache();
		}

		@Bean
		FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@AfterEach
	public void after() {
		myResourceChangeListenerCache.clearListenersForUnitTest();
	}

	@Test
	public void registerUnregister() {
		IResourceChangeListener listener1 = mock(IResourceChangeListener.class);
		myResourceChangeListenerCache.add(PATIENT_RESOURCE_NAME, listener1, myMap, TEST_REFRESH_INTERVAL_MS);
		myResourceChangeListenerCache.add(OBSERVATION_RESOURCE_NAME, listener1, myMap, TEST_REFRESH_INTERVAL_MS);

		when(mySearchParamMatcher.match(any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		assertEquals(2, myResourceChangeListenerCache.size());

		IResourceChangeListener listener2 = mock(IResourceChangeListener.class);
		myResourceChangeListenerCache.add(PATIENT_RESOURCE_NAME, listener2, myMap, TEST_REFRESH_INTERVAL_MS);
		assertEquals(3, myResourceChangeListenerCache.size());

		List<RegisteredResourceChangeListener> entries = Lists.newArrayList(myResourceChangeListenerCache.iterator());
		assertThat(entries, hasSize(3));

		List<IResourceChangeListener> listeners = entries.stream().map(RegisteredResourceChangeListener::getResourceChangeListener).collect(Collectors.toList());
		assertThat(listeners, contains(listener1, listener1, listener2));

		List<String> resourceNames = entries.stream().map(RegisteredResourceChangeListener::getResourceName).collect(Collectors.toList());
		assertThat(resourceNames, contains(PATIENT_RESOURCE_NAME, OBSERVATION_RESOURCE_NAME, PATIENT_RESOURCE_NAME));

		RegisteredResourceChangeListener firstEntry = entries.iterator().next();
		assertEquals(myMap, firstEntry.getSearchParameterMap());

		myResourceChangeListenerCache.remove(listener1);
		assertEquals(1, myResourceChangeListenerCache.size());
		RegisteredResourceChangeListener entry = myResourceChangeListenerCache.iterator().next();
		assertEquals(PATIENT_RESOURCE_NAME, entry.getResourceName());
		assertEquals(listener2, entry.getResourceChangeListener());
		myResourceChangeListenerCache.remove(listener2);
		assertEquals(0, myResourceChangeListenerCache.size());
	}

	@Test
	public void testNotifyListenersEmptyEmptyNotInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		RegisteredResourceChangeListener entry = new RegisteredResourceChangeListener(PATIENT_RESOURCE_NAME, listener, myMap, TEST_REFRESH_INTERVAL_MS);
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		assertFalse(entry.isInitialized());
		myResourceChangeListenerCache.notifyListener(entry, newResourceVersionMap);
		assertTrue(entry.isInitialized());
		verify(listener, times(1)).handleInit(any());
	}

	@Test
	public void testNotifyListenersEmptyEmptyInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		RegisteredResourceChangeListener entry = new RegisteredResourceChangeListener(PATIENT_RESOURCE_NAME, listener, myMap, TEST_REFRESH_INTERVAL_MS);
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		entry.setInitialized(true);
		assertTrue(entry.isInitialized());
		myResourceChangeListenerCache.notifyListener(entry, newResourceVersionMap);
		assertTrue(entry.isInitialized());
		verifyNoInteractions(listener);
	}

}
