package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * A ValueSet include or exclude applied to the current version of its CodeSystem instead of the version it names,
 * which no validation support module has
 *
 * @param codeSystemUrl the CodeSystem URL
 * @param unknownVersion the version the include or exclude names
 * @param currentVersion the version used instead, or {@literal null} if the current one has none
 * @since 9.0.0
 */
// Created by Claude Opus 5.5
public record CodeSystemVersionReplacement(
		@Nonnull String codeSystemUrl, @Nonnull String unknownVersion, @Nullable String currentVersion) {

	/**
	 * @return the CodeSystem as the include or exclude names it ({@literal system|version})
	 */
	@Nonnull
	public String unknownCanonical() {
		return UrlUtil.toCanonicalUrl(codeSystemUrl, unknownVersion);
	}

	/**
	 * @return the CodeSystem as it was used ({@literal system|version}, or {@literal system} when the current
	 * version has none)
	 */
	@Nonnull
	public String currentCanonical() {
		return UrlUtil.toCanonicalUrl(codeSystemUrl, currentVersion);
	}

	/**
	 * @param theFhirContext the context whose localizer provides the message
	 * @return the warning naming both versions
	 */
	@Nonnull
	public String toMessage(@Nonnull FhirContext theFhirContext) {
		String key = isNotBlank(currentVersion) ? "versionReplaced" : "versionReplacedWithUnversioned";
		return theFhirContext
				.getLocalizer()
				.getMessage(CodeSystemVersionReplacement.class, key, codeSystemUrl, unknownVersion, currentVersion);
	}
}
