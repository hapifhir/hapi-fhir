package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.test.utilities.RandomDataHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JobInstanceUtilTest {

	/**
	 * Fill with random data and round-trip via instance.
	 */
	@Test
	void fromEntityToInstance() {
		JobInstance instance = new JobInstance();
		RandomDataHelper.fillFieldsRandomly(instance);

		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		JobInstanceUtil.fromInstanceToEntity(instance, entity);
		JobInstance instanceCopyBack = JobInstanceUtil.fromEntityToInstance(entity);

		assertTrue(EqualsBuilder.reflectionEquals(instance, instanceCopyBack));

	}

}
