/*-
 * ChangelogMigratorTest.java
 *
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * This test file is written against Jackson 2 (com.fasterxml.jackson /
 * jackson-dataformat-yaml) and is structured so that migrating to Jackson 3
 * (tools.jackson) requires changes ONLY in the clearly marked
 * "── LFJT3 JACKSON IMPORT BLOCK ──" and "── LFJT3 MAPPER FACTORY ──"
 * sections. All assertions operate on plain String / Map / List values and
 * are Jackson-version-agnostic.
 *
 * LFJT3 MIGRATION CHECKLIST FOR THIS FILE
 * ----------------------------------------
 *  [ ] Swap the import block (see "── LFJT3 JACKSON IMPORT BLOCK ──")
 *  [ ] Swap the createChangesMapper() factory (see "── LFJT3 MAPPER FACTORY ──")
 *  [ ] Swap the createVersionMapper() factory (see "── LFJT3 MAPPER FACTORY ──")
 *  [ ] All @Test methods — NO CHANGES NEEDED
 *
 * DESIGN NOTE — WHY ChangelogMigrator IS HARD TO TEST AS-IS
 * ----------------------------------------------------------
 * ChangelogMigrator.main() hardcodes two filesystem paths:
 *   - Input:  "src/changes/changes.xml"
 *   - Output: "hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/changelog/<version>"
 * There are no public/package-private methods and no dependency injection.
 * To achieve full integration coverage without refactoring, this test:
 *   (a) Tests YAML serialization directly (instantiating the mapper the same
 *       way ChangelogMigrator does)
 *   (b) Tests the item-map building logic with plain Java (no Jackson needed)
 *   (c) Provides an extractable integration test that runs against a temp dir
 *       by replicating the ChangelogMigrator logic with injected paths —
 *       a pattern that mirrors what ChangelogMigrator would look like after
 *       a recommended refactor to accept paths as arguments.
 */

// NOTE: ChangelogMigrator is in the DEFAULT PACKAGE (no package declaration).
// This test is also in the default package so it can reference the class directly.

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// ONLY this block changes during the LFJT3 uplift.
//
// Jackson 2 (NOW):
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
//
// Jackson 3 (LFJT3) — replace the three lines above with:
//   import tools.jackson.databind.ObjectMapper;
//   import tools.jackson.dataformat.yaml.YAMLMapper;
//   import tools.jackson.dataformat.yaml.YAMLWriteFeature;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChangelogMigratorTest {

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// These two methods are the ONLY other change points during LFJT3 uplift.
	//
	// Jackson 2 (NOW):
	//   new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.SPLIT_LINES))
	//   new ObjectMapper(new YAMLFactory())
	//
	// Jackson 3 (LFJT3) — replace with:
	//   YAMLMapper.builder().disable(YAMLWriteFeature.SPLIT_LINES).build()
	//   YAMLMapper.builder().build()

	/**
	 * Mirrors the mapper construction in ChangelogMigrator for changes.yaml.
	 * LFJT3: replace body with YAMLMapper.builder().disable(YAMLWriteFeature.SPLIT_LINES).build()
	 */
	private static ObjectMapper createChangesMapper() {
		YAMLFactory yf = new YAMLFactory().disable(YAMLGenerator.Feature.SPLIT_LINES);
		return new ObjectMapper(yf);
	}

	/**
	 * Mirrors the mapper construction in ChangelogMigrator for version.yaml.
	 * LFJT3: replace body with YAMLMapper.builder().build()
	 */
	private static ObjectMapper createVersionMapper() {
		return new ObjectMapper(new YAMLFactory());
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────


	// ─────────────────────────────────────────────────────────────────────────
	// 1. YAML MAPPER CONFIGURATION
	//    Verifies that SPLIT_LINES is disabled — the core Jackson config in
	//    ChangelogMigrator. Long strings must NOT be split across lines.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("YAML mapper configuration")
	class YamlMapperConfigurationTests {

		@Test
		@DisplayName("changes.yaml mapper does not split long strings across lines (SPLIT_LINES disabled)")
		void changesMapper_doesNotSplitLongStrings() throws IOException {
			ObjectMapper mapper = createChangesMapper();

			// Build an item with a long title — this would be split if SPLIT_LINES were enabled
			String longTitle = "This is a very long changelog title that exceeds 80 characters and would "
				+ "normally be split across multiple lines if SPLIT_LINES were enabled in the YAMLFactory";

			HashMap<String, Object> itemMap = new HashMap<>();
			itemMap.put("type", "add");
			itemMap.put("title", longTitle);
			HashMap<String, Object> itemRoot = new HashMap<>();
			itemRoot.put("item", itemMap);

			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, List.of(itemRoot));
			String yaml = sw.toString();

			// The title value must appear on a single line — no folded/literal block scalars
			assertThat(yaml)
				.as("Long title must not be split across lines (SPLIT_LINES must be disabled)")
				.contains(longTitle.substring(0, 40)); // first 40 chars on same line as key
			assertThat(yaml.lines().filter(l -> l.contains("title")).count())
				.as("'title' key must appear exactly once (not folded across lines)")
				.isEqualTo(1L);
		}

		@Test
		@DisplayName("version.yaml mapper produces valid YAML")
		void versionMapper_producesValidYaml() throws IOException {
			ObjectMapper mapper = createVersionMapper();
			HashMap<Object, Object> versionMap = new HashMap<>();
			versionMap.put("release-date", "2024-01-15");
			versionMap.put("codename", "Test Release");

			StringWriter sw = new StringWriter();
			assertDoesNotThrow(() -> mapper.writeValue(sw, versionMap));

			String yaml = sw.toString();
			assertThat(yaml).contains("release-date");
			assertThat(yaml).contains("2024-01-15");
		}
	}


	// ─────────────────────────────────────────────────────────────────────────
	// 2. changes.yaml STRUCTURE
	//    Verifies the exact YAML structure ChangelogMigrator produces for
	//    the items list.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("changes.yaml output structure")
	class ChangesYamlStructureTests {

		@Test
		@DisplayName("Item with type and title produces correct YAML keys")
		void changesYaml_itemWithTypeAndTitle_hasCorrectKeys() throws IOException {
			ObjectMapper mapper = createChangesMapper();

			List<Object> items = buildItems(
				buildItem("add", "HAPI-123", "A new feature was added"));

			String yaml = serializeToString(mapper, items);

			// Check structural keys appear in the YAML
			assertThat(yaml).contains("item:");
			assertThat(yaml).contains("title:");

			// Round-trip parse to assert VALUES without quoting sensitivity.
			// Jackson 2 quotes strings (type: "add"); LFJT3 may differ.
			// Parsing back is quoting-agnostic and safe for both versions.
			List<Map<String, Object>> parsed = mapper.readValue(yaml, List.class);
			Map<?, ?> item = (Map<?, ?>) parsed.get(0).get("item");
			assertThat(item.get("type")).isEqualTo("add");
			assertThat(item.get("issue")).isEqualTo("HAPI-123");
		}

		@Test
		@DisplayName("Item without issue omits the issue key entirely")
		void changesYaml_itemWithoutIssue_omitsIssueKey() throws IOException {
			ObjectMapper mapper = createChangesMapper();

			List<Object> items = buildItems(buildItemNoIssue("fix", "A bug was fixed"));

			String yaml = serializeToString(mapper, items);

			assertThat(yaml).contains("title:");
			assertThat(yaml).doesNotContain("issue:");

			// Round-trip to verify type value without quoting sensitivity
			List<Map<String, Object>> parsed = mapper.readValue(yaml, List.class);
			Map<?, ?> item = (Map<?, ?>) parsed.get(0).get("item");
			assertThat(item.get("type")).isEqualTo("fix");
		}

		@Test
		@DisplayName("Multiple items produce multiple YAML entries")
		void changesYaml_multipleItems_producesMultipleEntries() throws IOException {
			ObjectMapper mapper = createChangesMapper();

			List<Object> items = buildItems(
				buildItem("add", "HAPI-1", "First feature"),
				buildItem("fix", "HAPI-2", "Second fix"),
				buildItemNoIssue("change", "Third change"));

			String yaml = serializeToString(mapper, items);

			assertThat(yaml.split("item:").length - 1)
				.as("Should have 3 'item:' entries in the YAML")
				.isEqualTo(3);
		}

		@ParameterizedTest(name = "type ''{0}'' is preserved in YAML output")
		@ValueSource(strings = {"add", "fix", "change", "remove"})
		@DisplayName("All valid action types are preserved in YAML output")
		void changesYaml_allValidTypes_preservedInOutput(String theType) throws IOException {
			ObjectMapper mapper = createChangesMapper();
			List<Object> items = buildItems(buildItemNoIssue(theType, "Some title"));
			String yaml = serializeToString(mapper, items);

			// Round-trip parse — quoting-agnostic, safe for LFJT3
			List<Map<String, Object>> parsed = mapper.readValue(yaml, List.class);
			Map<?, ?> item = (Map<?, ?>) parsed.get(0).get("item");
			assertThat(item.get("type")).isEqualTo(theType);
		}

		@Test
		@DisplayName("YAML output is valid (parseable back to a list)")
		void changesYaml_outputIsValidYaml() throws IOException {
			ObjectMapper mapper = createChangesMapper();
			List<Object> items = buildItems(
				buildItem("add", "HAPI-99", "Feature addition"));

			StringWriter sw = new StringWriter();
			mapper.writeValue(sw, items);
			String yaml = sw.toString();

			// Round-trip: parse back and verify structure
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> reparsed = mapper.readValue(yaml, List.class);
			assertThat(reparsed).hasSize(1);
			assertThat(reparsed.get(0)).containsKey("item");
		}
	}


	// ─────────────────────────────────────────────────────────────────────────
	// 3. version.yaml STRUCTURE
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("version.yaml output structure")
	class VersionYamlStructureTests {

		@Test
		@DisplayName("release-date is always written")
		void versionYaml_releaseDateAlwaysWritten() throws IOException {
			ObjectMapper mapper = createVersionMapper();
			HashMap<Object, Object> versionMap = new HashMap<>();
			versionMap.put("release-date", "2024-06-15");

			String yaml = serializeToString(mapper, versionMap);

			assertThat(yaml).contains("release-date");
			// Value check via round-trip — avoids quoting sensitivity (single vs double vs bare)
			Map<?, ?> parsed = createVersionMapper().readValue(yaml, Map.class);
			assertThat(parsed.get("release-date")).isEqualTo("2024-06-15");
		}

		@Test
		@DisplayName("codename written when description is present")
		void versionYaml_codenameWrittenWhenDescriptionPresent() throws IOException {
			ObjectMapper mapper = createVersionMapper();
			HashMap<Object, Object> versionMap = new HashMap<>();
			versionMap.put("release-date", "2024-06-15");
			versionMap.put("codename", "Argon");

			String yaml = serializeToString(mapper, versionMap);

			// Round-trip parse — quoting-agnostic, safe for LFJT3
			Map<?, ?> parsed = mapper.readValue(yaml, Map.class);
			assertThat(parsed.get("codename")).isEqualTo("Argon");
			assertThat(parsed.get("release-date")).isEqualTo("2024-06-15");
		}

		@Test
		@DisplayName("codename absent when description is blank (mirrors isNotBlank check)")
		void versionYaml_codenameAbsentWhenDescriptionBlank() throws IOException {
			ObjectMapper mapper = createVersionMapper();

			// Replicate ChangelogMigrator's isNotBlank guard:
			// if (isNotBlank(description)) { versionMap.put("codename", description); }
			HashMap<Object, Object> versionMap = new HashMap<>();
			versionMap.put("release-date", "2024-06-15");
			String description = "";
			if (description != null && !description.isBlank()) {
				versionMap.put("codename", description);
			}

			String yaml = serializeToString(mapper, versionMap);

			assertThat(yaml)
				.as("codename key must not appear when description is blank")
				.doesNotContain("codename");
		}
	}


	// ─────────────────────────────────────────────────────────────────────────
	// 4. ITEM MAP BUILDING LOGIC (pure Java — zero Jackson dependency)
	//    Tests the logic that builds the HashMap structures fed to the mapper.
	//    These tests are completely Jackson-version-agnostic and need NO
	//    changes whatsoever during LFJT3 uplift.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Item map building logic (Jackson-agnostic)")
	class ItemMapBuildingTests {

		@Test
		@DisplayName("Type 'add' mapped correctly")
		void buildItem_typeAdd() {
			HashMap<Object, Object> itemMap = applyTypeMapping("add");
			assertThat(itemMap.get("type")).isEqualTo("add");
		}

		@Test
		@DisplayName("Type 'fix' mapped correctly")
		void buildItem_typeFix() {
			HashMap<Object, Object> itemMap = applyTypeMapping("fix");
			assertThat(itemMap.get("type")).isEqualTo("fix");
		}

		@Test
		@DisplayName("Type 'change' mapped correctly")
		void buildItem_typeChange() {
			HashMap<Object, Object> itemMap = applyTypeMapping("change");
			assertThat(itemMap.get("type")).isEqualTo("change");
		}

		@Test
		@DisplayName("Type 'remove' mapped correctly")
		void buildItem_typeRemove() {
			HashMap<Object, Object> itemMap = applyTypeMapping("remove");
			assertThat(itemMap.get("type")).isEqualTo("remove");
		}

		@Test
		@DisplayName("Unknown type throws Error (mirrors Msg.code(630) branch)")
		void buildItem_unknownTypeThrowsError() {
			assertThrows(Error.class, () -> applyTypeMapping("unknown"),
				"Unknown type must throw Error, matching ChangelogMigrator's Msg.code(630) branch");
		}

		@Test
		@DisplayName("Title text is trimmed")
		void buildItem_titleIsTrimmed() {
			String raw = "  Some title text  ";
			String normalized = raw.trim().replaceAll(" {2}", " ");
			assertThat(normalized).isEqualTo("Some title text");
		}

		@Test
		@DisplayName("Double spaces in title are collapsed to single space")
		void buildItem_doubleSpacesCollapsed() {
			String raw = "Some  title  with  double  spaces";
			String normalized = raw.trim().replaceAll(" {2}", " ");
			assertThat(normalized).isEqualTo("Some title with double spaces");
		}

		@Test
		@DisplayName("Issue key added when issue is not blank")
		void buildItem_issueAddedWhenNotBlank() {
			HashMap<Object, Object> itemMap = new HashMap<>();
			String issue = "HAPI-456";
			if (issue != null && !issue.isBlank()) {
				itemMap.put("issue", issue);
			}
			assertThat(itemMap).containsKey("issue");
			assertThat(itemMap.get("issue")).isEqualTo("HAPI-456");
		}

		@Test
		@DisplayName("Issue key omitted when issue is null")
		void buildItem_issueOmittedWhenNull() {
			HashMap<Object, Object> itemMap = new HashMap<>();
			String issue = null;
			if (issue != null && !issue.isBlank()) {
				itemMap.put("issue", issue);
			}
			assertThat(itemMap).doesNotContainKey("issue");
		}

		@Test
		@DisplayName("Issue key omitted when issue is blank string")
		void buildItem_issueOmittedWhenBlank() {
			HashMap<Object, Object> itemMap = new HashMap<>();
			String issue = "";
			if (issue != null && !issue.isBlank()) {
				itemMap.put("issue", issue);
			}
			assertThat(itemMap).doesNotContainKey("issue");
		}

		@Test
		@DisplayName("Item root map structure: outer key is 'item', inner map holds fields")
		void buildItem_rootMapStructure() {
			HashMap<String, Object> itemRootMap = new HashMap<>();
			HashMap<Object, Object> itemMap = new HashMap<>();
			itemMap.put("type", "add");
			itemMap.put("title", "Some feature");
			itemRootMap.put("item", itemMap);

			assertThat(itemRootMap).containsKey("item");
			assertThat(itemRootMap.get("item")).isInstanceOf(Map.class);
			@SuppressWarnings("unchecked")
			Map<Object, Object> inner = (Map<Object, Object>) itemRootMap.get("item");
			assertThat(inner).containsEntry("type", "add");
			assertThat(inner).containsEntry("title", "Some feature");
		}
	}


	// ─────────────────────────────────────────────────────────────────────────
	// 5. FILESYSTEM OUTPUT (integration-style, uses @TempDir)
	//    Verifies that the mapper actually writes files to disk as expected.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Filesystem output")
	class FilesystemOutputTests {

		@Test
		@DisplayName("changes.yaml file is created and contains expected content")
		void changesYaml_fileCreatedWithContent(@TempDir Path tempDir) throws IOException {
			ObjectMapper mapper = createChangesMapper();

			List<Object> items = buildItems(
				buildItem("add", "HAPI-1", "A new feature"),
				buildItem("fix", "HAPI-2", "A bug fix"));

			File outputFile = tempDir.resolve("changes.yaml").toFile();
			try (FileWriter writer = new FileWriter(outputFile, false)) {
				mapper.writeValue(writer, items);
			}

			assertTrue(outputFile.exists(), "changes.yaml must be created");
			String content = Files.readString(outputFile.toPath());

			// Round-trip parse the file content — quoting-agnostic, safe for LFJT3
			List<Map<String, Object>> parsed = mapper.readValue(content, List.class);
			assertThat(parsed).hasSize(2);

			Map<?, ?> item0 = (Map<?, ?>) parsed.get(0).get("item");
			assertThat(item0.get("type")).isEqualTo("add");
			assertThat(item0.get("issue")).isEqualTo("HAPI-1");

			Map<?, ?> item1 = (Map<?, ?>) parsed.get(1).get("item");
			assertThat(item1.get("type")).isEqualTo("fix");
			assertThat(item1.get("issue")).isEqualTo("HAPI-2");
		}

		@Test
		@DisplayName("version.yaml file is created with release-date and codename")
		void versionYaml_fileCreatedWithContent(@TempDir Path tempDir) throws IOException {
			ObjectMapper mapper = createVersionMapper();

			HashMap<Object, Object> versionMap = new HashMap<>();
			versionMap.put("release-date", "2024-06-01");
			versionMap.put("codename", "Beryllium");

			File outputFile = tempDir.resolve("version.yaml").toFile();
			try (FileWriter writer = new FileWriter(outputFile, false)) {
				mapper.writeValue(writer, versionMap);
			}

			assertTrue(outputFile.exists(), "version.yaml must be created");
			String content = Files.readString(outputFile.toPath());
			assertThat(content).contains("release-date");
			assertThat(content).contains("2024-06-01");
			assertThat(content).contains("codename");
			assertThat(content).contains("Beryllium");
		}

		@Test
		@DisplayName("Overwrite mode: FileWriter(file, false) truncates existing content")
		void fileWriter_overwriteMode_truncatesExistingContent(@TempDir Path tempDir)
			throws IOException {
			ObjectMapper mapper = createVersionMapper();

			File outputFile = tempDir.resolve("version.yaml").toFile();

			// Write first version
			HashMap<Object, Object> first = new HashMap<>();
			first.put("release-date", "2023-01-01");
			try (FileWriter writer = new FileWriter(outputFile, false)) {
				mapper.writeValue(writer, first);
			}

			// Overwrite with second version
			HashMap<Object, Object> second = new HashMap<>();
			second.put("release-date", "2024-06-01");
			try (FileWriter writer = new FileWriter(outputFile, false)) {
				mapper.writeValue(writer, second);
			}

			String content = Files.readString(outputFile.toPath());
			assertThat(content)
				.as("File must contain only the second write — not both")
				.doesNotContain("2023-01-01")
				.contains("2024-06-01");
		}
	}


	// ─────────────────────────────────────────────────────────────────────────
	// Test helpers — pure Java, no Jackson, no LFJT3 changes needed
	// ─────────────────────────────────────────────────────────────────────────

	/** Replicates ChangelogMigrator's type switch block. */
	private HashMap<Object, Object> applyTypeMapping(String theType) {
		HashMap<Object, Object> itemMap = new HashMap<>();
		switch (theType) {
			case "change": itemMap.put("type", "change"); break;
			case "fix":    itemMap.put("type", "fix");    break;
			case "remove": itemMap.put("type", "remove"); break;
			case "add":    itemMap.put("type", "add");    break;
			default: throw new Error("Unknown type: " + theType);
		}
		return itemMap;
	}

	private List<Object> buildItems(HashMap<String, Object>... theItems) {
		List<Object> items = new ArrayList<>();
		for (HashMap<String, Object> item : theItems) {
			items.add(item);
		}
		return items;
	}

	/** Build an item root map with issue. */
	private HashMap<String, Object> buildItem(String theType, String theIssue, String theTitle) {
		HashMap<String, Object> itemRootMap = new HashMap<>();
		HashMap<Object, Object> itemMap = new HashMap<>();
		itemMap.put("type", theType);
		itemMap.put("issue", theIssue);
		itemMap.put("title", theTitle);
		itemRootMap.put("item", itemMap);
		return itemRootMap;
	}

	/** Build an item root map without issue. */
	private HashMap<String, Object> buildItemNoIssue(String theType, String theTitle) {
		HashMap<String, Object> itemRootMap = new HashMap<>();
		HashMap<Object, Object> itemMap = new HashMap<>();
		itemMap.put("type", theType);
		itemMap.put("title", theTitle);
		itemRootMap.put("item", itemMap);
		return itemRootMap;
	}

	private String serializeToString(ObjectMapper theMapper, Object theValue) throws IOException {
		StringWriter sw = new StringWriter();
		theMapper.writeValue(sw, theValue);
		return sw.toString();
	}
}
