package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.RandomDataHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JobInstanceTest {

	@Test
	void testCopyConstructor_randomFieldsCopied_areEqual() {
	    // given
		JobInstance instance = new JobInstance();
		RandomDataHelper.fillFieldsRandomly(instance);

		// when
		JobInstance copy = new JobInstance(instance);

		// then
		assertTrue(EqualsBuilder.reflectionEquals(instance, copy));
	}

	@Test
	void testAddUserData_withSerializableValue_doesNotThrowException(){
		JobInstance instance = new JobInstance();
		instance.addUserData("some-key", "some-value");
		assertEquals("some-value", instance.getUserData().get("some-key"));
	}

	@Test
	void testAddUserData_withNonSerializableValue_throwsException(){
		JobInstance instance = new JobInstance();
		try {
			instance.addUserData("some-key", new NonSerializableClass("some-value"));
			fail();
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("HAPI-1741: Failed to encode"));
		}
	}

	@Test
	void testSetUserData_withSerializableValue_doesNotThrowException(){
		JobInstance instance = new JobInstance();
		Map<String, Object> userData = Map.of("some-key", "some-value");
		instance.setUserData(userData);
		assertEquals("some-value", instance.getUserData().get("some-key"));
	}

	@Test
	void testSetUserData_withNonSerializableValue_throwsException(){
		JobInstance instance = new JobInstance();
		Map<String, Object> userData = Map.of("some-key", new NonSerializableClass("some-value"));
		try {
			instance.setUserData(userData);
			fail();
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("HAPI-1741: Failed to encode"));
		}
	}

	class NonSerializableClass {
		private final String myValue;

		public NonSerializableClass(String theValue) {
			myValue = theValue;
		}
	}
}
