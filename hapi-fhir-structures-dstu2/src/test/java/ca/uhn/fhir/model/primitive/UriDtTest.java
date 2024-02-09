package ca.uhn.fhir.model.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UriDtTest {

	@Test
	public void testFromOid() {
		UriDt uri = UriDt.fromOid("0.1.2.3.4");
		assertThat(uri.getValue()).isEqualTo("urn:oid:0.1.2.3.4");
	}
	
	@Test
	public void testFromOidNull() {
		UriDt uri = UriDt.fromOid(null);
		assertThat(uri.getValue()).isNull();
	}
	
	@Test
	public void testEqualsObject() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertThat(dt).isEqualTo(dt);
		assertThat(dt.equals(null)).isFalse();
		assertThat(new UriDt()).isNotEqualTo(dt);
		assertThat(new UriDt("http://example.com/foo")).isEqualTo(dt);
		assertThat(new UriDt("http://example.com/foo/")).isEqualTo(dt);
		assertThat(new UriDt("http://blah.com/foo/")).isNotEqualTo(dt);
		assertThat(new StringDt("http://example.com/foo")).isNotEqualTo(dt);
	}

	@Test
	public void testEqualsString() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertThat(dt.equals("http://example.com/foo")).isTrue();
	}

	@Test
	public void testHashCode() {
		UriDt dt = new UriDt("http://example.com/foo");
		assertThat(dt.hashCode()).isEqualTo(-1671329151);
		
		dt = new UriDt();
		assertThat(dt.hashCode()).isEqualTo(31);

	}

	@Test
	public void testSetInvalid() {
		UriDt dt = new UriDt();
		dt.setValue("blah : // AA");
		assertThat(dt.hashCode()).isEqualTo(-1078724630);
	}

}
