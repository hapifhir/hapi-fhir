package ca.uhn.fhir.model.primitive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.util.TestUtil;

public class UriDtTest {
	@Test
	public void testEquals() {
		assertEquals(new UriDt("http://foo"), new UriDt("http://foo"));
	}
	
	@Test
	public void testEqualsNormalize() {
		assertEquals(new UriDt("http://foo"), new UriDt("http://foo/"));
				
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
