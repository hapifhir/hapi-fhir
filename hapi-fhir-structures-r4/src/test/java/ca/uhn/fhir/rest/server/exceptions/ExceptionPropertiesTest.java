package ca.uhn.fhir.rest.server.exceptions;

import static org.junit.Assert.*;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.util.TestUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.util.Set;

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
		assertEquals("Resource of type Practitioner with ID Patient/123 is gone/deleted", new ResourceGoneException(Practitioner.class, new IdType("Patient/123")).getMessage());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testExceptionsAreGood() throws Exception {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AssignableTypeFilter(BaseServerResponseException.class));
		Set<BeanDefinition> classes = scanner.findCandidateComponents(BaseServerResponseException.class.getPackage().getName());
		assertTrue(classes.toString(), classes.size() > 5);

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
				fail(classInfo.getBeanClassName() + " has no constructor with params: (String, IBaseOperationOutcome)");
			}
		}

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
