package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceIndexedSearchParamNumberTest {
	private static final String GRITTSCORE = "grittscore";

	public static final ResourceIndexedSearchParamNumber PARAM_VALUE_10_FIRST = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(10));
	public static final ResourceIndexedSearchParamNumber PARAM_VALUE_10_SECOND = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(10));
	public static final ResourceIndexedSearchParamNumber PARAM_VALUE_12_FIRST = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(12));

	@BeforeEach
	void setUp() {
		final ResourceTable resourceTable = new ResourceTable();
		resourceTable.setId(1L);
		PARAM_VALUE_10_FIRST.setResource(resourceTable);
		PARAM_VALUE_10_SECOND.setResource(resourceTable);
		PARAM_VALUE_12_FIRST.setResource(resourceTable);
	}

	@Test
	void notEqual() {
		assertThat(PARAM_VALUE_12_FIRST).isNotEqualTo(PARAM_VALUE_10_FIRST);
		assertThat(PARAM_VALUE_10_FIRST).isNotEqualTo(PARAM_VALUE_12_FIRST);
		assertThat(PARAM_VALUE_12_FIRST.hashCode()).isNotEqualTo(PARAM_VALUE_10_FIRST.hashCode());
	}

	@Test
	void equalByReference() {
		assertThat(PARAM_VALUE_10_FIRST).isEqualTo(PARAM_VALUE_10_FIRST);
		assertThat(PARAM_VALUE_10_FIRST.hashCode()).isEqualTo(PARAM_VALUE_10_FIRST.hashCode());
	}

	@Test
	void equalByContract() {
		assertThat(PARAM_VALUE_10_SECOND).isEqualTo(PARAM_VALUE_10_FIRST);
		assertThat(PARAM_VALUE_10_FIRST).isEqualTo(PARAM_VALUE_10_SECOND);
		assertThat(PARAM_VALUE_10_SECOND.hashCode()).isEqualTo(PARAM_VALUE_10_FIRST.hashCode());
	}
}
