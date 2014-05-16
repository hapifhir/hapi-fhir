package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class QueryParameterOrBinder implements IParamBinder {
	private final Class<? extends IQueryParameterOr> myType;

	QueryParameterOrBinder(Class<? extends IQueryParameterOr> theType) {
		myType = theType;
	}

	@Override
	public List<List<String>> encode(Object theString) throws InternalErrorException {
		List<String> retVal = ((IQueryParameterOr) theString).getValuesAsQueryTokens();
		return Collections.singletonList(retVal);
	}

	@Override
	public Object parse(List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		IQueryParameterOr dt;
		try {
			dt = myType.newInstance();
			if (theString.size() == 0 || theString.get(0).size() == 0) {
				return dt;
			}
			if (theString.size() > 1) {
				throw new InvalidRequestException("Multiple values detected");
			}
			
			dt.setValuesAsQueryTokens(theString.get(0));
		} catch (InstantiationException e) {
			throw new InternalErrorException(e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (SecurityException e) {
			throw new InternalErrorException(e);
		}
		return dt;
	}
}
