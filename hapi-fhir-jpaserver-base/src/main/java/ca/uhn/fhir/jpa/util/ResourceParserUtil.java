/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.decodeResource;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utility class for parsing and processing FHIR resource data.
 * Provides methods for text extraction, type determination, and externally stored resource handling.
 */
public class ResourceParserUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceParserUtil.class);

	private ResourceParserUtil() {}

	/**
	 * Extracts resource text from either the text field or decoded byte array.
	 */
	public static String getResourceText(
			byte[] theResourceBytes, String theResourceText, ResourceEncodingEnum theResourceEncoding) {
		if (theResourceText != null) {
			return theResourceText;
		} else {
			return decodeResource(theResourceBytes, theResourceEncoding);
		}
	}

	/**
	 * Determines the appropriate resource type to parse based on profile tags.
	 * If a custom type is registered for a profile tag, returns that type instead of the default.
	 */
	@SuppressWarnings("unchecked")
	public static <R extends IBaseResource> Class<R> determineTypeToParse(
			FhirContext theFhirContext, Class<R> theResourceType, @Nullable Collection<? extends BaseTag> tagList) {
		if (tagList == null || tagList.isEmpty() || !theFhirContext.hasDefaultTypeForProfile()) {
			return theResourceType;
		}

		for (BaseTag nextTag : tagList) {
			TagDefinition tagDef = nextTag.getTag();
			if (tagDef == null || tagDef.getTagType() != TagTypeEnum.PROFILE || isBlank(tagDef.getCode())) {
				continue;
			}

			String profile = tagDef.getCode();
			Class<? extends IBaseResource> newType = theFhirContext.getDefaultTypeForProfile(profile);
			if (newType != null && theResourceType.isAssignableFrom(newType)) {
				ourLog.debug("Using custom type {} for profile: {}", newType.getName(), profile);
				return (Class<R>) newType;
			}
		}

		return theResourceType;
	}

	/**
	 * Details for an externally stored resource, containing provider ID and storage address.
	 */
	public record EsrResourceDetails(String providerId, String address) {}

	/**
	 * Parses externally stored resource address in format "providerId:address".
	 */
	public static EsrResourceDetails getEsrResourceDetails(String theResourceText) {
		int colonIndex = theResourceText.indexOf(':');
		Validate.isTrue(colonIndex > 0, "Invalid ESR address: %s", theResourceText);
		String providerId = theResourceText.substring(0, colonIndex);
		String address = theResourceText.substring(colonIndex + 1);
		Validate.notBlank(providerId, "No provider ID in ESR address: %s", theResourceText);
		Validate.notBlank(address, "No address in ESR address: %s", theResourceText);
		return new EsrResourceDetails(providerId, address);
	}
}
