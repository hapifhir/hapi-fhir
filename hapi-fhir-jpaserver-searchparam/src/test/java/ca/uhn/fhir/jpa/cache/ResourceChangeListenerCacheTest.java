package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

	@Autowired
	ResourceChangeListenerCache myResourceChangeListenerCache;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	private final SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
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
		myResourceChangeListenerCache.add(PATIENT_RESOURCE_NAME, listener1, myMap);
		myResourceChangeListenerCache.add(OBSERVATION_RESOURCE_NAME, listener1, myMap);

		when(mySearchParamMatcher.match(any(), any())).thenReturn(InMemoryMatchResult.successfulMatch());

		assertTrue(myResourceChangeListenerCache.hasListenerFor(new Patient()));
		assertTrue(myResourceChangeListenerCache.hasListenerFor(new Observation()));
		assertFalse(myResourceChangeListenerCache.hasListenerFor(new CarePlan()));

		assertEquals(2, myResourceChangeListenerCache.size());
		assertTrue(myResourceChangeListenerCache.hasEntriesForResourceName(PATIENT_RESOURCE_NAME));
		assertThat(myResourceChangeListenerCache.resourceNames(), containsInAnyOrder(PATIENT_RESOURCE_NAME, OBSERVATION_RESOURCE_NAME));

		IResourceChangeListener listener2 = mock(IResourceChangeListener.class);
		myResourceChangeListenerCache.add(PATIENT_RESOURCE_NAME, listener2, myMap);
		assertEquals(3, myResourceChangeListenerCache.size());

		Set<ResourceChangeListenerWithSearchParamMap> entries = myResourceChangeListenerCache.getListenerEntries(PATIENT_RESOURCE_NAME);
		assertThat(entries, hasSize(2));
		entries = myResourceChangeListenerCache.getListenerEntries(OBSERVATION_RESOURCE_NAME);
		assertThat(entries, hasSize(1));
		ResourceChangeListenerWithSearchParamMap entry = entries.iterator().next();
		assertEquals(listener1, entry.getResourceChangeListener());
		assertEquals(OBSERVATION_RESOURCE_NAME, entry.getResourceName());
		assertEquals(myMap, entry.getSearchParameterMap());

		myResourceChangeListenerCache.remove(listener1);
		assertEquals(1, myResourceChangeListenerCache.size());
		myResourceChangeListenerCache.remove(listener2);
		assertEquals(0, myResourceChangeListenerCache.size());
		assertFalse(myResourceChangeListenerCache.hasEntriesForResourceName(PATIENT_RESOURCE_NAME));
	}

	@Test
	public void testNotifyListenersEmptyEmptyNotInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		ResourceChangeListenerWithSearchParamMap entry = new ResourceChangeListenerWithSearchParamMap(PATIENT_RESOURCE_NAME, listener, myMap);
		ResourceVersionCache oldResourceVersionCache = new ResourceVersionCache();
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		assertFalse(entry.isInitialized());
		myResourceChangeListenerCache.notifyListener(entry, oldResourceVersionCache, newResourceVersionMap);
		assertTrue(entry.isInitialized());
		verify(listener, times(1)).handleInit(any());
	}

	@Test
	public void testNotifyListenersEmptyEmptyInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		ResourceChangeListenerWithSearchParamMap entry = new ResourceChangeListenerWithSearchParamMap(PATIENT_RESOURCE_NAME, listener, myMap);
		ResourceVersionCache oldResourceVersionCache = new ResourceVersionCache();
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		entry.setInitialized(true);
		assertTrue(entry.isInitialized());
		myResourceChangeListenerCache.notifyListener(entry, oldResourceVersionCache, newResourceVersionMap);
		assertTrue(entry.isInitialized());
		verifyNoInteractions(listener);
	}

}
