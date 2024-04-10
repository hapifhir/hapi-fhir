package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.search.hash.ResourceIndexHasher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceIndexedSearchParamQuantityNormalizedTest {


	@Test
	public void testEquals() {
		BaseResourceIndexedSearchParamQuantity val1 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		BaseResourceIndexedSearchParamQuantity val2 = new ResourceIndexedSearchParamQuantityNormalized()
			.setValue(Double.parseDouble("123"));
		calculateHashes(val1, val2);
		assertEquals(val1, val1);
		assertEquals(val1, val2);
		assertNotEquals(val1, null);
		assertNotEquals(val1, "");
	}

	private void calculateHashes(BaseResourceIndex... theParams) {
		Arrays.stream(theParams).forEach(param -> param.calculateHashes(new ResourceIndexHasher(new PartitionSettings(), new StorageSettings())));
	}
}
