package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseSearchParamRegistryTest {

	@Mock
	private ISchedulerService mySchedulerService;
	@Mock
	private ISearchParamProvider mySearchParamProvider;
	private int myAnswerCount = 0;


	@Before
	public void before() {
		myAnswerCount = 0;
	}

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
	public void testRefreshCacheIfNecessary() {
		SearchParamRegistryR4 registry = new SearchParamRegistryR4();

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
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

		registry.requestRefresh();
		assertTrue(registry.refreshCacheIfNecessary());
	}

	@Test
	public void testGetActiveUniqueSearchParams_Empty() {
		SearchParamRegistryR4 registry = new SearchParamRegistryR4();
		assertThat(registry.getActiveUniqueSearchParams("Patient"), Matchers.empty());
	}

	@Test
	public void testGetActiveSearchParams() {
		SearchParamRegistryR4 registry = new SearchParamRegistryR4();
		registry.setFhirContextForUnitTest(FhirContext.forR4());
		registry.postConstruct();

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			if (myAnswerCount == 0) {
				myAnswerCount++;
				throw new InternalErrorException("this is an error!");
			}

			registry.doRefresh(0);

			return 0;
		});

		registry.setSearchParamProviderForUnitTest(mySearchParamProvider);
		Map<String, RuntimeSearchParam> outcome = registry.getActiveSearchParams("Patient");
	}

}
