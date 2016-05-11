package ca.uhn.fhir.rest.param;

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

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TransactionParameter implements IParameter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionParameter.class);	
	private FhirContext myContext;
	private ParamStyle myParamStyle;
	private Class<? extends IBaseResource> myResourceBundleType;
	
	public TransactionParameter(FhirContext theContext) {
		myContext = theContext;
	}

	private String createParameterTypeError(Method theMethod) {
		return "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName() + " but is not of type List<" + IResource.class.getCanonicalName() + "> or Bundle";
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName() + " but can not be a collection of collections");
		}
		if (theParameterType.equals(Bundle.class)) {
			myParamStyle = ParamStyle.DSTU1_BUNDLE;
			if (theInnerCollectionType != null) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		} else if (Modifier.isInterface(theParameterType.getModifiers()) == false && IBaseResource.class.isAssignableFrom(theParameterType)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> parameterType = (Class<? extends IBaseResource>) theParameterType;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(parameterType);
			if ("Bundle".equals(def.getName())) {
				myParamStyle = ParamStyle.RESOURCE_BUNDLE;
				myResourceBundleType = parameterType;
			} else {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
		} else {
			if (theInnerCollectionType.equals(List.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
			if (theParameterType.equals(IResource.class) == false) {
				throw new ConfigurationException(createParameterTypeError(theMethod));
			}
			myParamStyle = ParamStyle.RESOURCE_LIST;
		}
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		// nothing

	}
	
	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		// TODO: don't use a default encoding, just fail!
		EncodingEnum encoding = RestfulServerUtils.determineRequestEncoding(theRequest);

		IParser parser = encoding.newParser(theRequest.getServer().getFhirContext());

		Reader reader;
		reader = ResourceParameter.createRequestReader(theRequest);

		switch (myParamStyle) {
			case DSTU1_BUNDLE: {
				Bundle bundle;
				bundle = parser.parseBundle(reader);
				return bundle;
			}
			case RESOURCE_LIST: {
				Bundle bundle = parser.parseBundle(reader);
				ArrayList<IResource> resourceList = new ArrayList<IResource>();
				for (BundleEntry next : bundle.getEntries()) {
					if (next.getResource() != null) {
						resourceList.add(next.getResource());
					}
				}
				return resourceList;
			}
			case RESOURCE_BUNDLE:
				return parser.parseResource(myResourceBundleType, reader);
		}

		throw new IllegalStateException("Unknown type: " + myParamStyle); // should not happen
	}

	public ParamStyle getParamStyle() {
		return myParamStyle;
	}

	public enum ParamStyle {
		/** Old style bundle (defined in hapi-fhir-base) */
		DSTU1_BUNDLE,
		/** New style bundle (defined in hapi-fhir-structures-* as a resource definition itself */
		RESOURCE_BUNDLE,
		/** List of resources */
		RESOURCE_LIST
	}

}
