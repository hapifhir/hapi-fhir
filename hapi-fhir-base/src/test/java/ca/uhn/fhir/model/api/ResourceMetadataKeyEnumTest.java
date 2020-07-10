package ca.uhn.fhir.model.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceMetadataKeyEnumTest {

	@Test
	public void testHashCode() {
		assertEquals(-60968467, ResourceMetadataKeyEnum.PUBLISHED.hashCode());
	}

	@Test
	public void testEquals() {
		assertNotEquals(ResourceMetadataKeyEnum.PROFILES, null);
		assertNotEquals(ResourceMetadataKeyEnum.PROFILES, "");
		assertNotEquals(ResourceMetadataKeyEnum.PROFILES, ResourceMetadataKeyEnum.PUBLISHED);
		assertEquals(ResourceMetadataKeyEnum.PROFILES, ResourceMetadataKeyEnum.PROFILES);
	}


	@Test
	public void testExtensionResourceEquals() {
		assertNotEquals(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"), new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://bar"));
		assertNotEquals(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"), null);
		assertNotEquals(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"), "");
		assertEquals(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"), new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));

		ResourceMetadataKeyEnum.ExtensionResourceMetadataKey foo = new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo");
		assertEquals(foo, foo);
	}


}
