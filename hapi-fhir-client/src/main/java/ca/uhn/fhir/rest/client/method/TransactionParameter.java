package ca.uhn.fhir.rest.client.method;

/*-
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class TransactionParameter implements IParameter {

	// private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionParameter.class);
	private FhirContext myContext;
	private ParamStyle myParamStyle;

	public TransactionParameter(FhirContext theContext) {
		myContext = theContext;
	}

	private String createParameterTypeError(Method theMethod) {
		return "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName()
				+ " but is not of type List<" + IResource.class.getCanonicalName() + "> or Bundle";
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(1454) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @"
					+ TransactionParam.class.getName() + " but can not be a collection of collections");
		}
		if (Modifier.isInterface(theParameterType.getModifiers()) == false && IBaseResource.class.isAssignableFrom(theParameterType)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> parameterType = (Class<? extends IBaseResource>) theParameterType;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(parameterType);
			if ("Bundle".equals(def.getName())) {
				myParamStyle = ParamStyle.RESOURCE_BUNDLE;
			} else {
				throw new ConfigurationException(Msg.code(1455) + createParameterTypeError(theMethod));
			}
		} else {
			if (theInnerCollectionType.equals(List.class) == false) {
				throw new ConfigurationException(Msg.code(1456) + createParameterTypeError(theMethod));
			}
			if (theParameterType.equals(IResource.class) == false && theParameterType.equals(IBaseResource.class) == false) {
				throw new ConfigurationException(Msg.code(1457) + createParameterTypeError(theMethod));
			}
			myParamStyle = ParamStyle.RESOURCE_LIST;
		}
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		// nothing

	}

	public ParamStyle getParamStyle() {
		return myParamStyle;
	}

	public enum ParamStyle {
		/** New style bundle (defined in hapi-fhir-structures-* as a resource definition itself */
		RESOURCE_BUNDLE,
		/** List of resources */
		RESOURCE_LIST
	}

}
