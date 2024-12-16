package ca.uhn.fhir.batch2.jobs.models;

import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypedPidJsonTest {

	@Test
	public void testEstimateSize() {
		TypedPidJson id = new TypedPidJson();
		id.setPid("12345");
		id.setResourceType("Patient");
		String serialized = JsonUtil.serialize(id, false);
		assertEquals(serialized.length(), id.estimateSerializedSize(), serialized);
	}


}
