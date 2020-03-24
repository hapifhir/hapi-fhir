package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.rules.VectorWeightMap;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class VectorWeightMapTest {
	@Test
	public void splitFieldMatchNames() {
		{
			String[] result = VectorWeightMap.splitFieldMatchNames("a,b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}

		{
			String[] result = VectorWeightMap.splitFieldMatchNames("a,  b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}
	}
}
