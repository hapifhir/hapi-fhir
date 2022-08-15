package ca.uhn.fhir.rest.server.method;

/*-
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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SearchContainedModeParameter implements IParameter {

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		return getTypeForRequestOrThrowInvalidRequestException(theRequest);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore
	}

	public static SearchContainedModeEnum getTypeForRequestOrThrowInvalidRequestException(RequestDetails theRequest) {
		String[] paramValues = theRequest.getParameters().getOrDefault(Constants.PARAM_CONTAINED, Constants.EMPTY_STRING_ARRAY);
		if (paramValues.length > 0 && isNotBlank(paramValues[0])) {
			return SearchContainedModeEnum.fromCode(paramValues[0]);
		}
		return null;
	}

}
