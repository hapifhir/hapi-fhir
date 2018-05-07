package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.StringDt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Utilities for dealing with parameters resources in a version indepenedent way
 */
public class ParametersUtil {

	public static List<String> getNamedParameterValuesAsString(FhirContext theCtx, IBaseParameters theParameters, String theParameterName) {
		Validate.notNull(theParameters, "theParameters must not be null");
		RuntimeResourceDefinition resDef = theCtx.getResourceDefinition(theParameters.getClass());
		BaseRuntimeChildDefinition parameterChild = resDef.getChildByName("parameter");
		List<IBase> parameterReps = parameterChild.getAccessor().getValues(theParameters);

		List<String> retVal = new ArrayList<>();

		for (IBase nextParameter : parameterReps) {
			BaseRuntimeElementCompositeDefinition<?> nextParameterDef = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(nextParameter.getClass());
			BaseRuntimeChildDefinition nameChild = nextParameterDef.getChildByName("name");
			List<IBase> nameValues = nameChild.getAccessor().getValues(nextParameter);
			Optional<? extends IPrimitiveType<?>> nameValue = nameValues
				.stream()
				.filter(t -> t instanceof IPrimitiveType<?>)
				.map(t -> ((IPrimitiveType<?>) t))
				.findFirst();
			if (!nameValue.isPresent() || !theParameterName.equals(nameValue.get().getValueAsString())) {
				continue;
			}

			BaseRuntimeChildDefinition valueChild = nextParameterDef.getChildByName("value[x]");
			List<IBase> valueValues = valueChild.getAccessor().getValues(nextParameter);
			valueValues
				.stream()
				.filter(t->t instanceof IPrimitiveType<?>)
				.map(t->((IPrimitiveType<?>)t).getValueAsString())
				.filter(StringUtils::isNotBlank)
				.forEach(retVal::add);

		}

		return retVal;
	}

	private static void addClientParameter(FhirContext theContext, Object theValue, IBaseResource theTargetResource, BaseRuntimeChildDefinition paramChild, BaseRuntimeElementCompositeDefinition<?> paramChildElem, String theName) {
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
			throw new IllegalArgumentException("Don't know how to handle value of type " + theValue.getClass() + " for paramater " + theName);
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

	public static IBaseParameters newInstance(FhirContext theContext) {
		Validate.notNull(theContext, "theContext must not be null");
		return (IBaseParameters) theContext.getResourceDefinition("Parameters").newInstance();
	}

}
