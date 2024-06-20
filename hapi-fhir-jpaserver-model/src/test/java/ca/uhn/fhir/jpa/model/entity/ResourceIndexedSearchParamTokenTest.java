package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamTokenTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testHashFunctionsWithOverlapNames() {
		ResourceIndexedSearchParamToken token = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(-8558989679010582575L, token.getHashSystem().longValue());
		assertEquals(-8644532105141886455L, token.getHashSystemAndValue().longValue());
		assertEquals(-1970227166134682431L, token.getHashValue().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamToken val1 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamToken val2 = new ResourceIndexedSearchParamToken()
			.setValue("AAA");
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		validateEquals(val1, val2);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		ResourceIndexedSearchParamToken param = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");
		ResourceIndexedSearchParamToken param2 = new ResourceIndexedSearchParamToken(new PartitionSettings(), "Patient", "NAME", "SYSTEM", "VALUE");

		param2.optimizeIndexStorage();

		validateEquals(param, param2);
	}

	private void validateEquals(ResourceIndexedSearchParamToken theParam1,
								ResourceIndexedSearchParamToken theParam2) {
		assertEquals(theParam2, theParam1);
		assertEquals(theParam1, theParam2);
		assertEquals(theParam1.hashCode(), theParam2.hashCode());
	}

	@ParameterizedTest
	@CsvSource({
		"Patient, param, system, value, false,   Observation, param, system, value, false, ResourceType is different",
		"Patient, param, system, value, false,   Patient,     name,  system, value, false, ParamName is different",
		"Patient, param, system, value, false,   Patient,     param, sys,    value, false, System is different",
		"Patient, param, system, value, false,   Patient,     param, system, val,   false, Value is different",
		"Patient, param, system, value, false,   Patient,     param, system, value, true,  Missing is different",
	})
	public void testEqualsAndHashCode_withDifferentParams_equalsIsFalseAndHashCodeIsDifferent(String theFirstResourceType,
																							  String theFirstParamName,
																							  String theFirstSystem,
																							  String theFirstValue,
																							  boolean theFirstMissing,
																							  String theSecondResourceType,
																							  String theSecondParamName,
																							  String theSecondSystem,
																							  String theSecondValue,
																							  boolean theSecondMissing,
																							  String theMessage) {
		ResourceIndexedSearchParamToken param = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), theFirstResourceType, theFirstParamName, theFirstSystem, theFirstValue);
		param.setMissing(theFirstMissing);
		ResourceIndexedSearchParamToken param2 = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), theSecondResourceType, theSecondParamName, theSecondSystem, theSecondValue);
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
	}
}
