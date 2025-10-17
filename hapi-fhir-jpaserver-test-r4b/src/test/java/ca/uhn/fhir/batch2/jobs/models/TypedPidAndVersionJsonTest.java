package ca.uhn.fhir.batch2.jobs.models;

import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypedPidAndVersionJsonTest {

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testEstimateSize(boolean theAddVersion) {
		TypedPidAndVersionJson id = new TypedPidAndVersionJson();
		id.setPid("12345");
		id.setResourceType("Patient");
		if (theAddVersion) {
			id.setVersionId(123L);
		}
		String serialized = JsonUtil.serialize(id, false);
		assertEquals(serialized.length(), id.estimateSerializedSize(), serialized);
	}

}
