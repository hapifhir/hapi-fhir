package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

public final class DaoTestUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoTestUtils.class);

	private DaoTestUtils() {}

    public static void assertConflictException(ResourceVersionConflictException e) {
        assertThat(e.getMessage(), matchesPattern(
            Msg.code(550) + Msg.code(515) + "Unable to delete [a-zA-Z]+/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource [a-zA-Z]+/[0-9]+ in path [a-zA-Z]+.[a-zA-Z]+"));
    }

	public static void logAllInterceptors(IInterceptorService theInterceptorRegistry) {
		List<Object> allInterceptors = theInterceptorRegistry.getAllRegisteredInterceptors();
		String interceptorList = allInterceptors
			.stream()
			.map(t -> t.getClass().toString())
			.sorted()
			.collect(Collectors.joining("\n * "));
		ourLog.info("Registered interceptors:\n * {}", interceptorList);
	}
}
