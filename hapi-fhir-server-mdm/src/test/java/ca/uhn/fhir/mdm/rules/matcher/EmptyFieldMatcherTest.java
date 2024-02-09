package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.EmptyFieldMatcher;
import ca.uhn.fhir.model.primitive.StringDt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmptyFieldMatcherTest extends BaseMatcherR4Test {

	@Test
	public void testEmptyFieldMatch() {
		StringDt left = new StringDt("other value");
		StringDt leftEmpty = new StringDt("");
		StringDt rightEmpty = new StringDt("");
		StringDt right = new StringDt("a value");

		EmptyFieldMatcher fieldMatch = new EmptyFieldMatcher();

		assertThat(fieldMatch.matches(null, null, null)).isTrue();
		assertThat(fieldMatch.matches(null, rightEmpty, null)).isTrue();
		assertThat(fieldMatch.matches(leftEmpty, null, null)).isTrue();
		assertThat(fieldMatch.matches(leftEmpty, rightEmpty, null)).isTrue();
		assertThat(fieldMatch.matches(null, right, null)).isFalse();
		assertThat(fieldMatch.matches(left, null, null)).isFalse();
		assertThat(fieldMatch.matches(left, right, null)).isFalse();
	}


}
