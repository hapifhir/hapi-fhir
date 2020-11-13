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
class ResourceChangeListenerRegistryInterceptorTest {
	@Autowired
	ResourceChangeListenerRegistryInterceptor myResourceChangeListenerRegistryInterceptor;

	@MockBean
	private IInterceptorService myInterceptorBroadcaster;
	@MockBean
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Configuration
	static class SpringContext {
		@Bean
		public ResourceChangeListenerRegistryInterceptor resourceChangeListenerRegistryInterceptor() {
			return new ResourceChangeListenerRegistryInterceptor();
		}
	}

	@Test
	public void testRefreshCalled() {
		Patient patient = new Patient();
		myResourceChangeListenerRegistryInterceptor.created(patient);
		verify(myResourceChangeListenerRegistry).requestRefreshIfWatching(patient);
	}
}
