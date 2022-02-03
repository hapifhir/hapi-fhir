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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Method;
import java.util.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.binder.CollectionBinder;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class SummaryEnumParameter implements IParameter {

	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;


	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		Set<SummaryEnum> value = getSummaryValueOrNull(theRequest);
		if (value == null || value.isEmpty()) {
			return null;
		}
		
		if (myInnerCollectionType == null) {
			return value.iterator().next();
		}
		
		try {
			Collection retVal = myInnerCollectionType.newInstance();
			retVal.addAll(value);
			return retVal;
		} catch (InstantiationException e) {
			throw new InternalErrorException(Msg.code(378) + "Failed to instantiate " + myInnerCollectionType, e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(Msg.code(379) + "Failed to instantiate " + myInnerCollectionType, e);
		}
	}

	public static Set<SummaryEnum> getSummaryValueOrNull(RequestDetails theRequest) {
		String[] summary = theRequest.getParameters().get(Constants.PARAM_SUMMARY);

		Set<SummaryEnum> retVal;
		if (summary == null || summary.length == 0) {
			retVal = null;
		} else if (isBlank(summary[0])) {
			retVal = null;
		} else if (summary.length == 1 && summary[0].indexOf(',') == -1) {
			retVal = toCollectionOrNull(SummaryEnum.fromCode(summary[0]));
			if (retVal == null) {
				retVal = toCollectionOrNull(SummaryEnum.fromCode(summary[0].toLowerCase()));
			}
		} else {
			retVal = new HashSet<>();
			for (String nextParamValue : summary) {
				for (String nextParamValueTok : nextParamValue.split(",")) {
					SummaryEnum value = SummaryEnum.fromCode(nextParamValueTok);
					if (value == null) {
						value = SummaryEnum.fromCode(nextParamValueTok.toLowerCase());
					}
					if (value != null) {
						retVal.add(value);
					}
				}
			}
		}
		
		if (retVal != null) {
			if (retVal.contains(SummaryEnum.TEXT)) {
				if (retVal.size() > 1) {
					String msg = theRequest.getServer().getFhirContext().getLocalizer().getMessage(SummaryEnumParameter.class, "cantCombineText");
					throw new InvalidRequestException(Msg.code(380) + msg);
				}
			}
		}
		
		return retVal;
	}

	private static Set<SummaryEnum> toCollectionOrNull(SummaryEnum theFromCode) {
		if (theFromCode == null) {
			return null;
		}
		return Collections.singleton(theFromCode);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(381) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of type " + SummaryEnum.class + " but can not be a collection of collections");
		}
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, SummaryEnum.class.getSimpleName());
		}
	}

}
