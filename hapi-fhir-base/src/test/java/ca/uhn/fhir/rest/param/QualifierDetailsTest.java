package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QualifierDetailsTest {

	@Test
	public void testBlacklist() {

		QualifierDetails details = new QualifierDetails();
		details.setColonQualifier(":Patient");
		assertFalse(details.passes(null, Sets.newHashSet(":Patient")));
		assertTrue(details.passes(null, Sets.newHashSet(":Observation")));

	}


}
