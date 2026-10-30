package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The outcome of one in-memory expansion, built up while its includes and excludes are expanded: the expanded
 * ValueSet, messages explaining an expansion that came out empty, and the CodeSystem versions that
 * {@link InMemoryTerminologyServerValidationSupport#isUseCurrentCodeSystemVersionWhenNamedVersionMissing()} replaced. An expansion of a ValueSet that an include
 * names gets its own instance, whose replacements are merged into the including one.
 */
// Created by Claude Opus 5.5
class InMemoryExpansionResult {

	private org.hl7.fhir.r5.model.ValueSet myValueSet;
	private final List<String> myMessages = new ArrayList<>();

	/**
	 * The version an include or exclude named ({@literal system|version}) mapped to the one used instead
	 * ({@literal system|version}, or {@literal system} when the current one has no version), in the order
	 * they were replaced
	 */
	private final Map<String, String> myNamedToUsedCodeSystemVersions = new LinkedHashMap<>();

	void addCodeSystemVersionReplacement(
			String theCodeSystemUrl, String theNamedVersion, @Nullable String theUsedVersion) {
		myNamedToUsedCodeSystemVersions.putIfAbsent(
				UrlUtil.toCanonicalUrl(theCodeSystemUrl, theNamedVersion),
				UrlUtil.toCanonicalUrl(theCodeSystemUrl, theUsedVersion));
	}

	void addWarningsOf(InMemoryExpansionResult theSubExpansion) {
		theSubExpansion.myNamedToUsedCodeSystemVersions.forEach(myNamedToUsedCodeSystemVersions::putIfAbsent);
	}

	/**
	 * One warning for each CodeSystem version that was replaced, naming both versions
	 */
	List<String> getWarnings(FhirContext theFhirContext) {
		return myNamedToUsedCodeSystemVersions.entrySet().stream()
				.map(t -> {
					UrlUtil.CanonicalUrlParts named = UrlUtil.parseCanonicalUrl(t.getKey());
					String usedVersion =
							UrlUtil.parseCanonicalUrl(t.getValue()).versionId().orElse(null);
					return InMemoryTerminologyServerValidationSupport.createVersionReplacedMessage(
							theFhirContext, named.url(), named.versionId().orElseThrow(), usedVersion);
				})
				.toList();
	}

	/**
	 * The {@literal system|version} a code is looked up under in this expansion: a version an include named that
	 * was replaced by the current one maps to that one, anything else is returned as given.
	 */
	@Nullable
	String toUsedCodeSystemVersion(@Nullable String theCodeSystemUrlAndVersion) {
		return myNamedToUsedCodeSystemVersions.getOrDefault(theCodeSystemUrlAndVersion, theCodeSystemUrlAndVersion);
	}

	public void setValueSet(org.hl7.fhir.r5.model.ValueSet theValueSet) {
		myValueSet = theValueSet;
	}

	public void addMessage(String theMessage) {
		myMessages.add(theMessage);
	}

	public org.hl7.fhir.r5.model.ValueSet getValueSet() {
		return myValueSet;
	}

	public List<String> getMessages() {
		return myMessages;
	}
}
