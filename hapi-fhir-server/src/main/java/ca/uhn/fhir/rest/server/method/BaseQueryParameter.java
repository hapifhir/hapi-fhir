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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

	protected abstract boolean supportsRepetition();

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
		QualifierDetails qualifiers = QualifierDetails.extractQualifiersFromParameterName(theQualifier);
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
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {

		List<QualifiedParamList> paramList = new ArrayList<>();
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
				return parse(theRequest.getFhirContext(), paramList);
			}
			return null;
		}

		return parse(theRequest.getFhirContext(), paramList);

	}

}
