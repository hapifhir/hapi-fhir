package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceVersionMapTest {

	@Test
	void testCreate_fromIds() {
	    // given
		List<IIdType> ids = List.of(
			new IdDt("Patient", "p1", "2"),
			new IdDt("Patient", "p2", "1"),
			new IdDt("Observation", "o1", "1")
		);

	    // when
		ResourceVersionMap resourceVersionMap = ResourceVersionMap.fromIdsWithVersions(ids);

		// then
		assertThat(resourceVersionMap.getSourceIds()).isEqualTo(Set.copyOf(ids));
		assertThat(resourceVersionMap.get(new IdDt("Patient", "p1"))).isEqualTo(2);
	}

}
