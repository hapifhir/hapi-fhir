package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerRegistryImplTest {
	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	ResourceChangeListenerRegistryImpl myResourceChangeListenerRegistry;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@MockBean
	private ResourceChangeListenerCache myResourceChangeListenerCache;

	private IResourceChangeListener myTestListener = mock(IResourceChangeListener.class);
	private SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
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
		// FIXME KHS move to IT
//		myFemaleMap = new SearchParameterMap();
//		myFemaleMap.setLoadSynchronous(true);
//		myFemaleMap.add("gender", new TokenParam("female"));
		when(myResourceVersionSvc.getVersionMap("Patient", myMap)).thenReturn(ResourceVersionMap.fromResourceIds(new ArrayList<>()));
		Set<ResourceChangeListenerWithSearchParamMap> entries = new HashSet<>();
		ResourceChangeListenerWithSearchParamMap entry = new ResourceChangeListenerWithSearchParamMap(myTestListener, myMap);
		entries.add(entry);
		when(myResourceChangeListenerCache.getListenerEntries("Patient")).thenReturn(entries);
		when(myResourceChangeListenerCache.notifyListener(any(), any(), any())).thenReturn(new ResourceChangeResult());
	}

	@Test
	public void addingListenerForNonResourceFails() {
		try {
			myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Foo", myMap, myTestListener);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"Foo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void addingListenerResetsTimer() {
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Patient", myMap, myTestListener);
		myResourceChangeListenerRegistry.forceRefresh("Patient");
		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		// Add a second listener to reset the timer
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Patient", myMap, mock(IResourceChangeListener.class));
		assertEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));
	}

	@Test
	public void doNotRefreshIfNotMatches() {
		// FIXME KHS create IT version of this test that tests with actual searchparams
		myResourceChangeListenerRegistry.registerResourceResourceChangeListener("Patient", myMap, mock(IResourceChangeListener.class));
		myResourceChangeListenerRegistry.forceRefresh("Patient");
		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		Patient patient = new Patient();

		// Don't reset timer if it doesn't match any searchparams
		when(myResourceChangeListenerCache.hasListenerFor(patient)).thenReturn(false);
		myResourceChangeListenerRegistry.requestRefreshIfWatching(patient);

		assertNotEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		// Reset timer if it does match searchparams
		when(myResourceChangeListenerCache.hasListenerFor(patient)).thenReturn(true);
		myResourceChangeListenerRegistry.requestRefreshIfWatching(patient);

		assertEquals(Instant.MIN, myResourceChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));
	}
}
