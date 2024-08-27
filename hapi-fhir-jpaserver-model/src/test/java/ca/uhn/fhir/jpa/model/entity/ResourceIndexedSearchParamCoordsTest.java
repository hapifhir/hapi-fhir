package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamCoordsTest {

	@Test
	public void testEqualsAndHashCode_withSameParams_equalsIsTrueAndHashCodeIsSame() {
		ResourceIndexedSearchParamCoords val1 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamCoords val2 = new ResourceIndexedSearchParamCoords()
			.setLatitude(100)
			.setLongitude(10);
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		validateEquals(val2, val1);
	}

	private void validateEquals(ResourceIndexedSearchParamCoords theParam1, ResourceIndexedSearchParamCoords theParam2) {
		assertEquals(theParam2, theParam1);
		assertEquals(theParam1, theParam2);
		assertEquals(theParam1.hashCode(), theParam2.hashCode());
	}

	@Test
	public void testEqualsAndHashCode_withOptimizedSearchParam_equalsIsTrueAndHashCodeIsSame() {
		ResourceIndexedSearchParamCoords param = new ResourceIndexedSearchParamCoords(
			new PartitionSettings(), "Patient", "param", 100, 10);
		ResourceIndexedSearchParamCoords param2 = new ResourceIndexedSearchParamCoords(
			new PartitionSettings(), "Patient", "param", 100, 10);

		param2.optimizeIndexStorage();

		validateEquals(param, param2);
	}

	@ParameterizedTest
	@CsvSource({
		"Patient, param, 100, 100, false,    Observation, param, 100, 100, false, ResourceType is different",
		"Patient, param, 100, 100, false,    Patient,     name,  100, 100, false, ParamName is different",
		"Patient, param, 10,  100, false,    Patient,     param, 100, 100, false, Latitude is different",
		"Patient, param, 100, 10,  false,    Patient,     param, 100, 100, false, Longitude is different",
		"Patient, param, 100, 100, true,     Patient,     param, 100, 100, false, Missing is different",
	})
	public void testEqualsAndHashCode_withDifferentParams_equalsIsFalseAndHashCodeIsDifferent(String theFirstResourceType,
																							  String theFirstParamName,
																							  double theFirstLatitude,
																							  double theFirstLongitude,
																							  boolean theFirstMissing,
																							  String theSecondResourceType,
																							  String theSecondParamName,
																							  double theSecondLatitude,
																							  double theSecondLongitude,
																							  boolean theSecondMissing,
																							  String theMessage) {
		ResourceIndexedSearchParamCoords param = new ResourceIndexedSearchParamCoords(
			new PartitionSettings(), theFirstResourceType, theFirstParamName, theFirstLatitude, theFirstLongitude);
		param.setMissing(theFirstMissing);
		ResourceIndexedSearchParamCoords param2 = new ResourceIndexedSearchParamCoords(
			new PartitionSettings(), theSecondResourceType, theSecondParamName, theSecondLatitude, theSecondLongitude);
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
	}
}
