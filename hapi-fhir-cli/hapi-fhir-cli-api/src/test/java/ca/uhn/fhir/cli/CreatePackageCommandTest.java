package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.BaseTest;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatePackageCommandTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(CreatePackageCommandTest.class);

	static {
		System.setProperty("test", "true");
	}

	private File myWorkDirectory;
	private FhirContext myContext = FhirContext.forR4();
	private File myTargetDirectory;
	private File myExtractDirectory;

	@BeforeEach
	public void start() {
		myWorkDirectory = Files.createTempDir();
		myTargetDirectory = Files.createTempDir();
		myExtractDirectory = Files.createTempDir();
	}

	@AfterEach
	public void stop() {
		try {
			FileUtils.deleteDirectory(myWorkDirectory);
			FileUtils.deleteDirectory(myTargetDirectory);
			FileUtils.deleteDirectory(myExtractDirectory);
		} catch (IOException e) {
			throw new InternalErrorException("Failed to delete temporary directory \"" + myWorkDirectory.getAbsolutePath() + "\"", e);
		}
	}

	@Test
	public void testCreatePackage() throws IOException {

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo/1");
		writeFile(sd, "foo1.json");

		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/2");
		writeFile(vs, "foo2.json");

		App.main(new String[]{
			"create-package",
			"--fhir-version", "R4",
			"--name", "com.example.ig",
			"--version", "1.0.1",
			"--include-package", myWorkDirectory.getAbsolutePath() + "/*.json",
			"--target-directory", myTargetDirectory.getAbsolutePath(),
			"--dependency", "hl7.fhir.core:4.0.1",
			"--dependency", "foo.bar:1.2.3"
		});

		Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");

		File igArchive = new File(myTargetDirectory, "com.example.ig-1.0.1.tgz");
		archiver.extract(igArchive, myExtractDirectory);

		List<String> allFiles = FileUtils.listFiles(myExtractDirectory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)
			.stream()
			.map(t -> t.getPath())
			.sorted()
			.collect(Collectors.toList());
		ourLog.info("Archive contains files:\n * {}", allFiles.stream().collect(Collectors.joining("\n * ")));

		// Verify package.json
		String packageJsonContents = IOUtils.toString(new FileInputStream(new File(myExtractDirectory, "package/package.json")), Charsets.UTF_8);
		ourLog.info("Package.json:\n{}", packageJsonContents);

		String expectedPackageJson = "{\n" +
			"  \"name\": \"com.example.ig\",\n" +
			"  \"version\": \"1.0.1\",\n" +
			"  \"fhirVersions\": [\n" +
			"    \"4.0.1\"\n" +
			"  ],\n" +
			"  \"dependencies\": {\n" +
			"    \"hl7.fhir.core\": \"4.0.1\",\n" +
			"    \"foo.bar\": \"1.2.3\"\n" +
			"  }\n" +
			"}";
		assertEquals(expectedPackageJson, packageJsonContents);

		// Try parsing the module again to make sure we can
		NpmPackage loadedPackage = NpmPackage.fromPackage(new FileInputStream(igArchive));
		assertEquals("com.example.ig", loadedPackage.name());
	}

	@Test
	public void testCreatePackage_NoDependencies() throws IOException {

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo/1");
		writeFile(sd, "foo1.json");

		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/2");
		writeFile(vs, "foo2.json");

		App.main(new String[]{
			"create-package",
			"--fhir-version", "R4",
			"--name", "com.example.ig",
			"--version", "1.0.1",
			"--include-package", myWorkDirectory.getAbsolutePath() + "/*.json",
			"--target-directory", myTargetDirectory.getAbsolutePath()
		});

		Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");

		File igArchive = new File(myTargetDirectory, "com.example.ig-1.0.1.tgz");
		archiver.extract(igArchive, myExtractDirectory);

		List<String> allFiles = FileUtils.listFiles(myExtractDirectory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)
			.stream()
			.map(t -> t.getPath())
			.sorted()
			.collect(Collectors.toList());
		ourLog.info("Archive contains files:\n * {}", allFiles.stream().collect(Collectors.joining("\n * ")));

		// Verify package.json
		String packageJsonContents = IOUtils.toString(new FileInputStream(new File(myExtractDirectory, "package/package.json")), Charsets.UTF_8);
		ourLog.info("Package.json:\n{}", packageJsonContents);

		String expectedPackageJson = "{\n" +
			"  \"name\": \"com.example.ig\",\n" +
			"  \"version\": \"1.0.1\",\n" +
			"  \"fhirVersions\": [\n" +
			"    \"4.0.1\"\n" +
			"  ]\n" +
			"}";
		assertEquals(expectedPackageJson, packageJsonContents);

		// Try parsing the module again to make sure we can
		NpmPackage loadedPackage = NpmPackage.fromPackage(new FileInputStream(igArchive));
		assertEquals("com.example.ig", loadedPackage.name());

	}

	public void writeFile(IBaseResource theResource, String theFileName) throws IOException {
		try (FileWriter w = new FileWriter(new File(myWorkDirectory, theFileName), false)) {
			myContext.newJsonParser().encodeResourceToWriter(theResource, w);
		}
	}

}
