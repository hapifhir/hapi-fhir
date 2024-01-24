package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
	    assertThat(requestPartitionId, notNullValue());
	    assertThat(requestPartitionId.getPartitionIds(), equalTo(List.of(1,2)));
	    assertThat(requestPartitionId.getPartitionNames(), equalTo(List.of("p1","p2")));
	}

}
