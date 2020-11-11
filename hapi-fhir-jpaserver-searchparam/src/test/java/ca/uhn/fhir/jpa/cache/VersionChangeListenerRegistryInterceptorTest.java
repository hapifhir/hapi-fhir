package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class VersionChangeListenerRegistryInterceptorTest {
	@Autowired
	VersionChangeListenerRegistryInterceptor myVersionChangeListenerRegistryInterceptor;

	@MockBean
	private IInterceptorService myInterceptorBroadcaster;
	@MockBean
	private IVersionChangeListenerRegistry myVersionChangeListenerRegistry;

	@Configuration
	static class SpringContext {
		@Bean
		public VersionChangeListenerRegistryInterceptor versionChangeListenerRegistryInterceptor() {
			return new VersionChangeListenerRegistryInterceptor();
		}
	}

	@Test
	public void testRefreshCalled() {
		Patient patient = new Patient();
		myVersionChangeListenerRegistryInterceptor.created(patient);
		verify(myVersionChangeListenerRegistry).requestRefreshIfWatching(patient);
	}
}
