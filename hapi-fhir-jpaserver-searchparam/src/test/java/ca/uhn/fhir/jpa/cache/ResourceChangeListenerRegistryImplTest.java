package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.cache.config.RegisteredResourceListenerFactoryConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerRegistryImplTest {
	public static final String RESOURCE_NAME = "Patient";
	private static final long TEST_REFRESH_INTERVAL = DateUtils.MILLIS_PER_HOUR;
	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@Autowired
	RegisteredResourceListenerFactory myRegisteredResourceListenerFactory;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerCache myResourceChangeListenerCache;
	@MockBean
	private ResourceChangeListenerCacheRefresherImpl myResourceChangeListenerCacheRefresher;
	@MockBean
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	private SearchParamMatcher mySearchParamMatcher;

	private IResourceChangeListener myTestListener = mock(IResourceChangeListener.class);
	private SearchParameterMap myMap = SearchParameterMap.newSynchronous();
	private Set<RegisteredResourceChangeListener> myEntries;
	private RegisteredResourceChangeListener myEntry;

	@Configuration
	@Import(RegisteredResourceListenerFactoryConfig.class)
	static class SpringContext {
		@Bean
		public IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
			return new ResourceChangeListenerRegistryImpl();
		}

		@Bean
		public FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@BeforeEach
	public void before() {
		myEntries = new HashSet<>();
		myEntry = myRegisteredResourceListenerFactory.create(RESOURCE_NAME, myMap, myTestListener, TEST_REFRESH_INTERVAL);
		myEntries.add(myEntry);
	}


	@AfterEach
	public void after() {
		RegisteredResourceChangeListener.setNowForUnitTests(null);
	}

	@Test
	public void addingListenerForNonResourceFails() {
		try {
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Foo", myMap, myTestListener, TEST_REFRESH_INTERVAL);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"Foo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void addingNonInMemorySearchParamFails() {
		try {
			mockInMemorySupported(InMemoryMatchResult.unsupportedFromReason("TEST REASON"));
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener(RESOURCE_NAME, myMap, myTestListener, TEST_REFRESH_INTERVAL);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("SearchParameterMap SearchParameterMap[] cannot be evaluated in-memory: TEST REASON.  Only search parameter maps that can be evaluated in-memory may be registered.", e.getMessage());
		}
	}

	private void mockInMemorySupported(InMemoryMatchResult theTheInMemoryMatchResult) {
		when(myInMemoryResourceMatcher.checkIfInMemorySupported(myMap, ourFhirContext.getResourceDefinition(RESOURCE_NAME))).thenReturn(theTheInMemoryMatchResult);
	}
}
