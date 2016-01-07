package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Collection;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * Utilities for dealing with parameters resources
 */
public class ParametersUtil {

	public static void addParameterToParameters(FhirContext theContext, IBaseResource theTargetResource, Object sourceClientArgument, String theName) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theTargetResource);
		BaseRuntimeChildDefinition paramChild = def.getChildByName("parameter");
		BaseRuntimeElementCompositeDefinition<?> paramChildElem = (BaseRuntimeElementCompositeDefinition<?>) paramChild.getChildByName("parameter");

		addClientParameter(theContext, sourceClientArgument, theTargetResource, paramChild, paramChildElem, theName);
	}

	private static void addClientParameter(FhirContext theContext, Object theSourceClientArgument, IBaseResource theTargetResource, BaseRuntimeChildDefinition paramChild, BaseRuntimeElementCompositeDefinition<?> paramChildElem, String theName) {
		if (theSourceClientArgument instanceof IBaseResource) {
			IBase parameter = createParameterRepetition(theContext, theTargetResource, paramChild, paramChildElem, theName);
			paramChildElem.getChildByName("resource").getMutator().addValue(parameter, (IBaseResource) theSourceClientArgument);
		} else if (theSourceClientArgument instanceof IBaseDatatype) {
			IBase parameter = createParameterRepetition(theContext, theTargetResource, paramChild, paramChildElem, theName);
			paramChildElem.getChildByName("value[x]").getMutator().addValue(parameter, (IBaseDatatype) theSourceClientArgument);
		} else if (theSourceClientArgument instanceof Collection) {
			Collection<?> collection = (Collection<?>) theSourceClientArgument;
			for (Object next : collection) {
				addClientParameter(theContext, next, theTargetResource, paramChild, paramChildElem, theName);
			}
		} else {
			throw new IllegalArgumentException("Don't know how to handle value of type " + theSourceClientArgument.getClass() + " for paramater " + theName);
		}
	}

	private static IBase createParameterRepetition(FhirContext theContext, IBaseResource theTargetResource, BaseRuntimeChildDefinition paramChild, BaseRuntimeElementCompositeDefinition<?> paramChildElem, String theName) {
		IBase parameter = paramChildElem.newInstance();
		paramChild.getMutator().addValue(theTargetResource, parameter);
		IPrimitiveType<?> value;
		if (theContext.getVersion().getVersion().isRi()) {
			value = (IPrimitiveType<?>) theContext.getElementDefinition("string").newInstance(theName);
		} else {
			value = new StringDt(theName);
		}
		paramChildElem.getChildByName("name").getMutator().addValue(parameter, value);
		return parameter;
	}

	public static IBaseParameters newInstance(FhirContext theContext) {
		return (IBaseParameters) theContext.getResourceDefinition("Parameters").newInstance();
	}
}
