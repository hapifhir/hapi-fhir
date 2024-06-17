package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityNormalizedTest {


	@Test
	public void testEquals() {
		BaseResourceIndexedSearchParamQuantity val1 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		BaseResourceIndexedSearchParamQuantity val2 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		validateEquals(val1, val2);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		BaseResourceIndexedSearchParamQuantity param = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), "Patient", "param", 123.0, "http://unitsofmeasure.org", "kg");
		BaseResourceIndexedSearchParamQuantity param2 = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), "Patient", "param", 123.0, "http://unitsofmeasure.org", "kg");

		param2.optimizeIndexStorage();

		validateEquals(param, param2);
	}

	private void validateEquals(BaseResourceIndexedSearchParamQuantity theParam1,
								BaseResourceIndexedSearchParamQuantity theParam2) {
		assertEquals(theParam2, theParam1);
		assertEquals(theParam1, theParam2);
		assertEquals(theParam1.hashCode(), theParam2.hashCode());
	}

	@ParameterizedTest
	@CsvSource({
		"Patient, param, 123.0, units, kg, false,   Observation, param, 123.0, units,     kg, false, ResourceType is different",
		"Patient, param, 123.0, units, kg, false,   Patient,     name,  123.0, units,     kg, false, ParamName is different",
		"Patient, param, 123.0, units, kg, false,   Patient,     param, 321.0, units,     kg, false, Value is different",
		"Patient, param, 123.0, units, kg, false,   Patient,     param, 123.0, unitsDiff, kg, false, System is different",
		"Patient, param, 123.0, units, kg, false,   Patient,     param, 123.0, units,     lb, false, Units is different",
		"Patient, param, 123.0, units, kg, false,   Patient,     param, 123.0, units,     kg, true,  Missing is different",
	})
	public void testEqualsAndHashCode_withDifferentParams_equalsIsFalseAndHashCodeIsDifferent(String theFirstResourceType,
																							  String theFirstParamName,
																							  double theFirstValue,
																							  String theFirstSystem,
																							  String theFirstUnits,
																							  boolean theFirstMissing,
																							  String theSecondResourceType,
																							  String theSecondParamName,
																							  double theSecondValue,
																							  String theSecondSystem,
																							  String theSecondUnits,
																							  boolean theSecondMissing,
																							  String theMessage) {
		BaseResourceIndexedSearchParamQuantity param = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), theFirstResourceType, theFirstParamName, theFirstValue, theFirstSystem, theFirstUnits);
		param.setMissing(theFirstMissing);
		BaseResourceIndexedSearchParamQuantity param2 = new ResourceIndexedSearchParamQuantityNormalized(
			new PartitionSettings(), theSecondResourceType, theSecondParamName, theSecondValue, theSecondSystem, theSecondUnits);
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
	}
}
