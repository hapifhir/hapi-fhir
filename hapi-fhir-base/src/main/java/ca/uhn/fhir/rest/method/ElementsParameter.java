package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ElementsParameter implements IParameter {

	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;

	@SuppressWarnings("unchecked")
	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		if (theSourceClientArgument instanceof Collection) {
			StringBuilder values = new StringBuilder();
			for (String next : (Collection<String>) theSourceClientArgument) {
				if (isNotBlank(next)) {
					if (values.length() > 0) {
						values.append(',');
					}
					values.append(next);
				}
			}
			theTargetQueryArguments.put(Constants.PARAM_ELEMENTS, Collections.singletonList(values.toString()));
		} else {
			String elements = (String) theSourceClientArgument;
			if (elements != null) {
				theTargetQueryArguments.put(Constants.PARAM_ELEMENTS, Collections.singletonList(elements));
			}
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		Set<String> value = getElementsValueOrNull(theRequest);
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
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		}
	}

	public static Set<String> getElementsValueOrNull(RequestDetails theRequest) {
		String[] summary = theRequest.getParameters().get(Constants.PARAM_ELEMENTS);

		if (summary != null && summary.length > 0) {
			Set<String> retVal = new HashSet<String>();
			for (String next : summary) {
				StringTokenizer tok = new StringTokenizer(next, ",");
				while (tok.hasMoreTokens()) {
					String token = tok.nextToken();
					if (isNotBlank(token)) {
						retVal.add(token);
					}
				}
			}
			if (retVal.isEmpty()) {
				return null;
			}
			
			// Always include the meta element even for subsetted values
			retVal.add("meta");
			
			return retVal;
		}
		return null;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of type " + SummaryEnum.class + " but can not be a collection of collections");
		}
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, SummaryEnum.class.getSimpleName());
		}
	}

}
