package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public abstract class BaseQueryParameter implements IParameter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseQueryParameter.class);

	public abstract List<QualifiedParamList> encode(FhirContext theContext, Object theObject) throws InternalErrorException;

	public abstract String getName();

	public abstract RestSearchParameterTypeEnum getParamType();

	/**
	 * Returns null if blacklist is "none"
	 */
	public Set<String> getQualifierBlacklist() {
		return null;
	}

	/**
	 * Returns null if whitelist is "all"
	 */
	public Set<String> getQualifierWhitelist() {
		return null;
	}

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

	private void parseParams(RequestDetails theRequest, List<QualifiedParamList> paramList, String theQualifiedParamName, String theQualifier) {
		QualifierDetails qualifiers = SearchMethodBinding.extractQualifiersFromParameterName(theQualifier);
		if (!qualifiers.passes(getQualifierWhitelist(), getQualifierBlacklist())) {
			return;
		}

		String[] value = theRequest.getParameters().get(theQualifiedParamName);
		if (value != null) {
			for (String nextParam : value) {
				if (nextParam.contains(",") == false) {
					paramList.add(QualifiedParamList.singleton(theQualifier, nextParam));
				} else {
					paramList.add(QualifiedParamList.splitQueryStringByCommasIgnoreEscape(theQualifier, nextParam));
				}
			}
		}
	}

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
					paramValues = new ArrayList<String>(value.size());
					theTargetQueryArguments.put(paramName, paramValues);
				}

				paramValues.add(b.toString());
			}

		}
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {

		List<QualifiedParamList> paramList = new ArrayList<QualifiedParamList>();
		String name = getName();
		parseParams(theRequest, paramList, name, null);

		List<String> qualified = theRequest.getUnqualifiedToQualifiedNames().get(name);
		if (qualified != null) {
			for (String nextQualified : qualified) {
				parseParams(theRequest, paramList, nextQualified, nextQualified.substring(name.length()));
			}
		}

		if (paramList.isEmpty()) {

			ourLog.debug("No value for parameter '{}' - Qualified names {} and qualifier whitelist {}", new Object[] { getName(), qualified, getQualifierWhitelist() });

			if (handlesMissing()) {
				return parse(theRequest.getServer().getFhirContext(), paramList);
			}
			return null;
		}

		return parse(theRequest.getServer().getFhirContext(), paramList);

	}

}
