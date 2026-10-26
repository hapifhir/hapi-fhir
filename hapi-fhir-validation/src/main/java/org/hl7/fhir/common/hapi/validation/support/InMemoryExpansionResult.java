package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.context.support.MissingCodeSystemPolicyEvaluator;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The outcome of one in-memory expansion, built up while its includes and excludes are expanded: the expanded
 * ValueSet, messages explaining an expansion that came out empty, the CodeSystem versions that
 * {@link MissingCodeSystemPolicy#USE_INSTALLED_VERSION} replaced, and the CodeSystems whose listed codes
 * {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} accepted without checking them. An expansion of a ValueSet that
 * an include names gets its own instance, whose replacements and unchecked CodeSystems are merged into the including
 * one.
 */
// Created by Claude Opus 5.5
class InMemoryExpansionResult {

	private org.hl7.fhir.r5.model.ValueSet myValueSet;
	private final List<String> myMessages = new ArrayList<>();

	/**
	 * The version an include or exclude named ({@literal system|version}) mapped to the one used instead
	 * ({@literal system|version}, or {@literal system} when the installed one has no version), in the order
	 * they were replaced
	 */
	private final Map<String, String> myNamedToUsedCodeSystemVersions = new LinkedHashMap<>();

	void addCodeSystemVersionReplacement(
			String theCodeSystemUrl, String theNamedVersion, @Nullable String theUsedVersion) {
		myNamedToUsedCodeSystemVersions.putIfAbsent(
				UrlUtil.toCanonicalUrl(theCodeSystemUrl, theNamedVersion),
				UrlUtil.toCanonicalUrl(theCodeSystemUrl, theUsedVersion));
	}

	/**
	 * The CodeSystems ({@literal system|version}) whose listed codes
	 * {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} accepted without checking them, in the order accepted
	 */
	private final Set<String> myUncheckedCodeSystems = new LinkedHashSet<>();

	void addUncheckedCodeSystem(String theCodeSystemUrlAndVersion) {
		myUncheckedCodeSystems.add(theCodeSystemUrlAndVersion);
	}

	Set<String> getUncheckedCodeSystems() {
		return myUncheckedCodeSystems;
	}

	void addWarningsOf(InMemoryExpansionResult theSubExpansion) {
		theSubExpansion.myNamedToUsedCodeSystemVersions.forEach(myNamedToUsedCodeSystemVersions::putIfAbsent);
		myUncheckedCodeSystems.addAll(theSubExpansion.myUncheckedCodeSystems);
	}

	/**
	 * One warning for each CodeSystem version that was replaced, naming both versions, then one for each
	 * CodeSystem whose listed codes were accepted without being checked
	 */
	List<String> getWarnings(MissingCodeSystemPolicyEvaluator thePolicyEvaluator) {
		Stream<String> versionWarnings = myNamedToUsedCodeSystemVersions.entrySet().stream()
				.map(t -> {
					UrlUtil.CanonicalUrlParts named = UrlUtil.parseCanonicalUrl(t.getKey());
					String usedVersion =
							UrlUtil.parseCanonicalUrl(t.getValue()).versionId().orElse(null);
					return thePolicyEvaluator.createVersionReplacedMessage(
							named.url(), named.versionId().orElseThrow(), usedVersion);
				});
		Stream<String> uncheckedWarnings =
				myUncheckedCodeSystems.stream().map(thePolicyEvaluator::createListedCodesUncheckedMessage);
		return Stream.concat(versionWarnings, uncheckedWarnings).toList();
	}

	/**
	 * The {@literal system|version} a code is looked up under in this expansion: a version an include named that
	 * was replaced by the installed one maps to that one, anything else is returned as given.
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
