package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class VersionEnumTest {

	@Test
	public void testCurrentVersionExists() {
		List<String> versions = Arrays.stream(VersionEnum.values())
			.map(Enum::name)
			.collect(Collectors.toList());

		String version = VersionUtil.getVersion();
		version = "V" + version.replace(".", "_");
		version = version.replaceAll("-PRE[0-9]+", "");
		version = version.replace("-SNAPSHOT", "");

		assertThat(versions, hasItem(version));
	}


}
