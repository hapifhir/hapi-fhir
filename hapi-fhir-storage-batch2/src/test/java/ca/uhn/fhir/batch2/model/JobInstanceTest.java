package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.test.utilities.RandomDataHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JobInstanceTest {

	@Test
	void testCopyConstructor_randomFieldsCopied_areEqual() {
	    // given
		JobInstance instance = new JobInstance();
		RandomDataHelper.fillFieldsRandomly(instance);

		// when
		JobInstance copy = new JobInstance(instance);

		// then
		assertTrue(EqualsBuilder.reflectionEquals(instance, copy));
	}
	
}
