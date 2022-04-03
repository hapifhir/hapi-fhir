package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.StringDt;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utilities for dealing with parameters resources in a version indepenedent way
 */
public class ParametersUtil {

	public static Optional<String> getNamedParameterValueAsString(FhirContext theCtx, IBaseParameters theParameters, String theParameterName) {
		Function<IPrimitiveType<?>, String> mapper = t -> defaultIfBlank(t.getValueAsString(), null);
		return extractNamedParameters(theCtx, theParameters, theParameterName, mapper).stream().findFirst();
	}

	public static List<String> getNamedParameterValuesAsString(FhirContext theCtx, IBaseParameters theParameters, String theParameterName) {
		Function<IPrimitiveType<?>, String> mapper = t -> defaultIfBlank(t.getValueAsString(), null);
		return extractNamedParameters(theCtx, theParameters, theParameterName, mapper);
	}

	public static List<Integer> getNamedParameterValuesAsInteger(FhirContext theCtx, IBaseParameters theParameters, String theParameterName) {
		Function<IPrimitiveType<?>, Integer> mapper = t -> (Integer) t.getValue();
		return extractNamedParameters(theCtx, theParameters, theParameterName, mapper);
	}

	public static Optional<Integer> getNamedParameterValueAsInteger(FhirContext theCtx, IBaseParameters theParameters, String theParameterName) {
		return getNamedParameterValuesAsInteger(theCtx, theParameters, theParameterName).stream().findFirst();
	}

	public static Optional<IBase> getNamedParameter(FhirContext theCtx, IBaseResource theParameters, String theParameterName) {
		return getNamedParameters(theCtx, theParameters, theParameterName).stream().findFirst();
	}

	public static List<IBase> getNamedParameters(FhirContext theCtx, IBaseResource theParameters, String theParameterName) {
		Validate.notNull(theParameters, "theParameters must not be null");
		RuntimeResourceDefinition resDef = theCtx.getResourceDefinition(theParameters.getClass());
		BaseRuntimeChildDefinition parameterChild = resDef.getChildByName("parameter");
		List<IBase> parameterReps = parameterChild.getAccessor().getValues(theParameters);

		return parameterReps
			.stream()
			.filter(param -> {
				BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(param.getClass());
				BaseRuntimeChildDefinition nameChild = nextParameterDef.getChildByName("name");
				List<IBase> nameValues = nameChild.getAccessor().getValues(param);
				Optional<? extends IPrimitiveType<?>> nameValue = nameValues
					.stream()
					.filter(t -> t instanceof IPrimitiveType<?>)
					.map(t -> ((IPrimitiveType<?>) t))
					.findFirst();
				return nameValue.isPresent() && theParameterName.equals(nameValue.get().getValueAsString());
			})
			.collect(Collectors.toList());

	}

	public static Optional<IBase> getParameterPart(FhirContext theCtx, IBase theParameter, String theParameterName) {
		BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(theParameter.getClass());
		BaseRuntimeChildDefinition valueChild = nextParameterDef.getChildByName("part");
		List<IBase> parts = valueChild.getAccessor().getValues(theParameter);

		for (IBase nextPart : parts) {
			Optional<IPrimitiveType> name = theCtx.newTerser().getSingleValue(nextPart, "name", IPrimitiveType.class);
			if (name.isPresent() && theParameterName.equals(name.get().getValueAsString())) {
				return Optional.of(nextPart);
			}
		}

		return Optional.empty();
	}

	public static Optional<IBase> getParameterPartValue(FhirContext theCtx, IBase theParameter, String theParameterName) {
		Optional<IBase> part = getParameterPart(theCtx, theParameter, theParameterName);
		if (part.isPresent()) {
			return theCtx.newTerser().getSingleValue(part.get(), "value[x]", IBase.class);
		} else {
			return Optional.empty();
		}
	}

	public static String getParameterPartValueAsString(FhirContext theCtx, IBase theParameter, String theParameterName) {
		return getParameterPartValue(theCtx, theParameter, theParameterName).map(t -> (IPrimitiveType<?>) t).map(t -> t.getValueAsString()).orElse(null);
	}

	private static <T> List<T> extractNamedParameters(FhirContext theCtx, IBaseParameters theParameters, String theParameterName, Function<IPrimitiveType<?>, T> theMapper) {
		List<T> retVal = new ArrayList<>();

		List<IBase> namedParameters = getNamedParameters(theCtx, theParameters, theParameterName);
		for (IBase nextParameter : namedParameters) {
			BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(nextParameter.getClass());
			BaseRuntimeChildDefinition valueChild = nextParameterDef.getChildByName("value[x]");
			List<IBase> valueValues = valueChild.getAccessor().getValues(nextParameter);
			valueValues
				.stream()
				.filter(t -> t instanceof IPrimitiveType<?>)
				.map(t -> ((IPrimitiveType<?>) t))
				.map(theMapper)
				.filter(t -> t != null)
				.forEach(retVal::add);

		}
		return retVal;
	}

	private static void addClientParameter(FhirContext theContext, Object theValue, IBaseResource theTargetResource, BaseRuntimeChildDefinition paramChild, BaseRuntimeElementCompositeDefinition<?> paramChildElem, String theName) {
		Validate.notNull(theValue, "theValue must not be null");

		if (theValue instanceof IBaseResource) {
			IBase parameter = createParameterRepetition(theContext, theTargetResource, paramChild, paramChildElem, theName);
			paramChildElem.getChildByName("resource").getMutator().addValue(parameter, (IBaseResource) theValue);
		} else if (theValue instanceof IBaseDatatype) {
			IBase parameter = createParameterRepetition(theContext, theTargetResource, paramChild, paramChildElem, theName);
			paramChildElem.getChildByName("value[x]").getMutator().addValue(parameter, (IBaseDatatype) theValue);
		} else if (theValue instanceof Collection) {
			Collection<?> collection = (Collection<?>) theValue;
			for (Object next : collection) {
				addClientParameter(theContext, next, theTargetResource, paramChild, paramChildElem, theName);
			}
		} else {
			throw new IllegalArgumentException(Msg.code(1806) + "Don't know how to handle value of type " + theValue.getClass() + " for parameter " + theName);
		}
	}

	/**
	 * Add a parameter value to a Parameters resource
	 *
	 * @param theContext    The FhirContext
	 * @param theParameters The Parameters resource
	 * @param theName       The parametr name
	 * @param theValue      The parameter value (can be a {@link IBaseResource resource} or a {@link IBaseDatatype datatype})
	 */
	public static void addParameterToParameters(FhirContext theContext, IBaseParameters theParameters, String theName, Object theValue) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theParameters);
		BaseRuntimeChildDefinition paramChild = def.getChildByName("parameter");
		BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");

		addClientParameter(theContext, theValue, theParameters, paramChild, paramChildElem, theName);
	}

	/**
	 * Add a parameter value to a Parameters resource
	 *
	 * @param theContext           The FhirContext
	 * @param theParameters        The Parameters resource
	 * @param theName              The parameter name
	 * @param thePrimitiveDatatype The datatype, e.g. "string", or "uri"
	 * @param theValue             The value
	 */
	public static void addParameterToParameters(FhirContext theContext, IBaseParameters theParameters, String theName, String thePrimitiveDatatype, String theValue) {
		Validate.notBlank(thePrimitiveDatatype, "thePrimitiveDatatype must not be null or empty");

		BaseRuntimeElementDefinition<?> datatypeDef = theContext.getElementDefinition(thePrimitiveDatatype);
		IPrimitiveType<?> value = (IPrimitiveType<?>) datatypeDef.newInstance();
		value.setValueAsString(theValue);

		addParameterToParameters(theContext, theParameters, theName, value);
	}

	private static IBase createParameterRepetition(FhirContext theContext, IBaseResource theTargetResource, BaseRuntimeChildDefinition paramChild, BaseRuntimeElementCompositeDefinition<?> paramChildElem, String theName) {
		IBase parameter = paramChildElem.newInstance();
		paramChild.getMutator().addValue(theTargetResource, parameter);
		IPrimitiveType<?> value;
		value = createString(theContext, theName);
		paramChildElem.getChildByName("name").getMutator().addValue(parameter, value);
		return parameter;
	}

	public static IPrimitiveType<?> createString(FhirContext theContext, String theValue) {
		IPrimitiveType<?> value;
		if (theContext.getVersion().getVersion().isRi()) {
			value = (IPrimitiveType<?>) theContext.getElementDefinition("string").newInstance(theValue);
		} else {
			value = new StringDt(theValue);
		}
		return value;
	}

	public static IPrimitiveType<?> createUri(FhirContext theContext, String theValue) {
		IPrimitiveType<?> value = (IPrimitiveType<?>) theContext.getElementDefinition("uri").newInstance(theValue);
		return value;
	}

	public static IPrimitiveType<?> createCode(FhirContext theContext, String theValue) {
		IPrimitiveType<?> value = (IPrimitiveType<?>) theContext.getElementDefinition("code").newInstance(theValue);
		return value;
	}

	public static IBaseParameters newInstance(FhirContext theContext) {
		Validate.notNull(theContext, "theContext must not be null");
		return (IBaseParameters) theContext.getResourceDefinition("Parameters").newInstance();
	}

	@SuppressWarnings("unchecked")
	public static void addParameterToParametersBoolean(FhirContext theCtx, IBaseParameters theParameters, String theName, boolean theValue) {
		addParameterToParameters(theCtx, theParameters, theName, theCtx.getPrimitiveBoolean(theValue));
	}

	@SuppressWarnings("unchecked")
	public static void addParameterToParametersCode(FhirContext theCtx, IBaseParameters theParameters, String theName, String theValue) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theCtx.getElementDefinition("code").newInstance();
		value.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, value);
	}

	@SuppressWarnings("unchecked")
	public static void addParameterToParametersInteger(FhirContext theCtx, IBaseParameters theParameters, String theName, int theValue) {
		IPrimitiveType<Integer> count = (IPrimitiveType<Integer>) theCtx.getElementDefinition("integer").newInstance();
		count.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, count);
	}

	public static void addParameterToParametersLong(FhirContext theCtx, IBaseParameters theParameters, String theName, long theValue) {
		addParameterToParametersDecimal(theCtx, theParameters, theName, BigDecimal.valueOf(theValue));
	}

	public static void addParameterToParametersDecimal(FhirContext theCtx, IBaseParameters theParameters, String theName, BigDecimal theValue) {
		IPrimitiveType<BigDecimal> count = (IPrimitiveType<BigDecimal>) theCtx.getElementDefinition("decimal").newInstance();
		count.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, count);
	}

	public static void addParameterToParametersReference(FhirContext theCtx, IBaseParameters theParameters, String theName, String theReference) {
		IBaseReference target = (IBaseReference) theCtx.getElementDefinition("reference").newInstance();
		target.setReference(theReference);
		addParameterToParameters(theCtx, theParameters, theName, target);
	}

	@SuppressWarnings("unchecked")
	public static void addParameterToParametersString(FhirContext theCtx, IBaseParameters theParameters, String theName, String theValue) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theCtx.getElementDefinition("string").newInstance();
		value.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, value);
	}

	@SuppressWarnings("unchecked")
	public static void addParameterToParametersUri(FhirContext theCtx, IBaseParameters theParameters, String theName, String theValue) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theCtx.getElementDefinition("uri").newInstance();
		value.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, value);

	}

	/**
	 * Add a parameter with no value (typically because we'll be adding sub-parameters)
	 */
	public static IBase addParameterToParameters(FhirContext theContext, IBaseParameters theParameters, String theName) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theParameters);
		BaseRuntimeChildDefinition paramChild = def.getChildByName("parameter");
		BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");

		return createParameterRepetition(theContext, theParameters, paramChild, paramChildElem, theName);
	}

	public static void addPartCode(FhirContext theContext, IBase theParameter, String theName, String theCode) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theContext.getElementDefinition("code").newInstance();
		value.setValue(theCode);

		addPart(theContext, theParameter, theName, value);
	}

	public static void addPartInteger(FhirContext theContext, IBase theParameter, String theName, Integer theInteger) {
		IPrimitiveType<Integer> value = (IPrimitiveType<Integer>) theContext.getElementDefinition("integer").newInstance();
		value.setValue(theInteger);

		addPart(theContext, theParameter, theName, value);
	}

	public static void addPartString(FhirContext theContext, IBase theParameter, String theName, String theValue) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theContext.getElementDefinition("string").newInstance();
		value.setValue(theValue);

		addPart(theContext, theParameter, theName, value);
	}

	public static void addPartUrl(FhirContext theContext, IBase theParameter, String theName, String theCode) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theContext.getElementDefinition("url").newInstance();
		value.setValue(theCode);

		addPart(theContext, theParameter, theName, value);
	}

	public static void addPartBoolean(FhirContext theContext, IBase theParameter, String theName, Boolean theValue) {
		addPart(theContext, theParameter, theName, theContext.getPrimitiveBoolean(theValue));
	}

	public static void addPartDecimal(FhirContext theContext, IBase theParameter, String theName, Double theValue) {
		IPrimitiveType<BigDecimal> value = (IPrimitiveType<BigDecimal>) theContext.getElementDefinition("decimal").newInstance();
		value.setValue(theValue == null ? null : new BigDecimal(theValue));

		addPart(theContext, theParameter, theName, value);
	}

	public static void addPartCoding(FhirContext theContext, IBase theParameter, String theName, String theSystem, String theCode, String theDisplay) {
		IBase coding = theContext.getElementDefinition("coding").newInstance();

		BaseRuntimeElementCompositeDefinition<?> codingDef = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(coding.getClass());
		codingDef.getChildByName("system").getMutator().addValue(coding, createUri(theContext, theSystem));
		codingDef.getChildByName("code").getMutator().addValue(coding, createCode(theContext, theCode));
		codingDef.getChildByName("display").getMutator().addValue(coding, createString(theContext, theDisplay));

		addPart(theContext, theParameter, theName, coding);
	}

	public static void addPart(FhirContext theContext, IBase theParameter, String theName, IBase theValue) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theParameter.getClass());
		BaseRuntimeChildDefinition partChild = def.getChildByName("part");

		BaseRuntimeElementCompositeDefinition<?> partChildElem = (BaseRuntimeElementCompositeDefinition<?>) partChild.getChildByName("part");
		IBase part = partChildElem.newInstance();
		partChild.getMutator().addValue(theParameter, part);

		IPrimitiveType<String> name = (IPrimitiveType<String>) theContext.getElementDefinition("string").newInstance();
		name.setValue(theName);
		partChildElem.getChildByName("name").getMutator().addValue(part, name);

		if (theValue instanceof IBaseResource) {
			partChildElem.getChildByName("resource").getMutator().addValue(part, theValue);
		} else {
			partChildElem.getChildByName("value[x]").getMutator().addValue(part, theValue);
		}
	}

	public static void addPartResource(FhirContext theContext, IBase theParameter, String theName, IBaseResource theValue) {
		BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theParameter.getClass());
		BaseRuntimeChildDefinition partChild = def.getChildByName("part");

		BaseRuntimeElementCompositeDefinition<?> partChildElem = (BaseRuntimeElementCompositeDefinition<?>) partChild.getChildByName("part");
		IBase part = partChildElem.newInstance();
		partChild.getMutator().addValue(theParameter, part);

		IPrimitiveType<String> name = (IPrimitiveType<String>) theContext.getElementDefinition("string").newInstance();
		name.setValue(theName);
		partChildElem.getChildByName("name").getMutator().addValue(part, name);

		partChildElem.getChildByName("resource").getMutator().addValue(part, theValue);
	}

	public static List<String> getNamedParameterPartAsString(FhirContext theCtx, IBaseParameters theParameters, String thePartName, String theParameterName) {
		return extractNamedParameterPartsAsString(theCtx, theParameters, thePartName, theParameterName);
	}

	// TODO KHS need to consolidate duplicated functionality that came in from different branches
	private static List<String> extractNamedParameterPartsAsString(FhirContext theCtx, IBaseParameters theParameters, String thePartName, String theParameterName) {
		List<IBase> parameterReps = getParameterReps(theCtx, theParameters);

		List<String> retVal = new ArrayList<>();

		for (IBase nextParameter : parameterReps) {
			BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(nextParameter.getClass());
			Optional<? extends IPrimitiveType<?>> nameValue = getNameValue(nextParameter, nextParameterDef);
			if (!nameValue.isPresent() || !thePartName.equals(nameValue.get().getValueAsString())) {
				continue;
			}

			BaseRuntimeChildDefinition partChild = nextParameterDef.getChildByName("part");
			List<IBase> partValues = partChild.getAccessor().getValues(nextParameter);
			for (IBase partValue : partValues) {
				BaseRuntimeElementCompositeDefinition<?> partParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(partValue.getClass());
				Optional<? extends IPrimitiveType<?>> partNameValue = getNameValue(partValue, partParameterDef);
				if (!partNameValue.isPresent() || !theParameterName.equals(partNameValue.get().getValueAsString())) {
					continue;
				}
				BaseRuntimeChildDefinition valueChild = partParameterDef.getChildByName("value[x]");
				List<IBase> valueValues = valueChild.getAccessor().getValues(partValue);
				valueValues
					.stream()
					.filter(t -> t instanceof IPrimitiveType<?>)
					.map(t -> ((IPrimitiveType<String>) t))
					.map(t -> defaultIfBlank(t.getValueAsString(), null))
					.filter(t -> t != null)
					.forEach(retVal::add);

			}
		}
		return retVal;
	}

	private static List<IBase> getParameterReps(FhirContext theCtx, IBaseParameters theParameters) {
		Validate.notNull(theParameters, "theParameters must not be null");
		RuntimeResourceDefinition resDef = theCtx.getResourceDefinition(theParameters.getClass());
		BaseRuntimeChildDefinition parameterChild = resDef.getChildByName("parameter");
		return parameterChild.getAccessor().getValues(theParameters);
	}

	private static Optional<? extends IPrimitiveType<?>> getNameValue(IBase nextParameter, BaseRuntimeElementCompositeDefinition<?> theNextParameterDef) {
		BaseRuntimeChildDefinition nameChild = theNextParameterDef.getChildByName("name");
		List<IBase> nameValues = nameChild.getAccessor().getValues(nextParameter);
		return nameValues
			.stream()
			.filter(t -> t instanceof IPrimitiveType<?>)
			.map(t -> ((IPrimitiveType<?>) t))
			.findFirst();
	}

	@Nullable
	public static String extractDescription(AnnotatedElement theType) {
		Description description = theType.getAnnotation(Description.class);
		if (description != null) {
			return extractDescription(description);
		} else {
			return null;
		}
	}

	@Nullable
	public static String extractDescription(Description desc) {
		String description = desc.value();
		if (isBlank(description)) {
			description = desc.formalDefinition();
		}
		if (isBlank(description)) {
			description = desc.shortDefinition();
		}
		return defaultIfBlank(description, null);
	}

	@Nullable
	public static String extractShortDefinition(AnnotatedElement theType) {
		Description description = theType.getAnnotation(Description.class);
		if (description != null) {
			return defaultIfBlank(description.shortDefinition(), null);
		} else {
			return null;
		}
	}

	public static String extractDescription(Annotation[] theParameterAnnotations) {
		for (Annotation next : theParameterAnnotations) {
			if (next instanceof Description) {
				return extractDescription((Description)next);
			}
		}
		return null;
	}

	public static List<String> extractExamples(Annotation[] theParameterAnnotations) {
		ArrayList<String> retVal = null;
		for (Annotation next : theParameterAnnotations) {
			if (next instanceof Description) {
				String[] examples = ((Description) next).example();
				if (examples.length > 0) {
					if (retVal == null) {
						retVal = new ArrayList<>();
					}
					retVal.addAll(Arrays.asList(examples));
				}
			}
		}
		return retVal;
	}
}
