package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseSearchParamRegistryTest {

	@Mock
	private ISchedulerService mySchedulerService;
	@Mock
	private ISearchParamProvider mySearchParamProvider;

	@Test
	public void testRefreshAfterExpiry() {
		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());

		SearchParamRegistryR4 registry = new SearchParamRegistryR4();
		registry.setSchedulerServiceForUnitTest(mySchedulerService);
		registry.setFhirContextForUnitTest(FhirContext.forR4());
		registry.setSearchParamProviderForUnitTest(mySearchParamProvider);
		registry.postConstruct();

		registry.requestRefresh();
		assertEquals(146, registry.doRefresh(100000));

		// Second time we don't need to run because we ran recently
		assertEquals(0, registry.doRefresh(100000));

		assertEquals(146, registry.getActiveSearchParams().size());
	}

	@Test
	public void testRefreshCacheIfNeccessary() {
		SearchParamRegistryR4 registry = new SearchParamRegistryR4();

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t->{
			registry.doRefresh(t.getArgument(1, Long.class));
			return 0;
		});

		registry.setSchedulerServiceForUnitTest(mySchedulerService);
		registry.setFhirContextForUnitTest(FhirContext.forR4());
		registry.setSearchParamProviderForUnitTest(mySearchParamProvider);
		registry.postConstruct();
		registry.requestRefresh();

		assertTrue(registry.refreshCacheIfNecessary());
		assertFalse(registry.refreshCacheIfNecessary());
	}

}
