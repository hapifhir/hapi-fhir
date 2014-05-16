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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class QueryParameterTypeBinder implements IParamBinder {
	private final Class<? extends IQueryParameterType> myType;

	QueryParameterTypeBinder(Class<? extends IQueryParameterType> theType) {
		myType = theType;
	}

	@Override
	public List<List<String>> encode(Object theString) throws InternalErrorException {
		String retVal = ((IQueryParameterType) theString).getValueAsQueryToken();
		return Collections.singletonList(Collections.singletonList(retVal));
	}

	@Override
	public Object parse(List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		IQueryParameterType dt;
		try {
			dt = myType.newInstance();
			if (theParams.size() == 0 || theParams.get(0).size() == 0) {
				return dt;
			}
			if (theParams.size() > 1 || theParams.get(0).size() > 1) {
				throw new InvalidRequestException("Multiple values detected");
			}
			
			dt.setValueAsQueryToken(theParams.get(0).getQualifier(), theParams.get(0).get(0));
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
