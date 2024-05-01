package ca.uhn.fhir.model.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceMetadataKeyEnumTest {

	@Test
	public void testHashCode() {
		assertThat(ResourceMetadataKeyEnum.PUBLISHED.hashCode()).isEqualTo(-60968467);
	}

	@Test
	public void testEquals() {
		assertNotNull(ResourceMetadataKeyEnum.PROFILES);
		assertThat("").isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertThat(ResourceMetadataKeyEnum.PUBLISHED).isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertThat(ResourceMetadataKeyEnum.PROFILES).isEqualTo(ResourceMetadataKeyEnum.PROFILES);
	}


	@Test
	public void testExtensionResourceEquals() {
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://bar")).isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertNotNull(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertThat("").isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo")).isEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));

		ResourceMetadataKeyEnum.ExtensionResourceMetadataKey foo = new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo");
		assertThat(foo).isEqualTo(foo);
	}


}
