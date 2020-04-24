package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;

/**
 * Parameter type for methods annotated with {@link Patch}
 */
public enum PatchTypeEnum {

	JSON_PATCH(Constants.CT_JSON_PATCH),
	XML_PATCH(Constants.CT_XML_PATCH);

	private final String myContentType;

	PatchTypeEnum(String theContentType) {
		myContentType = theContentType;
	}

	public String getContentType() {
		return myContentType;
	}

	public static PatchTypeEnum forContentTypeOrThrowInvalidRequestException(String theContentType) {
		String contentType = theContentType;
		int semiColonIdx = contentType.indexOf(';');
		if (semiColonIdx != -1) {
			contentType = theContentType.substring(0, semiColonIdx);
		}
		contentType = contentType.trim();
		if (Constants.CT_JSON_PATCH.equals(contentType)) {
			return JSON_PATCH;
		} else if (Constants.CT_XML_PATCH.equals(contentType)) {
			return XML_PATCH;
		} else {
			throw new InvalidRequestException("Invalid Content-Type for PATCH operation: " + UrlUtil.sanitizeUrlPart(theContentType));
		}
	}
}
