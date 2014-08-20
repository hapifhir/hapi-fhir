package ca.uhn.fhir.rest.server.exception;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.internal.matchers.GreaterThan;

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ExceptionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionTest.class);

	@Test
	public void testExceptionsAreGood() throws Exception {
		ImmutableSet<ClassInfo> classes = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses(BaseServerResponseException.class.getPackage().getName());
		assertTrue(classes.size() > 5);

		for (ClassInfo classInfo : classes) {
			ourLog.info("Scanning {}", classInfo.getName());

			Class<?> next = Class.forName(classInfo.getName());
			assertNotNull(next);

			if (next == AuthenticationException.class) {
				continue;
			}
			if (next == BaseServerResponseException.class) {
				continue;
			}
			
			try {
				next.getConstructor(String.class, OperationOutcome.class);
			} catch (NoSuchMethodException e) {
				fail(classInfo.getName() + " has no constructor with params: (String, OperationOutcome)");
			}
		}

	}

}
