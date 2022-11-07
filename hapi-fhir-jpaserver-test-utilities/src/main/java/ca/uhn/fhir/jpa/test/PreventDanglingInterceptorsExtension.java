package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test extension makes sure that tests don't leave any interceptors
 * registered that weren't there before they started.
 */
public class PreventDanglingInterceptorsExtension implements BeforeEachCallback, AfterEachCallback {

	private static final Logger ourLog = LoggerFactory.getLogger(PreventDanglingInterceptorsExtension.class);
	private final Supplier<IInterceptorService> myInterceptorServiceSuplier;
	private List<Object> myBeforeInterceptors;

	public PreventDanglingInterceptorsExtension(Supplier<IInterceptorService> theInterceptorServiceSuplier) {
		myInterceptorServiceSuplier = theInterceptorServiceSuplier;
	}

	@Override
	public void beforeEach(ExtensionContext theExtensionContext) throws Exception {
		myBeforeInterceptors = myInterceptorServiceSuplier.get().getAllRegisteredInterceptors();

		ourLog.info("Registered interceptors:\n * " + myBeforeInterceptors.stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
	}

	@Override
	public void afterEach(ExtensionContext theExtensionContext) throws Exception {
		List<Object> afterInterceptors = myInterceptorServiceSuplier.get().getAllRegisteredInterceptors();
		Map<Object, Object> delta = new IdentityHashMap<>();
		afterInterceptors.forEach(t -> delta.put(t, t));
		myBeforeInterceptors.forEach(t -> delta.remove(t));
		delta.keySet().forEach(t->myInterceptorServiceSuplier.get().unregisterInterceptor(t));
		assertTrue(delta.isEmpty(), () -> "Test added interceptor(s) and did not clean them up:\n * " + delta.keySet().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));

	}
}
