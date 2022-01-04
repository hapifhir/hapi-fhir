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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class HistoryMethodBinding extends BaseResourceReturningMethodBinding {

	private final Integer myIdParamIndex;
	private String myResourceName;
	private final RestOperationTypeEnum myResourceOperationType;

	public HistoryMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(toReturnType(theMethod, theProvider), theMethod, theContext, theProvider);

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());

		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (Modifier.isInterface(type.getModifiers())) {
			myResourceOperationType = RestOperationTypeEnum.HISTORY_SYSTEM;
		} else {
			if (myIdParamIndex != null) {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_INSTANCE;
			} else {
				myResourceOperationType = RestOperationTypeEnum.HISTORY_TYPE;
			}
		}

		if (type != IBaseResource.class && type != IResource.class) {
			myResourceName = theContext.getResourceType(type);
		} else {
			myResourceName = null;
		}

	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return myResourceOperationType;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return BundleTypeEnum.HISTORY;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IIdType id = null;
		String resourceName = myResourceName;
		if (myIdParamIndex != null) {
			id = (IIdType) theArgs[myIdParamIndex];
			if (id == null || isBlank(id.getValue())) {
				throw new NullPointerException(Msg.code(1441) + "ID can not be null");
			}
		}

		String historyId = id != null ? id.getIdPart() : null;
		HttpGetClientInvocation retVal = createHistoryInvocation(getContext(), resourceName, historyId, null, null, null);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], retVal.getParameters(), null);
			}
		}

		return retVal;
	}

	public static HttpGetClientInvocation createHistoryInvocation(FhirContext theContext, String theResourceName, String theId, IPrimitiveType<Date> theSince, Integer theLimit, DateRangeParam theAt) {
		StringBuilder b = new StringBuilder();
		if (theResourceName != null) {
			b.append(theResourceName);
			if (isNotBlank(theId)) {
				b.append('/');
				b.append(theId);
			}
		}
		if (b.length() > 0) {
			b.append('/');
		}
		b.append(Constants.PARAM_HISTORY);

		boolean haveParam = false;
		if (theSince != null && !theSince.isEmpty()) {
			haveParam = true;
			b.append('?').append(Constants.PARAM_SINCE).append('=').append(theSince.getValueAsString());
		}
		if (theLimit != null) {
			b.append(haveParam ? '&' : '?');
			haveParam = true;
			b.append(Constants.PARAM_COUNT).append('=').append(theLimit);
		}
		if (theAt != null) {
			for (DateParam next : theAt.getValuesAsQueryTokens()) {
				b.append(haveParam ? '&' : '?');
				haveParam = true;
				b.append(Constants.PARAM_AT);
				b.append("=");
				b.append(next.getValueAsQueryToken(theContext));
			}
		}

		HttpGetClientInvocation retVal = new HttpGetClientInvocation(theContext, b.toString());
		return retVal;
	}

	private static Class<? extends IBaseResource> toReturnType(Method theMethod, Object theProvider) {
		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IBaseResource> type = historyAnnotation.type();
		if (type != IBaseResource.class && type != IResource.class) {
			return type;
		}
		return null;
	}

}
