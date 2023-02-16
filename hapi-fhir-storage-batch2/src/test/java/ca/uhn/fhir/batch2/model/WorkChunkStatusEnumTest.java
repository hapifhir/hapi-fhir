package ca.uhn.fhir.batch2.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class WorkChunkStatusEnumTest {
	@ParameterizedTest
	@EnumSource(WorkChunkStatusEnum.class)
	void incomplete_true_onlyWhenComplete(WorkChunkStatusEnum theEnum) {
	    assertEquals(theEnum!= WorkChunkStatusEnum.COMPLETED, theEnum.isIncomplete());
	}
	
	
}
