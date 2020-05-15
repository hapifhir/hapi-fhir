package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.model.primitive.StringDt;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * Utilities for dealing with parameters resources in a version indepenedent way
 */
public class ParametersUtil {

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

	public static List<String> getNamedParameterPartAsString(FhirContext theCtx, IBaseParameters theParameters, String thePartName, String theParameterName) {
		return extractNamedParameterPartsAsString(theCtx, theParameters, thePartName, theParameterName);
	}

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

	private static <T> List<T> extractNamedParameters(FhirContext theCtx, IBaseParameters theParameters, String theParameterName, Function<IPrimitiveType<?>, T> theMapper) {
		List<IBase> parameterReps = getParameterReps(theCtx, theParameters);

		List<T> retVal = new ArrayList<>();

		for (IBase nextParameter : parameterReps) {
			BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(nextParameter.getClass());
			Optional<? extends IPrimitiveType<?>> nameValue = getNameValue(nextParameter, nextParameterDef);
			if (!nameValue.isPresent() || !theParameterName.equals(nameValue.get().getValueAsString())) {
				continue;
			}

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
			throw new IllegalArgumentException("Don't know how to handle value of type " + theValue.getClass() + " for parameter " + theName);
		}
	}

	/**
	 * Add a paratemer value to a Parameters resource
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
	 * Add a paratemer value to a Parameters resource
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
		IPrimitiveType<Boolean> value = (IPrimitiveType<Boolean>) theCtx.getElementDefinition("boolean").newInstance();
		value.setValue(theValue);
		addParameterToParameters(theCtx, theParameters, theName, value);
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

	public static void addPartString(FhirContext theContext, IBase theParameter, String theName, String theValue) {
		IPrimitiveType<String> value = (IPrimitiveType<String>) theContext.getElementDefinition("string").newInstance();
		value.setValue(theValue);

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

		partChildElem.getChildByName("value[x]").getMutator().addValue(part, theValue);
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
}
