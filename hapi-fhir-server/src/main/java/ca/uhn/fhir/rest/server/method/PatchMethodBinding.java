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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.annotation.Nonnull;

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
			throw new ConfigurationException(Msg.code(370) + "Method has no parameter of type " + PatchTypeEnum.class.getName() + " - " + theMethod.toString());
		}
		if (myResourceParamIndex == -1) {
			throw new ConfigurationException(Msg.code(371) + "Method has no parameter with @" + ResourceParam.class.getSimpleName() + " annotation - " + theMethod.toString());
		}
	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		MethodMatchEnum retVal = super.incomingServerRequestMatchesMethod(theRequest);
		if (retVal.ordinal() > MethodMatchEnum.NONE.ordinal()) {
			PatchTypeParameter.getTypeForRequestOrThrowInvalidRequestException(theRequest);
		}
		return retVal;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.PATCH;
	}

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.PATCH);
	}



	@Override
	protected void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams) {
		IIdType id = theRequest.getId();
		id = UpdateMethodBinding.applyETagAsVersion(theRequest, id);
		theParams[getIdParameterIndex()] = id;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}


}
