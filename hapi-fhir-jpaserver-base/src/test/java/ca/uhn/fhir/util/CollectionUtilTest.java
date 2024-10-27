package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.util.CollectionUtil.nullSafeUnion;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CollectionUtilTest {

	@Test
	void testNullSafeUnion() {
		assertThat(nullSafeUnion(null, null)).isEmpty();
		assertThat(nullSafeUnion(Set.of(), Set.of())).isEmpty();
		assertThat(nullSafeUnion(Set.of("A"), null)).containsExactly("A");
		assertThat(nullSafeUnion(Set.of("A"), Set.of())).containsExactly("A");
		assertThat(nullSafeUnion(null, Set.of("B"))).containsExactly("B");
		assertThat(nullSafeUnion(Set.of(), Set.of("B"))).containsExactly("B");
		assertThat(nullSafeUnion(Set.of("A"), Set.of("B"))).containsExactlyInAnyOrder("A", "B");
		assertThat(nullSafeUnion(List.of("A"), Set.of("B"))).containsExactlyInAnyOrder("A", "B");
	}

}
