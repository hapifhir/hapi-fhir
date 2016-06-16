package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

public class UriDtTest {

	@Test
	public void testFromOid() {
		UriDt uri = UriDt.fromOid("0.1.2.3.4");
		assertEquals("urn:oid:0.1.2.3.4", uri.getValue());
	}
	
	@Test
	public void testFromOidNull() {
		UriDt uri = UriDt.fromOid(null);
		assertEquals(null, uri.getValue());
	}
	
	@Test
	public void testEqualsObject() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertTrue(dt.equals(dt));
		assertFalse(dt.equals(null));
		assertFalse(dt.equals(new UriDt()));
		assertTrue(dt.equals(new UriDt("http://example.com/foo")));
		assertTrue(dt.equals(new UriDt("http://example.com/foo/")));
		assertFalse(dt.equals(new UriDt("http://blah.com/foo/")));
		assertFalse(dt.equals(new StringDt("http://example.com/foo")));
	}

	@Test
	public void testEqualsString() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertTrue(dt.equals("http://example.com/foo"));
	}

	@Test
	public void testHashCode() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertEquals(-1671329151, dt.hashCode());
		
		dt = new UriDt();
		assertEquals(31, dt.hashCode());

	}

	@Test
	public void testSetInvalid() {
		UriDt dt = new UriDt();
		dt.setValue("blah : // AA");
		dt.hashCode();
	}

}
