package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.cache.config.RegisteredResourceListenerFactoryConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class ResourceChangeListenerCacheRefresherImplTest {
	public static final String PATIENT_RESOURCE_NAME = "Patient";
	private static final SearchParameterMap ourMap = SearchParameterMap.newSynchronous();
	private static final long TEST_REFRESH_INTERVAL_MS = DateUtils.MILLIS_PER_HOUR;

	@Autowired
	ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;

	@Configuration
	@Import(RegisteredResourceListenerFactoryConfig.class)
	static class SpringContext {
		@Bean
		IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
		}
	}

	@Test
	public void testNotifyListenersEmptyEmptyNotInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		ResourceChangeListenerCache cache = new ResourceChangeListenerCache(PATIENT_RESOURCE_NAME, listener, ourMap, TEST_REFRESH_INTERVAL_MS);
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		assertFalse(cache.isInitialized());
		myResourceChangeListenerCacheRefresher.notifyListener(cache, newResourceVersionMap);
		assertTrue(cache.isInitialized());
		verify(listener, times(1)).handleInit(any());
	}

	@Test
	public void testNotifyListenersEmptyEmptyInitialized() {
		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		ResourceChangeListenerCache cache = new ResourceChangeListenerCache(PATIENT_RESOURCE_NAME, listener, ourMap, TEST_REFRESH_INTERVAL_MS);
		ResourceVersionMap newResourceVersionMap = ResourceVersionMap.fromResourceTableEntities(Collections.emptyList());
		cache.setInitialized(true);
		assertTrue(cache.isInitialized());
		myResourceChangeListenerCacheRefresher.notifyListener(cache, newResourceVersionMap);
		assertTrue(cache.isInitialized());
		verifyNoInteractions(listener);
	}

	@Test
	public void testNoActionOnStoppingScheduler() {
		when(mySchedulerService.isStopping()).thenReturn(true);

		IResourceChangeListener listener = mock(IResourceChangeListener.class);
		ResourceChangeListenerCache cache = new ResourceChangeListenerCache(PATIENT_RESOURCE_NAME, listener, ourMap, TEST_REFRESH_INTERVAL_MS);
		myResourceChangeListenerCacheRefresher.refreshCacheAndNotifyListener(cache);

		verify(myResourceVersionSvc, times(0)).getVersionMap(any(), any());
	}

}
