package ca.uhn.fhir.jpa.searchparam.interceptor;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

import static javolution.testing.TestContext.assertTrue;

@Component
public class InterceptorRegistry {
	private final Map<String, List<Predicate<Object>>> interceptorMap = new HashMap<>();

	public void addInterceptor(String key, Predicate<Object> interceptor) {
		interceptorMap.computeIfAbsent(key, entry -> new ArrayList<>()).add(interceptor);
	}

	public void removeInterceptor(String key, Predicate<Object> interceptor) {
		assertTrue(interceptorMap.get(key).remove(interceptor));
	}

	// TODO KHS this feels like it should be a one-line lambda
	public Boolean trigger(String key, Object object) {
		List<Predicate<Object>> predicates = interceptorMap.get(key);
		if (predicates != null) {
			for (Predicate<Object> predicate : predicates) {
				if (!predicate.test(object)) {
					return false;
				}
			}
		}
		return true;
	}
}
