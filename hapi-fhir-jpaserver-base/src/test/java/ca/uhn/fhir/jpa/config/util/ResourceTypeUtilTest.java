package ca.uhn.fhir.jpa.config.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceTypeUtilTest {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceTypeUtilTest.class);

	@Test
	public void testGenerateResourceTypes() {
		List<String> resourceTypes = ResourceTypeUtil.generateResourceTypes();

		// verify
		assertThat(resourceTypes).isNotNull().isNotEmpty();
		ourLog.info("Resource type size: {}", resourceTypes.size());
		resourceTypes.forEach(ourLog::info);
	}

}
