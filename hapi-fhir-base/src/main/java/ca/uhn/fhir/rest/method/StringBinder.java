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

import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class StringBinder implements IParamBinder<String> {
	StringBinder() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, Object theString) throws InternalErrorException {
		String retVal = ((String) theString);
		List<?> retValList = Collections.singletonList(MethodUtil.singleton(new StringParam(retVal)));
		return (List<IQueryParameterOr<?>>) retValList;
	}

	@Override
	public String parse(String theName, List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		if (theParams.size() == 0 || theParams.get(0).size() == 0) {
			return "";
		}
		if (theParams.size() > 1 || theParams.get(0).size() > 1) {
			throw new InvalidRequestException("Multiple values detected for non-repeatable parameter '" + theName + "'. This server is not configured to allow multiple (AND) values for this param.");
		}

		return theParams.get(0).get(0);
	}

}
