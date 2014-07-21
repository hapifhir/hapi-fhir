package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

public class UriDtTest {
	@Test
	public void testEquals() {
		assertEquals(new UriDt("http://foo"), new UriDt("http://foo"));
	}
	
	@Test
	public void testEqualsNormalize() {
		assertEquals(new UriDt("http://foo"), new UriDt("http://foo/"));
				
	}

}
