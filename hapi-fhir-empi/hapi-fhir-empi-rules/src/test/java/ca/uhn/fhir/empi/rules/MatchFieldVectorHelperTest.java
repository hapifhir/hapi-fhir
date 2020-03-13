package ca.uhn.fhir.empi.rules;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MatchFieldVectorHelperTest {
	@Test
	public void splitFieldMatchNames() {
		{
			String[] result = MatchFieldVectorHelper.splitFieldMatchNames("a,b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}

		{
			String[] result = MatchFieldVectorHelper.splitFieldMatchNames("a,  b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}
	}
}
