package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.ParamsWithIdParamAndTypeConversion;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.ParamsWithTypeConversion;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SampleParams;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SampleParamsWithIdParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS;
import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SIMPLE_METHOD_WITH_PARAMS_CONVERSION;
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
	void sampleMethodOperationParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
		new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodOperationParamsWithFhirTypes() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class, BooleanType.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class,null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
		new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParamsRequestDetailsFirst() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_FIRST, RequestDetails.class, SampleParams.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(RequestDetailsParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new RequestDetailsParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParamsRequestDetailsLast() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_REQUEST_DETAILS_LAST, SampleParams.class, RequestDetails.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class, RequestDetailsParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new RequestDetailsParameterToAssert()
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void sampleMethodEmbeddedParamsWithFhirTypes() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS_WITH_ID_TYPE, SampleParamsWithIdParam.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new OperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void paramsConversionZonedDateTime() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SIMPLE_METHOD_WITH_PARAMS_CONVERSION, ParamsWithTypeConversion.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new OperationParameterToAssert(ourFhirContext, "periodStart", SIMPLE_METHOD_WITH_PARAMS_CONVERSION,null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.START),
			 new OperationParameterToAssert(ourFhirContext, "periodEnd", SIMPLE_METHOD_WITH_PARAMS_CONVERSION, null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.END)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	void paramsConversionIdTypeZonedDateTime() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_ID_TYPE_AND_TYPE_CONVERSION, ParamsWithIdParamAndTypeConversion.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new OperationParameterToAssert(ourFhirContext, "periodStart", SIMPLE_METHOD_WITH_PARAMS_CONVERSION,null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.START),
			 new OperationParameterToAssert(ourFhirContext, "periodEnd", SIMPLE_METHOD_WITH_PARAMS_CONVERSION, null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.END)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
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

		if (theExpectedParameter instanceof OperationParameterToAssert expectedOperationParameter && theActualParameter instanceof OperationParameter actualOperationParameter) {
			assertThat(actualOperationParameter.getContext().getVersion().getVersion()).isEqualTo(expectedOperationParameter.myContext().getVersion().getVersion());
			assertThat(actualOperationParameter.getName()).isEqualTo(expectedOperationParameter.myName());
			assertThat(actualOperationParameter.getParamType()).isEqualTo(expectedOperationParameter.myParamType());
			assertThat(actualOperationParameter.getInnerCollectionType()).isEqualTo(expectedOperationParameter.myInnerCollectionType());
			assertThat(actualOperationParameter.getSourceType()).isEqualTo(expectedOperationParameter.myTypeToConvertFrom());
			assertThat(actualOperationParameter.getRangeType()).isEqualTo(expectedOperationParameter.myRangeType());

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
		 String myParamType,
		 Class<?> myTypeToConvertFrom,
		 OperationParameterRangeType myRangeType) implements IParameterToAssert {
	}
}
