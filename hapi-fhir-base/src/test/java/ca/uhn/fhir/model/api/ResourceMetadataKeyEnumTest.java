package ca.uhn.fhir.model.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceMetadataKeyEnumTest {

	@Test
	public void testHashCode() {
		assertEquals(-60968467, ResourceMetadataKeyEnum.PUBLISHED.hashCode());
	}

	@Test
	public void testEquals() {
		assertNotNull(ResourceMetadataKeyEnum.PROFILES);
		assertThat("").isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertThat(ResourceMetadataKeyEnum.PUBLISHED).isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertEquals(ResourceMetadataKeyEnum.PROFILES, ResourceMetadataKeyEnum.PROFILES);
	}


	@Test
	public void testExtensionResourceEquals() {
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://bar")).isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertNotNull(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertThat("").isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertEquals(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"), new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));

		ResourceMetadataKeyEnum.ExtensionResourceMetadataKey foo = new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo");
		assertEquals(foo, foo);
	}


}
