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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

final class StringBinder implements IParamBinder {
	StringBinder() {
	}

	@Override
	public List<QualifiedParamList> encode(FhirContext theContext, Object theString) throws InternalErrorException {
		String retVal = ((String) theString);
		return Collections.singletonList(QualifiedParamList.singleton(retVal));
	}

	@Override
	public Object parse(List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		if (theParams.size() == 0 || theParams.get(0).size() == 0) {
			return "";
		}
		if (theParams.size() > 1 || theParams.get(0).size() > 1) {
			throw new InvalidRequestException("Multiple values detected");
		}

		return theParams.get(0).get(0);
	}

}
