package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class VersionChangeListenerRegistryImplTest {
	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	VersionChangeListenerRegistryImpl myVersionChangeListenerRegistry;
	@MockBean
	private IInterceptorService myInterceptorBroadcaster;
	@MockBean
	private ISchedulerService mySchedulerService;
	@MockBean
	private IResourceVersionSvc myResourceVersionSvc;
	@Autowired
	private FhirContext myFhirContext;

	private IVersionChangeListener myTestListener = mock(IVersionChangeListener.class);
	private SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
	static class SpringContext {
		@Bean
		public IVersionChangeListenerRegistry versionChangeListenerRegistry() {
			return new VersionChangeListenerRegistryImpl();
		}
		@Bean
		public FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@BeforeEach
	public void before() {
		when(myResourceVersionSvc.getVersionMap("Patient", myMap)).thenReturn(ResourceVersionMap.fromResourceIds(new ArrayList<>()));
	}

	@Test
	public void addingListenerForNonResourceFails() {
		try {
			myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Foo", myMap, myTestListener);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"Foo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void addingListenerResetsTimer() {
		myVersionChangeListenerRegistry.forceRefresh("Patient");
		assertEquals(Instant.MIN, myVersionChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Patient", myMap, myTestListener);
		myVersionChangeListenerRegistry.forceRefresh("Patient");
		assertNotEquals(Instant.MIN, myVersionChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		// Add a second listener to reset the timer
		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Patient", myMap, mock(IVersionChangeListener.class));
		assertEquals(Instant.MIN, myVersionChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));
	}
}
