package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.searchParamMatchesAtLeastOnePattern;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

	// Created by Claude Opus 4.7
	@Test
	void fromFhirContext_builtInAbstractBaseSearchParameter_isRegisteredUnderEveryConcreteResourceType() {
		// The FHIR R4 core spec ships SearchParameters such as "_id" with base=[Resource].
		// fromFhirContext must delegate to SearchParameterUtil.expandBaseAsStrings so the SP
		// lands under every concrete resource type rather than under the literal "Resource" key.
		FhirContext ctx = FhirContext.forR4Cached();
		SearchParameterCanonicalizer canonicalizer = new SearchParameterCanonicalizer(ctx);

		ReadOnlySearchParamCache cache = ReadOnlySearchParamCache.fromFhirContext(ctx, canonicalizer);

		ResourceSearchParams patientParams = cache.getSearchParamMap("Patient");
		ResourceSearchParams observationParams = cache.getSearchParamMap("Observation");
		ResourceSearchParams practitionerParams = cache.getSearchParamMap("Practitioner");
		assertNotNull(patientParams.get("_id"));
		assertNotNull(observationParams.get("_id"));
		assertNotNull(practitionerParams.get("_id"));
	}

}

