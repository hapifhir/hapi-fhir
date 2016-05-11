package ca.uhn.fhir.rest.method;

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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class SummaryEnumParameter implements IParameter {

	@SuppressWarnings("rawtypes")
	private Class<? extends Collection> myInnerCollectionType;

	@SuppressWarnings("unchecked")
	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		if (theSourceClientArgument instanceof Collection) {
			List<String> values = new ArrayList<String>();
			for (SummaryEnum next : (Collection<SummaryEnum>) theSourceClientArgument) {
				if (next != null) {
					values.add(next.getCode());
				}
			}
			theTargetQueryArguments.put(Constants.PARAM_SUMMARY, values);
		} else {
			SummaryEnum ss = (SummaryEnum) theSourceClientArgument;
			if (ss != null) {
				theTargetQueryArguments.put(Constants.PARAM_SUMMARY, Collections.singletonList(ss.getCode()));
			}
		}
	}

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
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException("Failed to instantiate " + myInnerCollectionType, e);
		}
	}

	public static Set<SummaryEnum> getSummaryValueOrNull(RequestDetails theRequest) {
		String[] summary = theRequest.getParameters().get(Constants.PARAM_SUMMARY);

		Set<SummaryEnum> retVal;
		if (summary == null || summary.length == 0) {
			retVal = null;
		} else if (isBlank(summary[0])) {
			retVal = null;
		} else if (summary.length == 1) {
			retVal = toCollectionOrNull(SummaryEnum.fromCode(summary[0]));
			if (retVal == null) {
				retVal = toCollectionOrNull(SummaryEnum.fromCode(summary[0].toLowerCase()));
			}
		} else {
			retVal = new HashSet<SummaryEnum>();
			for (String next : summary) {
				SummaryEnum value = SummaryEnum.fromCode(next);
				if (value == null) {
					value = SummaryEnum.fromCode(next.toLowerCase());
				}
				if (value != null) {
					retVal.add(value);
				}
			}
		}
		
		if (retVal != null) {
			if (retVal.contains(SummaryEnum.TEXT)) {
				if (retVal.size() > 1) {
					String msg = theRequest.getServer().getFhirContext().getLocalizer().getMessage(SummaryEnumParameter.class, "cantCombineText");
					throw new InvalidRequestException(msg);
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
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of type " + SummaryEnum.class + " but can not be a collection of collections");
		}
		if (theInnerCollectionType != null) {
			myInnerCollectionType = CollectionBinder.getInstantiableCollectionType(theInnerCollectionType, SummaryEnum.class.getSimpleName());
		}
	}

}
