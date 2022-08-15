package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ExceptionPropertiesTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExceptionPropertiesTest.class);

	@Test
	public void testConstructors() {
		test(new FhirClientConnectionException(""));
		test(new FhirClientConnectionException("", new Exception()));
		test(new FhirClientConnectionException(new Exception()));
		test(new NotImplementedOperationException(""));
		test(new NotImplementedOperationException(null, new OperationOutcome()));
		test(new FhirClientInappropriateForServerException(new Exception()));
		test(new FhirClientInappropriateForServerException("", new Exception()));

		assertEquals("Resource Patient/123 is gone/deleted", new ResourceGoneException(new IdDt("Patient/123")).getMessage());
		assertEquals("FOO", new ResourceGoneException("FOO", new OperationOutcome()).getMessage());
		assertEquals("Resource of type Practitioner with ID Patient/123 is gone/deleted", new ResourceGoneException(Practitioner.class, new IdType("Patient/123")).getMessage());
	}

	private void test(Exception theE) {
		try {
			throw theE;
		} catch (Exception e) {
			// good
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testExceptionsAreGood() throws Exception {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(BaseServerResponseException.class));
		Set<BeanDefinition> classes = scanner.findCandidateComponents(BaseServerResponseException.class.getPackage().getName());
		assertTrue(classes.size() > 5, classes.toString());

		for (BeanDefinition classInfo : classes) {
			ourLog.info("Scanning {}", classInfo.getBeanClassName());

			Class<?> next = Class.forName(classInfo.getBeanClassName());
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
				// This one is deprecated
				continue;
			}

			assertTrue(BaseServerResponseException.isExceptionTypeRegistered(next), "Type " + next + " is not registered");

			if (next == AuthenticationException.class) {
				continue;
			}

			try {
				assertNotNull(next.getConstructor(String.class, IBaseOperationOutcome.class));
			} catch (NoSuchMethodException e) {
				fail(classInfo.getBeanClassName() + " has no constructor with params: (String, IBaseOperationOutcome)");
			}
		}

	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
