package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseMethodBindingMethodParameterBuilderTest {

	@Mock
	private Method myMethod;

	@Mock
	private Annotation myAnnotation;

	@Test
	void buildMethodParams_withNullMethod_shouldThrowInternalErrorException() {
		InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(null, new Object[]{"param1"});
		});

		assertTrue(exception.getMessage().contains("Method cannot be null"));
	}

	@Test
	void buildMethodParams_withNullParameterTypes_shouldThrowInternalErrorException() throws Exception {
		when(myMethod.getParameterTypes()).thenReturn(null);
		when(myMethod.getParameterAnnotations()).thenReturn(new Annotation[][]{{}});

		InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(myMethod, new Object[]{"param1"});
		});

		assertTrue(exception.getMessage().contains("Parameter types cannot be null"));
	}

	@Test
	void buildMethodParams_withNullParameterAnnotations_shouldThrowInternalErrorException() throws Exception {
		when(myMethod.getParameterTypes()).thenReturn(new Class<?>[]{String.class});
		when(myMethod.getParameterAnnotations()).thenReturn(null);

		InternalErrorException exception = assertThrows(InternalErrorException.class, () -> {
			BaseMethodBindingMethodParameterBuilder.buildMethodParams(myMethod, new Object[]{"param1"});
		});

		assertTrue(exception.getMessage().contains("Parameter annotations cannot be null"));
	}

	@Test
	void buildMethodParams_withEmptyParameterTypes_shouldReturnEmptyArray() throws Exception {
		when(myMethod.getParameterTypes()).thenReturn(new Class<?>[]{});
		when(myMethod.getParameterAnnotations()).thenReturn(new Annotation[][]{{}});

		Object[] result = BaseMethodBindingMethodParameterBuilder.buildMethodParams(myMethod, new Object[]{});

		assertArrayEquals(new Object[]{}, result);
	}

	@Test
	void buildMethodParams_withSingleOperationEmbeddedParam_shouldReturnCorrectParams() throws Exception {
		when(myMethod.getParameterTypes()).thenReturn(new Class<?>[]{String.class});
		final Annotation[][] doubleArray = new Annotation[][] {{myAnnotation}};
//		when(myMethod.getParameterAnnotations()).thenReturn(doubleArray);
//		when(myMethod.getParameterAnnotations()).thenReturn(new Annotation[][]{
//			{new OperationEmbeddedParam() {
//				@Override
//				public String name() {
//					return "";
//				}
//
//				@Override
//				public Class<? extends IBase> type() {
//					return null;
//				}
//
//				@Override
//				public String typeName() {
//					return "";
//				}
//
//				@Override
//				public int min() {
//					return 0;
//				}
//
//				@Override
//				public int max() {
//					return 0;
//				}
//
//				@Override
//				public Class<? extends Annotation> annotationType() {
//					return OperationEmbeddedParam.class;
//				}
//			}}
//		});

		Object[] result = BaseMethodBindingMethodParameterBuilder.buildMethodParams(myMethod, new Object[]{"param1"});

		assertEquals(1, result.length);
		assertTrue(result[0] instanceof String);
	}
}
