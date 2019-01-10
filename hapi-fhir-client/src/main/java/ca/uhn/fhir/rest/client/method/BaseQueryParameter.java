package ca.uhn.fhir.rest.client.method;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public abstract class BaseQueryParameter implements IParameter {

	public abstract List<QualifiedParamList> encode(FhirContext theContext, Object theObject) throws InternalErrorException;

	public abstract String getName();

	public abstract RestSearchParameterTypeEnum getParamType();

	/**
	 * Parameter should return true if {@link #parse(FhirContext, List)} should be called even if the query string
	 * contained no values for the given parameter
	 */
	public abstract boolean handlesMissing();

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore for now
	}

	public abstract boolean isRequired();

	public abstract Object parse(FhirContext theContext, List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException;

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		if (theSourceClientArgument == null) {
			if (isRequired()) {
				throw new NullPointerException("SearchParameter '" + getName() + "' is required and may not be null");
			}
		} else {
			List<QualifiedParamList> value = encode(theContext, theSourceClientArgument);

			for (QualifiedParamList nextParamEntry : value) {
				StringBuilder b = new StringBuilder();
				for (String str : nextParamEntry) {
					if (b.length() > 0) {
						b.append(",");
					}
					b.append(str);
				}

				String qualifier = nextParamEntry.getQualifier();
				String paramName = isNotBlank(qualifier) ? getName() + qualifier : getName();
				List<String> paramValues = theTargetQueryArguments.get(paramName);
				if (paramValues == null) {
					paramValues = new ArrayList<>(value.size());
					theTargetQueryArguments.put(paramName, paramValues);
				}

				paramValues.add(b.toString());
			}

		}
	}

}
