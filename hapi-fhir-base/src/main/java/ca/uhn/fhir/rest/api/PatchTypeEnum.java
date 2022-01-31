package ca.uhn.fhir.rest.api;

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
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Parameter type for methods annotated with {@link Patch}
 */
public enum PatchTypeEnum {

	JSON_PATCH(Constants.CT_JSON_PATCH),
	XML_PATCH(Constants.CT_XML_PATCH),
	FHIR_PATCH_JSON(Constants.CT_FHIR_JSON_NEW),
	FHIR_PATCH_XML(Constants.CT_FHIR_XML_NEW);

	private static volatile Map<String, PatchTypeEnum> ourContentTypeToPatchType;
	private final String myContentType;

	PatchTypeEnum(String theContentType) {
		myContentType = theContentType;
	}

	public String getContentType() {
		return myContentType;
	}

	@Nonnull
	public static PatchTypeEnum forContentTypeOrThrowInvalidRequestException(FhirContext theContext, String theContentType) {
		String contentType = defaultString(theContentType);
		int semiColonIdx = contentType.indexOf(';');
		if (semiColonIdx != -1) {
			contentType = theContentType.substring(0, semiColonIdx);
		}
		contentType = contentType.trim();


		Map<String, PatchTypeEnum> map = ourContentTypeToPatchType;
		if (map == null) {
			map = new HashMap<>();
			for (PatchTypeEnum next : values()) {
				map.put(next.getContentType(), next);
			}
			ourContentTypeToPatchType = map;
		}

		PatchTypeEnum retVal = map.get(contentType);
		if (retVal == null) {
			if (isBlank(contentType)) {
				String msg = theContext.getLocalizer().getMessage(PatchTypeEnum.class, "missingPatchContentType");
				throw new InvalidRequestException(Msg.code(1964) + msg);
			}

			String msg = theContext.getLocalizer().getMessageSanitized(PatchTypeEnum.class, "invalidPatchContentType", contentType);
			throw new InvalidRequestException(Msg.code(1965) + msg);
		}

		return retVal;
	}
}
