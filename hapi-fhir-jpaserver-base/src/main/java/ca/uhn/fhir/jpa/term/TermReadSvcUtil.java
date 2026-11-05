/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_VALUESET_URL;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_VALUESET_URL_PLUS_SLASH;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

public class TermReadSvcUtil {
	private static final Logger ourLog = LoggerFactory.getLogger(TermReadSvcUtil.class);

	public static Optional<String> getValueSetId(String theUrl) {
		if (!theUrl.startsWith(LOINC_GENERIC_VALUESET_URL)) return Optional.empty();

		if (!theUrl.startsWith(LOINC_GENERIC_VALUESET_URL_PLUS_SLASH)) {
			if (theUrl.equals(LOINC_GENERIC_VALUESET_URL)) {
				// the request is for the loinc all valueset which when loading was given the name: 'loinc-all'
				return Optional.of(LOINC_ALL_VALUESET_ID);
			}

			ourLog.error("Don't know how to extract ValueSet's ForcedId from url: " + theUrl);
			return Optional.empty();
		}

		String forcedId = theUrl.substring(LOINC_GENERIC_VALUESET_URL_PLUS_SLASH.length());
		return isBlank(forcedId) ? Optional.empty() : Optional.of(forcedId);
	}

	/**
	 * @param theUrl a code system or value set url, which may carry a version as <code>url|version</code>
	 * @return true if the url contains "loinc", ignoring case, and carries no version
	 */
	public static boolean isLoincUnversioned(String theUrl) {
		return Strings.CI.contains(theUrl, LOINC_LOW)
				&& !UrlUtil.parseCanonicalUrl(theUrl).hasVersion();
	}

	/**
	 * Whether an exclude that names the given code system version removes a concept of the given version. An exclude
	 * without a version removes every version. A concept without a version came from an include that names none, so
	 * every exclude applies to it.
	 */
	static boolean excludeAppliesToVersion(@Nullable String theExcludeVersion, @Nullable String theConceptVersion) {
		return isBlank(theExcludeVersion) || isBlank(theConceptVersion) || theExcludeVersion.equals(theConceptVersion);
	}
}
