package ca.uhn.fhir.model.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UriDtTest {

	@Test
	public void testFromOid() {
		UriDt uri = UriDt.fromOid("0.1.2.3.4");
		assertEquals("urn:oid:0.1.2.3.4", uri.getValue());
	}
	
	@Test
	public void testFromOidNull() {
		UriDt uri = UriDt.fromOid(null);
		assertNull(uri.getValue());
	}
	
	@Test
	public void testEqualsObject() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertEquals(dt, dt);
		assertFalse(dt.equals(null));
		assertThat(new UriDt()).isNotEqualTo(dt);
		assertEquals(dt, new UriDt("http://example.com/foo"));
		assertEquals(dt, new UriDt("http://example.com/foo/"));
		assertThat(new UriDt("http://blah.com/foo/")).isNotEqualTo(dt);
		assertThat(new StringDt("http://example.com/foo")).isNotEqualTo(dt);
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
		assertEquals(-1078724630, dt.hashCode());
	}

}
