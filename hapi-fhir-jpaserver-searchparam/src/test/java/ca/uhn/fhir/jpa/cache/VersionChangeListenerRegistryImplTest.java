package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
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
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
	@MockBean
	private ListenerNotifier myListenerNotifier;
	@Autowired
	private FhirContext myFhirContext;

	private IVersionChangeListener myTestListener = new TestListener();
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
		} catch (IllegalArgumentException e) {
			assertEquals("'Foo' is not a valid resource type in " + ourFhirContext.getVersion().toString(), e.getMessage());
		}
	}

	@Test
	public void addingListenerResetsTimer() {
		myVersionChangeListenerRegistry.forceRefresh("Patient");
		assertEquals(Instant.MIN, myVersionChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));

		myVersionChangeListenerRegistry.registerResourceVersionChangeListener("Patient", myMap, myTestListener);
		myVersionChangeListenerRegistry.forceRefresh("Patient");
		assertNotEquals(Instant.MIN, myVersionChangeListenerRegistry.getNextRefreshTimeForUnitTest("Patient"));
	}

	private static class TestListener implements IVersionChangeListener {

		@Override
		public void handleCreate(IdDt theResourceId) {

		}

		@Override
		public void handleUpdate(IdDt theResourceId) {

		}

		@Override
		public void handleDelete(IdDt theResourceId) {

		}

		@Override
		public void handleInit(Collection<IdDt> theResourceIds) {

		}
	}
}
