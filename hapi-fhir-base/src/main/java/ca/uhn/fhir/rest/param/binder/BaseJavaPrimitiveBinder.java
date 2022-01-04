package ca.uhn.fhir.rest.param.binder;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

abstract class BaseJavaPrimitiveBinder<T>implements IParamBinder<T> {

	public BaseJavaPrimitiveBinder() {
		super();
	}

	protected abstract String doEncode(T theString);

	protected abstract T doParse(String theString);

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, T theString) throws InternalErrorException {
		String retVal = doEncode(theString);
		if (isBlank(retVal)) {
			return Collections.emptyList();
		}
		List<?> retValList = Collections.singletonList(ParameterUtil.singleton(new StringParam(retVal), null));
		return (List<IQueryParameterOr<?>>) retValList;
	}

	@Override
	public T parse(FhirContext theContext, String theName, List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		if (theParams.size() == 0 || theParams.get(0).size() == 0) {
			return null;
		}
		if (theParams.size() > 1 || theParams.get(0).size() > 1) {
			throw new InvalidRequestException(Msg.code(1955) + "Multiple values detected for non-repeatable parameter '" + theName + "'. This server is not configured to allow multiple (AND) values for this param.");
		}
	
		T value = doParse(theParams.get(0).get(0));
		return value;
	}

}
