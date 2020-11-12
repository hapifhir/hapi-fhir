package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.IVersionChangeListener;
import ca.uhn.fhir.jpa.cache.IVersionChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceVersionMap;
import ca.uhn.fhir.jpa.cache.VersionChangeListenerCache;
import ca.uhn.fhir.jpa.cache.VersionChangeListenerEntry;
import ca.uhn.fhir.jpa.cache.VersionChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SearchParamRegistryImplTest {
	public static long TEST_SEARCH_PARAMS = 13L;
	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	private IVersionChangeListenerRegistry myVersionChangeListenerRegistry;

	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private ISearchParamProvider mySearchParamProvider;
	@MockBean
	private ModelConfig myModelConfig;
	@MockBean
	private IInterceptorService myInterceptorBroadcaster;
	@MockBean
	private VersionChangeListenerCache myVersionChangeListenerCache;

	@Configuration
	static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean
		ISearchParamRegistry searchParamRegistry() {
			return new SearchParamRegistryImpl();
		}

		@Bean
		SearchParameterCanonicalizer searchParameterCanonicalizer(FhirContext theFhirContext) {
			return new SearchParameterCanonicalizer(theFhirContext);
		}

		@Bean
		IVersionChangeListenerRegistry versionChangeListenerRegistry() {
			return new VersionChangeListenerRegistryImpl();
		}

		@Bean
		IResourceVersionSvc resourceVersionSvc() {
			IResourceVersionSvc retval = mock(IResourceVersionSvc.class);

			List<ResourceTable> entities = new ArrayList<>();
			for (long id = 0; id < TEST_SEARCH_PARAMS; ++id) {
				entities.add(createEntity(id, 1));
			}
			ResourceVersionMap resourceVersionMap = ResourceVersionMap.fromResourceIds(entities);
			when(retval.getVersionMap(anyString(), any())).thenReturn(resourceVersionMap);
			return retval;
		}
	}

	@Nonnull
	private static ResourceTable createEntity(long theId, int theVersion) {
		ResourceTable searchParamEntity = new ResourceTable();
		searchParamEntity.setResourceType("SearchParameter");
		searchParamEntity.setId(theId);
		searchParamEntity.setVersion(theVersion);
		return searchParamEntity;
	}

	private int myAnswerCount = 0;

	@BeforeEach
	public void before() {
		myAnswerCount = 0;

		Set<VersionChangeListenerEntry> entries = new HashSet<>();
		VersionChangeListenerEntry entry = new VersionChangeListenerEntry(mock(IVersionChangeListener.class), SearchParameterMap.newSynchronous());
		entries.add(entry);
		when(myVersionChangeListenerCache.getListenerEntries("SearchParameter")).thenReturn(entries);
		when(myVersionChangeListenerCache.notifyListener(any(), any(), any())).thenReturn(TEST_SEARCH_PARAMS);

	}

	@Test
	public void testRefreshAfterExpiry() {
//		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());

		mySearchParamRegistry.requestRefresh();
		assertEquals(TEST_SEARCH_PARAMS, myVersionChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter"));
		// Second time we don't need to run because we ran recently
		assertEquals(0, myVersionChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter"));

		assertEquals(146, mySearchParamRegistry.getActiveSearchParams().size());
	}

	@Test
	public void testRefreshCacheIfNecessary() {

		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());

		mySearchParamRegistry.requestRefresh();

		// FIXME KHS find suitable replacement asserts
//		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
//		assertFalse(mySearchParamRegistry.refreshCacheIfNecessary());

		mySearchParamRegistry.requestRefresh();
//		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
	}

	@Test
	public void testGetActiveUniqueSearchParams_Empty() {
		assertThat(mySearchParamRegistry.getActiveUniqueSearchParams("Patient"), Matchers.empty());
	}

	@Test
	public void testGetActiveSearchParams() {
		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());
		when(mySearchParamProvider.refreshCache(any(), anyLong())).thenAnswer(t -> {
			if (myAnswerCount == 0) {
				myAnswerCount++;
				throw new InternalErrorException("this is an error!");
			}

			return 0;
		});

		Map<String, RuntimeSearchParam> outcome = mySearchParamRegistry.getActiveSearchParams("Patient");
	}

	@Test
	public void testExtractExtensions() {
		SearchParameter searchParameter = buildSearchParameter();

		when(mySearchParamProvider.read(any())).thenReturn(searchParameter);
		mySearchParamRegistry.forceRefresh();
		Map<String, RuntimeSearchParam> outcome = mySearchParamRegistry.getActiveSearchParams("Patient");

		RuntimeSearchParam converted = outcome.get("foo");
		assertNotNull(converted);

		assertEquals(1, converted.getExtensions("http://foo").size());
		IPrimitiveType<?> value = (IPrimitiveType<?>) converted.getExtensions("http://foo").get(0).getValue();
		assertEquals("FOO", value.getValueAsString());
	}

	// FIXME KHS add tests

	@Nonnull
	private SearchParameter buildSearchParameter() {
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
		return searchParameter;
	}

}
