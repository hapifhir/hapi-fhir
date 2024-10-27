package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamNumberTest {
	private static final String GRITTSCORE = "grittscore";

	public static ResourceIndexedSearchParamNumber PARAM_VALUE_10_FIRST;
	public static ResourceIndexedSearchParamNumber PARAM_VALUE_10_SECOND;
	public static ResourceIndexedSearchParamNumber PARAM_VALUE_12_FIRST;

	@BeforeEach
	void setUp() {
		final ResourceTable resourceTable = new ResourceTable();
		resourceTable.setId(1L);
		PARAM_VALUE_10_FIRST = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(10));
		PARAM_VALUE_10_SECOND = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(10));
		PARAM_VALUE_12_FIRST = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(12));
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

	@ParameterizedTest
	@CsvSource({
		"Patient, param, 10, false,    Observation, param, 10, false, ResourceType is different",
		"Patient, param, 10, false,    Patient,     name,  10, false, ParamName is different",
		"Patient, param, 10, false,    Patient,     param, 9,  false, Value is different",
		"Patient, param, 10, false,    Patient,     param, 10, true,  Missing is different",
	})
	public void testEqualsAndHashCode_withDifferentParams_equalsIsFalseAndHashCodeIsDifferent(String theFirstResourceType,
																							  String theFirstParamName,
																							  int theFirstValue,
																							  boolean theFirstMissing,
																							  String theSecondResourceType,
																							  String theSecondParamName,
																							  int theSecondValue,
																							  boolean theSecondMissing,
																							  String theMessage) {
		ResourceIndexedSearchParamNumber param = new ResourceIndexedSearchParamNumber(
			new PartitionSettings(), theFirstResourceType, theFirstParamName, BigDecimal.valueOf(theFirstValue));
		param.setMissing(theFirstMissing);
		ResourceIndexedSearchParamNumber param2 = new ResourceIndexedSearchParamNumber(
			new PartitionSettings(), theSecondResourceType, theSecondParamName, BigDecimal.valueOf(theSecondValue));
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
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

	@Test
	void equalsIsTrueForOptimizedSearchParam() {
		PARAM_VALUE_10_SECOND.optimizeIndexStorage();

		assertEquals(PARAM_VALUE_10_FIRST, PARAM_VALUE_10_SECOND);
		assertEquals(PARAM_VALUE_10_SECOND, PARAM_VALUE_10_FIRST);
		assertEquals(PARAM_VALUE_10_FIRST.hashCode(), PARAM_VALUE_10_SECOND.hashCode());
	}
}
