package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodUtilTest {

    @Mock
    private FhirContext myFhirContext;

    @Mock
    private Method method;

    @Mock
    private Object provider;

    void sampleMethod(@RequiredParam(name = "param") String param) {
        // Sample method for testing
    }

    @Test
    void getResourceParameters_withOptionalParam_shouldReturnSearchParameter() throws NoSuchMethodException {
        Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{{new OptionalParam() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return OptionalParam.class;
            }

			@Override
			public String[] chainBlacklist() {
				return new String[0];
			}

			@Override
			public String[] chainWhitelist() {
				return new String[0];
			}

			@Override
			public Class<? extends IQueryParameterType>[] compositeTypes() {
				return new Class[0];
			}

			@Override
            public String name() {
                return "param";
            }

			@Override
			public Class<? extends IBaseResource>[] targetTypes() {
				return new Class[0];
			}
		}}});
        when(method.getParameterTypes()).thenReturn(sampleMethod.getParameterTypes());

        List<IParameter> parameters = MethodUtil.getResourceParameters(myFhirContext, method, provider);

        assertEquals(1, parameters.size());
		assertInstanceOf(SearchParameter.class, parameters.get(0));
        SearchParameter searchParameter = (SearchParameter) parameters.get(0);
        assertEquals("param", searchParameter.getName());
        assertFalse(searchParameter.isRequired());
    }

    @Test
    void getResourceParameters_withInvalidAnnotation_shouldThrowConfigurationException() throws NoSuchMethodException {
        Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{{new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        }}});
        when(method.getParameterTypes()).thenReturn(sampleMethod.getParameterTypes());

        ConfigurationException exception = assertThrows(ConfigurationException.class, () -> {
            MethodUtil.getResourceParameters(myFhirContext, method, provider);
        });

        assertTrue(exception.getMessage().contains("has no recognized FHIR interface parameter nextParameterAnnotations"));
    }

    @Test
    void getResourceParameters_withMultipleAnnotations_shouldReturnCorrectParameters() throws NoSuchMethodException {
        Method sampleMethod = this.getClass().getDeclaredMethod("sampleMethod", String.class);
        when(method.getParameterAnnotations()).thenReturn(new Annotation[][]{
            {new RequiredParam() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return RequiredParam.class;
                }

				@Override
				public String[] chainBlacklist() {
					return new String[0];
				}

				@Override
				public String[] chainWhitelist() {
					return new String[0];
				}

				@Override
				public Class<? extends IQueryParameterType>[] compositeTypes() {
					return new Class[0];
				}

				@Override
                public String name() {
                    return "param1";
                }

				@Override
				public Class<? extends IBaseResource>[] targetTypes() {
					return new Class[0];
				}
			}},
            {new OptionalParam() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return OptionalParam.class;
                }

				@Override
				public String[] chainBlacklist() {
					return new String[0];
				}

				@Override
				public String[] chainWhitelist() {
					return new String[0];
				}

				@Override
				public Class<? extends IQueryParameterType>[] compositeTypes() {
					return new Class[0];
				}

				@Override
                public String name() {
                    return "param2";
                }

				@Override
				public Class<? extends IBaseResource>[] targetTypes() {
					return new Class[0];
				}
			}}
        });
        when(method.getParameterTypes()).thenReturn(new Class[]{String.class, String.class});

        List<IParameter> parameters = MethodUtil.getResourceParameters(myFhirContext, method, provider);

        assertEquals(2, parameters.size());
        assertTrue(parameters.get(0) instanceof SearchParameter);
        assertTrue(parameters.get(1) instanceof SearchParameter);
        SearchParameter searchParameter1 = (SearchParameter) parameters.get(0);
        SearchParameter searchParameter2 = (SearchParameter) parameters.get(1);
        assertEquals("param1", searchParameter1.getName());
        assertTrue(searchParameter1.isRequired());
        assertEquals("param2", searchParameter2.getName());
        assertFalse(searchParameter2.isRequired());
    }
}
