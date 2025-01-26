package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SUPER_SIMPLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

// This test lives in hapi-fhir-structures-r4 because if we introduce it in hapi-fhir-server, there will be a
// circular dependency
class MethodUtilTest {

	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(MethodUtilTest.class);

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private final EmbeddedParamsInnerClassesAndMethods myEmbeddedParamsInnerClassesAndMethods = new EmbeddedParamsInnerClassesAndMethods();

    private final Object myProvider = new Object();

	@Test
	void simpleMethodNoParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SUPER_SIMPLE);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isEmpty();
	}

	@Test
	void invalid_methodWithOperationParamsNoOperation() {
		assertThatThrownBy(
			() -> getMethodAndExecute(INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION,
				String.class))
			.isInstanceOf(ConfigurationException.class);
	}

	@Test
	void invalidMethodWithNoAnnotations() {
		assertThatThrownBy(() -> getMethodAndExecute("methodWithNoAnnotations", String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations");
	}

	@Test
	void invalidMethodWithInvalidGenericType() {
		assertThatThrownBy(() -> getMethodAndExecute("methodWithInvalidGenericType", List.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("is of an invalid generic type");
	}

	@Test
	void invalidMethodWithUnknownTypeName() {
		assertThatThrownBy(() -> getMethodAndExecute("methodWithUnknownTypeName", String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
	}

	@Test
	void invalidMethodWithNonAssignableTypeName() {
		assertThatThrownBy(() -> getMethodAndExecute("methodWithNonAssignableTypeName", String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining(" has no recognized FHIR interface parameter nextParameterAnnotations. Don't know how to handle this parameter");
	}

	@Test
	void invalidMethodWithInvalidAnnotation() {
		assertThatThrownBy(() -> getMethodAndExecute("methodWithInvalidAnnotation", String.class))
			 .isInstanceOf(ConfigurationException.class)
			 .hasMessageContaining("has no recognized FHIR interface parameter nextParameterAnnotations");
	}

	private List<IParameter> getMethodAndExecute(String theMethodName, Class<?>... theParamClasses) {
		return MethodUtil.getResourceParameters(
			ourFhirContext,
			myEmbeddedParamsInnerClassesAndMethods.getDeclaredMethod(theMethodName, theParamClasses),
			myProvider);
	}

	private boolean assertParametersEqual(List<? extends IParameterToAssert> theExpectedParameters, List<? extends IParameter> theActualParameters) {
		if (theActualParameters.size() != theExpectedParameters.size()) {
			fail("Expected parameters size does not match actual parameters size");
			return false;
		}

		for (int i = 0; i < theActualParameters.size(); i++) {
			final IParameterToAssert expectedParameter = theExpectedParameters.get(i);
			final IParameter actualParameter = theActualParameters.get(i);

			if (! assertParametersEqual(expectedParameter, actualParameter)) {
				return false;
			}
		}

		return true;
	}

	private boolean assertParametersEqual(IParameterToAssert theExpectedParameter, IParameter theActualParameter) {
		if (theExpectedParameter instanceof NullParameterToAssert && theActualParameter instanceof NullParameter) {
			return true;
		}

		if (theExpectedParameter instanceof RequestDetailsParameterToAssert && theActualParameter instanceof RequestDetailsParameter) {
			return true;
		}

		return false;
	}

	private interface IParameterToAssert {}

	private record NullParameterToAssert() implements IParameterToAssert {
	}

	private record RequestDetailsParameterToAssert() implements IParameterToAssert {
	}

	private record OperationParameterToAssert(
		 FhirContext myContext,
		 String myName,
		 String myOperationName,
		 @SuppressWarnings("rawtypes")
		 Class<? extends Collection> myInnerCollectionType,
		 Class<?> myParameterType,
		 String myParamType) implements IParameterToAssert {
	}
}
