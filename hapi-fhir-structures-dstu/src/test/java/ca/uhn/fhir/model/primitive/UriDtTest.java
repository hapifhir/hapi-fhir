package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

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


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
