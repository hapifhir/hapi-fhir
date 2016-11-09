package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.Assert.*;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Practitioner;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.util.TestUtil;

public class ExceptionPropertiesTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionPropertiesTest.class);

	@Test
	public void testConstructors() {
		new FhirClientConnectionException("");
		new FhirClientConnectionException("", new Exception());
		new FhirClientConnectionException(new Exception());
		new NotImplementedOperationException("");
		new NotImplementedOperationException(null, new OperationOutcome());
		new FhirClientInappropriateForServerException(new Exception());
		new FhirClientInappropriateForServerException("", new Exception());
		
		assertEquals("Resource Patient/123 is gone/deleted", new ResourceGoneException(new IdDt("Patient/123")).getMessage());
		assertEquals("FOO", new ResourceGoneException("FOO", new OperationOutcome()).getMessage());
		assertEquals("Resource of type Practitioner with ID Patient/123 is gone/deleted", new ResourceGoneException(Practitioner.class, new IdDt("Patient/123")).getMessage());
	}
	
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


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
