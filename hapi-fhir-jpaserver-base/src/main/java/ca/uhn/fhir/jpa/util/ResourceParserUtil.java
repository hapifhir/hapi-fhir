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
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ResourceParserUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceParserUtil.class);

	private ResourceParserUtil() {}

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
}
