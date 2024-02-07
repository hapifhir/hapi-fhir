package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
			assertThat(requestPartitionId).isNotNull();
		assertThat(requestPartitionId.getPartitionIds()).isEqualTo(List.of(1, 2));
		assertThat(requestPartitionId.getPartitionNames()).isEqualTo(List.of("p1", "p2"));
	}

}
