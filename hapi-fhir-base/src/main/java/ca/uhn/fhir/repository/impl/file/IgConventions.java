package ca.uhn.fhir.repository.impl.file;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * This class represents the different file structures for an IG repository. The main differences
 * between the various configurations are whether or not the files are organized by resource type
 * and/or category, and whether or not the files are prefixed with the resource type.
 */
public record IgConventions(
	IgConventions.FhirTypeLayout typeLayout,
	IgConventions.CategoryLayout categoryLayout,
	IgConventions.CompartmentLayout compartmentLayout,
	IgConventions.FilenameMode filenameMode) {

	private static final Logger logger = LoggerFactory.getLogger(IgConventions.class);

	/**
	 * Whether or not the files are organized by resource type.
	 */
	public enum FhirTypeLayout {
		DIRECTORY_PER_TYPE,
		FLAT
	}

	/**
	 * Whether or not the resources are organized by category (tests, resources, vocabulary)
	 */
	public enum CategoryLayout {
		DIRECTORY_PER_CATEGORY,
		FLAT
	}

	/**
	 * Whether or not the files are organized by compartment. This is primarily used for tests to
	 * provide isolation between test cases.
	 */
	public enum CompartmentLayout {
		DIRECTORY_PER_COMPARTMENT,
		FLAT
	}

	/**
	 * Whether or not the files are prefixed with the resource type.
	 */
	public enum FilenameMode {
		TYPE_AND_ID,
		ID_ONLY
	}

	public static final IgConventions FLAT = new IgConventions(
		FhirTypeLayout.FLAT, CategoryLayout.FLAT, CompartmentLayout.FLAT, FilenameMode.TYPE_AND_ID);
	public static final IgConventions STANDARD = new IgConventions(
		FhirTypeLayout.DIRECTORY_PER_TYPE,
		CategoryLayout.DIRECTORY_PER_CATEGORY,
		CompartmentLayout.FLAT,
		FilenameMode.ID_ONLY);

	private static final List<String> FHIR_TYPE_NAMES = Stream.of(FHIRAllTypes.values())
		.map(FHIRAllTypes::name)
		.map(String::toLowerCase)
		.distinct()
		.toList();

	/**
	 * Auto-detect the IG conventions based on the structure of the IG. If the path is null or the
	 * convention can not be reliably detected, the default configuration is returned.
	 *
	 * @param path The path to the IG.
	 * @return The IG conventions.
	 */
	public static IgConventions autoDetect(Path path) {

		if (path == null || !java.nio.file.Files.exists(path)) {
			return STANDARD;
		}

		// A "category" hierarchy may exist in the ig file structure,
		// where resource categories ("data", "terminology", "content") are organized into
		// subdirectories ("tests", "vocabulary", "resources").
		//
		// e.g. "input/tests", "input/vocabulary".
		//
		// Check all possible category paths and grab the first that exists,
		// or use the IG path if none exist.

		var categoryPath = Stream.of("tests", "vocabulary", "resources")
			.map(path::resolve)
			.filter(x -> x.toFile().exists())
			.findFirst()
			.orElse(path);

		var hasCategoryDirectory = !path.equals(categoryPath);

		var hasCompartmentDirectory = false;

		// Compartments can only exist for test data
		if (hasCategoryDirectory) {
			var tests = path.resolve("tests");
			// A compartment under the tests looks like a set of subdirectories
			// e.g. "input/tests/Patient", "input/tests/Practitioner"
			// that themselves contain subdirectories for each test case.
			// e.g. "input/tests/Patient/test1", "input/tests/Patient/test2"
			// Then within those, the structure may be flat (e.g. "input/tests/Patient/test1/123.json")
			// or grouped by type (e.g. "input/tests/Patient/test1/Patient/123.json").
			//
			// The trick is that the in the case that the test cases are
			// grouped by type, the compartment directory will be the same as the type directory.
			// so we need to look at the resource type directory and check if the contents are files
			// or more directories. If more directories exist, and the directory name is not a
			// FHIR type, then we have a compartment directory.

			if (tests.toFile().exists()) {
				var compartments = FHIR_TYPE_NAMES.stream().map(tests::resolve).filter(x -> x.toFile()
					.exists());

				final List<Path> compartmentsList = compartments.toList();

				// Check if any of the potential compartment directories
				// have subdirectories that are not FHIR types (e.g. "input/tests/Patient/test1).
				hasCompartmentDirectory = compartmentsList.stream()
					.flatMap(IgConventions::listFiles)
					.filter(java.nio.file.Files::isDirectory)
					.anyMatch(IgConventions::matchesAnyResource);
			}
		}

		// A "type" may also exist in the igs file structure, where resources
		// are grouped by type into subdirectories.
		//
		// e.g. "input/vocabulary/valueset", "input/resources/valueset".
		//
		// Check all possible type paths and grab the first that exists,
		// or use the category directory if none exist
		var typePath = FHIR_TYPE_NAMES.stream()
			.map(categoryPath::resolve)
			.filter(java.nio.file.Files::exists)
			.findFirst()
			.orElse(categoryPath);

		var hasTypeDirectory = !categoryPath.equals(typePath);

		// A file "claims" to be a FHIR resource type if its filename starts with a valid FHIR type name.
		// For files that "claim" to be a FHIR resource type, we check to see if the contents of the file
		// have a resource that matches the claimed type.
		var hasTypeFilename = hasTypeFilename(typePath);

		var config = new IgConventions(
			hasTypeDirectory ? FhirTypeLayout.DIRECTORY_PER_TYPE : FhirTypeLayout.FLAT,
			hasCategoryDirectory ? CategoryLayout.DIRECTORY_PER_CATEGORY : CategoryLayout.FLAT,
			hasCompartmentDirectory ? CompartmentLayout.DIRECTORY_PER_COMPARTMENT : CompartmentLayout.FLAT,
			hasTypeFilename ? FilenameMode.TYPE_AND_ID : FilenameMode.ID_ONLY);

		logger.info("Auto-detected repository configuration: {}", config);

		return config;
	}

	private static boolean hasTypeFilename(Path typePath) {
		try (var fileStream = java.nio.file.Files.list(typePath)) {
			return fileStream
				.filter(IgConventions::fileNameMatchesType)
				.filter(filePath -> claimedFhirType(filePath) != FHIRAllTypes.NULL)
				.anyMatch(filePath -> contentsMatchClaimedType(filePath, claimedFhirType(filePath)));
		} catch (IOException exception) {
			logger.error("Error listing files in path: {}", typePath, exception);
			return false;
		}
	}

	private static boolean fileNameMatchesType(Path innerFile) {
		Objects.requireNonNull(innerFile);
		var fileName = innerFile.getFileName().toString();
		return FHIR_TYPE_NAMES.stream().anyMatch(type -> fileName.toLowerCase().startsWith(type));
	}

	private static boolean matchesAnyResource(Path innerFile) {
		return !FHIR_TYPE_NAMES.contains(innerFile.getFileName().toString().toLowerCase());
	}

	@Nonnull
	private static Stream<Path> listFiles(Path innerPath) {
		try {
			return java.nio.file.Files.list(innerPath);
		} catch (IOException e) {
			logger.error("Error listing files in path: {}", innerPath, e);
			return Stream.empty();
		}
	}

	// This method checks to see if the contents of a file match the type claimed by the filename
	private static boolean contentsMatchClaimedType(Path filePath, FHIRAllTypes claimedFhirType) {
		Objects.requireNonNull(filePath);
		Objects.requireNonNull(claimedFhirType);

		try (var linesStream = Files.lines(filePath, StandardCharsets.UTF_8)) {
			var contents = linesStream.collect(Collectors.joining());
			if (contents.isEmpty()) {
				return false;
			}

			var filename = filePath.getFileName().toString();
			var fileNameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
			// Check that the contents contain the claimed type, and that the id is not the same as the filename
			// NOTE: This does not work for XML files.
			return contents.toUpperCase().contains("\"RESOURCETYPE\": \"%s\"".formatted(claimedFhirType.name()))
				&& !contents.toUpperCase()
				.contains("\"ID\": \"%s\"".formatted(fileNameWithoutExtension.toUpperCase()));

		} catch (IOException e) {
			return false;
		}
	}

	// Detects the FHIR type claimed by the filename
	private static FHIRAllTypes claimedFhirType(Path filePath) {
		var filename = filePath.getFileName().toString();
		if (!filename.contains("-")) {
			return FHIRAllTypes.NULL;
		}

		var codeName = filename.substring(0, filename.indexOf("-")).toUpperCase();
		try {
			return FHIRAllTypes.valueOf(codeName);
		} catch (Exception e) {
			return FHIRAllTypes.NULL;
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		IgConventions that = (IgConventions) other;
		return typeLayout == that.typeLayout
			&& filenameMode == that.filenameMode
			&& categoryLayout == that.categoryLayout
			&& compartmentLayout == that.compartmentLayout;
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeLayout, categoryLayout, compartmentLayout, filenameMode);
	}

	@Override
	@Nonnull
	public String toString() {
		return "IGConventions [typeLayout=%s, categoryLayout=%s compartmentLayout=%s, filenameMode=%s]"
			.formatted(typeLayout, categoryLayout, compartmentLayout, filenameMode);
	}
}
