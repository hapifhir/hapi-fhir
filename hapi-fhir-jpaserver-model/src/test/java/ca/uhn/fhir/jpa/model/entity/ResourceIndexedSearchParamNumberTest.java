package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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
		assertNotEquals(PARAM_VALUE_10_FIRST, PARAM_VALUE_12_FIRST);
		assertNotEquals(PARAM_VALUE_12_FIRST, PARAM_VALUE_10_FIRST);
		assertNotEquals(PARAM_VALUE_10_FIRST.hashCode(), PARAM_VALUE_12_FIRST.hashCode());
	}

	@Test
	void equalByReference() {
		assertEquals(PARAM_VALUE_10_FIRST, PARAM_VALUE_10_FIRST);
		assertEquals(PARAM_VALUE_10_FIRST.hashCode(), PARAM_VALUE_10_FIRST.hashCode());
	}

	@Test
	void equalByContract() {
		assertEquals(PARAM_VALUE_10_FIRST, PARAM_VALUE_10_SECOND);
		assertEquals(PARAM_VALUE_10_SECOND, PARAM_VALUE_10_FIRST);
		assertEquals(PARAM_VALUE_10_FIRST.hashCode(), PARAM_VALUE_10_SECOND.hashCode());
	}
}
