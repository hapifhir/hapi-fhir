package ca.uhn.fhir.jpa.searchparam.registry;

import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.searchparam.registry.ReadOnlySearchParamCache.searchParamMatchesAtLeastOnePattern;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class ReadOnlySearchParamCacheTest {

	@Test
	void testSearchParamMatchesAtLeastOnePattern() {
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("*"), "Patient", "name")).isTrue();
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:name"), "Patient", "name")).isTrue();
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:*"), "Patient", "name")).isTrue();
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("*:name"), "Patient", "name")).isTrue();
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("Patient:foo"), "Patient", "name")).isFalse();
		assertThat(searchParamMatchesAtLeastOnePattern(newHashSet("Foo:name"), "Patient", "name")).isFalse();
	}


	@Test
	void testSearchParamMatchesAtLeastOnePattern_InvalidPattern() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet("aaa"), "Patient", "name"));
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet(":name"), "Patient", "name"));
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> searchParamMatchesAtLeastOnePattern(newHashSet("Patient:"), "Patient", "name"));
	}

}

