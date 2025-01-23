package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OperationParameterRangeType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.ParamsWithTypeConversion;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SampleParams;
import ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.SampleParamsWithIdParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.EmbeddedParamsInnerClassesAndMethods.INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION;
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

// LUKETODO: try to test for every case in embedded params where there's a throws

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
		assertThat(resourceParameters).hasExactlyElementsOfTypes(EmbeddedOperationParameter.class, EmbeddedOperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
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
		assertThat(resourceParameters).hasExactlyElementsOfTypes(RequestDetailsParameter.class, EmbeddedOperationParameter.class, EmbeddedOperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new RequestDetailsParameterToAssert(),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE)
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
		assertThat(resourceParameters).hasExactlyElementsOfTypes(EmbeddedOperationParameter.class, EmbeddedOperationParameter.class, RequestDetailsParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
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
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, EmbeddedOperationParameter.class, EmbeddedOperationParameter.class, EmbeddedOperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new NullParameterToAssert(),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param1", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "param2", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, ArrayList.class, String.class, null, Void.class, OperationParameterRangeType.NOT_APPLICABLE),
		new EmbeddedOperationParameterToAssert(ourFhirContext, "param3", SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS,null, String.class, "boolean", Void.class, OperationParameterRangeType.NOT_APPLICABLE)
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
		assertThat(resourceParameters).hasExactlyElementsOfTypes(EmbeddedOperationParameter.class, EmbeddedOperationParameter.class);

		final List<IParameterToAssert> expectedParameters = List.of(
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "periodStart", SIMPLE_METHOD_WITH_PARAMS_CONVERSION,null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.START),
			 new EmbeddedOperationParameterToAssert(ourFhirContext, "periodEnd", SIMPLE_METHOD_WITH_PARAMS_CONVERSION, null, ZonedDateTime.class, null, String.class, OperationParameterRangeType.END)
		);

		assertThat(resourceParameters)
			 .matches(theActualParameters -> assertParametersEqual(expectedParameters, theActualParameters),
				  "Expected parameters do not match actual parameters");
	}

	@Test
	@Disabled
    void getResourceParameters_withOptionalParam_shouldReturnSearchParameter() throws NoSuchMethodException {
//        final Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
//        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{{new OptionalParam() {
//            @Override
//            public Class<? extends Annotation> annotationType() {
//                return OptionalParam.class;
//            }
//
//			@Override
//			public String[] chainBlacklist() {
//				return new String[0];
//			}
//
//			@Override
//			public String[] chainWhitelist() {
//				return new String[0];
//			}
//
//			@Override
//			public Class<? extends IQueryParameterType>[] compositeTypes() {
//				return new Class[0];
//			}
//
//			@Override
//            public String name() {
//                return "param";
//            }
//
//			@Override
//			public Class<? extends IBaseResource>[] targetTypes() {
//				return new Class[0];
//			}
//		}}});
//        when(method.getParameterTypes()).thenReturn(sampleMethod.getParameterTypes());
//
//        List<IParameter> parameters = MethodUtil.getResourceParameters(myFhirContext, method, provider);
//
//        assertEquals(1, parameters.size());
//		assertInstanceOf(SearchParameter.class, parameters.get(0));
//        SearchParameter searchParameter = (SearchParameter) parameters.get(0);
//        assertEquals("param", searchParameter.getName());
//        assertFalse(searchParameter.isRequired());
    }

    @Test
	@Disabled
    void getResourceParameters_withInvalidAnnotation_shouldThrowConfigurationException() throws NoSuchMethodException {
//        Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
//        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{{new Annotation() {
//            @Override
//            public Class<? extends Annotation> annotationType() {
//                return Annotation.class;
//            }
//        }}});
//        when(method.getParameterTypes()).thenReturn(sampleMethod.getParameterTypes());
//
//        ConfigurationException exception = assertThrows(ConfigurationException.class, () -> {
//            MethodUtil.getResourceParameters(myFhirContext, method, provider);
//        });
//
//        assertTrue(exception.getMessage().contains("has no recognized FHIR interface parameter nextParameterAnnotations"));
    }

    @Test
	@Disabled
    void getResourceParameters_withMultipleAnnotations_shouldReturnCorrectParameters() throws NoSuchMethodException {
//        Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
//        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{
//            {new RequiredParam() {
//                @Override
//                public Class<? extends Annotation> annotationType() {
//                    return RequiredParam.class;
//                }
//
//				@Override
//				public String[] chainBlacklist() {
//					return new String[0];
//				}
//
//				@Override
//				public String[] chainWhitelist() {
//					return new String[0];
//				}
//
//				@Override
//				public Class<? extends IQueryParameterType>[] compositeTypes() {
//					return new Class[0];
//				}
//
//				@Override
//                public String name() {
//                    return "param1";
//                }
//
//				@Override
//				public Class<? extends IBaseResource>[] targetTypes() {
//					return new Class[0];
//				}
//			}},
//            {new OptionalParam() {
//                @Override
//                public Class<? extends Annotation> annotationType() {
//                    return OptionalParam.class;
//                }
//
//				@Override
//				public String[] chainBlacklist() {
//					return new String[0];
//				}
//
//				@Override
//				public String[] chainWhitelist() {
//					return new String[0];
//				}
//
//				@Override
//				public Class<? extends IQueryParameterType>[] compositeTypes() {
//					return new Class[0];
//				}
//
//				@Override
//                public String name() {
//                    return "param2";
//                }
//
//				@Override
//				public Class<? extends IBaseResource>[] targetTypes() {
//					return new Class[0];
//				}
//			}}
//        });
//        when(method.getParameterTypes()).thenReturn(new Class[]{String.class, String.class});
//
//        List<IParameter> parameters = MethodUtil.getResourceParameters(myFhirContext, method, provider);
//
//        assertEquals(2, parameters.size());
//        assertTrue(parameters.get(0) instanceof SearchParameter);
//        assertTrue(parameters.get(1) instanceof SearchParameter);
//        SearchParameter searchParameter1 = (SearchParameter) parameters.get(0);
//        SearchParameter searchParameter2 = (SearchParameter) parameters.get(1);
//        assertEquals("param1", searchParameter1.getName());
//        assertTrue(searchParameter1.isRequired());
//        assertEquals("param2", searchParameter2.getName());
//        assertFalse(searchParameter2.isRequired());
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

		if (theExpectedParameter instanceof EmbeddedOperationParameterToAssert expectedEmbeddedOperationParameter && theActualParameter instanceof EmbeddedOperationParameter actualEmbeddedOperationParameter) {
			assertThat(actualEmbeddedOperationParameter.getContext().getVersion().getVersion()).isEqualTo(expectedEmbeddedOperationParameter.myContext().getVersion().getVersion());
			assertThat(actualEmbeddedOperationParameter.getName()).isEqualTo(expectedEmbeddedOperationParameter.myName());
			assertThat(actualEmbeddedOperationParameter.getParamType()).isEqualTo(expectedEmbeddedOperationParameter.myParamType());
			assertThat(actualEmbeddedOperationParameter.getInnerCollectionType()).isEqualTo(expectedEmbeddedOperationParameter.myInnerCollectionType());
			assertThat(actualEmbeddedOperationParameter.getSourceType()).isEqualTo(expectedEmbeddedOperationParameter.myTypeToConvertFrom());
			assertThat(actualEmbeddedOperationParameter.getRangeType()).isEqualTo(expectedEmbeddedOperationParameter.myRangeType());

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

	private record EmbeddedOperationParameterToAssert(
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
