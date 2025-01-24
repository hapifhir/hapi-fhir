package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.annotation.EmbeddableOperationParams;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EmbeddedOperationUtilsTest {
	@EmbeddableOperationParams
	private static class SimpleFieldsAndConstructorInOrder {
		private final String myParam1;
		private final int myParam2;

		public SimpleFieldsAndConstructorInOrder(
			 @OperationParam(name = "param1")
			 String theParam1,
			 @OperationParam(name = "param2")
			 int theParam2) {
			myParam1 = theParam1;
			myParam2 = theParam2;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SimpleFieldsAndConstructorInOrder that = (SimpleFieldsAndConstructorInOrder) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}
	}

	private static class SimpleFieldsAndConstructorArgLengthMismatch {
		private final String myParam1;
		private final int myParam2 = 1;

		public SimpleFieldsAndConstructorArgLengthMismatch (String theParam1) {
			myParam1 = theParam1;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SimpleFieldsAndConstructorArgLengthMismatch that = (SimpleFieldsAndConstructorArgLengthMismatch ) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}
	}

	private static class SimpleFieldsAndConstructorArgOneFieldNotFinal {
		private final String myParam1;
		private int myParam2 = 1;

		public SimpleFieldsAndConstructorArgOneFieldNotFinal(String theParam1, int theParam2) {
			myParam1 = theParam1;
			myParam2 = theParam2;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SimpleFieldsAndConstructorArgOneFieldNotFinal that = (SimpleFieldsAndConstructorArgOneFieldNotFinal) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}
	}

	private static class SimpleFieldsAndConstructorOutOfOrder {
		private final String myParam1;
		private final int myParam2;

		public SimpleFieldsAndConstructorOutOfOrder(int theParam2, String theParam1) {
			myParam2 = theParam2;
			myParam1 = theParam1;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			SimpleFieldsAndConstructorOutOfOrder that = (SimpleFieldsAndConstructorOutOfOrder) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2);
		}
	}

	@EmbeddableOperationParams
	private static class WithGenericFieldsAndConstructorInOrder {
		private final String myParam1;
		private final int myParam2;
		private final List<String> myParam3;
		private final List<Integer> myParam4;

		public WithGenericFieldsAndConstructorInOrder(
				 @OperationParam(name = "param1")
				 String theParam1,
				 @OperationParam(name = "param2")
				 int theParam2,
				 @OperationParam(name = "param3")
				 List<String> theParam3,
				 @OperationParam(name = "param4")
				 List<Integer> theParam4) {
			myParam1 = theParam1;
			myParam2 = theParam2;
			myParam3 = theParam3;
			myParam4 = theParam4;
		}

		@Override
		public boolean equals(Object o) {

			if (o == null || getClass() != o.getClass()) return false;
			WithGenericFieldsAndConstructorInOrder that = (WithGenericFieldsAndConstructorInOrder) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam3, that.myParam3) && Objects.equals(myParam4, that.myParam4);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2, myParam3, myParam4);
		}
	}

	private static class WithGenericFieldsAndConstructorOutOfOrder {
		private final String myParam1;
		private final int myParam2;
		private final List<String> myParam3;
		private final List<Integer> myParam4;

		public WithGenericFieldsAndConstructorOutOfOrder(String theParam1, int theParam2, List<Integer> theParam4, List<String> theParam3) {
			myParam1 = theParam1;
			myParam2 = theParam2;
			myParam4 = theParam4;
			myParam3 = theParam3;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			WithGenericFieldsAndConstructorOutOfOrder that = (WithGenericFieldsAndConstructorOutOfOrder) o;
			return myParam2 == that.myParam2 && Objects.equals(myParam1, that.myParam1) && Objects.equals(myParam3, that.myParam3) && Objects.equals(myParam4, that.myParam4);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParam1, myParam2, myParam3, myParam4);
		}
	}

	@Test
	void simpleConstructorInOrder() throws InvocationTargetException, InstantiationException, IllegalAccessException {
		final Constructor<?> constructor = EmbeddedOperationUtils.validateAndGetConstructor(SimpleFieldsAndConstructorInOrder.class);

		final String param1 = "string";
		final int param2 = 1;

		assertThat(constructor.newInstance(param1, param2))
			 .isEqualTo(new SimpleFieldsAndConstructorInOrder(param1, param2));
	}

	@Test
	void simpleConstructorOutOfOrder() {
		assertThatThrownBy(() -> EmbeddedOperationUtils.validateAndGetConstructor(SimpleFieldsAndConstructorOutOfOrder.class))
			 .isInstanceOf(ConfigurationException.class);
	}

	@Test
	void simpleConstructorOneFieldNotFinal() {
		assertThatThrownBy(() -> EmbeddedOperationUtils.validateAndGetConstructor(SimpleFieldsAndConstructorArgOneFieldNotFinal.class))
			 .isInstanceOf(ConfigurationException.class);
	}

	@Test
	void simpleConstructorConstructorArgMismatch() {
		assertThatThrownBy(() -> EmbeddedOperationUtils.validateAndGetConstructor(SimpleFieldsAndConstructorArgLengthMismatch.class))
			 .isInstanceOf(ConfigurationException.class);
	}

	@Test
	void withGenericsInOrder() throws InvocationTargetException, InstantiationException, IllegalAccessException {
		final Constructor<?> constructor = EmbeddedOperationUtils.validateAndGetConstructor(WithGenericFieldsAndConstructorInOrder.class);

		final String param1 = "string";
		final int param2 = 1;
		final List<String> param3 = List.of("string1", "string2");
		final List<Integer> param4 = List.of(3, 4);

		assertThat(constructor.newInstance(param1, param2, param3, param4))
			 .isEqualTo(new WithGenericFieldsAndConstructorInOrder(param1, param2, param3, param4));
	}

	@Test
	void withGenericsOutOfOrder() {
		assertThatThrownBy(() -> EmbeddedOperationUtils.validateAndGetConstructor(WithGenericFieldsAndConstructorOutOfOrder.class))
				.isInstanceOf(ConfigurationException.class);
//				.hasMessageContaining("1234: mismatch between constructor args: [class java.lang.String, class int, class java.util.List, class java.util.List] and non-request details parameter args: [string, 1, [3, 4], [string1, string2]]");
	}
}
