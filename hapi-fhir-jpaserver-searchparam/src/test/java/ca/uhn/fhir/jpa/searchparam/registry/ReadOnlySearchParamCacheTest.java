package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.searchParamMatchesAtLeastOnePattern;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReadOnlySearchParamCacheTest {

	@ParameterizedTest
	@CsvSource({
		"'*',           Patient, name",
		"'Patient:name', Patient, name",
		"'Patient:*',   Patient, name",
		"'*:name',      Patient, name",
	})
	void searchParamMatchesAtLeastOnePattern_matches(String thePattern, String theResourceType, String theParamName) {
		assertTrue(searchParamMatchesAtLeastOnePattern(newHashSet(thePattern), theResourceType, theParamName));
	}

	@ParameterizedTest
	@CsvSource({
		"'Patient:foo', Patient, name",
		"'Foo:name',    Patient, name",
	})
	void searchParamMatchesAtLeastOnePattern_noMatch(String thePattern, String theResourceType, String theParamName) {
		assertFalse(searchParamMatchesAtLeastOnePattern(newHashSet(thePattern), theResourceType, theParamName));
	}

	@ParameterizedTest
	@ValueSource(strings = {"aaa", ":name", "Patient:"})
	void searchParamMatchesAtLeastOnePattern_invalidPattern_throwsException(String thePattern) {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet(thePattern), "Patient", "name"));
	}

	// Created by Claude Opus 4.7
	@ParameterizedTest
	@CsvSource({
		// _id has base=[Resource] — applies to every resource type
		"_id,   Patient",
		"_id,   Observation",
		"_id,   Practitioner",
		// _text has base=[DomainResource] — applies to every DomainResource subtype
		"_text, Patient",
		"_text, Observation",
		"_text, Practitioner",
	})
	void fromFhirContext_builtInAbstractBaseSearchParameter_isRegisteredUnderEveryConcreteResourceType(
			String theSpCode, String theResourceType) {
		FhirContext ctx = FhirContext.forR4Cached();
		ReadOnlySearchParamCache cache = ReadOnlySearchParamCache.fromFhirContext(ctx, new SearchParameterCanonicalizer(ctx));

		assertNotNull(cache.getSearchParamMap(theResourceType).get(theSpCode),
				() -> theSpCode + " should be indexed under " + theResourceType);
	}

}
