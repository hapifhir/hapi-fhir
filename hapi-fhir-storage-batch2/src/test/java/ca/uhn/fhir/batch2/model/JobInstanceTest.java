package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.RandomDataHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JobInstanceTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JobInstanceTest.class);

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
	void testAddUserData_withSerializableValue_doesNotThrowException() {
		JobInstance instance = new JobInstance();
		instance.addUserData("some-key", "some-value");
		assertEquals("some-value", instance.getUserData().get("some-key"));
	}

	@Test
	void testAddUserData_withNonSerializableValue_throwsException() {
		JobInstance instance = new JobInstance();
		try {
			instance.addUserData("some-key", new NonSerializableClass("some-value"));
			fail();
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("HAPI-1741: Failed to encode"));
		}
	}

	@Test
	void testSetUserData_withSerializableValue_doesNotThrowException() {
		JobInstance instance = new JobInstance();
		Map<String, Object> userData = Map.of("some-key", "some-value");
		instance.setUserData(userData);
		assertEquals("some-value", instance.getUserData().get("some-key"));
	}

	@Test
	void testSetUserData_withNonSerializableValue_throwsException() {
		JobInstance instance = new JobInstance();
		Map<String, Object> userData = Map.of("some-key", new NonSerializableClass("some-value"));
		try {
			instance.setUserData(userData);
			fail();
		} catch (InvalidRequestException e) {
			assertTrue(e.getMessage().contains("HAPI-1741: Failed to encode"));
		}
	}

	@Test
	void testGetReportTyped_Null() {
		JobInstance instance = new JobInstance();
		assertNull(instance.getReport(WorkChunk.class));
	}


	static class NonSerializableClass {

		/**
		 * No default constructor, so this can't be deserialized
		 */
		public NonSerializableClass(String theValue) {
			ourLog.info("value: {}", theValue);
		}
	}

	static class SerializableClass implements IModelJson {

		@JsonProperty("value")
		private String myValue;

		public String getValue() {
			return myValue;
		}

		public void setValue(String theValue) {
			myValue = theValue;
		}

	}

}
