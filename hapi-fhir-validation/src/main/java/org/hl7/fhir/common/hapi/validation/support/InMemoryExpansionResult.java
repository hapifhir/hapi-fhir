package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The outcome of one in-memory expansion, built up while its includes and excludes are expanded: the expanded
 * ValueSet, messages explaining an expansion that came out empty, and the CodeSystem versions that
 * {@link InMemoryTerminologyServerValidationSupport#isUseCurrentVersionForUnknownCodeSystemVersion()} replaced. An expansion of a ValueSet that an include
 * names gets its own instance, whose replacements are merged into the including one.
 */
// Created by Claude Opus 5.5
class InMemoryExpansionResult {

	private org.hl7.fhir.r5.model.ValueSet myValueSet;
	private final List<String> myMessages = new ArrayList<>();

	/**
	 * The CodeSystem versions that were replaced, keyed by the {@literal system|version} the include or exclude named,
	 * in the order they were replaced
	 */
	private final Map<String, CodeSystemVersionReplacement> myReplacementsByUnknownCanonical = new LinkedHashMap<>();

	void addCodeSystemVersionReplacement(CodeSystemVersionReplacement theReplacement) {
		myReplacementsByUnknownCanonical.putIfAbsent(theReplacement.unknownCanonical(), theReplacement);
	}

	void addWarningsOf(InMemoryExpansionResult theSubExpansion) {
		theSubExpansion.myReplacementsByUnknownCanonical.forEach(myReplacementsByUnknownCanonical::putIfAbsent);
	}

	/**
	 * One warning for each CodeSystem version that was replaced, naming both versions
	 */
	List<String> getWarnings(FhirContext theFhirContext) {
		return myReplacementsByUnknownCanonical.values().stream()
				.map(t -> t.toMessage(theFhirContext))
				.toList();
	}

	/**
	 * The {@literal system|version} a code is looked up under in this expansion: a version an include named that
	 * was replaced by the current one maps to that one, anything else is returned as given.
	 */
	@Nullable
	String toUsedCodeSystemVersion(@Nullable String theCodeSystemUrlAndVersion) {
		CodeSystemVersionReplacement replacement = myReplacementsByUnknownCanonical.get(theCodeSystemUrlAndVersion);
		return replacement != null ? replacement.currentCanonical() : theCodeSystemUrlAndVersion;
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
