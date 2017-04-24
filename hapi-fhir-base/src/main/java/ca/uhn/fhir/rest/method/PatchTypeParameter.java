package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.defaultString;

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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class PatchTypeParameter implements IParameter {
	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		// nothing
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		return getTypeForRequestOrThrowInvalidRequestException(theRequest);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore
	}

	public static PatchTypeEnum getTypeForRequestOrThrowInvalidRequestException(RequestDetails theRequest) {
		String contentTypeAll = defaultString(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE));
		String contentType = contentTypeAll;
		int semiColonIdx = contentType.indexOf(';');
		if (semiColonIdx != -1) {
			contentType = contentTypeAll.substring(0, semiColonIdx);
		}
		contentType = contentType.trim();
		if (Constants.CT_JSON_PATCH.equals(contentType)) {
			return PatchTypeEnum.JSON_PATCH;
		} else if (Constants.CT_XML_PATCH.equals(contentType)) {
			return PatchTypeEnum.XML_PATCH;
		} else {
			throw new InvalidRequestException("Invalid Content-Type for PATCH operation: " + contentTypeAll);
		}
	}

}
