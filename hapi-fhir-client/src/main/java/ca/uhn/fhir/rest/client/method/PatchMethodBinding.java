package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Base class for an operation that has a resource type but not a resource body in the
 * request body
 *
 */
public class PatchMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody {

	private int myPatchTypeParameterIndex = -1;
	private int myResourceParamIndex;

	public PatchMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider, Patch.class, theMethod.getAnnotation(Patch.class).type());

		for (ListIterator<Class<?>> iter = Arrays.asList(theMethod.getParameterTypes()).listIterator(); iter.hasNext();) {
			int nextIndex = iter.nextIndex();
			Class<?> next = iter.next();
			if (next.equals(PatchTypeEnum.class)) {
				myPatchTypeParameterIndex = nextIndex;
			}
			for (Annotation nextAnnotation : theMethod.getParameterAnnotations()[nextIndex]) {
				if (nextAnnotation instanceof ResourceParam) {
					myResourceParamIndex = nextIndex;
				}
			}
		}

		if (myPatchTypeParameterIndex == -1) {
			throw new ConfigurationException(Msg.code(1414) + "Method has no parameter of type " + PatchTypeEnum.class.getName() + " - " + theMethod.toString());
		}
		if (myResourceParamIndex == -1) {
			throw new ConfigurationException(Msg.code(1415) + "Method has no parameter with @" + ResourceParam.class.getSimpleName() + " annotation - " + theMethod.toString());
		}
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.PATCH;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.PATCH);
	}

	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IBaseResource theResource) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(getContext().getResourceType(theResource));

		return new HttpPostClientInvocation(getContext(), theResource, urlExtension.toString());
	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IIdType idDt = (IIdType) theArgs[getIdParameterIndex()];
		if (idDt == null) {
			throw new NullPointerException(Msg.code(1416) + "ID can not be null");
		}

		if (idDt.hasResourceType() == false) {
			idDt = idDt.withResourceType(getResourceName());
		} else if (getResourceName().equals(idDt.getResourceType()) == false) {
			throw new InvalidRequestException(Msg.code(1417) + "ID parameter has the wrong resource type, expected '" + getResourceName() + "', found: " + idDt.getResourceType());
		}

		PatchTypeEnum patchType = (PatchTypeEnum) theArgs[myPatchTypeParameterIndex];
		String body = (String) theArgs[myResourceParamIndex];

		HttpPatchClientInvocation retVal = createPatchInvocation(getContext(), idDt, patchType, body);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, IIdType theId, PatchTypeEnum thePatchType, String theBody) {
		HttpPatchClientInvocation retVal = new HttpPatchClientInvocation(theContext, theId, thePatchType.getContentType(), theBody);
		return retVal;
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, String theUrlPath, PatchTypeEnum thePatchType, String theBody) {
		HttpPatchClientInvocation retVal = new HttpPatchClientInvocation(theContext, theUrlPath, thePatchType.getContentType(), theBody);
		return retVal;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

	public static HttpPatchClientInvocation createPatchInvocation(FhirContext theContext, PatchTypeEnum thePatchType, String theBody, String theResourceType, Map<String, List<String>> theMatchParams) {
		StringBuilder urlBuilder = MethodUtil.createUrl(theResourceType, theMatchParams);
		String url = urlBuilder.toString();
		HttpPatchClientInvocation retVal = new HttpPatchClientInvocation(theContext, url, thePatchType.getContentType(), theBody);
		return retVal;
	}

}
