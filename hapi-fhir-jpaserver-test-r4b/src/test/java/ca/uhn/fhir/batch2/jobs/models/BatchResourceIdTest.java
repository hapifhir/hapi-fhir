package ca.uhn.fhir.batch2.jobs.models;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatchResourceIdTest {

	@Test
	public void testEstimateSize() {
		BatchResourceId id = new BatchResourceId();
		id.setId("12345");
		id.setResourceType("Patient");
		String serialized = JsonUtil.serialize(id, false);
		assertEquals(serialized.length(), id.estimateSerializedSize(), serialized);
	}


}
