package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.annotation.Search;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.spec.IvParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class VersionChangeListenerCacheTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	VersionChangeListenerCache myVersionChangeListenerCache;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	private final SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
	static class SpringContext {
		@Bean
		VersionChangeListenerCache versionChangeListenerCache() {
			return new VersionChangeListenerCache();
		}

		@Bean
		FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@Test
	public void registerUnregister() {
		IVersionChangeListener listener1 = mock(IVersionChangeListener.class);
		myVersionChangeListenerCache.add("Patient", listener1, myMap);
		assertEquals(1, myVersionChangeListenerCache.size());

		IVersionChangeListener listener2 = mock(IVersionChangeListener.class);
		myVersionChangeListenerCache.add("Patient", listener2, myMap);
		assertEquals(2, myVersionChangeListenerCache.size());

		myVersionChangeListenerCache.remove(listener1);
		assertEquals(1, myVersionChangeListenerCache.size());
		myVersionChangeListenerCache.remove(listener2);
		assertEquals(0, myVersionChangeListenerCache.size());
	}
}
