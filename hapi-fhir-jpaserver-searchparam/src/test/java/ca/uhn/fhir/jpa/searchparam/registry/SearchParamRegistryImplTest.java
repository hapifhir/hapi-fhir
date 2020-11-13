package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.cache.IResourceVersionSvc;
import ca.uhn.fhir.jpa.cache.IVersionChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceVersionMap;
import ca.uhn.fhir.jpa.cache.VersionChangeListenerCache;
import ca.uhn.fhir.jpa.cache.VersionChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import static org.hamcrest.Matchers.empty;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SearchParamRegistryImplTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();
	private static final ReadOnlySearchParamCache ourBuiltInSearchParams = ReadOnlySearchParamCache.fromFhirContext(ourFhirContext);

	public static long TEST_SEARCH_PARAMS = 13L;
	private static List<ResourceTable> ourEntities;
	private static ResourceVersionMap ourResourceVersionMap;

	static {
		ourEntities = new ArrayList<>();
		for (long id = 0; id < TEST_SEARCH_PARAMS; ++id) {
			ourEntities.add(createEntity(id, 1));
		}
		ourResourceVersionMap = ResourceVersionMap.fromResourceIds(ourEntities);
	}

	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;
	@Autowired
	private IVersionChangeListenerRegistry myVersionChangeListenerRegistry;
	@Autowired
	private IResourceVersionSvc myResourceVersionSvc;

	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private ISearchParamProvider mySearchParamProvider;
	@MockBean
	private ModelConfig myModelConfig;
	@MockBean
	private IInterceptorService myInterceptorBroadcaster;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;

	@Configuration
	static class SpringConfig {
		@Bean
		FhirContext fhirContext() {
			return ourFhirContext;
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
		VersionChangeListenerCache versionChangeListenerCache() {
			return new VersionChangeListenerCache();
		}

		@Bean
		IResourceVersionSvc resourceVersionSvc() {
			IResourceVersionSvc retval = mock(IResourceVersionSvc.class);
			// FIXME KHS do we still need this here?
			when(retval.getVersionMap(anyString(), any())).thenReturn(ourResourceVersionMap);
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
		when(myResourceVersionSvc.getVersionMap(anyString(), any())).thenReturn(ourResourceVersionMap);
	}

	@AfterEach
	public void after() {
		myVersionChangeListenerRegistry.clearCacheForUnitTest();
	}

	@Test
	public void testBuildinSearchparams() {
		assertEquals(ourBuiltInSearchParams.size(), mySearchParamRegistry.getActiveSearchParams().size());
	}

	@Test
	public void testRefreshAfterExpiry() {
		mySearchParamRegistry.requestRefresh();
		assertEquals(TEST_SEARCH_PARAMS, myVersionChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter"));
		// Second time we don't need to run because we ran recently
		assertEquals(0, myVersionChangeListenerRegistry.refreshCacheIfNecessary("SearchParameter"));
	}

	@Test
	public void testRefreshCacheIfNecessary() {
		when(mySearchParamProvider.search(any())).thenReturn(new SimpleBundleProvider());

		mySearchParamRegistry.requestRefresh();

		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
		assertFalse(mySearchParamRegistry.refreshCacheIfNecessary());

		mySearchParamRegistry.requestRefresh();
		assertFalse(mySearchParamRegistry.refreshCacheIfNecessary());

		addNewSearchParameterToDatabase(Enumerations.PublicationStatus.ACTIVE);
		mySearchParamRegistry.requestRefresh();
		assertTrue(mySearchParamRegistry.refreshCacheIfNecessary());
	}

	@Test
	public void testGetActiveUniqueSearchParams_Empty() {
		assertThat(mySearchParamRegistry.getActiveUniqueSearchParams("Patient"), is(empty()));
	}

	@Test
	public void testGetActiveSearchParamsRetries() {
		AtomicBoolean retried = new AtomicBoolean(false);
		when(myResourceVersionSvc.getVersionMap(anyString(), any())).thenAnswer(t -> {
			if (myAnswerCount == 0) {
				myAnswerCount++;
				retried.set(true);
				throw new InternalErrorException("this is an error!");
			}

			return ourResourceVersionMap;
		});

		assertFalse(retried.get());
		mySearchParamRegistry.forceRefresh();
		Map<String, RuntimeSearchParam> activeSearchParams = mySearchParamRegistry.getActiveSearchParams("Patient");
		assertTrue(retried.get());
		assertEquals(ourBuiltInSearchParams.getSearchParamMap("Patient").size(), activeSearchParams.size());
	}

	@Test
	public void testAddActiveSearchparam() {
		// Initialize the registry
		mySearchParamRegistry.forceRefresh();

		addNewSearchParameterToDatabase(Enumerations.PublicationStatus.ACTIVE);

		mySearchParamRegistry.forceRefresh();
		Map<String, RuntimeSearchParam> outcome = mySearchParamRegistry.getActiveSearchParams("Patient");

		RuntimeSearchParam converted = outcome.get("foo");
		assertNotNull(converted);

		assertEquals(1, converted.getExtensions("http://foo").size());
		IPrimitiveType<?> value = (IPrimitiveType<?>) converted.getExtensions("http://foo").get(0).getValue();
		assertEquals("FOO", value.getValueAsString());
	}

	private void addNewSearchParameterToDatabase(Enumerations.PublicationStatus theActive) {
		// Add a new search parameter entity
		List<ResourceTable> newEntities = new ArrayList(ourEntities);
		newEntities.add(createEntity(TEST_SEARCH_PARAMS, 1));
		ResourceVersionMap resourceVersionMap = ResourceVersionMap.fromResourceIds(newEntities);
		when(myResourceVersionSvc.getVersionMap(anyString(), any())).thenReturn(resourceVersionMap);

		// When we ask for the new entity, return our foo search parameter
		when(mySearchParamProvider.read(any())).thenReturn(buildSearchParameter(theActive));
	}

	// FIXME KHS add tests

	@Nonnull
	private SearchParameter buildSearchParameter(Enumerations.PublicationStatus theStatus) {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setCode("foo");
		searchParameter.setStatus(theStatus);
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
