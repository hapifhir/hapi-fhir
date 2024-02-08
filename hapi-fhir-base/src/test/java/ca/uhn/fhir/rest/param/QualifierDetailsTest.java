package ca.uhn.fhir.rest.param;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QualifierDetailsTest {

	@Test
	public void testBlacklist() {

		QualifierDetails details = new QualifierDetails();
		details.setColonQualifier(":Patient");
		assertThat(details.passes(null, Sets.newHashSet(":Patient"))).isFalse();
		assertThat(details.passes(null, Sets.newHashSet(":Observation"))).isTrue();

	}


}
