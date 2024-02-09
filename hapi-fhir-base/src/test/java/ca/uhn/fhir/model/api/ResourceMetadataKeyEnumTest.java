package ca.uhn.fhir.model.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceMetadataKeyEnumTest {

	@Test
	public void testHashCode() {
		assertThat(ResourceMetadataKeyEnum.PUBLISHED.hashCode()).isEqualTo(-60968467);
	}

	@Test
	public void testEquals() {
		assertThat(ResourceMetadataKeyEnum.PROFILES).isNotNull();
		assertThat("").isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertThat(ResourceMetadataKeyEnum.PUBLISHED).isNotEqualTo(ResourceMetadataKeyEnum.PROFILES);
		assertThat(ResourceMetadataKeyEnum.PROFILES).isEqualTo(ResourceMetadataKeyEnum.PROFILES);
	}


	@Test
	public void testExtensionResourceEquals() {
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://bar")).isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo")).isNotNull();
		assertThat("").isNotEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));
		assertThat(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo")).isEqualTo(new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo"));

		ResourceMetadataKeyEnum.ExtensionResourceMetadataKey foo = new ResourceMetadataKeyEnum.ExtensionResourceMetadataKey("http://foo");
		assertThat(foo).isEqualTo(foo);
	}


}
