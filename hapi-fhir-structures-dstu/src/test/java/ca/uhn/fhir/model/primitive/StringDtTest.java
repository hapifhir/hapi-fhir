package ca.uhn.fhir.model.primitive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.util.TestUtil;

public class StringDtTest {

	@Test
	public void testBlank() {
		
		assertTrue(new StringDt("").isEmpty());
		
	}
	

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
