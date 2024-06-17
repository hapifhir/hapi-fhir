package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamUriTest {

	@Test
	public void testHashFunctions() {
		ResourceIndexedSearchParamUri token = new ResourceIndexedSearchParamUri(new PartitionSettings(), "Patient", "NAME", "http://example.com");
		token.setResource(new ResourceTable().setResourceType("Patient"));
		token.calculateHashes();

		// Make sure our hashing function gives consistent results
		assertEquals(-6132951326739875838L, token.getHashUri().longValue());
	}

	@Test
	public void testEquals() {
		ResourceIndexedSearchParamUri val1 = new ResourceIndexedSearchParamUri()
			.setUri("http://foo");
		val1.setPartitionSettings(new PartitionSettings());
		val1.calculateHashes();
		ResourceIndexedSearchParamUri val2 = new ResourceIndexedSearchParamUri()
			.setUri("http://foo");
		val2.setPartitionSettings(new PartitionSettings());
		val2.calculateHashes();
		validateEquals(val1, val2);
	}

	@Test
	public void equalsIsTrueForOptimizedSearchParam() {
		ResourceIndexedSearchParamUri param = new ResourceIndexedSearchParamUri(new PartitionSettings(), "Patient", "NAME", "http://foo");
		ResourceIndexedSearchParamUri param2 = new ResourceIndexedSearchParamUri(new PartitionSettings(), "Patient", "NAME", "http://foo");

		param2.optimizeIndexStorage();

		validateEquals(param, param2);
	}

	private void validateEquals(ResourceIndexedSearchParamUri theParam1,
								ResourceIndexedSearchParamUri theParam2) {
		assertEquals(theParam2, theParam1);
		assertEquals(theParam1, theParam2);
		assertEquals(theParam1.hashCode(), theParam2.hashCode());
	}

	@ParameterizedTest
	@CsvSource({
		"Patient, param, http://test, false,   Observation, param, http://test, false, ResourceType is different",
		"Patient, param, http://test, false,   Patient,     name,  http://test, false, ParamName is different",
		"Patient, param, http://test, false,   Patient,     param, http://diff, false, Uri is different",
		"Patient, param, http://test, false,   Patient,     param, http://test, true,  Missing is different",
	})
	public void testEqualsAndHashCode_withDifferentParams_equalsIsFalseAndHashCodeIsDifferent(String theFirstResourceType,
																							  String theFirstParamName,
																							  String theFirstUri,
																							  boolean theFirstMissing,
																							  String theSecondResourceType,
																							  String theSecondParamName,
																							  String theSecondUri,
																							  boolean theSecondMissing,
																							  String theMessage) {
		ResourceIndexedSearchParamUri param = new ResourceIndexedSearchParamUri(new PartitionSettings(),
			theFirstResourceType, theFirstParamName, theFirstUri);
		param.setMissing(theFirstMissing);
		ResourceIndexedSearchParamUri param2 = new ResourceIndexedSearchParamUri(new PartitionSettings(),
			theSecondResourceType, theSecondParamName, theSecondUri);
		param2.setMissing(theSecondMissing);

		assertNotEquals(param, param2, theMessage);
		assertNotEquals(param2, param, theMessage);
		assertNotEquals(param.hashCode(), param2.hashCode(), theMessage);
	}

}
