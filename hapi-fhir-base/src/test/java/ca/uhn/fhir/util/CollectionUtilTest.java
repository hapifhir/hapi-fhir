package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.util.CollectionUtil.nullSafeUnion;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CollectionUtilTest {

	@Test
	void testNullSafeUnionWithTwoNonEmptyInputsIsUnmodifiable() {
		var union = nullSafeUnion(List.of("A", "A"), List.of("B"));
		assertThat(union).containsExactlyInAnyOrder("A", "A", "B");
		assertThatThrownBy(() -> union.add("C")).isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(() -> union.remove("A")).isInstanceOf(UnsupportedOperationException.class);
		assertThatThrownBy(union::clear).isInstanceOf(UnsupportedOperationException.class);
	}

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
