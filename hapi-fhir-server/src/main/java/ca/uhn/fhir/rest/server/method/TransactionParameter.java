package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

public class TransactionParameter implements IParameter {

	// private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionParameter.class);
	private FhirContext myContext;
	private ParamStyle myParamStyle;
	private Class<? extends IBaseResource> myResourceBundleType;

	TransactionParameter(FhirContext theContext) {
		myContext = theContext;
	}

	private String createParameterTypeError(Method theMethod) {
		return "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + TransactionParam.class.getName()
			+ " but is not of type Bundle, IBaseResource, IBaseBundle, or List<" + IResource.class.getCanonicalName() + ">";
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(429) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @"
				+ TransactionParam.class.getName() + " but can not be a collection of collections");
		}
		if (Modifier.isInterface(theParameterType.getModifiers()) == false && IBaseResource.class.isAssignableFrom(theParameterType)) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> parameterType = (Class<? extends IBaseResource>) theParameterType;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(parameterType);
			if ("Bundle".equals(def.getName())) {
				myParamStyle = ParamStyle.RESOURCE_BUNDLE;
				myResourceBundleType = parameterType;
				return;
			}
		} else if (theParameterType.equals(IBaseResource.class) || theParameterType.equals(IBaseBundle.class)) {
			myParamStyle = ParamStyle.RESOURCE_BUNDLE;
			myResourceBundleType = myContext.getResourceDefinition("Bundle").getImplementingClass();
			return;
		} else if (theInnerCollectionType != null) {
			if (theInnerCollectionType.equals(List.class) == false) {
				throw new ConfigurationException(Msg.code(430) + createParameterTypeError(theMethod));
			}
			if (theParameterType.equals(IResource.class) == false) {
				throw new ConfigurationException(Msg.code(431) + createParameterTypeError(theMethod));
			}
			myParamStyle = ParamStyle.RESOURCE_LIST;
			return;
		}

		throw new ConfigurationException(Msg.code(432) + createParameterTypeError(theMethod));
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		IBaseResource parsedBundle = ResourceParameter.parseResourceFromRequest(theRequest, theMethodBinding, myResourceBundleType);

		switch (myParamStyle) {
			case RESOURCE_LIST:
				return BundleUtil.toListOfResources(myContext, (IBaseBundle) parsedBundle);
			case RESOURCE_BUNDLE:
			default:
				assert myParamStyle == ParamStyle.RESOURCE_BUNDLE;
				break;
		}

		return parsedBundle;
	}

	ParamStyle getParamStyle() {
		return myParamStyle;
	}

	public enum ParamStyle {
		/**
		 * New style bundle (defined in hapi-fhir-structures-* as a resource definition itself
		 */
		RESOURCE_BUNDLE,
		/**
		 * List of resources
		 */
		RESOURCE_LIST
	}

}
