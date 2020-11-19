package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class ResourceChangeListenerCacheTest {
	private static final FhirContext ourFhirContext = FhirContext.forR4();

	@Autowired
	ResourceChangeListenerCache myResourceChangeListenerCache;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;

	private final SearchParameterMap myMap = SearchParameterMap.newSynchronous();

	@Configuration
	static class SpringContext {
		@Bean
		ResourceChangeListenerCache resourceChangeListenerCache() {
			return new ResourceChangeListenerCache();
		}

		@Bean
		FhirContext fhirContext() {
			return ourFhirContext;
		}
	}

	@Test
	public void registerUnregister() {
		IResourceChangeListener listener1 = mock(IResourceChangeListener.class);
		myResourceChangeListenerCache.add("Patient", listener1, myMap);
		assertEquals(1, myResourceChangeListenerCache.size());

		IResourceChangeListener listener2 = mock(IResourceChangeListener.class);
		myResourceChangeListenerCache.add("Patient", listener2, myMap);
		assertEquals(2, myResourceChangeListenerCache.size());

		myResourceChangeListenerCache.remove(listener1);
		assertEquals(1, myResourceChangeListenerCache.size());
		myResourceChangeListenerCache.remove(listener2);
		assertEquals(0, myResourceChangeListenerCache.size());
	}

	@Test
	public void testCompareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges() {
		// FIXME KHS Add Test Code Here ...
	}
}
