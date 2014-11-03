package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;

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

			if (next == getClass()) {
				continue;
			}
			if (next == BaseServerResponseException.class) {
				continue;
			}
			if (next == UnclassifiedServerFailureException.class) {
				continue;
			}
			
			assertTrue(BaseServerResponseException.isExceptionTypeRegistered(next));
			
			if (next == AuthenticationException.class) {
				continue;
			}
			
			try {
				next.getConstructor(String.class, BaseOperationOutcome.class);
			} catch (NoSuchMethodException e) {
				fail(classInfo.getName() + " has no constructor with params: (String, OperationOutcome)");
			}
		}

	}

}
