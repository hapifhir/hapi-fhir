package ca.uhn.fhir.model.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BatchJobParametersTest {

	private static class TestParameters extends BaseBatchJobParameters {

	}

	private static class TestParam {
		private final Object myTestValue;
		private final boolean myExpectedToWork;

		public TestParam(Object theValue, boolean theExpected) {
			myTestValue = theValue;
			myExpectedToWork = theExpected;
		}

		public Object getTestValue() {
			return myTestValue;
		}

		public boolean isExpectedToWork() {
			return myExpectedToWork;
		}
	}

	private static List<TestParam> parameters() {
		List<TestParam> params = new ArrayList<>();

		// should pass
		params.add(new TestParam("string", true));
		params.add(new TestParam(1, true));
		params.add(new TestParam(1.1f, true));
		params.add(new TestParam(1.1d, true));
		params.add(new TestParam(true, true));
		params.add(new TestParam(-1, true));

		// should not pass
		params.add(new TestParam(List.of("strings"), false));
		params.add(new TestParam(new Object(), false));

		return params;
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void setUserData_acceptsStringNumberAndBooleansOnly(TestParam theParams) {
		// setup
		String key = "key";
		TestParameters parameters = new TestParameters();
		Object testValue = theParams.getTestValue();

		// test
		if (theParams.isExpectedToWork()) {
			parameters.setUserData(key, testValue);
			assertFalse(parameters.getUserData().isEmpty());
			assertThat(parameters.getUserData()).containsEntry(key, testValue);
		} else {
			try {
				parameters.setUserData(key, testValue);
				fail();
			} catch (IllegalArgumentException ex) {
				String dataType = testValue.getClass().getName();
				assertThat(ex.getMessage().contains("Invalid data type provided " + dataType)).as(ex.getMessage()).isTrue();
				assertTrue(parameters.getUserData().isEmpty());
			}
		}
	}

	@Test
	public void setUserData_invalidKey_throws() {
		// setup
		TestParameters parameters = new TestParameters();

		// test
		for (String key : new String[] { null, "" }) {
			try {
				parameters.setUserData(key, "test");
				fail();
			} catch (IllegalArgumentException ex) {
				assertThat(ex.getMessage().contains("Invalid key; key must be non-empty, non-null")).as(ex.getMessage()).isTrue();
			}
		}
	}

	@Test
	public void setUserData_nullValue_removes() {
		// setup
		TestParameters parameters = new TestParameters();
		String key = "key";

		// test
		parameters.setUserData(key, "test");
		assertThat(parameters.getUserData()).containsKey(key);

		parameters.setUserData(key, null);
		assertFalse(parameters.getUserData().containsKey(key));
	}
}
