package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.Assert.*;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ExceptionPropertiesTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionPropertiesTest.class);

	@SuppressWarnings("deprecation")
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
			if (next == ResourceVersionNotSpecifiedException.class) {
				// This one is deprocated
				continue;
			}
			
			assertTrue("Type " + next + " is not registered", BaseServerResponseException.isExceptionTypeRegistered(next));
			
			if (next == AuthenticationException.class) {
				continue;
			}
			
			try {
				next.getConstructor(String.class, IBaseOperationOutcome.class);
			} catch (NoSuchMethodException e) {
				fail(classInfo.getName() + " has no constructor with params: (String, IBaseOperationOutcome)");
			}
		}

	}

}
