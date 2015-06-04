package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class SortParameter implements IParameter {

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		SortSpec ss = (SortSpec) theSourceClientArgument;
		while (ss != null) {
			String name;
			if (ss.getOrder() == null) {
				name = Constants.PARAM_SORT;
			} else if (ss.getOrder() == SortOrderEnum.ASC) {
				name = Constants.PARAM_SORT_ASC;
			} else {
				name = Constants.PARAM_SORT_DESC;
			}

			if (ss.getParamName() != null) {
				if (!theTargetQueryArguments.containsKey(name)) {
					theTargetQueryArguments.put(name, new ArrayList<String>());
				}
				theTargetQueryArguments.get(name).add(ss.getParamName());
			}
			ss = ss.getChain();
		}
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, byte[] theRequestContents, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT)) {
			if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT_ASC)) {
				if (!theRequest.getParameters().containsKey(Constants.PARAM_SORT_DESC)) {
					return null;
				}
			}
		}

		SortSpec outerSpec = null;
		SortSpec innerSpec = null;
		for (String nextParamName : theRequest.getParameters().keySet()) {
			SortOrderEnum order;
			if (Constants.PARAM_SORT.equals(nextParamName)) {
				order = null;
			} else if (Constants.PARAM_SORT_ASC.equals(nextParamName)) {
				order = SortOrderEnum.ASC;
			} else if (Constants.PARAM_SORT_DESC.equals(nextParamName)) {
				order = SortOrderEnum.DESC;
			} else {
				continue;
			}

			String[] values = theRequest.getParameters().get(nextParamName);
			if (values != null) {
				for (String nextValue : values) {
					if (isNotBlank(nextValue)) {
						SortSpec spec = new SortSpec();
						spec.setOrder(order);
						spec.setParamName(nextValue);
						if (innerSpec == null) {
							outerSpec = spec;
							innerSpec = spec;
						} else {
							innerSpec.setChain(spec);
							innerSpec = spec;
						}
					}
				}
			}
		}

		return outerSpec;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null || theInnerCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName() + " but can not be of collection type");
		}
		if (!theParameterType.equals(SortSpec.class)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is annotated with @" + Sort.class.getName() + " but is an invalid type, must be: " + SortSpec.class.getCanonicalName());
		}

	}

}
