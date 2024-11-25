package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UuidUtilsTest {

	@Test
	void testFindsUuid() {
		String xml = "<id value=#cdb6dfa1-74b7-4ea9-88e0-d3afaef8c016/>";
		String uuid = UuidUtils.findFirstUUID(xml);
		assertEquals("cdb6dfa1-74b7-4ea9-88e0-d3afaef8c016", uuid);
	}

	@Test
	void testFindsFirstUuid() {
		String xml = "<id value=#cdb6dfa1-74b7-4ea9-88e0-d3afaef8c016/><id value=#da8a08e3-ddf5-4a62-baae-3e8c3ea04687/>";
		String uuid = UuidUtils.findFirstUUID(xml);
		assertEquals("cdb6dfa1-74b7-4ea9-88e0-d3afaef8c016", uuid);
	}

	@Test
	void testNoUuidReturnsNull() {
		String xml = "<id value=x />";
		assertNull(UuidUtils.findFirstUUID(xml));
	}
}
