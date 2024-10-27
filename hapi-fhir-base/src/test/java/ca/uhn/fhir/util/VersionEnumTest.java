package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionEnumTest {

	@Test
	public void testCurrentVersionExists() {
		List<String> versions = Arrays.stream(VersionEnum.values())
			.map(Enum::name)
			.collect(Collectors.toList());

		String version = VersionUtil.getVersion();

		version = version.replaceAll("-PRE[0-9]+", "");
		version = version.replace("-SNAPSHOT", "");

		String[] parts = version.split("\\.");
		assertEquals(3, parts.length);
		int major = Integer.parseInt(parts[0]);
		int minor = Integer.parseInt(parts[1]);
		int patch = Integer.parseInt(parts[2]);

		if ((major == 6 && minor >= 3) || (major >= 7)) {
			if (minor % 2 == 1) {
				patch = 0;
			}
		}
		version = major + "." + minor + "." + patch;

		version = "V" + version.replace(".", "_");

		assertThat(versions).contains(version);
	}

}
