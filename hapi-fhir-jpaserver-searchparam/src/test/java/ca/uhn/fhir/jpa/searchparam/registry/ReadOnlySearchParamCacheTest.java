package ca.uhn.fhir.jpa.searchparam.registry;

import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.isNonDisableableBuiltInSearchParam;
import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.searchParamMatchesAtLeastOnePattern;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReadOnlySearchParamCacheTest {

	@Test
	void testSearchParamMatchesAtLeastOnePattern() {
		assertTrue(searchParamMatchesAtLeastOnePattern(newHashSet("*"), "Patient", "name"));
		assertTrue(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:name"), "Patient", "name"));
		assertTrue(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:*"), "Patient", "name"));
		assertTrue(searchParamMatchesAtLeastOnePattern(newHashSet("*:name"), "Patient", "name"));
		assertFalse(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:foo"), "Patient", "name"));
		assertFalse(searchParamMatchesAtLeastOnePattern(newHashSet("Foo:name"), "Patient", "name"));
	}


	@Test
	void testSearchParamMatchesAtLeastOnePattern_InvalidPattern() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet("aaa"), "Patient", "name"));
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet(":name"), "Patient", "name"));
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet("Patient:"), "Patient", "name"));
	}

	@Test
	void testIsNonDisableableBuiltInSearchParam_builtInNonDisableable() {
		// Created by Claude Sonnet 4.6
		// Basic:* pattern
		assertTrue(isNonDisableableBuiltInSearchParam("http://hl7.org/fhir/SearchParameter/Basic-code", "Basic", "code"));
		// *:url pattern
		assertTrue(isNonDisableableBuiltInSearchParam("http://hl7.org/fhir/SearchParameter/conformance-url", "ValueSet", "url"));
		// Subscription:* pattern
		assertTrue(isNonDisableableBuiltInSearchParam("http://hl7.org/fhir/SearchParameter/Subscription-status", "Subscription", "status"));
		// SearchParameter:* pattern
		assertTrue(isNonDisableableBuiltInSearchParam("http://hl7.org/fhir/SearchParameter/SearchParameter-url", "SearchParameter", "url"));
	}

	@Test
	void testIsNonDisableableBuiltInSearchParam_customUrlReturnsFalse() {
		// Created by Claude Sonnet 4.6
		// Custom URL on a non-disableable resource type must NOT be protected
		assertFalse(isNonDisableableBuiltInSearchParam("http://example.com/fhir/SearchParameter/Basic-custom", "Basic", "custom"));
		assertFalse(isNonDisableableBuiltInSearchParam("http://example.com/fhir/SearchParameter/Subscription-foo", "Subscription", "foo"));
		assertFalse(isNonDisableableBuiltInSearchParam("http://example.com/fhir/SearchParameter/CustomResource-url", "CustomResource", "url"));
		// Null URI
		assertFalse(isNonDisableableBuiltInSearchParam(null, "Basic", "code"));
	}

	@Test
	void testIsNonDisableableBuiltInSearchParam_builtInButDisableable() {
		// Created by Claude Sonnet 4.6
		// Built-in URL but resource type not in NON_DISABLEABLE_SEARCH_PARAMS
		assertFalse(isNonDisableableBuiltInSearchParam("http://hl7.org/fhir/SearchParameter/Patient-name", "Patient", "name"));
	}

}

