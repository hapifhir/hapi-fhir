package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.ConfigurationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ChangelogFilesTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ChangelogFilesTest.class);

	@Test
	void testDocAnchors_noDeprecatedNameAttribute() throws Exception {
		Path docsDir = Paths.get("src/main/resources/ca/uhn/hapi/fhir/docs");
		List<String> violations = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(docsDir)) {
			paths.filter(p -> p.toString().endsWith(".md")).forEach(p -> {
				try {
					List<String> lines = Files.readAllLines(p);
					for (int i = 0; i < lines.size(); i++) {
						if (lines.get(i).contains("<a name=")) {
							violations.add(p + ":" + (i + 1) + " - " + lines.get(i).trim());
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		}

		assertThat(violations)
			.as("Found deprecated <a name=\"...\"> anchors — use <a id=\"...\"></a> instead")
			.isEmpty();
	}

	@Test
	public void testChangelogFiles() {
		Collection<File> files = FileUtils.listFiles(
			new File("src/main/resources/ca/uhn/hapi/fhir/changelog"),
			new String[]{"yaml"},
			true);

		for (File next : files) {
			ourLog.info("Checking file: {}", next);

			String nextFilename = next.getName();
			if (nextFilename.equals("changes.yaml")) {
				continue;
			}
			if (nextFilename.equals("version.yaml")) {
				continue;
			}

			if (!nextFilename.matches("[a-zA-Z0-9]+-[a-zA-Z0-9_-]+\\.yaml")) {
				fail("Invalid changelog filename: " + next);
			}

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			ObjectNode tree;
			try (FileInputStream fis = new FileInputStream(next)) {
				tree = (ObjectNode) mapper.readTree(new InputStreamReader(fis, Charsets.UTF_8));
			} catch (Exception e) {
				throw new ConfigurationException("Failed to read " + next, e);
			}

			List<String> fieldNames = IteratorUtils.toList(tree.fieldNames());
			boolean title = fieldNames.remove("title");
			assertThat(title).as("No 'title' element in " + next).isTrue();

			boolean type = fieldNames.remove("type");
			assertThat(type).as("No 'type' element in " + next).isTrue();

			// this one is optional
			boolean haveIssue = fieldNames.remove("issue");

			// this one is optional
			fieldNames.remove("backport");

			// this one is optional
			fieldNames.remove("jira");

			assertThat(fieldNames).as("Invalid element in " + next + ": " + fieldNames).isEmpty();

			if (haveIssue) {
				String issue = tree.get("issue").asText();
				try {
					Integer.parseInt(issue);
				} catch (NumberFormatException e) {
					fail("Invalid issue value in " + next);
				}
			}
		}
	}
}
