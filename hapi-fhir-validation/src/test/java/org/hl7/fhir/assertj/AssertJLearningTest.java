package org.hl7.fhir.assertj;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AssertJLearningTest {
	@Test
	public void multilineNoMatch() {
		String test = """
			aaa
			bbb
			ccc
			ddd
			eee
			""";

		assertThatThrownBy(() ->
		assertThat(test).doesNotContainPattern("(?s)bbb.*ddd")
		).isInstanceOf(AssertionError.class)
			.hasMessageContaining("not to contain pattern");
	}
}
