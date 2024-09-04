package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.util.VersionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VersionEnumDocsSlugTest {

	@Test
	void testVersionedDocsSlugsWhileSnapshot() {
		try (MockedStatic<VersionUtil> versionUtil = Mockito.mockStatic(VersionUtil.class)) {
			versionUtil.when(VersionUtil::isSnapshot).thenReturn(true);

			String versionedDocsSlug = VersionEnum.V5_0_1.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("5.0.1");

			versionedDocsSlug = VersionEnum.V7_0_0.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("7.0.0");

			versionedDocsSlug = VersionEnum.V7_4_0.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("7.4.0");
		}
	}

	@Test
	public void testVersionedDocsNoSnapshot() {
		try (MockedStatic<VersionUtil> versionUtil = Mockito.mockStatic(VersionUtil.class)) {
			versionUtil.when(VersionUtil::isSnapshot).thenReturn(false);

			String versionedDocsSlug = VersionEnum.V5_0_1.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("5.0.1");

			versionedDocsSlug = VersionEnum.V7_0_0.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("7.0.0");

			versionedDocsSlug = VersionEnum.V7_4_0.getVersionedDocsSlug();
			assertThat(versionedDocsSlug).isEqualTo("7.4.0");
		}
	}

}
