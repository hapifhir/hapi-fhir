package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class TransactionParamBinder implements IParameter {

	private boolean myParamIsBundle;
	private FhirContext myContext;
	private boolean myParamIsResource;

	public TransactionParamBinder(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		// nothing

	}

	@Override
	public Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		Bundle resource = (Bundle) theRequestContents;
		if (myParamIsBundle) {
			return resource;
		}

		ArrayList<IResource> retVal = new ArrayList<IResource>();
		for (BundleEntry next : resource.getEntries()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			}
		}

		return retVal;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName() + " but can not be a collection of collections");
		}
		if (theParameterType.equals(Bundle.class)) {
			myParamIsBundle=true;
			if (theInnerCollectionType!=null) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		} else if (Modifier.isInterface(theParameterType.getModifiers()) == false && IBaseResource.class.isAssignableFrom(theParameterType)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> parameterType = (Class<? extends IBaseResource>) theParameterType;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(parameterType);
			if ("Bundle".equals(def.getName())) {
				myParamIsResource = true;
			} else {
				throw new ConfigurationException(createParameterTypeError(theMethod));				
			}
		} else {
			myParamIsBundle=false;
			if (theInnerCollectionType.equals(List.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
			if (theParameterType.equals(IResource.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		}
	}

	private String createParameterTypeError(Method theMethod) {
		return "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName() + " but is not of type List<" + IResource.class.getCanonicalName()
				+ "> or Bundle";
	}

}
