package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.searchParamMatchesAtLeastOnePattern;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;

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
		assertThrows(IllegalArgumentException.class, () -> searchParamMatchesAtLeastOnePattern(newHashSet("aaa"), "Patient", "name"));
		assertThrows(IllegalArgumentException.class, () -> searchParamMatchesAtLeastOnePattern(newHashSet(":name"), "Patient", "name"));
		assertThrows(IllegalArgumentException.class, () -> searchParamMatchesAtLeastOnePattern(newHashSet("Patient:"), "Patient", "name"));
	}

	@Test
	public void testMultiResourcePatternsApplied() {
		FhirContext ctx = FhirContext.forR5Cached();
		SearchParameterCanonicalizer canonicalizer = new SearchParameterCanonicalizer(ctx);
		ReadOnlySearchParamCache cache = ReadOnlySearchParamCache.fromFhirContext(ctx, canonicalizer);

		Map<String, RuntimeSearchParam> observationParams = cache.getSearchParamMap("Observation");
		assertThat(observationParams.keySet().toString(), observationParams.keySet(), hasItem("code"));
	}

	@Test
	public void testCompartmentDefinitionsApplied() {
		FhirContext ctx = FhirContext.forR5Cached();
		SearchParameterCanonicalizer canonicalizer = new SearchParameterCanonicalizer(ctx);
		ReadOnlySearchParamCache cache = ReadOnlySearchParamCache.fromFhirContext(ctx, canonicalizer);

		Map<String, RuntimeSearchParam> observationParams = cache.getSearchParamMap("Observation");
		RuntimeSearchParam patientSp = observationParams.get("patient");
		assertThat(patientSp.getProvidesMembershipInCompartments(), Matchers.contains("Patient"));

		RuntimeSearchParam subjectSp = observationParams.get("subject");
		assertThat(subjectSp.getProvidesMembershipInCompartments(), Matchers.containsInAnyOrder("Device", "Patient"));

		RuntimeSearchParam performerSp = observationParams.get("performer");
		assertThat(performerSp.getProvidesMembershipInCompartments(), Matchers.containsInAnyOrder("Practitioner", "Patient", "RelatedPerson"));

		RuntimeSearchParam codeSp = observationParams.get("code");
		assertNull(codeSp.getProvidesMembershipInCompartments());
	}

}

