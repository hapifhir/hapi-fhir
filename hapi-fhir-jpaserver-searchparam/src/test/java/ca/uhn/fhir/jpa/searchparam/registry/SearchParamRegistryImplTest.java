package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchParamRegistryImplTest {

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

		SearchParamRegistryImpl registry = new SearchParamRegistryImpl();
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
		SearchParamRegistryImpl registry = new SearchParamRegistryImpl();

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
		SearchParamRegistryImpl registry = new SearchParamRegistryImpl();
		assertThat(registry.getActiveUniqueSearchParams("Patient"), Matchers.empty());
	}

	@Test
	public void testGetActiveSearchParams() {
		SearchParamRegistryImpl registry = new SearchParamRegistryImpl();
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

	@Test
	public void testExtractExtensions() {
		SearchParamRegistryImpl registry = new SearchParamRegistryImpl();
		registry.setFhirContextForUnitTest(FhirContext.forR4());
		registry.postConstruct();

		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setCode("foo");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("Patient.name");
		searchParameter.addBase("Patient");
		searchParameter.addExtension("http://foo", new StringType("FOO"));
		searchParameter.addExtension("http://bar", new StringType("BAR"));

		// Invalid entries
		searchParameter.addExtension("http://bar", null);
		searchParameter.addExtension(null, new StringType("BAR"));

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider(searchParameter));
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			registry.doRefresh(0);
			return 0;
		});

		registry.setSearchParamProviderForUnitTest(mySearchParamProvider);
		Map<String, RuntimeSearchParam> outcome = registry.getActiveSearchParams("Patient");

		RuntimeSearchParam converted = outcome.get("foo");
		assertNotNull(converted);

		assertEquals(1, converted.getExtensions("http://foo").size());
		IPrimitiveType<?> value = (IPrimitiveType<?>) converted.getExtensions("http://foo").get(0).getValue();
		assertEquals("FOO", value.getValueAsString());

	}

}
