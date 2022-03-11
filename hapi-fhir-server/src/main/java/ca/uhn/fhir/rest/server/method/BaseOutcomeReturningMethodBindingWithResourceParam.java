package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.i18n.Msg;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private Integer myIdParamIndex;
	private String myResourceName;
	private int myResourceParameterIndex = -1;
	private Class<? extends IBaseResource> myResourceType;
	private Class<? extends IIdType> myIdParamType;
	private int myConditionalUrlIndex = -1;

	@SuppressWarnings("unchecked")
	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);

		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				if (resourceParameter.getMode() != ResourceParameter.Mode.RESOURCE) {
					continue;
				}
				if (myResourceType != null) {
					throw new ConfigurationException(Msg.code(454) + "Method " + theMethod.getName() + " on type " + theMethod.getDeclaringClass() + " has more than one @ResourceParam. Only one is allowed.");
				}

				myResourceType = resourceParameter.getResourceType();

				myResourceParameterIndex = index;
			} else if (next instanceof ConditionalParamBinder) {
				myConditionalUrlIndex = index;
			}
			index++;
		}

		if ((myResourceType == null || Modifier.isAbstract(myResourceType.getModifiers())) && (theProvider instanceof IResourceProvider)) {
			myResourceType = ((IResourceProvider) theProvider).getResourceType();
		}
		if (myResourceType == null) {
			throw new ConfigurationException(Msg.code(455) + "Unable to determine resource type for method: " + theMethod);
		}

		myResourceName = theContext.getResourceType(myResourceType);
		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
		if (myIdParamIndex != null) {
			myIdParamType = (Class<? extends IIdType>) theMethod.getParameterTypes()[myIdParamIndex];
		}

		if (resourceParameter == null) {
			throw new ConfigurationException(Msg.code(456) + "Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a resource parameter annotated with @"
					+ ResourceParam.class.getSimpleName());
		}

	}

	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		if (myIdParamIndex != null) {
			theParams[myIdParamIndex] = ParameterUtil.convertIdToType(theRequest.getId(), myIdParamType);
		}
		if (myResourceParameterIndex != -1) {
			IBaseResource resource = ((IBaseResource) theParams[myResourceParameterIndex]);
			if (resource != null) {
				String resourceId = resource.getIdElement().getIdPart();
				String urlId = theRequest.getId() != null ? theRequest.getId().getIdPart() : null;
				if (getContext().getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3) == false) {
					resource.setId(theRequest.getId());
				}

				String matchUrl = null;
				if (myConditionalUrlIndex != -1) {
					matchUrl = (String) theParams[myConditionalUrlIndex];
					matchUrl = defaultIfBlank(matchUrl, null);
				}
				validateResourceIdAndUrlIdForNonConditionalOperation(resource, resourceId, urlId, matchUrl);
			}
		}
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	protected void populateActionRequestDetailsForInterceptor(RequestDetails theRequestDetails, ActionRequestDetails theDetails, Object[] theMethodParams) {
		super.populateActionRequestDetailsForInterceptor(theRequestDetails, theDetails, theMethodParams);

		/*
		 * If the method has no parsed resource parameter, we parse here in order to have something for the interceptor.
		 */
		IBaseResource resource;
		if (myResourceParameterIndex != -1) {
			resource = (IBaseResource) theMethodParams[myResourceParameterIndex];
		} else {
			resource = ResourceParameter.parseResourceFromRequest(theRequestDetails, this, myResourceType);
		}

		theRequestDetails.setResource(resource);
		if (theDetails != null) {
			theDetails.setResource(resource);
		}

	}

	/**
	 * Subclasses may override
	 */
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		return;
	}

}
