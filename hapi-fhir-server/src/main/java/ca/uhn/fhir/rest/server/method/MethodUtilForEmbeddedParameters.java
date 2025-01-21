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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// LUKETODO:  javadoc
public class MethodUtilForEmbeddedParameters {
	private final FhirContext myContext;
	private final Method myMethod;
	private final Operation myOperation;
	private final Class<?>[] myParameterTypes;
	private final Class<?> myOperationEmbeddedType;
	// LUKETODO:  think carefully about whether we need this at all..
	private final int myParamIndex;

	public MethodUtilForEmbeddedParameters(
			FhirContext theContext,
			Method theMethod,
			Operation theOperation,
			Class<?>[] theParameterTypes,
			Class<?> theOperationEmbeddedType,
			int theParamIndex) {
		myContext = theContext;
		myMethod = theMethod;
		myOperation = theOperation;
		myParameterTypes = theParameterTypes;
		myOperationEmbeddedType = theOperationEmbeddedType;
		myParamIndex = theParamIndex;
	}

	List<OuterContext> doStuffOuterOuter() {
		final List<OuterContext> outerContexts = new ArrayList<>();

		for (Field field : myOperationEmbeddedType.getDeclaredFields()) {
			outerContexts.add(doStuffOuter(field));
		}

		return outerContexts;
	}

	private OuterContext doStuffOuter(
			Field theField) {
		final String fieldName = theField.getName();
		final Class<?> fieldType = theField.getType();
		final Annotation[] fieldAnnotations = theField.getAnnotations();

		if (fieldAnnotations.length < 1) {
			throw new ConfigurationException(String.format(
					"%sNo annotations for field: %s for method: %s",
					Msg.code(9999926), fieldName, myMethod.getName()));
		}

		if (fieldAnnotations.length > 1) {
			// LUKETODO:  error
			throw new ConfigurationException(String.format(
					"%sMore than one annotation for field: %s for method: %s",
					Msg.code(999998), fieldName, myMethod.getName()));
		}

		// This is the parameter on the field in question on the type with embedded params
		// class:  ex
		// myCount
		final Annotation fieldAnnotation = fieldAnnotations[0];

		if (fieldAnnotation instanceof IdParam) {
			return new OuterContext(new NullParameter(), null);
		} else if (fieldAnnotation instanceof OperationEmbeddedParam) {

			final MethodUtilMutableLoopStateHolder stateHolder = doStuff(
					fieldType,
					theField,
				(OperationEmbeddedParam) fieldAnnotation);

			return new OuterContext(null, stateHolder);

		} else {
			// LUKETODO:  Nsome kind of Exception for now?
			return new OuterContext(null, null);
		}
	}

	private MethodUtilMutableLoopStateHolder doStuff(
			Class<?> theFieldType,
			Field theField,
			OperationEmbeddedParam theOperationEmbeddedParam) {

		final OperationEmbeddedParameter operationEmbeddedParameter =
			getOperationEmbeddedParameter(theOperationEmbeddedParam);

		Class<?> parameterType = theFieldType;
		Method methodToUse = myMethod;
		Class<? extends java.util.Collection<?>> outerCollectionType = null;
		Class<? extends java.util.Collection<?>> innerCollectionType = null;

		// LUKETODO:  simplify this as much as possible:

		if (Collection.class.isAssignableFrom(parameterType)) {
			// LUKETODO: unsafe cast
			innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
			parameterType = ReflectionUtil.getGenericCollectionTypeOfField(theField);
			// LUKETODO:  what is this and do we need it at all?
			if (parameterType == null && methodToUse.getDeclaringClass().isSynthetic()) {
				try {
					methodToUse = methodToUse
							.getDeclaringClass()
							.getSuperclass()
							.getMethod(methodToUse.getName(), myParameterTypes);
					parameterType =
							// LUKETODO:  what to do here if anything?
							ReflectionUtil.getGenericCollectionTypeOfMethodParameter(methodToUse, myParamIndex);
				} catch (NoSuchMethodException e) {
					throw new ConfigurationException(Msg.code(400) + "A method with name '"
							+ methodToUse.getName() + "' does not exist for super class '"
							+ methodToUse.getDeclaringClass().getSuperclass() + "'");
				}
			}
		}
		// LUKETODO:  null handling
		if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
			outerCollectionType = innerCollectionType;
			// LUKETODO: unsafe cast
			innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
			// LUKETODO: come up with another method to do this for field params
			parameterType = ReflectionUtil.getGenericCollectionTypeOfField(theField);
			// LUKETODO:
			//								declaredParameterType = parameterType;
		}
		// LUKETODO:  as a guard:  if this is still a Collection, then throw because
		// something went
		// wrong
		// LUKETODO:  null handling
		if (parameterType != null && Collection.class.isAssignableFrom(parameterType)) {
			throw new ConfigurationException(Msg.code(401) + "Argument #" + myParamIndex + " of Method '"
					+ methodToUse.getName()
					+ "' in type '"
					+ methodToUse.getDeclaringClass().getCanonicalName()
					+ "' is of an invalid generic type (can not be a collection of a collection of a collection)");
		}

		// LUKETODO:  do I need to worry about this:
		/*

		Class<?> newParameterType = elementDefinition.getImplementingClass();
		if (!declaredParameterType.isAssignableFrom(newParameterType)) {
			throw new ConfigurationException(Msg.code(405) + "Non assignable parameter typeName=\""
					+ operationParam.typeName() + "\" specified on method " + methodToUse);
		}
		parameterType = newParameterType;
		 */

		final MethodUtilParamInitializationContext paramContext = new MethodUtilParamInitializationContext(
				operationEmbeddedParameter, parameterType, outerCollectionType, innerCollectionType);

		return new MethodUtilMutableLoopStateHolder(paramContext);
	}

	@Nonnull
	private OperationEmbeddedParameter getOperationEmbeddedParameter(OperationEmbeddedParam operationParam) {
		final Annotation[] fieldAnnotationArray = new Annotation[] {operationParam};
		final String description = ParametersUtil.extractDescription(fieldAnnotationArray);
		final List<String> examples = ParametersUtil.extractExamples(fieldAnnotationArray);

		// LUKETODO:  capabilities statemenet provider
		// LUKETODO:  consider taking  ALL  hapi-fhir storage-cr INTO the clinical-reasoning
		// repo
		return new OperationEmbeddedParameter(
				myContext,
				myOperation.name(),
				operationParam.name(),
				operationParam.min(),
				operationParam.max(),
				description,
				examples);
	}
}
