package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PartitionEntityTest {

	@Test
	void buildRequestPartitionFromList() {
	    // given
		List<PartitionEntity> entities = List.of(
			new PartitionEntity().setId(1).setName("p1"),
			new PartitionEntity().setId(2).setName("p2")
		);

	    // when
		RequestPartitionId requestPartitionId = PartitionEntity.buildRequestPartitionId(entities);

		// then
			assertNotNull(requestPartitionId);
		assertEquals(List.of(1, 2), requestPartitionId.getPartitionIds());
		assertEquals(List.of("p1", "p2"), requestPartitionId.getPartitionNames());
	}

}
