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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.binder.CollectionBinder;
import ca.uhn.fhir.rest.server.ElementsSupportEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ElementsParameter implements IParameter {

	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		Set<String> value = getElementsValueOrNull(theRequest, false);
		if (value == null || value.isEmpty()) {
			return null;
		}

		if (myInnerCollectionType == null) {
			return StringUtils.join(value, ',');
		}

		try {
			Collection retVal = myInnerCollectionType.newInstance();
			retVal.addAll(value);
			return retVal;
		} catch (InstantiationException e) {
			throw new InternalErrorException(Msg.code(413) + "Failed to instantiate " + myInnerCollectionType, e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(Msg.code(414) + "Failed to instantiate " + myInnerCollectionType, e);
		}
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(415) + "Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of type " + SummaryEnum.class
				+ " but can not be a collection of collections");
		}
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, SummaryEnum.class.getSimpleName());
		}
	}

	public static Set<String> getElementsValueOrNull(RequestDetails theRequest, boolean theExclude) {
		boolean standardMode = theRequest.getServer().getElementsSupport() != ElementsSupportEnum.EXTENDED;
		if (theExclude && standardMode) {
			return null;
		}

		String paramName = Constants.PARAM_ELEMENTS;
		if (theExclude) {
			paramName = Constants.PARAM_ELEMENTS + Constants.PARAM_ELEMENTS_EXCLUDE_MODIFIER;
		}
		String[] elementsValues = theRequest.getParameters().get(paramName);

		if (elementsValues != null && elementsValues.length > 0) {
			Set<String> retVal = new HashSet<>();
			for (String next : elementsValues) {
				StringTokenizer tok = new StringTokenizer(next, ",");
				while (tok.hasMoreTokens()) {
					String token = tok.nextToken();
					if (isNotBlank(token)) {
						if (token.contains("."))
							if (standardMode) {
								continue;
							}
						retVal.add(token);
					}
				}
			}
			if (retVal.isEmpty()) {
				return null;
			}

			return retVal;
		}
		return null;
	}

}
