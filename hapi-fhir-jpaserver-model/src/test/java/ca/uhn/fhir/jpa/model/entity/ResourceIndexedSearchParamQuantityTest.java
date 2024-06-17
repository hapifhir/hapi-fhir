package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityTest {

	private ResourceIndexedSearchParamQuantity createParam(String theParamName, String theValue, String theSystem, String theUnits) {
		ResourceIndexedSearchParamQuantity token = new ResourceIndexedSearchParamQuantity(new PartitionSettings(), "Patient", theParamName, new BigDecimal(theValue), theSystem, theUnits);
		token.setResource(new ResourceTable().setResourceType("Patient"));
		return token;
	}

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamQuantity token = createParam("NAME", "123.001", "value", "VALUE");
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(834432764963581074L, token.getHashIdentity().longValue());
		assertEquals(-1970227166134682431L, token.getHashIdentityAndUnits().longValue());
	}


	@Test
	public void testEquals() {
		ResourceIndexedSearchParamQuantity val1 = new ResourceIndexedSearchParamQuantity()
			.setValue(new BigDecimal(123));
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamQuantity val2 = new ResourceIndexedSearchParamQuantity()
			.setValue(new BigDecimal(123));
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		validateEquals(val1, val2);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		BaseResourceIndexedSearchParamQuantity param = createParam("NAME", "123.001", "value", "VALUE");
		BaseResourceIndexedSearchParamQuantity param2 = createParam("NAME", "123.001", "value", "VALUE");

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
		BaseResourceIndexedSearchParamQuantity param = new ResourceIndexedSearchParamQuantity(
			new PartitionSettings(), theFirstResourceType, theFirstParamName, new BigDecimal(theFirstValue), theFirstSystem, theFirstUnits);
		param.setMissing(theFirstMissing);
		BaseResourceIndexedSearchParamQuantity param2 = new ResourceIndexedSearchParamQuantity(
			new PartitionSettings(), theSecondResourceType, theSecondParamName, new BigDecimal(theSecondValue), theSecondSystem, theSecondUnits);
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
	}

}
