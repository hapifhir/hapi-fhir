package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationEmbeddedParam;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.annotation.Nonnull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.*;

/**
 * Leveraged by {@link MethodUtil} exclusively to convert {@link OperationEmbeddedParam} parameters for a method to
 * either a {@link NullParameter} or an {@link OperationEmbeddedParam}.
 */
public class EmbeddedParameterConverter {
	private static final org.slf4j.Logger ourLog = getLogger(EmbeddedParameterConverter.class);

	private final FhirContext myContext;
	private final Method myMethod;
	private final Operation myOperation;
	// LUKETODO:  warning?
	private final Class<?>[] myParameterTypes;
	private final Class<?> myOperationEmbeddedType;

	public EmbeddedParameterConverter(
			FhirContext theContext,
			Method theMethod,
			Operation theOperation,
			Class<?>[] theParameterTypes,
			Class<?> theOperationEmbeddedType) {
		myContext = theContext;
		myMethod = theMethod;
		myOperation = theOperation;
		myParameterTypes = theParameterTypes;
		myOperationEmbeddedType = theOperationEmbeddedType;
	}

	List<EmbeddedParameterConverterContext> convert() {
		return Arrays.stream(myOperationEmbeddedType.getDeclaredFields())
				.map(this::convertField)
				.collect(Collectors.toUnmodifiableList());
	}

	private EmbeddedParameterConverterContext convertField(Field theField) {
		final String fieldName = theField.getName();
		final Class<?> fieldType = theField.getType();
		final Annotation[] fieldAnnotations = theField.getAnnotations();

		if (fieldAnnotations.length < 1) {
			throw new ConfigurationException(String.format(
					"%sNo annotations for field: %s for method: %s", Msg.code(9999926), fieldName, myMethod.getName()));
		}

		if (fieldAnnotations.length > 1) {
			throw new ConfigurationException(String.format(
					"%sMore than one annotation for field: %s for method: %s",
					Msg.code(999998), fieldName, myMethod.getName()));
		}

		final Annotation fieldAnnotation = fieldAnnotations[0];

		if (fieldAnnotation instanceof IdParam) {
			return EmbeddedParameterConverterContext.forParameter(new NullParameter());
		} else if (fieldAnnotation instanceof OperationEmbeddedParam) {
			final ParamInitializationContext paramContext =
					buildParamContext(fieldType, theField, (OperationEmbeddedParam) fieldAnnotation);

			return EmbeddedParameterConverterContext.forEmbeddedContext(paramContext);
		} else {
			final String error = String.format(
					"%sUnsupported annotation type: %s for a class: %s with OperationEmbeddedParams which is part of method: %s: ",
					Msg.code(912732197), myOperationEmbeddedType, fieldAnnotation.annotationType(), myMethod.getName());

			throw new ConfigurationException(error);
		}
	}

	private ParamInitializationContext buildParamContext(
			Class<?> theFieldType, Field theField, OperationEmbeddedParam theOperationEmbeddedParam) {

		final OperationEmbeddedParameter operationEmbeddedParameter =
				getOperationEmbeddedParameter(theOperationEmbeddedParam);

		Class<?> parameterType = theFieldType;
		Class<? extends java.util.Collection<?>> outerCollectionType = null;
		Class<? extends java.util.Collection<?>> innerCollectionType = null;

		// Flat collection
		if (Collection.class.isAssignableFrom(parameterType)) {
			innerCollectionType = unsafeCast(parameterType);
			parameterType = ReflectionUtil.getGenericCollectionTypeOfField(theField);
			if (parameterType == null) {
				final String error = String.format(
						"%s Cannot find generic type for field: %s in class: %s for method: %s",
						Msg.code(724612469),
						theField.getName(),
						theField.getDeclaringClass().getCanonicalName(),
						myMethod.getName());
				throw new ConfigurationException(error);
			}

			// Collection of a Collection: Permitted
			if (Collection.class.isAssignableFrom(parameterType)) {
				outerCollectionType = innerCollectionType;
				innerCollectionType = unsafeCast(parameterType);
				parameterType = ReflectionUtil.getGenericCollectionTypeOfField(theField);
			}

			// Collection of a Collection of a Collection:  Prohibited
			if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
				final String error = String.format(
						"%sInvalid generic type (a collection of a collection of a collection) for field: %s in class: %s for method: %s",
						Msg.code(724612469),
						theField.getName(),
						theField.getDeclaringClass().getCanonicalName(),
						myMethod.getName());
				throw new ConfigurationException(error);
			}
		}

		// TODO: LD:  Don't worry about the OperationEmbeddedParam.type() for now until we chose to implement it later

		return new ParamInitializationContext(
				operationEmbeddedParameter, parameterType, outerCollectionType, innerCollectionType);
	}

	@Nonnull
	private OperationEmbeddedParameter getOperationEmbeddedParameter(OperationEmbeddedParam operationParam) {
		final Annotation[] fieldAnnotationArray = new Annotation[] {operationParam};

		return new OperationEmbeddedParameter(
				myContext,
				myOperation.name(),
				operationParam.name(),
				operationParam.min(),
				operationParam.max(),
				ParametersUtil.extractDescription(fieldAnnotationArray),
				ParametersUtil.extractExamples(fieldAnnotationArray));
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
