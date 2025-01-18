package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SampleParams;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.INVALID_METHOD_OPERATION_PARAMS_NO_OPERATION;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SAMPLE_METHOD_OPERATION_PARAMS;
import static ca.uhn.fhir.rest.server.method.InnerClassesAndMethods.SUPER_SIMPLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MethodUtilTest {

	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(MethodUtilTest.class);

	// Need FHIR structures in test pom to use this, which is only needed for a tiny number of test cases
	@Mock
    private FhirContext myFhirContext;

	@Mock
	private IFhirVersion myFhirVersion;

	private final InnerClassesAndMethods myInnerClassesAndMethods = new InnerClassesAndMethods();

    @Mock
    private Object myProvider;

	@BeforeEach
	void beforeEach() {
		lenient().when(myFhirVersion.getVersion()).thenReturn(FhirVersionEnum.R4);
		lenient().when(myFhirContext.getVersion()).thenReturn(myFhirVersion);
	}

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
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_OPERATION_PARAMS, IIdType.class, String.class, List.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(NullParameter.class, OperationParameter.class, OperationParameter.class);

		// LUKETODO:  assert the actual OperationParameter values
	}

	@Test
	void sampleMethodEmbeddedParams() {
		final List<IParameter> resourceParameters = getMethodAndExecute(SAMPLE_METHOD_EMBEDDED_TYPE_NO_REQUEST_DETAILS, SampleParams.class);

		assertThat(resourceParameters).isNotNull();
		assertThat(resourceParameters).isNotEmpty();
		assertThat(resourceParameters).hasExactlyElementsOfTypes(OperationEmbeddedParameter.class, OperationEmbeddedParameter.class);

		// LUKETODO:  assert the actual OperationEmbeddedParameter values
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
			myFhirContext,
			myInnerClassesAndMethods.getDeclaredMethod(theMethodName, theParamClasses),
			myProvider);
	}

	private List<IParameter> getResourceParameters(Method theMethod) {
		return MethodUtil.getResourceParameters(myFhirContext, theMethod, myProvider);
	}
}
