package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.util.CollectionUtil.nullSafeUnion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

class CollectionUtilTest {

	@Test
	void testNullSafeUnion() {
		assertThat(nullSafeUnion(null, null), empty());
		assertThat(nullSafeUnion(Set.of(), Set.of()), empty());
		assertThat(nullSafeUnion(Set.of("A"), null), containsInAnyOrder("A"));
		assertThat(nullSafeUnion(Set.of("A"), Set.of()), containsInAnyOrder("A"));
		assertThat(nullSafeUnion(null, Set.of("B")), containsInAnyOrder("B"));
		assertThat(nullSafeUnion(Set.of(), Set.of("B")), containsInAnyOrder("B"));
		assertThat(nullSafeUnion(Set.of("A"), Set.of("B")), containsInAnyOrder("A", "B"));
		assertThat(nullSafeUnion(List.of("A"), Set.of("B")), containsInAnyOrder("A", "B"));
	}

}
